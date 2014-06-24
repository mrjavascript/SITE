package org.sitenv.statistics.dao;

import java.util.List;

import org.sitenv.statistics.dto.PdtiTestCase;
import org.sitenv.statistics.dto.PdtiWeeklyCounts;

public interface PdtiTestDAO {

	public void createPdtiTest(String endpointUrl, List<PdtiTestCase> testCases);
	
	
	public Long getPdtiTestCaseCount(String testcaseName, Boolean pass, Integer numOfDays);
	
	public Long getHttpErrorCount(Boolean hasHttpError, Integer numOfDays);
	
	public List<PdtiWeeklyCounts> getTestCasesWeeklyCounts(Integer numOfWeeks);
}
