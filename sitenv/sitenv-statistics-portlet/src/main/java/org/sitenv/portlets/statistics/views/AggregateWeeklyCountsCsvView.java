package org.sitenv.portlets.statistics.views;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.sitenv.statistics.dto.AggregateWeeklyCounts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

@Component("aggregateWeeklyCountsCsvView")
public class AggregateWeeklyCountsCsvView extends AbstractView {

private Logger logger = Logger.getLogger(AggregateWeeklyCountsCsvView.class);
	
	public AggregateWeeklyCountsCsvView() {
		super();
		
		setContentType("text/plain");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    logger.info("Resolving ajax request view - " + map);
    
   
    logger.info("content Type = " + getContentType());
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    
    StringBuilder sbuilder = new StringBuilder();
    sbuilder.append("Week,Total Number of C-CDA Validations, Total Number of QRDA Validations, Total Number of Direct Messages Sent, Total Number of Direct Messages Received, Total Number of PD Requests\n");
    
    if (map.get("weeklyCounts") != null)
    {
    	for (AggregateWeeklyCounts count : (List<AggregateWeeklyCounts>) map.get("weeklyCounts")) {
    		sbuilder.append(count.getStartDate() + "," + count.getCcdaCount() + "," + count.getQrdaCount() + "," + count.getDirectReceiveCount() + "," + count.getDirectSentCount() + "," + count.getPdtiCount() + "\n");
    	}
    }
    
    response.getWriter().write(sbuilder.toString());
    response.getWriter().flush();
    }
	
}
