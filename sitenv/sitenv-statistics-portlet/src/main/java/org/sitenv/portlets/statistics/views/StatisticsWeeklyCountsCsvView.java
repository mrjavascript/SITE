package org.sitenv.portlets.statistics.views;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.sitenv.statistics.dto.StatisticsCounts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

@Component("statisticsWeeklyCountsCsvView")
public class StatisticsWeeklyCountsCsvView extends AbstractView {

private Logger logger = Logger.getLogger(StatisticsWeeklyCountsCsvView.class);
	
	public StatisticsWeeklyCountsCsvView() {
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
    sbuilder.append(map.get("headerLine"));
    
    if (map.get("weeklyCounts") != null)
    {
    	for (StatisticsCounts count : (List<StatisticsCounts>) map.get("weeklyCounts")) {
    		sbuilder.append(count.getStartDate() + "," + count.getSuccessCount() + "," + count.getFailedCount() + "\n");
    	}
    }
    
    response.getWriter().write(sbuilder.toString());
    response.getWriter().flush();
    }
	
}
