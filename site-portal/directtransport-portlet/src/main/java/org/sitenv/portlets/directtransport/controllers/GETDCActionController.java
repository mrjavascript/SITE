package org.sitenv.portlets.directtransport.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.ActionResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.sitenv.common.statistics.manager.StatisticsManager;
import org.sitenv.common.utilities.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

@Controller
@RequestMapping("VIEW")
public class GETDCActionController extends BaseController {

	// Get DC URLs
	private static final String DEFAULT_GETDC_URL = "http://devccda.sitenv.org/GetDCService";
	private static final String GETDC_ACTION_FORMAT_STRING = "%s/getdc/discover/?endpoint=%s";

	// Validation
	private Pattern pattern;
	private Matcher matcher;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String DOMAIN_NAME_PATTERN = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";

	@Autowired
	private StatisticsManager statisticsManager;

	@ActionMapping(params = "javax.portlet.action=GETDCAction")
	public void response(MultipartActionRequest request, ActionResponse response) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("view");
	}

	@ResourceMapping(value = "queryGetdcAction")
	@ResponseBody
	public void queryGetdcAction(ResourceRequest request,
			ResourceResponse response) throws PortalException, SystemException,
			IOException, ClientProtocolException, RuntimeException {

		// Find the service
		loadProperties();
		String URL = props.getProperty("getDcService");
		if (URL == null) {
			URL = DEFAULT_GETDC_URL;
		}
		String endpoint = request.getParameter("directAddress");

		// Validate the endpoint
		String errors = validateEndpoint(endpoint);
		if (errors != null) {
			JSONObject json = JSONFactoryUtil.createJSONObject(errors
					.toString());
			response.getWriter().write(json.toString());
			return;
		}
		String getDc = String.format(GETDC_ACTION_FORMAT_STRING, URL, endpoint);

		/*
		 * Make a HTTP get request to obtain the email mapping information.
		 */
		String line;
		StringBuilder output;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(getDc);
		getRequest.addHeader("accept", "application/json");
		HttpResponse httpResponse = httpClient.execute(getRequest);
		if (httpResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ httpResponse.getStatusLine().getStatusCode());
		}

		/*
		 * Pass back the response from GETDC to the calling script.
		 */
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(httpResponse.getEntity().getContent())));
		output = new StringBuilder();
		while ((line = br.readLine()) != null) {
			output.append(line);
		}
		httpClient.getConnectionManager().shutdown();
		JSONObject json = JSONFactoryUtil.createJSONObject(output.toString());
		response.getWriter().write(json.toString());

	}

	/*
	 * Validates whether the endpoint is a valid direct address or domain name
	 */
	private String validateEndpoint(String endpoint) {
		if (endpoint.contains("@")) {
			// Validate email pattern
			pattern = Pattern.compile(EMAIL_PATTERN);
			matcher = pattern.matcher(endpoint);
			if (! matcher.matches()) {
				return createJsonError(String.format(
						"Invalid Direct Address Entered: %s", endpoint));
			}
		} else {
			// Validate domain name pattern
			pattern = Pattern.compile(DOMAIN_NAME_PATTERN);
			if (! pattern.matcher(endpoint).find()) {
				return createJsonError(String.format(
						"Invalid Domain Entered: %s", endpoint));
			}
		}

		return null;
	}

	private String createJsonError(String error) {
		JSONObject object = JSONFactoryUtil.createJSONObject();
		object.put("error", error);
		return object.toString();
	}

	/*
	 * GET/SET
	 */
	public StatisticsManager getStatisticsManager() {
		return statisticsManager;
	}

	public void setStatisticsManager(StatisticsManager statisticsManager) {
		this.statisticsManager = statisticsManager;
	}

}
