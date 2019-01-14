package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo de cajas por organización para POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 1/14/19.
 */
public class MZScanntechConfigCaja extends X_Z_ScanntechConfigCaja {

    public MZScanntechConfigCaja(Properties ctx, int Z_ScanntechConfigCaja_ID, String trxName) {
        super(ctx, Z_ScanntechConfigCaja_ID, trxName);
    }

    public MZScanntechConfigCaja(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
