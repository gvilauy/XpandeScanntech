package org.xpande.stech.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;
import org.python.parser.ast.Str;
import org.xpande.stech.model.MZScanntechConfig;
import org.xpande.stech.model.MZScanntechConfigOrg;
import org.xpande.stech.model.MZStechInterfaceVta;
import org.xpande.stech.model.MZStechVtaCtaCte;
import org.xpande.stech.utils.ProcesadorInterfaceOut;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Proceso para eliminar una determinada interface de venta del POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 5/7/20.
 */
public class DeleteInterfaceVta extends SvrProcess {

    private int adOrgID = 0;
    private Timestamp fechaConsulta = null;

    @Override
    protected void prepare() {

        ProcessInfoParameter[] para = getParameter();

        for (int i = 0; i < para.length; i++){

            String name = para[i].getParameterName();

            if (name != null){
                if (para[i].getParameter() != null){
                    if (name.trim().equalsIgnoreCase("AD_Org_ID")){
                        this.adOrgID = ((BigDecimal)para[i].getParameter()).intValueExact();
                    }
                    else if (name.trim().equalsIgnoreCase("DateTrx")){
                        this.fechaConsulta = (Timestamp)para[i].getParameter();
                    }
                }
            }
        }
    }

    @Override
    protected String doIt() throws Exception {

        if (this.fechaConsulta == null){
            this.fechaConsulta = TimeUtil.trunc(new Timestamp(System.currentTimeMillis()), TimeUtil.TRUNC_DAY);
        }

        String message = this.deleteInterface();

        if (message != null){
            return "@Error@ " + message;
        }

        return "OK";
    }

    /***
     * Elimina información asociada a este interface.
     * Xpande. Created by Gabriel Vila on 5/7/20.
     * @return
     */
    private String deleteInterface() {

        String message = null;
        String action = "";

        try{

            // Obtengo modelo de interface de venta según organización y fecha recibidos por parametro.
            MZStechInterfaceVta interfaceVta = MZStechInterfaceVta.getByOrgDate(getCtx(), this.adOrgID, this.fechaConsulta, get_TrxName());
            if ((interfaceVta == null) || (interfaceVta.get_ID() <= 0)){
                return "No se pudo obtener Interface de Venta para Organización y Fecha indicados";
            }

            // Obtengo ventas de cuenta corriente de esta interface de venta
            List<MZStechVtaCtaCte> vtaCtaCteList = interfaceVta.getVtasCtaCte();

            // Recorro ventas cuenta corriente para reactivar y eliminar invoices asociadas
            for (MZStechVtaCtaCte vtaCtaCte: vtaCtaCteList){
                if (vtaCtaCte.getC_Invoice_ID() > 0){
                    MInvoice invoice = (MInvoice) vtaCtaCte.getC_Invoice();
                    if ((invoice != null) && (invoice.get_ID() > 0)){

                        // Reactivo invoice
                        if (!invoice.reActivateIt()){
                            message = "No se pudo reactivar comprobate de venta número : " + invoice.getDocumentNo();
                            if (invoice.getProcessMsg() != null){
                                message += " - " + invoice.getProcessMsg();
                            }
                        }
                        invoice.saveEx();

                        // Elimino invoice
                        invoice.deleteEx(true);
                    }
                }
            }

            // Elimino información directo de tablas
            action = " delete from z_stechvtactacte where Z_StechInterfaceVta_ID =" + interfaceVta.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            action = " delete from z_stech_tk_movdetdtos where Z_StechInterfaceVta_ID =" + interfaceVta.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            action = " delete from z_stech_tk_movpago where Z_StechInterfaceVta_ID =" + interfaceVta.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            action = " alter table z_stech_tk_movdet disable trigger all ";
            DB.executeUpdateEx(action, get_TrxName());

            action = " delete from z_stech_tk_movdet where Z_StechInterfaceVta_ID =" + interfaceVta.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            action = " alter table z_stech_tk_movdet enable trigger all ";
            DB.executeUpdateEx(action, get_TrxName());

            action = " alter table z_stech_tk_mov disable trigger all ";
            DB.executeUpdateEx(action, get_TrxName());

            action = " delete from z_stech_tk_mov where Z_StechInterfaceVta_ID =" + interfaceVta.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            action = " alter table z_stech_tk_mov enable trigger all ";
            DB.executeUpdateEx(action, get_TrxName());

            action = " alter table z_stechinterfacevta disable trigger all ";
            DB.executeUpdateEx(action, get_TrxName());

            action = " delete from Z_StechInterfaceVta where Z_StechInterfaceVta_ID =" + interfaceVta.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            action = " alter table z_stechinterfacevta enable trigger all ";
            DB.executeUpdateEx(action, get_TrxName());

            action = " delete from z_bi_invprodday where ad_org_id =" + this.adOrgID +
                    " and datetrx ='" + this.fechaConsulta + "' ";
            DB.executeUpdateEx(action, get_TrxName());

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return null;
    }

}
