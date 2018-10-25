package org.xpande.stech.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para auditoria de comunicación al pos de productos - organizacion.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/25/18.
 */
public class MZStechProdOrgCom extends X_Z_StechProdOrgCom {

    public MZStechProdOrgCom(Properties ctx, int Z_StechProdOrgCom_ID, String trxName) {
        super(ctx, Z_StechProdOrgCom_ID, trxName);
    }

    public MZStechProdOrgCom(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }


    /***
     * Verfica si en una determinada organización, un determinado producto fue comunicado al pos.
     * Xpande. Created by Gabriel Vila on 10/25/18.
     * @param ctx
     * @param mProductID
     * @param adOrgTrxID
     * @param trxName
     * @return
     */
    public static boolean isComunicadoPOS(Properties ctx, int mProductID, int adOrgTrxID, String trxName){

        boolean result = false;

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            sql = " select z_stechprodorgcom_id " +
                    "from z_stechprodorgcom " +
                    "where ad_orgtrx_id =" + adOrgTrxID +
                    "and m_product_id =" + mProductID +
                    "and crudtype ='C'";

            pstmt = DB.prepareStatement(sql, trxName);
            rs = pstmt.executeQuery();

            if (rs.next()){
                result = true;
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
            rs = null; pstmt = null;
        }

        return result;
    }

}
