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

/** Generated Interface for Z_Stech_TMP_Mov
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_Stech_TMP_Mov 
{

    /** TableName=Z_Stech_TMP_Mov */
    public static final String Table_Name = "Z_Stech_TMP_Mov";

    /** AD_Table_ID=1000377 */
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

    /** Column name JSonBody */
    public static final String COLUMNNAME_JSonBody = "JSonBody";

	/** Set JSonBody.
	  * Body de información utilizado en aplicaciones móbiles con JSon
	  */
	public void setJSonBody (String JSonBody);

	/** Get JSonBody.
	  * Body de información utilizado en aplicaciones móbiles con JSon
	  */
	public String getJSonBody();

    /** Column name SC_CantidadItems */
    public static final String COLUMNNAME_SC_CantidadItems = "SC_CantidadItems";

	/** Set SC_CantidadItems	  */
	public void setSC_CantidadItems (int SC_CantidadItems);

	/** Get SC_CantidadItems	  */
	public int getSC_CantidadItems();

    /** Column name SC_CodigoCaja */
    public static final String COLUMNNAME_SC_CodigoCaja = "SC_CodigoCaja";

	/** Set SC_CodigoCaja	  */
	public void setSC_CodigoCaja (int SC_CodigoCaja);

	/** Get SC_CodigoCaja	  */
	public int getSC_CodigoCaja();

    /** Column name SC_CodigoEmpresa */
    public static final String COLUMNNAME_SC_CodigoEmpresa = "SC_CodigoEmpresa";

	/** Set SC_CodigoEmpresa	  */
	public void setSC_CodigoEmpresa (int SC_CodigoEmpresa);

	/** Get SC_CodigoEmpresa	  */
	public int getSC_CodigoEmpresa();

    /** Column name SC_CodigoLocal */
    public static final String COLUMNNAME_SC_CodigoLocal = "SC_CodigoLocal";

	/** Set SC_CodigoLocal	  */
	public void setSC_CodigoLocal (int SC_CodigoLocal);

	/** Get SC_CodigoLocal	  */
	public int getSC_CodigoLocal();

    /** Column name SC_CodigoMoneda */
    public static final String COLUMNNAME_SC_CodigoMoneda = "SC_CodigoMoneda";

	/** Set SC_CodigoMoneda	  */
	public void setSC_CodigoMoneda (String SC_CodigoMoneda);

	/** Get SC_CodigoMoneda	  */
	public String getSC_CodigoMoneda();

    /** Column name SC_CodigoSeguridadCfe */
    public static final String COLUMNNAME_SC_CodigoSeguridadCfe = "SC_CodigoSeguridadCfe";

	/** Set SC_CodigoSeguridadCfe	  */
	public void setSC_CodigoSeguridadCfe (String SC_CodigoSeguridadCfe);

	/** Get SC_CodigoSeguridadCfe	  */
	public String getSC_CodigoSeguridadCfe();

    /** Column name SC_CotizacionCompra */
    public static final String COLUMNNAME_SC_CotizacionCompra = "SC_CotizacionCompra";

	/** Set SC_CotizacionCompra	  */
	public void setSC_CotizacionCompra (BigDecimal SC_CotizacionCompra);

	/** Get SC_CotizacionCompra	  */
	public BigDecimal getSC_CotizacionCompra();

    /** Column name SC_CotizacionVenta */
    public static final String COLUMNNAME_SC_CotizacionVenta = "SC_CotizacionVenta";

	/** Set SC_CotizacionVenta	  */
	public void setSC_CotizacionVenta (BigDecimal SC_CotizacionVenta);

	/** Get SC_CotizacionVenta	  */
	public BigDecimal getSC_CotizacionVenta();

    /** Column name SC_CuponAnulada */
    public static final String COLUMNNAME_SC_CuponAnulada = "SC_CuponAnulada";

	/** Set SC_CuponAnulada.
	  *  
	  */
	public void setSC_CuponAnulada (boolean SC_CuponAnulada);

	/** Get SC_CuponAnulada.
	  *  
	  */
	public boolean isSC_CuponAnulada();

    /** Column name SC_CuponCancelado */
    public static final String COLUMNNAME_SC_CuponCancelado = "SC_CuponCancelado";

	/** Set SC_CuponCancelado	  */
	public void setSC_CuponCancelado (boolean SC_CuponCancelado);

	/** Get SC_CuponCancelado	  */
	public boolean isSC_CuponCancelado();

    /** Column name SC_DescuentoTotal */
    public static final String COLUMNNAME_SC_DescuentoTotal = "SC_DescuentoTotal";

	/** Set SC_DescuentoTotal	  */
	public void setSC_DescuentoTotal (BigDecimal SC_DescuentoTotal);

	/** Get SC_DescuentoTotal	  */
	public BigDecimal getSC_DescuentoTotal();

    /** Column name SC_DireccionFactura */
    public static final String COLUMNNAME_SC_DireccionFactura = "SC_DireccionFactura";

	/** Set SC_DireccionFactura.
	  * Direccion de factura para Scanntech
	  */
	public void setSC_DireccionFactura (String SC_DireccionFactura);

	/** Get SC_DireccionFactura.
	  * Direccion de factura para Scanntech
	  */
	public String getSC_DireccionFactura();

    /** Column name SC_FechaOperacion */
    public static final String COLUMNNAME_SC_FechaOperacion = "SC_FechaOperacion";

	/** Set SC_FechaOperacion	  */
	public void setSC_FechaOperacion (Timestamp SC_FechaOperacion);

	/** Get SC_FechaOperacion	  */
	public Timestamp getSC_FechaOperacion();

    /** Column name SC_NombreFactura */
    public static final String COLUMNNAME_SC_NombreFactura = "SC_NombreFactura";

	/** Set SC_NombreFactura.
	  * Nombre de factura para Scanntech
	  */
	public void setSC_NombreFactura (String SC_NombreFactura);

	/** Get SC_NombreFactura.
	  * Nombre de factura para Scanntech
	  */
	public String getSC_NombreFactura();

    /** Column name SC_NumeroMov */
    public static final String COLUMNNAME_SC_NumeroMov = "SC_NumeroMov";

	/** Set SC_NumeroMov	  */
	public void setSC_NumeroMov (String SC_NumeroMov);

	/** Get SC_NumeroMov	  */
	public String getSC_NumeroMov();

    /** Column name SC_NumeroOperacion */
    public static final String COLUMNNAME_SC_NumeroOperacion = "SC_NumeroOperacion";

	/** Set SC_NumeroOperacion	  */
	public void setSC_NumeroOperacion (String SC_NumeroOperacion);

	/** Get SC_NumeroOperacion	  */
	public String getSC_NumeroOperacion();

    /** Column name SC_Redondeo */
    public static final String COLUMNNAME_SC_Redondeo = "SC_Redondeo";

	/** Set SC_Redondeo	  */
	public void setSC_Redondeo (BigDecimal SC_Redondeo);

	/** Get SC_Redondeo	  */
	public BigDecimal getSC_Redondeo();

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

    /** Column name SC_TipoOperacion */
    public static final String COLUMNNAME_SC_TipoOperacion = "SC_TipoOperacion";

	/** Set SC_TipoOperacion	  */
	public void setSC_TipoOperacion (String SC_TipoOperacion);

	/** Get SC_TipoOperacion	  */
	public String getSC_TipoOperacion();

    /** Column name SC_Total */
    public static final String COLUMNNAME_SC_Total = "SC_Total";

	/** Set SC_Total	  */
	public void setSC_Total (BigDecimal SC_Total);

	/** Get SC_Total	  */
	public BigDecimal getSC_Total();

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

    /** Column name Z_Stech_TMP_Mov_ID */
    public static final String COLUMNNAME_Z_Stech_TMP_Mov_ID = "Z_Stech_TMP_Mov_ID";

	/** Set Z_Stech_TMP_Mov ID	  */
	public void setZ_Stech_TMP_Mov_ID (int Z_Stech_TMP_Mov_ID);

	/** Get Z_Stech_TMP_Mov ID	  */
	public int getZ_Stech_TMP_Mov_ID();
}
