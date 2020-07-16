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

/** Generated Interface for Z_StechLoadInvFile
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_StechLoadInvFile 
{

    /** TableName=Z_StechLoadInvFile */
    public static final String Table_Name = "Z_StechLoadInvFile";

    /** AD_Table_ID=1000345 */
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

    /** Column name AD_OrgTrx_ID */
    public static final String COLUMNNAME_AD_OrgTrx_ID = "AD_OrgTrx_ID";

	/** Set Trx Organization.
	  * Performing or initiating organization
	  */
	public void setAD_OrgTrx_ID (int AD_OrgTrx_ID);

	/** Get Trx Organization.
	  * Performing or initiating organization
	  */
	public int getAD_OrgTrx_ID();

    /** Column name AmtRounding */
    public static final String COLUMNNAME_AmtRounding = "AmtRounding";

	/** Set AmtRounding.
	  * Monto de redondeo
	  */
	public void setAmtRounding (BigDecimal AmtRounding);

	/** Get AmtRounding.
	  * Monto de redondeo
	  */
	public BigDecimal getAmtRounding();

    /** Column name AmtSubtotal */
    public static final String COLUMNNAME_AmtSubtotal = "AmtSubtotal";

	/** Set AmtSubtotal.
	  * Subtotales para no mostrar impuestos incluídos
	  */
	public void setAmtSubtotal (BigDecimal AmtSubtotal);

	/** Get AmtSubtotal.
	  * Subtotales para no mostrar impuestos incluídos
	  */
	public BigDecimal getAmtSubtotal();

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

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public I_C_Currency getC_Currency() throws RuntimeException;

    /** Column name C_DocTypeInvoice_ID */
    public static final String COLUMNNAME_C_DocTypeInvoice_ID = "C_DocTypeInvoice_ID";

	/** Set Document Type for Invoice.
	  * Document type used for invoices generated from this sales document
	  */
	public void setC_DocTypeInvoice_ID (int C_DocTypeInvoice_ID);

	/** Get Document Type for Invoice.
	  * Document type used for invoices generated from this sales document
	  */
	public int getC_DocTypeInvoice_ID();

	public I_C_DocType getC_DocTypeInvoice() throws RuntimeException;

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

    /** Column name C_Tax_ID */
    public static final String COLUMNNAME_C_Tax_ID = "C_Tax_ID";

	/** Set Tax.
	  * Tax identifier
	  */
	public void setC_Tax_ID (int C_Tax_ID);

	/** Get Tax.
	  * Tax identifier
	  */
	public int getC_Tax_ID();

	public I_C_Tax getC_Tax() throws RuntimeException;

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

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name DocumentNoRef */
    public static final String COLUMNNAME_DocumentNoRef = "DocumentNoRef";

	/** Set DocumentNoRef.
	  * Numero de documento referenciado
	  */
	public void setDocumentNoRef (String DocumentNoRef);

	/** Get DocumentNoRef.
	  * Numero de documento referenciado
	  */
	public String getDocumentNoRef();

    /** Column name DocumentSerie */
    public static final String COLUMNNAME_DocumentSerie = "DocumentSerie";

	/** Set DocumentSerie.
	  * Serie de un Documento
	  */
	public void setDocumentSerie (String DocumentSerie);

	/** Get DocumentSerie.
	  * Serie de un Documento
	  */
	public String getDocumentSerie();

    /** Column name ErrorMsg */
    public static final String COLUMNNAME_ErrorMsg = "ErrorMsg";

	/** Set Error Msg	  */
	public void setErrorMsg (String ErrorMsg);

	/** Get Error Msg	  */
	public String getErrorMsg();

    /** Column name FechaCadena */
    public static final String COLUMNNAME_FechaCadena = "FechaCadena";

	/** Set FechaCadena.
	  * Fecha en formato Cadena
	  */
	public void setFechaCadena (String FechaCadena);

	/** Get FechaCadena.
	  * Fecha en formato Cadena
	  */
	public String getFechaCadena();

    /** Column name FileLineText */
    public static final String COLUMNNAME_FileLineText = "FileLineText";

	/** Set FileLineText	  */
	public void setFileLineText (String FileLineText);

	/** Get FileLineText	  */
	public String getFileLineText();

    /** Column name I_IsImported */
    public static final String COLUMNNAME_I_IsImported = "I_IsImported";

	/** Set Imported.
	  * Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported);

	/** Get Imported.
	  * Has this import been processed
	  */
	public boolean isI_IsImported();

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

    /** Column name IsConfirmed */
    public static final String COLUMNNAME_IsConfirmed = "IsConfirmed";

	/** Set Confirmed.
	  * Assignment is confirmed
	  */
	public void setIsConfirmed (boolean IsConfirmed);

	/** Get Confirmed.
	  * Assignment is confirmed
	  */
	public boolean isConfirmed();

    /** Column name IsOmitted */
    public static final String COLUMNNAME_IsOmitted = "IsOmitted";

	/** Set IsOmitted.
	  * Omitida si o no
	  */
	public void setIsOmitted (boolean IsOmitted);

	/** Get IsOmitted.
	  * Omitida si o no
	  */
	public boolean isOmitted();

    /** Column name LineNetAmt */
    public static final String COLUMNNAME_LineNetAmt = "LineNetAmt";

	/** Set Line Amount.
	  * Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public void setLineNetAmt (BigDecimal LineNetAmt);

	/** Get Line Amount.
	  * Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public BigDecimal getLineNetAmt();

    /** Column name LineNumber */
    public static final String COLUMNNAME_LineNumber = "LineNumber";

	/** Set LineNumber	  */
	public void setLineNumber (int LineNumber);

	/** Get LineNumber	  */
	public int getLineNumber();

    /** Column name LineTotalAmt */
    public static final String COLUMNNAME_LineTotalAmt = "LineTotalAmt";

	/** Set Line Total.
	  * Total line amount incl. Tax
	  */
	public void setLineTotalAmt (BigDecimal LineTotalAmt);

	/** Get Line Total.
	  * Total line amount incl. Tax
	  */
	public BigDecimal getLineTotalAmt();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public I_M_Product getM_Product() throws RuntimeException;

    /** Column name NomDocumento */
    public static final String COLUMNNAME_NomDocumento = "NomDocumento";

	/** Set NomDocumento.
	  * Nombre Documento
	  */
	public void setNomDocumento (String NomDocumento);

	/** Get NomDocumento.
	  * Nombre Documento
	  */
	public String getNomDocumento();

    /** Column name NomImpuestoPOS */
    public static final String COLUMNNAME_NomImpuestoPOS = "NomImpuestoPOS";

	/** Set NomImpuestoPOS.
	  * Nombre de impuesto en POS
	  */
	public void setNomImpuestoPOS (String NomImpuestoPOS);

	/** Get NomImpuestoPOS.
	  * Nombre de impuesto en POS
	  */
	public String getNomImpuestoPOS();

    /** Column name NomLocal */
    public static final String COLUMNNAME_NomLocal = "NomLocal";

	/** Set NomLocal.
	  * Nombre de Local
	  */
	public void setNomLocal (String NomLocal);

	/** Get NomLocal.
	  * Nombre de Local
	  */
	public String getNomLocal();

    /** Column name NomMoneda */
    public static final String COLUMNNAME_NomMoneda = "NomMoneda";

	/** Set NomMoneda.
	  * Nombre de Moneda
	  */
	public void setNomMoneda (String NomMoneda);

	/** Get NomMoneda.
	  * Nombre de Moneda
	  */
	public String getNomMoneda();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Rate */
    public static final String COLUMNNAME_Rate = "Rate";

	/** Set Rate.
	  * Rate or Tax or Exchange
	  */
	public void setRate (BigDecimal Rate);

	/** Get Rate.
	  * Rate or Tax or Exchange
	  */
	public BigDecimal getRate();

    /** Column name SC_CodigoIVA */
    public static final String COLUMNNAME_SC_CodigoIVA = "SC_CodigoIVA";

	/** Set SC_CodigoIVA.
	  * Codigo de impuesto para POS Scanntech
	  */
	public void setSC_CodigoIVA (String SC_CodigoIVA);

	/** Get SC_CodigoIVA.
	  * Codigo de impuesto para POS Scanntech
	  */
	public String getSC_CodigoIVA();

    /** Column name TaxAmt */
    public static final String COLUMNNAME_TaxAmt = "TaxAmt";

	/** Set Tax Amount.
	  * Tax Amount for a document
	  */
	public void setTaxAmt (BigDecimal TaxAmt);

	/** Get Tax Amount.
	  * Tax Amount for a document
	  */
	public BigDecimal getTaxAmt();

    /** Column name TaxID */
    public static final String COLUMNNAME_TaxID = "TaxID";

	/** Set Tax ID.
	  * Tax Identification
	  */
	public void setTaxID (String TaxID);

	/** Get Tax ID.
	  * Tax Identification
	  */
	public String getTaxID();

    /** Column name TotalAmt */
    public static final String COLUMNNAME_TotalAmt = "TotalAmt";

	/** Set Total Amount.
	  * Total Amount
	  */
	public void setTotalAmt (BigDecimal TotalAmt);

	/** Get Total Amount.
	  * Total Amount
	  */
	public BigDecimal getTotalAmt();

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

    /** Column name Z_StechLoadInvFile_ID */
    public static final String COLUMNNAME_Z_StechLoadInvFile_ID = "Z_StechLoadInvFile_ID";

	/** Set Z_StechLoadInvFile ID	  */
	public void setZ_StechLoadInvFile_ID (int Z_StechLoadInvFile_ID);

	/** Get Z_StechLoadInvFile ID	  */
	public int getZ_StechLoadInvFile_ID();

    /** Column name Z_StechLoadInv_ID */
    public static final String COLUMNNAME_Z_StechLoadInv_ID = "Z_StechLoadInv_ID";

	/** Set Z_StechLoadInv ID	  */
	public void setZ_StechLoadInv_ID (int Z_StechLoadInv_ID);

	/** Get Z_StechLoadInv ID	  */
	public int getZ_StechLoadInv_ID();

	public I_Z_StechLoadInv getZ_StechLoadInv() throws RuntimeException;
}
