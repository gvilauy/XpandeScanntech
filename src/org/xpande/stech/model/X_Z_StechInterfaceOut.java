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

/** Generated Model for Z_StechInterfaceOut
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_Z_StechInterfaceOut extends PO implements I_Z_StechInterfaceOut, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170706L;

    /** Standard Constructor */
    public X_Z_StechInterfaceOut (Properties ctx, int Z_StechInterfaceOut_ID, String trxName)
    {
      super (ctx, Z_StechInterfaceOut_ID, trxName);
      /** if (Z_StechInterfaceOut_ID == 0)
        {
			setCRUDType (null);
			setIsError (false);
// N
			setIsExecuted (false);
// N
			setIsPriceChanged (false);
// N
			setRecord_ID (0);
			setSeqNo (0);
			setZ_StechInterfaceOut_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Z_StechInterfaceOut (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Z_StechInterfaceOut[")
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

	/** Set Error.
		@param IsError 
		An Error occurred in the execution
	  */
	public void setIsError (boolean IsError)
	{
		set_Value (COLUMNNAME_IsError, Boolean.valueOf(IsError));
	}

	/** Get Error.
		@return An Error occurred in the execution
	  */
	public boolean isError () 
	{
		Object oo = get_Value(COLUMNNAME_IsError);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Record ID.
		@param Record_ID 
		Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID)
	{
		if (Record_ID < 0) 
			set_Value (COLUMNNAME_Record_ID, null);
		else 
			set_Value (COLUMNNAME_Record_ID, Integer.valueOf(Record_ID));
	}

	/** Get Record ID.
		@return Direct internal record ID
	  */
	public int getRecord_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Record_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Z_StechInterfaceOut ID.
		@param Z_StechInterfaceOut_ID Z_StechInterfaceOut ID	  */
	public void setZ_StechInterfaceOut_ID (int Z_StechInterfaceOut_ID)
	{
		if (Z_StechInterfaceOut_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Z_StechInterfaceOut_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Z_StechInterfaceOut_ID, Integer.valueOf(Z_StechInterfaceOut_ID));
	}

	/** Get Z_StechInterfaceOut ID.
		@return Z_StechInterfaceOut ID	  */
	public int getZ_StechInterfaceOut_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Z_StechInterfaceOut_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}