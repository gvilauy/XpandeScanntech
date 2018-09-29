package org.xpande.stech.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_M_Product;
import org.compiere.process.SvrProcess;
import org.xpande.core.model.I_Z_ProductoUPC;
import org.xpande.stech.model.MZScanntechConfig;
import org.xpande.stech.model.MZScanntechConfigOrg;
import org.xpande.stech.model.MZStechInterfaceOut;
import org.xpande.stech.utils.ProcesadorInterfaceOut;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 8/13/18.
 */
public class ExecuteMarcaInterface extends SvrProcess {

    MZStechInterfaceOut interfaceOut = null;

    @Override
    protected void prepare() {
        this.interfaceOut = new MZStechInterfaceOut(getCtx(), this.getRecord_ID(), get_TrxName());
    }

    @Override
    protected String doIt() throws Exception {

        try{

            MZScanntechConfig scanntechConfig = MZScanntechConfig.getDefault(getCtx(), get_TrxName());
            MZScanntechConfigOrg configOrg = scanntechConfig.getOrgConfig(this.interfaceOut.getAD_OrgTrx_ID());

            ProcesadorInterfaceOut procesadorInterfaceOut = new ProcesadorInterfaceOut(getCtx(), get_TrxName());

            if (this.interfaceOut.getAD_Table_ID() == I_M_Product.Table_ID){
                procesadorInterfaceOut.executeInterfaceOutProduct(this.interfaceOut, 0, false, configOrg);
            }
            else if (this.interfaceOut.getAD_Table_ID() == I_Z_ProductoUPC.Table_ID){
                procesadorInterfaceOut.executeInterfaceOutUpc(this.interfaceOut, 0, false, configOrg, null);
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return "OK";
    }
}
