package org.xpande.stech.process;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.xpande.stech.model.MZScanntechConfig;
import org.xpande.stech.model.MZScanntechConfigOrg;
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
    private int adOrgID = 1000000;
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

        this.procesadorInterfaceOut = new ProcesadorInterfaceOut(getCtx(), get_TrxName());

    }

    @Override
    protected String doIt() throws Exception {

        String message = null;

        MZScanntechConfig scanntechConfig = MZScanntechConfig.getDefault(getCtx(), get_TrxName());

        if (this.adOrgID > 0){
            MZScanntechConfigOrg configOrg = scanntechConfig.getOrgConfig(this.adOrgID);
            message = this.procesadorInterfaceOut.executeInterfaceMov(configOrg, this.fechaConsulta);
        }
        else {
            // Tengo que procesar todas las organización que tienen a Scanntech como POS

            List<MZScanntechConfigOrg> configOrgList = scanntechConfig.getOrganization();
            for (MZScanntechConfigOrg configOrg: configOrgList){
                message = this.procesadorInterfaceOut.executeInterfaceMov(configOrg, this.fechaConsulta);
                if (message != null){
                    return "@Error@ " + message;
                }
            }
        }

        if (message != null){
            return "@Error@ " + message;
        }

        return "OK";
    }

}
