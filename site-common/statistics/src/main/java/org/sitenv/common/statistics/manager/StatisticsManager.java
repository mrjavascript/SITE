package org.sitenv.common.statistics.manager;

import java.util.List;

import org.sitenv.common.statistics.dto.AggregateWeeklyCounts;
import org.sitenv.common.statistics.dto.CcdaWeeklyCounts;
import org.sitenv.common.statistics.dto.DirectWeeklyCounts;
import org.sitenv.common.statistics.dto.GoogleAnalyticsData;
import org.sitenv.common.statistics.dto.PdtiTestCase;
import org.sitenv.common.statistics.dto.PdtiWeeklyCounts;
import org.sitenv.common.statistics.dto.QrdaWeeklyCounts;

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
	
	public List<CcdaWeeklyCounts> getCcdaWeeklyCounts(Integer numOfWeeks);
	public List<QrdaWeeklyCounts> getQrdaWeeklyCounts(Integer numOfWeeks);
	public List<PdtiWeeklyCounts> getPdtiWeeklyCounts(Integer numOfWeeks);
	public List<DirectWeeklyCounts> getDirectWeeklyCounts(Integer numOfWeeks, Boolean send);
	
	
	public void addQrdaValidation(Integer category, Boolean hasSchemaErrors, Boolean hasSchematronErrors, Boolean hasSchematronWarnings, Boolean hasHttpError);
	public Long getSuccessfulQrdaValidationCount(Integer category, Integer numOfDays);
	public Long getFailedQrdaValidationCount(Integer category, Integer numOfDays);
	public Long getWarningQrdaValidationCount(Integer category, Integer numOfDays);
	public Long getTotalQrdaValidationCount(Integer category, Integer numOfDays);
	public Long getHttpErrorQrdaValidationCount(Integer category, Integer numOfDays);
	
	
	
	public void addDirectReceive(String domain, Boolean uploaded, Boolean precanned, Boolean hasErrors);
	public void addDirectTrustUpload(Boolean hasErrors);
	public Long getSuccessfulDirectReceiveCount(Integer numOfDays);
	public Long getFailedDirectReceiveCount(Integer numOfDays);
	public Long getSuccessfulPrecannedDirectReceiveCount(Integer numOfDays);
	public Long getSuccessfulUploadedDirectReceiveCount(Integer numOfDays);
	public Long getSuccessfulTrustAnchorUploadCount(Integer numOfDays);
	public Long getFailedTrustAnchorUploadCount(Integer numOfDays);
	
	
	public void addPdtiTest(String endpointUrl, List<PdtiTestCase> testCases);
	public Long getSuccessfulPdtiTestCount(Integer numOfDays);
	public Long getFailedPdtiTestCount(Integer numOfDays);
	public Long getTotalPdtiTestCount(Integer numOfDays);
	public Long getHttpErrorPdtiTestCount(Integer numOfDays);
	

	public void addDcdtHostingVerification(String testcase, String directAddress, String response);
	
	public List<AggregateWeeklyCounts> getAggregateWeeklyCounts(Integer numOfWeeks);
	
	public Long getJiraIssuesCreatedCount(Integer numOfDays);
	public Long getJiraIssuesResolvedCount(Integer numOfDays);
	
	public GoogleAnalyticsData getGoogleAnalyticsData(String p12CertPath);
	
}
