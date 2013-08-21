package sit.portal.directtransport.services.model;

public class GenericResult {
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
