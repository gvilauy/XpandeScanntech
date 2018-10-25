package org.xpande.stech.utils;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.eevolution.model.X_C_TaxGroup;
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
            MTax tax = TaxUtils.getLastTaxByCategory(this.ctx, product.getC_TaxCategory_ID(), null);
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

        try{

            //String url = this.scanntechConfig.getURL() + "/" + this.scanntechConfig.getMetodoPos() + "/" + configOrg.getCodigoEmpPos().trim() + "/" + serviceName;

            String url = this.scanntechConfig.getURL() + this.scanntechConfig.getMetodoPos() + "/" + configOrg.getCodigoEmpPos().trim() + "/" + serviceName;

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

}
