package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/6/20.
 */
public class MZStechTMPMovPago extends X_Z_Stech_TMP_MovPago{

    public MZStechTMPMovPago(Properties ctx, int Z_Stech_TMP_MovPago_ID, String trxName) {
        super(ctx, Z_Stech_TMP_MovPago_ID, trxName);
    }

    public MZStechTMPMovPago(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
