package org.sitenv.services.ccda;


//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
//import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sitenv.common.statistics.manager.StatisticsManager;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/")
@Produces("text/xml")
public class CCDAService {
    
	Logger logger = LogManager.getLogger(CCDAService.class.getName());
	
	public static final String DEFAULT_PROPERTIES_FILE = "environment.properties";
	
	protected Properties props;
	
	@Autowired
	private StatisticsManager statisticsManager;
	
	
    
	protected void loadProperties() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE);
		
		if (in == null)
		{
			props = null;
			throw new FileNotFoundException("Environment Properties File not found in class path.");
		}
		else
		{
			props = new Properties();
			props.load(in);
		}
	}
	
	public CCDAService() throws IOException {
        loadProperties();
    }
    
    
    
    @POST
    @Path("/Validate/")
    @Consumes("multipart/form-data")
    @Produces("application/json")
    public String Validate(MultipartBody body){
    	
    	Date requestStart = new Date(); 
    	
		JSONObject json = null;
		
		String mu2_ccda_type_value = null;
		
		mu2_ccda_type_value = body.getAttachmentObject("type_val", String.class);
		
		if(mu2_ccda_type_value == null)
		{
			mu2_ccda_type_value = "";
		}
		
		
		Attachment file = body.getAttachment("file");
		
		DataHandler fileHandler = file.getDataHandler();
		
		try {
			
			
			if (this.props == null)
			{
				this.loadProperties();
			}
			
			String mu2CcdaURL = null;
			
			if (mu2_ccda_type_value.equals("NonSpecificCCDA")) {
				mu2CcdaURL = this.props.getProperty("NonSpecificCCDAValidationServiceURL");
			} else {
				mu2CcdaURL = this.props.getProperty("CCDAValidationServiceURL");
			}
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(mu2CcdaURL);
			
			MultipartEntity entity = new MultipartEntity();
			
			// set the file content
			entity.addPart("file", new InputStreamBody(fileHandler.getInputStream() , fileHandler.getName()));
			
			// set the CCDA type
			entity.addPart("ccda_type",new StringBody(mu2_ccda_type_value));
			entity.addPart("return_json_param", new StringBody("true"));
			
			//Change this to "false" in production:
			entity.addPart("debug_mode", new StringBody("true"));
			
			post.setEntity(entity);
			HttpResponse relayResponse = client.execute(post);
			json = handleCCDAResponse(relayResponse, mu2_ccda_type_value);
			
	    } catch (Exception e) {
	    	statisticsManager.addCcdaValidation(mu2_ccda_type_value, false, false, false, true);
	    	logger.error("Error while accessing CCDA service: ",  e);
	    	try {
				json = new JSONObject("{ \"error\" : {\"message\":"+"\""+e.getMessage()+"\""+"}}");
			} catch (JSONException e1) {
				logger.error("Error while creating error JSON output: ",  e1);
			}
	    }
		
		Date requestFinish = new Date();
		
		String logMessage = "";
		try {
			JSONObject error = json.getJSONObject("error");
			String message = error.getString("message");
			logMessage = "[Failure] RequestTime: "+requestStart.toString()+" ResponseTime:"+requestFinish+" Message:"+message;
			
		} catch (JSONException e) {
			logMessage = "[Success] RequestTime: "+requestStart.toString()+" ResponseTime:"+requestFinish;
		}
		logger.info(logMessage);
		
		return json.toString();
    }
    
    
    private JSONObject handleCCDAResponse(HttpResponse relayResponse, String mu2_ccda_type_value) throws ClientProtocolException, IOException, JSONException{
    	
    	ResponseHandler<String> handler = new BasicResponseHandler();
		
		int code = relayResponse.getStatusLine().getStatusCode();
		
		JSONObject jsonbody = null;
		
		if(code!=200)
		{
			
			//do the error handling.
			logger.log(Level.ERROR, "Error while accessing CCDA service: "
			+ code + ": "
			+ relayResponse.getStatusLine().getReasonPhrase());
			
			jsonbody = new JSONObject("{ \"error\" : {\"message\": Error while accessing CCDA service - "
			+"\""+code +"-"+relayResponse.getStatusLine().getReasonPhrase() +"\""+"}}");
			
			statisticsManager.addCcdaValidation(mu2_ccda_type_value, false, false, false, true);
		}
		else
		{
			boolean hasErrors = true, hasWarnings = true, hasInfo = true;
			
			
	    	org.apache.http.Header[] timeAndDateHeaders = relayResponse.getHeaders("response_time_and_date");
	    	
	    	String timeAndDate = "";
	    	
	    	if (timeAndDateHeaders.length > 0){
	    		org.apache.http.Header timeAndDateHeader = timeAndDateHeaders[0];
	    		timeAndDate = timeAndDateHeader.getValue();
	    	}
	    	
	    	org.apache.http.Header[] processingTimeHeaders = relayResponse.getHeaders("round_trip_response_time");
	    	String processingTime = "";
	    	
	    	if (timeAndDateHeaders.length > 0){
	    		org.apache.http.Header processingTimeHeader = processingTimeHeaders[0];
	    		processingTime = processingTimeHeader.getValue();
	    	}
	    	
	    	JSONObject performance_object = new JSONObject().put("dateTimeOfRequest", timeAndDate);
	    	performance_object.put("processingTime", processingTime);
			
	    	
			String body = handler.handleResponse(relayResponse);
			Document doc = Jsoup.parseBodyFragment(body);
			org.jsoup.nodes.Element json = doc.select("pre").first();
			
			
			jsonbody = new JSONObject(json.text());
			JSONObject report = jsonbody.getJSONObject("report");
			hasErrors = report.getBoolean("hasErrors");
			hasWarnings = report.getBoolean("hasWarnings");
			hasInfo = report.getBoolean("hasInfo");
			
			jsonbody.put("performance", performance_object);
			statisticsManager.addCcdaValidation(mu2_ccda_type_value, hasErrors, hasWarnings, hasInfo, false);	
		}
		return jsonbody;
    }
    
    
    /*
    @GET
    @Path("/TestValidate")
    @Produces("application/json")
    public String TestValidate()
    {	
    	String json = null;
		try {
			 
			String fileLoc = "C:\\CWWorkspace\\SITE\\CCDA\\CCDA_Ambulatory.xml";
			InputStream is = new FileInputStream(fileLoc);
			
			
			HttpClient client = new DefaultHttpClient();
			String URL = "http://localhost:7080/CcdaValidatorServices/CCDA/Validate/";
			HttpPost post = new HttpPost(URL);
			
			MultipartEntity entity = new MultipartEntity();
			
			// set the file content
			entity.addPart("file", new InputStreamBody(is , "testFile"));
			
			// set the CCDA type
			//entity.addPart("type_val",new StringBody("TransitionsOfCareAmbulatorySummary"));
			entity.addPart("type_val",new StringBody("NonSpecificCCDA"));
			
			
			post.setEntity(entity);
			HttpResponse relayResponse = client.execute(post);
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			String body = handler.handleResponse(relayResponse);
			json = body;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return json;
    }
    */
    
}
