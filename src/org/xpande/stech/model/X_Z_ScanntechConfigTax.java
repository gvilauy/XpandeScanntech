/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.										*
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.xpande.stech.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for Z_ScanntechConfigTax
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_ScanntechConfigTax extends PO implements I_Z_ScanntechConfigTax, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200525L;

    /** Standard Constructor */
    public X_Z_ScanntechConfigTax (Properties ctx, int Z_ScanntechConfigTax_ID, String trxName)
    {
      super (ctx, Z_ScanntechConfigTax_ID, trxName);
      /** if (Z_ScanntechConfigTax_ID == 0)
        {
			setAplicaInterface (false);
// N
			setC_TaxCategory_ID (0);
			setC_Tax_ID (0);
			setSC_PorcentajeIVA (Env.ZERO);
			setZ_ScanntechConfig_ID (0);
			setZ_ScanntechConfigTax_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_ScanntechConfigTax (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_Z_ScanntechConfigTax[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AplicaInterface.
		@param AplicaInterface 
		Si aplica o no en interface de datos
	  */
	public void setAplicaInterface (boolean AplicaInterface)
	{
		set_Value (COLUMNNAME_AplicaInterface, Boolean.valueOf(AplicaInterface));
	}

	/** Get AplicaInterface.
		@return Si aplica o no en interface de datos
	  */
	public boolean isAplicaInterface () 
	{
		Object oo = get_Value(COLUMNNAME_AplicaInterface);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_C_TaxCategory getC_TaxCategory() throws RuntimeException
    {
		return (I_C_TaxCategory)MTable.get(getCtx(), I_C_TaxCategory.Table_Name)
			.getPO(getC_TaxCategory_ID(), get_TrxName());	}

	/** Set Tax Category.
		@param C_TaxCategory_ID 
		Tax Category
	  */
	public void setC_TaxCategory_ID (int C_TaxCategory_ID)
	{
		if (C_TaxCategory_ID < 1) 
			set_Value (COLUMNNAME_C_TaxCategory_ID, null);
		else 
			set_Value (COLUMNNAME_C_TaxCategory_ID, Integer.valueOf(C_TaxCategory_ID));
	}

	/** Get Tax Category.
		@return Tax Category
	  */
	public int getC_TaxCategory_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_TaxCategory_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Tax getC_Tax() throws RuntimeException
    {
		return (I_C_Tax)MTable.get(getCtx(), I_C_Tax.Table_Name)
			.getPO(getC_Tax_ID(), get_TrxName());	}

	/** Set Tax.
		@param C_Tax_ID 
		Tax identifier
	  */
	public void setC_Tax_ID (int C_Tax_ID)
	{
		if (C_Tax_ID < 1) 
			set_Value (COLUMNNAME_C_Tax_ID, null);
		else 
			set_Value (COLUMNNAME_C_Tax_ID, Integer.valueOf(C_Tax_ID));
	}

	/** Get Tax.
		@return Tax identifier
	  */
	public int getC_Tax_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Tax_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_Product getM_Product() throws RuntimeException
    {
		return (I_M_Product)MTable.get(getCtx(), I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_CodigoIVA.
		@param SC_CodigoIVA 
		Codigo de impuesto para POS Scanntech
	  */
	public void setSC_CodigoIVA (String SC_CodigoIVA)
	{
		set_Value (COLUMNNAME_SC_CodigoIVA, SC_CodigoIVA);
	}

	/** Get SC_CodigoIVA.
		@return Codigo de impuesto para POS Scanntech
	  */
	public String getSC_CodigoIVA () 
	{
		return (String)get_Value(COLUMNNAME_SC_CodigoIVA);
	}

	/** Set SC_PorcentajeIVA.
		@param SC_PorcentajeIVA SC_PorcentajeIVA	  */
	public void setSC_PorcentajeIVA (BigDecimal SC_PorcentajeIVA)
	{
		set_Value (COLUMNNAME_SC_PorcentajeIVA, SC_PorcentajeIVA);
	}

	/** Get SC_PorcentajeIVA.
		@return SC_PorcentajeIVA	  */
	public BigDecimal getSC_PorcentajeIVA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_PorcentajeIVA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Immutable Universally Unique Identifier.
		@param UUID 
		Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID)
	{
		set_Value (COLUMNNAME_UUID, UUID);
	}

	/** Get Immutable Universally Unique Identifier.
		@return Immutable Universally Unique Identifier
	  */
	public String getUUID () 
	{
		return (String)get_Value(COLUMNNAME_UUID);
	}

	public I_Z_ScanntechConfig getZ_ScanntechConfig() throws RuntimeException
    {
		return (I_Z_ScanntechConfig)MTable.get(getCtx(), I_Z_ScanntechConfig.Table_Name)
			.getPO(getZ_ScanntechConfig_ID(), get_TrxName());	}

	/** Set Z_ScanntechConfig ID.
		@param Z_ScanntechConfig_ID Z_ScanntechConfig ID	  */
	public void setZ_ScanntechConfig_ID (int Z_ScanntechConfig_ID)
	{
		if (Z_ScanntechConfig_ID < 1) 
			set_Value (COLUMNNAME_Z_ScanntechConfig_ID, null);
		else 
			set_Value (COLUMNNAME_Z_ScanntechConfig_ID, Integer.valueOf(Z_ScanntechConfig_ID));
	}

	/** Get Z_ScanntechConfig ID.
		@return Z_ScanntechConfig ID	  */
	public int getZ_ScanntechConfig_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ScanntechConfig_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_ScanntechConfigTax ID.
		@param Z_ScanntechConfigTax_ID Z_ScanntechConfigTax ID	  */
	public void setZ_ScanntechConfigTax_ID (int Z_ScanntechConfigTax_ID)
	{
		if (Z_ScanntechConfigTax_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfigTax_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfigTax_ID, Integer.valueOf(Z_ScanntechConfigTax_ID));
	}

	/** Get Z_ScanntechConfigTax ID.
		@return Z_ScanntechConfigTax ID	  */
	public int getZ_ScanntechConfigTax_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ScanntechConfigTax_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}