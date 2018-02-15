package org.xpande.stech.model;

import org.compiere.model.*;
import org.compiere.util.DB;
import org.xpande.core.model.I_Z_ProductoUPC;
import org.xpande.core.model.MZProductoUPC;

import java.util.List;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 7/21/17.
 */
public class ValidatorScanntech implements ModelValidator {

    private int adClientID = 0;

    @Override
    public void initialize(ModelValidationEngine engine, MClient client) {

        // Guardo compañia
        if (client != null){
            this.adClientID = client.get_ID();
        }

        // DB Validations
        engine.addModelChange(I_M_Product.Table_Name, this);
        engine.addModelChange(I_Z_ProductoUPC.Table_Name, this);
        engine.addModelChange(I_M_ProductPrice.Table_Name, this);
        engine.addModelChange(I_C_BPartner.Table_Name, this);
    }

    @Override
    public int getAD_Client_ID() {
        return this.adClientID;
    }

    @Override
    public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
        return null;
    }

    @Override
    public String modelChange(PO po, int type) throws Exception {

        if (po.get_TableName().equalsIgnoreCase(I_M_Product.Table_Name)){
            return modelChange((MProduct) po, type);
        }
        else if (po.get_TableName().equalsIgnoreCase(I_Z_ProductoUPC.Table_Name)){
            return modelChange((MZProductoUPC) po, type);
        }
        else if (po.get_TableName().equalsIgnoreCase(I_M_ProductPrice.Table_Name)){
            return modelChange((MProductPrice) po, type);
        }
        else if (po.get_TableName().equalsIgnoreCase(I_C_BPartner.Table_Name)){
            return modelChange((MBPartner) po, type);
        }

        return null;
    }

    @Override
    public String docValidate(PO po, int timing) {
        return null;
    }

    /***
     * Validaciones para el modelo de Productos
     * Xpande. Created by Gabriel Vila on 6/30/17.
     * @param model
     * @param type
     * @return
     * @throws Exception
     */
    public String modelChange(MProduct model, int type) throws Exception {

        String mensaje = null;

        // Interface salida POS
        if ((type == ModelValidator.TYPE_AFTER_NEW) || (type == ModelValidator.TYPE_AFTER_CHANGE)){

            // Proceso para cada organización asociada a la configuracion de scanntech
            // Manejo una marca de interface por cada organización de este pos
            MZScanntechConfig scanntechConfig = MZScanntechConfig.getDefault(model.getCtx(), model.get_TrxName());
            List<MZScanntechConfigOrg> orgs = scanntechConfig.getOrganization();

            for (MZScanntechConfigOrg configOrg: orgs){

                if (type == ModelValidator.TYPE_AFTER_NEW){

                    // Si el producto no se vende o no esta activo al momento de crearse, no hago nada
                    if ((!model.isSold()) || (!model.isActive())){
                        return mensaje;
                    }

                    // Marca de Creacion de Producto
                    MZStechInterfaceOut scanntechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                    scanntechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_CREATE);
                    scanntechInterfaceOut.setSeqNo(10);
                    scanntechInterfaceOut.setAD_Table_ID(I_M_Product.Table_ID);
                    scanntechInterfaceOut.setRecord_ID(model.get_ID());
                    scanntechInterfaceOut.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());
                    scanntechInterfaceOut.saveEx();

                }
                else if (type == ModelValidator.TYPE_AFTER_CHANGE){

                    // Pregunto por los campos cuyo cambio requiere informar al POS
                    if ((model.is_ValueChanged("C_UOM_ID")) || (model.is_ValueChanged("M_Product_Tandem_ID"))
                            || (model.is_ValueChanged("Description")) || (model.is_ValueChanged("C_TaxCategory_ID"))
                            || (model.is_ValueChanged("Name")) || (model.is_ValueChanged("EsProductoBalanza"))
                            || (model.is_ValueChanged("Z_ProductoSeccion_ID")) || (model.is_ValueChanged("Z_ProductoRubro_ID"))
                            || (model.is_ValueChanged("Z_ProductoFamilia_ID")) || (model.is_ValueChanged("Z_ProductoSubfamilia_ID"))
                            || (model.is_ValueChanged("IsBonificable"))){


                        if ((model.is_ValueChanged("IsActive")) || (model.is_ValueChanged(X_M_Product.COLUMNNAME_IsSold))){

                            // Si desactiva o marca producto como no vendible
                            if ((!model.isActive()) || (!model.isSold())){
                                // Marca Delete
                                MZStechInterfaceOut scanntechInterfaceOut = MZStechInterfaceOut.getRecord(model.getCtx(), I_M_Product.Table_ID, model.get_ID(),
                                                                                                          configOrg.getAD_OrgTrx_ID(), model.get_TrxName());
                                if ((scanntechInterfaceOut != null) && (scanntechInterfaceOut.get_ID() > 0)){
                                    // Proceso segun marca que ya tenía este socio antes de su actualización.
                                    // Si marca anterior es CREATE
                                    if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){
                                        // Elimino marca anterior de create, ya que finalmente este socio de negocio no va al POS
                                        scanntechInterfaceOut.deleteEx(true);
                                        //return mensaje;
                                        continue;
                                    }
                                    else if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){
                                        // Si marca anterior es DELETEAR, es porque el socio se inactivo anteriormente.
                                        // No hago nada y respeto primer marca.
                                        //return mensaje;
                                        continue;
                                    }
                                }
                                // Si no tengo marca de delete, la creo ahora.
                                if ((scanntechInterfaceOut == null) || (scanntechInterfaceOut.get_ID() <= 0)){
                                    scanntechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                                    scanntechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_DELETE);
                                    scanntechInterfaceOut.setAD_Table_ID(I_M_Product.Table_ID);
                                    scanntechInterfaceOut.setSeqNo(10);
                                    scanntechInterfaceOut.setRecord_ID(model.get_ID());
                                    scanntechInterfaceOut.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());
                                    scanntechInterfaceOut.saveEx();
                                }
                            }
                            else{
                                // Si es producto esta activo y se vende
                                if (model.isActive() && model.isSold()){
                                    // Doy de alta
                                    MZStechInterfaceOut scanntechInterfaceOut = MZStechInterfaceOut.getRecord(model.getCtx(), I_M_Product.Table_ID, model.get_ID(),
                                            configOrg.getAD_OrgTrx_ID(), model.get_TrxName());
                                    if ((scanntechInterfaceOut != null) && (scanntechInterfaceOut.get_ID() > 0)){
                                        // Proceso segun marca que ya tenía este socio antes de su actualización.
                                        // Si marca anterior es CREATE
                                        if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){
                                            // No hago nada
                                            //return mensaje;
                                            continue;
                                        }
                                        else if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){
                                            // Si marca anterior es DELETEAR, es porque el socio se inactivo anteriormente.
                                            // Elimino marca anterior de create, ya que finalmente este socio de negocio va al POS
                                            scanntechInterfaceOut.deleteEx(true);
                                            scanntechInterfaceOut = null;
                                        }
                                    }
                                    // Si no tengo marca, la creo ahora.
                                    if ((scanntechInterfaceOut == null) || (scanntechInterfaceOut.get_ID() <= 0)){
                                        scanntechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                                        scanntechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_CREATE);
                                        scanntechInterfaceOut.setSeqNo(10);
                                        scanntechInterfaceOut.setAD_Table_ID(I_M_Product.Table_ID);
                                        scanntechInterfaceOut.setRecord_ID(model.get_ID());
                                        scanntechInterfaceOut.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());
                                        scanntechInterfaceOut.saveEx();
                                    }
                                }
                            }

                        }

                        // Marca Update
                        MZStechInterfaceOut scanntechInterfaceOut = MZStechInterfaceOut.getRecord(model.getCtx(), I_M_Product.Table_ID, model.get_ID(),
                                configOrg.getAD_OrgTrx_ID(), model.get_TrxName());
                        if ((scanntechInterfaceOut != null) && (scanntechInterfaceOut.get_ID() > 0)){
                            // Proceso segun marca que ya tenía este producto antes de su actualización.
                            // Si marca anterior es CREATE
                            if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){
                                // No hago nada y respeto primer marca
                                //return mensaje;
                                continue;
                            }
                            else if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){
                                // Si marca anterior es DELETEAR, es porque el producto se inactivo anteriormente.
                                // Si este producto sigue estando inactivo
                                if (!model.isActive()){
                                    // No hago nada y respeto primer marca.
                                    //return mensaje;
                                    continue;
                                }
                            }
                        }

                        // Si no tengo marca de update, la creo ahora.
                        if ((scanntechInterfaceOut == null) || (scanntechInterfaceOut.get_ID() <= 0)){
                            // No existe aun marca de UPDATE sobre este producto, la creo ahora.
                            scanntechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                            scanntechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_UPDATE);
                            scanntechInterfaceOut.setAD_Table_ID(I_M_Product.Table_ID);
                            scanntechInterfaceOut.setSeqNo(20);
                            scanntechInterfaceOut.setRecord_ID(model.get_ID());
                            scanntechInterfaceOut.setIsPriceChanged(false);
                            scanntechInterfaceOut.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());
                            scanntechInterfaceOut.saveEx();
                        }

                        // Marca de update para Tandem si cambio
                        if (model.is_ValueChanged("M_Product_Tandem_ID")){
                            scanntechInterfaceOut.setIsTandemChanged(true);
                            // Guardo tandem anterior en caso de haber cambio
                            if (model.get_ValueOldAsInt("M_Product_Tandem_ID") > 0){
                                if (scanntechInterfaceOut.getM_Product_Tandem_ID() <= 0){
                                    scanntechInterfaceOut.setM_Product_Tandem_ID(model.get_ValueOldAsInt("M_Product_Tandem_ID"));
                                }
                            }
                            scanntechInterfaceOut.saveEx();
                        }

                        //mensaje = scanntechInterfaceOut.execute(); // Info a cajas del pos en este momento.
                    }

                }

            }

        }

        return mensaje;
    }


    /***
     * Validaciones para el modelo de Códigos de Barras de Productos.
     * Xpande. Created by Gabriel Vila on 6/30/17.
     * @param model
     * @param type
     * @return
     * @throws Exception
     */
    public String modelChange(MZProductoUPC model, int type) throws Exception {

        String mensaje = null;

        MProduct product = (MProduct)model.getM_Product();

        // Si el producto no se vende o no esta activo, no hago nada
        if ((!product.isSold()) || (!product.isActive())){
            return mensaje;
        }

        // Proceso para cada organización asociada a la configuracion de scanntech
        // Manejo una marca de interface por cada organización de este pos
        MZScanntechConfig scanntechConfig = MZScanntechConfig.getDefault(model.getCtx(), model.get_TrxName());
        List<MZScanntechConfigOrg> orgs = scanntechConfig.getOrganization();

        for (MZScanntechConfigOrg configOrg: orgs){

            if (type == ModelValidator.TYPE_AFTER_NEW){
                // Marca Create
                MZStechInterfaceOut scanntechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                scanntechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_CREATE);
                scanntechInterfaceOut.setAD_Table_ID(I_Z_ProductoUPC.Table_ID);
                scanntechInterfaceOut.setRecord_ID(model.get_ID());
                scanntechInterfaceOut.setSeqNo(15);
                scanntechInterfaceOut.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());
                scanntechInterfaceOut.saveEx();

            }
            else if (type == ModelValidator.TYPE_AFTER_DELETE){

                // Marca Update si tengo UPC
                if (model.getUPC() != null){
                    MZStechInterfaceOut scanntechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                    scanntechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_DELETE);
                    scanntechInterfaceOut.setAD_Table_ID(I_Z_ProductoUPC.Table_ID);
                    scanntechInterfaceOut.setRecord_ID(model.get_ID());
                    scanntechInterfaceOut.setDescription(model.getUPC().trim());
                    scanntechInterfaceOut.setSeqNo(13);
                    scanntechInterfaceOut.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());
                    scanntechInterfaceOut.saveEx();
                }
            }
        }

        return mensaje;
    }

    /***
     * Validaciones para el modelo de Precios de Productos
     * Xpande. Created by Gabriel Vila on 6/30/17.
     * @param model
     * @param type
     * @return
     * @throws Exception
     */
    public String modelChange(MProductPrice model, int type) throws Exception {

        String mensaje = null;

        // Retail. Interface salida POS
        if ((type == ModelValidator.TYPE_AFTER_NEW) || (type == ModelValidator.TYPE_AFTER_CHANGE)){

            // Solo listas de ventas con organización distinto de *
            MPriceListVersion priceListVersion = new MPriceListVersion(model.getCtx(), model.getM_PriceList_Version_ID(), model.get_TrxName());
            MPriceList priceList = priceListVersion.getPriceList();
            if (!priceList.isSOPriceList()) return mensaje;
            if (priceList.getAD_Org_ID() == 0) return mensaje;

            MProduct product = (MProduct)model.getM_Product();
            // Si el producto no se vende o no esta activo, no hago nada
            if ((!product.isSold()) || (!product.isActive())){
                return mensaje;
            }

            // Debo verificar que la organizacion asociada a esta lista de precios, trabaje con el POS de Scanntech, sino es asi no hago nada
            String sql = " select count(a.*) from z_posvendororg a " +
                    " inner join z_posvendor b on a.z_posvendor_id = b.z_posvendor_id and b.value='SCANNTECH' " +
                    " where ad_orgtrx_id =" + priceList.getAD_Org_ID();
            int contador = DB.getSQLValueEx(null, sql);
            if (contador <= 0){
                return mensaje;
            }

            // Si existe, obtengo marca de interface de este producto
            MZStechInterfaceOut scanntechInterfaceOut = MZStechInterfaceOut.getRecord(model.getCtx(), I_M_Product.Table_ID, product.get_ID(),
                    priceList.getAD_Org_ID(), model.get_TrxName());
            if ((scanntechInterfaceOut != null) && (scanntechInterfaceOut.get_ID() > 0)){
                // Proceso segun marca que ya tenía este producto antes de su actualización.
                // Si marca anterior es CREATE
                if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){
                    // No hago nada y respeto primer marca
                    return mensaje;
                }
                else if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){
                    // Si marca anterior es DELETEAR, es porque el producto se inactivo anteriormente.
                    // Si este producto sigue estando inactivo
                    if (!model.isActive()){
                        // No hago nada y respeto primer marca.
                        return mensaje;
                    }
                }
            }

            // Si no tengo marca de update, la creo ahora.
            if ((scanntechInterfaceOut == null) || (scanntechInterfaceOut.get_ID() <= 0)) {
                // No existe aun marca de UPDATE sobre este producto, la creo ahora.
                scanntechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                scanntechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_UPDATE);
                scanntechInterfaceOut.setAD_Table_ID(I_M_Product.Table_ID);
                scanntechInterfaceOut.setRecord_ID(product.get_ID());
                scanntechInterfaceOut.setAD_OrgTrx_ID(priceList.getAD_Org_ID());
                scanntechInterfaceOut.setSeqNo(30);
            }

            scanntechInterfaceOut.setIsPriceChanged(true);
            scanntechInterfaceOut.setM_PriceList_ID(priceList.get_ID());
            scanntechInterfaceOut.saveEx();

        }

        return mensaje;
    }

    /***
     * Validaciones para el modelo de Socios de Negocio
     * Xpande. Created by Gabriel Vila on 6/30/17.
     * @param model
     * @param type
     * @return
     * @throws Exception
     */
    public String modelChange(MBPartner model, int type) throws Exception {

        String mensaje = null;

        // Interface salida POS
        if ((type == ModelValidator.TYPE_AFTER_NEW) || (type == ModelValidator.TYPE_AFTER_CHANGE)){

            // Proceso para cada organización asociada a la configuracion de scanntech
            // Manejo una marca de interface por cada organización de este pos
            MZScanntechConfig scanntechConfig = MZScanntechConfig.getDefault(model.getCtx(), model.get_TrxName());
            List<MZScanntechConfigOrg> orgs = scanntechConfig.getOrganization();

            for (MZScanntechConfigOrg configOrg: orgs){

                if (type == ModelValidator.TYPE_AFTER_NEW){

                    // Si el socio no es cliente o no esta activo al momento de crearse, no hago nada
                    if ((!model.isCustomer()) || (!model.isActive())){
                        return mensaje;
                    }

                    // Marca de Creacion de Socio
                    MZStechInterfaceOut scanntechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                    scanntechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_CREATE);
                    scanntechInterfaceOut.setSeqNo(10);
                    scanntechInterfaceOut.setAD_Table_ID(I_C_BPartner.Table_ID);
                    scanntechInterfaceOut.setRecord_ID(model.get_ID());
                    scanntechInterfaceOut.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());
                    scanntechInterfaceOut.saveEx();

                }
                else if (type == ModelValidator.TYPE_AFTER_CHANGE){

                    // Pregunto por los campos cuyo cambio requiere informar al POS
                    if ((model.is_ValueChanged(X_C_BPartner.COLUMNNAME_Name)) || (model.is_ValueChanged(X_C_BPartner.COLUMNNAME_Name2))
                            || (model.is_ValueChanged(X_C_BPartner.COLUMNNAME_TaxID)) || (model.is_ValueChanged("EMail"))
                            || (model.is_ValueChanged(X_C_BPartner.COLUMNNAME_IsActive)) || (model.is_ValueChanged(X_C_BPartner.COLUMNNAME_IsCustomer))){



                        if ((model.is_ValueChanged(X_C_BPartner.COLUMNNAME_IsActive)) || (model.is_ValueChanged(X_C_BPartner.COLUMNNAME_IsCustomer))){

                            // Si desactiva cliente, mando marca de delete
                            if ((!model.isActive()) || (!model.isCustomer())){
                                // Marca Delete
                                MZStechInterfaceOut scanntechInterfaceOut = MZStechInterfaceOut.getRecord(model.getCtx(), I_C_BPartner.Table_ID, model.get_ID(),
                                        configOrg.getAD_OrgTrx_ID(), model.get_TrxName());
                                if ((scanntechInterfaceOut != null) && (scanntechInterfaceOut.get_ID() > 0)){
                                    // Proceso segun marca que ya tenía este socio antes de su actualización.
                                    // Si marca anterior es CREATE
                                    if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){
                                        // Elimino marca anterior de create, ya que finalmente este socio de negocio no va al POS
                                        scanntechInterfaceOut.deleteEx(true);
                                        //return mensaje;
                                        continue;
                                    }
                                    else if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){
                                        // Si marca anterior es DELETEAR, es porque el socio se inactivo anteriormente.
                                        // No hago nada y respeto primer marca.
                                        //return mensaje;
                                        continue;
                                    }
                                }
                                // Si no tengo marca de delete, la creo ahora.
                                if ((scanntechInterfaceOut == null) || (scanntechInterfaceOut.get_ID() <= 0)){
                                    scanntechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                                    scanntechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_DELETE);
                                    scanntechInterfaceOut.setAD_Table_ID(I_C_BPartner.Table_ID);
                                    scanntechInterfaceOut.setSeqNo(30);
                                    scanntechInterfaceOut.setRecord_ID(model.get_ID());
                                    scanntechInterfaceOut.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());
                                    scanntechInterfaceOut.saveEx();
                                }
                            }
                            else{
                                // Si es cliente y esta activo
                                if (model.isActive() && model.isCustomer()){
                                    // Doy de alta
                                    MZStechInterfaceOut scanntechInterfaceOut = MZStechInterfaceOut.getRecord(model.getCtx(), I_C_BPartner.Table_ID, model.get_ID(),
                                            configOrg.getAD_OrgTrx_ID(), model.get_TrxName());
                                    if ((scanntechInterfaceOut != null) && (scanntechInterfaceOut.get_ID() > 0)){
                                        // Proceso segun marca que ya tenía este socio antes de su actualización.
                                        // Si marca anterior es CREATE
                                        if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){
                                            // No hago nada
                                            //return mensaje;
                                            continue;
                                        }
                                        else if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){
                                            // Si marca anterior es DELETEAR, es porque el socio se inactivo anteriormente.
                                            // Elimino marca anterior de create, ya que finalmente este socio de negocio va al POS
                                            scanntechInterfaceOut.deleteEx(true);
                                            scanntechInterfaceOut = null;
                                        }
                                    }
                                    // Si no tengo marca, la creo ahora.
                                    if ((scanntechInterfaceOut == null) || (scanntechInterfaceOut.get_ID() <= 0)){
                                        scanntechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                                        scanntechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_CREATE);
                                        scanntechInterfaceOut.setSeqNo(10);
                                        scanntechInterfaceOut.setAD_Table_ID(I_C_BPartner.Table_ID);
                                        scanntechInterfaceOut.setRecord_ID(model.get_ID());
                                        scanntechInterfaceOut.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());
                                        scanntechInterfaceOut.saveEx();
                                    }
                                }
                            }

                        }
                        else{

                            // Si el socio no es cliente o no esta activo al momento de crearse, no hago nada
                            if ((!model.isCustomer()) || (!model.isActive())){
                                return mensaje;
                            }

                            // Marca Update
                            MZStechInterfaceOut scanntechInterfaceOut = MZStechInterfaceOut.getRecord(model.getCtx(), I_C_BPartner.Table_ID, model.get_ID(),
                                    configOrg.getAD_OrgTrx_ID(), model.get_TrxName());
                            if ((scanntechInterfaceOut != null) && (scanntechInterfaceOut.get_ID() > 0)){
                                // Proceso segun marca que ya tenía este producto antes de su actualización.
                                // Si marca anterior es CREATE
                                if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){
                                    // No hago nada y respeto primer marca
                                    //return mensaje;
                                    continue;
                                }
                                else if (scanntechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){
                                    // Si marca anterior es DELETEAR, es porque el producto se inactivo anteriormente.
                                    // Si este producto sigue estando inactivo
                                    if (!model.isActive()){
                                        // No hago nada y respeto primer marca.
                                        //return mensaje;
                                        continue;
                                    }
                                }
                            }
                            // Si no tengo marca de update, la creo ahora.
                            if ((scanntechInterfaceOut == null) || (scanntechInterfaceOut.get_ID() <= 0)){
                                scanntechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                                scanntechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_UPDATE);
                                scanntechInterfaceOut.setAD_Table_ID(I_C_BPartner.Table_ID);
                                scanntechInterfaceOut.setSeqNo(20);
                                scanntechInterfaceOut.setRecord_ID(model.get_ID());
                                scanntechInterfaceOut.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());
                                scanntechInterfaceOut.saveEx();
                            }

                        }
                    }
                }

            }
        }

        return mensaje;
    }

}
