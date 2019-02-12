package org.xpande.stech.utils;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.eevolution.model.X_C_TaxGroup;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.xpande.core.model.I_Z_ProductoUPC;
import org.xpande.core.model.MZProductoUPC;
import org.xpande.core.utils.DateUtils;
import org.xpande.core.utils.PriceListUtils;
import org.xpande.core.utils.TaxUtils;
import org.xpande.stech.model.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.json.JSONObject;


/**
 * Clase para el proceso de interface de salida de datos desde el sistema hacia Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 2/2/18.
 */
public class ProcesadorInterfaceOut {

    private Properties ctx = null;
    private String trxName = null;
    private final int cantRowsVentaPagina = 100;
    private int cantMovProcesados = 0;

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
            // Obtengo parametrizacion de Scanntech para la organización de este proceso
            MZScanntechConfigOrg configOrg = scanntechConfig.getOrgConfig(adOrgID);

            // Obtengo y recorro lineas de interface aun no ejecutadas para socios de negocio
            List<MZStechInterfaceOut> interfaceOuts = this.getLinesBPartnerNotExecuted(adOrgID);
            for (MZStechInterfaceOut interfaceOut: interfaceOuts){

                boolean success = false;
                String errorMessage = null;

                // Instancio modelo de socio y localización.
                MBPartner partner = new MBPartner(this.ctx, interfaceOut.getRecord_ID(), null);
                MBPartnerLocation[] partnerLocations = partner.getLocations(true);
                MBPartnerLocation partnerLocation = null;
                MLocation location = null;
                if (partnerLocations.length > 0){
                    partnerLocation = partnerLocations[0];
                    if (partnerLocation.getC_Location_ID() > 0){
                        location = (MLocation) partnerLocation.getC_Location();
                    }
                }

                try{
                    String messagePOS = null;

                    if (interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){
                        JSONObject jsonPartner = this.getJsonPartner(configOrg, partner, partnerLocation, location, false);
                        messagePOS = this.executeJsonPOST("clientes", jsonPartner, configOrg);
                    }
                    else if (interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_UPDATE)){
                        JSONObject jsonPartner = this.getJsonPartner(configOrg, partner, partnerLocation, location, false);
                        messagePOS = this.executeJsonPUT("clientes/" + partner.getValue(), jsonPartner, configOrg);
                    }
                    else if (interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){
                        JSONObject jsonPartner = this.getJsonPartner(configOrg, partner, partnerLocation, location, true);
                        messagePOS = this.executeJsonDELETE("clientes/" + partner.getValue(), jsonPartner, configOrg);
                    }
                    else{
                        messagePOS = "Operación CRUD inválida en Marca con ID : " + interfaceOut.get_ID();
                    }

                    if (messagePOS == null){
                        success = true;
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

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        return message;

    }


    /***
     * Obtiene objecto JSON correspondiente a un determinaoo socio de negocio y organización recibibos.
     * Xpande. Created by Gabriel Vila on 2/11/18.
     * @param configOrg
     * @param partner
     * @param partnerLocation
     * @param location
     * @param isDelete
     * @return
     */
    private JSONObject getJsonPartner(MZScanntechConfigOrg configOrg, MBPartner partner, MBPartnerLocation partnerLocation, MLocation location, boolean isDelete) {

        JSONObject jsonPartner = new JSONObject();

        try {

            // Local al cual aplica este proceso
            List<JSONObject> jsonLocalesList = new ArrayList<JSONObject>();
            JSONObject jsonLocal = new JSONObject();
            jsonLocal.put("codigoLocal", Integer.valueOf(configOrg.getCodigoLocalPos()));
            jsonLocalesList.add(jsonLocal);
            jsonPartner.put("locales", jsonLocalesList);

            // Cuando se corresponde a una operación de DELETE, solo debo informar los locales a los cuales se aplica el DELETE
            if (isDelete){
                return jsonPartner;
            }

            // Codigo interno
            jsonPartner.put("codigoCliente", partner.getValue().trim());

            // Nombre (max.100 caracteres)
            String nombre = partner.getName().trim();
            if (nombre.length() > 100){
                nombre = nombre.substring(0, 100);
            }
            jsonPartner.put("nombre", nombre);

            // Tipo y numero de documento
            // Para Scanntech: 0.RUT - 1.CI - 2.C.U.I.T. - 3.PASAPORTE - 4.D.N.I. - 99.OTROS
            String tipoDocumento = "99";
            X_C_TaxGroup taxGroup = (X_C_TaxGroup) partner.getC_TaxGroup();
            if (taxGroup.getValue() != null){
                if (taxGroup.getValue().equalsIgnoreCase("RUT")){
                    tipoDocumento = "0";
                }
                else if (taxGroup.getValue().equalsIgnoreCase("CI")){
                    tipoDocumento = "1";
                }
            }
            jsonPartner.put("tipoDocumento", tipoDocumento);
            jsonPartner.put("numeroDocumento", partner.getTaxID().trim());

            // Datos geográficos
            String calle = location.getAddress1();
            if ((calle == null) || (calle.trim().equalsIgnoreCase(""))){
                calle = "SIN INFORMACION";
            }
            if (calle.length() > 50){
                calle = calle.substring(0, 50);
            }
            String nroPuerta = location.getAddress2();
            if ((nroPuerta == null) || (nroPuerta.trim().equalsIgnoreCase(""))){
                nroPuerta = "0000";
            }
            if (nroPuerta.length() > 5){
                nroPuerta = nroPuerta.substring(0, 5);
            }
            String codPostal = location.getPostal();
            if ((codPostal == null) || (codPostal.trim().equalsIgnoreCase(""))){
                codPostal = "00000";
            }
            if (codPostal.length() > 5){
                codPostal = codPostal.substring(0, 5);
            }
            String mail = partner.get_ValueAsString("EMail");
            if ((mail == null) || (mail.trim().equalsIgnoreCase(""))){
                mail = "SIN INFORMACION";
            }
            if (mail.length() > 100){
                mail = mail.substring(0, 100);
            }
            String telefono = partnerLocation.getPhone();
            if ((telefono == null) || (telefono.trim().equalsIgnoreCase(""))){
                telefono = "SIN INFORMACION";
            }
            if (telefono.length() > 60){
                telefono = telefono.substring(0, 60);
            }

            jsonPartner.put("calle", calle);
            jsonPartner.put("numeroPuerta", nroPuerta);
            jsonPartner.put("barrio", "BARRIO");
            jsonPartner.put("codigoPostal", codPostal);
            jsonPartner.put("mail", mail);
            jsonPartner.put("telefono", telefono);

        }
        catch (Exception e) {
            throw new AdempiereException(e.getMessage());
        }

        return jsonPartner;
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
        String action = "";

        HashMap<Integer, Integer> hashProds = new HashMap<Integer, Integer>();

        try{

            // Obtengo parametrizacion de Scanntech para la organización de este proceso
            MZScanntechConfigOrg configOrg = scanntechConfig.getOrgConfig(adOrgID);

            // Obtengo y recorro lineas de interface aun no ejecutadas para productos
            List<MZStechInterfaceOut> interfaceOuts = this.getLinesProdsNotExecuted(adOrgID, zComunicacionPosID, processPrices);
            for (MZStechInterfaceOut interfaceOut: interfaceOuts){

                this.executeInterfaceOutProduct(interfaceOut, zComunicacionPosID, processPrices, configOrg);

                if (interfaceOut.isExecuted()){
                    // Guardo id de producto en hash para luego ver consideración o no de códigos de barras.
                    if (!hashProds.containsKey(interfaceOut.getRecord_ID())){
                        hashProds.put(interfaceOut.getRecord_ID(), interfaceOut.getRecord_ID());
                    }
                }
            }

            // Obtengo y recorro lineas de interface aun no ejecutadas para códigos de barra de productos
            interfaceOuts = this.getLinesUPCNotExecuted();
            for (MZStechInterfaceOut interfaceOut: interfaceOuts){

                this.executeInterfaceOutUpc(interfaceOut, zComunicacionPosID, processPrices, configOrg, hashProds);

            }

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        return message;

    }

    /***
     * Ejecuta interface de una marca de producto recibida.
     * Xpande. Created by Gabriel Vila on 8/8/18.
     * @param interfaceOut
     * @param zComunicacionPosID
     * @param processPrices
     * @param configOrg
     */
    public void executeInterfaceOutProduct(MZStechInterfaceOut interfaceOut, int zComunicacionPosID, boolean processPrices,
                                           MZScanntechConfigOrg configOrg) {
        String action = "";

        try{

            if (this.scanntechConfig == null){
                // Obtengo configurador de scanntech
                this.scanntechConfig = MZScanntechConfig.getDefault(ctx, null);
            }

            Timestamp fechaHoy = TimeUtil.trunc(new Timestamp(System.currentTimeMillis()), TimeUtil.TRUNC_DAY);

            // Si es marca de oferta y su vigencia no es acorde a la fecha actual, no proceso esta marca
            if (interfaceOut.isWithOfferSO()){
                if ((interfaceOut.getStartDate().after(fechaHoy)) || (interfaceOut.getEndDate().before(fechaHoy))){
                    return;
                }
            }

            boolean success = false;
            String errorMessage = null;
            MProduct product = new MProduct(this.ctx, interfaceOut.getRecord_ID(), null);

            // Si no es una operación de Delete
            if (!interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){

                // Obtengo el precio de venta del producto.
                MPriceList priceList = null;
                if (interfaceOut.getM_PriceList_ID() > 0){
                    priceList = (MPriceList) interfaceOut.getM_PriceList();
                }
                else{
                    priceList = PriceListUtils.getPriceListByOrg(this.ctx, interfaceOut.getAD_Client_ID(), configOrg.getAD_OrgTrx_ID(), 142, true, null);
                    if ((priceList == null) || (priceList.get_ID() <= 0)){
                        priceList = PriceListUtils.getPriceListByOrg(this.ctx, interfaceOut.getAD_Client_ID(), configOrg.getAD_OrgTrx_ID(), 100, true, null);
                    }
                }
                // Precio de venta
                MPriceListVersion priceListVersion = priceList.getPriceListVersion(null);
                MProductPrice productPrice = MProductPrice.get(this.ctx, priceListVersion.get_ID(), product.get_ID(), null);
                if (productPrice == null){
                    interfaceOut.setIsExecuted(true);
                    interfaceOut.setDescription("No se obtuvo precio de venta para el producto con código interno : " + product.getValue());
                    interfaceOut.saveEx();
                    return;
                    //throw new AdempiereException("No se obtuvo precio de venta para el producto con código interno : " + product.getValue());
                }
                BigDecimal priceSO = productPrice.getPriceList();

                // Si es marca de producto en oferta, tomo directo el precio de oferta seteado aqui
                if (interfaceOut.isWithOfferSO()){
                    if ((interfaceOut.getPriceSO() == null) || (interfaceOut.getPriceSO().compareTo(Env.ZERO) <= 0)){
                        interfaceOut.setIsExecuted(true);
                        interfaceOut.setDescription("No se obtuvo precio de venta de OFERTA para el producto con código interno : " + product.getValue());
                        interfaceOut.saveEx();
                        return;

                        //throw new AdempiereException("No se obtuvo precio de venta de OFERTA para el producto con código interno : " + product.getValue());
                    }
                }
                else{
                    interfaceOut.setPriceSO(priceSO); // Guardo precio de venta obtenido y que será el comunicado al POS
                }
            }

            try{
                String messagePOS = null;

                if (interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){
                    JSONObject jsonProduct = this.getJsonProduct(configOrg, product, interfaceOut.getPriceSO(), false, false);
                    messagePOS = this.executeJsonPOST("articulos", jsonProduct, configOrg);
                }
                else if (interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_UPDATE)){

                    boolean updatePrecioVta = interfaceOut.isPriceChanged();

                    // Mando false como modificacion sola de precio, por las dudas que ademas del precio se hayan modificado otros atributos
                    // del producto.
                    JSONObject jsonProduct = this.getJsonProduct(configOrg, product, interfaceOut.getPriceSO(), false, false);

                        /*
                        // Por ahora mando modificacion total del producto por las dudas. Este codigo servira luego para afinar cuando solo es modificacion de precio
                        if (updatePrecioVta){
                            messagePOS = this.executeJsonPUT("articulos/" + product.getValue().trim() + "/precios", jsonProduct, configOrg);
                        }
                        else{
                            messagePOS = this.executeJsonPUT("articulos/" + product.getValue(), jsonProduct, configOrg);
                        }
                        */
                    messagePOS = this.executeJsonPUT("articulos/" + product.getValue(), jsonProduct, configOrg);

                }
                else if (interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){

                    JSONObject jsonProduct = this.getJsonProduct(configOrg, product, interfaceOut.getPriceSO(), true, false);
                    messagePOS = this.executeJsonDELETE("articulos/" + product.getValue(), jsonProduct, configOrg);

                }
                else{
                    messagePOS = "Operación CRUD inválida en Marca con ID : " + interfaceOut.get_ID();
                }

                if (messagePOS == null){
                    success = true;

                    // Guardo auditoria de Comunicacion para organización - producto.
                    MZStechProdOrgCom stechProdOrgCom = new MZStechProdOrgCom(this.ctx, 0, this.trxName);
                    stechProdOrgCom.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());
                    stechProdOrgCom.setCRUDType(interfaceOut.getCRUDType());
                    stechProdOrgCom.setDateDoc(new Timestamp(System.currentTimeMillis()));
                    stechProdOrgCom.setM_Product_ID(product.get_ID());
                    stechProdOrgCom.setPriceSO(interfaceOut.getPriceSO());

                    if (zComunicacionPosID > 0){
                        stechProdOrgCom.setZ_ComunicacionPOS_ID(zComunicacionPosID);
                    }
                    stechProdOrgCom.setZ_StechInterfaceOut_ID(interfaceOut.get_ID());
                    stechProdOrgCom.saveEx();

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
        catch (Exception e){
            throw new AdempiereException(e);
        }
    }


    /***
     * Ejecuta interface de una marca de codigo de barra recibida.
     * Xpande. Created by Gabriel Vila on 8/8/18.
     * @param interfaceOut
     * @param zComunicacionPosID
     * @param processPrices
     * @param configOrg
     * @param hashProds
     */
    public void executeInterfaceOutUpc(MZStechInterfaceOut interfaceOut, int zComunicacionPosID, boolean processPrices,
                                       MZScanntechConfigOrg configOrg, HashMap<Integer, Integer> hashProds) {

        try{
            boolean success = false;
            String errorMessage = null;

            if (this.scanntechConfig == null){
                // Obtengo configurador de scanntech
                this.scanntechConfig = MZScanntechConfig.getDefault(ctx, null);
            }

            try{
                String messagePOS = null;

                // Si es marca de create
                if (interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){

                    // Cuando creo un UPC, el Record_ID de la marca = ID de la tabla de codigos de barra
                    MZProductoUPC productoUPC = new MZProductoUPC(this.ctx, interfaceOut.getRecord_ID(), this.trxName);
                    MProduct product = (MProduct) productoUPC.getM_Product();

                    boolean comunicadoPOS = MZStechProdOrgCom.isComunicadoPOS(this.ctx, product.get_ID(), configOrg.getAD_OrgTrx_ID(), this.trxName);

                    // Si estoy en la opcion de no procesar cambios de precios
                    if (!processPrices){

                        // Debo verificar que el producto asociado a este codigo de barras, haya sido comunicado alguna vez al pos.
                        if (!comunicadoPOS){
                            return;
                        }
                    }
                    else{
                        // Estoy comunicando precios
                        // Si el producto no fue comunicado nunca al pos
                        if (!comunicadoPOS){
                            // Si el producto no esta siendo comunicado en este proceso
                            if (!hashProds.containsKey(product.get_ID())){
                                // No comunico esta barra
                                return;
                            }
                        }
                    }

                    JSONObject jsonUpc = this.getJsonUpc(product, productoUPC.getUPC().trim(), configOrg.getCodigoEmpPos());
                    messagePOS = this.executeJsonPOST("articulos/" + product.getValue().trim() + "/barras", jsonUpc, configOrg);

                }
                else if (interfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){

                    // Cuando elimino un UPC, el Record_ID de la marca = ID del producto de este upc
                    MProduct product = new MProduct(this.ctx, interfaceOut.getRecord_ID(), this.trxName);

                    messagePOS = this.executeJsonDELETE("articulos/" + product.getValue().trim() + "/barras/" + interfaceOut.getDescription().trim(), configOrg);

                }
                else{
                    messagePOS = "Operación CRUD inválida en Marca con ID : " + interfaceOut.get_ID();
                }

                if (messagePOS == null){
                    success = true;
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
        catch (Exception e){
            throw new AdempiereException(e);
        }
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
    private List<MZStechInterfaceOut> getLinesBPartnerNotExecuted(int adOrgID){

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_IsExecuted + " ='N' " +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_AD_Table_ID + " =" + I_C_BPartner.Table_ID +
                " AND " + X_Z_StechInterfaceOut.COLUMNNAME_AD_OrgTrx_ID + " =" + adOrgID;

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

        String whereClause = X_Z_StechInterfaceOut.COLUMNNAME_Z_ComunicacionPOS_ID + " =" + zComunicacionPosID;

        List<MZStechInterfaceOut> lines = new Query(this.ctx, I_Z_StechInterfaceOut.Table_Name, whereClause, this.trxName).list();

        return lines;

    }


    /***
     * Obtiene objecto JSON correspondiente a un determinaoo producto y organización recibibos.
     * Xpande. Created by Gabriel Vila on 2/11/18.
     * @param configOrg
     * @param product
     * @param priceSO
     * @param isDelete
     * @param updatePrecioVta
     * @return
     */
    private JSONObject getJsonProduct(MZScanntechConfigOrg configOrg, MProduct product, BigDecimal priceSO, boolean isDelete, boolean updatePrecioVta) {

        JSONObject jsonProduct = new JSONObject();
        String sql = "";

        try {

            // Local al cual aplica este proceso
            List<JSONObject> jsonLocalesList = new ArrayList<JSONObject>();
            JSONObject jsonLocal = new JSONObject();
            jsonLocal.put("codigoLocal", Integer.valueOf(configOrg.getCodigoLocalPos()));
            jsonLocalesList.add(jsonLocal);
            jsonProduct.put("locales", jsonLocalesList);

            // Cuando se corresponde a una operación de DELETE del producto, solo debo informar los locales a los cuales se aplica el DELETE
            if (isDelete){
                return jsonProduct;
            }

            // Codigo interno
            jsonProduct.put("codigoArticulo", product.getValue());

            // Si es solamente una modificación del precio
            if (updatePrecioVta){
                jsonProduct.put("precioVenta", priceSO);
                return jsonProduct;
            }

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
            jsonProduct.put("exportable", productoBalanza);
            jsonProduct.put("plu", codigoPesable);

            // Jerarquias del producto
            // Rubro
            if (product.get_ValueAsInt("Z_ProductoRubro_ID") > 0){
                sql = " select cast(codigopos as numeric(10,0)) from z_productorubro where z_productorubro_id =" + product.get_ValueAsInt("Z_ProductoRubro_ID");
                int codigoPos = DB.getSQLValueEx(null, sql);
                if (codigoPos > 0){
                    jsonProduct.put("codigoCategoria", codigoPos); // Rubro
                }
            }

            // Familia
            if (product.get_ValueAsInt("Z_ProductoFamilia_ID") > 0){
                sql = " select cast(codigopos as numeric(10,0)) from z_productofamilia where z_productofamilia_id =" + product.get_ValueAsInt("Z_ProductoFamilia_ID");
                int codigoPos = DB.getSQLValueEx(null, sql);
                if (codigoPos > 0){
                    jsonProduct.put("codigoFamilia", codigoPos); // Familia
                }
            }
            // SubFamilia
            if (product.get_ValueAsInt("Z_ProductoSubfamilia_ID") > 0){
                sql = " select cast(codigopos as numeric(10,0)) from z_productosubfamilia where z_productosubfamilia_id =" + product.get_ValueAsInt("Z_ProductoSubfamilia_ID");
                int codigoSubFamiliaPos = DB.getSQLValueEx(null, sql);
                if (codigoSubFamiliaPos > 0){
                    jsonProduct.put("codigoSubFamilia", codigoSubFamiliaPos); // Familia
                }
            }

            // Producto tandem asociada en caso de tenerlo
            if (product.get_ValueAsInt("M_Product_Tandem_ID") > 0){
                MProduct prodTandem = new MProduct(ctx, product.get_ValueAsInt("M_Product_Tandem_ID"), null);
                if ((prodTandem != null) && (prodTandem.get_ID() > 0)){
                    jsonProduct.put("codigoEnvase", prodTandem.getValue());
                }
            }

            // Precio, descuentos, impuestos
            if (product.getC_TaxCategory_ID() <= 0){
                throw new AdempiereException("Producto no tiene Impuesto Asociado");
            }


            // Codigo IVA.
            // Por defacto el IVA normal de venta
            int cTaxCategoryID = product.getC_TaxCategory_ID();

            // Verifico si este producto-organización no tiene impuesto diferencial de venta y/o venta a contribuyentes
            sql = " select c_taxcategory_id " +
                    " from z_productotaxorg " +
                    " where m_product_id =" + product.get_ID() +
                    " and ad_orgtrx_id =" + configOrg.getAD_OrgTrx_ID() +
                    " and isactive ='Y' ";
            int taxCategoryID_Aux = DB.getSQLValueEx(null, sql);
            if (taxCategoryID_Aux > 0){
                cTaxCategoryID = taxCategoryID_Aux;
            }

            MTax tax = TaxUtils.getLastTaxByCategory(this.ctx, cTaxCategoryID, null);
            if ((tax == null) || (tax.get_ID() <= 0)){
                throw new AdempiereException("No hay Impuesto asociado a la Categoría de Impuesto del Producto");
            }
            jsonProduct.put("ivaVenta", tax.getRate());

            jsonProduct.put("precioVenta", priceSO);

            boolean esBonificable = product.get_ValueAsBoolean("IsBonificable");
            jsonProduct.put("descuento", esBonificable);

        }
        catch (Exception e) {
            throw new AdempiereException(e.getMessage());
        }

        return jsonProduct;
    }


    /***
     * Obtiene objecto JSON correspondiente a un determinaoo código de barras recibido.
     * Xpande. Created by Gabriel Vila on 2/11/18.
     * @param product
     * @param upc
     * @param codigoEmpresa
     * @return
     */
    private JSONObject getJsonUpc(MProduct product, String upc, String codigoEmpresa) {

        JSONObject jsonUpc = new JSONObject();

        try{

            jsonUpc.put("codigoEmpresa", codigoEmpresa);
            jsonUpc.put("codigoArticulo", product.getValue().trim());
            jsonUpc.put("codigoGtin", upc);

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return jsonUpc;
    }





        /***
         * Envía información al POS mediante un JSon Object. POST.
         * Xpande. Created by Gabriel Vila on 2/12/18.
         * @param serviceName
         * @param jsonObject
         * @param configOrg
         * @return
         */
    private String executeJsonPOST(String serviceName, JSONObject jsonObject, MZScanntechConfigOrg configOrg){

        String message = null;
        int timeout = 120;

        CloseableHttpClient httpClient = null;

        try{

            //String url = this.scanntechConfig.getURL() + "/" + this.scanntechConfig.getMetodoPos() + "/" + configOrg.getCodigoEmpPos().trim() + "/" + serviceName;

            String url = this.scanntechConfig.getURL() + this.scanntechConfig.getMetodoPos() + "/" + configOrg.getCodigoEmpPos().trim() + "/" + serviceName;

            String credentials = this.scanntechConfig.getUsuarioPos().trim() + ":" + this.scanntechConfig.getClavePos().trim();

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();


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
        finally {
            if (httpClient != null){
                try{
                    httpClient.close();
                }
                catch (Exception e){
                }
            }
        }

        return message;
    }



    /***
     * Envía información al POS mediante un JSon Object. PUT.
     * Xpande. Created by Gabriel Vila on 2/12/18.
     * @param serviceName
     * @param jsonObject
     * @param configOrg
     * @return
     */
    private String executeJsonPUT(String serviceName, JSONObject jsonObject, MZScanntechConfigOrg configOrg){

        String message = null;
        int timeout = 120;

        try{

            String url = this.scanntechConfig.getURL() + this.scanntechConfig.getMetodoPos() + "/" +
                    configOrg.getCodigoEmpPos().trim() + "/" + serviceName;

            String credentials = this.scanntechConfig.getUsuarioPos().trim() + ":" + this.scanntechConfig.getClavePos().trim();

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();


            HttpPut request = new HttpPut(url);

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


    /***
     * Envía información al POS mediante un JSon Object. DELETE.
     * Xpande. Created by Gabriel Vila on 2/12/18.
     * @param serviceName
     * @param jsonObject
     * @param configOrg
     * @return
     */
    private String executeJsonDELETE(String serviceName, JSONObject jsonObject, MZScanntechConfigOrg configOrg){

        String message = null;
        int timeout = 120;

        try{

            String url = this.scanntechConfig.getURL() + this.scanntechConfig.getMetodoPos() + "/" +
                    configOrg.getCodigoEmpPos().trim() + "/" + serviceName;

            String credentials = this.scanntechConfig.getUsuarioPos().trim() + ":" + this.scanntechConfig.getClavePos().trim();

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();


            CustomHttpDelete request = new CustomHttpDelete(url);

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


    /***
     * Envía información al POS mediante un JSon Object. DELETE.
     * Xpande. Created by Gabriel Vila on 2/12/18.
     * @param serviceName
     * @param configOrg
     * @return
     */
    private String executeJsonDELETE(String serviceName, MZScanntechConfigOrg configOrg){

        String message = null;
        int timeout = 120;

        try{

            String url = this.scanntechConfig.getURL() + this.scanntechConfig.getMetodoPos() + "/" +
                    configOrg.getCodigoEmpPos().trim() + "/" + serviceName;

            String credentials = this.scanntechConfig.getUsuarioPos().trim() + ":" + this.scanntechConfig.getClavePos().trim();

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();


            HttpDelete request = new HttpDelete(url);

            /*
            System.out.println("----------------------------------------");
            System.out.println(request.getURI());
            System.out.println(json.toString());
            */
            request.addHeader("Accept", "application/json");
            request.addHeader("content-type", "application/json");

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


    /***
     * Ejecuta el proceso de interface de ventas desde el POS Scanntech hacia ADempiere.
     * Xpande. Created by Gabriel Vila on 1/16/19.
     * @param configOrg
     * @param fechaConsulta
     * @return
     */
    public String executeInterfaceMov(MZScanntechConfigOrg configOrg, Timestamp fechaConsulta) {

        String message = null;

        try{

            // Obtengo configurador de scanntech
            this.scanntechConfig = MZScanntechConfig.getDefault(ctx, null);

            // Formatos requeridos por Scanntech de fecha desde y hasta para obtener movimientos.
            Timestamp fechaConsultaAuxDesde = new Timestamp(fechaConsulta.getTime());
            Timestamp fechaConsultaAuxHasta = new Timestamp(fechaConsulta.getTime());
            Timestamp fechaDesde = DateUtils.getTSManualHour(fechaConsultaAuxDesde, 0, 0, 0, 0);
            Timestamp fechaHasta = DateUtils.getTSManualHour(fechaConsultaAuxHasta, 23, 59, 59, 0);

            String fechaAux = fechaConsulta.toString();
            String fechaConsultaSTR = fechaAux.substring(0, 10);

            fechaAux = fechaDesde.toString();
            String fechaDesdeSTR = fechaAux.substring(0, 10) + "T" + fechaAux.substring(11) + "00-0300";

            fechaAux = fechaHasta.toString();
            String fechaHastaSTR = fechaAux.substring(0, 10) + "T" + fechaAux.substring(11) + "00-0300";

            String serviceText = "";

            // Recorro cajas de esta organización para obtener movimientos de cada una de ellas.
            List<MZScanntechConfigCaja> configCajaList = configOrg.getCajas();

            if (configCajaList.size() <= 0){
                return "No hay cajas definidas para procesar.";
            }

            // Genero nuevo cabezal de proceso de interface de ventas
            MZStechInterfaceVta interfaceVta = new MZStechInterfaceVta(this.ctx, 0, this.trxName);
            interfaceVta.set_ValueOfColumn("AD_Client_ID", configOrg.getAD_Client_ID());
            interfaceVta.setAD_Org_ID(configOrg.getAD_OrgTrx_ID());
            interfaceVta.setDateTrx(fechaConsulta);
            interfaceVta.setDateFrom(fechaDesde);
            interfaceVta.setDateTo(fechaHasta);
            interfaceVta.setStartDate(new Timestamp(System.currentTimeMillis()));
            interfaceVta.saveEx();

            for(MZScanntechConfigCaja configCaja: configCajaList){

                int pageSize = 0, pageOffSet = 0;

                serviceText ="locales/" + configOrg.getCodigoLocalPos() + "/cajas/" + configCaja.getCodigoPOS() +
                        "/movimientos?fechaConsulta=" + fechaConsultaSTR +
                        "&fechaEnvioDesde=" + fechaDesdeSTR +
                        "&fechaEnvioHasta=" + fechaHastaSTR +
                        "&pageOffset=" + pageOffSet +
                        "&pageSize=" + cantRowsVentaPagina;


                JSONArray jsonArrayMov = this.executeJsonGET(serviceText, configOrg);

                if (jsonArrayMov == null){
                    continue;
                }

                // Obtengo cantidad de registros de la consulta
                pageSize = jsonArrayMov.length();

                // Si no tengo información para esta caja, sigo con otra.
                if (pageSize == 0){
                    continue;
                }

                // Guardo información de movimientos obtenidos.
                message = this.setInfoMovimientosVenta(configOrg, configCaja, interfaceVta, jsonArrayMov);
                if (message != null){
                    throw new AdempiereException(message);
                }

                // Realizo nuevamente consulta paginando mientras haya mas registros que el tamaño de pagina.
                while (pageSize >= this.cantRowsVentaPagina){

                    pageOffSet = pageOffSet + pageSize;

                    serviceText ="locales/" + configOrg.getCodigoLocalPos() + "/cajas/" + configCaja.getCodigoPOS() +
                            "/movimientos?fechaConsulta=" + fechaConsultaSTR +
                            "&fechaEnvioDesde=" + fechaDesdeSTR +
                            "&fechaEnvioHasta=" + fechaHastaSTR +
                            "&pageOffset=" + pageOffSet +
                            "&pageSize=" + cantRowsVentaPagina;


                    jsonArrayMov = this.executeJsonGET(serviceText, configOrg);

                    if (jsonArrayMov == null){
                        pageSize = 0;
                    }
                    else{
                        // Obtengo cantidad de registros de la consulta
                        pageSize = jsonArrayMov.length();
                    }

                    if (pageSize > 0){

                        // Guardo información de movimientos obtenidos.
                        message = this.setInfoMovimientosVenta(configOrg, configCaja, interfaceVta, jsonArrayMov);
                        if (message != null){
                            throw new AdempiereException(message);
                        }
                    }

                }

                /*
                pageSize = resp.length();
                cantTotalMov = pageSize;
                if(0==pageSize) {
                    MRTLogFile log = new MRTLogFile(getCtx(), 0, get_TrxName());
                    log.setDescription("No hay movimientos en la caja "+inIdCaja+" del local "+inIdLocal+" a partir de las "+inFch.toString());
                    log.set_ValueOfColumn("UY_RT_LoadTicket_ID", idMRTLoadTicketID);
                    log.setName("No hay movimientos");
                    log.saveEx();
                    //log.log(Level.SEVERE,pageSize+ " movimientos para el d�a: "+fechaDelDia+", local:  "+inIdLocal+" y caja:  "+inIdCaja);
                    return "OK - 0";//"No hay datos para el d�a: "+fch+",local:"+inIdLocal+" y caja:"+inIdCaja ;
                }

                count += procesarMovimientos(resp,inIdLocal, inIdCaja, idOrg, idMRTLoadTicketID);
                //Realizo la misma consulta salteando p�ginads mientras la cantidad de registros obtenidos sea 100
                while(pageSize>=100){
                    pageOffSet +=pageSize;
                    resp = MRTRetailInterface.enviarMovimientoTimestamp(getCtx(), this.getAD_Client_ID(),//Env.getAD_Client_ID(getCtx()),
                            get_TrxName(), inIdLocal, inIdCaja, inFch,inDesde,inHasta,idEmpresa,pageOffSet);
                    pageSize = resp.length();
                    cantTotalMov += pageSize;
                    if(pageSize>0){
                        count += procesarMovimientos(resp,inIdLocal, inIdCaja, idOrg, idMRTLoadTicketID);
                    }
                }
                if(pageOffSet==0) pageOffSet=pageSize;
                System.out.println("Se leen "+count+", de "+cantTotalMov);
                */

            }

            interfaceVta.setEndDate(new Timestamp(System.currentTimeMillis()));
            interfaceVta.setContadorLineas(cantMovProcesados);
            interfaceVta.saveEx();

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return message;
    }


    /***
     * Guarda información de movimientos obtenidos por interface de ventas.
     * Xpande. Created by Gabriel Vila on 2/12/19.
     * @param configOrg
     * @param configCaja
     * @param interfaceVta
     * @param jsonArrayMov
     * @return
     */
    private String setInfoMovimientosVenta(MZScanntechConfigOrg configOrg, MZScanntechConfigCaja configCaja, MZStechInterfaceVta interfaceVta, JSONArray jsonArrayMov) {

        String message = null;

        try{

            int pageSize = jsonArrayMov.length();

            // Recorro y guardo movimientos obtenidos
            for (int i = 0; i < pageSize; i++){

                System.out.println("Procesando Movimiento " + (i + 1) + " de " + pageSize);

                JSONObject jsonMovimiento = jsonArrayMov.getJSONObject(i);

                MZStechTKMov tkMov = this.setJsonMovimiento(configOrg, configCaja, interfaceVta, jsonMovimiento);

                // Recorro y guardo detalles de este movimiento
                JSONArray jsonArrayDetalles = jsonMovimiento.getJSONArray("detalles");
                if (jsonArrayDetalles != null){
                    for (int j = 0; j < jsonArrayDetalles.length(); j++){

                        JSONObject jsonDetalle = jsonArrayDetalles.getJSONObject(j);

                        MZStechTKMovDet tkMovDet = this.setJsonDetalleMov(tkMov, jsonDetalle);

                        // Recorro y guardo descuentos de este detalle de movimiento
                        JSONArray jsonArrayDetDtos = jsonDetalle.getJSONArray("descuentos");
                        if (jsonArrayDetDtos != null){
                            for (int k = 0; k < jsonArrayDetDtos.length(); k++){

                                JSONObject jsonDetDto = jsonArrayDetDtos.getJSONObject(k);

                                this.setJsonDetalleDto(tkMovDet, jsonDetDto);
                            }
                        }
                    }
                }

                // Recorro y guardo medios de pago de este movimiento
                JSONArray jsonArrayMediosPago = jsonMovimiento.getJSONArray("pagos");
                if (jsonArrayMediosPago != null){
                    for (int k = 0; k < jsonArrayMediosPago.length(); k++){

                        JSONObject jsonMedioPago = jsonArrayMediosPago.getJSONObject(k);

                        this.setJsonMedioPagoMov(tkMov, jsonMedioPago);

                    }
                }
            }

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return message;
    }


    /***
     * Guarda información de medio de pago de un movimiento de interface de venta.
     * Xpande. Created by Gabriel Vila on 2/12/19.
     * @param tkMov
     * @param jsonMedioPago
     */
    private void setJsonMedioPagoMov(MZStechTKMov tkMov, JSONObject jsonMedioPago) {

        MZStechTKMovPago tkMovPago = null;

        try{

            tkMovPago = new MZStechTKMovPago(this.ctx, 0, this.trxName);
            tkMovPago.setZ_Stech_TK_Mov_ID(tkMov.get_ID());
            tkMovPago.setZ_StechInterfaceVta_ID(tkMov.getZ_StechInterfaceVta_ID());
            tkMovPago.setJSonBody(jsonMedioPago.toString());

            // codigoTipoPago
            if (!jsonMedioPago.get("codigoTipoPago").equals(JSONObject.NULL)){
                tkMovPago.setSC_CodigoTipoPago(Integer.valueOf(jsonMedioPago.get("codigoTipoPago").toString().trim()));
            }

            // codigoPlanPagos
            if (!jsonMedioPago.get("codigoPlanPagos").equals(JSONObject.NULL)){
                tkMovPago.setSC_CodigoPlanPagos(Integer.valueOf(jsonMedioPago.get("codigoPlanPagos").toString().trim()));
            }

            // numeroTarjeta
            if (!jsonMedioPago.get("numeroTarjeta").equals(JSONObject.NULL)){
                tkMovPago.setSC_NumeroTarjeta(jsonMedioPago.get("numeroTarjeta").toString().trim());
            }

            // numeroAutorizacion
            if (!jsonMedioPago.get("numeroAutorizacion").equals(JSONObject.NULL)){
                tkMovPago.setSC_NumeroAutorizacion(jsonMedioPago.get("numeroAutorizacion").toString().trim());
            }

            // fechaVencimiento
            if (!jsonMedioPago.get("fechaVencimiento").equals(JSONObject.NULL)){
                String fch = jsonMedioPago.get("fechaVencimiento").toString().trim();
                String fchHra = fch.substring(0, 4)+fch.substring(5, 7)+fch.substring(8, 10)+fch.substring(11, 13)+fch.substring(14, 16)+fch.substring(17, 19);
                Timestamp fchVenc =  DateUtils.convertStringToTimestamp_YYYYMMddHHMMss(fchHra);
                tkMovPago.setSC_FechaVencimiento(fchVenc);
            }

            // codigoMoneda
            if (!jsonMedioPago.get("codigoMoneda").equals(JSONObject.NULL)){
                tkMovPago.setSC_CodigoMoneda(jsonMedioPago.get("codigoMoneda").toString().trim());
            }

            // importe
            if (!jsonMedioPago.get("importe").equals(JSONObject.NULL)){
                tkMovPago.setSC_Importe(BigDecimal.valueOf(jsonMedioPago.getDouble("importe")));
            }

            // cotizacionCompra
            if (!jsonMedioPago.get("cotizacionCompra").equals(JSONObject.NULL)){
                tkMovPago.setSC_CotizacionCompra(BigDecimal.valueOf(jsonMedioPago.getDouble("cotizacionCompra")));
            }

            // cotizacionVenta
            if (!jsonMedioPago.get("cotizacionVenta").equals(JSONObject.NULL)){
                tkMovPago.setSC_CotizacionVenta(BigDecimal.valueOf(jsonMedioPago.getDouble("cotizacionVenta")));
            }

            // codigoCliente
            if (!jsonMedioPago.get("codigoCliente").equals(JSONObject.NULL)){
                tkMovPago.setSC_CodigoCliente(jsonMedioPago.get("codigoCliente").toString().trim());
            }

            // documentoCliente
            if (!jsonMedioPago.get("documentoCliente").equals(JSONObject.NULL)){
                tkMovPago.setSC_DocumentoCliente(jsonMedioPago.get("documentoCliente").toString().trim());
            }

            // numeroDocumentoPago
            if (!jsonMedioPago.get("numeroDocumentoPago").equals(JSONObject.NULL)){
                tkMovPago.setSC_NumeroDocumentoPago(jsonMedioPago.get("numeroDocumentoPago").toString().trim());
            }

            // codigoCredito
            if (!jsonMedioPago.get("codigoCredito").equals(JSONObject.NULL)){
                tkMovPago.setSC_CodigoCredito(Integer.valueOf(jsonMedioPago.get("codigoCredito").toString().trim()));
            }

            // numeroCuotasPago
            if (!jsonMedioPago.get("numeroCuotasPago").equals(JSONObject.NULL)){
                tkMovPago.setSC_NumeroCuotasPago(Integer.valueOf(jsonMedioPago.get("numeroCuotasPago").toString().trim()));
            }

            // terminalCredito
            if (!jsonMedioPago.get("terminalCredito").equals(JSONObject.NULL)){
                tkMovPago.setSC_TerminalCredito(jsonMedioPago.get("terminalCredito").toString().trim());
            }

            // comercioCredito
            if (!jsonMedioPago.get("comercioCredito").equals(JSONObject.NULL)){
                tkMovPago.setSC_ComercioCredito(jsonMedioPago.get("comercioCredito").toString().trim());
            }

            // cambio
            tkMovPago.setSC_Cambio(jsonMedioPago.getBoolean("cambio"));

            // importePago
            if (!jsonMedioPago.get("importePago").equals(JSONObject.NULL)){
                tkMovPago.setSC_ImportePago(BigDecimal.valueOf(jsonMedioPago.getDouble("importePago")));
            }

            tkMovPago.saveEx();

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
    }


    /***
     * Guarda información de un descuento de un detalle de movimiento de interface de venta
     * Xpande. Created by Gabriel Vila on 2/12/19.
     * @param tkMovDet
     * @param jsonDetDto
     */
    private void setJsonDetalleDto(MZStechTKMovDet tkMovDet, JSONObject jsonDetDto) {

        MZStechTKMovDetDtos tkMovDetDtos = null;

        try{

            tkMovDetDtos = new MZStechTKMovDetDtos(this.ctx, 0, this.trxName);
            tkMovDetDtos.setZ_Stech_TK_Mov_ID(tkMovDet.getZ_Stech_TK_Mov_ID());
            tkMovDetDtos.setZ_Stech_TK_MovDet_ID(tkMovDet.get_ID());
            tkMovDetDtos.setZ_StechInterfaceVta_ID(tkMovDet.getZ_StechInterfaceVta_ID());
            tkMovDetDtos.setJSonBody(jsonDetDto.toString());

            // idDescuento
            if (!jsonDetDto.get("idDescuento").equals(JSONObject.NULL)){
                tkMovDetDtos.setSC_IdDescuento(Integer.valueOf(jsonDetDto.get("idDescuento").toString().trim()));
            }

            // importeDescuento
            if (!jsonDetDto.get("importeDescuento").equals(JSONObject.NULL)){
                tkMovDetDtos.setSC_ImporteDescuento(BigDecimal.valueOf(jsonDetDto.getDouble("importeDescuento")));
            }

            // tipoDescuento
            if (!jsonDetDto.get("tipoDescuento").equals(JSONObject.NULL)){
                tkMovDetDtos.setSC_TipoDescuento(jsonDetDto.get("tipoDescuento").toString().trim());
            }

            if (!jsonDetDto.get("idPromocion").equals(JSONObject.NULL)){
                tkMovDetDtos.setSC_IdPromocion(jsonDetDto.get("idPromocion").toString().trim());
            }

            tkMovDetDtos.saveEx();
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
    }


    /***
     * Guarda información de un movimiento de interface de venta.
     * Xpande. Created by Gabriel Vila on 2/12/19.
     * @param configOrg
     * @param configCaja
     * @param interfaceVta
     * @param jsonMovimiento
     * @return
     */
    private MZStechTKMov setJsonMovimiento(MZScanntechConfigOrg configOrg, MZScanntechConfigCaja configCaja, MZStechInterfaceVta interfaceVta, JSONObject jsonMovimiento) {

        MZStechTKMov tkMov = null;

        try{

            tkMov = new MZStechTKMov(this.ctx, 0, this.trxName);
            tkMov.setZ_StechInterfaceVta_ID(interfaceVta.get_ID());
            tkMov.setJSonBody(jsonMovimiento.toString());

            // numeroMov
            if (!jsonMovimiento.get("numeroMov").equals(JSONObject.NULL)){
                tkMov.setSC_NumeroMov(jsonMovimiento.get("numeroMov").toString().trim());
            }

            // codigoEmpresa
            tkMov.setSC_CodigoEmpresa(Integer.valueOf(configOrg.getCodigoEmpPos().trim()));
            if (!jsonMovimiento.get("codigoEmpresa").equals(JSONObject.NULL)){
                tkMov.setSC_CodigoEmpresa(Integer.valueOf(jsonMovimiento.get("codigoEmpresa").toString().trim()));
            }

            // codigoLocal
            tkMov.setSC_CodigoLocal(Integer.valueOf(configOrg.getCodigoLocalPos()));
            if (!jsonMovimiento.get("codigoLocal").equals(JSONObject.NULL)){
                tkMov.setSC_CodigoLocal(Integer.valueOf(jsonMovimiento.get("codigoLocal").toString().trim()));
            }

            // codigoCaja
            tkMov.setSC_CodigoCaja(Integer.valueOf(configCaja.getCodigoPOS()));
            if (!jsonMovimiento.get("codigoCaja").equals(JSONObject.NULL)){
                tkMov.setSC_CodigoCaja(Integer.valueOf(jsonMovimiento.get("codigoCaja").toString().trim()));
            }

            // fechaOperacion
            String fch = jsonMovimiento.getString("fechaOperacion");   //"fechaOperacion":"2015-03-17T00:00:00.000-0300"
            String fchHra = fch.substring(0, 4)+fch.substring(5, 7)+fch.substring(8, 10)+fch.substring(11, 13)+fch.substring(14, 16)+fch.substring(17, 19);
            Timestamp fchOperacion =  DateUtils.convertStringToTimestamp_YYYYMMddHHMMss(fchHra);
            tkMov.setSC_FechaOperacion(fchOperacion);

            // tipoOperacion
            if (!jsonMovimiento.get("tipoOperacion").equals(JSONObject.NULL)){
                tkMov.setSC_TipoOperacion(jsonMovimiento.get("tipoOperacion").toString().trim());
            }

            // tipoCfe
            if (!jsonMovimiento.get("tipoCfe").equals(JSONObject.NULL)){
                tkMov.setSC_TipoCfe(Integer.valueOf(jsonMovimiento.get("tipoCfe").toString().trim()));
            }

            // serieCfe
            if (!jsonMovimiento.get("serieCfe").equals(JSONObject.NULL)){
                tkMov.setSC_SerieCfe(jsonMovimiento.get("serieCfe").toString().trim());
            }

            // numeroOperacion
            if (!jsonMovimiento.get("numeroOperacion").equals(JSONObject.NULL)){
                tkMov.setSC_NumeroOperacion(jsonMovimiento.get("numeroOperacion").toString().trim());
            }

            // codigoSeguridadCfe
            if (!jsonMovimiento.get("codigoSeguridadCfe").equals(JSONObject.NULL)){
                tkMov.setSC_CodigoSeguridadCfe(jsonMovimiento.get("codigoSeguridadCfe").toString().trim());
            }

            // descuentoTotal
            if (!jsonMovimiento.get("descuentoTotal").equals(JSONObject.NULL)){
                tkMov.setSC_DescuentoTotal(BigDecimal.valueOf(jsonMovimiento.getDouble("descuentoTotal")));
            }

            // cantidadItems
            if (!jsonMovimiento.get("cantidadItems").equals(JSONObject.NULL)){
                tkMov.setSC_CantidadItems(Integer.valueOf(jsonMovimiento.get("cantidadItems").toString().trim()));
            }

            // codigoMoneda
            if (!jsonMovimiento.get("codigoMoneda").equals(JSONObject.NULL)){
                tkMov.setSC_CodigoMoneda(jsonMovimiento.get("codigoMoneda").toString().trim());
            }

            // total
            if (!jsonMovimiento.get("total").equals(JSONObject.NULL)){
                tkMov.setSC_Total(BigDecimal.valueOf(jsonMovimiento.getDouble("total")));
            }

            // redondeo
            if (!jsonMovimiento.get("redondeo").equals(JSONObject.NULL)){
                tkMov.setSC_Redondeo(BigDecimal.valueOf(jsonMovimiento.getDouble("redondeo")));
            }

            // cotizacionCompra
            if (!jsonMovimiento.get("cotizacionCompra").equals(JSONObject.NULL)){
                tkMov.setSC_CotizacionCompra(BigDecimal.valueOf(jsonMovimiento.getDouble("cotizacionCompra")));
            }

            // cotizacionVenta
            if (!jsonMovimiento.get("cotizacionVenta").equals(JSONObject.NULL)){
                tkMov.setSC_CotizacionVenta(BigDecimal.valueOf(jsonMovimiento.getDouble("cotizacionVenta")));
            }

            // cuponCancelado
            if (!jsonMovimiento.get("cuponCancelado").equals(JSONObject.NULL)){
                tkMov.setSC_CuponCancelado(jsonMovimiento.getBoolean("cuponCancelado"));
            }

            // cuponAnulada
            if (!jsonMovimiento.get("cuponAnulada").equals(JSONObject.NULL)){
                tkMov.setSC_CuponAnulada(jsonMovimiento.getBoolean("cuponAnulada"));
            }

            tkMov.saveEx();

            cantMovProcesados++;

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return tkMov;
    }


    /***
     * Guarda información de un detalle de movimiento de interface de venta
     * Xpande. Created by Gabriel Vila on 2/12/19.
     * @param tkMov
     * @param jsonDetalle
     * @return
     */
    private MZStechTKMovDet setJsonDetalleMov(MZStechTKMov tkMov, JSONObject jsonDetalle) {

        MZStechTKMovDet tkMovDet = null;

        try{

            tkMovDet = new MZStechTKMovDet(this.ctx, 0, this.trxName);
            tkMovDet.setZ_Stech_TK_Mov_ID(tkMov.get_ID());
            tkMovDet.setZ_StechInterfaceVta_ID(tkMov.getZ_StechInterfaceVta_ID());
            tkMovDet.setJSonBody(jsonDetalle.toString());

            // codigoTipoDetalle
            if (!jsonDetalle.get("codigoTipoDetalle").equals(JSONObject.NULL)){
                tkMovDet.setSC_CodigoTipoDetalle(Integer.valueOf(jsonDetalle.get("codigoTipoDetalle").toString().trim()));
            }

            // codigoArticuloPadre
            if (!jsonDetalle.get("codigoArticuloPadre").equals(JSONObject.NULL)){
                tkMovDet.setSC_CodigoArticuloPadre(jsonDetalle.get("codigoArticuloPadre").toString().trim());
            }

            // codigoArticulo
            if (!jsonDetalle.get("codigoArticulo").equals(JSONObject.NULL)){
                tkMovDet.setSC_CodigoArticulo(jsonDetalle.get("codigoArticulo").toString().trim());
            }

            // codigoBarras
            if (!jsonDetalle.get("codigoBarras").equals(JSONObject.NULL)){
                tkMovDet.setSC_CodigoBarras(jsonDetalle.get("codigoBarras").toString().trim());
            }

            // descripcionArticulo
            if (!jsonDetalle.get("descripcionArticulo").equals(JSONObject.NULL)){
                tkMovDet.setSC_DescripcionArticulo(jsonDetalle.get("descripcionArticulo").toString().trim());
            }

            // cantidad
            if (!jsonDetalle.get("cantidad").equals(JSONObject.NULL)){
                tkMovDet.setSC_Cantidad(BigDecimal.valueOf(jsonDetalle.getDouble("cantidad")));
            }

            // importeUnitario
            if (!jsonDetalle.get("importeUnitario").equals(JSONObject.NULL)){
                tkMovDet.setSC_ImporteUnitario(BigDecimal.valueOf(jsonDetalle.getDouble("importeUnitario")));
            }

            // importe
            if (!jsonDetalle.get("importe").equals(JSONObject.NULL)){
                tkMovDet.setSC_Importe(BigDecimal.valueOf(jsonDetalle.getDouble("importe")));
            }

            // descuento
            if (!jsonDetalle.get("descuento").equals(JSONObject.NULL)){
                tkMovDet.setSC_Descuento(BigDecimal.valueOf(jsonDetalle.getDouble("descuento")));
            }

            // medidaVenta
            if (!jsonDetalle.get("medidaVenta").equals(JSONObject.NULL)){
                tkMovDet.setSC_MedidaVenta(jsonDetalle.get("medidaVenta").toString().trim());
            }

            // codigoCategoria
            if (!jsonDetalle.get("codigoCategoria").equals(JSONObject.NULL)){
                tkMovDet.setSC_CodigoCategoria(Integer.valueOf(jsonDetalle.get("codigoCategoria").toString().trim()));
            }

            // codigoServicio
            if (!jsonDetalle.get("codigoServicio").equals(JSONObject.NULL)){
                tkMovDet.setSC_CodigoServicio(Integer.valueOf(jsonDetalle.get("codigoServicio").toString().trim()));
            }

            // numeroServicio
            if (!jsonDetalle.get("numeroServicio").equals(JSONObject.NULL)){
                tkMovDet.setSC_NumeroServicio(jsonDetalle.get("numeroServicio").toString().trim());
            }

            // fechaServicio
            if (!jsonDetalle.get("fechaServicio").equals(JSONObject.NULL)){
                String fch = jsonDetalle.get("fechaServicio").toString().trim();
                String fchHra = fch.substring(0, 4)+fch.substring(5, 7)+fch.substring(8, 10)+fch.substring(11, 13)+fch.substring(14, 16)+fch.substring(17, 19);
                Timestamp fchServicio =  DateUtils.convertStringToTimestamp_YYYYMMddHHMMss(fchHra);
                tkMovDet.setSC_FechaServicio(fchServicio);
            }

            // porcentajeIVA
            if (!jsonDetalle.get("porcentajeIVA").equals(JSONObject.NULL)){
                tkMovDet.setSC_PorcentajeIVA(BigDecimal.valueOf(jsonDetalle.getDouble("porcentajeIVA")));
            }

            // montoIVA
            if (!jsonDetalle.get("montoIVA").equals(JSONObject.NULL)){
                tkMovDet.setSC_MontoIVA(BigDecimal.valueOf(jsonDetalle.getDouble("montoIVA")));
            }

            tkMovDet.saveEx();
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return tkMovDet;
    }


    /***
     * Ejecuta operación GET para un array de objetos JSon.
     * Xpande. Created by Gabriel Vila on 2/12/19.
     * @param serviceName
     * @param configOrg
     * @return
     */
    private JSONArray executeJsonGET(String serviceName, MZScanntechConfigOrg configOrg){

        JSONArray jsonArray = null;
        String message = null;
        int timeout = 120;

        try{

            String url = this.scanntechConfig.getURL() + this.scanntechConfig.getMetodoPos() + "/" +
                    configOrg.getCodigoEmpPos().trim() + "/" + serviceName;

            String credentials = this.scanntechConfig.getUsuarioPos().trim() + ":" + this.scanntechConfig.getClavePos().trim();

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

            HttpGet request = new HttpGet(url);

            byte[] encodedAuth = Base64.encodeBase64(credentials.getBytes(Charset.forName("ISO-8859-1")));
            String authHeader = "Basic " + new String(encodedAuth);
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);

            CloseableHttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() != 200) {
                InputStream inStream = response.getEntity().getContent();
                if (inStream != null){
                    message = new Scanner(inStream).useDelimiter("\\A").next();
                }
            }
            else{

                if(response.getEntity().getContent() != null){

                    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
                    int i = br.read();
                    if(-1<i){
                        String line;
                        StringBuilder sb = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        JSONTokener tokener = new JSONTokener("["+ sb.toString() +"]");
                        jsonArray = new JSONArray(tokener);
                    }
                }
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return jsonArray;
    }


}
