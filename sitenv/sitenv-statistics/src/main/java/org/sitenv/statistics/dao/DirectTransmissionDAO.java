package org.sitenv.statistics.dao;

public interface DirectTransmissionDAO {

	public void createDirectReceive(Boolean precanned, Boolean uploaded, Boolean hasErrors);
	
	public void createDirectTrustUpload(Boolean hasErrors);
	
	
	public Long getDirectReceivePrecannedCount(Boolean precanned, Boolean hasErrors, Integer numOfDays);
	
	public Long getDirectReceiveUploadCount(Boolean upload, Boolean hasErrors, Integer numOfDays);
	
	public Long getDirectReceiveCount(Boolean hasErrors, Integer numberOfDays);
	

	public Long getDirectTrustUploadCount(Boolean hasErrors, Integer numberOfDays);
	
}
