package org.xpande.stech.model;

import org.compiere.model.MTax;
import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 11/19/19.
 */
public class MZScanntechConfigTax extends X_Z_ScanntechConfigTax {

    public MZScanntechConfigTax(Properties ctx, int Z_ScanntechConfigTax_ID, String trxName) {
        super(ctx, Z_ScanntechConfigTax_ID, trxName);
    }

    public MZScanntechConfigTax(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord) {

        // Seteo categoría de impuesto según tasa de impuesto seleccionada
        if ((newRecord) || (is_ValueChanged(X_Z_ScanntechConfigTax.COLUMNNAME_C_Tax_ID))){
            MTax tax = new MTax(getCtx(), this.getC_Tax_ID(), null);
            this.setC_TaxCategory_ID(tax.getC_TaxCategory_ID());
        }

        return true;
    }

    /***
     * Obtiene y retorna modelo ID de impuesto recibido.
     * Xpande. Created by Gabriel Vila on 4/2/20.
     * @param ctx
     * @param cTaxID
     * @param trxName
     * @return
     */
    public static MZScanntechConfigTax getByTaxID(Properties ctx, int cTaxID, String trxName){

        String whereClause = X_Z_ScanntechConfigTax.COLUMNNAME_C_Tax_ID + " =" + cTaxID;

        MZScanntechConfigTax model = new Query(ctx, I_Z_ScanntechConfigTax.Table_Name, whereClause, trxName).setOnlyActiveRecords(true).first();

        return model;
    }

}
