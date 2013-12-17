package org.sitenv.portlets.qrda.models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author etang
 * 
 */

@XmlRootElement(name = "QRDAValidationResponse")
public class QRDAValidationResponse implements Serializable {
	private int returnCode;
	private boolean success;
	private String validationResult;
	private String errorMessage;
	private String[] validationResults;
	private String orgXml;
	private String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrgXml() {
		return orgXml;
	}

	public void setOrgXml(String orgXml) {
		this.orgXml = orgXml;
	}

	public QRDAValidationResponse() {
		// validationResults = new JsonArray();
	}

	public String[] getValidationResults() {
		return validationResults;
	}

	public void parse() {
		validationResults = this.validationResult.split("\\r?\\n");
	}

	public void setValidationResult(String validationResult) {
		this.validationResult = validationResult;
		/*
		 * String[] strs = validationResult.split("\\r?\\n"); for (String str :
		 * strs) { this.validationResults.add(new JsonPrimitive(str)); }
		 */
	}

	public int getReturnCode() {
		return returnCode;
	}

	public String getValidationResult() {
		return validationResult;
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

	public void setSuccess(boolean isSuccess) {
		this.success = isSuccess;
	}
}
