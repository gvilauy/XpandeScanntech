package org.xpande.stech.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;
import org.xpande.financial.utils.FinancialUtils;
import org.xpande.stech.model.MZStechInterfaceVta;
import org.xpande.stech.model.MZStechVtaCtaCte;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Proceso para corregir y cargar la información de eFacturas, eFacturasNC y eFActurasND que se generaron
 * en una interface de venta del POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/10/20.
 */
public class FixInterfaceVtaComp extends SvrProcess {

    private Timestamp startDate = null;
    private Timestamp endDate = null;
    private int adOrgID = 0;

    @Override
    protected void prepare() {

        ProcessInfoParameter[] para = getParameter();

        for (int i = 0; i < para.length; i++){

            String name = para[i].getParameterName();

            if (name != null){
                if (para[i].getParameter() != null){
                    if (name.trim().equalsIgnoreCase("DateTrx")){
                        this.startDate = (Timestamp)para[i].getParameter();
                        this.endDate = (Timestamp)para[i].getParameter_To();
                    }
                    else if (name.trim().equalsIgnoreCase("AD_Org_ID")){
                        this.adOrgID = ((BigDecimal)para[i].getParameter()).intValueExact();
                    }
                }
            }
        }

    }

    @Override
    protected String doIt() throws Exception {

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        start.setTime(this.startDate);
        end.setTime(this.endDate);

        // Loop recorriendo rango de fechas ingresadas
        while( !start.after(end)){

            Timestamp fechaProceso = new Timestamp(start.getTime().getTime());

            String message = this.setVentasComprobantes(fechaProceso);

            if (message != null){
                return "@Error@ " + message;
            }

            start.add(Calendar.DATE, 1);
        }

        return "OK";
    }

    /***
     * Obtengo información de comprobantes de venta: eFactura, eFacturaNC y eFacturaND desde POS.
     * No se generan invoices en el sistema. Esto es solo para luego poder utilizar este info en
     * la generación de formularios para DGI (Ej: 2/181)
     * Xpande. Created by Gabriel Vila on 7/10/20.
     * @param fechaProceso
     */
    private String setVentasComprobantes(Timestamp fechaProceso) {

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String message = null;

        try{

            // Obtengo modelo de interface de venta según organización y fecha recibidos por parametro.
            MZStechInterfaceVta interfaceVta = MZStechInterfaceVta.getByOrgDate(getCtx(), this.adOrgID, fechaProceso, get_TrxName());
            if ((interfaceVta == null) || (interfaceVta.get_ID() <= 0)){
                return "No se pudo obtener Interface de Venta para Organización y Fecha indicados";
            }

            sql = " select mov.sc_tipocfe, mov.sc_rucfactura, mov.sc_seriecfe, mov.sc_numerooperacion, " +
                    " mov.SC_CodigoMoneda, mov.SC_CodigoCaja, mov.Z_Stech_TK_Mov_ID, mov.sc_numeromov, " +
                    " mov.sc_total as sc_importe " +
                    " from z_stech_tk_mov mov " +
                    " where mov.ad_org_id =" + this.adOrgID +
                    " and mov.sc_tipooperacion='VENTA' " +
                    " and mov.z_stechinterfacevta_id =" + interfaceVta.get_ID() +
                    " and mov.sc_tipocfe in (111, 112, 113) " +
                    " order by 1,2,3,4 ";

            pstmt = DB.prepareStatement(sql, get_TrxName());
            rs = pstmt.executeQuery();

            while(rs.next()){

                int cCurrencyID = 142;
                if ((rs.getString("SC_CodigoMoneda") != null) && (!rs.getString("SC_CodigoMoneda").equalsIgnoreCase("858"))){
                    cCurrencyID = 100;
                }

                MZStechVtaCtaCte stechVtaCtaCte = new MZStechVtaCtaCte(getCtx(), 0, get_TrxName());
                stechVtaCtaCte.setZ_StechInterfaceVta_ID(interfaceVta.get_ID());
                stechVtaCtaCte.setZ_Stech_TK_Mov_ID(rs.getInt("Z_Stech_TK_Mov_ID"));
                stechVtaCtaCte.setAD_Org_ID(this.adOrgID);
                stechVtaCtaCte.set_ValueOfColumn("AD_Client_ID", interfaceVta.getAD_Client_ID());
                stechVtaCtaCte.setDateTrx(interfaceVta.getDateTrx());
                stechVtaCtaCte.setSC_TipoCfe(rs.getInt("sc_tipocfe"));
                stechVtaCtaCte.setSC_RucFactura(rs.getString("sc_rucfactura"));
                stechVtaCtaCte.setSC_SerieCfe(rs.getString("sc_seriecfe"));
                stechVtaCtaCte.setSC_NumeroOperacion(rs.getString("sc_numerooperacion"));
                stechVtaCtaCte.setSC_Importe(rs.getBigDecimal("sc_importe"));
                stechVtaCtaCte.setSC_CodigoCaja(rs.getInt("SC_CodigoCaja"));
                stechVtaCtaCte.setC_Currency_ID(cCurrencyID);
                stechVtaCtaCte.setSC_NumeroMov(rs.getString("sc_numeromov"));
                stechVtaCtaCte.setIsExecuted(true);
                stechVtaCtaCte.setEsVentaEmpresa(true);

                BigDecimal amtTotal = rs.getBigDecimal("sc_importe");
                if (amtTotal == null) amtTotal = Env.ZERO;
                if (amtTotal.compareTo(Env.ZERO) == 0){
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("Venta con importe CERO");
                    stechVtaCtaCte.saveEx();
                    continue;
                }
                if (amtTotal.compareTo(Env.ZERO) < 0){
                    amtTotal = amtTotal.negate();
                }

                // Determino tipo de documento segun tipo cfe obtenido
                String tipoCFE = String.valueOf(rs.getInt("sc_tipocfe"));
                String descCFE ="", numeroComprobante= "";

                // e-ticket o e-factura
                if ((tipoCFE.equalsIgnoreCase("101")) || (tipoCFE.equalsIgnoreCase("111"))){

                    if (tipoCFE.equalsIgnoreCase("101")){
                        descCFE ="E-Ticket";
                    }
                    else {
                        descCFE ="E-Factura";
                    }
                }
                // e-ticket nc o e-factura nc
                else if ((tipoCFE.equalsIgnoreCase("102")) || (tipoCFE.equalsIgnoreCase("112"))){
                    if (tipoCFE.equalsIgnoreCase("102")){
                        descCFE ="E-Ticket Nota de Crédito";
                    }
                    else {
                        descCFE ="E-Factura Nota de Crédito";
                    }
                }
                // e-ticket nd o e-factura nd
                else if ((tipoCFE.equalsIgnoreCase("103")) || (tipoCFE.equalsIgnoreCase("113"))){
                    if (tipoCFE.equalsIgnoreCase("103")){
                        descCFE ="E-Ticket Nota de Débito";
                    }
                    else {
                        descCFE ="E-Factura Nota de Débito";
                    }
                }

                int cBParnterID = 0;
                String whereClause = "";

                String taxID = rs.getString("sc_rucfactura");

                if ((taxID != null) && (!taxID.trim().equalsIgnoreCase(""))){
                    whereClause = " c_bpartner.taxID ='" + taxID + "'";
                }
                else {
                    // No tengo identificador de socio de negocio, no hago nada.
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("Venta sin Número de Identificación asociado");
                    stechVtaCtaCte.saveEx();
                    continue;
                }

                int[] partnersIDs = MBPartner.getAllIDs(I_C_BPartner.Table_Name, whereClause, null);
                if (partnersIDs.length <= 0){

                    // No tengo socio de negocio en ADempiere con el numero de identificación recibido.
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("No se encontró un Socio de Negocio para el nro. de identificación : " + stechVtaCtaCte.getSC_RucFactura());
                    stechVtaCtaCte.saveEx();
                    continue;
                }
                cBParnterID = partnersIDs[0];
                MBPartner partner = new MBPartner(getCtx(), cBParnterID, null);

                MBPartnerLocation[] partnerLocations = partner.getLocations(true);
                if (partnerLocations.length <= 0){
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("Socio de Negocio no tiene Localización configurada");
                    stechVtaCtaCte.saveEx();
                    continue;
                }
                MBPartnerLocation partnerLocation = partnerLocations[0];

                MPaymentTerm paymentTerm = FinancialUtils.getPaymentTermByDefault(getCtx(), null);
                if ((paymentTerm == null) || (paymentTerm.get_ID() <= 0)){
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("No se pudo obtener Término de Pago por defecto.");
                    stechVtaCtaCte.saveEx();
                    continue;
                }

                stechVtaCtaCte.setC_BPartner_ID(partner.get_ID());
                stechVtaCtaCte.setC_BPartner_Location_ID(partnerLocation.get_ID());
                stechVtaCtaCte.saveEx();
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
            rs = null; pstmt = null;
        }

        return message;
    }

}
