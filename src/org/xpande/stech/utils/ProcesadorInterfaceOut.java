package org.xpande.stech.utils;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.xpande.core.model.I_Z_ProductoUPC;
import org.xpande.core.model.MZProductoUPC;
import org.xpande.core.utils.PriceListUtils;
import org.xpande.core.utils.TaxUtils;
import org.xpande.stech.model.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


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
            this.scanntechConfig = MZScanntechConfig.getDefault(ctx, null);

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

        HashMap<Integer, Integer> hashProds = new HashMap<Integer, Integer>();

        try{

            Timestamp fechaHoy = TimeUtil.trunc(new Timestamp(System.currentTimeMillis()), TimeUtil.TRUNC_DAY);

            // Obtengo parametrizacion de Scanntech para la organización de este proceso
            MZScanntechConfigOrg configOrg = scanntechConfig.getOrgConfig(adOrgID);

            // Obtengo y recorro lineas de interface aun no ejecutadas para productos
            List<MZStechInterfaceOut> interfaceOuts = this.getLinesProdsNotExecuted(adOrgID, zComunicacionPosID, processPrices);
            for (MZStechInterfaceOut interfaceOut: interfaceOuts){

                // Si es marca de oferta y su vigencia no es acorde a la fecha actual, no proceso esta marca
                if (interfaceOut.isWithOfferSO()){
                    if ((interfaceOut.getStartDate().after(fechaHoy)) || (interfaceOut.getEndDate().before(fechaHoy))){
                        continue;
                    }
                }

                boolean success = false;
                String errorMessage = null;
                MProduct product = new MProduct(this.ctx, interfaceOut.getRecord_ID(), null);

                MPriceList priceList = null;
                if (interfaceOut.getM_PriceList_ID() > 0){
                    priceList = (MPriceList) interfaceOut.getM_PriceList();
                }
                else{
                    priceList = PriceListUtils.getPriceListByOrg(this.ctx, interfaceOut.getAD_Client_ID(), adOrgID, 142, true, null);
                    if ((priceList == null) || (priceList.get_ID() <= 0)){
                        priceList = PriceListUtils.getPriceListByOrg(this.ctx, interfaceOut.getAD_Client_ID(), adOrgID, 100, true, null);
                    }
                }
                // Precio de venta
                MPriceListVersion priceListVersion = priceList.getPriceListVersion(null);
                MProductPrice productPrice = MProductPrice.get(this.ctx, priceListVersion.get_ID(), product.get_ID(), null);
                if (productPrice == null){
                    throw new AdempiereException("No se obtuvo precio de venta para el producto con ID : " + product.get_ID());
                }
                BigDecimal priceSO = productPrice.getPriceList();

                // Si es marca de producto en oferta, tomo directo el precio de oferta seteado aqui
                if (interfaceOut.isWithOfferSO()){
                    if ((interfaceOut.getPriceSO() == null) || (interfaceOut.getPriceSO().compareTo(Env.ZERO) <= 0)){
                        throw new AdempiereException("No se obtuvo precio de venta de OFERTA para el producto con ID : " + product.get_ID());
                    }
                }
                else{
                    interfaceOut.setPriceSO(priceSO); // Guardo precio de venta obtenido y que será el comunicado al POS
                }

                // Si la marca para este producto es de CREAR,
                if (interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){

                    try{
                        JSONObject jsonProduct = this.getJsonProduct(configOrg, product, interfaceOut.getPriceSO());

                        String messagePOS = this.executeJsonPOST("articulos", jsonProduct, configOrg);

                        if (messagePOS == null){
                            success = true;

                            // Guardo id de producto en hash para luego ver consideración o no de códigos de barras.
                            if (!hashProds.containsKey(interfaceOut.getRecord_ID())){
                                hashProds.put(interfaceOut.getRecord_ID(), interfaceOut.getRecord_ID());
                            }

                        }
                        else{
                            success = false;
                            errorMessage = messagePOS;
                        }

                    }
                    catch (Exception e){
                        // Hubo error en el proceso de envio de info al POS para este producto
                        // Marco linea con error y no ejecutada
                        errorMessage = e.getMessage();
                        success = false;
                    }

                }
                // Si la marca para este producto es de UPDATE
                else if (interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_UPDATE)){

                }
                // Si la marca para este producto es de DELETE
                else if (interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){

                }

                // Marco linea con resultado del envío
                interfaceOut.setIsExecuted(success);
                if (errorMessage != null){
                    interfaceOut.setErrorMsg(errorMessage);
                }
                interfaceOut.setDateExecuted(new Timestamp(System.currentTimeMillis()));
                if (zComunicacionPosID > 0){
                    interfaceOut.setZ_ComunicacionPOS_ID(zComunicacionPosID);
                }
                interfaceOut.saveEx();
            }

            // Obtengo y recorro lineas de interface aun no ejecutadas para códigos de barra de productos
            interfaceOuts = this.getLinesUPCNotExecuted();
            for (MZStechInterfaceOut interfaceOut: interfaceOuts){

            }

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
     * @param adOrgID
     * @param zComunicacionPosID
     * @param processPrices
     */
    private List<MZStechInterfaceOut> getLinesProdsNotExecuted(int adOrgID, int zComunicacionPosID, boolean processPrices){

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_IsExecuted + " ='N' " +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_AD_Table_ID + " =" + I_M_Product.Table_ID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_AD_OrgTrx_ID + " =" + adOrgID;

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
     * Xpande. Created by Gabriel Vila on 7/24/17.
     * @return
     */
    private List<MZStechInterfaceOut> getLinesUPCNotExecuted(){

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_IsExecuted + " ='N' " +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_AD_Table_ID + " =" + I_Z_ProductoUPC.Table_ID;

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


    /***
     * Obtiene objecto JSON correspondiente a un determinaoo producto y organización recibibos.
     * Xpande. Created by Gabriel Vila on 2/11/18.
     * @param configOrg
     * @param product
     * @param priceSO
     * @return
     */
    private JSONObject getJsonProduct(MZScanntechConfigOrg configOrg, MProduct product, BigDecimal priceSO) {

        JSONObject jsonProduct = new JSONObject();

        try {

            // Local al cual aplica este proceso
            List<JSONObject> jsonLocalesList = new ArrayList<JSONObject>();
            JSONObject jsonLocal = new JSONObject();
            jsonLocal.put("codigoLocal", Integer.valueOf(configOrg.getCodigoLocalPos()));
            jsonLocalesList.add(jsonLocal);
            jsonProduct.put("locales", jsonLocalesList);

            // Codigo interno
            jsonProduct.put("codigoArticulo", product.getValue());

            // Nombre corto (max.30 caracteres)
            String nombreCorto = product.getDescription().trim();
            if (nombreCorto.length() > 30){
                nombreCorto = nombreCorto.substring(0, 30);
            }
            jsonProduct.put("descipcionReducida", nombreCorto);

            // Nombre largo (max.60 caracteres)
            String nombreLargo = product.getName().trim();
            if (nombreLargo.length() > 60){
                nombreLargo = nombreLargo.substring(0, 60);
            }
            jsonProduct.put("descipcion", nombreLargo);


            // Unidad de medida y atributos asociados
            String unidad = "Un";
            String tipoPeso = "U";
            boolean ventaFraccionada = false;

            MUOM uom = (MUOM) product.getC_UOM();
            if (uom != null){
                if (uom.getUOMSymbol().equalsIgnoreCase("Kg")){
                    unidad = "Kg";
                    tipoPeso = "P";
                    ventaFraccionada = true;
                }
                else if (uom.getUOMSymbol().equalsIgnoreCase("Ltr")){
                    unidad = "Litro";
                }
            }
            jsonProduct.put("unidad", unidad);
            jsonProduct.put("tipoPeso", tipoPeso);
            jsonProduct.put("ventaFraccionada", ventaFraccionada);


            // Balanza y atributos asociados
            boolean productoBalanza = product.get_ValueAsBoolean("EsProductoBalanza");
            int codigoPesable = 0;

            if (productoBalanza) {

                String codigoBalanza = product.getValue().trim();
                String codProdFormat = String.format("%%0%dd", 5);
                codigoBalanza = String.format(codProdFormat, Integer.parseInt(codigoBalanza));

                codigoPesable = Integer.valueOf(codigoBalanza);
                if (codigoBalanza.length() > 5) {
                    if (codigoBalanza.length() == 6 && codigoBalanza.startsWith("0")) {
                        codigoPesable = Integer.valueOf(codigoBalanza);
                    }
                }
            }
            jsonProduct.put("usaBalanza", productoBalanza);
            jsonProduct.put("plu", codigoPesable);

            // Jerarquia del producto
            jsonProduct.put("codigoCategoria", 1); // Sección
            jsonProduct.put("codigoFamilia", 1); // Rubro

            // Precio, descuentos, impuestos
            if (product.getC_TaxCategory_ID() <= 0){
                throw new AdempiereException("Producto no tiene Impuesto Asociado");
            }
            MTax tax = TaxUtils.getLastTaxByCategory(this.ctx, product.getC_TaxCategory_ID(), null);
            if ((tax == null) || (tax.get_ID() <= 0)){
                throw new AdempiereException("No hay Impuesto asociado a la Categoría de Impuesto del Producto");
            }
            jsonProduct.put("ivaVenta", tax.getRate());

            jsonProduct.put("precioVenta", priceSO);
            jsonProduct.put("descuento", false);

        }
        catch (Exception e) {
            throw new AdempiereException(e.getMessage());
        }

        return jsonProduct;
    }

    /***
     * Envía información al POS mediante un JSon Object.
     * Xpande. Created by Gabriel Vila on 2/12/18.
     * @param serviceName
     * @param jsonObject
     * @param configOrg
     * @return
     */
    private String executeJsonPOST(String serviceName, JSONObject jsonObject, MZScanntechConfigOrg configOrg){

        String message = null;
        int timeout = 120;

        try{

            String url = this.scanntechConfig.getURL() + "/" + this.scanntechConfig.getMetodoPos() + "/" +
                    configOrg.getCodigoEmpPos().trim() + "/" + serviceName;

            String credentials = this.scanntechConfig.getUsuarioPos().trim() + ":" + this.scanntechConfig.getClavePos().trim();

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();


            HttpPost request = new HttpPost(url);

            /*
            System.out.println("----------------------------------------");
            System.out.println(request.getURI());
            System.out.println(json.toString());
            */

            StringEntity params = new StringEntity(jsonObject.toString(), "UTF-8");
            request.addHeader("Accept", "application/json");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            byte[] encodedAuth = Base64.encodeBase64(credentials.getBytes(Charset.forName("ISO-8859-1")));
            String authHeader = "Basic " + new String(encodedAuth);
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);

            CloseableHttpResponse response = httpClient.execute(request);

            /*
            System.out.println(response.getStatusLine());
            System.out.println(response.getStatusLine().getStatusCode());
            */

            if (response.getStatusLine().getStatusCode() != 200) {
                InputStream inStream = response.getEntity().getContent();
                if (inStream != null){
                    message = new Scanner(inStream).useDelimiter("\\A").next();
                }
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return message;
    }

}
