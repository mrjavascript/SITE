package org.sitenv.portlets.statistics.views;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.sitenv.statistics.dto.QrdaWeeklyCounts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

@Component("qrdaWeeklyCountsCsvView")
public class QrdaWeeklyCountsCsvView extends AbstractView {

private Logger logger = Logger.getLogger(QrdaWeeklyCountsCsvView.class);
	
	public QrdaWeeklyCountsCsvView() {
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
    sbuilder.append("Week,Total Number of Category 1 Validations,Total Number of Category 3 Validations\n");
    
    if (map.get("weeklyCounts") != null)
    {
    	for (QrdaWeeklyCounts count : (List<QrdaWeeklyCounts>) map.get("weeklyCounts")) {
    		sbuilder.append(count.getStartDate() + "," + count.getCategory1Count() + "," + count.getCategory3Count() + "\n");
    	}
    }
    
   
    response.getWriter().write(sbuilder.toString());
    response.getWriter().flush();
    }
	
}
