package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/6/20.
 */
public class MZStechTMPMovDet extends X_Z_Stech_TMP_MovDet{

    public MZStechTMPMovDet(Properties ctx, int Z_Stech_TMP_MovDet_ID, String trxName) {
        super(ctx, Z_Stech_TMP_MovDet_ID, trxName);
    }

    public MZStechTMPMovDet(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
