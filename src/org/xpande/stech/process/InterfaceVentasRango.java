package org.xpande.stech.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Trx;
import org.xpande.stech.model.MZScanntechConfig;
import org.xpande.stech.model.MZScanntechConfigOrg;
import org.xpande.stech.model.MZStechInterfaceVta;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 2/12/19.
 */
public class InterfaceVentasRango extends SvrProcess {

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


        MZScanntechConfig scanntechConfig = MZScanntechConfig.getDefault(getCtx(), null);

        // Si indico organización, proceso solo para esta, sino proceso para todas las que tenga asociadas al proveedor de POS
        List<MZScanntechConfigOrg> orgList = scanntechConfig.getOrganizationsByOrg(this.adOrgID);

        for (MZScanntechConfigOrg configOrg: orgList){

            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();

            start.setTime(this.startDate);
            end.setTime(this.endDate);


            // Loop recorriendo rango de fechas ingresadas
            while( !start.after(end)){

                Timestamp fechaProceso = new Timestamp(start.getTime().getTime());

                Trx trans = null;
                try{

                    String trxName = Trx.createTrxName();
                    trans = Trx.get(trxName, true);

                    MZStechInterfaceVta interfaceVta = new MZStechInterfaceVta(getCtx(), 0, trxName);
                    interfaceVta.set_ValueOfColumn("AD_Client_ID", scanntechConfig.getAD_Client_ID());
                    interfaceVta.setAD_Org_ID(configOrg.getAD_OrgTrx_ID());
                    interfaceVta.saveEx();

                    String message = interfaceVta.execute(configOrg, fechaProceso);

                    trans.close();
                }
                catch (Exception e){
                    if (trans != null){
                        trans.rollback();
                    }
                    throw new AdempiereException(e);
                }

                start.add(Calendar.DATE, 1);
            }
        }

        return "OK";
    }

}
