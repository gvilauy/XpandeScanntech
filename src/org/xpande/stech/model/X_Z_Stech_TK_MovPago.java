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

/** Generated Model for Z_Stech_TK_MovPago
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_Stech_TK_MovPago extends PO implements I_Z_Stech_TK_MovPago, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190212L;

    /** Standard Constructor */
    public X_Z_Stech_TK_MovPago (Properties ctx, int Z_Stech_TK_MovPago_ID, String trxName)
    {
      super (ctx, Z_Stech_TK_MovPago_ID, trxName);
      /** if (Z_Stech_TK_MovPago_ID == 0)
        {
			setSC_Cambio (false);
// N
			setZ_StechInterfaceVta_ID (0);
			setZ_Stech_TK_Mov_ID (0);
			setZ_Stech_TK_MovPago_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_Stech_TK_MovPago (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_Stech_TK_MovPago[")
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

	/** Set SC_Cambio.
		@param SC_Cambio SC_Cambio	  */
	public void setSC_Cambio (boolean SC_Cambio)
	{
		set_Value (COLUMNNAME_SC_Cambio, Boolean.valueOf(SC_Cambio));
	}

	/** Get SC_Cambio.
		@return SC_Cambio	  */
	public boolean isSC_Cambio () 
	{
		Object oo = get_Value(COLUMNNAME_SC_Cambio);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set SC_CodigoCliente.
		@param SC_CodigoCliente SC_CodigoCliente	  */
	public void setSC_CodigoCliente (String SC_CodigoCliente)
	{
		set_Value (COLUMNNAME_SC_CodigoCliente, SC_CodigoCliente);
	}

	/** Get SC_CodigoCliente.
		@return SC_CodigoCliente	  */
	public String getSC_CodigoCliente () 
	{
		return (String)get_Value(COLUMNNAME_SC_CodigoCliente);
	}

	/** Set SC_CodigoCredito.
		@param SC_CodigoCredito SC_CodigoCredito	  */
	public void setSC_CodigoCredito (int SC_CodigoCredito)
	{
		set_Value (COLUMNNAME_SC_CodigoCredito, Integer.valueOf(SC_CodigoCredito));
	}

	/** Get SC_CodigoCredito.
		@return SC_CodigoCredito	  */
	public int getSC_CodigoCredito () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_CodigoCredito);
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

	/** Set SC_CodigoPlanPagos.
		@param SC_CodigoPlanPagos SC_CodigoPlanPagos	  */
	public void setSC_CodigoPlanPagos (int SC_CodigoPlanPagos)
	{
		set_Value (COLUMNNAME_SC_CodigoPlanPagos, Integer.valueOf(SC_CodigoPlanPagos));
	}

	/** Get SC_CodigoPlanPagos.
		@return SC_CodigoPlanPagos	  */
	public int getSC_CodigoPlanPagos () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_CodigoPlanPagos);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_CodigoTipoPago.
		@param SC_CodigoTipoPago SC_CodigoTipoPago	  */
	public void setSC_CodigoTipoPago (int SC_CodigoTipoPago)
	{
		set_Value (COLUMNNAME_SC_CodigoTipoPago, Integer.valueOf(SC_CodigoTipoPago));
	}

	/** Get SC_CodigoTipoPago.
		@return SC_CodigoTipoPago	  */
	public int getSC_CodigoTipoPago () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_CodigoTipoPago);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_ComercioCredito.
		@param SC_ComercioCredito SC_ComercioCredito	  */
	public void setSC_ComercioCredito (String SC_ComercioCredito)
	{
		set_Value (COLUMNNAME_SC_ComercioCredito, SC_ComercioCredito);
	}

	/** Get SC_ComercioCredito.
		@return SC_ComercioCredito	  */
	public String getSC_ComercioCredito () 
	{
		return (String)get_Value(COLUMNNAME_SC_ComercioCredito);
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

	/** Set SC_DocumentoCliente.
		@param SC_DocumentoCliente SC_DocumentoCliente	  */
	public void setSC_DocumentoCliente (String SC_DocumentoCliente)
	{
		set_Value (COLUMNNAME_SC_DocumentoCliente, SC_DocumentoCliente);
	}

	/** Get SC_DocumentoCliente.
		@return SC_DocumentoCliente	  */
	public String getSC_DocumentoCliente () 
	{
		return (String)get_Value(COLUMNNAME_SC_DocumentoCliente);
	}

	/** Set SC_FechaVencimiento.
		@param SC_FechaVencimiento SC_FechaVencimiento	  */
	public void setSC_FechaVencimiento (Timestamp SC_FechaVencimiento)
	{
		set_Value (COLUMNNAME_SC_FechaVencimiento, SC_FechaVencimiento);
	}

	/** Get SC_FechaVencimiento.
		@return SC_FechaVencimiento	  */
	public Timestamp getSC_FechaVencimiento () 
	{
		return (Timestamp)get_Value(COLUMNNAME_SC_FechaVencimiento);
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

	/** Set SC_ImportePago.
		@param SC_ImportePago SC_ImportePago	  */
	public void setSC_ImportePago (BigDecimal SC_ImportePago)
	{
		set_Value (COLUMNNAME_SC_ImportePago, SC_ImportePago);
	}

	/** Get SC_ImportePago.
		@return SC_ImportePago	  */
	public BigDecimal getSC_ImportePago () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SC_ImportePago);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SC_NumeroAutorizacion.
		@param SC_NumeroAutorizacion SC_NumeroAutorizacion	  */
	public void setSC_NumeroAutorizacion (String SC_NumeroAutorizacion)
	{
		set_Value (COLUMNNAME_SC_NumeroAutorizacion, SC_NumeroAutorizacion);
	}

	/** Get SC_NumeroAutorizacion.
		@return SC_NumeroAutorizacion	  */
	public String getSC_NumeroAutorizacion () 
	{
		return (String)get_Value(COLUMNNAME_SC_NumeroAutorizacion);
	}

	/** Set SC_NumeroCuotasPago.
		@param SC_NumeroCuotasPago SC_NumeroCuotasPago	  */
	public void setSC_NumeroCuotasPago (int SC_NumeroCuotasPago)
	{
		set_Value (COLUMNNAME_SC_NumeroCuotasPago, Integer.valueOf(SC_NumeroCuotasPago));
	}

	/** Get SC_NumeroCuotasPago.
		@return SC_NumeroCuotasPago	  */
	public int getSC_NumeroCuotasPago () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SC_NumeroCuotasPago);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SC_NumeroDocumentoPago.
		@param SC_NumeroDocumentoPago SC_NumeroDocumentoPago	  */
	public void setSC_NumeroDocumentoPago (String SC_NumeroDocumentoPago)
	{
		set_Value (COLUMNNAME_SC_NumeroDocumentoPago, SC_NumeroDocumentoPago);
	}

	/** Get SC_NumeroDocumentoPago.
		@return SC_NumeroDocumentoPago	  */
	public String getSC_NumeroDocumentoPago () 
	{
		return (String)get_Value(COLUMNNAME_SC_NumeroDocumentoPago);
	}

	/** Set SC_NumeroTarjeta.
		@param SC_NumeroTarjeta SC_NumeroTarjeta	  */
	public void setSC_NumeroTarjeta (String SC_NumeroTarjeta)
	{
		set_Value (COLUMNNAME_SC_NumeroTarjeta, SC_NumeroTarjeta);
	}

	/** Get SC_NumeroTarjeta.
		@return SC_NumeroTarjeta	  */
	public String getSC_NumeroTarjeta () 
	{
		return (String)get_Value(COLUMNNAME_SC_NumeroTarjeta);
	}

	/** Set SC_TerminalCredito.
		@param SC_TerminalCredito SC_TerminalCredito	  */
	public void setSC_TerminalCredito (String SC_TerminalCredito)
	{
		set_Value (COLUMNNAME_SC_TerminalCredito, SC_TerminalCredito);
	}

	/** Get SC_TerminalCredito.
		@return SC_TerminalCredito	  */
	public String getSC_TerminalCredito () 
	{
		return (String)get_Value(COLUMNNAME_SC_TerminalCredito);
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

	/** Set Z_Stech_TK_MovPago ID.
		@param Z_Stech_TK_MovPago_ID Z_Stech_TK_MovPago ID	  */
	public void setZ_Stech_TK_MovPago_ID (int Z_Stech_TK_MovPago_ID)
	{
		if (Z_Stech_TK_MovPago_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_Stech_TK_MovPago_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_Stech_TK_MovPago_ID, Integer.valueOf(Z_Stech_TK_MovPago_ID));
	}

	/** Get Z_Stech_TK_MovPago ID.
		@return Z_Stech_TK_MovPago ID	  */
	public int getZ_Stech_TK_MovPago_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_Stech_TK_MovPago_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}