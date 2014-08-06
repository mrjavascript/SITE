package org.sitenv.portlets.uploadportlet.controllers;

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
import org.sitenv.statistics.manager.StatisticsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

@Controller
@RequestMapping("VIEW")
public class CCDAValidatorController extends BaseController {

	private JSONArray fileJson;
	private JSONObject JSONResponseBody;
	
	@Autowired
	private StatisticsManager statisticsManager;

	@ActionMapping(params = "javax.portlet.action=uploadCCDA")
	public void response(MultipartActionRequest request, ActionResponse response) throws IOException {
		
		String ccda_type_value = null;
		
		if (this.props == null)
		{
			this.loadProperties();
		}
		
		// handle the files:
		
		response.setRenderParameter("javax.portlet.action", "uploadCCDA");
		MultipartFile file = request.getFile("file");
		
		fileJson = new JSONArray();
		
		
		try {

				JSONObject jsono = new JSONObject();
				jsono.put("name", file.getOriginalFilename());
				jsono.put("size", file.getSize());
				
				fileJson.put(jsono);
				
				// handle the data
				
				ccda_type_value = request.getParameter("ccda_type_val");
				
				//System.out.println(ccda_type_value);
				
				if(ccda_type_value == null)
				{
					ccda_type_value = "";
				}
				
				HttpClient client = new DefaultHttpClient();
				
				String ccdaURL = this.props.getProperty("CCDAValidationServiceURL");
				
				
				HttpPost post = new HttpPost(ccdaURL);

				MultipartEntity entity = new MultipartEntity();
				// set the file content
				entity.addPart("file", new InputStreamBody(file.getInputStream() , file.getOriginalFilename()));
				// set the CCDA type
				
				entity.addPart("ccda_type",new StringBody(ccda_type_value));
				
				
				entity.addPart("return_json_param", new StringBody("true"));
				entity.addPart("debug_mode", new StringBody("true"));
				
				post.setEntity(entity);
				
				HttpResponse relayResponse = client.execute(post);
				//create the handler
				ResponseHandler<String> handler = new BasicResponseHandler();
				
				int code = relayResponse.getStatusLine().getStatusCode();
				
				
				if(code!=200) 
				{
					
					//do the error handling.
					statisticsManager.addCcdaValidation(ccda_type_value, false, false, false, true);
				}
				else
				{
					boolean hasErrors = true, hasWarnings = true, hasInfo = true;
					
					String body = handler.handleResponse(relayResponse);
					
					Document doc = Jsoup.parseBodyFragment(body);
					
					Element json = doc.select("pre").first();
					
					JSONObject jsonbody = new JSONObject(json.text());
					
					JSONObject report = jsonbody.getJSONObject("report");
					hasErrors = report.getBoolean("hasErrors");
					hasWarnings = report.getBoolean("hasWarnings");
					hasInfo = report.getBoolean("hasInfo");
					
					JSONResponseBody = jsonbody;
					
					statisticsManager.addCcdaValidation(ccda_type_value, hasErrors, hasWarnings, hasInfo, false);
				}				
				

		} catch (Exception e) {
			statisticsManager.addCcdaValidation(ccda_type_value, false, false, false, true);
			
			throw new RuntimeException(e);
		} 
		
	}

	@RequestMapping(params = "javax.portlet.action=uploadCCDA")
	public ModelAndView process(RenderRequest request, Model model)
			throws IOException {
		Map map = new HashMap();
		
		map.put("files", fileJson);
		
		map.put("body", JSONResponseBody);
		
		return new ModelAndView("cCDAValidatorJsonView", map);
	}

	@RenderMapping()
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("view");

		return modelAndView;
	}

	public StatisticsManager getStatisticsManager() {
		return statisticsManager;
	}

	public void setStatisticsManager(StatisticsManager statisticsManager) {
		this.statisticsManager = statisticsManager;
	}
}
