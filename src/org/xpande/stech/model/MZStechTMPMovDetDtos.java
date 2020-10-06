package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/6/20.
 */
public class MZStechTMPMovDetDtos extends X_Z_Stech_TMP_MovDetDtos{

    public MZStechTMPMovDetDtos(Properties ctx, int Z_Stech_TMP_MovDetDtos_ID, String trxName) {
        super(ctx, Z_Stech_TMP_MovDetDtos_ID, trxName);
    }

    public MZStechTMPMovDetDtos(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
