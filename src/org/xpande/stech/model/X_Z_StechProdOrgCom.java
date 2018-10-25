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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for Z_StechProdOrgCom
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_StechProdOrgCom extends PO implements I_Z_StechProdOrgCom, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181025L;

    /** Standard Constructor */
    public X_Z_StechProdOrgCom (Properties ctx, int Z_StechProdOrgCom_ID, String trxName)
    {
      super (ctx, Z_StechProdOrgCom_ID, trxName);
      /** if (Z_StechProdOrgCom_ID == 0)
        {
			setAD_OrgTrx_ID (0);
			setCRUDType (null);
			setM_Product_ID (0);
			setZ_StechProdOrgCom_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_StechProdOrgCom (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_StechProdOrgCom[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Trx Organization.
		@param AD_OrgTrx_ID 
		Performing or initiating organization
	  */
	public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
	{
		if (AD_OrgTrx_ID < 1) 
			set_Value (COLUMNNAME_AD_OrgTrx_ID, null);
		else 
			set_Value (COLUMNNAME_AD_OrgTrx_ID, Integer.valueOf(AD_OrgTrx_ID));
	}

	/** Get Trx Organization.
		@return Performing or initiating organization
	  */
	public int getAD_OrgTrx_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_OrgTrx_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** CRUDType AD_Reference_ID=1000007 */
	public static final int CRUDTYPE_AD_Reference_ID=1000007;
	/** CREATE = C */
	public static final String CRUDTYPE_CREATE = "C";
	/** UPDATE = U */
	public static final String CRUDTYPE_UPDATE = "U";
	/** DELETE = D */
	public static final String CRUDTYPE_DELETE = "D";
	/** READ = R */
	public static final String CRUDTYPE_READ = "R";
	/** Set CRUDType.
		@param CRUDType 
		Tipo de acción de alta, baja, modificacion o lectura de registro en Base de Datos
	  */
	public void setCRUDType (String CRUDType)
	{

		set_Value (COLUMNNAME_CRUDType, CRUDType);
	}

	/** Get CRUDType.
		@return Tipo de acción de alta, baja, modificacion o lectura de registro en Base de Datos
	  */
	public String getCRUDType () 
	{
		return (String)get_Value(COLUMNNAME_CRUDType);
	}

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	/** Set File Name.
		@param FileName 
		Name of the local file or URL
	  */
	public void setFileName (String FileName)
	{
		set_Value (COLUMNNAME_FileName, FileName);
	}

	/** Get File Name.
		@return Name of the local file or URL
	  */
	public String getFileName () 
	{
		return (String)get_Value(COLUMNNAME_FileName);
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

	/** Set PriceSO.
		@param PriceSO 
		PriceSO
	  */
	public void setPriceSO (BigDecimal PriceSO)
	{
		set_Value (COLUMNNAME_PriceSO, PriceSO);
	}

	/** Get PriceSO.
		@return PriceSO
	  */
	public BigDecimal getPriceSO () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PriceSO);
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

	/** Set Z_ComunicacionPOS ID.
		@param Z_ComunicacionPOS_ID Z_ComunicacionPOS ID	  */
	public void setZ_ComunicacionPOS_ID (int Z_ComunicacionPOS_ID)
	{
		if (Z_ComunicacionPOS_ID < 1) 
			set_Value (COLUMNNAME_Z_ComunicacionPOS_ID, null);
		else 
			set_Value (COLUMNNAME_Z_ComunicacionPOS_ID, Integer.valueOf(Z_ComunicacionPOS_ID));
	}

	/** Get Z_ComunicacionPOS ID.
		@return Z_ComunicacionPOS ID	  */
	public int getZ_ComunicacionPOS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ComunicacionPOS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_StechInterfaceOut getZ_StechInterfaceOut() throws RuntimeException
    {
		return (I_Z_StechInterfaceOut)MTable.get(getCtx(), I_Z_StechInterfaceOut.Table_Name)
			.getPO(getZ_StechInterfaceOut_ID(), get_TrxName());	}

	/** Set Z_StechInterfaceOut ID.
		@param Z_StechInterfaceOut_ID Z_StechInterfaceOut ID	  */
	public void setZ_StechInterfaceOut_ID (int Z_StechInterfaceOut_ID)
	{
		if (Z_StechInterfaceOut_ID < 1) 
			set_Value (COLUMNNAME_Z_StechInterfaceOut_ID, null);
		else 
			set_Value (COLUMNNAME_Z_StechInterfaceOut_ID, Integer.valueOf(Z_StechInterfaceOut_ID));
	}

	/** Get Z_StechInterfaceOut ID.
		@return Z_StechInterfaceOut ID	  */
	public int getZ_StechInterfaceOut_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_StechInterfaceOut_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_StechProdOrgCom ID.
		@param Z_StechProdOrgCom_ID Z_StechProdOrgCom ID	  */
	public void setZ_StechProdOrgCom_ID (int Z_StechProdOrgCom_ID)
	{
		if (Z_StechProdOrgCom_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_StechProdOrgCom_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_StechProdOrgCom_ID, Integer.valueOf(Z_StechProdOrgCom_ID));
	}

	/** Get Z_StechProdOrgCom ID.
		@return Z_StechProdOrgCom ID	  */
	public int getZ_StechProdOrgCom_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_StechProdOrgCom_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}