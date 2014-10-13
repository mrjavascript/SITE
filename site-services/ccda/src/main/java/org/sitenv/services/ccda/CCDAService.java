package org.sitenv.services.ccda;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.sitenv.common.statistics.manager.StatisticsManager;


public interface CCDAService {
	
	public abstract String getValidatorID();
    public abstract String Validate(MultipartBody body);
    
}
