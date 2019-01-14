package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para descuento de detalle de cabezal de movimiento de venta obtenido por interface al POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 1/14/19.
 */
public class MZStechTKMovDetDtos extends X_Z_Stech_TK_MovDetDtos {

    public MZStechTKMovDetDtos(Properties ctx, int Z_Stech_TK_MovDetDtos_ID, String trxName) {
        super(ctx, Z_Stech_TK_MovDetDtos_ID, trxName);
    }

    public MZStechTKMovDetDtos(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
