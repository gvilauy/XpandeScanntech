package org.xpande.stech.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.Query;
import org.compiere.util.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 10/6/20.
 */
public class MZStechTMPMov extends X_Z_Stech_TMP_Mov{

    public MZStechTMPMov(Properties ctx, int Z_Stech_TMP_Mov_ID, String trxName) {
        super(ctx, Z_Stech_TMP_Mov_ID, trxName);
    }

    public MZStechTMPMov(Properties ctx, ResultSet rs, String trxName) {
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
            sql = " select z_stech_tmp_mov_id from z_stech_tmp_mov where sc_numeromov='" + numeroMov + "'";

            pstmt = DB.prepareStatement(sql, trxName);
            rs = pstmt.executeQuery();

            if (rs.next()){
                id = rs.getInt("z_stech_tmp_mov_id");
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

    /***
     * Obtiene y retorna lista de medios de pago, asociados a este movimiento, según código de medio de pago recibido.
     * Xpande. Created by Gabriel Vila on 6/3/19.
     * @param codigoMedioPagoVale
     * @return
     */
    public List<MZStechTMPMovPago> getMediosPagoByCodigo(String codigoMedioPago){

        String whereClause = X_Z_Stech_TMP_MovPago.COLUMNNAME_Z_Stech_TMP_Mov_ID + " =" + this.get_ID() +
                " AND " + X_Z_Stech_TMP_MovPago.COLUMNNAME_SC_CodigoTipoPago + " ='" + codigoMedioPago + "' ";

        List<MZStechTMPMovPago> lines = new Query(getCtx(), I_Z_Stech_TMP_MovPago.Table_Name, whereClause, get_TrxName()).list();

        return lines;
    }

}
