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

/** Generated Model for Z_ScanntechConfig
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_ScanntechConfig extends PO implements I_Z_ScanntechConfig, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170711L;

    /** Standard Constructor */
    public X_Z_ScanntechConfig (Properties ctx, int Z_ScanntechConfig_ID, String trxName)
    {
      super (ctx, Z_ScanntechConfig_ID, trxName);
      /** if (Z_ScanntechConfig_ID == 0)
        {
			setClavePos (null);
			setMetodoPos (null);
			setURL (null);
			setUsuarioPos (null);
			setZ_ScanntechConfig_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_ScanntechConfig (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_ScanntechConfig[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ClavePos.
		@param ClavePos 
		Clave de usuario de proveedor POS para interface de datos
	  */
	public void setClavePos (String ClavePos)
	{
		set_Value (COLUMNNAME_ClavePos, ClavePos);
	}

	/** Get ClavePos.
		@return Clave de usuario de proveedor POS para interface de datos
	  */
	public String getClavePos () 
	{
		return (String)get_Value(COLUMNNAME_ClavePos);
	}

	/** Set EmpresaPos.
		@param EmpresaPos 
		ID de empresa para interface POS
	  */
	public void setEmpresaPos (int EmpresaPos)
	{
		set_Value (COLUMNNAME_EmpresaPos, Integer.valueOf(EmpresaPos));
	}

	/** Get EmpresaPos.
		@return ID de empresa para interface POS
	  */
	public int getEmpresaPos () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_EmpresaPos);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set MetodoPos.
		@param MetodoPos 
		Método de proveedor POS para interface de datos
	  */
	public void setMetodoPos (String MetodoPos)
	{
		set_Value (COLUMNNAME_MetodoPos, MetodoPos);
	}

	/** Get MetodoPos.
		@return Método de proveedor POS para interface de datos
	  */
	public String getMetodoPos () 
	{
		return (String)get_Value(COLUMNNAME_MetodoPos);
	}

	/** Set Timeout.
		@param Timeout 
		Is Timeout (In milliseconds) for sending or receive data
	  */
	public void setTimeout (int Timeout)
	{
		set_Value (COLUMNNAME_Timeout, Integer.valueOf(Timeout));
	}

	/** Get Timeout.
		@return Is Timeout (In milliseconds) for sending or receive data
	  */
	public int getTimeout () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Timeout);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set URL.
		@param URL 
		Full URL address - e.g. http://www.adempiere.org
	  */
	public void setURL (String URL)
	{
		set_Value (COLUMNNAME_URL, URL);
	}

	/** Get URL.
		@return Full URL address - e.g. http://www.adempiere.org
	  */
	public String getURL () 
	{
		return (String)get_Value(COLUMNNAME_URL);
	}

	/** Set UsuarioPos.
		@param UsuarioPos 
		Usuario de proveedor POS para interface de datos
	  */
	public void setUsuarioPos (String UsuarioPos)
	{
		set_Value (COLUMNNAME_UsuarioPos, UsuarioPos);
	}

	/** Get UsuarioPos.
		@return Usuario de proveedor POS para interface de datos
	  */
	public String getUsuarioPos () 
	{
		return (String)get_Value(COLUMNNAME_UsuarioPos);
	}

	/** Set Z_ScanntechConfig ID.
		@param Z_ScanntechConfig_ID Z_ScanntechConfig ID	  */
	public void setZ_ScanntechConfig_ID (int Z_ScanntechConfig_ID)
	{
		if (Z_ScanntechConfig_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfig_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfig_ID, Integer.valueOf(Z_ScanntechConfig_ID));
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
}