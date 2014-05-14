package org.sitenv.statistics.manager.impl;

import java.util.List;

import org.sitenv.statistics.dao.CcdaValidationDAO;
import org.sitenv.statistics.dao.DirectTransmissionDAO;
import org.sitenv.statistics.dao.PdtiTestDAO;
import org.sitenv.statistics.dao.QrdaValidationDAO;
import org.sitenv.statistics.dto.PdtiTestCase;
import org.sitenv.statistics.manager.StatisticsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatisticsManagerImpl implements StatisticsManager {

	@Autowired
	private CcdaValidationDAO ccdaValidationDAO;
	
	@Autowired
	private QrdaValidationDAO qrdaValidationDAO;
	
	@Autowired
	private DirectTransmissionDAO directTransmissionDAO;
	
	@Autowired
	private PdtiTestDAO pdtiTestDAO;
	
	@Transactional
	public void addCcdaValidation(Boolean hasErrors, Boolean hasWarnings,
			Boolean hasInfo, Boolean hasHttpError) {
		
		ccdaValidationDAO.createCcdaValidation(hasErrors, hasWarnings, hasInfo, hasHttpError);
		
	}

	@Transactional
	public Long getSuccessfulCcdaValidationCount(Integer numOfDays) {
		
		return ccdaValidationDAO.getErrorCount(false, numOfDays);
	}

	@Transactional
	public Long getFailedCcdaValidationCount(Integer numOfDays) {
		return ccdaValidationDAO.getErrorCount(true, numOfDays);
	}

	@Transactional
	public Long getWarningCcdaValidationCount(Integer numOfDays) {
		return ccdaValidationDAO.getWarningCount(true, numOfDays);
	}
	
	@Transactional
	public Long getHttpErrorCcdaValidationCount(Integer numOfDays) {
		return ccdaValidationDAO.getHttpErrorCount(true, numOfDays);
	}
	
	@Transactional
	public Long getInfoCcdaValidationCount(Integer numOfDays) {
		return ccdaValidationDAO.getInfoCount(true, numOfDays);
	}

	@Transactional
	public Long getTotalCcdaValidationCount(Integer numOfDays) {
		return ccdaValidationDAO.getTotalCount(numOfDays);
	}

	
	@Transactional
	public void addSmartCcdaValidation(Boolean hasHttpError) {
		this.ccdaValidationDAO.createSmartCcdaValidation(hasHttpError);
	}

	@Transactional
	public void addCcdaDownload() {
		this.ccdaValidationDAO.createCcdaDownload();
	}

	@Transactional
	public Long getSmartCcdaCount(Integer numOfDays) {
		return this.ccdaValidationDAO.getSmartCcdaCount(false, numOfDays);
	}

	@Transactional
	public Long getSmartCcdaWithHttpErrorCount(Integer numOfDays) {
		return this.ccdaValidationDAO.getSmartCcdaCount(true, numOfDays);
	}

	@Transactional
	public Long getCcdaDownloadCount(Integer numOfDays) {
		return this.ccdaValidationDAO.getCcdaDownloadCount(numOfDays);
	}
	
	
	
	@Transactional
	public void addQrdaValidation(Integer category, Boolean hasSchemaErrors, Boolean hasSchematronErrors, Boolean hasSchematronWarnings, Boolean hasHttpError){
	
		qrdaValidationDAO.createQrdaValidation(category, hasSchemaErrors, hasSchematronErrors, hasSchematronWarnings, hasHttpError);
		
	}

	@Transactional
	public Long getSuccessfulQrdaValidationCount(Integer category,
			Integer numOfDays) {
		
		return qrdaValidationDAO.getTotalNonErrorCount(category, numOfDays);
		
	}

	@Transactional
	public Long getFailedQrdaValidationCount(Integer category, Integer numOfDays) {

		return qrdaValidationDAO.getTotalErrorCount(category, numOfDays);
		
	}
	
	
	@Transactional
	public Long getWarningQrdaValidationCount(Integer category,
			Integer numOfDays) {
		
		return qrdaValidationDAO.getSchematronWarningCount(category, true, numOfDays);
		
	}

	@Transactional
	public Long getTotalQrdaValidationCount(Integer category, Integer numOfDays) {
		
		return qrdaValidationDAO.getTotalCount(category, numOfDays);
		
	}
	
	@Transactional
	public Long getHttpErrorQrdaValidationCount(Integer category,
			Integer numOfDays) {
		
		return qrdaValidationDAO.getHttpErrorCount(category, true, numOfDays);
		
	}
	
	
	@Transactional
	public void addDirectReceive(Boolean uploaded, Boolean precanned,
			Boolean hasErrors) {
		directTransmissionDAO.createDirectReceive(precanned, uploaded, hasErrors);
		
	}
	
	@Transactional
	public void addDirectTrustUpload(Boolean hasErrors) {
		directTransmissionDAO.createDirectTrustUpload(hasErrors);
	}

	@Transactional
	public Long getSuccessfulDirectReceiveCount(Integer numOfDays) {
		
		return directTransmissionDAO.getDirectReceiveCount(false, numOfDays);
		
	}

	@Transactional
	public Long getFailedDirectReceiveCount(Integer numOfDays) {
		return directTransmissionDAO.getDirectReceiveCount(true, numOfDays);
	}

	@Transactional
	public Long getSuccessfulPrecannedDirectReceiveCount(Integer numOfDays) {
		return directTransmissionDAO.getDirectReceivePrecannedCount(true, false, numOfDays);
	}

	@Transactional
	public Long getSuccessfulUploadedDirectReceiveCount(Integer numOfDays) {
		return directTransmissionDAO.getDirectReceiveUploadCount(true, false, numOfDays);
	}

	@Transactional
	public Long getSuccessfulTrustAnchorUploadCount(Integer numOfDays) {
		return directTransmissionDAO.getDirectTrustUploadCount(false, numOfDays);
		
	}

	@Transactional
	public Long getFailedTrustAnchorUploadCount(Integer numOfDays) {
		return directTransmissionDAO.getDirectTrustUploadCount(true, numOfDays);
		
	}
	
	
	
	
	
	@Transactional
	public void addPdtiTest(List<PdtiTestCase> testCases) {
		pdtiTestDAO.createPdtiTest(testCases);
		
	}

	@Transactional
	public Long getSuccessfulPdtiTestCount(Integer numOfDays) {
		return pdtiTestDAO.getPdtiTestCaseCount(null, true, numOfDays);
	}

	@Transactional
	public Long getFailedPdtiTestCount(Integer numOfDays) {
		return pdtiTestDAO.getPdtiTestCaseCount(null, false, numOfDays);
	}

	@Transactional
	public Long getTotalPdtiTestCount(Integer numOfDays) {
		return pdtiTestDAO.getPdtiTestCaseCount(null, null, numOfDays);
	}

	@Transactional
	public Long getHttpErrorPdtiTestCount(Integer numOfDays) {
		// TODO Auto-generated method stub
		return null;
	}

	public CcdaValidationDAO getCcdaValidationDAO() {
		return ccdaValidationDAO;
	}

	public void setCcdaValidationDAO(CcdaValidationDAO ccdaValidationDAO) {
		this.ccdaValidationDAO = ccdaValidationDAO;
	}

	public QrdaValidationDAO getQrdaValidationDAO() {
		return qrdaValidationDAO;
	}

	public void setQrdaValidationDAO(QrdaValidationDAO qrdaValidationDAO) {
		this.qrdaValidationDAO = qrdaValidationDAO;
	}

	public DirectTransmissionDAO getDirectTransmissionDAO() {
		return directTransmissionDAO;
	}

	public void setDirectTransmissionDAO(DirectTransmissionDAO directTransmissionDAO) {
		this.directTransmissionDAO = directTransmissionDAO;
	}

	public PdtiTestDAO getPdtiTestDAO() {
		return pdtiTestDAO;
	}

	public void setPdtiTestDAO(PdtiTestDAO pdtiTestDAO) {
		this.pdtiTestDAO = pdtiTestDAO;
	}

	

	

	

	
	
	
	

}
