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

/** Generated Interface for Z_Stech_TK_MovDet
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_Stech_TK_MovDet 
{

    /** TableName=Z_Stech_TK_MovDet */
    public static final String Table_Name = "Z_Stech_TK_MovDet";

    /** AD_Table_ID=1000213 */
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

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID(int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public I_M_Product getM_Product() throws RuntimeException;

    /** Column name SC_Cantidad */
    public static final String COLUMNNAME_SC_Cantidad = "SC_Cantidad";

	/** Set SC_Cantidad	  */
	public void setSC_Cantidad(BigDecimal SC_Cantidad);

	/** Get SC_Cantidad	  */
	public BigDecimal getSC_Cantidad();

    /** Column name SC_CodigoArticulo */
    public static final String COLUMNNAME_SC_CodigoArticulo = "SC_CodigoArticulo";

	/** Set SC_CodigoArticulo	  */
	public void setSC_CodigoArticulo(String SC_CodigoArticulo);

	/** Get SC_CodigoArticulo	  */
	public String getSC_CodigoArticulo();

    /** Column name SC_CodigoArticuloPadre */
    public static final String COLUMNNAME_SC_CodigoArticuloPadre = "SC_CodigoArticuloPadre";

	/** Set SC_CodigoArticuloPadre	  */
	public void setSC_CodigoArticuloPadre(String SC_CodigoArticuloPadre);

	/** Get SC_CodigoArticuloPadre	  */
	public String getSC_CodigoArticuloPadre();

    /** Column name SC_CodigoBarras */
    public static final String COLUMNNAME_SC_CodigoBarras = "SC_CodigoBarras";

	/** Set SC_CodigoBarras	  */
	public void setSC_CodigoBarras(String SC_CodigoBarras);

	/** Get SC_CodigoBarras	  */
	public String getSC_CodigoBarras();

    /** Column name SC_CodigoCategoria */
    public static final String COLUMNNAME_SC_CodigoCategoria = "SC_CodigoCategoria";

	/** Set SC_CodigoCategoria	  */
	public void setSC_CodigoCategoria(int SC_CodigoCategoria);

	/** Get SC_CodigoCategoria	  */
	public int getSC_CodigoCategoria();

    /** Column name SC_CodigoServicio */
    public static final String COLUMNNAME_SC_CodigoServicio = "SC_CodigoServicio";

	/** Set SC_CodigoServicio	  */
	public void setSC_CodigoServicio(int SC_CodigoServicio);

	/** Get SC_CodigoServicio	  */
	public int getSC_CodigoServicio();

    /** Column name SC_CodigoTipoDetalle */
    public static final String COLUMNNAME_SC_CodigoTipoDetalle = "SC_CodigoTipoDetalle";

	/** Set SC_CodigoTipoDetalle	  */
	public void setSC_CodigoTipoDetalle(int SC_CodigoTipoDetalle);

	/** Get SC_CodigoTipoDetalle	  */
	public int getSC_CodigoTipoDetalle();

    /** Column name SC_DescripcionArticulo */
    public static final String COLUMNNAME_SC_DescripcionArticulo = "SC_DescripcionArticulo";

	/** Set SC_DescripcionArticulo	  */
	public void setSC_DescripcionArticulo(String SC_DescripcionArticulo);

	/** Get SC_DescripcionArticulo	  */
	public String getSC_DescripcionArticulo();

    /** Column name SC_Descuento */
    public static final String COLUMNNAME_SC_Descuento = "SC_Descuento";

	/** Set SC_Descuento	  */
	public void setSC_Descuento(BigDecimal SC_Descuento);

	/** Get SC_Descuento	  */
	public BigDecimal getSC_Descuento();

    /** Column name SC_FechaServicio */
    public static final String COLUMNNAME_SC_FechaServicio = "SC_FechaServicio";

	/** Set SC_FechaServicio	  */
	public void setSC_FechaServicio(Timestamp SC_FechaServicio);

	/** Get SC_FechaServicio	  */
	public Timestamp getSC_FechaServicio();

    /** Column name SC_Importe */
    public static final String COLUMNNAME_SC_Importe = "SC_Importe";

	/** Set SC_Importe	  */
	public void setSC_Importe(BigDecimal SC_Importe);

	/** Get SC_Importe	  */
	public BigDecimal getSC_Importe();

    /** Column name SC_ImporteUnitario */
    public static final String COLUMNNAME_SC_ImporteUnitario = "SC_ImporteUnitario";

	/** Set SC_ImporteUnitario	  */
	public void setSC_ImporteUnitario(BigDecimal SC_ImporteUnitario);

	/** Get SC_ImporteUnitario	  */
	public BigDecimal getSC_ImporteUnitario();

    /** Column name SC_MedidaVenta */
    public static final String COLUMNNAME_SC_MedidaVenta = "SC_MedidaVenta";

	/** Set SC_MedidaVenta	  */
	public void setSC_MedidaVenta(String SC_MedidaVenta);

	/** Get SC_MedidaVenta	  */
	public String getSC_MedidaVenta();

    /** Column name SC_MontoIVA */
    public static final String COLUMNNAME_SC_MontoIVA = "SC_MontoIVA";

	/** Set SC_MontoIVA	  */
	public void setSC_MontoIVA(BigDecimal SC_MontoIVA);

	/** Get SC_MontoIVA	  */
	public BigDecimal getSC_MontoIVA();

    /** Column name SC_NumeroServicio */
    public static final String COLUMNNAME_SC_NumeroServicio = "SC_NumeroServicio";

	/** Set SC_NumeroServicio	  */
	public void setSC_NumeroServicio(String SC_NumeroServicio);

	/** Get SC_NumeroServicio	  */
	public String getSC_NumeroServicio();

    /** Column name SC_PorcentajeIVA */
    public static final String COLUMNNAME_SC_PorcentajeIVA = "SC_PorcentajeIVA";

	/** Set SC_PorcentajeIVA	  */
	public void setSC_PorcentajeIVA(BigDecimal SC_PorcentajeIVA);

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
	public void setUUID(String UUID);

	/** Get Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public String getUUID();

    /** Column name Z_ProductoUPC_ID */
    public static final String COLUMNNAME_Z_ProductoUPC_ID = "Z_ProductoUPC_ID";

	/** Set Z_ProductoUPC ID	  */
	public void setZ_ProductoUPC_ID(int Z_ProductoUPC_ID);

	/** Get Z_ProductoUPC ID	  */
	public int getZ_ProductoUPC_ID();

    /** Column name Z_StechInterfaceVta_ID */
    public static final String COLUMNNAME_Z_StechInterfaceVta_ID = "Z_StechInterfaceVta_ID";

	/** Set Z_StechInterfaceVta ID	  */
	public void setZ_StechInterfaceVta_ID(int Z_StechInterfaceVta_ID);

	/** Get Z_StechInterfaceVta ID	  */
	public int getZ_StechInterfaceVta_ID();

	public I_Z_StechInterfaceVta getZ_StechInterfaceVta() throws RuntimeException;

    /** Column name Z_Stech_TK_MovDet_ID */
    public static final String COLUMNNAME_Z_Stech_TK_MovDet_ID = "Z_Stech_TK_MovDet_ID";

	/** Set Z_Stech_TK_MovDet ID	  */
	public void setZ_Stech_TK_MovDet_ID(int Z_Stech_TK_MovDet_ID);

	/** Get Z_Stech_TK_MovDet ID	  */
	public int getZ_Stech_TK_MovDet_ID();

    /** Column name Z_Stech_TK_Mov_ID */
    public static final String COLUMNNAME_Z_Stech_TK_Mov_ID = "Z_Stech_TK_Mov_ID";

	/** Set Z_Stech_TK_Mov ID	  */
	public void setZ_Stech_TK_Mov_ID(int Z_Stech_TK_Mov_ID);

	/** Get Z_Stech_TK_Mov ID	  */
	public int getZ_Stech_TK_Mov_ID();

	public I_Z_Stech_TK_Mov getZ_Stech_TK_Mov() throws RuntimeException;
}
