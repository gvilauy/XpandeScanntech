package org.xpande.stech.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Modelo para cabezales de movimientos de ventas obtenidos por interface al POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 1/14/19.
 */
public class MZStechTKMov  extends X_Z_Stech_TK_Mov {

    public MZStechTKMov(Properties ctx, int Z_Stech_TK_Mov_ID, String trxName) {
        super(ctx, Z_Stech_TK_Mov_ID, trxName);
    }

    public MZStechTKMov(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }


    /***
     * Obtiene y retorna ID de este modelo, según numero de movimiento recibido.
     * Xpande. Created by Gabriel Vila on 2/12/19.
     * @param ctx
     * @param numeroMov
     * @param trxName
     * @return
     */
    public static int getIDByNumeroMov(Properties ctx, String numeroMov, String trxName){

        int id = -1;

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            sql = " select z_stech_tk_mov_id from z_stech_tk_mov where sc_numeromov='" + numeroMov + "'";

        	pstmt = DB.prepareStatement(sql, trxName);
        	rs = pstmt.executeQuery();

        	if (rs.next()){
        	    id = rs.getInt("z_stech_tk_mov_id");
        	}
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
        	rs = null; pstmt = null;
        }

        return id;
    }

}
