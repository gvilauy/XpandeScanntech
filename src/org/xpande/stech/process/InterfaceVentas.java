package org.xpande.stech.process;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;
import org.xpande.stech.model.MZScanntechConfig;
import org.xpande.stech.model.MZScanntechConfigOrg;
import org.xpande.stech.model.MZStechInterfaceVta;
import org.xpande.stech.utils.ProcesadorInterfaceOut;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Procesa de interface de datos de venta desde POS a ADempiere.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 1/14/19.
 */
public class InterfaceVentas extends SvrProcess {

    private ProcesadorInterfaceOut procesadorInterfaceOut = null;
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

        //this.procesadorInterfaceOut = new ProcesadorInterfaceOut(getCtx(), get_TrxName());

    }

    @Override
    protected String doIt() throws Exception {

        MZScanntechConfig scanntechConfig = MZScanntechConfig.getDefault(getCtx(), get_TrxName());

        // Si indico organización, proceso solo para esta, sino proceso para todas las que tenga asociadas al proveedor de POS
        List<MZScanntechConfigOrg> orgList = scanntechConfig.getOrganizationsByOrg(this.adOrgID);

        if (this.fechaConsulta == null){
            this.fechaConsulta = TimeUtil.trunc(new Timestamp(System.currentTimeMillis()), TimeUtil.TRUNC_DAY);
        }

        for (MZScanntechConfigOrg configOrg: orgList){

            // Evito interfaces duplicados por organización y fecha
            String sql = " select z_stechinterfacevta_id from z_stechinterfacevta " +
                            " where ad_org_id =" + configOrg.getAD_OrgTrx_ID() +
                            " and datetrx ='" + this.fechaConsulta + "'";
            int zStechInterfaceVtaIDAux = DB.getSQLValueEx(get_TrxName(), sql);
            if (zStechInterfaceVtaIDAux > 0){
                continue;
            }

            MZStechInterfaceVta interfaceVta = new MZStechInterfaceVta(getCtx(), 0, get_TrxName());
            interfaceVta.set_ValueOfColumn("AD_Client_ID", scanntechConfig.getAD_Client_ID());
            interfaceVta.setAD_Org_ID(configOrg.getAD_OrgTrx_ID());
            interfaceVta.saveEx();

            String message = interfaceVta.execute(configOrg, this.fechaConsulta);

            if (message != null){
                return "@Error@ " + message;
            }
        }

        return "OK";
    }

}
