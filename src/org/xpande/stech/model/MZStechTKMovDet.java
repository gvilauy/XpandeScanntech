package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para detalle de cabezal de movimiento de venta obtenido por interface al POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 1/14/19.
 */
public class MZStechTKMovDet extends X_Z_Stech_TK_MovDet {

    public MZStechTKMovDet(Properties ctx, int Z_Stech_TK_MovDet_ID, String trxName) {
        super(ctx, Z_Stech_TK_MovDet_ID, trxName);
    }

    public MZStechTKMovDet(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
