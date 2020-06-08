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

/** Generated Model for Z_StechVtaCtaCte
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_StechVtaCtaCte extends PO implements I_Z_StechVtaCtaCte, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200608L;

    /** Standard Constructor */
    public X_Z_StechVtaCtaCte (Properties ctx, int Z_StechVtaCtaCte_ID, String trxName)
    {
      super (ctx, Z_StechVtaCtaCte_ID, trxName);
      /** if (Z_StechVtaCtaCte_ID == 0)
        {
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
			setIsExecuted (false);
// N
			setZ_StechInterfaceVta_ID (0);
			setZ_Stech_TK_Mov_ID (0);
			setZ_StechVtaCtaCte_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_StechVtaCtaCte (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_StechVtaCtaCte[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException
    {
		return (I_C_BPartner_Location)MTable.get(getCtx(), I_C_BPartner_Location.Table_Name)
			.getPO(getC_BPartner_Location_ID(), get_TrxName());	}

	/** Set Partner Location.
		@param C_BPartner_Location_ID 
		Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
	{
		if (C_BPartner_Location_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, Integer.valueOf(C_BPartner_Location_ID));
	}

	/** Get Partner Location.
		@return Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Currency getC_Currency() throws RuntimeException
    {
		return (I_C_Currency)MTable.get(getCtx(), I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
		throw new IllegalArgumentException ("C_DocType_ID is virtual column");	}

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

	public I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (I_C_Invoice)MTable.get(getCtx(), I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CodigoCajeroPOS.
		@param CodigoCajeroPOS 
		Codigo de cajero en POS
	  */
	public void setCodigoCajeroPOS (String CodigoCajeroPOS)
	{
		set_Value (COLUMNNAME_CodigoCajeroPOS, CodigoCajeroPOS);
	}

	/** Get CodigoCajeroPOS.
		@return Codigo de cajero en POS
	  */
	public String getCodigoCajeroPOS () 
	{
		return (String)get_Value(COLUMNNAME_CodigoCajeroPOS);
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

	/** Set Error Msg.
		@param ErrorMsg Error Msg	  */
	public void setErrorMsg (String ErrorMsg)
	{
		set_Value (COLUMNNAME_ErrorMsg, ErrorMsg);
	}

	/** Get Error Msg.
		@return Error Msg	  */
	public String getErrorMsg () 
	{
		return (String)get_Value(COLUMNNAME_ErrorMsg);
	}

	/** Set IsExecuted.
		@param IsExecuted IsExecuted	  */
	public void setIsExecuted (boolean IsExecuted)
	{
		set_Value (COLUMNNAME_IsExecuted, Boolean.valueOf(IsExecuted));
	}

	/** Get IsExecuted.
		@return IsExecuted	  */
	public boolean isExecuted () 
	{
		Object oo = get_Value(COLUMNNAME_IsExecuted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set SC_RucFactura.
		@param SC_RucFactura 
		RUC de Factura para Scanntech
	  */
	public void setSC_RucFactura (String SC_RucFactura)
	{
		set_Value (COLUMNNAME_SC_RucFactura, SC_RucFactura);
	}

	/** Get SC_RucFactura.
		@return RUC de Factura para Scanntech
	  */
	public String getSC_RucFactura () 
	{
		return (String)get_Value(COLUMNNAME_SC_RucFactura);
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

	/** Set Z_StechVtaCtaCte ID.
		@param Z_StechVtaCtaCte_ID Z_StechVtaCtaCte ID	  */
	public void setZ_StechVtaCtaCte_ID (int Z_StechVtaCtaCte_ID)
	{
		if (Z_StechVtaCtaCte_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_StechVtaCtaCte_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_StechVtaCtaCte_ID, Integer.valueOf(Z_StechVtaCtaCte_ID));
	}

	/** Get Z_StechVtaCtaCte ID.
		@return Z_StechVtaCtaCte ID	  */
	public int getZ_StechVtaCtaCte_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_StechVtaCtaCte_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}