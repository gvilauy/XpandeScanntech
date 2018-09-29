package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo de servicio API en configuración de POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/10/17.
 */
public class MZScanntechConfigServ extends X_Z_ScanntechConfigServ {

    public MZScanntechConfigServ(Properties ctx, int Z_ScanntechConfigServ_ID, String trxName) {
        super(ctx, Z_ScanntechConfigServ_ID, trxName);
    }

    public MZScanntechConfigServ(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
