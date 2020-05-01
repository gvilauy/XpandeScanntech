package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para información de ventas cuenta corriente desde interface de POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 5/1/20.
 */
public class MZStechVtaCtaCte extends X_Z_StechVtaCtaCte {

    public MZStechVtaCtaCte(Properties ctx, int Z_StechVtaCtaCte_ID, String trxName) {
        super(ctx, Z_StechVtaCtaCte_ID, trxName);
    }

    public MZStechVtaCtaCte(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
