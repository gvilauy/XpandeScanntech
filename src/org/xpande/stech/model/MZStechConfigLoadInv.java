package org.xpande.stech.model;

import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 5/31/20.
 */
public class MZStechConfigLoadInv extends X_Z_StechConfigLoadInv {

    public MZStechConfigLoadInv(Properties ctx, int Z_StechConfigLoadInv_ID, String trxName) {
        super(ctx, Z_StechConfigLoadInv_ID, trxName);
    }

    public MZStechConfigLoadInv(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /***
     * Obtiene y retorna modelo segun nombre de documebto de Scanntech.
     * @param ctx
     * @param zStechConfigID
     * @param nomDoc
     * @param trxName
     * @return
     */
    public static MZStechConfigLoadInv getByNomDoc(Properties ctx, int zStechConfigID, String nomDoc, String trxName){

        String whereClause = X_Z_StechConfigLoadInv.COLUMNNAME_Z_ScanntechConfig_ID + " =" + zStechConfigID +
                " AND " + X_Z_StechConfigLoadInv.COLUMNNAME_NomDocumento + " ='" + nomDoc + "'";

        MZStechConfigLoadInv model = new Query(ctx, I_Z_StechConfigLoadInv.Table_Name, whereClause, trxName).first();

        return model;
    }
}
