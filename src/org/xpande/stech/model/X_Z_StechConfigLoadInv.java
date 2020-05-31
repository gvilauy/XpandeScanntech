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

/** Generated Model for Z_StechConfigLoadInv
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_StechConfigLoadInv extends PO implements I_Z_StechConfigLoadInv, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200531L;

    /** Standard Constructor */
    public X_Z_StechConfigLoadInv (Properties ctx, int Z_StechConfigLoadInv_ID, String trxName)
    {
      super (ctx, Z_StechConfigLoadInv_ID, trxName);
      /** if (Z_StechConfigLoadInv_ID == 0)
        {
			setC_DocType_ID (0);
			setNomDocumento (null);
			setZ_ScanntechConfig_ID (0);
			setZ_StechConfigLoadInv_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_StechConfigLoadInv (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_StechConfigLoadInv[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_DocType getC_DocType() throws RuntimeException
    {
		return (I_C_DocType)MTable.get(getCtx(), I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set NomDocumento.
		@param NomDocumento 
		Nombre Documento
	  */
	public void setNomDocumento (String NomDocumento)
	{
		set_Value (COLUMNNAME_NomDocumento, NomDocumento);
	}

	/** Get NomDocumento.
		@return Nombre Documento
	  */
	public String getNomDocumento () 
	{
		return (String)get_Value(COLUMNNAME_NomDocumento);
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

	/** Set Z_StechConfigLoadInv ID.
		@param Z_StechConfigLoadInv_ID Z_StechConfigLoadInv ID	  */
	public void setZ_StechConfigLoadInv_ID (int Z_StechConfigLoadInv_ID)
	{
		if (Z_StechConfigLoadInv_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_StechConfigLoadInv_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_StechConfigLoadInv_ID, Integer.valueOf(Z_StechConfigLoadInv_ID));
	}

	/** Get Z_StechConfigLoadInv ID.
		@return Z_StechConfigLoadInv ID	  */
	public int getZ_StechConfigLoadInv_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_StechConfigLoadInv_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}