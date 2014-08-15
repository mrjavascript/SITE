package org.sitenv.portlets.directtransport.controllers;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.sitenv.common.utilities.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller

@RequestMapping("VIEW")
public class DirectTransportViewController extends BaseController
{
	@RenderMapping()
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("view");

		return modelAndView;
	}
}
