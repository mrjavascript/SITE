package org.sitenv.portlets.uploadportlet;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;

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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

;

public class UploadPortlet extends MVCPortlet {
	/*
	 * @Override public void processAction( ActionRequest actionRequest,
	 * ActionResponse actionResponse) throws IOException, PortletException {
	 * PortletPreferences prefs = actionRequest.getPreferences(); String
	 * greeting = actionRequest.getParameter("greeting"); if (greeting != null)
	 * { prefs.setValue("greeting", greeting); prefs.store(); }
	 * 
	 * SessionMessages.add(actionRequest, "success");
	 * super.processAction(actionRequest, actionResponse); }
	 */

	public void setGreeting(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		PortletPreferences prefs = actionRequest.getPreferences();
		String greeting = actionRequest.getParameter("greeting");
		if (greeting != null) {
			try {
				prefs.setValue("greeting", greeting);
				prefs.store();
				SessionMessages.add(actionRequest, "success");
			} catch (Exception e) {
				SessionErrors.add(actionRequest, "error");
			}
		}
	}

	protected String realPath;
	private static Log logger = LogFactoryUtil.getLog(UploadPortlet.class);

	public void uploadFile(ActionRequest actionRequest, ActionResponse actionRresponse) throws PortletException, IOException {
		// get the default folder to save the file.
		String folder = getInitParameter("uploadFolder");
		// construct the path.
		realPath = getPortletContext().getRealPath("/");
		// log the complete path.
		logger.info("RealPath" + realPath + " UploadFolder :" + folder);
		
		try {
			UploadPortletRequest uploadRequest = PortalUtil
					.getUploadPortletRequest(actionRequest);
			
			String sourceFileName = uploadRequest.getFileName("upload_ccda");
			
			InputStream in = new BufferedInputStream(
					uploadRequest.getFileAsStream("upload_ccda"));

			if (uploadRequest.getSize("upload_ccda") == 0) {
				SessionErrors.add(actionRequest, "error");
			}

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(
					"http://localhost:7080/CCDADocumentValidator/ValidationController");

			MultipartEntity entity = new MultipartEntity();
			// set the file content
			entity.addPart("file", new InputStreamBody(in, sourceFileName));
			// set the CCDA type
			entity.addPart("ccdaType",
					new StringBody(uploadRequest.getParameter("ccda_type_val")));

			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			//create the handler
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			int code = response.getStatusLine().getStatusCode();
			if(code!=200) 
			{
				//do the error handling.
			}
			
			String body = handler.handleResponse(response);
			
			Document doc = Jsoup.parseBodyFragment(body);
			Element bodyElm = doc.body();
			String refinedBody = bodyElm.toString();
			
			/*
			//print the body..
			
			logger.info("Siamo nel try");

			System.out.println("Size: " + uploadRequest.getSize("fileName"));

			File file = uploadRequest.getFile("fileName");
			
			logger.info("Nome file:" + uploadRequest.getFileName("fileName"));
			File newFile = null;
			newFile = new File(folder + sourceFileName);
			logger.info("New file name: " + newFile.getName());
			logger.info("New file path: " + newFile.getPath());

			// InputStream in = new BufferedInputStream(
			// uploadRequest.getFileAsStream("fileName"));

			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(newFile);

			byte[] bytes_ = FileUtil.getBytes(in);
			int i = fis.read(bytes_);

			while (i != -1) {
				fos.write(bytes_, 0, i);
				i = fis.read(bytes_);
			}

			fis.close();
			fos.close();
			Float size = (float) newFile.length();
			
			System.out.println("file size bytes:" + size);
			System.out.println("file size Mb:" + size / 1048576);
			
			logger.info("File created: " + newFile.getName());
			SessionMessages.add(actionRequest, "success");
			*/
			
			SessionMessages.add(actionRequest, "success");
			SessionMessages.add(actionRequest, "error");
			//SessionMessages.add(actionRequest, "validationresponse",refinedBody);
			actionRresponse.setRenderParameter("validationresponse", refinedBody);
			
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
			e.printStackTrace();
			SessionMessages.add(actionRequest, "error");
		} catch (NullPointerException e) {
			System.out.println("File Not Found");
			e.printStackTrace();
			SessionMessages.add(actionRequest, "error");
		}

		catch (IOException e1) {
			System.out.println("Error Reading The File.");
			SessionMessages.add(actionRequest, "error");
			e1.printStackTrace();
		}

	}

	

	

}
