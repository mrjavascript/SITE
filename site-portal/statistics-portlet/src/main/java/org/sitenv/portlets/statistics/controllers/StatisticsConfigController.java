package org.sitenv.portlets.statistics.controllers;

import java.io.IOException;

import javax.portlet.ActionResponse;
import javax.portlet.PortletModeException;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.sitenv.common.utilities.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;


@Controller("statisticsConfigController")
@RequestMapping("EDIT")
public class StatisticsConfigController extends BaseController {

	@RenderMapping
	public String handleRenderRequest(Model model, PortletPreferences prefs) {

		model.addAttribute("viewPage", prefs.getValue("viewPage", "all-stats"));

		return "edit";
	}
	
	@ActionMapping(params = "action=save")
	public void action(@RequestParam("viewPage") String viewPage,   PortletPreferences prefs,  ActionResponse response) throws ReadOnlyException, ValidatorException, IOException, PortletModeException
	{
		if (viewPage != null && !viewPage.equals(""))
		{
			prefs.reset("viewPage");
			prefs.setValue("viewPage", viewPage);
			}
		prefs.store();
	}
}
