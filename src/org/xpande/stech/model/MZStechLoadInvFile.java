package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Clase para lineas leídas en interface de comprobantes de compra de POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 5/25/20.
 */
public class MZStechLoadInvFile extends X_Z_StechLoadInvFile {

    public MZStechLoadInvFile(Properties ctx, int Z_StechLoadInvFile_ID, String trxName) {
        super(ctx, Z_StechLoadInvFile_ID, trxName);
    }

    public MZStechLoadInvFile(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
