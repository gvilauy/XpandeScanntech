package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para medio de pago de cabezal de movimiento de venta obtenido por interface al POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 1/14/19.
 */
public class MZStechTKMovPago extends X_Z_Stech_TK_MovPago {

    public MZStechTKMovPago(Properties ctx, int Z_Stech_TK_MovPago_ID, String trxName) {
        super(ctx, Z_Stech_TK_MovPago_ID, trxName);
    }

    public MZStechTKMovPago(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
