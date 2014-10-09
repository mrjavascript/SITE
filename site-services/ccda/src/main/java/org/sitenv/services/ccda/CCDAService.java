package org.sitenv.services.ccda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.sitenv.common.statistics.manager.StatisticsManager;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class CCDAService {
	
	public static final String DEFAULT_PROPERTIES_FILE = "environment.properties";
	
	protected Properties props;
	
	@Autowired
	private StatisticsManager statisticsManager;
	
	public CCDAService() throws IOException {
        loadProperties();
    }

	protected void loadProperties() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE);
		
		if (in == null)
		{
			props = null;
			throw new FileNotFoundException("Environment Properties File not found in class path.");
		}
		else
		{
			props = new Properties();
			props.load(in);
		}
	}
	
	protected StatisticsManager getStatisticsManager() {
		return statisticsManager;
	}

	protected void setStatisticsManager(StatisticsManager statisticsManager) {
		this.statisticsManager = statisticsManager;
	}
    
	public void recordStatistics(String testType, Boolean hasErrors, Boolean hasWarnings, Boolean hasInfo, Boolean hasHttpError){
		getStatisticsManager().addCcdaServiceCall(testType, hasErrors, hasWarnings, hasInfo, hasHttpError, getValidatorID());
	}
	
	public abstract String getValidatorID();
    public abstract String Validate(MultipartBody body);
    
    
}
