package sit.portal.ccdavalidator.services;
        
import com.liferay.portal.kernel.servlet.PortalDelegateServlet;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class TestServlet extends HttpServlet {

	//If the size is greater than this value, it will be stored on disk, temporarily. The value is measured in bytes.
	private static final int THRESHOLD_SIZE = 1024 * 1024 * 3;    // 3MB
	//specifies the maximum size of an upload file
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 10;    // 10MB 
	//specifies the maximum size of an HTTP request which contains the upload file and other form’s values, 
	//so this constant should be greater than the MAX_FILE_SIZE.
	private static final int REQUEST_SIZE = 1024 * 1024 * 11;    // 11MB
	
	private static final String CCDATYPE_FLDNAME = "ccda_type_val"; 
	private static final String CCDAFILE_FLDNAME = "upload_ccda";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
	    out.println("CCDA validation service v1.0 is running...");
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
		
		org.apache.commons.fileupload.servlet.ServletFileUpload upload = 
				new org.apache.commons.fileupload.servlet.ServletFileUpload(factory);
		
		upload.setFileSizeMax(MAX_FILE_SIZE);
		upload.setSizeMax(REQUEST_SIZE);
		
		String ccda_type_value = null;
		String upload_ccda_filename = null;
		InputStream uploadedFileStream = null;
		Boolean uploadSuccess = false;
		
		List<FileItem> items;
		try{
			items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				
				//data not related to an uploaded file
				if (item.isFormField()) {
					//verify the field name,
					String _fldName = item.getFieldName();
					if(_fldName.equals(CCDATYPE_FLDNAME))
						ccda_type_value = item.getString();
				} else {
					//verify the fieldname,
					String _fldName = item.getFieldName();
					if(!_fldName.equals(CCDAFILE_FLDNAME))
						continue;
					
					upload_ccda_filename = item.getName();
					//get the stream.
					uploadedFileStream = item.getInputStream();
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
		
		String remoteServiceResponseString = null;
		
		//replay the file to the web service.
		if(uploadSuccess && 
				ccda_type_value!= null && 
				upload_ccda_filename !=null && 
				uploadedFileStream !=null)
		{
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(
					"http://localhost:7080/CCDADocumentValidator/ValidationController");

			MultipartEntity entity = new MultipartEntity();
			// set the file content
			entity.addPart("file", new InputStreamBody(uploadedFileStream , upload_ccda_filename));
			// set the CCDA type
			entity.addPart("ccdaType",
					new StringBody(ccda_type_value));

			post.setEntity(entity);

			HttpResponse relayResponse = client.execute(post);
			//create the handler
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			int code = relayResponse.getStatusLine().getStatusCode();
			
			if(code!=200) 
			{
				//do the error handling.
			}
			
			String body = handler.handleResponse(relayResponse);
			
			Document doc = Jsoup.parseBodyFragment(body);
			Element bodyElm = doc.body();
			remoteServiceResponseString = bodyElm.toString();
		}
		
		PrintWriter out = response.getWriter();
		if(remoteServiceResponseString!=null)
			out.println(remoteServiceResponseString);
		else
			out.println("Upload failed.");
	}


	

}
