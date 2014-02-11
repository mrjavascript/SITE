package org.sitenv.portlets.qrda.models;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author etang
 * 
 */

@XmlRootElement(name = "QRDAValidationResponse")
public class QRDAValidationResponse {

	private int returnCode;
	private boolean success;
	private String validationResult;
	private String errorMessage;
	private String note;
	private ArrayList<QRDAValidationEnhancedResult> schematronWarnings;
	private ArrayList<QRDAValidationEnhancedResult> schematronErrors;
	private ArrayList<QRDASchemaError> schemaErrors;
	private String orgXml;
	private String orgFileName;
	private String selectedCategory;

	public String getOrgFileName() {
		return orgFileName;
	}

	public void setOrgFileName(String orgFileName) {
		this.orgFileName = orgFileName;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public String getOrgXml() {
		return orgXml;
	}

	public void setOrgXml(String orgXml) {
		this.orgXml = orgXml;
	}

	public QRDAValidationResponse() {
		schematronWarnings = new ArrayList<QRDAValidationEnhancedResult>();
		schematronErrors = new ArrayList<QRDAValidationEnhancedResult>();
		schemaErrors = new ArrayList<QRDASchemaError>();
	}

	public ArrayList<QRDAValidationEnhancedResult> getSchematronWarnings() {
		return schematronWarnings;
	}

	public void setSchematronWarnings(
			ArrayList<QRDAValidationEnhancedResult> schematronWarnings) {
		this.schematronWarnings = schematronWarnings;
	}

	public ArrayList<QRDAValidationEnhancedResult> getSchematronErrors() {
		return schematronErrors;
	}

	public void setSchematronErrors(
			ArrayList<QRDAValidationEnhancedResult> schematronErrors) {
		this.schematronErrors = schematronErrors;
	}

	public ArrayList<QRDASchemaError> getSchemaErrors() {
		return schemaErrors;
	}

	public void setSchemaErrors(ArrayList<QRDASchemaError> schemaErrors) {
		this.schemaErrors = schemaErrors;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public String getValidationResult() {
		return validationResult;
	}

	public void setValidationResult(String validationResult) {
		this.validationResult = validationResult;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
