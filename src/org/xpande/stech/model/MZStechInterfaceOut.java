package org.xpande.stech.model;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.compiere.model.*;
import org.compiere.util.DB;
import org.json.JSONObject;
import org.xpande.core.model.I_Z_ProductoUPC;
import org.xpande.core.model.MZProductoUPC;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Modelo para interface de datos desde el sistema hacia ScannTech
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/6/17.
 */
public class MZStechInterfaceOut extends X_Z_StechInterfaceOut {

    private MZScanntechConfig scanntechConfig = null;
    private String errorMessage = null;
    private final int RESPUESTA_OK = 200;

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
     * @param adTableID
     * @param recordID
     * @param adOrgID
     * @param trxName
     * @return
     */
    public static MZStechInterfaceOut getRecord(Properties ctx, int adTableID, int recordID, int adOrgID, String trxName){

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_AD_Table_ID + " =" + adTableID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_Record_ID + " =" + recordID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_AD_OrgTrx_ID + " =" + adOrgID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_IsExecuted + " ='N'";

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
    public static MZStechInterfaceOut getRecord(Properties ctx, String CRUDType, int adTableID, int recordID, boolean isPriceChanged, String trxName){

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_AD_Table_ID + " =" + adTableID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_Record_ID + " =" + recordID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_CRUDType + " ='" + CRUDType + "'" +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_IsPriceChanged + " ='" + ((isPriceChanged) ? 'Y' : 'N') + "'" +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_IsExecuted + " ='N'";

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
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_IsPriceChanged + " ='" + ((isPriceChanged) ? 'Y' : 'N') + "'" +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_IsExecuted + " ='N'";

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


    /***
     * Ejecuta interface contenida en este objeto.
     * Xpande. Created by Gabriel Vila on 7/11/17.
     * @return
     */
    public String execute(){

        String message = null;

        try{

            // Instancio modelo de configuracion del POS Scanntech
            this.scanntechConfig = MZScanntechConfig.getDefault(getCtx(), get_TrxName());

            // Procedo según entidad y acción

            // Si es entidad = Producto
            if (this.getAD_Table_ID() == I_M_Product.Table_ID){

                // Nuevo producto
                if (this.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){

                    message = this.posNuevoProducto();

                }
                // Eliminación de producto
                else if (this.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){


                }
                // Actualización de producto
                else if (this.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_UPDATE)){

                }

            }
            // Si es entidad = Código de barras
            else if (this.getAD_Table_ID() == I_Z_ProductoUPC.Table_ID){

            }

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return message;

    }

    /***
     * Envía nuevo producto al POS.
     * Xpande. Created by Gabriel Vila on 7/11/17.
     * @return
     */
    private String posNuevoProducto() {

        String message =  null;

        try{
            // Obtengo objeto json para creación de producto
            JSONObject jsonObject = this.jsonProductCreate();
            if (jsonObject == null){
                if (this.errorMessage == null){
                    this.errorMessage = "No se obtuvo objeto Json para el envío del producto con ID : " + this.getRecord_ID();
                }
                return this.errorMessage;
            }

            // Obtengo servicio api para http request
            MZScanntechConfigServ configServ = this.scanntechConfig.getConfigServ(this.getAD_Table_ID(), this.getCRUDType(), this.isPriceChanged());
            if ((configServ == null) || (configServ.get_ID() <= 0)){
                return "No se obtuvo parametros de Http Request para ID de interface : " + this.get_ID();
            }

            // Ejecuto interface de creación de producto
            CloseableHttpResponse response = this.executeHttpRequest(jsonObject, configServ);

            // Proceso respuesta
            if (response.getStatusLine().getStatusCode() == RESPUESTA_OK){
                this.setIsExecuted(true);
                this.setIsError(false);
            }
            else {
                this.setIsExecuted(false);
                this.setIsError(true);
                if (response.getEntity().getContent() != null){
                    this.setErrorMsg(new Scanner(response.getEntity().getContent()).useDelimiter("\\A").next());
                }
            }
            this.saveEx();

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return message;
    }

    /***
     * Ejecuta el Http Request de interface POS según parametros recibidos.
     * Xpande. Created by Gabriel Vila on 7/11/17.
     * @param jsonObject
     * @param configServ
     * @return
     */
    private CloseableHttpResponse executeHttpRequest(JSONObject jsonObject, MZScanntechConfigServ configServ) {

        CloseableHttpResponse response = null;

        try{

            int timeout = 120;
            if (this.scanntechConfig.getTimeout() > 0){
                timeout = this.scanntechConfig.getTimeout();
            }

            String urlRequest = this.scanntechConfig.getURL() + this.scanntechConfig.getMetodoPos() + this.parseServicio(configServ.getServicioApiPos());
            String loginCredential = this.scanntechConfig.getUsuarioPos() + ":" + this.scanntechConfig.getClavePos();

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

            // PUT y POST
            if ((configServ.getHttpRequestPos().equalsIgnoreCase(X_Z_ScanntechConfigServ.HTTPREQUESTPOS_POST))
                || (configServ.getHttpRequestPos().equalsIgnoreCase(X_Z_ScanntechConfigServ.HTTPREQUESTPOS_PUT))){

                HttpEntityEnclosingRequestBase request = null;

                if (configServ.getHttpRequestPos().equalsIgnoreCase(X_Z_ScanntechConfigServ.HTTPREQUESTPOS_POST)){
                    request = new HttpPost(urlRequest);
                }
                else if (configServ.getHttpRequestPos().equalsIgnoreCase(X_Z_ScanntechConfigServ.HTTPREQUESTPOS_PUT)){
                    request = new HttpPut(urlRequest);
                }

                this.setURL(request.getURI().toString());
                this.setServicioApiPos(jsonObject.toString());

                StringEntity params = new StringEntity(jsonObject.toString(), "UTF-8");
                request.addHeader("Accept", "application/json");
                request.addHeader("content-type", "application/json");
                request.setEntity(params);

                byte[] encodedAuth = Base64.encodeBase64(loginCredential.getBytes(Charset.forName("ISO-8859-1")));
                String authHeader = "Basic " + new String(encodedAuth);
                request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);

                response = httpClient.execute(request);

            }

            // DELETE
            else if (configServ.getHttpRequestPos().equalsIgnoreCase(X_Z_ScanntechConfigServ.HTTPREQUESTPOS_DELETE)){

                HttpDelete request = new HttpDelete(urlRequest);

                request.addHeader("Accept","application/json");
                request.addHeader("content-type", "application/json");

                byte[] encodedAuth = Base64.encodeBase64(loginCredential.getBytes(Charset.forName("ISO-8859-1")));
                String authHeader = "Basic " + new String(encodedAuth);
                request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);

                response = httpClient.execute(request);
            }

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return response;
    }


    /***
     * Dado un servicio api recibido, reemplaza tags para su ejecución.
     * Xpande. Created by Gabriel Vila on 7/11/17.
     * @param servicioApiPos
     * @return
     */
    private String parseServicio(String servicioApiPos) {

        String cleanServicio = servicioApiPos;
        cleanServicio = cleanServicio.replace("{idEmpresa}", String.valueOf(this.scanntechConfig.getEmpresaPos()));
        return cleanServicio;
    }


    /***
     * Setea y retorna objeto JSON correspondiente al objecto "articulo" del API de Scanntech.
     * Xpande. Created by Gabriel Vila on 7/11/17.
     * @return
     */
    private JSONObject jsonProductCreate() {

        JSONObject jsonObject = new JSONObject();

        try{

            this.errorMessage = null;

            MProduct product = new MProduct(getCtx(), this.getRecord_ID(), get_TrxName());
            if ((product == null) || (product.get_ID() <= 0)){
                this.errorMessage = "No se obtuvo producto con ID : " + this.getRecord_ID();
                return null;
            }

            // Obtiene información de locales
            List<JSONObject> jsonObjectsLocales = new ArrayList<JSONObject>();
            List<MZScanntechConfigOrg> locales = this.scanntechConfig.getLocales();
            for (MZScanntechConfigOrg local: locales){
                JSONObject jsonObjectLocal = new JSONObject();
                jsonObjectLocal.put("codigoLocal", Integer.getInteger(local.getCodigoLocalPos()).intValue());
                jsonObjectsLocales.add(jsonObjectLocal);
            }

            // Setea locales
            jsonObject.put("locales", jsonObjectsLocales);

            // Codigo y descripciones del producto
            jsonObject.put("codigoArticulo", product.getValue());
            jsonObject.put("descipcionReducida", product.getDescription());
            jsonObject.put("descipcion", product.getDescription());


            // Obtengo ultimo codigo de barras del producto (si es que lo tiene)
            MZProductoUPC productoUPC = MZProductoUPC.getByProduct(getCtx(), product.get_ID(), null);
            if ((productoUPC != null) && (productoUPC.get_ID() > 0)){
                // Seteo código de barras
                jsonObject.put("codigoGTIN", productoUPC.getUPC());
            }

            // Obtengo unidad de medida para POS según unidad de medida del producto
            MZScanntechConfigUom configUom = this.scanntechConfig.getConfigUOM(product.getC_UOM_ID());
            if ((configUom == null) || (configUom.get_ID() <= 0)){
                this.errorMessage = "No se obtuvo unidad de medida para POS correspondiente a la unidad de medida del sistema con ID : " + product.getC_UOM_ID();
                return null;
            }

            // Seteo unidad de medida
            jsonObject.put("unidad", configUom.getUnidadMedidaPos());

            // Seteo unidad de peso y venta fraccionada
            MUOM uom = (MUOM) configUom.getC_UOM();
            if (uom.getUOMType().equalsIgnoreCase(X_C_UOM.UOMTYPE_Weigth)){
                jsonObject.put("tipoPeso", "P");
                jsonObject.put("ventaFraccionada", true);
                if (product.get_ValueAsBoolean("EsProductoBalanza")){
                    jsonObject.put("plu", product.getValue());
                }
            }
            else{
                jsonObject.put("tipoPeso", "U");
                jsonObject.put("ventaFraccionada", false);
            }

            // Obtengo tasa de impuesto del producto
            String whereClause = X_C_Tax.COLUMNNAME_C_TaxCategory_ID + " =" + product.getC_TaxCategory_ID() +
                    " AND " +  X_C_Tax.COLUMNNAME_IsDefault + " ='Y' AND IsActive ='Y'";
            int[] taxesIDs = MTax.getAllIDs(I_C_Tax.Table_Name, whereClause, null);
            if (taxesIDs.length <= 0){
                this.errorMessage = "No se obtuvo Tasa de impuesto para el producto con ID : " + product.get_ID();
                return null;
            }
            MTax tax = new MTax(getCtx(), taxesIDs[0], null);

            // Seteo tasa de impuesto venta
            jsonObject.put("ivaVenta", tax.getRate().setScale(2, BigDecimal.ROUND_HALF_UP));

            // Seteo categoria y familia (rubro y familia en el sistema)
            if (product.get_ValueAsInt("Z_ProductoRubro_ID") > 0){
                String rubroPos = DB.getSQLValueString(null, " select codigorubropos from z_productorubro where z_productorubro_id =" + product.get_ValueAsInt("Z_ProductoRubro_ID"));
                if (rubroPos != null){
                    jsonObject.put("codigoCategoria", rubroPos);
                }
                if (product.get_ValueAsInt("Z_ProductoFamilia_ID") > 0){
                    String familiaPos = DB.getSQLValueString(null, " select codigofamiliapos from z_productofamilia where z_productofamilia_id =" + product.get_ValueAsInt("Z_ProductoFamilia_ID"));
                    if (familiaPos != null){
                        jsonObject.put("codigoFamilia", familiaPos);
                    }
                }
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return jsonObject;
    }


    private List<JSONObject> getListaLocales() {

        return null;

    }


}
