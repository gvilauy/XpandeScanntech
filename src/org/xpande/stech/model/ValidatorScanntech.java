package org.xpande.stech.model;

import org.compiere.model.*;
import org.compiere.util.DB;
import org.xpande.core.model.I_Z_ProductoUPC;
import org.xpande.core.model.MZProductoUPC;

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

        // Sisteco. Interface salida POS
        if ((type == ModelValidator.TYPE_AFTER_NEW) || (type == ModelValidator.TYPE_AFTER_CHANGE)){

            if (type == ModelValidator.TYPE_AFTER_NEW){

                // Si el producto no se vende o no esta activo al momento de crearse, no hago nada
                if ((!model.isSold()) || (!model.isActive())){
                    return mensaje;
                }

                // Marca de Creacion de Producto
                MZStechInterfaceOut stechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                stechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_CREATE);
                stechInterfaceOut.setSeqNo(10);
                stechInterfaceOut.setAD_Table_ID(I_M_Product.Table_ID);
                stechInterfaceOut.setRecord_ID(model.get_ID());
                stechInterfaceOut.saveEx();

            }
            else if (type == ModelValidator.TYPE_AFTER_CHANGE){

                MZStechInterfaceOut stechInterfaceOut = MZStechInterfaceOut.getRecord(model.getCtx(), I_M_Product.Table_ID, model.get_ID(), model.get_TrxName());
                if ((stechInterfaceOut != null) && (stechInterfaceOut.get_ID() > 0)){
                    // Proceso segun marca que ya tenía este producto antes de su actualización.
                    // Si marca anterior es CREATE
                    if (stechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){
                        // No hago nada y respeto primer marca
                        return mensaje;
                    }
                    else if (stechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){
                        // Si marca anterior es DELETEAR, es porque el producto se inactivo anteriormente.
                        // Si este producto sigue estando inactivo
                        if (!model.isActive()){
                            // No hago nada y respeto primer marca.
                            return mensaje;
                        }
                    }
                }

                // Para Sisteco, las actualizaciones de producto se guardan como marcas para en un proceso posterio considerarse en la generación de un archivo plano de interface.
                // Si no tengo marca de update, la creo ahora.
                if ((stechInterfaceOut == null) || (stechInterfaceOut.get_ID() <= 0)){
                    // No existe aun marca de UPDATE sobre este producto, la creo ahora.
                    stechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                    stechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_UPDATE);
                    stechInterfaceOut.setAD_Table_ID(I_M_Product.Table_ID);
                    stechInterfaceOut.setSeqNo(20);
                    stechInterfaceOut.setRecord_ID(model.get_ID());
                    stechInterfaceOut.setIsPriceChanged(false);
                    stechInterfaceOut.saveEx();
                }
                mensaje = stechInterfaceOut.execute(); // Info a cajas del pos en este momento.
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

        // Sisteco. Interface salida POS
        // Para Sisteco, solo se crean la marcas para luego considerarse en la generación del archivo plano.
        if (type == ModelValidator.TYPE_AFTER_NEW){

            // Marca Create
            MZStechInterfaceOut stechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
            stechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_CREATE);
            stechInterfaceOut.setAD_Table_ID(I_Z_ProductoUPC.Table_ID);
            stechInterfaceOut.setRecord_ID(model.get_ID());
            stechInterfaceOut.setSeqNo(15);
            stechInterfaceOut.saveEx();

            mensaje = stechInterfaceOut.execute(); // Info a cajas del pos en este momento.
        }
        else if (type == ModelValidator.TYPE_AFTER_DELETE){

            // Marca Update
            MZStechInterfaceOut stechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
            stechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_DELETE);
            stechInterfaceOut.setAD_Table_ID(I_Z_ProductoUPC.Table_ID);
            stechInterfaceOut.setRecord_ID(model.get_ID());
            stechInterfaceOut.setSeqNo(13);
            stechInterfaceOut.saveEx();

            mensaje = stechInterfaceOut.execute(); // Info a cajas del pos en este momento.
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

            // Debo verificar que la organizacion asociada a esta lista de precios, trabaje con el POS de Sisteco, sino es asi no hago nada
            String sql = " select count(a.*) from z_posvendororg a " +
                    " inner join z_posvendor b on a.z_posvendor_id = b.z_posvendor_id and b.value='SISTECO' " +
                    " where ad_orgtrx_id =" + priceList.getAD_Org_ID();
            int contador = DB.getSQLValueEx(null, sql);
            if (contador <= 0){
                return mensaje;
            }

            if (type == ModelValidator.TYPE_AFTER_NEW){

                MZStechInterfaceOut stechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                stechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_UPDATE);
                stechInterfaceOut.setAD_Table_ID(I_M_Product.Table_ID);
                stechInterfaceOut.setRecord_ID(product.get_ID());
                stechInterfaceOut.setSeqNo(30);
                stechInterfaceOut.setIsPriceChanged(true);
                stechInterfaceOut.setAD_OrgTrx_ID(priceList.getAD_Org_ID());
                stechInterfaceOut.saveEx();

            }
            else if (type == ModelValidator.TYPE_AFTER_CHANGE){

                MZStechInterfaceOut stechInterfaceOut = MZStechInterfaceOut.getRecord(model.getCtx(), I_M_Product.Table_ID, model.get_ID(), model.get_TrxName());
                if ((stechInterfaceOut != null) && (stechInterfaceOut.get_ID() > 0)){
                    // Proceso segun marca que ya tenía este producto antes de su actualización.
                    // Si marca anterior es CREATE
                    if (stechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_CREATE)){
                        // No hago nada y respeto primer marca
                        return mensaje;
                    }
                    else if (stechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_DELETE)){
                        // Si marca anterior es DELETEAR, es porque el producto se inactivo anteriormente.
                        // Si este producto sigue estando inactivo
                        if (!model.isActive()){
                            // No hago nada y respeto primer marca.
                            return mensaje;
                        }
                    }
                    else if (stechInterfaceOut.getCRUDType().equalsIgnoreCase(X_Z_StechInterfaceOut.CRUDTYPE_UPDATE)){
                        // Si tenia marca anterior de cambio de precio para este producto, no hago nada
                        if (stechInterfaceOut.isPriceChanged()){
                            return  mensaje;
                        }
                    }
                }

                // Para proveedores de pos, los cambios de precios se marcan pero se procesan luego (primero se imprimen etiquetas antes de enviar
                // cambios de precios a las cajas).
                stechInterfaceOut = new MZStechInterfaceOut(model.getCtx(), 0, model.get_TrxName());
                stechInterfaceOut.setCRUDType(X_Z_StechInterfaceOut.CRUDTYPE_UPDATE);
                stechInterfaceOut.setAD_Table_ID(I_M_Product.Table_ID);
                stechInterfaceOut.setRecord_ID(product.get_ID());
                stechInterfaceOut.setSeqNo(30);
                stechInterfaceOut.setIsPriceChanged(true);
                stechInterfaceOut.setAD_OrgTrx_ID(priceList.getAD_Org_ID());
                stechInterfaceOut.saveEx();
            }
        }

        return mensaje;
    }
    
    
}