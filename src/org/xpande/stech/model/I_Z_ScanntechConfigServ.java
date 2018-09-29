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

/** Generated Interface for Z_ScanntechConfigServ
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_ScanntechConfigServ 
{

    /** TableName=Z_ScanntechConfigServ */
    public static final String Table_Name = "Z_ScanntechConfigServ";

    /** AD_Table_ID=1000068 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 6 - System - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(6);

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
	public void setAD_Org_ID(int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_Table_ID */
    public static final String COLUMNNAME_AD_Table_ID = "AD_Table_ID";

	/** Set Table.
	  * Database Table information
	  */
	public void setAD_Table_ID(int AD_Table_ID);

	/** Get Table.
	  * Database Table information
	  */
	public int getAD_Table_ID();

	public I_AD_Table getAD_Table() throws RuntimeException;

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

    /** Column name CRUDType */
    public static final String COLUMNNAME_CRUDType = "CRUDType";

	/** Set CRUDType.
	  * Tipo de acción de alta, baja, modificacion o lectura de registro en Base de Datos
	  */
	public void setCRUDType(String CRUDType);

	/** Get CRUDType.
	  * Tipo de acción de alta, baja, modificacion o lectura de registro en Base de Datos
	  */
	public String getCRUDType();

    /** Column name HttpRequestPos */
    public static final String COLUMNNAME_HttpRequestPos = "HttpRequestPos";

	/** Set HttpRequestPos.
	  * Http Request de Servicio para API de interface con POS
	  */
	public void setHttpRequestPos(String HttpRequestPos);

	/** Get HttpRequestPos.
	  * Http Request de Servicio para API de interface con POS
	  */
	public String getHttpRequestPos();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive(boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsPriceChanged */
    public static final String COLUMNNAME_IsPriceChanged = "IsPriceChanged";

	/** Set IsPriceChanged.
	  * El precio fue modificado
	  */
	public void setIsPriceChanged(boolean IsPriceChanged);

	/** Get IsPriceChanged.
	  * El precio fue modificado
	  */
	public boolean isPriceChanged();

    /** Column name ServicioApiPos */
    public static final String COLUMNNAME_ServicioApiPos = "ServicioApiPos";

	/** Set ServicioApiPos.
	  * Servicio API para Interface con POS
	  */
	public void setServicioApiPos(String ServicioApiPos);

	/** Get ServicioApiPos.
	  * Servicio API para Interface con POS
	  */
	public String getServicioApiPos();

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

    /** Column name Z_ScanntechConfig_ID */
    public static final String COLUMNNAME_Z_ScanntechConfig_ID = "Z_ScanntechConfig_ID";

	/** Set Z_ScanntechConfig ID	  */
	public void setZ_ScanntechConfig_ID(int Z_ScanntechConfig_ID);

	/** Get Z_ScanntechConfig ID	  */
	public int getZ_ScanntechConfig_ID();

	public org.xpande.stech.model.I_Z_ScanntechConfig getZ_ScanntechConfig() throws RuntimeException;

    /** Column name Z_ScanntechConfigServ_ID */
    public static final String COLUMNNAME_Z_ScanntechConfigServ_ID = "Z_ScanntechConfigServ_ID";

	/** Set Z_ScanntechConfigServ ID	  */
	public void setZ_ScanntechConfigServ_ID(int Z_ScanntechConfigServ_ID);

	/** Get Z_ScanntechConfigServ ID	  */
	public int getZ_ScanntechConfigServ_ID();
}
