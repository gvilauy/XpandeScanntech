package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para cabezales de movimientos de ventas obtenidos por interface al POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 1/14/19.
 */
public class MZStechTKMov  extends X_Z_Stech_TK_Mov {

    public MZStechTKMov(Properties ctx, int Z_Stech_TK_Mov_ID, String trxName) {
        super(ctx, Z_Stech_TK_Mov_ID, trxName);
    }

    public MZStechTKMov(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
