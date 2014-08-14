package org.sitenv.portlets.uploadportlet.views;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

@Component("smartCCDAValidatorJsonView")
public class SmartCCDAValidatorJsonView extends AbstractView {
	
	private Logger logger = Logger.getLogger(SmartCCDAValidatorJsonView.class);
	
	public SmartCCDAValidatorJsonView() {
		super();
		
		setContentType("text/plain");
	}
	
	@Override
    protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    logger.info("Resolving ajax request view - " + map);
    
    Object smartCcdaResponse = null, smartCcdaRubricResponse = null;
    
    smartCcdaResponse = map.get("smartCcdaResponse");
    smartCcdaRubricResponse = map.get("smartCcdaRubricResponse");
    
    String returnJson = null;
    if (smartCcdaResponse != null && smartCcdaRubricResponse != null)
    {
    	String returnJsonTemplate = "{\"IsSuccess\":true,\"RubricLookup\":{RUBRICLOOKUP},\"Results\":{RESPONSE}}";
		returnJson = returnJsonTemplate.replace("{RUBRICLOOKUP}",smartCcdaRubricResponse.toString()).replace("{RESPONSE}", smartCcdaResponse.toString());
    }
    else
    {
    	returnJson = "{\"IsSuccess\":false, \"Message\":\"Relay failed.\"}";
    }
    

    response.getWriter().write(returnJson);
    response.getWriter().flush();
    }

}