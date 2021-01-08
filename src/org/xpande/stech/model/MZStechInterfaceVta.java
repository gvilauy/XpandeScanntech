package org.xpande.stech.model;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.compiere.acct.Doc;
import org.compiere.model.*;
import org.compiere.process.DocAction;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xpande.core.utils.DateUtils;
import org.xpande.core.utils.PriceListUtils;
import org.xpande.financial.utils.FinancialUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Modelo para cabezales de procesos de interface de ventas contra POS Scanntech.
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 2/12/19.
 */
public class MZStechInterfaceVta extends X_Z_StechInterfaceVta {

    //Logger
    private CLogger log = CLogger.getCLogger (getClass());

    // Modelo de configuración del proceso de Interface
    private MZScanntechConfig scanntechConfig = null;

    private final int cantRowsVentaPagina = 100;
    private int cantMovProcesados = 0;


    public MZStechInterfaceVta(Properties ctx, int Z_StechInterfaceVta_ID, String trxName) {
        super(ctx, Z_StechInterfaceVta_ID, trxName);
    }

    public MZStechInterfaceVta(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /***
     * Obtiene y retorna modelo segun organización y fecha pos recibidos.
     * Xpande. Created by Gabriel Vila on 5/7/20.
     * @param ctx
     * @param adOrgID
     * @param dateTrx
     * @param trxName
     * @return
     */
    public static MZStechInterfaceVta getByOrgDate(Properties ctx, int adOrgID, Timestamp dateTrx, String trxName) {

        String whereClause = X_Z_StechInterfaceVta.COLUMNNAME_AD_Org_ID + " =" + adOrgID +
                " AND " + X_Z_StechInterfaceVta.COLUMNNAME_DateTrx + " ='" + dateTrx + "' ";

        MZStechInterfaceVta model = new Query(ctx, I_Z_StechInterfaceVta.Table_Name, whereClause, trxName).first();

        return model;
    }


    /***
     * Ejecuta el proceso de interface de ventas desde el POS Scanntech hacia ADempiere.
     * Xpande. Created by Gabriel Vila on 1/16/19.
     * @param configOrg
     * @param fechaConsulta
     * @return
     */
    public String execute(MZScanntechConfigOrg configOrg, Timestamp fechaConsulta) {

        String message = null;

        try{

            // Obtengo configurador de scanntech
            if (this.scanntechConfig == null){
                this.scanntechConfig = MZScanntechConfig.getDefault(getCtx(), null);
            }

            // Tiempo inicial del proceso
            this.setStartDate(new Timestamp(System.currentTimeMillis()));

            // Formatos requeridos por Scanntech de fecha desde y hasta para obtener movimientos.
            Timestamp fechaConsultaAuxDesde = new Timestamp(fechaConsulta.getTime());
            Timestamp fechaConsultaAuxHasta = new Timestamp(fechaConsulta.getTime());
            Timestamp fechaDesde = DateUtils.getTSManualHour(fechaConsultaAuxDesde, 0, 0, 0, 0);
            Timestamp fechaHastaAux = DateUtils.getTSManualHour(fechaConsultaAuxHasta, 23, 59, 59, 0);

            Date dateFechaAux = new Date(fechaHastaAux.getTime());
            dateFechaAux =  DateUtils.addDays(dateFechaAux, 1);
            Timestamp fechaHasta = new Timestamp(dateFechaAux.getTime());

            String fechaAux = fechaConsulta.toString();
            String fechaConsultaSTR = fechaAux.substring(0, 10);

            fechaAux = fechaDesde.toString();
            String fechaDesdeSTR = fechaAux.substring(0, 10) + "T" + fechaAux.substring(11) + "00-0300";

            fechaAux = fechaHasta.toString();
            String fechaHastaSTR = fechaAux.substring(0, 10) + "T" + fechaAux.substring(11) + "00-0300";

            String serviceText = "";


            // Recorros cajas de esta organización para obtener movimientos de cada una de ellas.
            List<MZScanntechConfigCaja> configCajaList = configOrg.getCajas();

            if (configCajaList.size() <= 0){
                return "No hay cajas definidas para procesar.";
            }

            this.setDateTrx(fechaConsulta);
            this.setDateFrom(fechaDesde);
            this.setDateTo(fechaHasta);

            // Obtengo información para cada CAJA de esta organización
            for(MZScanntechConfigCaja configCaja: configCajaList){

                System.out.println("----- Organizacion : " + configOrg.getAD_OrgTrx_ID() + " - Caja : " + configCaja.getCodigoPOS());

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
                message = this.setInfoMovimientosVenta(configOrg, configCaja, jsonArrayMov);
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
                        message = this.setInfoMovimientosVenta(configOrg, configCaja, jsonArrayMov);
                        if (message != null){
                            throw new AdempiereException(message);
                        }
                    }
                }
            }

            // Guardo totales
            this.setContadorLineas(cantMovProcesados);

            // Paso info a tablas finales
            System.out.println("-----Inteface Scanntech : setInfoTablasFinales");
            this.setInfoTablasFinales();

            // Proceso ventas con credito de la casa
            System.out.println("-----Inteface Scanntech : setVentasCredito");
            this.setVentasCredito();

            // Proceso ventas con RUT (no se hace invoice, es solo para formularios de DGI)
            System.out.println("-----Inteface Scanntech : setVentasComprobante");
            this.setVentasComprobantes();

            // Guardo información de ventas para informes
            System.out.println("-----Inteface Scanntech : setDataInformes");
            this.setDataInformes();

            // Elimino datos de tablas temporales
            System.out.println("-----Inteface Scanntech : deleteTablasTemporales");
            this.deleteTablasTemporales();

            // Tiempo final de proceso
            this.setEndDate(new Timestamp(System.currentTimeMillis()));
            this.saveEx();

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

        return message;
    }

    private void setDataInformes() {

        String sql, action;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            // Por ahora solo para adumel
            if (this.getAD_Org_ID() != 1000000){
                return;
            }

            String insert = " insert into z_bi_invprodday (ad_client_id, ad_org_id, dateinvoiced, c_currency_id, m_product_id, " +
                    " c_uom_id, qtysold, amtsubtotal, totalamt, qtypurchased, amtsubtotalpo, totalamtpo, z_bi_dia_id) ";

            // Cupones no cancelados
            sql = " select a.ad_org_id, a.datetrx, dimday.z_bi_dia_id, mov.sc_codigomoneda, coalesce(mdet.m_product_id, " +
                    " upc.m_product_id) as m_product_id, prod.c_uom_id, " +
                    " sum(mdet.sc_cantidad) as cantidad, sum(mdet.sc_importe - mdet.sc_montoiva) as subtotal, " +
                    " sum(mdet.sc_importe) as total " +
                    " from z_stechinterfacevta a " +
                    " inner join z_stech_tmp_mov mov on a.z_stechinterfacevta_id = mov.z_stechinterfacevta_id " +
                    " inner join z_stech_tmp_movdet mdet on mov.z_stech_tmp_mov_id = mdet.z_stech_tmp_mov_id " +
                    " left outer join m_product prod on mdet.m_product_id = prod.m_product_id " +
                    " left outer join z_bi_dia dimday on a.datetrx = dimday.datetrx " +
                    " left outer join z_productoupc upc on mdet.sc_codigobarras = upc.upc " +
                    " where a.ad_org_id =" + this.getAD_Org_ID() +
                    " and a.datetrx ='" + this.getDateTrx() + "' " +
                    " and mov.sc_cuponcancelado ='N' " +
                    " and mov.sc_cuponanulada ='N'" +
                    " group by 1,2,3,4,5,6";

            pstmt = DB.prepareStatement(sql, get_TrxName());
            rs = pstmt.executeQuery();

            while(rs.next()){

                int currencyID = 142;
                if (rs.getString("sc_codigomoneda") != null){
                    if (!rs.getString("sc_codigomoneda").equalsIgnoreCase("858")){
                        currencyID = 100;
                    }
                }

                // Verifico si no existe un registro para esta clave en la tabla de analisis
                sql = " select count(*) from z_bi_invprodday " +
                        " where ad_org_id =" + rs.getInt("ad_org_id") +
                        " and m_product_id =" + rs.getInt("m_product_id") +
                        " and c_currency_id =" + currencyID +
                        " and dateinvoiced ='" + rs.getTimestamp("datetrx") + "' ";
                int contador = DB.getSQLValueEx(get_TrxName(), sql);

                // Si no existe aún un registro para esta clave
                if (contador <= 0){

                    // Insert
                    action = " values (" + this.getAD_Client_ID() + ", " + rs.getInt("ad_org_id") + ", '" +
                            rs.getTimestamp("datetrx") + "', " + currencyID + ", " +
                            rs.getInt("m_product_id") + ", " + rs.getInt("c_uom_id") + ", " +
                            rs.getBigDecimal("cantidad") + ", " + rs.getBigDecimal("subtotal") + ", " +
                            rs.getBigDecimal("total") + ", 0, 0, 0, " + rs.getInt("z_bi_dia_id") + ") ";

                    DB.executeUpdateEx(insert + action, get_TrxName());

                }
                else {
                    // Actualizo
                    action = " update z_bi_invprodday set " +
                            " qtysold = qtysold + " + rs.getBigDecimal("cantidad") + ", " +
                            " amtsubtotal = amtsubtotal + " + rs.getBigDecimal("subtotal") + ", " +
                            " totalamt = totalamt + " + rs.getBigDecimal("total") +
                            " where ad_org_id =" + rs.getInt("ad_org_id") +
                            " and m_product_id =" + rs.getInt("m_product_id") +
                            " and c_currency_id =" + currencyID +
                            " and dateinvoiced ='" + rs.getTimestamp("datetrx") + "' ";

                    DB.executeUpdateEx(action, get_TrxName());
                }
            }
            DB.close(rs, pstmt);
            rs = null; pstmt = null;


            // Cupones cancelados
            sql = " select a.ad_org_id, a.datetrx, dimday.z_bi_dia_id, mov.sc_codigomoneda, coalesce(mdet.m_product_id, " +
                    " upc.m_product_id) as m_product_id, prod.c_uom_id, " +
                    " sum(mdet.sc_cantidad) as cantidad, sum(mdet.sc_importe - mdet.sc_montoiva) as subtotal, " +
                    " sum(mdet.sc_importe) as total " +
                    " from z_stechinterfacevta a " +
                    " inner join z_stech_tmp_mov mov on a.z_stechinterfacevta_id = mov.z_stechinterfacevta_id " +
                    " inner join z_stech_tmp_movdet mdet on mov.z_stech_tmp_mov_id = mdet.z_stech_tmp_mov_id " +
                    " left outer join m_product prod on mdet.m_product_id = prod.m_product_id " +
                    " left outer join z_bi_dia dimday on a.datetrx = dimday.datetrx " +
                    " left outer join z_productoupc upc on mdet.sc_codigobarras = upc.upc " +
                    " where a.ad_org_id =" + this.getAD_Org_ID() +
                    " and a.datetrx ='" + this.getDateTrx() + "' " +
                    " and mov.sc_cuponcancelado ='Y' " +
                    " group by 1,2,3,4,5,6";

            pstmt = DB.prepareStatement(sql, get_TrxName());
            rs = pstmt.executeQuery();

            while(rs.next()){

                int currencyID = 142;
                if (rs.getString("sc_codigomoneda") != null){
                    if (!rs.getString("sc_codigomoneda").equalsIgnoreCase("858")){
                        currencyID = 100;
                    }
                }

                // Me aseguro signo negativo en las cantidades y totales
                BigDecimal cantidad = rs.getBigDecimal("cantidad");
                BigDecimal subtotal = rs.getBigDecimal("subtotal");
                BigDecimal total = rs.getBigDecimal("total");

                if (cantidad == null) cantidad = Env.ZERO;
                if (subtotal == null) subtotal = Env.ZERO;
                if (total == null) total = Env.ZERO;

                if (cantidad.compareTo(Env.ZERO) > 0) cantidad = cantidad.negate();
                if (subtotal.compareTo(Env.ZERO) > 0) subtotal = subtotal.negate();
                if (total.compareTo(Env.ZERO) > 0) total = total.negate();

                // Verifico si no existe un registro para esta clave en la tabla de analisis
                sql = " select count(*) from z_bi_invprodday " +
                        " where ad_org_id =" + rs.getInt("ad_org_id") +
                        " and m_product_id =" + rs.getInt("m_product_id") +
                        " and c_currency_id =" + currencyID +
                        " and dateinvoiced ='" + rs.getTimestamp("datetrx") + "' ";
                int contador = DB.getSQLValueEx(get_TrxName(), sql);

                // Si no existe aún un registro para esta clave
                if (contador <= 0){

                    // Insert
                    action = " values (" + this.getAD_Client_ID() + ", " + rs.getInt("ad_org_id") + ", '" +
                            rs.getTimestamp("datetrx") + "', " + currencyID + ", " +
                            rs.getInt("m_product_id") + ", " + rs.getInt("c_uom_id") + ", " +
                            cantidad + ", " + subtotal + ", " + total + ", 0, 0, 0, " + rs.getInt("z_bi_dia_id") + ") ";

                    DB.executeUpdateEx(insert + action, get_TrxName());
                }
                else {
                    // Actualizo
                    action = " update z_bi_invprodday set " +
                            " qtysold = qtysold + " + cantidad + ", " +
                            " amtsubtotal = amtsubtotal + " + subtotal + ", " +
                            " totalamt = totalamt + " + total +
                            " where ad_org_id =" + rs.getInt("ad_org_id") +
                            " and m_product_id =" + rs.getInt("m_product_id") +
                            " and c_currency_id =" + currencyID +
                            " and dateinvoiced ='" + rs.getTimestamp("datetrx") + "' ";

                    DB.executeUpdateEx(action, get_TrxName());
                }
            }
            DB.close(rs, pstmt);
            rs = null; pstmt = null;
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
            rs = null; pstmt = null;
        }

        try{


        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
    }


    private void deleteTablasTemporales() {

        String action = "";

        try{

            action = " delete from z_stech_tmp_movpago where z_stechinterfacevta_id =" + this.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            action = " delete from z_stech_tmp_movdetdtos where z_stechinterfacevta_id =" + this.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            action = " delete from z_stech_tmp_movdet where z_stechinterfacevta_id =" + this.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            action = " delete from z_stech_tmp_mov where z_stechinterfacevta_id =" + this.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }

    }

    private void setInfoTablasFinales() {

        String action = "";

        try{

            // Inserto en tabla de movimientos
            action = " INSERT INTO z_stech_tk_mov(" +
                        " ad_client_id, ad_org_id, created, createdby, isactive, sc_numeromov, " +
                        " updated, updatedby, uuid, z_stech_tk_mov_id, sc_codigoempresa, " +
                        " sc_codigolocal, sc_codigocaja, sc_fechaoperacion, sc_tipooperacion, " +
                        " sc_tipocfe, sc_seriecfe, sc_numerooperacion, sc_codigoseguridadcfe, " +
                        " sc_descuentototal, sc_cantidaditems, sc_codigomoneda, sc_total, " +
                        " sc_redondeo, sc_cotizacioncompra, sc_cotizacionventa, sc_cuponcancelado, " +
                        " sc_cuponanulada, z_stechinterfacevta_id, jsonbody, datetrx, sc_nombrefactura, " +
                        " sc_rucfactura, sc_direccionfactura) " +
                    " SELECT ad_client_id, ad_org_id, created, createdby, isactive, sc_numeromov, " +
                        " updated, updatedby, uuid, z_stech_tmp_mov_id, sc_codigoempresa, " +
                        " sc_codigolocal, sc_codigocaja, sc_fechaoperacion, sc_tipooperacion, " +
                        " sc_tipocfe, sc_seriecfe, sc_numerooperacion, sc_codigoseguridadcfe, " +
                        " sc_descuentototal, sc_cantidaditems, sc_codigomoneda, sc_total, " +
                        " sc_redondeo, sc_cotizacioncompra, sc_cotizacionventa, sc_cuponcancelado, " +
                        " sc_cuponanulada, z_stechinterfacevta_id, jsonbody, datetrx, sc_nombrefactura, " +
                        " sc_rucfactura, sc_direccionfactura " +
                    " FROM z_stech_tmp_mov " +
                    " WHERE z_stechinterfacevta_id =" + this.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            // Inserto en tabla de detalles
            action = " INSERT INTO z_stech_tk_movdet(" +
                        " ad_client_id, ad_org_id, created, createdby, isactive, sc_cantidad, " +
                        " sc_codigoarticulo, sc_codigoarticulopadre, sc_codigobarras, sc_codigocategoria, " +
                        " sc_codigoservicio, sc_codigotipodetalle, sc_descripcionarticulo, " +
                        " sc_descuento, sc_fechaservicio, sc_importe, sc_importeunitario, " +
                        " sc_medidaventa, sc_montoiva, sc_numeroservicio, sc_porcentajeiva, " +
                        " updated, updatedby, uuid, z_stech_tk_movdet_id, z_stech_tk_mov_id, " +
                        " z_stechinterfacevta_id, jsonbody, m_product_id, z_productoupc_id, " +
                        " datetrx, sc_codigoiva) " +
                    " SELECT ad_client_id, ad_org_id, created, createdby, isactive, sc_cantidad, " +
                        " sc_codigoarticulo, sc_codigoarticulopadre, sc_codigobarras, sc_codigocategoria, " +
                        " sc_codigoservicio, sc_codigotipodetalle, sc_descripcionarticulo, " +
                        " sc_descuento, sc_fechaservicio, sc_importe, sc_importeunitario, " +
                        " sc_medidaventa, sc_montoiva, sc_numeroservicio, sc_porcentajeiva, " +
                        " updated, updatedby, uuid, z_stech_tmp_movdet_id, z_stech_tmp_mov_id, " +
                        " z_stechinterfacevta_id, jsonbody, m_product_id, z_productoupc_id, " +
                        " datetrx, sc_codigoiva " +
                    " FROM z_stech_tmp_movdet " +
                    " WHERE z_stechinterfacevta_id =" + this.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            // Inserto en tabla de descuentos
            action = "INSERT INTO z_stech_tk_movdetdtos( " +
                        " ad_client_id, ad_org_id, created, createdby, isactive, sc_iddescuento, " +
                        " sc_idpromocion, sc_importedescuento, sc_tipodescuento, updated, " +
                        " updatedby, uuid, z_stech_tk_movdetdtos_id, z_stech_tk_movdet_id, " +
                        " z_stech_tk_mov_id, z_stechinterfacevta_id, jsonbody, datetrx) " +
                    " SELECT ad_client_id, ad_org_id, created, createdby, isactive, sc_iddescuento, " +
                        " sc_idpromocion, sc_importedescuento, sc_tipodescuento, updated, " +
                        " updatedby, uuid, z_stech_tmp_movdetdtos_id, z_stech_tmp_movdet_id, " +
                        " z_stech_tmp_mov_id, z_stechinterfacevta_id, jsonbody, datetrx " +
                    " FROM z_stech_tmp_movdetdtos " +
                    " WHERE z_stechinterfacevta_id =" + this.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

            // Inserto en tabla de pagos
            action = "INSERT INTO z_stech_tk_movpago( " +
                        " ad_client_id, ad_org_id, created, createdby, isactive, sc_cambio, " +
                        " sc_codigocliente, sc_codigocredito, sc_codigomoneda, sc_codigoplanpagos, " +
                        " sc_codigotipopago, sc_comerciocredito, sc_cotizacioncompra, sc_cotizacionventa, " +
                        " sc_documentocliente, sc_fechavencimiento, sc_importe, sc_importepago, " +
                        " sc_numeroautorizacion, sc_numerocuotaspago, sc_numerodocumentopago, " +
                        " sc_numerotarjeta, sc_terminalcredito, updated, updatedby, uuid, " +
                        " z_stech_tk_mov_id, z_stech_tk_movpago_id, z_stechinterfacevta_id, " +
                        " jsonbody, datetrx, z_stechmediopago_id, z_stechcreditos_id, sc_codigovale, " +
                        " sc_descuentoafam, sc_descuentoincfin) " +
                    " SELECT ad_client_id, ad_org_id, created, createdby, isactive, sc_cambio, " +
                        " sc_codigocliente, sc_codigocredito, sc_codigomoneda, sc_codigoplanpagos, " +
                        " sc_codigotipopago, sc_comerciocredito, sc_cotizacioncompra, sc_cotizacionventa, " +
                        " sc_documentocliente, sc_fechavencimiento, sc_importe, sc_importepago, " +
                        " sc_numeroautorizacion, sc_numerocuotaspago, sc_numerodocumentopago, " +
                        " sc_numerotarjeta, sc_terminalcredito, updated, updatedby, uuid, " +
                        " z_stech_tmp_mov_id, z_stech_tmp_movpago_id, z_stechinterfacevta_id, " +
                        " jsonbody, datetrx, z_stechmediopago_id, z_stechcreditos_id, sc_codigovale, " +
                        " sc_descuentoafam, sc_descuentoincfin " +
                    " FROM z_stech_tmp_movpago " +
                    " WHERE z_stechinterfacevta_id =" + this.get_ID();
            DB.executeUpdateEx(action, get_TrxName());

        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
    }

    /***
     * Guarda información de movimientos obtenidos por interface de ventas.
     * Xpande. Created by Gabriel Vila on 2/12/19.
     * @param configOrg
     * @param configCaja
     * @param jsonArrayMov
     * @return
     */
    private String setInfoMovimientosVenta(MZScanntechConfigOrg configOrg, MZScanntechConfigCaja configCaja, JSONArray jsonArrayMov) {

        String message = null;

        try{

            int pageSize = jsonArrayMov.length();

            // Recorro y guardo movimientos obtenidos
            for (int i = 0; i < pageSize; i++){

                System.out.println(configOrg.getAD_OrgTrx_ID() + " - Procesando Movimiento " + (i + 1) + " de " + pageSize);

                JSONObject jsonMovimiento = jsonArrayMov.getJSONObject(i);

                // Controlo que no procese nuevamente un movimiento con este mismo numero, en cuyo caso me salteo este movimiento.
                if (!jsonMovimiento.get("numeroMov").equals(JSONObject.NULL)){
                    String numeroMov = jsonMovimiento.get("numeroMov").toString().trim();
                    int idMov = MZStechTMPMov.getIDByNumeroMov(getCtx(), numeroMov, get_TrxName());
                    if (idMov > 0){
                        continue;
                    }
                }

                MZStechTMPMov tkMov = this.setJsonMovimiento(configOrg, configCaja, jsonMovimiento);

                // Recorro y guardo detalles de este movimiento
                JSONArray jsonArrayDetalles = jsonMovimiento.getJSONArray("detalles");
                if (jsonArrayDetalles != null){
                    for (int j = 0; j < jsonArrayDetalles.length(); j++){

                        JSONObject jsonDetalle = jsonArrayDetalles.getJSONObject(j);

                        MZStechTMPMovDet tkMovDet = this.setJsonDetalleMov(tkMov, jsonDetalle);

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

                    /*
                    // En scanntech hay medios de pago del tipo VALE que dentro del mismo nro. de movimiento se asocian a otro medio de pago.
                    // Aquí se copian los datos del medio de pago asociado, al registro del VALE.
                    //this.setInfoVales(tkMov);
                    */
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
    private void setJsonMedioPagoMov(MZStechTMPMov tkMov, JSONObject jsonMedioPago) {

        MZStechTMPMovPago tkMovPago = null;
        String sql = "";

        try{

            tkMovPago = new MZStechTMPMovPago(getCtx(), 0, get_TrxName());
            tkMovPago.setZ_Stech_TMP_Mov_ID(tkMov.get_ID());
            tkMovPago.set_ValueOfColumn("AD_Client_ID", tkMov.getAD_Client_ID());
            tkMovPago.setAD_Org_ID(tkMov.getAD_Org_ID());
            tkMovPago.setZ_StechInterfaceVta_ID(tkMov.getZ_StechInterfaceVta_ID());
            tkMovPago.setDateTrx(tkMov.getDateTrx());
            tkMovPago.setJSonBody(jsonMedioPago.toString());

            // codigoTipoPago
            if (!jsonMedioPago.get("codigoTipoPago").equals(JSONObject.NULL)){
                tkMovPago.setSC_CodigoTipoPago(Integer.valueOf(jsonMedioPago.get("codigoTipoPago").toString().trim()));

                sql = "select z_stechmediopago_id from z_stechmediopago where value ='" + tkMovPago.getSC_CodigoTipoPago() + "'";
                int idAux = DB.getSQLValueEx(null, sql);
                if (idAux > 0){
                    tkMovPago.setZ_StechMedioPago_ID(idAux);
                }
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

            // codigoVale
            boolean tengoCreditoID = false;
            if (!jsonMedioPago.get("codigoVale").equals(JSONObject.NULL)){

                tkMovPago.setSC_CodigoVale(Integer.valueOf(jsonMedioPago.get("codigoVale").toString().trim()));

                sql = "select z_stechcreditos_id from z_stechcreditos where value ='" + tkMovPago.getSC_CodigoVale() + "'";
                int idAux = DB.getSQLValueEx(null, sql);
                if (idAux > 0){
                    tkMovPago.setZ_StechCreditos_ID(idAux);
                    tengoCreditoID = true;
                }
            }

            // codigoCredito
            if (!jsonMedioPago.get("codigoCredito").equals(JSONObject.NULL)){
                tkMovPago.setSC_CodigoCredito(Integer.valueOf(jsonMedioPago.get("codigoCredito").toString().trim()));

                if (!tengoCreditoID){
                    sql = "select z_stechcreditos_id from z_stechcreditos where value ='" + tkMovPago.getSC_CodigoCredito() + "'";
                    int idAux = DB.getSQLValueEx(null, sql);
                    if (idAux > 0){
                        tkMovPago.setZ_StechCreditos_ID(idAux);
                    }
                }
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

            // descuentoAfam
            if (!jsonMedioPago.get("descuentoAfam").equals(JSONObject.NULL)){
                tkMovPago.setSC_DescuentoAfam(BigDecimal.valueOf(jsonMedioPago.getDouble("descuentoAfam")));
            }

            // descuentoInclusionFinanciera
            if (!jsonMedioPago.get("descuentoInclusionFinanciera").equals(JSONObject.NULL)){
                tkMovPago.setSC_DescuentoIncFin(BigDecimal.valueOf(jsonMedioPago.getDouble("descuentoInclusionFinanciera")));
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
    private void setJsonDetalleDto(MZStechTMPMovDet tkMovDet, JSONObject jsonDetDto) {

        MZStechTMPMovDetDtos tkMovDetDtos = null;

        try{

            tkMovDetDtos = new MZStechTMPMovDetDtos(getCtx(), 0, get_TrxName());
            tkMovDetDtos.setZ_Stech_TMP_Mov_ID(tkMovDet.getZ_Stech_TMP_Mov_ID());
            tkMovDetDtos.set_ValueOfColumn("AD_Client_ID", tkMovDet.getAD_Client_ID());
            tkMovDetDtos.setAD_Org_ID(tkMovDet.getAD_Org_ID());
            tkMovDetDtos.setZ_Stech_TMP_MovDet_ID(tkMovDet.get_ID());
            tkMovDetDtos.setZ_StechInterfaceVta_ID(tkMovDet.getZ_StechInterfaceVta_ID());
            tkMovDetDtos.setJSonBody(jsonDetDto.toString());
            tkMovDetDtos.setDateTrx(tkMovDet.getDateTrx());

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
     * @param jsonMovimiento
     * @return
     */
    private MZStechTMPMov setJsonMovimiento(MZScanntechConfigOrg configOrg, MZScanntechConfigCaja configCaja, JSONObject jsonMovimiento) {

        MZStechTMPMov tkMov = null;

        try{

            tkMov = new MZStechTMPMov(getCtx(), 0, get_TrxName());
            tkMov.set_ValueOfColumn("AD_Client_ID", this.getAD_Client_ID());
            tkMov.setAD_Org_ID(configOrg.getAD_OrgTrx_ID());
            tkMov.setZ_StechInterfaceVta_ID(this.get_ID());
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
            tkMov.setDateTrx(TimeUtil.trunc(tkMov.getSC_FechaOperacion(), TimeUtil.TRUNC_DAY));

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

            // nombreFactura
            if (!jsonMovimiento.get("nombreFactura").equals(JSONObject.NULL)){
                tkMov.setSC_NombreFactura(jsonMovimiento.get("nombreFactura").toString().trim());
            }

            // rucFactura
            if (!jsonMovimiento.get("rucFactura").equals(JSONObject.NULL)){
                tkMov.setSC_RucFactura(jsonMovimiento.get("rucFactura").toString().trim());
            }

            // direccionFactura
            if (!jsonMovimiento.get("direccionFactura").equals(JSONObject.NULL)){
                tkMov.setSC_DireccionFactura(jsonMovimiento.get("direccionFactura").toString().trim());
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
    private MZStechTMPMovDet setJsonDetalleMov(MZStechTMPMov tkMov, JSONObject jsonDetalle) {

        MZStechTMPMovDet tkMovDet = null;

        try{

            tkMovDet = new MZStechTMPMovDet(getCtx(), 0, get_TrxName());
            tkMovDet.setZ_Stech_TMP_Mov_ID(tkMov.get_ID());
            tkMovDet.set_ValueOfColumn("AD_Client_ID", tkMov.getAD_Client_ID());
            tkMovDet.setAD_Org_ID(tkMov.getAD_Org_ID());
            tkMovDet.setZ_StechInterfaceVta_ID(tkMov.getZ_StechInterfaceVta_ID());
            tkMovDet.setJSonBody(jsonDetalle.toString());
            tkMovDet.setDateTrx(tkMov.getDateTrx());

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


                // Obtengo ID del producto en ADempiere, según codigo de articulo leído desde Scanntech.
                // Primero le saco los ceros por delante del codigo que le pone Scanntech.
                String codArticulo = tkMovDet.getSC_CodigoArticulo();
                if (codArticulo.indexOf("00000000000000") == 0){
                    codArticulo = codArticulo.replace("00000000000000","");
                }
                else if (codArticulo.indexOf("0000000000000") == 0){
                    codArticulo = codArticulo.replace("0000000000000","");
                }
                else if (codArticulo.indexOf("000000000000") == 0){
                    codArticulo = codArticulo.replace("000000000000","");
                }
                else if (codArticulo.indexOf("00000000000") == 0){
                    codArticulo = codArticulo.replace("00000000000","");
                }
                else if (codArticulo.indexOf("0000000000") == 0){
                    codArticulo = codArticulo.replace("0000000000","");
                }
                else if (codArticulo.indexOf("000000000") == 0){
                    codArticulo = codArticulo.replace("000000000","");
                }
                else if (codArticulo.indexOf("00000000") == 0){
                    codArticulo = codArticulo.replace("00000000","");
                }
                else if (codArticulo.indexOf("0000000") == 0){
                    codArticulo = codArticulo.replace("0000000","");
                }
                else if (codArticulo.indexOf("000000") == 0){
                    codArticulo = codArticulo.replace("000000","");
                }
                else if (codArticulo.indexOf("00000") == 0){
                    codArticulo = codArticulo.replace("00000","");
                }
                else if (codArticulo.indexOf("0000") == 0){
                    codArticulo = codArticulo.replace("0000","");
                }
                else if (codArticulo.indexOf("000") == 0){
                    codArticulo = codArticulo.replace("000","");
                }
                String sql = " select m_product_id from m_product where value='" + codArticulo + "'";
                int mProductID = DB.getSQLValueEx(null, sql);
                if (mProductID > 0){
                    tkMovDet.setM_Product_ID(mProductID);
                }

            }

            // codigoBarras
            if (!jsonDetalle.get("codigoBarras").equals(JSONObject.NULL)){
                tkMovDet.setSC_CodigoBarras(jsonDetalle.get("codigoBarras").toString().trim());

                // Obtengo ID del codigo de barras en ADempiere
                String sql = " select z_productoupc_id from z_productoupc where upc='" + tkMovDet.getSC_CodigoBarras() + "'";
                int zProductoUpcID = DB.getSQLValueEx(null, sql);
                if (zProductoUpcID > 0){
                    tkMovDet.setZ_ProductoUPC_ID(zProductoUpcID);
                }
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

            // codigoIVA
            if (!jsonDetalle.get("codigoIVA").equals(JSONObject.NULL)){
                tkMovDet.setSC_CodigoIVA(jsonDetalle.get("codigoIVA").toString().trim());
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

            // Hago un update para forzar el trigger
            tkMovDet.set_ValueOfColumn("CreatedBy", this.getCreatedBy());
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

    /***
     * Obtengo ventas por credito de la casa desde POS y genero los documentos correspondientes en ADempiere.
     * Xpande. Created by Gabriel Vila on 4/30/20.
     */
    private void setVentasCredito() {

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            // Producto a considerar en los documentos de venta
            if (this.scanntechConfig.getProdVtasCredPOS_ID() <= 0){
                return;
            }
            MProduct product = new MProduct(getCtx(), this.scanntechConfig.getProdVtasCredPOS_ID(), null);
            if ((product == null) || (product.get_ID() <= 0)){
                return;
            }

            // Impuesto asociado al producto
            MTaxCategory taxCategory = (MTaxCategory) product.getC_TaxCategory();
            if ((taxCategory == null) || (taxCategory.get_ID() <= 0)){
                return;
            }

            // Obtengo impuesto asociado al producto para este tipo de documentos
            MTax taxProduct = taxCategory.getDefaultTax();
            if ((taxProduct == null) || (taxProduct.get_ID() <= 0)){
                return;
            }

            sql = " select mov.sc_tipocfe, mov.sc_rucfactura, mov.sc_seriecfe, mov.sc_numerooperacion, " +
                    " mov.SC_CodigoMoneda, mov.SC_CodigoCaja, mov.Z_Stech_TMP_Mov_ID, mov.sc_numeromov, " +
                    " sum(coalesce(a.sc_importe,0) + coalesce(a.sc_descuentoafam,0) + coalesce(a.sc_descuentoincfin,0)) as sc_importe " +
                    " from z_stech_tmp_movpago a " +
                    " inner join z_stech_tmp_mov mov on a.z_stech_tmp_mov_id = mov.z_stech_tmp_mov_id " +
                    " inner join z_stechcreditos mp on a.z_stechcreditos_id = mp.z_stechcreditos_id " +
                    " where mov.ad_org_id =" + this.getAD_Org_ID() +
                    " and mov.sc_tipooperacion='VENTA' " +
                    " and mov.z_stechinterfacevta_id =" + this.get_ID() +
                    " and mp.ventacredito ='Y' " +
                    " group by 1,2,3,4,5,6,7,8 ";

            pstmt = DB.prepareStatement(sql, get_TrxName());
            rs = pstmt.executeQuery();

            while(rs.next()){

                int cCurrencyID = 142;
                if ((rs.getString("SC_CodigoMoneda") != null) && (!rs.getString("SC_CodigoMoneda").equalsIgnoreCase("858"))){
                    cCurrencyID = 100;
                }

                MZStechVtaCtaCte stechVtaCtaCte = new MZStechVtaCtaCte(getCtx(), 0, get_TrxName());
                stechVtaCtaCte.setZ_StechInterfaceVta_ID(this.get_ID());
                stechVtaCtaCte.setZ_Stech_TK_Mov_ID(rs.getInt("Z_Stech_TMP_Mov_ID"));
                stechVtaCtaCte.setAD_Org_ID(this.getAD_Org_ID());
                stechVtaCtaCte.set_ValueOfColumn("AD_Client_ID", this.scanntechConfig.getAD_Client_ID());
                stechVtaCtaCte.setDateTrx(this.getDateTrx());
                stechVtaCtaCte.setSC_TipoCfe(rs.getInt("sc_tipocfe"));
                stechVtaCtaCte.setSC_RucFactura(rs.getString("sc_rucfactura"));
                stechVtaCtaCte.setSC_SerieCfe(rs.getString("sc_seriecfe"));
                stechVtaCtaCte.setSC_NumeroOperacion(rs.getString("sc_numerooperacion"));
                stechVtaCtaCte.setSC_Importe(rs.getBigDecimal("sc_importe"));
                stechVtaCtaCte.setSC_CodigoCaja(rs.getInt("SC_CodigoCaja"));
                stechVtaCtaCte.setC_Currency_ID(cCurrencyID);
                stechVtaCtaCte.setSC_NumeroMov(rs.getString("sc_numeromov"));
                stechVtaCtaCte.setEsVentaEmpresa(false);

                BigDecimal amtTotal = rs.getBigDecimal("sc_importe");
                if (amtTotal == null) amtTotal = Env.ZERO;
                if (amtTotal.compareTo(Env.ZERO) == 0){
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("Venta con importe CERO");
                    stechVtaCtaCte.saveEx();
                    continue;
                }
                if (amtTotal.compareTo(Env.ZERO) < 0){
                    amtTotal = amtTotal.negate();
                }

                // Determino tipo de documento segun tipo cfe obtenido
                int cDocTypeID = 0;
                String tipoCFE = String.valueOf(rs.getInt("sc_tipocfe"));
                String descCFE ="", numeroComprobante= "";

                // e-ticket o e-factura
                if ((tipoCFE.equalsIgnoreCase("101")) || (tipoCFE.equalsIgnoreCase("111"))){
                    cDocTypeID = this.scanntechConfig.getDefaultDocPosARI_ID();

                    if (tipoCFE.equalsIgnoreCase("101")){
                        descCFE ="E-Ticket";
                    }
                    else {
                        descCFE ="E-Factura";
                    }
                }
                // e-ticket nc o e-factura nc
                else if ((tipoCFE.equalsIgnoreCase("102")) || (tipoCFE.equalsIgnoreCase("112"))){
                    cDocTypeID = this.scanntechConfig.getDefaultDocPosARC_ID();

                    if (tipoCFE.equalsIgnoreCase("102")){
                        descCFE ="E-Ticket Nota de Crédito";
                    }
                    else {
                        descCFE ="E-Factura Nota de Crédito";
                    }
                }
                // e-ticket nd o e-factura nd
                else if ((tipoCFE.equalsIgnoreCase("103")) || (tipoCFE.equalsIgnoreCase("113"))){
                    cDocTypeID = this.scanntechConfig.getDefaultDocPosNDARI_ID();

                    if (tipoCFE.equalsIgnoreCase("103")){
                        descCFE ="E-Ticket Nota de Débito";
                    }
                    else {
                        descCFE ="E-Factura Nota de Débito";
                    }
                }

                if (cDocTypeID <= 0){
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("No se pudo obtener tipo de documento a considerar desde configuración scanntech");
                    stechVtaCtaCte.saveEx();
                    continue;
                }
                MDocType docType = new MDocType(getCtx(), cDocTypeID, null);

                int cBParnterID = 0;
                String whereClause = "";

                String taxID = rs.getString("sc_rucfactura");

                if ((taxID != null) && (!taxID.trim().equalsIgnoreCase(""))){
                    whereClause = " c_bpartner.taxID ='" + taxID + "'";
                }
                else {
                    // No tengo identificador de socio de negocio, no hago nada.
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("Venta sin Número de Identificación asociado");
                    stechVtaCtaCte.saveEx();
                    continue;
                }

                int[] partnersIDs = MBPartner.getAllIDs(I_C_BPartner.Table_Name, whereClause, null);
                if (partnersIDs.length <= 0){

                    // No tengo socio de negocio en ADempiere con el numero de identificación recibido.
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("No se encontró un Socio de Negocio para el nro. de identificación : " + stechVtaCtaCte.getSC_RucFactura());
                    stechVtaCtaCte.saveEx();
                    continue;
                }
                cBParnterID = partnersIDs[0];
                MBPartner partner = new MBPartner(getCtx(), cBParnterID, null);

                MBPartnerLocation[] partnerLocations = partner.getLocations(true);
                if (partnerLocations.length <= 0){
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("Socio de Negocio no tiene Localización configurada");
                    stechVtaCtaCte.saveEx();
                    continue;
                }
                MBPartnerLocation partnerLocation = partnerLocations[0];

                MPaymentTerm paymentTerm = FinancialUtils.getPaymentTermByDefault(getCtx(), null);
                if ((paymentTerm == null) || (paymentTerm.get_ID() <= 0)){
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("No se pudo obtener Término de Pago por defecto.");
                    stechVtaCtaCte.saveEx();
                    continue;
                }

                numeroComprobante = rs.getString("sc_seriecfe") + rs.getString("sc_numerooperacion");
                numeroComprobante = numeroComprobante.trim();

                MInvoice invoice = new MInvoice(getCtx(), 0, get_TrxName());
                invoice.set_ValueOfColumn("AD_Client_ID", this.scanntechConfig.getAD_Client_ID());
                invoice.setAD_Org_ID(this.getAD_Org_ID());
                invoice.setIsSOTrx(true);
                invoice.setC_DocTypeTarget_ID(cDocTypeID);
                invoice.setC_DocType_ID(cDocTypeID);
                invoice.setDocumentNo(numeroComprobante);
                invoice.setDescription("Generado desde POS. Datos CFE : " + descCFE + " " + numeroComprobante +
                        " - Ticket: " + rs.getString("sc_numeromov"));

                if (docType.getDocBaseType().equalsIgnoreCase(Doc.DOCTYPE_ARCredit)){
                    invoice.set_ValueOfColumn("ReferenciaCFE", "Referencia Comprobante POS");
                }

                Timestamp fechaDoc = TimeUtil.trunc(this.getDateTrx(), TimeUtil.TRUNC_DAY);
                invoice.setDateInvoiced(fechaDoc);
                invoice.setDateAcct(fechaDoc);
                invoice.setC_BPartner_ID(partner.get_ID());
                invoice.setC_BPartner_Location_ID(partnerLocation.get_ID());
                invoice.setC_Currency_ID(cCurrencyID);
                invoice.setPaymentRule(X_C_Invoice.PAYMENTRULE_OnCredit);
                invoice.setC_PaymentTerm_ID(paymentTerm.get_ID());

                MPriceList priceList = PriceListUtils.getPriceListByOrg(getCtx(), invoice.getAD_Client_ID(), invoice.getAD_Org_ID(),
                        invoice.getC_Currency_ID(), true, null, null);
                if ((priceList == null) || (priceList.get_ID() <= 0)){
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("No se pudo obtener Lista de Precios para esta organización - moneda.");
                    stechVtaCtaCte.saveEx();
                    continue;
                }

                invoice.setM_PriceList_ID(priceList.get_ID());
                invoice.setIsTaxIncluded(priceList.isTaxIncluded());
                invoice.set_ValueOfColumn("DocBaseType", docType.getDocBaseType());
                invoice.set_ValueOfColumn("EstadoAprobacion", "APROBADO");
                invoice.set_ValueOfColumn("TipoFormaPago", "CREDITO");
                invoice.set_ValueOfColumn("AmtSubtotal", amtTotal);
                invoice.setTotalLines(amtTotal);
                invoice.setGrandTotal(amtTotal);
                invoice.saveEx();

                // Linea de Factura
                MInvoiceLine line = new MInvoiceLine(invoice);
                line.set_ValueOfColumn("AD_Client_ID", invoice.getAD_Client_ID());
                line.set_ValueOfColumn("IsBySelection", true);
                line.setAD_Org_ID(invoice.getAD_Org_ID());
                line.setM_Product_ID(product.get_ID());
                line.setC_UOM_ID(product.getC_UOM_ID());
                line.setQtyEntered(Env.ONE);
                line.setQtyInvoiced(Env.ONE);
                line.setPriceEntered(amtTotal);
                line.setPriceActual(amtTotal);
                line.setLineNetAmt(amtTotal);
                line.setC_Tax_ID(taxProduct.get_ID());
                line.setTaxAmt();
                line.setLineNetAmt();
                line.set_ValueOfColumn("AmtSubTotal", invoice.getTotalLines());
                line.saveEx();

                if (!invoice.processIt(DocAction.ACTION_Complete)){
                    String message = "";
                    if (invoice.getProcessMsg() != null) message = invoice.getProcessMsg();
                    System.out.println("No se pudo completar Invoice en Venta Crédito Scanntech : " + message);

                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("Error al completar Invoice : " + message);
                }
                else{
                    invoice.saveEx();
                    stechVtaCtaCte.setIsExecuted(true);

                }
                if (invoice.get_ID() > 0){
                    stechVtaCtaCte.setC_Invoice_ID(invoice.get_ID());
                }

                stechVtaCtaCte.setC_BPartner_ID(invoice.getC_BPartner_ID());
                stechVtaCtaCte.setC_BPartner_Location_ID(invoice.getC_BPartner_Location_ID());
                stechVtaCtaCte.saveEx();
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
            rs = null; pstmt = null;
        }
    }

    /***
     * Obtengo información de comprobantes de venta: eFactura, eFacturaNC y eFacturaND desde POS.
     * No se generan invoices en el sistema. Esto es solo para luego poder utilizar este info en
     * la generación de formularios para DGI (Ej: 2/181)
     * Xpande. Created by Gabriel Vila on 7/10/20.
     */
    private void setVentasComprobantes() {

        String sql = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            sql = "";

            sql = " select mov.sc_tipocfe, mov.sc_rucfactura, mov.sc_seriecfe, mov.sc_numerooperacion, " +
                    " mov.SC_CodigoMoneda, mov.SC_CodigoCaja, mov.Z_Stech_TMP_Mov_ID, mov.sc_numeromov, " +
                    " mov.sc_total as sc_importe, sdgi.c_doctype_id " +
                    " from z_stech_tmp_mov mov " +
                    " left outer join z_cfe_configdocdgi dgi on cast(mov.sc_tipocfe as character varying(20)) = dgi.codigodgi " +
                    " left outer join z_cfe_configdocsend sdgi on (dgi.z_cfe_configdocdgi_id = sdgi.z_cfe_configdocdgi_id and sdgi.ad_orgtrx_id = mov.ad_org_id) " +
                    " where mov.ad_org_id =" + this.getAD_Org_ID() +
                    " and mov.sc_tipooperacion='VENTA' " +
                    " and mov.z_stechinterfacevta_id =" + this.get_ID() +
                    " and mov.sc_tipocfe in (111, 112, 113) ";

            pstmt = DB.prepareStatement(sql, get_TrxName());
            rs = pstmt.executeQuery();

            while(rs.next()){

                int cCurrencyID = 142;
                if ((rs.getString("SC_CodigoMoneda") != null) && (!rs.getString("SC_CodigoMoneda").equalsIgnoreCase("858"))){
                    cCurrencyID = 100;
                }

                MZStechVtaCtaCte stechVtaCtaCte = new MZStechVtaCtaCte(getCtx(), 0, get_TrxName());
                stechVtaCtaCte.setZ_StechInterfaceVta_ID(this.get_ID());
                stechVtaCtaCte.setZ_Stech_TK_Mov_ID(rs.getInt("Z_Stech_TMP_Mov_ID"));
                stechVtaCtaCte.setAD_Org_ID(this.getAD_Org_ID());
                stechVtaCtaCte.set_ValueOfColumn("AD_Client_ID", this.scanntechConfig.getAD_Client_ID());
                stechVtaCtaCte.setDateTrx(this.getDateTrx());
                stechVtaCtaCte.setSC_TipoCfe(rs.getInt("sc_tipocfe"));
                stechVtaCtaCte.setSC_RucFactura(rs.getString("sc_rucfactura"));
                stechVtaCtaCte.setSC_SerieCfe(rs.getString("sc_seriecfe"));
                stechVtaCtaCte.setSC_NumeroOperacion(rs.getString("sc_numerooperacion"));
                stechVtaCtaCte.setSC_Importe(rs.getBigDecimal("sc_importe"));
                stechVtaCtaCte.setSC_CodigoCaja(rs.getInt("SC_CodigoCaja"));
                stechVtaCtaCte.setC_Currency_ID(cCurrencyID);
                stechVtaCtaCte.setSC_NumeroMov(rs.getString("sc_numeromov"));
                stechVtaCtaCte.setIsExecuted(true);
                stechVtaCtaCte.setEsVentaEmpresa(true);

                if (rs.getInt("c_doctype_id") > 0){
                    stechVtaCtaCte.setC_DocTypeTarget_ID(rs.getInt("c_doctype_id"));
                }

                BigDecimal amtTotal = rs.getBigDecimal("sc_importe");
                if (amtTotal == null) amtTotal = Env.ZERO;
                if (amtTotal.compareTo(Env.ZERO) == 0){
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("Venta con importe CERO");
                    stechVtaCtaCte.saveEx();
                    continue;
                }
                if (amtTotal.compareTo(Env.ZERO) < 0){
                    amtTotal = amtTotal.negate();
                }

                // Determino tipo de documento segun tipo cfe obtenido
                String tipoCFE = String.valueOf(rs.getInt("sc_tipocfe"));
                String descCFE ="", numeroComprobante= "";

                // e-ticket o e-factura
                if ((tipoCFE.equalsIgnoreCase("101")) || (tipoCFE.equalsIgnoreCase("111"))){
                    if (tipoCFE.equalsIgnoreCase("101")){
                        descCFE ="E-Ticket";
                    }
                    else {
                        descCFE ="E-Factura";
                    }
                }
                // e-ticket nc o e-factura nc
                else if ((tipoCFE.equalsIgnoreCase("102")) || (tipoCFE.equalsIgnoreCase("112"))){
                    if (tipoCFE.equalsIgnoreCase("102")){
                        descCFE ="E-Ticket Nota de Crédito";
                    }
                    else {
                        descCFE ="E-Factura Nota de Crédito";
                    }
                }
                // e-ticket nd o e-factura nd
                else if ((tipoCFE.equalsIgnoreCase("103")) || (tipoCFE.equalsIgnoreCase("113"))){
                    if (tipoCFE.equalsIgnoreCase("103")){
                        descCFE ="E-Ticket Nota de Débito";
                    }
                    else {
                        descCFE ="E-Factura Nota de Débito";
                    }
                }

                int cBParnterID = 0;
                String whereClause = "";

                String taxID = rs.getString("sc_rucfactura");

                if ((taxID != null) && (!taxID.trim().equalsIgnoreCase(""))){
                    whereClause = " c_bpartner.taxID ='" + taxID + "'";
                }
                else {
                    // No tengo identificador de socio de negocio, no hago nada.
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("Venta sin Número de Identificación asociado");
                    stechVtaCtaCte.saveEx();
                    continue;
                }

                int[] partnersIDs = MBPartner.getAllIDs(I_C_BPartner.Table_Name, whereClause, null);
                if (partnersIDs.length <= 0){

                    // No tengo socio de negocio en ADempiere con el numero de identificación recibido.
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("No se encontró un Socio de Negocio para el nro. de identificación : " + stechVtaCtaCte.getSC_RucFactura());
                    stechVtaCtaCte.saveEx();
                    continue;
                }
                cBParnterID = partnersIDs[0];
                MBPartner partner = new MBPartner(getCtx(), cBParnterID, null);

                MBPartnerLocation[] partnerLocations = partner.getLocations(true);
                if (partnerLocations.length <= 0){
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("Socio de Negocio no tiene Localización configurada");
                    stechVtaCtaCte.saveEx();
                    continue;
                }
                MBPartnerLocation partnerLocation = partnerLocations[0];

                MPaymentTerm paymentTerm = FinancialUtils.getPaymentTermByDefault(getCtx(), null);
                if ((paymentTerm == null) || (paymentTerm.get_ID() <= 0)){
                    stechVtaCtaCte.setIsExecuted(false);
                    stechVtaCtaCte.setErrorMsg("No se pudo obtener Término de Pago por defecto.");
                    stechVtaCtaCte.saveEx();
                    continue;
                }

                stechVtaCtaCte.setC_BPartner_ID(partner.get_ID());
                stechVtaCtaCte.setC_BPartner_Location_ID(partnerLocation.get_ID());
                stechVtaCtaCte.saveEx();
            }
        }
        catch (Exception e){
            throw new AdempiereException(e);
        }
        finally {
            DB.close(rs, pstmt);
            rs = null; pstmt = null;
        }
    }


    /***
     * Obtiene y retorna lista de modelos de venta por cuenta corriente para esta interface de venta.
     * Xpande. Created by Gabriel Vila on 5/7/20.
     * @return
     */
    public List<MZStechVtaCtaCte> getVtasCtaCte() {

        String whereClause = X_Z_StechVtaCtaCte.COLUMNNAME_Z_StechInterfaceVta_ID + " =" + this.get_ID();

        List<MZStechVtaCtaCte> lines = new Query(getCtx(), I_Z_StechVtaCtaCte.Table_Name, whereClause, get_TrxName()).list();

        return lines;
    }
}
