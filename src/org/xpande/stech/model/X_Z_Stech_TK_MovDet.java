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

/** Generated Model for Z_Stech_TK_MovDet
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_Stech_TK_MovDet extends PO implements I_Z_Stech_TK_MovDet, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190212L;

    /** Standard Constructor */
    public X_Z_Stech_TK_MovDet (Properties ctx, int Z_Stech_TK_MovDet_ID, String trxName)
    {
      super (ctx, Z_Stech_TK_MovDet_ID, trxName);
      /** if (Z_Stech_TK_MovDet_ID == 0)
        {
			setZ_StechInterfaceVta_ID (0);
			setZ_Stech_TK_MovDet_ID (0);
			setZ_Stech_TK_Mov_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_Stech_TK_MovDet (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_Stech_TK_MovDet[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_Value (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
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

	/** Set SC_Cantidad.
		@param SC_Cantidad SC_Cantidad	  */
	public void setSC_Cantidad (BigDecimal SC_Cantidad)
	{
		set_Value (COLUMNNAME_SC_Cantidad, SC_Cantidad);
	}

	/** Get SC_Cantidad.
		@return SC_Cantidad	  */
	public BigDecimal getSC_Cantidad () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_Cantidad);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SC_CodigoArticulo.
		@param SC_CodigoArticulo SC_CodigoArticulo	  */
	public void setSC_CodigoArticulo (String SC_CodigoArticulo)
	{
		set_Value (COLUMNNAME_SC_CodigoArticulo, SC_CodigoArticulo);
	}

	/** Get SC_CodigoArticulo.
		@return SC_CodigoArticulo	  */
	public String getSC_CodigoArticulo () 
	{
		return (String)get_Value(COLUMNNAME_SC_CodigoArticulo);
	}

	/** Set SC_CodigoArticuloPadre.
		@param SC_CodigoArticuloPadre SC_CodigoArticuloPadre	  */
	public void setSC_CodigoArticuloPadre (String SC_CodigoArticuloPadre)
	{
		set_Value (COLUMNNAME_SC_CodigoArticuloPadre, SC_CodigoArticuloPadre);
	}

	/** Get SC_CodigoArticuloPadre.
		@return SC_CodigoArticuloPadre	  */
	public String getSC_CodigoArticuloPadre () 
	{
		return (String)get_Value(COLUMNNAME_SC_CodigoArticuloPadre);
	}

	/** Set SC_CodigoBarras.
		@param SC_CodigoBarras SC_CodigoBarras	  */
	public void setSC_CodigoBarras (String SC_CodigoBarras)
	{
		set_Value (COLUMNNAME_SC_CodigoBarras, SC_CodigoBarras);
	}

	/** Get SC_CodigoBarras.
		@return SC_CodigoBarras	  */
	public String getSC_CodigoBarras () 
	{
		return (String)get_Value(COLUMNNAME_SC_CodigoBarras);
	}

	/** Set SC_CodigoCategoria.
		@param SC_CodigoCategoria SC_CodigoCategoria	  */
	public void setSC_CodigoCategoria (int SC_CodigoCategoria)
	{
		set_Value (COLUMNNAME_SC_CodigoCategoria, Integer.valueOf(SC_CodigoCategoria));
	}

	/** Get SC_CodigoCategoria.
		@return SC_CodigoCategoria	  */
	public int getSC_CodigoCategoria () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_CodigoCategoria);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_CodigoServicio.
		@param SC_CodigoServicio SC_CodigoServicio	  */
	public void setSC_CodigoServicio (int SC_CodigoServicio)
	{
		set_Value (COLUMNNAME_SC_CodigoServicio, Integer.valueOf(SC_CodigoServicio));
	}

	/** Get SC_CodigoServicio.
		@return SC_CodigoServicio	  */
	public int getSC_CodigoServicio () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_CodigoServicio);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_CodigoTipoDetalle.
		@param SC_CodigoTipoDetalle SC_CodigoTipoDetalle	  */
	public void setSC_CodigoTipoDetalle (int SC_CodigoTipoDetalle)
	{
		set_Value (COLUMNNAME_SC_CodigoTipoDetalle, Integer.valueOf(SC_CodigoTipoDetalle));
	}

	/** Get SC_CodigoTipoDetalle.
		@return SC_CodigoTipoDetalle	  */
	public int getSC_CodigoTipoDetalle () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_CodigoTipoDetalle);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_DescripcionArticulo.
		@param SC_DescripcionArticulo SC_DescripcionArticulo	  */
	public void setSC_DescripcionArticulo (String SC_DescripcionArticulo)
	{
		set_Value (COLUMNNAME_SC_DescripcionArticulo, SC_DescripcionArticulo);
	}

	/** Get SC_DescripcionArticulo.
		@return SC_DescripcionArticulo	  */
	public String getSC_DescripcionArticulo () 
	{
		return (String)get_Value(COLUMNNAME_SC_DescripcionArticulo);
	}

	/** Set SC_Descuento.
		@param SC_Descuento SC_Descuento	  */
	public void setSC_Descuento (BigDecimal SC_Descuento)
	{
		set_Value (COLUMNNAME_SC_Descuento, SC_Descuento);
	}

	/** Get SC_Descuento.
		@return SC_Descuento	  */
	public BigDecimal getSC_Descuento () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_Descuento);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SC_FechaServicio.
		@param SC_FechaServicio SC_FechaServicio	  */
	public void setSC_FechaServicio (Timestamp SC_FechaServicio)
	{
		set_Value (COLUMNNAME_SC_FechaServicio, SC_FechaServicio);
	}

	/** Get SC_FechaServicio.
		@return SC_FechaServicio	  */
	public Timestamp getSC_FechaServicio () 
	{
		return (Timestamp)get_Value(COLUMNNAME_SC_FechaServicio);
	}

	/** Set SC_Importe.
		@param SC_Importe SC_Importe	  */
	public void setSC_Importe (BigDecimal SC_Importe)
	{
		set_Value (COLUMNNAME_SC_Importe, SC_Importe);
	}

	/** Get SC_Importe.
		@return SC_Importe	  */
	public BigDecimal getSC_Importe () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_Importe);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SC_ImporteUnitario.
		@param SC_ImporteUnitario SC_ImporteUnitario	  */
	public void setSC_ImporteUnitario (BigDecimal SC_ImporteUnitario)
	{
		set_Value (COLUMNNAME_SC_ImporteUnitario, SC_ImporteUnitario);
	}

	/** Get SC_ImporteUnitario.
		@return SC_ImporteUnitario	  */
	public BigDecimal getSC_ImporteUnitario () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_ImporteUnitario);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SC_MedidaVenta.
		@param SC_MedidaVenta SC_MedidaVenta	  */
	public void setSC_MedidaVenta (String SC_MedidaVenta)
	{
		set_Value (COLUMNNAME_SC_MedidaVenta, SC_MedidaVenta);
	}

	/** Get SC_MedidaVenta.
		@return SC_MedidaVenta	  */
	public String getSC_MedidaVenta () 
	{
		return (String)get_Value(COLUMNNAME_SC_MedidaVenta);
	}

	/** Set SC_MontoIVA.
		@param SC_MontoIVA SC_MontoIVA	  */
	public void setSC_MontoIVA (BigDecimal SC_MontoIVA)
	{
		set_Value (COLUMNNAME_SC_MontoIVA, SC_MontoIVA);
	}

	/** Get SC_MontoIVA.
		@return SC_MontoIVA	  */
	public BigDecimal getSC_MontoIVA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_MontoIVA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SC_NumeroServicio.
		@param SC_NumeroServicio SC_NumeroServicio	  */
	public void setSC_NumeroServicio (String SC_NumeroServicio)
	{
		set_Value (COLUMNNAME_SC_NumeroServicio, SC_NumeroServicio);
	}

	/** Get SC_NumeroServicio.
		@return SC_NumeroServicio	  */
	public String getSC_NumeroServicio () 
	{
		return (String)get_Value(COLUMNNAME_SC_NumeroServicio);
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

	/** Set Z_ProductoUPC ID.
		@param Z_ProductoUPC_ID Z_ProductoUPC ID	  */
	public void setZ_ProductoUPC_ID (int Z_ProductoUPC_ID)
	{
		if (Z_ProductoUPC_ID < 1) 
			set_Value (COLUMNNAME_Z_ProductoUPC_ID, null);
		else 
			set_Value (COLUMNNAME_Z_ProductoUPC_ID, Integer.valueOf(Z_ProductoUPC_ID));
	}

	/** Get Z_ProductoUPC ID.
		@return Z_ProductoUPC ID	  */
	public int getZ_ProductoUPC_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_ProductoUPC_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Z_Stech_TK_MovDet ID.
		@param Z_Stech_TK_MovDet_ID Z_Stech_TK_MovDet ID	  */
	public void setZ_Stech_TK_MovDet_ID (int Z_Stech_TK_MovDet_ID)
	{
		if (Z_Stech_TK_MovDet_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_Stech_TK_MovDet_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_Stech_TK_MovDet_ID, Integer.valueOf(Z_Stech_TK_MovDet_ID));
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