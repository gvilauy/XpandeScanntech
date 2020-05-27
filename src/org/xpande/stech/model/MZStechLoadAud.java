package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 5/27/20.
 */
public class MZStechLoadAud extends X_Z_StechLoadAud{

    public MZStechLoadAud(Properties ctx, int Z_StechLoadAud_ID, String trxName) {
        super(ctx, Z_StechLoadAud_ID, trxName);
    }

    public MZStechLoadAud(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
