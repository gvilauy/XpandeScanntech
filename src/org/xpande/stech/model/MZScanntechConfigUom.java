package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para asociar unidades de medida entre el sistema y el POS de Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/10/17.
 */
public class MZScanntechConfigUom extends X_Z_ScanntechConfigUom {

    public MZScanntechConfigUom(Properties ctx, int Z_ScanntechConfigUom_ID, String trxName) {
        super(ctx, Z_ScanntechConfigUom_ID, trxName);
    }

    public MZScanntechConfigUom(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
