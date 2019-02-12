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

/** Generated Model for Z_Stech_TK_MovDetDtos
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_Stech_TK_MovDetDtos extends PO implements I_Z_Stech_TK_MovDetDtos, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190212L;

    /** Standard Constructor */
    public X_Z_Stech_TK_MovDetDtos (Properties ctx, int Z_Stech_TK_MovDetDtos_ID, String trxName)
    {
      super (ctx, Z_Stech_TK_MovDetDtos_ID, trxName);
      /** if (Z_Stech_TK_MovDetDtos_ID == 0)
        {
			setZ_StechInterfaceVta_ID (0);
			setZ_Stech_TK_MovDetDtos_ID (0);
			setZ_Stech_TK_MovDet_ID (0);
			setZ_Stech_TK_Mov_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_Stech_TK_MovDetDtos (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_Stech_TK_MovDetDtos[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set JSonBody.
		@param JSonBody 
		Body de información utilizado en aplicaciones móbiles con JSon
	  */
	public void setJSonBody (String JSonBody)
	{
		set_Value (COLUMNNAME_JSonBody, JSonBody);
	}

	/** Get JSonBody.
		@return Body de información utilizado en aplicaciones móbiles con JSon
	  */
	public String getJSonBody () 
	{
		return (String)get_Value(COLUMNNAME_JSonBody);
	}

	/** Set SC_IdDescuento.
		@param SC_IdDescuento SC_IdDescuento	  */
	public void setSC_IdDescuento (int SC_IdDescuento)
	{
		set_Value (COLUMNNAME_SC_IdDescuento, Integer.valueOf(SC_IdDescuento));
	}

	/** Get SC_IdDescuento.
		@return SC_IdDescuento	  */
	public int getSC_IdDescuento () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_IdDescuento);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_IdPromocion.
		@param SC_IdPromocion SC_IdPromocion	  */
	public void setSC_IdPromocion (String SC_IdPromocion)
	{
		set_Value (COLUMNNAME_SC_IdPromocion, SC_IdPromocion);
	}

	/** Get SC_IdPromocion.
		@return SC_IdPromocion	  */
	public String getSC_IdPromocion () 
	{
		return (String)get_Value(COLUMNNAME_SC_IdPromocion);
	}

	/** Set SC_ImporteDescuento.
		@param SC_ImporteDescuento SC_ImporteDescuento	  */
	public void setSC_ImporteDescuento (BigDecimal SC_ImporteDescuento)
	{
		set_Value (COLUMNNAME_SC_ImporteDescuento, SC_ImporteDescuento);
	}

	/** Get SC_ImporteDescuento.
		@return SC_ImporteDescuento	  */
	public BigDecimal getSC_ImporteDescuento () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_ImporteDescuento);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SC_TipoDescuento.
		@param SC_TipoDescuento SC_TipoDescuento	  */
	public void setSC_TipoDescuento (String SC_TipoDescuento)
	{
		set_Value (COLUMNNAME_SC_TipoDescuento, SC_TipoDescuento);
	}

	/** Get SC_TipoDescuento.
		@return SC_TipoDescuento	  */
	public String getSC_TipoDescuento () 
	{
		return (String)get_Value(COLUMNNAME_SC_TipoDescuento);
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

	public I_Z_StechInterfaceVta getZ_StechInterfaceVta() throws RuntimeException
    {
		return (I_Z_StechInterfaceVta)MTable.get(getCtx(), I_Z_StechInterfaceVta.Table_Name)
			.getPO(getZ_StechInterfaceVta_ID(), get_TrxName());	}

	/** Set Z_StechInterfaceVta ID.
		@param Z_StechInterfaceVta_ID Z_StechInterfaceVta ID	  */
	public void setZ_StechInterfaceVta_ID (int Z_StechInterfaceVta_ID)
	{
		if (Z_StechInterfaceVta_ID < 1) 
			set_Value (COLUMNNAME_Z_StechInterfaceVta_ID, null);
		else 
			set_Value (COLUMNNAME_Z_StechInterfaceVta_ID, Integer.valueOf(Z_StechInterfaceVta_ID));
	}

	/** Get Z_StechInterfaceVta ID.
		@return Z_StechInterfaceVta ID	  */
	public int getZ_StechInterfaceVta_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_StechInterfaceVta_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_Stech_TK_MovDetDtos ID.
		@param Z_Stech_TK_MovDetDtos_ID Z_Stech_TK_MovDetDtos ID	  */
	public void setZ_Stech_TK_MovDetDtos_ID (int Z_Stech_TK_MovDetDtos_ID)
	{
		if (Z_Stech_TK_MovDetDtos_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_Stech_TK_MovDetDtos_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_Stech_TK_MovDetDtos_ID, Integer.valueOf(Z_Stech_TK_MovDetDtos_ID));
	}

	/** Get Z_Stech_TK_MovDetDtos ID.
		@return Z_Stech_TK_MovDetDtos ID	  */
	public int getZ_Stech_TK_MovDetDtos_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_Stech_TK_MovDetDtos_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_Stech_TK_MovDet getZ_Stech_TK_MovDet() throws RuntimeException
    {
		return (I_Z_Stech_TK_MovDet)MTable.get(getCtx(), I_Z_Stech_TK_MovDet.Table_Name)
			.getPO(getZ_Stech_TK_MovDet_ID(), get_TrxName());	}

	/** Set Z_Stech_TK_MovDet ID.
		@param Z_Stech_TK_MovDet_ID Z_Stech_TK_MovDet ID	  */
	public void setZ_Stech_TK_MovDet_ID (int Z_Stech_TK_MovDet_ID)
	{
		if (Z_Stech_TK_MovDet_ID < 1) 
			set_Value (COLUMNNAME_Z_Stech_TK_MovDet_ID, null);
		else 
			set_Value (COLUMNNAME_Z_Stech_TK_MovDet_ID, Integer.valueOf(Z_Stech_TK_MovDet_ID));
	}

	/** Get Z_Stech_TK_MovDet ID.
		@return Z_Stech_TK_MovDet ID	  */
	public int getZ_Stech_TK_MovDet_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_Stech_TK_MovDet_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Z_Stech_TK_Mov getZ_Stech_TK_Mov() throws RuntimeException
    {
		return (I_Z_Stech_TK_Mov)MTable.get(getCtx(), I_Z_Stech_TK_Mov.Table_Name)
			.getPO(getZ_Stech_TK_Mov_ID(), get_TrxName());	}

	/** Set Z_Stech_TK_Mov ID.
		@param Z_Stech_TK_Mov_ID Z_Stech_TK_Mov ID	  */
	public void setZ_Stech_TK_Mov_ID (int Z_Stech_TK_Mov_ID)
	{
		if (Z_Stech_TK_Mov_ID < 1) 
			set_Value (COLUMNNAME_Z_Stech_TK_Mov_ID, null);
		else 
			set_Value (COLUMNNAME_Z_Stech_TK_Mov_ID, Integer.valueOf(Z_Stech_TK_Mov_ID));
	}

	/** Get Z_Stech_TK_Mov ID.
		@return Z_Stech_TK_Mov ID	  */
	public int getZ_Stech_TK_Mov_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_Stech_TK_Mov_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}