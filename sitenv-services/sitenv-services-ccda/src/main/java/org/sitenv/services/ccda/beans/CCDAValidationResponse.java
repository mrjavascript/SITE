package org.sitenv.services.ccda.beans;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cwatson
 *
 */

@XmlRootElement(name = "CCDAValidationResponse")
public class CCDAValidationResponse {
	private int returnCode;
	private boolean success;
	private String validationResult;
	private String errorMessage;
	private String note;
	
	public CCDAValidationResponse()
	{}
	

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
