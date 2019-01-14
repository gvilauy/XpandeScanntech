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

/** Generated Model for Z_ScanntechConfigCaja
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_ScanntechConfigCaja extends PO implements I_Z_ScanntechConfigCaja, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190114L;

    /** Standard Constructor */
    public X_Z_ScanntechConfigCaja (Properties ctx, int Z_ScanntechConfigCaja_ID, String trxName)
    {
      super (ctx, Z_ScanntechConfigCaja_ID, trxName);
      /** if (Z_ScanntechConfigCaja_ID == 0)
        {
			setCodigoPOS (null);
			setConsultarPOS (true);
// Y
			setName (null);
			setZ_ScanntechConfigCaja_ID (0);
			setZ_ScanntechConfigOrg_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_ScanntechConfigCaja (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_ScanntechConfigCaja[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set CodigoPOS.
		@param CodigoPOS 
		Código de la entidad para el proveedor de POS
	  */
	public void setCodigoPOS (String CodigoPOS)
	{
		set_Value (COLUMNNAME_CodigoPOS, CodigoPOS);
	}

	/** Get CodigoPOS.
		@return Código de la entidad para el proveedor de POS
	  */
	public String getCodigoPOS () 
	{
		return (String)get_Value(COLUMNNAME_CodigoPOS);
	}

	/** Set ConsultarPOS.
		@param ConsultarPOS 
		Si se debe consultar al POS o no en modulo de Retail
	  */
	public void setConsultarPOS (boolean ConsultarPOS)
	{
		set_Value (COLUMNNAME_ConsultarPOS, Boolean.valueOf(ConsultarPOS));
	}

	/** Get ConsultarPOS.
		@return Si se debe consultar al POS o no en modulo de Retail
	  */
	public boolean isConsultarPOS () 
	{
		Object oo = get_Value(COLUMNNAME_ConsultarPOS);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Z_ScanntechConfigCaja ID.
		@param Z_ScanntechConfigCaja_ID Z_ScanntechConfigCaja ID	  */
	public void setZ_ScanntechConfigCaja_ID (int Z_ScanntechConfigCaja_ID)
	{
		if (Z_ScanntechConfigCaja_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfigCaja_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfigCaja_ID, Integer.valueOf(Z_ScanntechConfigCaja_ID));
	}

	/** Get Z_ScanntechConfigCaja ID.
		@return Z_ScanntechConfigCaja ID	  */
	public int getZ_ScanntechConfigCaja_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ScanntechConfigCaja_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_ScanntechConfigOrg getZ_ScanntechConfigOrg() throws RuntimeException
    {
		return (I_Z_ScanntechConfigOrg)MTable.get(getCtx(), I_Z_ScanntechConfigOrg.Table_Name)
			.getPO(getZ_ScanntechConfigOrg_ID(), get_TrxName());	}

	/** Set Z_ScanntechConfigOrg ID.
		@param Z_ScanntechConfigOrg_ID Z_ScanntechConfigOrg ID	  */
	public void setZ_ScanntechConfigOrg_ID (int Z_ScanntechConfigOrg_ID)
	{
		if (Z_ScanntechConfigOrg_ID < 1) 
			set_Value (COLUMNNAME_Z_ScanntechConfigOrg_ID, null);
		else 
			set_Value (COLUMNNAME_Z_ScanntechConfigOrg_ID, Integer.valueOf(Z_ScanntechConfigOrg_ID));
	}

	/** Get Z_ScanntechConfigOrg ID.
		@return Z_ScanntechConfigOrg ID	  */
	public int getZ_ScanntechConfigOrg_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ScanntechConfigOrg_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}