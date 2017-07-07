package org.xpande.stech.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * Modelo para interface de datos desde el sistema hacia ScannTech
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/6/17.
 */
public class MZStechInterfaceOut extends X_Z_StechInterfaceOut {

    public MZStechInterfaceOut(Properties ctx, int Z_StechInterfaceOut_ID, String trxName) {
        super(ctx, Z_StechInterfaceOut_ID, trxName);
    }

    public MZStechInterfaceOut(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }


    /***
     * Obtiene y retorna modelo según parametros recibidos
     * Xpande. Created by Gabriel Vila on 7/6/17.
     * @param ctx
     * @param CRUDType
     * @param adTableID
     * @param recordID
     * @param isPriceChanged
     * @param trxName
     * @return
     */
    public static MZStechInterfaceOut getRecord(Properties ctx, String CRUDType, int adTableID, int recordID, boolean isPriceChanged, String trxName){

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_AD_Table_ID + " =" + adTableID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_Record_ID + " =" + recordID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_CRUDType + " ='" + CRUDType + "'" +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_IsPriceChanged + " ='" + ((isPriceChanged) ? 'Y' : 'N') + "'";
        MZStechInterfaceOut model = new Query(ctx, I_Z_StechInterfaceOut.Table_Name, whereClause, trxName).first();

        return model;

    }

    /***
     * Obtiene y retorna modelo según parametros recibidos
     * Xpande. Created by Gabriel Vila on 7/6/17.
     * @param ctx
     * @param CRUDType
     * @param adTableID
     * @param recordID
     * @param isPriceChanged
     * @param trxName
     * @return
     */
    public static MZStechInterfaceOut getRecord(Properties ctx, String CRUDType, int adTableID, int recordID, int adOrgTrxID, boolean isPriceChanged, String trxName){

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_AD_Table_ID + " =" + adTableID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_Record_ID + " =" + recordID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_AD_OrgTrx_ID + " =" + adOrgTrxID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_CRUDType + " ='" + CRUDType + "'" +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_IsPriceChanged + " ='" + ((isPriceChanged) ? 'Y' : 'N') + "'";

        MZStechInterfaceOut model = new Query(ctx, I_Z_StechInterfaceOut.Table_Name, whereClause, trxName).first();

        return model;

    }


    /***
     * Obtiene y retorna lista de modelos no ejecutados, ordenados por secuencia.
     * Xpande. Created by Gabriel Vila on 7/6/17.
     * @param ctx
     * @param trxName
     * @return
     */
    public static List<MZStechInterfaceOut> getLinesNotExecuted(Properties ctx, String trxName) {

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_IsExecuted + " ='N'";

        List<MZStechInterfaceOut> lines = new Query(ctx, I_Z_StechInterfaceOut.Table_Name, whereClause, trxName).setOrderBy(" SeqNo ").list();

        return lines;
    }


    public String execute(){

        String message = null;

        try{


        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return message;

    }


}
