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

/** Generated Interface for Z_ScanntechConfigTax
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_ScanntechConfigTax 
{

    /** TableName=Z_ScanntechConfigTax */
    public static final String Table_Name = "Z_ScanntechConfigTax";

    /** AD_Table_ID=1000334 */
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

    /** Column name AplicaInterface */
    public static final String COLUMNNAME_AplicaInterface = "AplicaInterface";

	/** Set AplicaInterface.
	  * Si aplica o no en interface de datos
	  */
	public void setAplicaInterface (boolean AplicaInterface);

	/** Get AplicaInterface.
	  * Si aplica o no en interface de datos
	  */
	public boolean isAplicaInterface();

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

    /** Column name C_TaxCategory_ID */
    public static final String COLUMNNAME_C_TaxCategory_ID = "C_TaxCategory_ID";

	/** Set Tax Category.
	  * Tax Category
	  */
	public void setC_TaxCategory_ID (int C_TaxCategory_ID);

	/** Get Tax Category.
	  * Tax Category
	  */
	public int getC_TaxCategory_ID();

	public I_C_TaxCategory getC_TaxCategory() throws RuntimeException;

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

    /** Column name SC_PorcentajeIVA */
    public static final String COLUMNNAME_SC_PorcentajeIVA = "SC_PorcentajeIVA";

	/** Set SC_PorcentajeIVA	  */
	public void setSC_PorcentajeIVA (BigDecimal SC_PorcentajeIVA);

	/** Get SC_PorcentajeIVA	  */
	public BigDecimal getSC_PorcentajeIVA();

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

    /** Column name Z_ScanntechConfig_ID */
    public static final String COLUMNNAME_Z_ScanntechConfig_ID = "Z_ScanntechConfig_ID";

	/** Set Z_ScanntechConfig ID	  */
	public void setZ_ScanntechConfig_ID (int Z_ScanntechConfig_ID);

	/** Get Z_ScanntechConfig ID	  */
	public int getZ_ScanntechConfig_ID();

	public I_Z_ScanntechConfig getZ_ScanntechConfig() throws RuntimeException;

    /** Column name Z_ScanntechConfigTax_ID */
    public static final String COLUMNNAME_Z_ScanntechConfigTax_ID = "Z_ScanntechConfigTax_ID";

	/** Set Z_ScanntechConfigTax ID	  */
	public void setZ_ScanntechConfigTax_ID (int Z_ScanntechConfigTax_ID);

	/** Get Z_ScanntechConfigTax ID	  */
	public int getZ_ScanntechConfigTax_ID();
}
