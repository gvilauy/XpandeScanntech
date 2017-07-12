package org.xpande.stech.model;

import java.sql.ResultSet;
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
}
