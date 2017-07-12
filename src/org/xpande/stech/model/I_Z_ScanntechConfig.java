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

/** Generated Interface for Z_ScanntechConfig
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_ScanntechConfig 
{

    /** TableName=Z_ScanntechConfig */
    public static final String Table_Name = "Z_ScanntechConfig";

    /** AD_Table_ID=1000066 */
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
	public void setAD_Org_ID(int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name ClavePos */
    public static final String COLUMNNAME_ClavePos = "ClavePos";

	/** Set ClavePos.
	  * Clave de usuario de proveedor POS para interface de datos
	  */
	public void setClavePos(String ClavePos);

	/** Get ClavePos.
	  * Clave de usuario de proveedor POS para interface de datos
	  */
	public String getClavePos();

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

    /** Column name EmpresaPos */
    public static final String COLUMNNAME_EmpresaPos = "EmpresaPos";

	/** Set EmpresaPos.
	  * ID de empresa para interface POS
	  */
	public void setEmpresaPos(int EmpresaPos);

	/** Get EmpresaPos.
	  * ID de empresa para interface POS
	  */
	public int getEmpresaPos();

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

    /** Column name MetodoPos */
    public static final String COLUMNNAME_MetodoPos = "MetodoPos";

	/** Set MetodoPos.
	  * Método de proveedor POS para interface de datos
	  */
	public void setMetodoPos(String MetodoPos);

	/** Get MetodoPos.
	  * Método de proveedor POS para interface de datos
	  */
	public String getMetodoPos();

    /** Column name Timeout */
    public static final String COLUMNNAME_Timeout = "Timeout";

	/** Set Timeout.
	  * Is Timeout (In milliseconds) for sending or receive data
	  */
	public void setTimeout(int Timeout);

	/** Get Timeout.
	  * Is Timeout (In milliseconds) for sending or receive data
	  */
	public int getTimeout();

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

    /** Column name URL */
    public static final String COLUMNNAME_URL = "URL";

	/** Set URL.
	  * Full URL address - e.g. http://www.adempiere.org
	  */
	public void setURL(String URL);

	/** Get URL.
	  * Full URL address - e.g. http://www.adempiere.org
	  */
	public String getURL();

    /** Column name UsuarioPos */
    public static final String COLUMNNAME_UsuarioPos = "UsuarioPos";

	/** Set UsuarioPos.
	  * Usuario de proveedor POS para interface de datos
	  */
	public void setUsuarioPos(String UsuarioPos);

	/** Get UsuarioPos.
	  * Usuario de proveedor POS para interface de datos
	  */
	public String getUsuarioPos();

    /** Column name Z_ScanntechConfig_ID */
    public static final String COLUMNNAME_Z_ScanntechConfig_ID = "Z_ScanntechConfig_ID";

	/** Set Z_ScanntechConfig ID	  */
	public void setZ_ScanntechConfig_ID(int Z_ScanntechConfig_ID);

	/** Get Z_ScanntechConfig ID	  */
	public int getZ_ScanntechConfig_ID();
}
