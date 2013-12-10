package org.sitenv.portlets.qrda.models;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author etang
 * 
 */

@XmlRootElement(name = "QRDAValidationResponse")
public class QRDAValidationResponse {
	private int returnCode;
	private boolean isSuccess;
	private String validationResult;
	private String errorMessage;

	public String[] getResults() {
		return validationResult.split("\\r?\\n");
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

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
