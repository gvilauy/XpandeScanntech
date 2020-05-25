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
import org.xpande.core.utils.DateUtils;
import org.xpande.financial.model.I_Z_LoadPagoFile;
import org.xpande.financial.model.MZLoadPagoFile;
import org.xpande.financial.model.X_Z_LoadPagoFile;

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

			options[newIndex++] = DocumentEngine.ACTION_None;
			//options[newIndex++] = DocumentEngine.ACTION_ReActivate;
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

		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateDoc(), dt.getDocBaseType(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}
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
		log.info("reActivateIt - " + toString());
		setProcessed(false);
		if (reverseCorrectIt())
			return true;
		return false;
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

			MZComercialConfig comercialConfig = MZComercialConfig.getDefault(getCtx(), null);

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

				stechLoadInvFile.setIsConfirmed(true);
				stechLoadInvFile.setAD_OrgTrx_ID(configOrg.getAD_OrgTrx_ID());

				// Obtengo impuesto según tasa ingresada
				MZScanntechConfigTax configTax = MZScanntechConfigTax.getByRateAplicaInterface(getCtx(), stechLoadInvFile.getSC_CodigoIVA(), true, null);
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

				// Tipo de Documento
				if ((stechLoadInvFile.getNomDocumento() == null) || (stechLoadInvFile.getNomDocumento().trim().equalsIgnoreCase(""))){
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Debe indicar Tipo de Documento");
				}
				else{
					if (stechLoadInvFile.getNomDocumento().trim().toUpperCase().equalsIgnoreCase("FACTURA")){
						stechLoadInvFile.setC_DocTypeInvoice_ID(comercialConfig.getDefaultDocAPI_ID());
					}
					else {
						stechLoadInvFile.setC_DocTypeInvoice_ID(comercialConfig.getDefaultDocAPC_ID());
					}
				}

				// Serie Documento
				if ((stechLoadInvFile.getDocumentSerie() == null) || (stechLoadInvFile.getDocumentSerie().trim().equalsIgnoreCase(""))){
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Debe indicar Serie del Documento");
				}

				// Numero Documento
				if ((stechLoadInvFile.getDocumentNoRef() == null) || (stechLoadInvFile.getDocumentNoRef().trim().equalsIgnoreCase(""))){
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Debe indicar Número del Documento");
				}

				// Socio de Negocio por RUT
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

				// Valido que no existe un comprobante con ese socio de negocio, tipo de documento, serie y numero.
				if (stechLoadInvFile.isConfirmed()){
					MInvoice invoice = ComercialUtils.getInvoiceByDocPartner(getCtx(), stechLoadInvFile.getC_DocTypeInvoice_ID(),
							stechLoadInvFile.getDocumentSerie(), stechLoadInvFile.getDocumentNoRef(), stechLoadInvFile.getC_BPartner_ID(), get_TrxName());
					if ((invoice != null) && (invoice.get_ID() > 0)){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Ya existe un comprobante en el sistema para ese Socio de Negocio - Documento");
					}
				}

				// Moneda
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

				// Fecha comprobante
				if ((stechLoadInvFile.getFechaCadena() == null) || (stechLoadInvFile.getFechaCadena().trim().equalsIgnoreCase(""))){
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Debe indicar Fecha de Comprobante");
				}
				else{
					Timestamp fecDoc = DateUtils.convertStringToTimestamp_ddMMyyyy(stechLoadInvFile.getFechaCadena());
					if (fecDoc == null){
						stechLoadInvFile.setIsConfirmed(false);
						stechLoadInvFile.setErrorMsg("Formato de Fecha de Emisión inválido : " + stechLoadInvFile.getFechaCadena());
					}
					stechLoadInvFile.setDateTrx(fecDoc);
				}

				// Montos
				if (stechLoadInvFile.getAmtRounding() == null) stechLoadInvFile.setAmtRounding(Env.ZERO);

				if ((stechLoadInvFile.getTotalAmt() == null) || (stechLoadInvFile.getTotalAmt().compareTo(Env.ZERO) <= 0)){
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Debe indicar Total del Comprobante");
				}

				if ((stechLoadInvFile.getAmtSubtotal() == null) || (stechLoadInvFile.getAmtSubtotal().compareTo(Env.ZERO) <= 0)){
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Debe indicar Subtotal del Comprobante");
				}

				if ((stechLoadInvFile.getLineTotalAmt() == null) || (stechLoadInvFile.getLineTotalAmt().compareTo(Env.ZERO) <= 0)){
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Debe indicar Total Lineas con Impuesto");
				}

				if ((stechLoadInvFile.getTaxAmt() == null) || (stechLoadInvFile.getTaxAmt().compareTo(Env.ZERO) <= 0)){
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Debe indicar Monto Impuesto");
				}

				if ((stechLoadInvFile.getLineNetAmt() == null) || (stechLoadInvFile.getLineNetAmt().compareTo(Env.ZERO) <= 0)){
					stechLoadInvFile.setIsConfirmed(false);
					stechLoadInvFile.setErrorMsg("Debe indicar Total Lineas sin Impuesto");
				}

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
}