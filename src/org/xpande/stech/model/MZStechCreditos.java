package org.xpande.stech.model;

import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 4/27/20.
 */
public class MZStechCreditos extends X_Z_StechCreditos {

    public MZStechCreditos(Properties ctx, int Z_StechCreditos_ID, String trxName) {
        super(ctx, Z_StechCreditos_ID, trxName);
    }

    public MZStechCreditos(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /***
     * Obtiene y retorna modelo segun código recibido.
     * Xpande. Created by Gabriel Vila on 4/27/20.
     * @param ctx
     * @param value
     * @param trxName
     * @return
     */
    public static MZStechCreditos getByValue(Properties ctx, String value, String trxName){

        String whereClause = X_Z_StechCreditos.COLUMNNAME_Value + " ='" + value + "'";

        MZStechCreditos model = new Query(ctx, I_Z_StechCreditos.Table_Name, whereClause, trxName).first();

        return model;
    }
}
