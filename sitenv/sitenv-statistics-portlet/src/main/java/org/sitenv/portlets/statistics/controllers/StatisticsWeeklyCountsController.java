package org.sitenv.portlets.statistics.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;

import org.sitenv.statistics.constants.StatisticsConstants;
import org.sitenv.statistics.dto.CcdaWeeklyCounts;
import org.sitenv.statistics.dto.DirectWeeklyCounts;
import org.sitenv.statistics.dto.PdtiWeeklyCounts;
import org.sitenv.statistics.dto.QrdaWeeklyCounts;
import org.sitenv.statistics.manager.StatisticsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

@Controller
@RequestMapping("VIEW")
public class StatisticsWeeklyCountsController {
	
	@Autowired
	private StatisticsManager statisticsManager = null;
	

	@ActionMapping(params = "javax.portlet.action=ccdaWeeklyCounts")
	public void ccdaWeeklyHandler(ActionRequest request, ActionResponse response)
			throws IOException {
		
		response.setRenderParameter("javax.portlet.action", "ccdaWeeklyCounts");

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(params = "javax.portlet.action=ccdaWeeklyCounts")
	public ModelAndView processCcdaWeekly(RenderRequest request, Model model)
			throws IOException {
		List<CcdaWeeklyCounts> weeklyCounts = null;
		
		weeklyCounts = statisticsManager.getCcdaWeeklyCounts(StatisticsConstants.SMALL_WEEKLY_STATISTICS_LIMIT);
		
		Map map = new HashMap();
		
		
		map.put("weeklyCounts", weeklyCounts);

		return new ModelAndView("ccdaWeeklyCountsCsvView", map);
	}
	
	
	@ActionMapping(params = "javax.portlet.action=pdtiWeeklyCounts")
	public void pdtiWeeklyHandler(ActionRequest request, ActionResponse response)
			throws IOException {
		
		response.setRenderParameter("javax.portlet.action", "pdtiWeeklyCounts");

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(params = "javax.portlet.action=pdtiWeeklyCounts")
	public ModelAndView processPdtiWeekly(RenderRequest request, Model model)
			throws IOException {
		List<PdtiWeeklyCounts> weeklyCounts = null;
		
		weeklyCounts = statisticsManager.getPdtiWeeklyCounts(StatisticsConstants.SMALL_WEEKLY_STATISTICS_LIMIT);
		
		Map map = new HashMap();
		
		
		map.put("weeklyCounts", weeklyCounts);

		return new ModelAndView("pdtiWeeklyCountsCsvView", map);
	}
	
	@ActionMapping(params = "javax.portlet.action=directReceiveWeeklyCounts")
	public void directReceiveWeeklyHandler(ActionRequest request, ActionResponse response)
			throws IOException {
		
		response.setRenderParameter("javax.portlet.action", "directReceiveWeeklyCounts");

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(params = "javax.portlet.action=directReceiveWeeklyCounts")
	public ModelAndView processDirectReceiveWeekly(RenderRequest request, Model model)
			throws IOException {
		List<DirectWeeklyCounts> weeklyCounts = null;
		
		weeklyCounts = statisticsManager.getDirectReceiveWeeklyCounts(StatisticsConstants.SMALL_WEEKLY_STATISTICS_LIMIT);
		
		Map map = new HashMap();
		
		
		map.put("weeklyCounts", weeklyCounts);

		return new ModelAndView("directWeeklyCountsCsvView", map);
	}
	
	@ActionMapping(params = "javax.portlet.action=qrdaWeeklyCounts")
	public void qrdaWeeklyHandler(ActionRequest request, ActionResponse response)
			throws IOException {
		
		response.setRenderParameter("javax.portlet.action", "qrdaWeeklyCounts");

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(params = "javax.portlet.action=qrdaWeeklyCounts")
	public ModelAndView processQrdaWeekly(RenderRequest request, Model model)
			throws IOException {
		List<QrdaWeeklyCounts> weeklyCounts = null;
		
		weeklyCounts = statisticsManager.getQrdaWeeklyCounts(StatisticsConstants.SMALL_WEEKLY_STATISTICS_LIMIT);
		
		Map map = new HashMap();
		
		
		map.put("weeklyCounts", weeklyCounts);

		return new ModelAndView("qrdaWeeklyCountsCsvView", map);
	}
	
}
