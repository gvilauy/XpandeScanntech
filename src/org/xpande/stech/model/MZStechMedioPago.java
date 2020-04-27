package org.xpande.stech.model;

import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 4/27/20.
 */
public class MZStechMedioPago extends X_Z_StechMedioPago {

    public MZStechMedioPago(Properties ctx, int Z_StechMedioPago_ID, String trxName) {
        super(ctx, Z_StechMedioPago_ID, trxName);
    }

    public MZStechMedioPago(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /***
     * Obtiene y retorna modelo segun codigo recibido.
     * Xpande. Created by Gabriel Vila on 4/27/20.
     * @param ctx
     * @param value
     * @param trxName
     * @return
     */
    public static MZStechMedioPago getByValue(Properties ctx, String value, String trxName){

        String whereClause = X_Z_StechMedioPago.COLUMNNAME_Value + " ='" + value + "'";

        MZStechMedioPago model = new Query(ctx, I_Z_StechMedioPago.Table_Name, whereClause, trxName).first();

        return model;
    }
}
