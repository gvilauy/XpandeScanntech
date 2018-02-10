package org.xpande.stech.utils;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_M_Product;
import org.compiere.model.Query;
import org.compiere.util.TimeUtil;
import org.xpande.core.model.I_Z_ProductoUPC;
import org.xpande.stech.model.I_Z_StechInterfaceOut;
import org.xpande.stech.model.MZScanntechConfig;
import org.xpande.stech.model.MZStechInterfaceOut;
import org.xpande.stech.model.X_Z_StechInterfaceOut;

import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

/**
 * Clase para el proceso de interface de salida de datos desde el sistema hacia Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 2/2/18.
 */
public class ProcesadorInterfaceOut {

    private Properties ctx = null;
    private String trxName = null;

    // Configurador de Scanntech
    private MZScanntechConfig scanntechConfig = null;

    /***
     * Constructor
     * @param ctx
     * @param trxName
     */
    public ProcesadorInterfaceOut(Properties ctx, String trxName) {
        this.ctx = ctx;
        this.trxName = trxName;
    }


    /***
     * Ejecuta proceso de interface de salida para Scanntech.
     * En caso de venir un ID correspondiente un proceso de comunicacin de datos al pos, entonces verifico que datos debo enviar o no.
     * En caso de no venir dicho ID, considero los flags que indican si debo procesar lo correspondiente a productos y socios.
     * Xpande. Created by Gabriel Vila on 7/27/17.
     * @param adOrgID
     * @param zComunicacionPosID
     * @param processPrices
     * @param processProducts
     * @param processPartners
     * @return
     */
    public String executeInterfaceOut(int adOrgID, int zComunicacionPosID, boolean processPrices, boolean processProducts, boolean processPartners){

        String message = null;

        try{

            // Si no tengo id de proceso de comunicacion
            if (zComunicacionPosID <= 0){
                // Si no recibo flags de procesar productos o socios, no hago nada
                if (!processProducts && !processPartners){
                    return message;
                }
            }
            else{
                // Siempre comunico socios si recibo ID de proceso de comunicacion de datos al pos
                processPartners = true;
            }

            // Obtengo configurador de scanntech
            this.scanntechConfig = MZScanntechConfig.getDefault(ctx, trxName);


            // Proceso lineas de interface de salida correspondiente a productos
            if ((zComunicacionPosID > 0) || (processProducts)){
                message = this.executeInterfaceOutProducts(adOrgID, zComunicacionPosID, processPrices);
                if (message != null) return message;
            }

            // Proces lineas de socios de negocio
            if (processPartners){
                message = this.executeInterfaceOutPartners(adOrgID, zComunicacionPosID);
                if (message != null) return message;
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return message;
    }

    /***
     * Procesa interface de salida de clientes para Scanntech.
     * Xpande. Created by Gabriel Vila on 10/9/17.
     * @param adOrgID
     * @param zComunicacionPosID
     * @return
     */
    private String executeInterfaceOutPartners(int adOrgID, int zComunicacionPosID) {

        String message = null;

        try{


        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        return message;

    }



    /***
     * Procesa interface de salida de productos para Sisteco.
     * Xpande. Created by Gabriel Vila on 7/27/17.
     * @param adOrgID
     * @param zComunicacionPosID
     * @param processPrices
     */
    private String executeInterfaceOutProducts(int adOrgID, int zComunicacionPosID, boolean processPrices) {

        String message = null;

        try{

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        return message;
    }




    /***
     * Obtiene y retorna lineas de interface de salida para productos no ejecutadas al momento.
     * En caso de recibir un id de proceso de comunicacion de datos al pos, debo filtrar segun proceso o no precios.
     * Xpande. Created by Gabriel Vila on 7/24/17.
     * @return
     * @param zComunicacionPosID
     * @param processPrices
     */
    private List<MZStechInterfaceOut> getLinesProdsNotExecuted(int zComunicacionPosID, boolean processPrices){

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_IsExecuted + " ='N' " +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_AD_Table_ID + " =" + I_M_Product.Table_ID;

        Timestamp fechaHoy = TimeUtil.trunc(new Timestamp(System.currentTimeMillis()), TimeUtil.TRUNC_DAY);

        // Si recibo ID de proceso de comunicacion de datos al pos
        if (zComunicacionPosID > 0){
            // Si en este proceso No se quiere comunicar precios de productos
            if (!processPrices){
                // No proceso ninguna marca de crear o actualizar productos. Solo considero las marcas de eliminar y aquellas marcas de
                // actualización pero que no sean por cambios de precio
                whereClause += " AND ((" + X_Z_StechInterfaceOut.COLUMNNAME_CRUDType + " ='" + X_Z_StechInterfaceOut.CRUDTYPE_DELETE + "') " +
                        " OR (" + X_Z_StechInterfaceOut.COLUMNNAME_CRUDType + " ='" + X_Z_StechInterfaceOut.CRUDTYPE_UPDATE + "'" +
                        " AND " + X_Z_StechInterfaceOut.COLUMNNAME_IsPriceChanged + " ='N')) ";
            }
            else {
                // Solo debo conisderar marcas de aquellos productos contenidos en el proceso de comunicacion de datos al pos.
                whereClause += " AND " + X_Z_StechInterfaceOut.COLUMNNAME_Record_ID + " IN " +
                        " (select m_product_id from z_confirmacionetiquetaprod " +
                        " where WithOfferSO ='N' and z_confirmacionetiquetadoc_id in " +
                        " (select z_confirmacionetiquetadoc_id from z_confirmacionetiquetadoc " +
                        " where comunicadopos='N' and isselected='Y' and isconfirmed='Y' " +
                        " and ((DateToPos is null) or (DateToPos <='" + fechaHoy + "')) " +
                        " and z_confirmacionetiqueta_id in " +
                        " (select z_confirmacionetiqueta_id from z_confirmacionetiqueta where z_comunicacionpos_id =" + zComunicacionPosID + "))) ";
            }
        }

        List<MZStechInterfaceOut> lines = new Query(ctx, I_Z_StechInterfaceOut.Table_Name, whereClause, trxName).setOrderBy(" SeqNo, Created  ").list();

        return lines;

    }


    /***
     * Obtiene y retorna lineas de interface de salida para códigos de barras no ejecutadas al momento.
     * En caso de recibir un id de proceso de comunicacion de datos al pos, debo filtrar segun proceso o no precios.
     * Xpande. Created by Gabriel Vila on 7/24/17.
     * @return
     * @param zComunicacionPosID
     * @param processPrices
     */
    private List<MZStechInterfaceOut> getLinesUPCNotExecuted(int zComunicacionPosID, boolean processPrices){

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_IsExecuted + " ='N' " +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_AD_Table_ID + " =" + I_Z_ProductoUPC.Table_ID;

        // Si recibo ID de proceso de comunicacion de datos al pos
        if (zComunicacionPosID > 0){
            // Si en este proceso No se quiere comunicar precios de productos
            if (!processPrices){
                // No proceso ninguna marca de crear o actualizar productos. Solo considero las marcas de eliminar.
                whereClause += " AND " + X_Z_StechInterfaceOut.COLUMNNAME_CRUDType + " ='" + X_Z_StechInterfaceOut.CRUDTYPE_DELETE + "' ";
            }
            else{
                // Solo debo conisderar marcas de aquellos productos contenidos en el proceso de comunicacion de datos al pos.

                /*
                whereClause += " AND " + X_Z_SistecoInterfaceOut.COLUMNNAME_Record_ID + " IN " +
                        " (select z_productoupc_id from z_productoupc where m_product_id in " +
                        " (select m_product_id from z_confirmacionetiquetaprod where z_confirmacionetiquetadoc_id in " +
                        " (select z_confirmacionetiquetadoc_id from z_confirmacionetiquetadoc where isselected='Y' and isconfirmed='Y' and z_confirmacionetiqueta_id in " +
                        " (select z_confirmacionetiqueta_id from z_confirmacionetiqueta where z_comunicacionpos_id =" + zComunicacionPosID + ")))) ";
                */

                /*
                whereClause += " AND " + X_Z_SistecoInterfaceOut.COLUMNNAME_Record_ID + " IN " +
                        " (select z_productoupc_id from z_productoupc where m_product_id in " +
                        " (select m_product_id from m_product where ComunicadoPos ='Y')) ";
                */
            }
        }

        List<MZStechInterfaceOut> lines = new Query(ctx, I_Z_StechInterfaceOut.Table_Name, whereClause, trxName).setOrderBy(" SeqNo, Created  ").list();

        return lines;

    }


    /***
     * Obtiene y retorna lineas de interface de salida para socios de negocio no ejecutadas al momento.
     * Xpande. Created by Gabriel Vila on 7/24/17.
     * @return
     */
    private List<MZStechInterfaceOut> getLinesBPartnerNotExecuted(){

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_IsExecuted + " ='N' " +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_AD_Table_ID + " =" + I_C_BPartner.Table_ID;

        List<MZStechInterfaceOut> lines = new Query(ctx, I_Z_StechInterfaceOut.Table_Name, whereClause, trxName).setOrderBy(" SeqNo, Created  ").list();

        return lines;

    }


    /***
     * Obtiene y retorna lista de marcas procesadas para un determinado proceso de Comunicacion de datos al POS.
     * Xpande. Created by Gabriel Vila on 10/13/17.
     * @param zComunicacionPosID
     * @return
     */
    public List<MZStechInterfaceOut> getMarcasProcesadas(int zComunicacionPosID) {

        //String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_Z_ComunicacionPOS_ID + " =" + zComunicacionPosID;
        String whereClause = "";

        List<MZStechInterfaceOut> lines = new Query(this.ctx, I_Z_StechInterfaceOut.Table_Name, whereClause, this.trxName).list();

        return lines;

    }


}
