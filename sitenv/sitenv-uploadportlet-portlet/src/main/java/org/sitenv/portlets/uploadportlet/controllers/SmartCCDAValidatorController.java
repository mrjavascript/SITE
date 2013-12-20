package org.sitenv.portlets.uploadportlet.controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.sitenv.common.utilities.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

@Controller
@RequestMapping("VIEW")
public class SmartCCDAValidatorController extends BaseController {

	private String smartCcdaResponse = null;
	private String smartCcdaRubricResponse = null;

	@ActionMapping(params = "javax.portlet.action=smartCCDA")
	public void response(MultipartActionRequest request, ActionResponse response) throws IOException {

		if (this.props == null)
		{
			this.loadProperties();
		}
		
		MultipartFile file = request.getFile("file");
		String uploadedFileContent = null;

		response.setRenderParameter("javax.portlet.action", "success");

		try {

			StringWriter writer = new StringWriter();
			IOUtils.copy(file.getInputStream(), writer, "UTF-8");
			uploadedFileContent = writer.toString();

			// handle the data

			String ccda_type_value = request.getParameter("ccda_type_val");
			if (ccda_type_value == null) {
				ccda_type_value = "";
			}

			HttpClient client = new DefaultHttpClient();

			// route the traffic into the fiddler.comment out this code when go
			// production.
			// HttpHost proxy = new HttpHost("127.0.0.1", 8888);
			// client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
			// proxy);

			HttpPost post = new HttpPost(
					props.getProperty("SmartCCDARemoteServiceUrl"));

			StringEntity entity = new StringEntity(uploadedFileContent);

			post.setEntity(entity);

			HttpResponse relayResponse = client.execute(post);
			// create the handler
			ResponseHandler<String> handler = new BasicResponseHandler();

			int code = relayResponse.getStatusLine().getStatusCode();
			if (code != 200) {
				// do the error handling.
			}

			smartCcdaResponse = handler.handleResponse(relayResponse);

			// fetch the rubric look up table.
			HttpGet getRubricRequest = new HttpGet(props.getProperty("SmartCCDARemoteRubricsUrl"));
			HttpResponse getRubricResponse = client.execute(getRubricRequest);
			code = getRubricResponse.getStatusLine().getStatusCode();
			if (code != 200) {
				// do the error handling.
			}
			smartCcdaRubricResponse = handler.handleResponse(getRubricResponse);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		response.setRenderParameter("javax.portlet.action", "smartCCDA");

	}

	@RequestMapping(params = "javax.portlet.action=smartCCDA")
	public ModelAndView process(RenderRequest request, Model model)
			throws IOException {
		Map map = new HashMap();

		map.put("smartCcdaResponse", smartCcdaResponse);

		map.put("smartCcdaRubricResponse", smartCcdaRubricResponse);

		return new ModelAndView("smartCCDAValidatorJsonView", map);
	}
}
