package org.xpande.stech.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para cabezales de procesos de interface de ventas contra POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 2/12/19.
 */
public class MZStechInterfaceVta extends X_Z_StechInterfaceVta {

    public MZStechInterfaceVta(Properties ctx, int Z_StechInterfaceVta_ID, String trxName) {
        super(ctx, Z_StechInterfaceVta_ID, trxName);
    }

    public MZStechInterfaceVta(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
