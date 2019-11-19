package org.xpande.stech.model;

import org.compiere.model.MTax;

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

}
