package org.xpande.stech.utils;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.acct.Doc;
import org.compiere.model.*;
import org.compiere.process.DocAction;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.xpande.core.utils.PriceListUtils;
import org.xpande.financial.utils.FinancialUtils;
import org.xpande.stech.model.MZScanntechConfig;
import org.xpande.stech.model.MZStechVtaCtaCte;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/19/20.
 */
public class FixStech extends SvrProcess {

    @Override
    protected void prepare() {
    }

    @Override
    protected String doIt() throws Exception {

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            MZScanntechConfig scanntechConfig = MZScanntechConfig.getDefault(getCtx(), null);

            MProduct product = new MProduct(getCtx(), scanntechConfig.getProdVtasCredPOS_ID(), null);
            if ((product == null) || (product.get_ID() <= 0)){
                return null;
            }

            // Impuesto asociado al producto
            MTaxCategory taxCategory = (MTaxCategory) product.getC_TaxCategory();
            if ((taxCategory == null) || (taxCategory.get_ID() <= 0)){
                return null;
            }

            // Obtengo impuesto asociado al producto para este tipo de documentos
            MTax taxProduct = taxCategory.getDefaultTax();
            if ((taxProduct == null) || (taxProduct.get_ID() <= 0)){
                return null;
            }

            sql = " select mov.sc_tipocfe, mov.sc_rucfactura, mov.sc_seriecfe, mov.sc_numerooperacion, " +
                    " mov.SC_CodigoMoneda, mov.SC_CodigoCaja, mov.Z_Stech_Tk_Mov_ID, mov.sc_numeromov, " +
                    " sum(coalesce(a.sc_importe,0) + coalesce(a.sc_descuentoafam,0) + coalesce(a.sc_descuentoincfin,0)) as sc_importe " +
                    " from z_stech_tk_movpago a " +
                    " inner join z_stech_tk_mov mov on a.z_stech_tk_mov_id = mov.z_stech_tk_mov_id " +
                    " inner join z_stechcreditos mp on a.z_stechcreditos_id = mp.z_stechcreditos_id " +
                    " where mov.ad_org_id = 1000007 " +
                    " and mov.sc_tipooperacion='VENTA' " +
                    " and mov.z_stechinterfacevta_id =1002520 " +
                    " and mov.Z_Stech_Tk_Mov_ID =3516460 " +
                    " and mp.ventacredito ='Y' " +
                    " group by 1,2,3,4,5,6,7,8 ";

            pstmt = DB.prepareStatement(sql, get_TrxName());
            rs = pstmt.executeQuery();

            while(rs.next()){

                int cCurrencyID = 142;
                if ((rs.getString("SC_CodigoMoneda") != null) && (!rs.getString("SC_CodigoMoneda").equalsIgnoreCase("858"))){
                    cCurrencyID = 100;
                }

                BigDecimal amtTotal = rs.getBigDecimal("sc_importe");
                if (amtTotal == null) amtTotal = Env.ZERO;
                if (amtTotal.compareTo(Env.ZERO) < 0){
                    amtTotal = amtTotal.negate();
                }

                // Determino tipo de documento segun tipo cfe obtenido
                int cDocTypeID = 0;
                String tipoCFE = String.valueOf(rs.getInt("sc_tipocfe"));
                String descCFE ="", numeroComprobante= "";

                // e-ticket o e-factura
                if ((tipoCFE.equalsIgnoreCase("101")) || (tipoCFE.equalsIgnoreCase("111"))){
                    cDocTypeID = scanntechConfig.getDefaultDocPosARI_ID();

                    if (tipoCFE.equalsIgnoreCase("101")){
                        descCFE ="E-Ticket";
                    }
                    else {
                        descCFE ="E-Factura";
                    }
                }
                MDocType docType = new MDocType(getCtx(), cDocTypeID, null);

                int cBParnterID = 0;
                String whereClause = "";

                String taxID = rs.getString("sc_rucfactura");

                if ((taxID != null) && (!taxID.trim().equalsIgnoreCase(""))){
                    whereClause = " c_bpartner.taxID ='" + taxID + "'";
                }
                else {
                    // No tengo identificador de socio de negocio, no hago nada.
                    continue;
                }

                int[] partnersIDs = MBPartner.getAllIDs(I_C_BPartner.Table_Name, whereClause, null);
                if (partnersIDs.length <= 0){

                    // No tengo socio de negocio en ADempiere con el numero de identificación recibido.
                    continue;
                }
                cBParnterID = partnersIDs[0];
                MBPartner partner = new MBPartner(getCtx(), cBParnterID, null);

                MBPartnerLocation[] partnerLocations = partner.getLocations(true);
                if (partnerLocations.length <= 0){
                    continue;
                }
                MBPartnerLocation partnerLocation = partnerLocations[0];

                MPaymentTerm paymentTerm = FinancialUtils.getPaymentTermByDefault(getCtx(), null);
                if ((paymentTerm == null) || (paymentTerm.get_ID() <= 0)){
                    continue;
                }

                numeroComprobante = rs.getString("sc_seriecfe") + rs.getString("sc_numerooperacion");
                numeroComprobante = numeroComprobante.trim();

                MInvoice invoice = new MInvoice(getCtx(), 0, get_TrxName());
                invoice.set_ValueOfColumn("AD_Client_ID", scanntechConfig.getAD_Client_ID());
                invoice.setAD_Org_ID(1000007);
                invoice.setIsSOTrx(true);
                invoice.setC_DocTypeTarget_ID(cDocTypeID);
                invoice.setC_DocType_ID(cDocTypeID);
                invoice.setDocumentNo("PP-" +numeroComprobante);
                invoice.setDescription("Generado desde POS. Datos CFE : " + descCFE + " " + numeroComprobante +
                        " - Ticket: " + rs.getString("sc_numeromov"));

                if (docType.getDocBaseType().equalsIgnoreCase(Doc.DOCTYPE_ARCredit)){
                    invoice.set_ValueOfColumn("ReferenciaCFE", "Referencia Comprobante POS");
                }

                Timestamp fechaDoc = TimeUtil.trunc(new Timestamp(System.currentTimeMillis()), TimeUtil.TRUNC_DAY);
                invoice.setDateInvoiced(fechaDoc);
                invoice.setDateAcct(fechaDoc);
                invoice.setC_BPartner_ID(partner.get_ID());
                invoice.setC_BPartner_Location_ID(partnerLocation.get_ID());
                invoice.setC_Currency_ID(cCurrencyID);
                invoice.setPaymentRule(X_C_Invoice.PAYMENTRULE_OnCredit);
                invoice.setC_PaymentTerm_ID(paymentTerm.get_ID());

                MPriceList priceList = PriceListUtils.getPriceListByOrg(getCtx(), invoice.getAD_Client_ID(), invoice.getAD_Org_ID(),
                        invoice.getC_Currency_ID(), true, null, null);
                if ((priceList == null) || (priceList.get_ID() <= 0)){
                    continue;
                }

                invoice.setM_PriceList_ID(priceList.get_ID());
                invoice.setIsTaxIncluded(priceList.isTaxIncluded());
                invoice.set_ValueOfColumn("DocBaseType", docType.getDocBaseType());
                invoice.set_ValueOfColumn("EstadoAprobacion", "APROBADO");
                invoice.set_ValueOfColumn("TipoFormaPago", "CREDITO");
                invoice.set_ValueOfColumn("AmtSubtotal", amtTotal);
                invoice.setTotalLines(amtTotal);
                invoice.setGrandTotal(amtTotal);
                invoice.saveEx();

                // Linea de Factura
                MInvoiceLine line = new MInvoiceLine(invoice);
                line.set_ValueOfColumn("AD_Client_ID", invoice.getAD_Client_ID());
                line.set_ValueOfColumn("IsBySelection", true);
                line.setAD_Org_ID(invoice.getAD_Org_ID());
                line.setM_Product_ID(product.get_ID());
                line.setC_UOM_ID(product.getC_UOM_ID());
                line.setQtyEntered(Env.ONE);
                line.setQtyInvoiced(Env.ONE);
                line.setPriceEntered(amtTotal);
                line.setPriceActual(amtTotal);
                line.setLineNetAmt(amtTotal);
                line.setC_Tax_ID(taxProduct.get_ID());
                line.setTaxAmt();
                line.setLineNetAmt();
                line.set_ValueOfColumn("AmtSubTotal", invoice.getTotalLines());
                line.saveEx();

                if (!invoice.processIt(DocAction.ACTION_Complete)){
                    String message = "";
                    if (invoice.getProcessMsg() != null) message = invoice.getProcessMsg();
                    System.out.println("No se pudo completar Invoice en Venta Crédito Scanntech : " + message);
                }
                else{
                    invoice.saveEx();
                }
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
            rs = null; pstmt = null;
        }

        return "OK";
    }
}
