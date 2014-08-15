package org.sitenv.portlets.xdrvalidator.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.sitenv.common.utilities.controller.BaseController;
import org.sitenv.common.statistics.manager.StatisticsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

@Controller(value = "xdr")
@RequestMapping("VIEW")
public class XdrValidationController extends BaseController {

	private JSONArray fileJson;
	private JSONObject jsonResponseBody;
	
	@Autowired
	private StatisticsManager statisticsManager;

	@ActionMapping(params = "javax.portlet.action=uploadXDR")
	public void response(MultipartActionRequest request, ActionResponse response) throws IOException {
		
		
		if (this.props == null)
		{
			this.loadProperties();
		}
		
		// handle the files:
		
		response.setRenderParameter("javax.portlet.action", "uploadXDR");
		MultipartFile file = request.getFile("file");

		fileJson = new JSONArray();
		
		jsonResponseBody = new JSONObject();
		
		try {

				JSONObject jsono = new JSONObject();
				jsono.put("name", file.getOriginalFilename());
				jsono.put("size", file.getSize());
				
				fileJson.put(jsono);
				
				// handle the data
				jsonResponseBody.put("IsSuccess", "true");
				jsonResponseBody.put("ErrorMessage", "Message Sent Successfully!");
				
				
				
				

		} catch (Exception e) {
			//statisticsManager.addCcdaValidation(ccda_type_value, false, false, false, true);
			
			throw new RuntimeException(e);
		} 
		
	}

	@RequestMapping(params = "javax.portlet.action=uploadXDR")
	public ModelAndView process(RenderRequest request, Model model)
			throws IOException {
		Map map = new HashMap();

		map.put("files", fileJson);
		map.put("result", jsonResponseBody);
		
		
		return new ModelAndView("xdrValidatorJsonView", map);
	}

	@RenderMapping()
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("view");

		return modelAndView;
	}
	
}
