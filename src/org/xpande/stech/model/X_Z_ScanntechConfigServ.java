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

/** Generated Model for Z_ScanntechConfigServ
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_ScanntechConfigServ extends PO implements I_Z_ScanntechConfigServ, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170710L;

    /** Standard Constructor */
    public X_Z_ScanntechConfigServ (Properties ctx, int Z_ScanntechConfigServ_ID, String trxName)
    {
      super (ctx, Z_ScanntechConfigServ_ID, trxName);
      /** if (Z_ScanntechConfigServ_ID == 0)
        {
			setAD_Table_ID (0);
			setCRUDType (null);
			setHttpRequestPos (null);
			setIsPriceChanged (false);
// N
			setServicioApiPos (null);
			setZ_ScanntechConfig_ID (0);
			setZ_ScanntechConfigServ_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_ScanntechConfigServ (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client 
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
      StringBuffer sb = new StringBuffer ("X_Z_ScanntechConfigServ[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_Table getAD_Table() throws RuntimeException
    {
		return (I_AD_Table)MTable.get(getCtx(), I_AD_Table.Table_Name)
			.getPO(getAD_Table_ID(), get_TrxName());	}

	/** Set Table.
		@param AD_Table_ID 
		Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1) 
			set_Value (COLUMNNAME_AD_Table_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
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

	/** HttpRequestPos AD_Reference_ID=1000010 */
	public static final int HTTPREQUESTPOS_AD_Reference_ID=1000010;
	/** GET = GET */
	public static final String HTTPREQUESTPOS_GET = "GET";
	/** PUT = PUT */
	public static final String HTTPREQUESTPOS_PUT = "PUT";
	/** POST = POST */
	public static final String HTTPREQUESTPOS_POST = "POST";
	/** DELETE = DELETE */
	public static final String HTTPREQUESTPOS_DELETE = "DELETE";
	/** Set HttpRequestPos.
		@param HttpRequestPos 
		Http Request de Servicio para API de interface con POS
	  */
	public void setHttpRequestPos (String HttpRequestPos)
	{

		set_Value (COLUMNNAME_HttpRequestPos, HttpRequestPos);
	}

	/** Get HttpRequestPos.
		@return Http Request de Servicio para API de interface con POS
	  */
	public String getHttpRequestPos () 
	{
		return (String)get_Value(COLUMNNAME_HttpRequestPos);
	}

	/** Set IsPriceChanged.
		@param IsPriceChanged 
		El precio fue modificado
	  */
	public void setIsPriceChanged (boolean IsPriceChanged)
	{
		set_Value (COLUMNNAME_IsPriceChanged, Boolean.valueOf(IsPriceChanged));
	}

	/** Get IsPriceChanged.
		@return El precio fue modificado
	  */
	public boolean isPriceChanged () 
	{
		Object oo = get_Value(COLUMNNAME_IsPriceChanged);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ServicioApiPos.
		@param ServicioApiPos 
		Servicio API para Interface con POS
	  */
	public void setServicioApiPos (String ServicioApiPos)
	{
		set_Value (COLUMNNAME_ServicioApiPos, ServicioApiPos);
	}

	/** Get ServicioApiPos.
		@return Servicio API para Interface con POS
	  */
	public String getServicioApiPos () 
	{
		return (String)get_Value(COLUMNNAME_ServicioApiPos);
	}

	public org.xpande.stech.model.I_Z_ScanntechConfig getZ_ScanntechConfig() throws RuntimeException
    {
		return (org.xpande.stech.model.I_Z_ScanntechConfig)MTable.get(getCtx(), org.xpande.stech.model.I_Z_ScanntechConfig.Table_Name)
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

	/** Set Z_ScanntechConfigServ ID.
		@param Z_ScanntechConfigServ_ID Z_ScanntechConfigServ ID	  */
	public void setZ_ScanntechConfigServ_ID (int Z_ScanntechConfigServ_ID)
	{
		if (Z_ScanntechConfigServ_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfigServ_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfigServ_ID, Integer.valueOf(Z_ScanntechConfigServ_ID));
	}

	/** Get Z_ScanntechConfigServ ID.
		@return Z_ScanntechConfigServ ID	  */
	public int getZ_ScanntechConfigServ_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ScanntechConfigServ_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}