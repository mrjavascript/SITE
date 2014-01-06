package org.sitenv.portlets.directtransport.models;

import java.io.Serializable;

public class GenericResult implements Serializable {
	private Boolean IsSuccess;
	private String ErrorMessage;
	public Boolean getIsSuccess() {
		return IsSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		IsSuccess = isSuccess;
	}
	public String getErrorMessage() {
		return ErrorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}
	
}
