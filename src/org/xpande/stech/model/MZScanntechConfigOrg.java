package org.xpande.stech.model;

import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * Modelo de local asociado a organización en configuración de POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/10/17.
 */
public class MZScanntechConfigOrg extends X_Z_ScanntechConfigOrg {

    public MZScanntechConfigOrg(Properties ctx, int Z_ScanntechConfigOrg_ID, String trxName) {
        super(ctx, Z_ScanntechConfigOrg_ID, trxName);
    }

    public MZScanntechConfigOrg(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }


    /***
     * Obtiene lista de cajas asociadas a esta organización.
     * Xpande. Created by Gabriel Vila on 1/14/19.
     * @return
     */
    public List<MZScanntechConfigCaja> getCajas(){

        String whereClause = X_Z_ScanntechConfigCaja.COLUMNNAME_Z_ScanntechConfigOrg_ID + " =" + this.get_ID();

        List<MZScanntechConfigCaja> lines = new Query(getCtx(), I_Z_ScanntechConfigCaja.Table_Name, whereClause, get_TrxName()).setOnlyActiveRecords(true).list();

        return lines;
    }


}
