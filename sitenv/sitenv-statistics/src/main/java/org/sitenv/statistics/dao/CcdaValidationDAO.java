package org.sitenv.statistics.dao;

import java.util.List;

import org.sitenv.statistics.dto.CcdaWeeklyCounts;

public interface CcdaValidationDAO {
	
	public void createCcdaValidation(String testType, Boolean hasErrors, Boolean hasWarnings, Boolean hasInfo, Boolean hasHttpError);
	
	public void createSmartCcdaValidation(Boolean hasHttpError);
	
	public void createCcdaDownload();
	
	
	public Long getErrorCount(Boolean hasErrors, Integer numOfDays);
	
	public Long getWarningCount(Boolean hasWarnings, Integer numOfDays);
	
	public Long getInfoCount(Boolean hasInfo, Integer numOfDays);
	
	public Long getTotalCount(Integer numOfDays);
	
	public Long getHttpErrorCount(Boolean hasHttpError, Integer numOfDays);
	
	
	public Long getCcdaDownloadCount(Integer numOfDays);
	
	public Long getSmartCcdaCount(Boolean hasHttpError, Integer numOfDays);
	
	public List<CcdaWeeklyCounts> getCcdaWeeklyCounts(Integer numOfWeeks);
}
