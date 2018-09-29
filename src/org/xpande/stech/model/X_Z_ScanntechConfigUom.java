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

/** Generated Model for Z_ScanntechConfigUom
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_ScanntechConfigUom extends PO implements I_Z_ScanntechConfigUom, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170710L;

    /** Standard Constructor */
    public X_Z_ScanntechConfigUom (Properties ctx, int Z_ScanntechConfigUom_ID, String trxName)
    {
      super (ctx, Z_ScanntechConfigUom_ID, trxName);
      /** if (Z_ScanntechConfigUom_ID == 0)
        {
			setC_UOM_ID (0);
			setUnidadMedidaPos (null);
			setZ_ScanntechConfig_ID (0);
			setZ_ScanntechConfigUom_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_ScanntechConfigUom (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_ScanntechConfigUom[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_UOM getC_UOM() throws RuntimeException
    {
		return (I_C_UOM)MTable.get(getCtx(), I_C_UOM.Table_Name)
			.getPO(getC_UOM_ID(), get_TrxName());	}

	/** Set UOM.
		@param C_UOM_ID 
		Unit of Measure
	  */
	public void setC_UOM_ID (int C_UOM_ID)
	{
		if (C_UOM_ID < 1) 
			set_Value (COLUMNNAME_C_UOM_ID, null);
		else 
			set_Value (COLUMNNAME_C_UOM_ID, Integer.valueOf(C_UOM_ID));
	}

	/** Get UOM.
		@return Unit of Measure
	  */
	public int getC_UOM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_UOM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set UnidadMedidaPos.
		@param UnidadMedidaPos 
		Unidad de Medida para interface POS 
	  */
	public void setUnidadMedidaPos (String UnidadMedidaPos)
	{
		set_Value (COLUMNNAME_UnidadMedidaPos, UnidadMedidaPos);
	}

	/** Get UnidadMedidaPos.
		@return Unidad de Medida para interface POS 
	  */
	public String getUnidadMedidaPos () 
	{
		return (String)get_Value(COLUMNNAME_UnidadMedidaPos);
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

	/** Set Z_ScanntechConfigUom ID.
		@param Z_ScanntechConfigUom_ID Z_ScanntechConfigUom ID	  */
	public void setZ_ScanntechConfigUom_ID (int Z_ScanntechConfigUom_ID)
	{
		if (Z_ScanntechConfigUom_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfigUom_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_ScanntechConfigUom_ID, Integer.valueOf(Z_ScanntechConfigUom_ID));
	}

	/** Get Z_ScanntechConfigUom ID.
		@return Z_ScanntechConfigUom ID	  */
	public int getZ_ScanntechConfigUom_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ScanntechConfigUom_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}