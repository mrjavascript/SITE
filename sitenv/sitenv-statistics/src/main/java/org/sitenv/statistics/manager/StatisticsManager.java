package org.sitenv.statistics.manager;

public interface StatisticsManager {

	public void addCcdaValidation(Boolean hasErrors, Boolean hasWarnings, Boolean hasInfo, Boolean hasHttpError);
	public void addSmartCcdaValidation(Boolean hasHttpError);
	public void addCcdaDownload();
	
	
	public Long getSuccessfulCcdaValidationCount(Integer numOfDays);
	public Long getFailedCcdaValidationCount(Integer numOfDays);
	public Long getHttpErrorCcdaValidationCount(Integer numOfDays);
	public Long getWarningCcdaValidationCount(Integer numOfDays);
	public Long getInfoCcdaValidationCount(Integer numOfDays);
	public Long getTotalCcdaValidationCount(Integer numOfDays);
	
	public Long getSmartCcdaCount(Integer numOfDays);
	public Long getSmartCcdaWithHttpErrorCount(Integer numOfDays);
	public Long getCcdaDownloadCount(Integer numOfDays);
	
}
