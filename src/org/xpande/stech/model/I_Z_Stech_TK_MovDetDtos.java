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

/** Generated Interface for Z_Stech_TK_MovDetDtos
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_Stech_TK_MovDetDtos 
{

    /** TableName=Z_Stech_TK_MovDetDtos */
    public static final String Table_Name = "Z_Stech_TK_MovDetDtos";

    /** AD_Table_ID=1000256 */
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
	public void setDateTrx(Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

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

    /** Column name JSonBody */
    public static final String COLUMNNAME_JSonBody = "JSonBody";

	/** Set JSonBody.
	  * Body de información utilizado en aplicaciones móbiles con JSon
	  */
	public void setJSonBody(String JSonBody);

	/** Get JSonBody.
	  * Body de información utilizado en aplicaciones móbiles con JSon
	  */
	public String getJSonBody();

    /** Column name SC_IdDescuento */
    public static final String COLUMNNAME_SC_IdDescuento = "SC_IdDescuento";

	/** Set SC_IdDescuento	  */
	public void setSC_IdDescuento(int SC_IdDescuento);

	/** Get SC_IdDescuento	  */
	public int getSC_IdDescuento();

    /** Column name SC_IdPromocion */
    public static final String COLUMNNAME_SC_IdPromocion = "SC_IdPromocion";

	/** Set SC_IdPromocion	  */
	public void setSC_IdPromocion(String SC_IdPromocion);

	/** Get SC_IdPromocion	  */
	public String getSC_IdPromocion();

    /** Column name SC_ImporteDescuento */
    public static final String COLUMNNAME_SC_ImporteDescuento = "SC_ImporteDescuento";

	/** Set SC_ImporteDescuento	  */
	public void setSC_ImporteDescuento(BigDecimal SC_ImporteDescuento);

	/** Get SC_ImporteDescuento	  */
	public BigDecimal getSC_ImporteDescuento();

    /** Column name SC_TipoDescuento */
    public static final String COLUMNNAME_SC_TipoDescuento = "SC_TipoDescuento";

	/** Set SC_TipoDescuento	  */
	public void setSC_TipoDescuento(String SC_TipoDescuento);

	/** Get SC_TipoDescuento	  */
	public String getSC_TipoDescuento();

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
	public void setUUID(String UUID);

	/** Get Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public String getUUID();

    /** Column name Z_StechInterfaceVta_ID */
    public static final String COLUMNNAME_Z_StechInterfaceVta_ID = "Z_StechInterfaceVta_ID";

	/** Set Z_StechInterfaceVta ID	  */
	public void setZ_StechInterfaceVta_ID(int Z_StechInterfaceVta_ID);

	/** Get Z_StechInterfaceVta ID	  */
	public int getZ_StechInterfaceVta_ID();

	public I_Z_StechInterfaceVta getZ_StechInterfaceVta() throws RuntimeException;

    /** Column name Z_Stech_TK_MovDetDtos_ID */
    public static final String COLUMNNAME_Z_Stech_TK_MovDetDtos_ID = "Z_Stech_TK_MovDetDtos_ID";

	/** Set Z_Stech_TK_MovDetDtos ID	  */
	public void setZ_Stech_TK_MovDetDtos_ID(int Z_Stech_TK_MovDetDtos_ID);

	/** Get Z_Stech_TK_MovDetDtos ID	  */
	public int getZ_Stech_TK_MovDetDtos_ID();

    /** Column name Z_Stech_TK_MovDet_ID */
    public static final String COLUMNNAME_Z_Stech_TK_MovDet_ID = "Z_Stech_TK_MovDet_ID";

	/** Set Z_Stech_TK_MovDet ID	  */
	public void setZ_Stech_TK_MovDet_ID(int Z_Stech_TK_MovDet_ID);

	/** Get Z_Stech_TK_MovDet ID	  */
	public int getZ_Stech_TK_MovDet_ID();

	public I_Z_Stech_TK_MovDet getZ_Stech_TK_MovDet() throws RuntimeException;

    /** Column name Z_Stech_TK_Mov_ID */
    public static final String COLUMNNAME_Z_Stech_TK_Mov_ID = "Z_Stech_TK_Mov_ID";

	/** Set Z_Stech_TK_Mov ID	  */
	public void setZ_Stech_TK_Mov_ID(int Z_Stech_TK_Mov_ID);

	/** Get Z_Stech_TK_Mov ID	  */
	public int getZ_Stech_TK_Mov_ID();

	public I_Z_Stech_TK_Mov getZ_Stech_TK_Mov() throws RuntimeException;
}
