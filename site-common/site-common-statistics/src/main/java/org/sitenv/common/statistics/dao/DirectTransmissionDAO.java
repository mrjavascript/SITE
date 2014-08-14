package org.sitenv.common.statistics.dao;

import java.util.List;

import org.sitenv.common.statistics.dto.DirectWeeklyCounts;

public interface DirectTransmissionDAO {

	public void createDirectReceive(String domain, Boolean precanned, Boolean uploaded, Boolean hasErrors);
	
	public void createDirectTrustUpload(Boolean hasErrors);
	
	
	public Long getDirectReceivePrecannedCount(Boolean precanned, Boolean hasErrors, Integer numOfDays);
	
	public Long getDirectReceiveUploadCount(Boolean upload, Boolean hasErrors, Integer numOfDays);
	
	public Long getDirectReceiveCount(Boolean hasErrors, Integer numberOfDays);
	

	public Long getDirectTrustUploadCount(Boolean hasErrors, Integer numberOfDays);
	
	
	public List<DirectWeeklyCounts> getDirectWeeklyCounts(Integer numOfWeeks, Boolean send);
}
