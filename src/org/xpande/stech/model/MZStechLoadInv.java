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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.impexp.ImpFormat;
import org.compiere.model.*;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.xpande.comercial.model.MZComercialConfig;
import org.xpande.comercial.utils.ComercialUtils;
import org.xpande.core.model.MZSocioListaPrecio;
import org.xpande.core.utils.DateUtils;


/** Generated Model for Z_StechLoadInv
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class MZStechLoadInv extends X_Z_StechLoadInv implements DocAction, DocOptions {

	/**
	 *
	 */
	private static final long serialVersionUID = 20200525L;

    /** Standard Constructor */
    public MZStechLoadInv (Properties ctx, int Z_StechLoadInv_ID, String trxName)
    {
      super (ctx, Z_StechLoadInv_ID, trxName);
    }

    /** Load Constructor */
    public MZStechLoadInv (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx, int AD_Table_ID, String[] docAction, String[] options, int index) {

		int newIndex = 0;

		if ((docStatus.equalsIgnoreCase(STATUS_Drafted))
				|| (docStatus.equalsIgnoreCase(STATUS_Invalid))
				|| (docStatus.equalsIgnoreCase(STATUS_InProgress))){

			options[newIndex++] = DocumentEngine.ACTION_Complete;

		}
		else if (docStatus.equalsIgnoreCase(STATUS_Completed)){

			//options[newIndex++] = DocumentEngine.ACTION_None;
			options[newIndex++] = DocumentEngine.ACTION_ReActivate;
			//options[newIndex++] = DocumentEngine.ACTION_Void;
		}

		return newIndex;
	}

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName() + get_ID() +"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	processIt
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt()
	{
		log.info("unlockIt - " + toString());
	//	setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt
	
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());

		/*
		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateDoc(), dt.getDocBaseType(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}
		*/

		//	Add up Amounts
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info("rejectIt - " + toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		log.info(toString());
		//

		// Genero comprobantes de venta.
		m_processMsg =  this.generateInvoices();
		if (m_processMsg != null){
			return DocAction.STATUS_Invalid;
		}

		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		//	Set Definitive Document No
		setDefiniteDocumentNo();

		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt

	/**
	 * 	Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setDateDoc(new Timestamp(System.currentTimeMillis()));
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = null;
			int index = p_info.getColumnIndex("C_DocType_ID");
			if (index == -1)
				index = p_info.getColumnIndex("C_DocTypeTarget_ID");
			if (index != -1)		//	get based on Doc Type (might return null)
				value = DB.getDocumentNo(get_ValueAsInt(index), get_TrxName(), true);
			if (value != null) {
				setDocumentNo(value);
			}
		}
	}

	/**
	 * 	Void Document.
	 * 	Same as Close.
	 * 	@return true if success 
	 */
	public boolean voidIt()
	{
		log.info("voidIt - " + toString());
		return closeIt();
	}	//	voidIt
	
	/**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		log.info("closeIt - " + toString());

		//	Close Not delivered Qty
		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Correction
	 * 	@return true if success 
	 */
	public boolean reverseCorrectIt()
	{
		log.info("reverseCorrectIt - " + toString());
		return false;
	}	//	reverseCorrectionIt
	
	/**
	 * 	Reverse Accrual - none
	 * 	@return true if success 
	 */
	public boolean reverseAccrualIt()
	{
		log.info("reverseAccrualIt - " + toString());
		return false;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
	public boolean reActivateIt()
	{
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;

		// Obtengo lista de comprobantes generados en el sistema
		List<MZStechLoadAud> loadAudList = this.getLinesAud();
		for (MZStechLoadAud loadAud: loadAudList){

			if (loadAud.getC_Invoice_ID() > 0){
				MInvoice invoice = (MInvoice) loadAud.getC_Invoice();
				if (!invoice.reActivateIt()) {
					if (invoice.getProcessMsg() != null) {
						m_processMsg = " No se puedo reactivar Comprobante " + invoice.getDocumentNo() + ". " + invoice.getProcessMsg();
					} else {
						m_processMsg = " No se puedo reactivar Comprobante " + invoice.getDocumentNo();
					}
					return false;
				}
				else {
					invoice.saveEx();
					invoice.deleteEx(true);
				}
			}
		}

		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;

		this.setProcessed(false);
		this.setDocStatus(DOCSTATUS_InProgress);
		this.setDocAction(DOCACTION_Complete);

		return true;
	}	//	reActivateIt
	
	
	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
	//	sb.append(": ")
	//		.append(Msg.translate(getCtx(),"TotalLines")).append("=").append(getTotalLines())
	//		.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
	//	return getSalesRep_ID();
		return 0;
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return null;	//getTotalLines();
	}	//	getApprovalAmt
	
	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID()
	{
	//	MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID());
	//	return pl.getC_Currency_ID();
		return 0;
	}	//	getC_Currency_ID

    @Override
    public String toString()
    {
      StringBuffer sb = new StringBuffer ("MZStechLoadInv[")
        .append(getSummary()).append("]");
      return sb.toString();
    }

	/***
	 * Metodo que ejecuta el proceso de interface desde archivo.
	 * Xpande. Created by Gabriel Vila on 5/25/20.
	 */
	public void executeInterface(){

		try{
			// Elimino información anterior.
			this.deleteFileData();

			// Lee lineas de archivo
			this.getDataFromFile();

			// Valida lineas de archivo y trae información asociada.
			this.setDataFromFile();

			// Valido totales calculados contra totales documentados
			this.controlTotalesDocs();

		}
		catch (Exception e){
			throw new AdempiereException(e);
		}
	}


	/***
	 * Elimina información leída desde archivo.
	 * Xpande. Created by Gabriel Vila on 5/25/20.
	 */
	private void deleteFileData() {

		String action = "";

		try{
			action = " delete from " + I_Z_StechLoadInvFile.Table_Name +
					" where " + X_Z_StechLoadInvFile.COLUMNNAME_Z_StechLoadInv_ID + " =" + this.get_ID();
			DB.executeUpdateEx(action, get_TrxName());
		}
		catch (Exception e){
			throw new AdempiereException(e);
		}
	}

	/***
	 * Proceso que lee archivo de interface.
	 * Xpande. Created by Gabriel Vila on 5/25/20.
	 */
	public void getDataFromFile() {

		FileReader fReader = null;
		BufferedReader bReader = null;

		String lineaArchivo = null;
		String mensaje = "";
		String action = "";

		try{

			// Formato de importación de archivo de interface
			ImpFormat formatoImpArchivo = ImpFormat.load("Scanntech_CargaComprobantes");

			// Abro archivo
			File archivo = new File(this.getFileName());
			fReader = new FileReader(archivo);
			bReader = new BufferedReader(fReader);

			int contLineas = 0;
			int lineaID = 0;

			// Leo lineas del archivo
			lineaArchivo = bReader.readLine();

			while (lineaArchivo != null) {

				lineaArchivo = lineaArchivo.replace("'", "");
				//lineaArchivo = lineaArchivo.replace(",", "");
				contLineas++;

				lineaID = formatoImpArchivo.updateDB(lineaArchivo, getCtx(), get_TrxName());

				if (lineaID <= 0){
					MZStechLoadInvFile stechLoadInvFile = new MZStechLoadInvFile(getCtx(), 0, get_TrxName());
					stechLoadInvFile.setZ_StechLoadInv_ID(this.get_ID());
					stechLoadInvFile.setLineNumber(contLineas);
					stechLoadInvFile.setFileLineText(lineaArchivo);
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Formato de Linea Incorrecto.");
					stechLoadInvFile.saveEx();
				}
				else{
					// Seteo atributos de linea procesada en tabla
					action = " update " + I_Z_StechLoadInvFile.Table_Name +
							" set " + X_Z_StechLoadInvFile.COLUMNNAME_Z_StechLoadInv_ID + " = " + this.get_ID() + ", " +
							" LineNumber =" + contLineas + ", " +
							" FileLineText ='" + lineaArchivo + "' " +
							" where " + X_Z_StechLoadInvFile.COLUMNNAME_Z_StechLoadInvFile_ID + " = " + lineaID;
					DB.executeUpdateEx(action, get_TrxName());
				}

				lineaArchivo = bReader.readLine();
			}

			this.setQtyCount(contLineas);
			this.saveEx();

		}
		catch (Exception e){
			throw new AdempiereException(e);
		}
		finally {
			if (bReader != null){
				try{
					bReader.close();
					if (fReader != null){
						fReader.close();
					}
				}
				catch (Exception e){
					log.log(Level.SEVERE, e.getMessage());
				}
			}
		}
	}

	/***
	 * Valida lineas leídas desde archivo y carga información asociada.
	 * Xpande. Created by Gabriel Vila on 4/2/19.
	 */
	private void setDataFromFile() {

		try{
			MZScanntechConfig scanntechConfig = MZScanntechConfig.getDefault(getCtx(), null);

			int contadorOK = 0;
			int contadorError = 0;

			List<MZStechLoadInvFile> stechLoadInvFileList = this.getLines();
			for (MZStechLoadInvFile stechLoadInvFile : stechLoadInvFileList){

				// Obtengo organización según nombre de local leído en esta linea
				MZScanntechConfigOrg configOrg = MZScanntechConfigOrg.getByName(getCtx(), stechLoadInvFile.getNomLocal(), null);
				// Si no tengo organización para es nombre de local, ignoro totalmente este linea y la elimino.
				if ((configOrg == null) || (configOrg.get_ID() <= 0)){
					stechLoadInvFile.deleteEx(true);
					continue;
				}

				if (stechLoadInvFile.getErrorMsg() != null){
					contadorError++;
					continue;
				}

				stechLoadInvFile.setIsConfirmed(true);
				stechLoadInvFile.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());

				// Obtengo impuesto según tasa indicada en la linea.
				// Si la tasa no es númerica, elimino linea y la ignoro totalmente.
				if ((stechLoadInvFile.getSC_CodigoIVA() == null) || (stechLoadInvFile.getSC_CodigoIVA().trim().equalsIgnoreCase(""))){
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Debe indicar Tasa de Impuesto");
				}
				else {

					// Me aseguro tasa de impuesto numérica
					String tasaImpuAux = stechLoadInvFile.getSC_CodigoIVA().trim().replaceAll("[^0-9]", "");
					if (tasaImpuAux.equalsIgnoreCase("")){
						stechLoadInvFile.deleteEx(true);
						continue;
					}
				}

				// Obtengo impuesto según tasa ingresada
				if (stechLoadInvFile.isConfirmed()){

					MZScanntechConfigTax configTax = null;

					// Por nombre de impuesto POS
					if ((stechLoadInvFile.getNomImpuestoPOS() != null) && (!stechLoadInvFile.getNomImpuestoPOS().trim().equalsIgnoreCase(""))){
						configTax = MZScanntechConfigTax.getByImpuAplicaInterface(getCtx(), stechLoadInvFile.getNomImpuestoPOS(), true, null);
					}
					// Si no obtuve por nombre de impuesto, lo busco por tasa.
					if ((configTax == null) || (configTax.get_ID() <= 0)){
						configTax = MZScanntechConfigTax.getByRateAplicaInterface(getCtx(), stechLoadInvFile.getSC_CodigoIVA(), true, null);
					}

					if ((configTax == null) || (configTax.get_ID() <= 0)){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("No se encuentra Tasa de Impuesto en configuración scanntech.");
					}
					else {
						stechLoadInvFile.setC_Tax_ID(configTax.getC_Tax_ID());
						if (configTax.getM_Product_ID() <= 0){
							stechLoadInvFile.setIsConfirmed(false);
							stechLoadInvFile.setErrorMsg("No se encuentra Producto para Tasa de Impuesto en configuración scanntech.");
						}
						else{
							stechLoadInvFile.setM_Product_ID(configTax.getM_Product_ID());
						}
					}
				}

				// Tipo de Documento
				if (stechLoadInvFile.isConfirmed()){
					if ((stechLoadInvFile.getNomDocumento() == null) || (stechLoadInvFile.getNomDocumento().trim().equalsIgnoreCase(""))){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Debe indicar Tipo de Documento");
					}
					else{
						// Obtengo tipo de documento de ADempiere, segun nombre de documento de Scanntech
						MZStechConfigLoadInv configLoadInv = MZStechConfigLoadInv.getByNomDoc(getCtx(), scanntechConfig.get_ID(),
								stechLoadInvFile.getNomDocumento().trim(), null);
						if ((configLoadInv == null) || (configLoadInv.get_ID() <= 0)){
							stechLoadInvFile.setIsConfirmed(false);
							stechLoadInvFile.setErrorMsg("Tipo de Documento no se encuentra en Configuración Scanntech.");
						}
						else {
							stechLoadInvFile.setC_DocTypeInvoice_ID(configLoadInv.getC_DocType_ID());
						}
					}
				}

				// Serie Documento
				if (stechLoadInvFile.isConfirmed()){
					if ((stechLoadInvFile.getDocumentSerie() == null) || (stechLoadInvFile.getDocumentSerie().trim().equalsIgnoreCase(""))){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Debe indicar Serie del Documento");
					}
				}

				// Numero Documento
				if (stechLoadInvFile.isConfirmed()){
					if ((stechLoadInvFile.getDocumentNoRef() == null) || (stechLoadInvFile.getDocumentNoRef().trim().equalsIgnoreCase(""))){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Debe indicar Número del Documento");
					}
				}

				// Socio de Negocio por RUT
				if (stechLoadInvFile.isConfirmed()){
					if ((stechLoadInvFile.getTaxID() == null) || (stechLoadInvFile.getTaxID().trim().equalsIgnoreCase(""))){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Debe indicar Número de Identificación del Socio de Negocio, en la Linea del Archivo");
					}
					else{
						MBPartner partner = ComercialUtils.getPartnerByTaxID(getCtx(), stechLoadInvFile.getTaxID(), null);
						if ((partner == null) || (partner.get_ID() <= 0)){
							stechLoadInvFile.setIsConfirmed(false);
							stechLoadInvFile.setErrorMsg("No existe Socio de Negocio definido en el sistema con ese Número de Identificación : " + stechLoadInvFile.getTaxID());
						}
						else{
							stechLoadInvFile.setC_BPartner_ID(partner.get_ID());
						}
					}
				}

				// Valido que no existe un comprobante con ese socio de negocio, tipo de documento, serie y numero.
				if (stechLoadInvFile.isConfirmed()){
					MInvoice invoice = ComercialUtils.getInvoiceByDocPartner(getCtx(), stechLoadInvFile.getAD_OrgTrx_ID(),
							stechLoadInvFile.getC_DocTypeInvoice_ID(), stechLoadInvFile.getDocumentSerie(),
							stechLoadInvFile.getDocumentNoRef(), stechLoadInvFile.getC_BPartner_ID(), get_TrxName());
					if ((invoice != null) && (invoice.get_ID() > 0)){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Ya existe un comprobante en el sistema para ese Socio de Negocio - Documento");
					}
				}

				// Moneda
				if (stechLoadInvFile.isConfirmed()){
					if ((stechLoadInvFile.getNomMoneda() == null) || (stechLoadInvFile.getNomMoneda().trim().equalsIgnoreCase(""))){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Debe indicar Moneda");
					}
					else {
						if (stechLoadInvFile.getNomMoneda().toUpperCase().trim().equalsIgnoreCase("U$S")){
							stechLoadInvFile.setC_Currency_ID(100);
						}
						else {
							stechLoadInvFile.setC_Currency_ID(142);
						}
					}
				}

				// Fecha comprobante
				if (stechLoadInvFile.isConfirmed()){
					if ((stechLoadInvFile.getFechaCadena() == null) || (stechLoadInvFile.getFechaCadena().trim().equalsIgnoreCase(""))){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Debe indicar Fecha de Comprobante");
					}
					else{
						Timestamp fecDoc = DateUtils.convertStringToTimestamp_ddMMyyyy(stechLoadInvFile.getFechaCadena(), "/");
						if (fecDoc == null){
							stechLoadInvFile.setIsConfirmed(false);
							stechLoadInvFile.setErrorMsg("Formato de Fecha de Emisión inválido : " + stechLoadInvFile.getFechaCadena());
						}
						stechLoadInvFile.setDateTrx(fecDoc);
					}
				}

				// Montos
				if (stechLoadInvFile.getAmtRounding() == null) stechLoadInvFile.setAmtRounding(Env.ZERO);

				if (stechLoadInvFile.getTotalAmt() == null){
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Debe indicar Total del Comprobante");
				}

				if (stechLoadInvFile.isConfirmed()){
					if (stechLoadInvFile.getAmtSubtotal() == null){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Debe indicar Subtotal del Comprobante");
					}
				}

				if (stechLoadInvFile.isConfirmed()){
					if (stechLoadInvFile.getLineTotalAmt() == null){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Debe indicar Total Lineas con Impuesto");
					}
				}

				if (stechLoadInvFile.isConfirmed()){
					if (stechLoadInvFile.getTaxAmt() == null){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Debe indicar Monto Impuesto");
					}
				}

				if (stechLoadInvFile.isConfirmed()){
					if (stechLoadInvFile.getLineNetAmt() == null){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Debe indicar Total Lineas sin Impuesto");
					}
				}

				// Redondeo a dos cifras decimales despues de la coma
				stechLoadInvFile.setAmtRounding(stechLoadInvFile.getAmtRounding().setScale(2, RoundingMode.HALF_UP));
				stechLoadInvFile.setAmtSubtotal(stechLoadInvFile.getAmtSubtotal().setScale(2, RoundingMode.HALF_UP));
				stechLoadInvFile.setTotalAmt(stechLoadInvFile.getTotalAmt().setScale(2, RoundingMode.HALF_UP));
				stechLoadInvFile.setTaxAmt(stechLoadInvFile.getTaxAmt().setScale(2, RoundingMode.HALF_UP));
				stechLoadInvFile.setLineNetAmt(stechLoadInvFile.getLineNetAmt().setScale(2, RoundingMode.HALF_UP));
				stechLoadInvFile.setLineTotalAmt(stechLoadInvFile.getLineTotalAmt().setScale(2, RoundingMode.HALF_UP));

				if (stechLoadInvFile.isConfirmed()){
					contadorOK++;
				}
				else{
					contadorError++;
				}

				stechLoadInvFile.saveEx();
			}

			this.setQty(contadorOK);
			this.setQtyReject(contadorError);
			this.saveEx();

		}
		catch (Exception e){
			throw new AdempiereException(e);
		}
	}

	/***
	 * Obtiene y retorna lineas de este documento.
	 * Xpande. Created by Gabriel Vila on 5/25/20.
	 * @return
	 */
	public List<MZStechLoadInvFile> getLines(){

		String whereClause = X_Z_StechLoadInvFile.COLUMNNAME_Z_StechLoadInv_ID + " =" + this.get_ID();

		List<MZStechLoadInvFile> lines = new Query(getCtx(), I_Z_StechLoadInvFile.Table_Name, whereClause, get_TrxName()).list();

		return lines;
	}

	/***
	 * Obtiene y retorna lineas de comprobantes generados en el sistema.
	 * Xpande. Created by Gabriel Vila on 5/27/20.
	 * @return
	 */
	public List<MZStechLoadAud> getLinesAud(){

		String whereClause = X_Z_StechLoadAud.COLUMNNAME_Z_StechLoadInv_ID + " =" + this.get_ID();

		List<MZStechLoadAud> lines = new Query(getCtx(), I_Z_StechLoadAud.Table_Name, whereClause, get_TrxName()).list();

		return lines;
	}


	/***
	 * Genero Comprobante de Compra.
	 * Xpande. Created by Gabriel Vila on 5/27/20.
	 * @return
	 */
	private String generateInvoices() {

		String message = null;

		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
		    sql = " select z_stechloadinvfile_id " +
					" from z_stechloadinvfile " +
					" where z_stechloadinv_id =" + this.get_ID() +
					" and isconfirmed ='Y' " +
					" order by ad_orgtrx_id, c_bpartner_id, c_doctypeinvoice_id, documentserie, documentnoref, datetrx ";

			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();

			int adOrgIDAux = -1, cBPartnerIDAux = -1, cDocTypeIDAux = -1;
			String documentSerieAux = "", documentNoAux = "";
			MInvoice invoice = null;
			int contador = 0;

			while(rs.next()){

				contador++;
				System.out.println("MZStechLoadInv: Procesando lines " + contador);

				MZStechLoadInvFile loadInvFile = new MZStechLoadInvFile(getCtx(), rs.getInt("z_stechloadinvfile_id"), get_TrxName());

				// Corte por orgnizacio, socio de negocio, tipo de documento, serie y numero
				if ((loadInvFile.getAD_OrgTrx_ID() != adOrgIDAux) || (loadInvFile.getC_BPartner_ID() != cBPartnerIDAux)
					|| (loadInvFile.getC_DocTypeInvoice_ID() != cDocTypeIDAux) || (!loadInvFile.getDocumentSerie().equalsIgnoreCase(documentSerieAux))
					|| (!loadInvFile.getDocumentNoRef().equalsIgnoreCase(documentNoAux))){

					// Si tengo invoice ya cargada, la completo ahora.
					if ((invoice != null) && (invoice.get_ID() > 0)){
						if (!invoice.processIt(DocAction.ACTION_Complete)){
							if (invoice.getProcessMsg() != null) message = invoice.getProcessMsg();
							return "No se pudo completar Invoice en Venta Crédito Scanntech : " + message;
						}
						else{
							invoice.saveEx();

							// Guardo Auditoría
							MZStechLoadAud stechLoadAud = new MZStechLoadAud(getCtx(), 0, get_TrxName());
							stechLoadAud.setZ_StechLoadInv_ID(this.get_ID());
							stechLoadAud.setAD_OrgTrx_ID(invoice.getAD_Org_ID());
							stechLoadAud.setC_BPartner_ID(invoice.getC_BPartner_ID());
							stechLoadAud.setC_Currency_ID(invoice.getC_Currency_ID());
							stechLoadAud.setC_DocTypeInvoice_ID(invoice.getC_DocTypeTarget_ID());
							stechLoadAud.setC_Invoice_ID(invoice.get_ID());
							stechLoadAud.setDateInvoiced(invoice.getDateInvoiced());
							stechLoadAud.setDocumentNoRef(invoice.getDocumentNo());
							stechLoadAud.setDocumentSerie(invoice.get_ValueAsString("DocumentSerie"));
							stechLoadAud.setGrandTotal(invoice.getGrandTotal());
							stechLoadAud.saveEx();
						}
					}

					// Genero nueva  invoice
					invoice = new MInvoice(getCtx(), 0, get_TrxName());
					invoice.setAD_Org_ID(loadInvFile.getAD_OrgTrx_ID());
					invoice.setC_DocTypeTarget_ID(loadInvFile.getC_DocTypeInvoice_ID());
					invoice.setC_DocType_ID(loadInvFile.getC_DocTypeInvoice_ID());
					invoice.setIsSOTrx(false);
					invoice.setC_BPartner_ID(loadInvFile.getC_BPartner_ID());
					invoice.set_ValueOfColumn("DocumentSerie", loadInvFile.getDocumentSerie());
					invoice.setDocumentNo(loadInvFile.getDocumentNoRef());
					invoice.setDescription("Generado Automáticamente desde Carga Comprobantes Scanntech.");
					invoice.setDateInvoiced(loadInvFile.getDateTrx());
					invoice.setDateAcct(loadInvFile.getDateTrx());

					MBPartner partner = (MBPartner) loadInvFile.getC_BPartner();
					MBPartnerLocation[] partnerLocations = partner.getLocations(true);
					if (partnerLocations.length <= 0){
						return "Socio de Negocio no tiene Localización configurada: " + partner.getValue() + " - " + partner.getName();
					}
					MBPartnerLocation partnerLocation = partnerLocations[0];

					invoice.setC_BPartner_Location_ID(partnerLocation.get_ID());
					invoice.setC_Currency_ID(loadInvFile.getC_Currency_ID());
					invoice.set_ValueOfColumn("SubDocBaseType", "RET");
					if (partner.getPaymentRulePO() != null){
						invoice.setPaymentRule(partner.getPaymentRulePO());
					}
					if (partner.getPO_PaymentTerm_ID() > 0){
						invoice.setC_PaymentTerm_ID(partner.getPO_PaymentTerm_ID());
					}

					// Seteo lista de precios de compra del proveedor segun moneda
					MZSocioListaPrecio socioListaPrecio = MZSocioListaPrecio.getByPartnerCurrency(getCtx(), partner.get_ID(), loadInvFile.getC_Currency_ID(), get_TrxName());
					if ((socioListaPrecio == null) || (socioListaPrecio.get_ID() <= 0)){

						// No existe lista para este socio de negocio y moneda de compra. La creo y seteo al socio de negocio.
						MCurrency currency = (MCurrency) loadInvFile.getC_Currency();
						MPriceList pl = new MPriceList(getCtx(), 0, get_TrxName());
						pl.setName("LISTA " + partner.getName().toUpperCase() + " " + currency.getISO_Code());
						pl.setC_Currency_ID(loadInvFile.getC_Currency_ID());
						pl.setIsSOPriceList(false);
						pl.setIsTaxIncluded(true);
						pl.setIsNetPrice(false);
						pl.setPricePrecision(currency.getStdPrecision());
						pl.setAD_Org_ID(0);
						pl.saveEx();

						MPriceListVersion plv = new MPriceListVersion(pl);
						plv.setName("VIGENTE " + partner.getName().toUpperCase() + " " + currency.getISO_Code());
						plv.setM_DiscountSchema_ID(1000000);
						plv.saveEx();

						socioListaPrecio =  new MZSocioListaPrecio(getCtx(), 0, get_TrxName());
						socioListaPrecio.setC_BPartner_ID(partner.get_ID());
						socioListaPrecio.setC_Currency_ID(loadInvFile.getC_Currency_ID());
						socioListaPrecio.setM_PriceList_ID(pl.get_ID());
						socioListaPrecio.saveEx();
					}

					invoice.setM_PriceList_ID(socioListaPrecio.getM_PriceList_ID());

					// Seteo impuestos incluidos segun lista de precios
					MPriceList priceList = (MPriceList)socioListaPrecio.getM_PriceList();
					invoice.setIsTaxIncluded(priceList.isTaxIncluded());

					// Para notas de crédito, doy vuelta el signo del redondeo
					BigDecimal amtRounding = loadInvFile.getAmtRounding();
					MDocType docType = (MDocType) invoice.getC_DocTypeTarget();
					if (docType.getDocBaseType().equalsIgnoreCase("APC")){
						amtRounding = amtRounding.negate();
					}

					invoice.set_ValueOfColumn("AmtRounding", amtRounding);

					//invoice.set_ValueOfColumn("AmtAuxiliar", loadInvFile.getTotalAmt());
					invoice.saveEx();

					adOrgIDAux = loadInvFile.getAD_OrgTrx_ID();
					cBPartnerIDAux = loadInvFile.getC_BPartner_ID();
					cDocTypeIDAux = loadInvFile.getC_DocTypeInvoice_ID();
					documentSerieAux = loadInvFile.getDocumentSerie();
					documentNoAux = loadInvFile.getDocumentNoRef();
				}

				// Genero linea de invoice
				MInvoiceLine invLine = new MInvoiceLine(invoice);
				invLine.setAD_Org_ID(invoice.getAD_Org_ID());
				invLine.setC_Invoice_ID(invoice.get_ID());
				invLine.setM_Product_ID(loadInvFile.getM_Product_ID());
				invLine.setC_UOM_ID(100);
				invLine.setQtyEntered(Env.ONE);
				invLine.setQtyInvoiced(Env.ONE);
				invLine.setC_Tax_ID(loadInvFile.getC_Tax_ID());

				BigDecimal precioTotal = loadInvFile.getLineTotalAmt();
				if (precioTotal.compareTo(Env.ZERO) < 0){
					precioTotal = precioTotal.negate();
				}

				BigDecimal precioNeto = loadInvFile.getLineNetAmt();
				if (precioNeto.compareTo(Env.ZERO) < 0){
					precioNeto = precioNeto.negate();
				}


				if (invoice.isTaxIncluded()){
					invLine.setPriceActual(precioTotal);
					invLine.setPriceList(precioTotal);
					invLine.setPriceLimit(precioTotal);
					invLine.setPriceEntered(precioTotal);
					invLine.set_ValueOfColumn("PricePO", precioTotal);
					invLine.set_ValueOfColumn("PricePONoDto", precioTotal);
				}
				else {
					invLine.setPriceActual(precioNeto);
					invLine.setPriceList(precioNeto);
					invLine.setPriceLimit(precioNeto);
					invLine.setPriceEntered(precioNeto);
					invLine.set_ValueOfColumn("PricePO", precioNeto);
					invLine.set_ValueOfColumn("PricePONoDto", precioNeto);
				}
				invLine.setLineNetAmt();
				invLine.saveEx();
			}

			// Si tengo invoice ya cargada, la completo ahora.
			if ((invoice != null) && (invoice.get_ID() > 0)){
				if (!invoice.processIt(DocAction.ACTION_Complete)){
					if (invoice.getProcessMsg() != null) message = invoice.getProcessMsg();
					return "No se pudo completar Invoice en Venta Crédito Scanntech : " + message;
				}
				else{
					invoice.saveEx();
					// Guardo Auditoría
					MZStechLoadAud stechLoadAud = new MZStechLoadAud(getCtx(), 0, get_TrxName());
					stechLoadAud.setZ_StechLoadInv_ID(this.get_ID());
					stechLoadAud.setAD_OrgTrx_ID(invoice.getAD_Org_ID());
					stechLoadAud.setC_BPartner_ID(invoice.getC_BPartner_ID());
					stechLoadAud.setC_Currency_ID(invoice.getC_Currency_ID());
					stechLoadAud.setC_DocTypeInvoice_ID(invoice.getC_DocTypeTarget_ID());
					stechLoadAud.setC_Invoice_ID(invoice.get_ID());
					stechLoadAud.setDateInvoiced(invoice.getDateInvoiced());
					stechLoadAud.setDocumentNoRef(invoice.getDocumentNo());
					stechLoadAud.setDocumentSerie(invoice.get_ValueAsString("DocumentSerie"));
					stechLoadAud.setGrandTotal(invoice.getGrandTotal());
					stechLoadAud.saveEx();
				}
			}

		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}
		finally {
		    DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return message;
	}

	/***
	 * Controla los totales calculados contra los totales de documentos que vienen en la interface.
	 * Xpande. Created by Gabriel Vila on 7/13/20.
	 */
	private void controlTotalesDocs(){

		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			BigDecimal bordeInferior = new BigDecimal(-0.1);
			BigDecimal bordeSuperior = new BigDecimal(0.1);

			sql = " select ad_orgtrx_id, c_bpartner_id, c_doctypeinvoice_id, documentserie, documentnoref, totalamt, amtrounding, " +
					" sum(linenetamt + taxamt) as montocontrol " +
					" from z_stechloadinvfile " +
					" where z_stechloadinv_id =" + this.get_ID() +
					" and isconfirmed ='Y' " +
					" group by ad_orgtrx_id, c_bpartner_id, c_doctypeinvoice_id, documentserie, documentnoref, totalamt, amtrounding ";

			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();

			while(rs.next()){

				// Validación de valores recibidos:
				// Se tiene que cumplir lo siguiente: LineNetAmt + TaxAmt + AmtRounding = TotalAmt
				BigDecimal totalCalculado = rs.getBigDecimal("montocontrol");
				BigDecimal totalDocumento = rs.getBigDecimal("totalamt");
				BigDecimal amtRounding = rs.getBigDecimal("amtrounding");

				if (totalCalculado == null) totalCalculado = Env.ZERO;
				if (totalDocumento == null) totalDocumento = Env.ZERO;
				if (amtRounding == null) amtRounding = Env.ZERO;

				totalCalculado = totalCalculado.add((amtRounding));

				if (totalCalculado.compareTo(totalDocumento) != 0){

					BigDecimal diferencia = totalCalculado.subtract(totalDocumento);

					// Validacion de bordes de redondeo
					if ((diferencia.compareTo(bordeInferior) < 0) || (diferencia.compareTo(bordeSuperior) > 0)){
						String message = "Diferencia de Valores: Total Calculado =" + totalCalculado +
								" - Total Ingresado =" + totalDocumento +
								" - Diferencia =" + totalCalculado.subtract(totalDocumento);

						String action = " update z_stechloadinvfile set isconfirmed ='N', errormsg ='" + message + "' " +
								" where ad_orgtrx_id =" + rs.getInt("ad_orgtrx_id") +
								" and c_bpartner_id =" + rs.getInt("c_bpartner_id") +
								" and c_doctypeinvoice_id =" + rs.getInt("c_doctypeinvoice_id") +
								" and documentserie ='" + rs.getString("documentserie") + "' " +
								" and documentnoref ='" + rs.getString("documentnoref") + "' ";
						DB.executeUpdateEx(action, get_TrxName());
					}
				}
			}
		}
		catch (Exception e){
		    throw new AdempiereException(e);
		}
		finally {
		    DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

	}

}