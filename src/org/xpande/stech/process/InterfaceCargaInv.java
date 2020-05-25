package org.xpande.stech.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.SvrProcess;
import org.xpande.financial.model.MZLoadPago;
import org.xpande.stech.model.MZStechLoadInv;

/**
 * Proceso para interface de carga de comprobantes de compra desde POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 5/25/20.
 */
public class InterfaceCargaInv extends SvrProcess {

    MZStechLoadInv stechLoadInv = null;

    @Override
    protected void prepare() {

        this.stechLoadInv = new MZStechLoadInv(getCtx(), this.getRecord_ID(), get_TrxName());
    }

    @Override
    protected String doIt() throws Exception {

        try{

            if ((this.stechLoadInv.getFileName() == null) || (this.stechLoadInv.getFileName().trim().equalsIgnoreCase(""))){
                return "@Error@ Debe indicar archivo a procesar ";
            }

            this.stechLoadInv.executeInterface();

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return "OK";
    }

}
