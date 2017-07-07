package org.xpande.stech.process;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.codec.binary.Base64;
import org.compiere.model.I_M_Product;
import org.compiere.model.MOrg;
import org.compiere.model.MProduct;
import org.compiere.process.SvrProcess;
import org.xpande.core.model.I_Z_ProductoUPC;
import org.xpande.core.model.MZProductoUPC;
import org.xpande.stech.model.MZStechInterfaceOut;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

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
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/6/17.
 */
public class InterfaceOut extends SvrProcess {

    //private String url = "http://200.40.123.20:19317";
    //private String metodo = "/api-iposs-be-ext-uy/api/minoristas";
    //private String metodo2 = "/api-iposs-be-ext-uy/api/minoristas";
    //private int idEmpresa = 3343

    private String url = "http://200.40.123.20:19317";
    private String metodo = "/api-iposs-be-ext-uy/api/minoristas";
    private String metodo2 = "/api-iposs-be-ext-uy/api/minoristas";


    private String urlfullinsertprod = "http://200.40.123.20:19328/api-iposs-be-ext-uy/api/minoristas/4539/articulos";
    String auth = "integrador_test@openup.com.uy:integrador";
    private int idEmpresa = 4539;

    //private int idEmpresa = 6903;
    //String auth = "integrador_prod@openup.com.uy:OpenUp8209";
    //private String urlfullinsertprod= "http://200.40.123.20:19317/api-iposs-be-ext-uy/api/minoristas/6903/articulos";

    @Override
    protected void prepare() {

    }

    @Override
    protected String doIt() throws Exception {

        try {

            List<MZStechInterfaceOut> lines = MZStechInterfaceOut.getLinesNotExecuted(getCtx(), get_TrxName());
            for (MZStechInterfaceOut line : lines) {

                if (line.getCRUDType().equalsIgnoreCase("C")) {
                    if (line.getAD_Table_ID() == I_M_Product.Table_ID) {

                        //this.newProduct(line);
                        //this.updateProduct(line);
                        this.updateProductPrice(line);

                    }
                    else if (line.getAD_Table_ID() == I_Z_ProductoUPC.Table_ID){
                        //this.newUPC(line);
                    }
                }

            }

        } catch (Exception e) {
            throw new AdempiereException(e);
        }

        return "OK";
    }

    private String newProduct(MZStechInterfaceOut line) {

        try {
            MProduct prod = new MProduct(getCtx(), line.getRecord_ID(), null);

            JSONObject json = productToJson(prod, getCtx(), get_TrxName(), idEmpresa, true, true);//Se indica que es un nuevo producto ya que se esta creando el mismo.
            if (json == null) return ("Error al parcear el producto");

            int timeout = 120;

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            CloseableHttpClient httpClient =
                    HttpClientBuilder.create().setDefaultRequestConfig(config).build();


            HttpPost request = new HttpPost(urlfullinsertprod);
            System.out.println("----------------------------------------");
            System.out.println(request.getURI());
            System.out.println(json.toString());
            StringEntity params = new StringEntity(json.toString(), "UTF-8");
            request.addHeader("Accept", "application/json");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
            String authHeader = "Basic " + new String(encodedAuth);
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);


            CloseableHttpResponse response = httpClient.execute(request);

            System.out.println(response.getStatusLine());
            System.out.println(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("RESPUESTA OK Articulo creado");
                System.out.println("----------------------------------------");
            } else {
                String output = getResponseText(response.getEntity().getContent());
                System.out.println(output);
            }

        } catch (Exception e) {
            throw new AdempiereException(e);
        }

        return "OK";
    }



    private static String getResponseText(InputStream inStream) {
        System.out.println("asdfasd");
        if(inStream!=null){
            return new Scanner(inStream).useDelimiter("\\A").next();
        }
        return "{NoDato:nodata}";
    }


    private JSONObject productToJson(MProduct prod, Properties ctx, String trxName, int idEmpres,
                                     boolean newProduct, boolean withUpc) {
        try {
            MOrg orgRef = null;

            //Se crea el objeto articulo
            JSONObject ret = new JSONObject();
            //Se crea la lista de locales (Se debe agregar en todos los locales)
            List<JSONObject> sList = new ArrayList<JSONObject>();


            JSONObject s1 = new JSONObject();
            s1.put("codigoLocal", 1);
            sList.add(s1);

            JSONObject s2 = new JSONObject();
            s2.put("codigoLocal", 2);
            sList.add(s2);

            JSONObject s3 = new JSONObject();
            s3.put("codigoLocal", 3);
            sList.add(s3);

            JSONObject s4 = new JSONObject();
            s4.put("codigoLocal", 4);
            sList.add(s4);

            ret.put("locales", sList);


            ret.put("codigoArticulo", prod.getValue());

            if (prod.getName() == null || prod.getName().isEmpty())
                throw new AdempiereException("No se puede enviar producto sin nombre");

            String nameProd = prod.getName().trim();

            if (nameProd.length() > 30) {
                System.out.println(nameProd);
                nameProd = nameProd.substring(0, 30);
            }

            ret.put("descipcionReducida", nameProd); //14/01/2016

            String descProd = prod.getDescription();

            if (descProd == null || descProd.isEmpty()) descProd = nameProd;

            if (descProd.length() > 60) {
                descProd = descProd.substring(0, 60);
            }
            ret.put("descipcion", descProd);

            if (withUpc) {
                MZProductoUPC pUpc = MZProductoUPC.getByProduct(getCtx(), prod.get_ID(), get_TrxName());
                if (pUpc != null) {
                    ret.put("codigoGTIN", pUpc.getUPC());//Asumimos que solo se va a asociar un upc sino se obtiene de uy_productupc
                }
            }

            String unidad = "Un"; //KG o Unidad o Litro
            String tipoPeso = "U";// Unidad o Peso

            if (null != prod.getC_UOM()) {
                if ("UNI".equalsIgnoreCase(prod.getC_UOM().getUOMSymbol())) {
                    unidad = "Un";
                } else if ("KG".equalsIgnoreCase(prod.getC_UOM().getUOMSymbol())) {
                    unidad = "Kg";
                    tipoPeso = "P";
                    ret.put("ventaFraccionada", true);//Es solicitado por Alvaro 13/09/2016 Issue #6981
                } else if ("Ltr".equalsIgnoreCase(prod.getC_UOM().getUOMSymbol())) {
                    unidad = "Litro";
                }
            }
            ret.put("unidad", unidad);
            ret.put("tipoPeso", tipoPeso);

            //ret.put("usaBalanza",prod.get_ValueAsBoolean("UsaBalanza"));
            ret.put("usaBalanza", false);
            int codigoPesable = 0; //---> OJO tener en cuenta que no puede ser value ya que no acepta valor como 1000311

            if (unidad.equalsIgnoreCase("kg") || prod.get_ValueAsBoolean("UsaBalanza")) {
                String codigoBalanza = prod.getValue().trim();

                String codProdFormat = String.format("%%0%dd", 5);
                codigoBalanza = String.format(codProdFormat, Integer.parseInt(codigoBalanza));

                if (codigoBalanza.length() > 5) {
                    if (codigoBalanza.length() == 6 && codigoBalanza.startsWith("0")) {
                        codigoPesable = Integer.valueOf(codigoBalanza);
                    } else {
                        throw new AdempiereException("El codigo del producto no puede tener ser mayor a 99999");
                    }
                } else {
                    codigoPesable = Integer.valueOf(codigoBalanza);
                }
            }
            ret.put("plu", codigoPesable);

            ret.put("ivaVenta", 22); // Valor obligatorio
            //ret.put("codigoEmpresa",rtConfig.getidentifempresa()); // No tiene este atrinbuto en api nueva
            //FIN Issue #5571

            ret.put("codigoCategoria", 1);

            ret.put("precioVenta", 12.00);
            //ret.put("truncamiento", 0);

            //OpenUp SBT 09/08/2012 Issue #6165	Se agrega campo en producto y se envia
            ret.put("descuento", false);

            return ret;

        } catch (Exception e) {
            throw new AdempiereException("ERROR - Producto To Sistema Cajas: " + e.getMessage());
        }
    }


    private String updateProduct(MZStechInterfaceOut line) {

        try {
            MProduct prod = new MProduct(getCtx(), line.getRecord_ID(), null);

            JSONObject json = updateProductToJson(prod, getCtx(), get_TrxName(), idEmpresa, true, true);//Se indica que es un nuevo producto ya que se esta creando el mismo.
            if (json == null) return ("Error al parcear el producto");

            int timeout = 120;

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            CloseableHttpClient httpClient =
                    HttpClientBuilder.create().setDefaultRequestConfig(config).build();


            //HttpPost request = new HttpPost(urlfullinsertprod + "/" + prod.getValue());
            HttpPost request = new HttpPost(urlfullinsertprod);
            System.out.println("----------------------------------------");
            System.out.println(request.getURI());
            System.out.println(json.toString());
            StringEntity params = new StringEntity(json.toString(), "UTF-8");
            request.addHeader("Accept", "application/json");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
            String authHeader = "Basic " + new String(encodedAuth);
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);


            CloseableHttpResponse response = httpClient.execute(request);

            System.out.println(response.getStatusLine());
            System.out.println(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("RESPUESTA OK Articulo creado");
                System.out.println("----------------------------------------");
            } else {
                String output = getResponseText(response.getEntity().getContent());
                System.out.println(output);
            }

        } catch (Exception e) {
            throw new AdempiereException(e);
        }

        return "OK";
    }

    private JSONObject updateProductToJson(MProduct prod, Properties ctx, String trxName, int idEmpres,
                                     boolean newProduct, boolean withUpc) {
        try {
            MOrg orgRef = null;

            //Se crea el objeto articulo
            JSONObject ret = new JSONObject();
            //Se crea la lista de locales (Se debe agregar en todos los locales)
            List<JSONObject> sList = new ArrayList<JSONObject>();


            JSONObject s1 = new JSONObject();
            s1.put("codigoLocal", 1);
            sList.add(s1);

            JSONObject s2 = new JSONObject();
            s2.put("codigoLocal", 2);
            sList.add(s2);

            JSONObject s3 = new JSONObject();
            s3.put("codigoLocal", 3);
            sList.add(s3);

            JSONObject s4 = new JSONObject();
            s4.put("codigoLocal", 4);
            sList.add(s4);

            ret.put("locales", sList);


            ret.put("codigoArticulo", prod.getValue());

            if (prod.getName() == null || prod.getName().isEmpty())
                throw new AdempiereException("No se puede enviar producto sin nombre");

            String nameProd = prod.getName().trim();

            if (nameProd.length() > 30) {
                System.out.println(nameProd);
                nameProd = nameProd.substring(0, 30);
            }

            ret.put("descipcionReducida", "ART MOD"); //14/01/2016

            String descProd = prod.getDescription();

            if (descProd == null || descProd.isEmpty()) descProd = nameProd;

            if (descProd.length() > 60) {
                descProd = descProd.substring(0, 60);
            }
            ret.put("descipcion", "ART MOD DESC");

            if (withUpc) {
                MZProductoUPC pUpc = MZProductoUPC.getByProduct(getCtx(), prod.get_ID(), get_TrxName());
                if (pUpc != null) {
                    ret.put("codigoGTIN", pUpc.getUPC());//Asumimos que solo se va a asociar un upc sino se obtiene de uy_productupc
                }
            }

            String unidad = "Un"; //KG o Unidad o Litro
            String tipoPeso = "U";// Unidad o Peso

            if (null != prod.getC_UOM()) {
                if ("UNI".equalsIgnoreCase(prod.getC_UOM().getUOMSymbol())) {
                    unidad = "Un";
                } else if ("KG".equalsIgnoreCase(prod.getC_UOM().getUOMSymbol())) {
                    unidad = "Kg";
                    tipoPeso = "P";
                    ret.put("ventaFraccionada", true);//Es solicitado por Alvaro 13/09/2016 Issue #6981
                } else if ("Ltr".equalsIgnoreCase(prod.getC_UOM().getUOMSymbol())) {
                    unidad = "Litro";
                }
            }
            ret.put("unidad", unidad);
            ret.put("tipoPeso", tipoPeso);

            //ret.put("usaBalanza",prod.get_ValueAsBoolean("UsaBalanza"));
            ret.put("usaBalanza", false);
            int codigoPesable = 0; //---> OJO tener en cuenta que no puede ser value ya que no acepta valor como 1000311

            if (unidad.equalsIgnoreCase("kg") || prod.get_ValueAsBoolean("UsaBalanza")) {
                String codigoBalanza = prod.getValue().trim();

                String codProdFormat = String.format("%%0%dd", 5);
                codigoBalanza = String.format(codProdFormat, Integer.parseInt(codigoBalanza));

                if (codigoBalanza.length() > 5) {
                    if (codigoBalanza.length() == 6 && codigoBalanza.startsWith("0")) {
                        codigoPesable = Integer.valueOf(codigoBalanza);
                    } else {
                        throw new AdempiereException("El codigo del producto no puede tener ser mayor a 99999");
                    }
                } else {
                    codigoPesable = Integer.valueOf(codigoBalanza);
                }
            }
            ret.put("plu", codigoPesable);

            ret.put("ivaVenta", 10); // Valor obligatorio
            //ret.put("codigoEmpresa",rtConfig.getidentifempresa()); // No tiene este atrinbuto en api nueva
            //FIN Issue #5571

            ret.put("codigoCategoria", 2);

            //ret.put("precioVenta", 12.00);
            //ret.put("truncamiento", 0);

            //OpenUp SBT 09/08/2012 Issue #6165	Se agrega campo en producto y se envia
            ret.put("descuento", false);

            return ret;

        } catch (Exception e) {
            throw new AdempiereException("ERROR - Producto To Sistema Cajas: " + e.getMessage());
        }
    }

    private JSONObject upcToJson(MProduct prod,String mUPC,int mIDEmpresa) {
        try{
			/*{
				"codigoEmpresa":24028,
				"codigoArticulo":"123509",
				"codigoGtin":"5000267023005"
			}*/
            JSONObject ret = new JSONObject();
            ret.put("codigoEmpresa", mIDEmpresa);
            ret.put("codigoArticulo", prod.getValue().trim());
            //ret.put("codigoArticulo", "110943");
            ret.put("codigoGtin", mUPC);

            return ret;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    private String newUPC(MZStechInterfaceOut line) {

        try {

            MZProductoUPC pupc = new MZProductoUPC(getCtx(), line.getRecord_ID(), null);
            MProduct prod = (MProduct) pupc.getM_Product();

            JSONObject json = upcToJson(prod, pupc.getUPC(), idEmpresa);
            if (json == null) return ("Error al parcear el upc");

            int timeout = 120;

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            CloseableHttpClient httpClient =
                    HttpClientBuilder.create().setDefaultRequestConfig(config).build();


            HttpPost request = new HttpPost(urlfullinsertprod + "/" + prod.getValue().trim() + "/barras");
            System.out.println("----------------------------------------");
            System.out.println(request.getURI());
            System.out.println(json.toString());
            StringEntity params = new StringEntity(json.toString(), "UTF-8");
            request.addHeader("Accept", "application/json");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
            String authHeader = "Basic " + new String(encodedAuth);
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);


            CloseableHttpResponse response = httpClient.execute(request);

            System.out.println(response.getStatusLine());
            System.out.println(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("RESPUESTA OK");
                System.out.println("----------------------------------------");
            } else {
                String output = getResponseText(response.getEntity().getContent());
                System.out.println(output);
            }

        } catch (Exception e) {
            throw new AdempiereException(e);
        }

        return "OK";
    }


    private JSONObject productUpdatePriceToJson(MProduct prod,String adOrgIDValue) {
        try{
            JSONObject ret = new JSONObject();
            List<JSONObject > sList=null;
            JSONObject s = null;
            //Se agrega la lista de locales, en este caso solo se agrega el local que corresponde
            sList = new ArrayList<JSONObject >();
            s = new JSONObject();
            s.put("codigoLocal", Integer.valueOf(adOrgIDValue));
            sList.add(s);
            ret.put("locales", sList);

            //Obtengo el precio de venta de la lista de precio correspondiente a la organizaci�n que corresponde
            BigDecimal precio = new BigDecimal(25.00);
            String price = "25.00";
            ret.put("codigoArticulo", prod.getValue().trim());
            //ret.put("precioVenta", (precio!=null)?price:"0.00");
            ret.put("precioVenta", price);
            //ret.put("descuentoMax", 0.5);//OJO LUEGO VERIFICAR DE DONDE OBTENG
            return ret;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    private String updateProductPrice(MZStechInterfaceOut line) {

        try {

            MProduct prod = new MProduct(getCtx(), line.getRecord_ID(), null);

            JSONObject json = productUpdatePriceToJson(prod,"1");
            if (json == null) return ("Error al parcear precio");

            int timeout = 120;

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            CloseableHttpClient httpClient =
                    HttpClientBuilder.create().setDefaultRequestConfig(config).build();


            HttpPut request = new HttpPut(urlfullinsertprod + "/" + prod.getValue().trim() + "/precios");
            System.out.println("----------------------------------------");
            System.out.println(request.getURI());
            System.out.println(json.toString());
            StringEntity params = new StringEntity(json.toString(), "UTF-8");
            request.addHeader("Accept", "application/json");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
            String authHeader = "Basic " + new String(encodedAuth);
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);


            CloseableHttpResponse response = httpClient.execute(request);

            System.out.println(response.getStatusLine());
            System.out.println(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("RESPUESTA OK");
                System.out.println("----------------------------------------");
            } else {
                String output = getResponseText(response.getEntity().getContent());
                System.out.println(output);
            }

        } catch (Exception e) {
            throw new AdempiereException(e);
        }

        return "OK";
    }

}
