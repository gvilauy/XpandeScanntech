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

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for Z_StechCreditos
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_StechCreditos extends PO implements I_Z_StechCreditos, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200428L;

    /** Standard Constructor */
    public X_Z_StechCreditos (Properties ctx, int Z_StechCreditos_ID, String trxName)
    {
      super (ctx, Z_StechCreditos_ID, trxName);
      /** if (Z_StechCreditos_ID == 0)
        {
			setName (null);
			setValue (null);
			setZ_ScanntechConfig_ID (0);
			setZ_StechCreditos_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_StechCreditos (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_StechCreditos[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
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

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

	/** Set Z_MedioPago ID.
		@param Z_MedioPago_ID Z_MedioPago ID	  */
	public void setZ_MedioPago_ID (int Z_MedioPago_ID)
	{
		if (Z_MedioPago_ID < 1) 
			set_Value (COLUMNNAME_Z_MedioPago_ID, null);
		else 
			set_Value (COLUMNNAME_Z_MedioPago_ID, Integer.valueOf(Z_MedioPago_ID));
	}

	/** Get Z_MedioPago ID.
		@return Z_MedioPago ID	  */
	public int getZ_MedioPago_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_MedioPago_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_MedioPagoIdent ID.
		@param Z_MedioPagoIdent_ID Z_MedioPagoIdent ID	  */
	public void setZ_MedioPagoIdent_ID (int Z_MedioPagoIdent_ID)
	{
		if (Z_MedioPagoIdent_ID < 1) 
			set_Value (COLUMNNAME_Z_MedioPagoIdent_ID, null);
		else 
			set_Value (COLUMNNAME_Z_MedioPagoIdent_ID, Integer.valueOf(Z_MedioPagoIdent_ID));
	}

	/** Get Z_MedioPagoIdent ID.
		@return Z_MedioPagoIdent ID	  */
	public int getZ_MedioPagoIdent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_MedioPagoIdent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_MPagoIdentProd ID.
		@param Z_MPagoIdentProd_ID Z_MPagoIdentProd ID	  */
	public void setZ_MPagoIdentProd_ID (int Z_MPagoIdentProd_ID)
	{
		if (Z_MPagoIdentProd_ID < 1) 
			set_Value (COLUMNNAME_Z_MPagoIdentProd_ID, null);
		else 
			set_Value (COLUMNNAME_Z_MPagoIdentProd_ID, Integer.valueOf(Z_MPagoIdentProd_ID));
	}

	/** Get Z_MPagoIdentProd ID.
		@return Z_MPagoIdentProd ID	  */
	public int getZ_MPagoIdentProd_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_MPagoIdentProd_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Z_StechCreditos ID.
		@param Z_StechCreditos_ID Z_StechCreditos ID	  */
	public void setZ_StechCreditos_ID (int Z_StechCreditos_ID)
	{
		if (Z_StechCreditos_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_StechCreditos_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_StechCreditos_ID, Integer.valueOf(Z_StechCreditos_ID));
	}

	/** Get Z_StechCreditos ID.
		@return Z_StechCreditos ID	  */
	public int getZ_StechCreditos_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_StechCreditos_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_StechMedioPago getZ_StechMedioPago() throws RuntimeException
    {
		return (I_Z_StechMedioPago)MTable.get(getCtx(), I_Z_StechMedioPago.Table_Name)
			.getPO(getZ_StechMedioPago_ID(), get_TrxName());	}

	/** Set Z_StechMedioPago ID.
		@param Z_StechMedioPago_ID Z_StechMedioPago ID	  */
	public void setZ_StechMedioPago_ID (int Z_StechMedioPago_ID)
	{
		if (Z_StechMedioPago_ID < 1) 
			set_Value (COLUMNNAME_Z_StechMedioPago_ID, null);
		else 
			set_Value (COLUMNNAME_Z_StechMedioPago_ID, Integer.valueOf(Z_StechMedioPago_ID));
	}

	/** Get Z_StechMedioPago ID.
		@return Z_StechMedioPago ID	  */
	public int getZ_StechMedioPago_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_StechMedioPago_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}