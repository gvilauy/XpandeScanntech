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

/** Generated Model for Z_ScanntechConfigOrg
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_ScanntechConfigOrg extends PO implements I_Z_ScanntechConfigOrg, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200525L;

    /** Standard Constructor */
    public X_Z_ScanntechConfigOrg (Properties ctx, int Z_ScanntechConfigOrg_ID, String trxName)
    {
      super (ctx, Z_ScanntechConfigOrg_ID, trxName);
      /** if (Z_ScanntechConfigOrg_ID == 0)
        {
			setAD_OrgTrx_ID (0);
			setCodigoLocalPos (null);
			setZ_ScanntechConfig_ID (0);
			setZ_ScanntechConfigOrg_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_ScanntechConfigOrg (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_ScanntechConfigOrg[")
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

	/** Set CodigoEmpPos.
		@param CodigoEmpPos 
		Codigo de empresa para el proveedor de POS
	  */
	public void setCodigoEmpPos (String CodigoEmpPos)
	{
		set_Value (COLUMNNAME_CodigoEmpPos, CodigoEmpPos);
	}

	/** Get CodigoEmpPos.
		@return Codigo de empresa para el proveedor de POS
	  */
	public String getCodigoEmpPos () 
	{
		return (String)get_Value(COLUMNNAME_CodigoEmpPos);
	}

	/** Set CodigoLocalPos.
		@param CodigoLocalPos 
		Codigo de local para interface POS
	  */
	public void setCodigoLocalPos (String CodigoLocalPos)
	{
		set_Value (COLUMNNAME_CodigoLocalPos, CodigoLocalPos);
	}

	/** Get CodigoLocalPos.
		@return Codigo de local para interface POS
	  */
	public String getCodigoLocalPos () 
	{
		return (String)get_Value(COLUMNNAME_CodigoLocalPos);
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

	/** Set Z_ScanntechConfigOrg ID.
		@param Z_ScanntechConfigOrg_ID Z_ScanntechConfigOrg ID	  */
	public void setZ_ScanntechConfigOrg_ID (int Z_ScanntechConfigOrg_ID)
	{
		if (Z_ScanntechConfigOrg_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfigOrg_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfigOrg_ID, Integer.valueOf(Z_ScanntechConfigOrg_ID));
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