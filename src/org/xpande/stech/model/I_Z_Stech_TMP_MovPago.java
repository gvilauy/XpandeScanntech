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

/** Generated Interface for Z_Stech_TMP_MovPago
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_Z_Stech_TMP_MovPago 
{

    /** TableName=Z_Stech_TMP_MovPago */
    public static final String Table_Name = "Z_Stech_TMP_MovPago";

    /** AD_Table_ID=1000380 */
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

    /** Column name SC_Cambio */
    public static final String COLUMNNAME_SC_Cambio = "SC_Cambio";

	/** Set SC_Cambio	  */
	public void setSC_Cambio (boolean SC_Cambio);

	/** Get SC_Cambio	  */
	public boolean isSC_Cambio();

    /** Column name SC_CodigoCliente */
    public static final String COLUMNNAME_SC_CodigoCliente = "SC_CodigoCliente";

	/** Set SC_CodigoCliente	  */
	public void setSC_CodigoCliente (String SC_CodigoCliente);

	/** Get SC_CodigoCliente	  */
	public String getSC_CodigoCliente();

    /** Column name SC_CodigoCredito */
    public static final String COLUMNNAME_SC_CodigoCredito = "SC_CodigoCredito";

	/** Set SC_CodigoCredito	  */
	public void setSC_CodigoCredito (int SC_CodigoCredito);

	/** Get SC_CodigoCredito	  */
	public int getSC_CodigoCredito();

    /** Column name SC_CodigoMoneda */
    public static final String COLUMNNAME_SC_CodigoMoneda = "SC_CodigoMoneda";

	/** Set SC_CodigoMoneda	  */
	public void setSC_CodigoMoneda (String SC_CodigoMoneda);

	/** Get SC_CodigoMoneda	  */
	public String getSC_CodigoMoneda();

    /** Column name SC_CodigoPlanPagos */
    public static final String COLUMNNAME_SC_CodigoPlanPagos = "SC_CodigoPlanPagos";

	/** Set SC_CodigoPlanPagos	  */
	public void setSC_CodigoPlanPagos (int SC_CodigoPlanPagos);

	/** Get SC_CodigoPlanPagos	  */
	public int getSC_CodigoPlanPagos();

    /** Column name SC_CodigoTipoPago */
    public static final String COLUMNNAME_SC_CodigoTipoPago = "SC_CodigoTipoPago";

	/** Set SC_CodigoTipoPago	  */
	public void setSC_CodigoTipoPago (int SC_CodigoTipoPago);

	/** Get SC_CodigoTipoPago	  */
	public int getSC_CodigoTipoPago();

    /** Column name SC_CodigoVale */
    public static final String COLUMNNAME_SC_CodigoVale = "SC_CodigoVale";

	/** Set SC_CodigoVale.
	  * Código de Vale para POS Scanntech
	  */
	public void setSC_CodigoVale (int SC_CodigoVale);

	/** Get SC_CodigoVale.
	  * Código de Vale para POS Scanntech
	  */
	public int getSC_CodigoVale();

    /** Column name SC_ComercioCredito */
    public static final String COLUMNNAME_SC_ComercioCredito = "SC_ComercioCredito";

	/** Set SC_ComercioCredito	  */
	public void setSC_ComercioCredito (String SC_ComercioCredito);

	/** Get SC_ComercioCredito	  */
	public String getSC_ComercioCredito();

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

    /** Column name SC_DescuentoAfam */
    public static final String COLUMNNAME_SC_DescuentoAfam = "SC_DescuentoAfam";

	/** Set SC_DescuentoAfam.
	  * Descuento Afam para POS Scanntech
	  */
	public void setSC_DescuentoAfam (BigDecimal SC_DescuentoAfam);

	/** Get SC_DescuentoAfam.
	  * Descuento Afam para POS Scanntech
	  */
	public BigDecimal getSC_DescuentoAfam();

    /** Column name SC_DescuentoIncFin */
    public static final String COLUMNNAME_SC_DescuentoIncFin = "SC_DescuentoIncFin";

	/** Set SC_DescuentoIncFin.
	  * Descuento por inclusión financiera para POS Scanntech
	  */
	public void setSC_DescuentoIncFin (BigDecimal SC_DescuentoIncFin);

	/** Get SC_DescuentoIncFin.
	  * Descuento por inclusión financiera para POS Scanntech
	  */
	public BigDecimal getSC_DescuentoIncFin();

    /** Column name SC_DocumentoCliente */
    public static final String COLUMNNAME_SC_DocumentoCliente = "SC_DocumentoCliente";

	/** Set SC_DocumentoCliente	  */
	public void setSC_DocumentoCliente (String SC_DocumentoCliente);

	/** Get SC_DocumentoCliente	  */
	public String getSC_DocumentoCliente();

    /** Column name SC_FechaOperacion */
    public static final String COLUMNNAME_SC_FechaOperacion = "SC_FechaOperacion";

	/** Set SC_FechaOperacion	  */
	public void setSC_FechaOperacion (Timestamp SC_FechaOperacion);

	/** Get SC_FechaOperacion	  */
	public Timestamp getSC_FechaOperacion();

    /** Column name SC_FechaVencimiento */
    public static final String COLUMNNAME_SC_FechaVencimiento = "SC_FechaVencimiento";

	/** Set SC_FechaVencimiento	  */
	public void setSC_FechaVencimiento (Timestamp SC_FechaVencimiento);

	/** Get SC_FechaVencimiento	  */
	public Timestamp getSC_FechaVencimiento();

    /** Column name SC_Importe */
    public static final String COLUMNNAME_SC_Importe = "SC_Importe";

	/** Set SC_Importe	  */
	public void setSC_Importe (BigDecimal SC_Importe);

	/** Get SC_Importe	  */
	public BigDecimal getSC_Importe();

    /** Column name SC_ImportePago */
    public static final String COLUMNNAME_SC_ImportePago = "SC_ImportePago";

	/** Set SC_ImportePago	  */
	public void setSC_ImportePago (BigDecimal SC_ImportePago);

	/** Get SC_ImportePago	  */
	public BigDecimal getSC_ImportePago();

    /** Column name SC_NumeroAutorizacion */
    public static final String COLUMNNAME_SC_NumeroAutorizacion = "SC_NumeroAutorizacion";

	/** Set SC_NumeroAutorizacion	  */
	public void setSC_NumeroAutorizacion (String SC_NumeroAutorizacion);

	/** Get SC_NumeroAutorizacion	  */
	public String getSC_NumeroAutorizacion();

    /** Column name SC_NumeroCuotasPago */
    public static final String COLUMNNAME_SC_NumeroCuotasPago = "SC_NumeroCuotasPago";

	/** Set SC_NumeroCuotasPago	  */
	public void setSC_NumeroCuotasPago (int SC_NumeroCuotasPago);

	/** Get SC_NumeroCuotasPago	  */
	public int getSC_NumeroCuotasPago();

    /** Column name SC_NumeroDocumentoPago */
    public static final String COLUMNNAME_SC_NumeroDocumentoPago = "SC_NumeroDocumentoPago";

	/** Set SC_NumeroDocumentoPago	  */
	public void setSC_NumeroDocumentoPago (String SC_NumeroDocumentoPago);

	/** Get SC_NumeroDocumentoPago	  */
	public String getSC_NumeroDocumentoPago();

    /** Column name SC_NumeroTarjeta */
    public static final String COLUMNNAME_SC_NumeroTarjeta = "SC_NumeroTarjeta";

	/** Set SC_NumeroTarjeta	  */
	public void setSC_NumeroTarjeta (String SC_NumeroTarjeta);

	/** Get SC_NumeroTarjeta	  */
	public String getSC_NumeroTarjeta();

    /** Column name SC_TerminalCredito */
    public static final String COLUMNNAME_SC_TerminalCredito = "SC_TerminalCredito";

	/** Set SC_TerminalCredito	  */
	public void setSC_TerminalCredito (String SC_TerminalCredito);

	/** Get SC_TerminalCredito	  */
	public String getSC_TerminalCredito();

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

    /** Column name Z_StechCreditos_ID */
    public static final String COLUMNNAME_Z_StechCreditos_ID = "Z_StechCreditos_ID";

	/** Set Z_StechCreditos ID	  */
	public void setZ_StechCreditos_ID (int Z_StechCreditos_ID);

	/** Get Z_StechCreditos ID	  */
	public int getZ_StechCreditos_ID();

	public I_Z_StechCreditos getZ_StechCreditos() throws RuntimeException;

    /** Column name Z_StechInterfaceVta_ID */
    public static final String COLUMNNAME_Z_StechInterfaceVta_ID = "Z_StechInterfaceVta_ID";

	/** Set Z_StechInterfaceVta ID	  */
	public void setZ_StechInterfaceVta_ID (int Z_StechInterfaceVta_ID);

	/** Get Z_StechInterfaceVta ID	  */
	public int getZ_StechInterfaceVta_ID();

	public I_Z_StechInterfaceVta getZ_StechInterfaceVta() throws RuntimeException;

    /** Column name Z_StechMedioPago_ID */
    public static final String COLUMNNAME_Z_StechMedioPago_ID = "Z_StechMedioPago_ID";

	/** Set Z_StechMedioPago ID	  */
	public void setZ_StechMedioPago_ID (int Z_StechMedioPago_ID);

	/** Get Z_StechMedioPago ID	  */
	public int getZ_StechMedioPago_ID();

	public I_Z_StechMedioPago getZ_StechMedioPago() throws RuntimeException;

    /** Column name Z_Stech_TMP_Mov_ID */
    public static final String COLUMNNAME_Z_Stech_TMP_Mov_ID = "Z_Stech_TMP_Mov_ID";

	/** Set Z_Stech_TMP_Mov ID	  */
	public void setZ_Stech_TMP_Mov_ID (int Z_Stech_TMP_Mov_ID);

	/** Get Z_Stech_TMP_Mov ID	  */
	public int getZ_Stech_TMP_Mov_ID();

	public I_Z_Stech_TMP_Mov getZ_Stech_TMP_Mov() throws RuntimeException;

    /** Column name Z_Stech_TMP_MovPago_ID */
    public static final String COLUMNNAME_Z_Stech_TMP_MovPago_ID = "Z_Stech_TMP_MovPago_ID";

	/** Set Z_Stech_TMP_MovPago ID	  */
	public void setZ_Stech_TMP_MovPago_ID (int Z_Stech_TMP_MovPago_ID);

	/** Get Z_Stech_TMP_MovPago ID	  */
	public int getZ_Stech_TMP_MovPago_ID();
}
