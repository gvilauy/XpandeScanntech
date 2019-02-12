package org.xpande.stech.model;

import org.compiere.model.MTable;
import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * Modelo para configuración de interface de datos contra Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/10/17.
 */
public class MZScanntechConfig extends X_Z_ScanntechConfig {

    public MZScanntechConfig(Properties ctx, int Z_ScanntechConfig_ID, String trxName) {
        super(ctx, Z_ScanntechConfig_ID, trxName);
    }

    public MZScanntechConfig(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }


    /***
     * Obtiene y retorna modelo de configuración Scanntech por defecto.
     * Xpande. Created by Gabriel Vila on 7/10/17.
     * @param ctx
     * @param trxName
     * @return
     */
    public static MZScanntechConfig getDefault(Properties ctx, String trxName){

        MZScanntechConfig model = new Query(ctx, I_Z_ScanntechConfig.Table_Name, null, trxName).setOnlyActiveRecords(true).first();

        return model;
    }


    /***
     * Obtiene y retorna lista de locales asociados a la configuración del POS Scanntech.
     * Xpande. Created by Gabriel Vila on 7/10/17.
     * @return
     */
    public List<MZScanntechConfigOrg> getLocales(){

        String whereClause = X_Z_ScanntechConfigOrg.COLUMNNAME_Z_ScanntechConfig_ID + " =" + this.get_ID();

        List<MZScanntechConfigOrg> lines =  new Query(getCtx(), I_Z_ScanntechConfigOrg.Table_Name, whereClause, get_TrxName()).setOnlyActiveRecords(true).list();

        return lines;

    }


    /***
     * Obtiene y retorna modelo segun ID de unidad de medida del sistema recibido.
     * Xpande. Created by Gabriel Vila on 7/11/17.
     * @param cUOMID
     * @return
     */
    public MZScanntechConfigUom getConfigUOM(int cUOMID){

        String whereClause = X_Z_ScanntechConfigUom.COLUMNNAME_Z_ScanntechConfig_ID + " =" + this.get_ID() +
                " AND " + X_Z_ScanntechConfigUom.COLUMNNAME_C_UOM_ID + " =" + cUOMID;
        MZScanntechConfigUom model = new Query(getCtx(), I_Z_ScanntechConfigUom.Table_Name, whereClause, get_TrxName()).setOnlyActiveRecords(true).first();

        return model;
    }

    /***
     * Obtiene y retorna servicio api de configuración de interface Scanntech, según parametros recibidos.
     * Xpande. Created by Gabriel Vila on 7/11/17.
     * @param adTableID
     * @param crudType
     * @param isPriceChanged
     * @return
     */
    public MZScanntechConfigServ getConfigServ(int adTableID, String crudType, boolean isPriceChanged) {

        String whereClause = X_Z_ScanntechConfigServ.COLUMNNAME_Z_ScanntechConfig_ID + " =" + this.get_ID() +
                " AND " + X_Z_ScanntechConfigServ.COLUMNNAME_AD_Table_ID + " =" + adTableID +
                " AND " + X_Z_ScanntechConfigServ.COLUMNNAME_CRUDType + " ='" + crudType + "'" +
                " AND " + X_Z_ScanntechConfigServ.COLUMNNAME_IsPriceChanged + " ='" + ((isPriceChanged) ? 'Y':'N') + "'";

        MZScanntechConfigServ model = new Query(getCtx(), I_Z_ScanntechConfigServ.Table_Name, whereClause, get_TrxName()).first();

        return model;
    }

    /***
     * Obtiene y retorna modelo de parametrización de organización recibida.
     * Xpande. Created by Gabriel Vila on 2/12/18.
     * @param adOrgID
     * @return
     */
    public MZScanntechConfigOrg getOrgConfig(int adOrgID) {

        String whereClause = X_Z_ScanntechConfigOrg.COLUMNNAME_Z_ScanntechConfig_ID + " =" + this.get_ID() +
                " AND " + X_Z_ScanntechConfigOrg.COLUMNNAME_AD_OrgTrx_ID + " =" + adOrgID;

        MZScanntechConfigOrg model = new Query(this.getCtx(), I_Z_ScanntechConfigOrg.Table_Name, whereClause, this.get_TrxName()).first();

        return model;
    }


    /***
     * Obtiene y retorna lista de organizaciones asociadas a la configuracion del proveedor de POS Scanntech
     * Xpande. Created by Gabriel Vila on 2/13/18.
     * @return
     */
    public List<MZScanntechConfigOrg> getOrganization(){

        String whereClause = X_Z_ScanntechConfigOrg.COLUMNNAME_Z_ScanntechConfig_ID + " =" + this.get_ID();

        List<MZScanntechConfigOrg> lines = new Query(getCtx(), I_Z_ScanntechConfigOrg.Table_Name, whereClause, get_TrxName()).setOnlyActiveRecords(true).list();

        return lines;
    }


    /***
     * Obtiene y retorna lista de organizaciones asociadas a la configuracion del proveedor de POS Scanntech.
     * Xpande. Created by Gabriel Vila on 2/12/19.
     * @return
     */
    public List<MZScanntechConfigOrg> getOrganizationsByOrg(int adOrgTrxID){

        String whereClause = X_Z_ScanntechConfigOrg.COLUMNNAME_Z_ScanntechConfig_ID + " =" + this.get_ID();

        if (adOrgTrxID > 0){
            whereClause += " AND " + X_Z_ScanntechConfigOrg.COLUMNNAME_AD_OrgTrx_ID + " =" + adOrgTrxID;
        }

        List<MZScanntechConfigOrg> lines = new Query(getCtx(), I_Z_ScanntechConfigOrg.Table_Name, whereClause, get_TrxName()).setOnlyActiveRecords(true).list();

        return lines;
    }

}
