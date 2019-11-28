package org.xpande.stech.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.xpande.stech.model.MZStechTKMov;
import org.xpande.stech.model.MZStechTKMovPago;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 11/27/19.
 */
public class FixGenericScanntech extends SvrProcess {

    @Override
    protected void prepare() {

    }

    @Override
    protected String doIt() throws Exception {

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            sql = " select distinct z_stech_tk_mov_id from z_stech_tk_movpago where datetrx between '2019-11-01 00:00:00' and '2019-11-27 00:00:00' and sc_codigotipopago ='12' and ad_org_id = 1000000" +
                    "order by z_stech_tk_mov_id ";

        	pstmt = DB.prepareStatement(sql, get_TrxName());
        	rs = pstmt.executeQuery();

        	while(rs.next()){

                MZStechTKMov tkMov = new MZStechTKMov(getCtx(), rs.getInt("z_stech_tk_mov_id"), get_TrxName());

                this.setInfoVales(tkMov);

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

    /***
     * En scanntech hay medios de pago del tipo VALE que dentro del mismo nro. de movimiento se asocian a otro medio de pago.
     * Aquí se copian los datos del medio de pago asociado, al registro del VALE.
     * Xpande. Created by Gabriel Vila on 6/3/19.
     * @param tkMov
     */
    private void setInfoVales(MZStechTKMov tkMov) {

        String sql = "";

        try{

            String codigoMedioPagoVale = "12";
            String codigoMedioPagoEfectivo ="9";

            List<MZStechTKMovPago> tkMovPagoList = tkMov.getMediosPagoByCodigo(codigoMedioPagoVale);
            for (MZStechTKMovPago tkMovPago: tkMovPagoList){

                // Obtengo ID del medio de pago asociado al VALE en el mismo movimiento.
                sql = " select max(z_stech_tk_movpago_id) as id from z_stech_tk_movpago where z_stech_tk_mov_id =" + tkMov.get_ID() +
                        " and (sc_codigotipopago <> '" + codigoMedioPagoVale + "' and sc_codigotipopago <> '" + codigoMedioPagoEfectivo + "')";
                int ID_MovPagoAsociado = DB.getSQLValueEx(get_TrxName(), sql);
                if (ID_MovPagoAsociado > 0){
                    MZStechTKMovPago tkMovPagoAsociado = new MZStechTKMovPago(getCtx(), ID_MovPagoAsociado, get_TrxName());
                    if ((tkMovPagoAsociado != null) && (tkMovPagoAsociado.get_ID() > 0)){
                        // Copio info del medio de paso asociado en el VALE
                        tkMovPago.setSC_CodigoPlanPagos(tkMovPagoAsociado.getSC_CodigoPlanPagos());
                        tkMovPago.setSC_NumeroTarjeta(tkMovPagoAsociado.getSC_NumeroTarjeta());
                        tkMovPago.setSC_NumeroAutorizacion(tkMovPagoAsociado.getSC_NumeroAutorizacion());
                        tkMovPago.setSC_FechaVencimiento(tkMovPagoAsociado.getSC_FechaVencimiento());
                        tkMovPago.setSC_CodigoCredito(tkMovPagoAsociado.getSC_CodigoCredito());
                        if (tkMovPagoAsociado.getZ_StechCreditos_ID() > 0){
                            tkMovPago.setZ_StechCreditos_ID(tkMovPagoAsociado.getZ_StechCreditos_ID());
                        }
                        tkMovPago.setSC_NumeroDocumentoPago(tkMovPagoAsociado.getSC_NumeroDocumentoPago());
                        tkMovPago.setSC_NumeroCuotasPago(tkMovPagoAsociado.getSC_NumeroCuotasPago());
                        tkMovPago.setSC_TerminalCredito(tkMovPagoAsociado.getSC_TerminalCredito());
                        tkMovPago.setSC_ComercioCredito(tkMovPagoAsociado.getSC_ComercioCredito());
                        tkMovPago.saveEx();
                    }
                }
            }

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
    }

}
