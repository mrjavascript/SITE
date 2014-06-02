package org.sitenv.statistics.manager;

import java.util.List;

import org.sitenv.statistics.dto.PdtiTestCase;

public interface StatisticsManager {

	public static final Integer QRDA_CATEGORY_I = new Integer(1);
	public static final Integer QRDA_CATEGORY_III = new Integer(3);
	
	public void addCcdaValidation(String testType, Boolean hasErrors, Boolean hasWarnings, Boolean hasInfo, Boolean hasHttpError);
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
	
	
	
	
	public void addQrdaValidation(Integer category, Boolean hasSchemaErrors, Boolean hasSchematronErrors, Boolean hasSchematronWarnings, Boolean hasHttpError);
	public Long getSuccessfulQrdaValidationCount(Integer category, Integer numOfDays);
	public Long getFailedQrdaValidationCount(Integer category, Integer numOfDays);
	public Long getWarningQrdaValidationCount(Integer category, Integer numOfDays);
	public Long getTotalQrdaValidationCount(Integer category, Integer numOfDays);
	public Long getHttpErrorQrdaValidationCount(Integer category, Integer numOfDays);
	
	
	
	public void addDirectReceive(Boolean uploaded, Boolean precanned, Boolean hasErrors);
	public void addDirectTrustUpload(Boolean hasErrors);
	public Long getSuccessfulDirectReceiveCount(Integer numOfDays);
	public Long getFailedDirectReceiveCount(Integer numOfDays);
	public Long getSuccessfulPrecannedDirectReceiveCount(Integer numOfDays);
	public Long getSuccessfulUploadedDirectReceiveCount(Integer numOfDays);
	public Long getSuccessfulTrustAnchorUploadCount(Integer numOfDays);
	public Long getFailedTrustAnchorUploadCount(Integer numOfDays);
	
	
	public void addPdtiTest(List<PdtiTestCase> testCases);
	public Long getSuccessfulPdtiTestCount(Integer numOfDays);
	public Long getFailedPdtiTestCount(Integer numOfDays);
	public Long getTotalPdtiTestCount(Integer numOfDays);
	public Long getHttpErrorPdtiTestCount(Integer numOfDays);
	
	
	public Long getJiraIssuesCreatedCount(Integer numOfDays);
	public Long getJiraIssuesResolvedCount(Integer numOfDays);
	
	public Long getGoogleAnalyticsSessionCount(Integer numOfDays);
	public Long getGoogleAnalyticsPageViewCount(Integer numOfDays);
	
}
