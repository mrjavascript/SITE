package org.sitenv.statistics.manager.impl;

import org.sitenv.statistics.dao.CcdaValidationDAO;
import org.sitenv.statistics.manager.StatisticsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatisticsManagerImpl implements StatisticsManager {

	@Autowired
	private CcdaValidationDAO ccdaValidationDAO;
	
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
	
	
	
	public CcdaValidationDAO getCcdaValidationDAO() {
		return ccdaValidationDAO;
	}

	public void setCcdaValidationDAO(CcdaValidationDAO ccdaValidationDAO) {
		this.ccdaValidationDAO = ccdaValidationDAO;
	}

	
	
	
	

}
