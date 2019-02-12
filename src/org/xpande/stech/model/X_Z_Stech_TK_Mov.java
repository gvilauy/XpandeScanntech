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

/** Generated Model for Z_Stech_TK_Mov
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_Stech_TK_Mov extends PO implements I_Z_Stech_TK_Mov, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190212L;

    /** Standard Constructor */
    public X_Z_Stech_TK_Mov (Properties ctx, int Z_Stech_TK_Mov_ID, String trxName)
    {
      super (ctx, Z_Stech_TK_Mov_ID, trxName);
      /** if (Z_Stech_TK_Mov_ID == 0)
        {
			setSC_CuponAnulada (false);
// N
			setSC_CuponCancelado (false);
// N
			setZ_StechInterfaceVta_ID (0);
			setZ_Stech_TK_Mov_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_Stech_TK_Mov (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_Stech_TK_Mov[")
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

	/** Set SC_CantidadItems.
		@param SC_CantidadItems SC_CantidadItems	  */
	public void setSC_CantidadItems (int SC_CantidadItems)
	{
		set_Value (COLUMNNAME_SC_CantidadItems, Integer.valueOf(SC_CantidadItems));
	}

	/** Get SC_CantidadItems.
		@return SC_CantidadItems	  */
	public int getSC_CantidadItems () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_CantidadItems);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_CodigoCaja.
		@param SC_CodigoCaja SC_CodigoCaja	  */
	public void setSC_CodigoCaja (int SC_CodigoCaja)
	{
		set_Value (COLUMNNAME_SC_CodigoCaja, Integer.valueOf(SC_CodigoCaja));
	}

	/** Get SC_CodigoCaja.
		@return SC_CodigoCaja	  */
	public int getSC_CodigoCaja () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_CodigoCaja);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_CodigoEmpresa.
		@param SC_CodigoEmpresa SC_CodigoEmpresa	  */
	public void setSC_CodigoEmpresa (int SC_CodigoEmpresa)
	{
		set_Value (COLUMNNAME_SC_CodigoEmpresa, Integer.valueOf(SC_CodigoEmpresa));
	}

	/** Get SC_CodigoEmpresa.
		@return SC_CodigoEmpresa	  */
	public int getSC_CodigoEmpresa () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_CodigoEmpresa);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_CodigoLocal.
		@param SC_CodigoLocal SC_CodigoLocal	  */
	public void setSC_CodigoLocal (int SC_CodigoLocal)
	{
		set_Value (COLUMNNAME_SC_CodigoLocal, Integer.valueOf(SC_CodigoLocal));
	}

	/** Get SC_CodigoLocal.
		@return SC_CodigoLocal	  */
	public int getSC_CodigoLocal () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_CodigoLocal);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_CodigoMoneda.
		@param SC_CodigoMoneda SC_CodigoMoneda	  */
	public void setSC_CodigoMoneda (String SC_CodigoMoneda)
	{
		set_Value (COLUMNNAME_SC_CodigoMoneda, SC_CodigoMoneda);
	}

	/** Get SC_CodigoMoneda.
		@return SC_CodigoMoneda	  */
	public String getSC_CodigoMoneda () 
	{
		return (String)get_Value(COLUMNNAME_SC_CodigoMoneda);
	}

	/** Set SC_CodigoSeguridadCfe.
		@param SC_CodigoSeguridadCfe SC_CodigoSeguridadCfe	  */
	public void setSC_CodigoSeguridadCfe (String SC_CodigoSeguridadCfe)
	{
		set_Value (COLUMNNAME_SC_CodigoSeguridadCfe, SC_CodigoSeguridadCfe);
	}

	/** Get SC_CodigoSeguridadCfe.
		@return SC_CodigoSeguridadCfe	  */
	public String getSC_CodigoSeguridadCfe () 
	{
		return (String)get_Value(COLUMNNAME_SC_CodigoSeguridadCfe);
	}

	/** Set SC_CotizacionCompra.
		@param SC_CotizacionCompra SC_CotizacionCompra	  */
	public void setSC_CotizacionCompra (BigDecimal SC_CotizacionCompra)
	{
		set_Value (COLUMNNAME_SC_CotizacionCompra, SC_CotizacionCompra);
	}

	/** Get SC_CotizacionCompra.
		@return SC_CotizacionCompra	  */
	public BigDecimal getSC_CotizacionCompra () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_CotizacionCompra);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SC_CotizacionVenta.
		@param SC_CotizacionVenta SC_CotizacionVenta	  */
	public void setSC_CotizacionVenta (BigDecimal SC_CotizacionVenta)
	{
		set_Value (COLUMNNAME_SC_CotizacionVenta, SC_CotizacionVenta);
	}

	/** Get SC_CotizacionVenta.
		@return SC_CotizacionVenta	  */
	public BigDecimal getSC_CotizacionVenta () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_CotizacionVenta);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SC_CuponAnulada.
		@param SC_CuponAnulada 
		 
	  */
	public void setSC_CuponAnulada (boolean SC_CuponAnulada)
	{
		set_Value (COLUMNNAME_SC_CuponAnulada, Boolean.valueOf(SC_CuponAnulada));
	}

	/** Get SC_CuponAnulada.
		@return  
	  */
	public boolean isSC_CuponAnulada () 
	{
		Object oo = get_Value(COLUMNNAME_SC_CuponAnulada);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set SC_CuponCancelado.
		@param SC_CuponCancelado SC_CuponCancelado	  */
	public void setSC_CuponCancelado (boolean SC_CuponCancelado)
	{
		set_Value (COLUMNNAME_SC_CuponCancelado, Boolean.valueOf(SC_CuponCancelado));
	}

	/** Get SC_CuponCancelado.
		@return SC_CuponCancelado	  */
	public boolean isSC_CuponCancelado () 
	{
		Object oo = get_Value(COLUMNNAME_SC_CuponCancelado);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set SC_DescuentoTotal.
		@param SC_DescuentoTotal SC_DescuentoTotal	  */
	public void setSC_DescuentoTotal (BigDecimal SC_DescuentoTotal)
	{
		set_Value (COLUMNNAME_SC_DescuentoTotal, SC_DescuentoTotal);
	}

	/** Get SC_DescuentoTotal.
		@return SC_DescuentoTotal	  */
	public BigDecimal getSC_DescuentoTotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_DescuentoTotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SC_FechaOperacion.
		@param SC_FechaOperacion SC_FechaOperacion	  */
	public void setSC_FechaOperacion (Timestamp SC_FechaOperacion)
	{
		set_Value (COLUMNNAME_SC_FechaOperacion, SC_FechaOperacion);
	}

	/** Get SC_FechaOperacion.
		@return SC_FechaOperacion	  */
	public Timestamp getSC_FechaOperacion () 
	{
		return (Timestamp)get_Value(COLUMNNAME_SC_FechaOperacion);
	}

	/** Set SC_NumeroMov.
		@param SC_NumeroMov SC_NumeroMov	  */
	public void setSC_NumeroMov (String SC_NumeroMov)
	{
		set_Value (COLUMNNAME_SC_NumeroMov, SC_NumeroMov);
	}

	/** Get SC_NumeroMov.
		@return SC_NumeroMov	  */
	public String getSC_NumeroMov () 
	{
		return (String)get_Value(COLUMNNAME_SC_NumeroMov);
	}

	/** Set SC_NumeroOperacion.
		@param SC_NumeroOperacion SC_NumeroOperacion	  */
	public void setSC_NumeroOperacion (String SC_NumeroOperacion)
	{
		set_Value (COLUMNNAME_SC_NumeroOperacion, SC_NumeroOperacion);
	}

	/** Get SC_NumeroOperacion.
		@return SC_NumeroOperacion	  */
	public String getSC_NumeroOperacion () 
	{
		return (String)get_Value(COLUMNNAME_SC_NumeroOperacion);
	}

	/** Set SC_Redondeo.
		@param SC_Redondeo SC_Redondeo	  */
	public void setSC_Redondeo (BigDecimal SC_Redondeo)
	{
		set_Value (COLUMNNAME_SC_Redondeo, SC_Redondeo);
	}

	/** Get SC_Redondeo.
		@return SC_Redondeo	  */
	public BigDecimal getSC_Redondeo () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_Redondeo);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SC_SerieCfe.
		@param SC_SerieCfe SC_SerieCfe	  */
	public void setSC_SerieCfe (String SC_SerieCfe)
	{
		set_Value (COLUMNNAME_SC_SerieCfe, SC_SerieCfe);
	}

	/** Get SC_SerieCfe.
		@return SC_SerieCfe	  */
	public String getSC_SerieCfe () 
	{
		return (String)get_Value(COLUMNNAME_SC_SerieCfe);
	}

	/** Set SC_TipoCfe.
		@param SC_TipoCfe SC_TipoCfe	  */
	public void setSC_TipoCfe (int SC_TipoCfe)
	{
		set_Value (COLUMNNAME_SC_TipoCfe, Integer.valueOf(SC_TipoCfe));
	}

	/** Get SC_TipoCfe.
		@return SC_TipoCfe	  */
	public int getSC_TipoCfe () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_TipoCfe);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** SC_TipoOperacion AD_Reference_ID=1000034 */
	public static final int SC_TIPOOPERACION_AD_Reference_ID=1000034;
	/** VENTA = VENTA */
	public static final String SC_TIPOOPERACION_VENTA = "VENTA";
	/** VENTA ANULADA = VENTA ANULADA */
	public static final String SC_TIPOOPERACION_VENTAANULADA = "VENTA ANULADA";
	/** INGRESO = INGRESO */
	public static final String SC_TIPOOPERACION_INGRESO = "INGRESO";
	/** EGRESO = EGRESO */
	public static final String SC_TIPOOPERACION_EGRESO = "EGRESO";
	/** Set SC_TipoOperacion.
		@param SC_TipoOperacion SC_TipoOperacion	  */
	public void setSC_TipoOperacion (String SC_TipoOperacion)
	{

		set_Value (COLUMNNAME_SC_TipoOperacion, SC_TipoOperacion);
	}

	/** Get SC_TipoOperacion.
		@return SC_TipoOperacion	  */
	public String getSC_TipoOperacion () 
	{
		return (String)get_Value(COLUMNNAME_SC_TipoOperacion);
	}

	/** Set SC_Total.
		@param SC_Total SC_Total	  */
	public void setSC_Total (BigDecimal SC_Total)
	{
		set_Value (COLUMNNAME_SC_Total, SC_Total);
	}

	/** Get SC_Total.
		@return SC_Total	  */
	public BigDecimal getSC_Total () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_Total);
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

	/** Set Z_Stech_TK_Mov ID.
		@param Z_Stech_TK_Mov_ID Z_Stech_TK_Mov ID	  */
	public void setZ_Stech_TK_Mov_ID (int Z_Stech_TK_Mov_ID)
	{
		if (Z_Stech_TK_Mov_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_Stech_TK_Mov_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_Stech_TK_Mov_ID, Integer.valueOf(Z_Stech_TK_Mov_ID));
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