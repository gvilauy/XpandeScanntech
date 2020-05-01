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
package org.xpande.stech.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for Z_StechVtaCtaCte
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_StechVtaCtaCte 
{

    /** TableName=Z_StechVtaCtaCte */
    public static final String Table_Name = "Z_StechVtaCtaCte";

    /** AD_Table_ID=1000341 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_BPartner_Location_ID */
    public static final String COLUMNNAME_C_BPartner_Location_ID = "C_BPartner_Location_ID";

	/** Set Partner Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID);

	/** Get Partner Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID();

	public I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException;

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public I_C_Invoice getC_Invoice() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DateTrx */
    public static final String COLUMNNAME_DateTrx = "DateTrx";

	/** Set Transaction Date.
	  * Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

    /** Column name ErrorMsg */
    public static final String COLUMNNAME_ErrorMsg = "ErrorMsg";

	/** Set Error Msg	  */
	public void setErrorMsg (String ErrorMsg);

	/** Get Error Msg	  */
	public String getErrorMsg();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsExecuted */
    public static final String COLUMNNAME_IsExecuted = "IsExecuted";

	/** Set IsExecuted	  */
	public void setIsExecuted (boolean IsExecuted);

	/** Get IsExecuted	  */
	public boolean isExecuted();

    /** Column name SC_Importe */
    public static final String COLUMNNAME_SC_Importe = "SC_Importe";

	/** Set SC_Importe	  */
	public void setSC_Importe (BigDecimal SC_Importe);

	/** Get SC_Importe	  */
	public BigDecimal getSC_Importe();

    /** Column name SC_NumeroOperacion */
    public static final String COLUMNNAME_SC_NumeroOperacion = "SC_NumeroOperacion";

	/** Set SC_NumeroOperacion	  */
	public void setSC_NumeroOperacion (String SC_NumeroOperacion);

	/** Get SC_NumeroOperacion	  */
	public String getSC_NumeroOperacion();

    /** Column name SC_RucFactura */
    public static final String COLUMNNAME_SC_RucFactura = "SC_RucFactura";

	/** Set SC_RucFactura.
	  * RUC de Factura para Scanntech
	  */
	public void setSC_RucFactura (String SC_RucFactura);

	/** Get SC_RucFactura.
	  * RUC de Factura para Scanntech
	  */
	public String getSC_RucFactura();

    /** Column name SC_SerieCfe */
    public static final String COLUMNNAME_SC_SerieCfe = "SC_SerieCfe";

	/** Set SC_SerieCfe	  */
	public void setSC_SerieCfe (String SC_SerieCfe);

	/** Get SC_SerieCfe	  */
	public String getSC_SerieCfe();

    /** Column name SC_TipoCfe */
    public static final String COLUMNNAME_SC_TipoCfe = "SC_TipoCfe";

	/** Set SC_TipoCfe	  */
	public void setSC_TipoCfe (int SC_TipoCfe);

	/** Get SC_TipoCfe	  */
	public int getSC_TipoCfe();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name UUID */
    public static final String COLUMNNAME_UUID = "UUID";

	/** Set Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID);

	/** Get Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public String getUUID();

    /** Column name Z_StechInterfaceVta_ID */
    public static final String COLUMNNAME_Z_StechInterfaceVta_ID = "Z_StechInterfaceVta_ID";

	/** Set Z_StechInterfaceVta ID	  */
	public void setZ_StechInterfaceVta_ID (int Z_StechInterfaceVta_ID);

	/** Get Z_StechInterfaceVta ID	  */
	public int getZ_StechInterfaceVta_ID();

	public I_Z_StechInterfaceVta getZ_StechInterfaceVta() throws RuntimeException;

    /** Column name Z_Stech_TK_Mov_ID */
    public static final String COLUMNNAME_Z_Stech_TK_Mov_ID = "Z_Stech_TK_Mov_ID";

	/** Set Z_Stech_TK_Mov ID	  */
	public void setZ_Stech_TK_Mov_ID (int Z_Stech_TK_Mov_ID);

	/** Get Z_Stech_TK_Mov ID	  */
	public int getZ_Stech_TK_Mov_ID();

	public I_Z_Stech_TK_Mov getZ_Stech_TK_Mov() throws RuntimeException;

    /** Column name Z_StechVtaCtaCte_ID */
    public static final String COLUMNNAME_Z_StechVtaCtaCte_ID = "Z_StechVtaCtaCte_ID";

	/** Set Z_StechVtaCtaCte ID	  */
	public void setZ_StechVtaCtaCte_ID (int Z_StechVtaCtaCte_ID);

	/** Get Z_StechVtaCtaCte ID	  */
	public int getZ_StechVtaCtaCte_ID();
}
