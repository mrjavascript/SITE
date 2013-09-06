package org.sitenv.portlets.uploadportlet.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class SmartCCDAValidationServlet extends HttpServlet{

	//if file is exceeding this size, it will be temporiarily stored on the disk.
	private static final int THRESHOLD_SIZE = 1024 * 1024 * 3;    // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 10;    // 10MB 
	private static final int REQUEST_SIZE = 1024 * 1024 * 11;    // 11MB
	private static final String CCDAFILE_FLDNAME = "upload_ccda";
	private static final String SMARTCCDA_URL_FLDNAME = "smart_ccda_url";
	private static final String SMARTCCDA_RUBRIC_URL_FLDNAME = "smart_ccda_rubrics_url";
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
	    out.println("Smart CCDA relay service v1.1 is running...");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		if(!ServletFileUpload.isMultipartContent(request)) 
			return;
		
		org.apache.commons.fileupload.disk.DiskFileItemFactory factory = 
				new org.apache.commons.fileupload.disk.DiskFileItemFactory();
		factory.setSizeThreshold(THRESHOLD_SIZE);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		//prepare to recieve the file uploaded.
		org.apache.commons.fileupload.servlet.ServletFileUpload upload = 
				new org.apache.commons.fileupload.servlet.ServletFileUpload(factory);
		
		upload.setFileSizeMax(MAX_FILE_SIZE);
		upload.setSizeMax(REQUEST_SIZE);
		
		String uploadedFileContent = null;
		Boolean uploadSuccess = false;
		String remoteSmartCCDAUrl = null;
		String remoteSmartCCDARubricUrl = null;
		List<FileItem> items;
		
		try{
			items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					//verify the field name,
					String _fldName = item.getFieldName();
					if(_fldName.equals(SMARTCCDA_URL_FLDNAME))
						remoteSmartCCDAUrl = item.getString();
					else if(_fldName.equals(SMARTCCDA_RUBRIC_URL_FLDNAME))
						remoteSmartCCDARubricUrl = item.getString();
				}else
				//data not related to an uploaded file
				{ 
					//verify the fieldname,
					String _fldName = item.getFieldName();
					if(!_fldName.equals(CCDAFILE_FLDNAME))
						continue;
					//get the stream.
					
					StringWriter writer = new StringWriter();
					IOUtils.copy(item.getInputStream(), writer, "UTF-8");
					uploadedFileContent = writer.toString();
				}
			}
			
			uploadSuccess = true;
			
		} catch (FileUploadException e) {
			if(e.getMessage().endsWith("bytes.")) {
				request.setAttribute("message", "Maxiumum file size exceeeded. " + 
						"Please return to the previous page and select a file that is less than "
							+ MAX_FILE_SIZE / 1024 / 1024 + "MB(s).");
			} else {
				request.setAttribute("message", "There was an error uploading the file: " + e.getMessage());
			}
		} catch (Exception e) {
			request.setAttribute("message", "There was an error saving the file: " + e.getMessage());
		}
		
		String JsonResponse = null;
		String JsonRubricResponse = null;
		
		if(uploadSuccess && remoteSmartCCDAUrl!=null && remoteSmartCCDARubricUrl!=null)
		{
			HttpClient client = new DefaultHttpClient();
			
			//route the traffic into the fiddler.comment out this code when go production.
			//HttpHost proxy = new HttpHost("127.0.0.1", 8888);
			//client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			
			HttpPost post = new HttpPost(remoteSmartCCDAUrl);
			
			StringEntity entity = new StringEntity(uploadedFileContent);
			
			post.setEntity(entity);

			HttpResponse relayResponse = client.execute(post);
			//create the handler
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			int code = relayResponse.getStatusLine().getStatusCode();
			if(code!=200) 
			{
				//do the error handling.
			}
			
			JsonResponse = handler.handleResponse(relayResponse);
			
			//fetch the rubric look up table.
			HttpGet getRubricRequest = new HttpGet(remoteSmartCCDARubricUrl);
			HttpResponse getRubricResponse = client.execute(getRubricRequest);
			code = getRubricResponse.getStatusLine().getStatusCode();
			if(code!=200) 
			{
				//do the error handling.
			}
			JsonRubricResponse = handler.handleResponse(getRubricResponse);
			
		}
		
		String returnJson = null;
		if(JsonRubricResponse!=null && JsonResponse!=null)
		{
			String returnJsonTemplate = "{\"IsSuccess\":\"true\",\"RubricLookup\":{RUBRICLOOKUP},\"Results\":{RESPONSE}}";
			returnJson =returnJsonTemplate.replace("{RUBRICLOOKUP}",JsonRubricResponse).replace("{RESPONSE}", JsonResponse);
		}
		
		if(returnJson!=null)
		{
			PrintWriter out = response.getWriter();
			System.out.println(returnJson);
			out.println(returnJson);
		}
		else
		{
			PrintWriter out = response.getWriter();
			out.println("{\"IsSuccess\":\"false\", \"Message\":\"Relay failed.\"}");
		}
	}
	
}
