package org.sitenv.portlets.qrda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

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
import org.sitenv.portlets.qrda.models.QRDAValidationResponse;
import org.sitenv.portlets.qrda.models.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.google.gson.Gson;

@Controller(value = "qrdaValiationController")
@RequestMapping("VIEW")
public class QrdaValiationController {

	// RESOURCE MAPPING DEFINITION FOR validate measurement document (This is
	// the operation called via
	// the AJAX Request)
	// @ResourceMapping("ajaxUploadFile")
	@ActionMapping("ajaxUploadFile")
	public void fileUploaded(
			// @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			@RequestParam("fileData") MultipartFile file2,
			BindingResult result, Model model, ResourceRequest request,
			ResourceResponse response) throws IOException {

		int i = 0;
		i = i + 1;

		System.out.println("Ajax hit1");
		System.out.println("filename" + file2.getOriginalFilename()
				+ file2.getName());
		InputStream inputStream = null;
		OutputStream outStream = null;
		QRDAValidationResponse r = null;

		/*
		 * String fileName = uploadedFile.getFileData().getOriginalFilename();
		 * 
		 * try { inputStream = uploadedFile.getFileData().getInputStream();
		 * 
		 * outStream = response.getPortletOutputStream();
		 * 
		 * Gson gson = new Gson();
		 * 
		 * r = relayCCDAtoQRDAValidator(inputStream, fileName,
		 * uploadedFile.getCategory());
		 * 
		 * outStream.write(gson.toJson(r).getBytes());
		 * 
		 * } catch (Exception e) { if (inputStream != null) { try {
		 * inputStream.close(); } catch (Exception e1) { } } if (outStream !=
		 * null) { try { outStream.close(); } catch (Exception e2) { } } }
		 */
	}

	// DEFAULT RENDERMAPPING FOR THE VIEW
	@RenderMapping()
	public String handleRenderRequest(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			Model model) throws Exception {

		// pass the validation result through the rendering.
		Errors errors = (Errors) model.asMap().get("errors");
		if (errors != null) {
			model.addAttribute(
					"org.springframework.validation.BindingResult.uploadedFile",
					errors);
		}

		return "uploadForm";
	}

	@Autowired
	Validator fileValidator;

	// post back handler
	@ActionMapping("uploadFile")
	public void fileUploaded(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result, Model model) {

		InputStream inputStream = null;

		fileValidator.validate(uploadedFile, result);

		if (result.hasErrors()) {

			// return new ModelAndView("uploadForm");

			model.addAttribute("errors", result);
			return;
		}

		String fileName = uploadedFile.getFileData().getOriginalFilename();

		try {
			inputStream = uploadedFile.getFileData().getInputStream();

			/*
			 * File newFile = new File("C:/temp/" + fileName); if
			 * (!newFile.exists()) { newFile.createNewFile(); } outputStream =
			 * new FileOutputStream(newFile); int read = 0; byte[] bytes = new
			 * byte[1024];
			 * 
			 * while ((read = inputStream.read(bytes)) != -1) {
			 * outputStream.write(bytes, 0, read); }
			 */

			QRDAValidationResponse response = relayCCDAtoQRDAValidator(
					inputStream, fileName, uploadedFile.getCategory());

			model.addAttribute("message", response.getValidationResult());
			model.addAttribute("results", response.getResults());
			/*
			 * if (response.isSuccess()) model.addAttribute("message",
			 * response.getValidationResult()); else
			 * model.addAttribute("message", response.getErrorMessage());
			 */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
				}
			}
		}

		return;
	}

	private QRDAValidationResponse relayCCDAtoQRDAValidator(
			InputStream doc2validate, String docFileName, String category)
			throws ClientProtocolException, IOException {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://localhost:7080/QrdaValidatorServices/QRDA/Validate");

		MultipartEntity entity = new MultipartEntity();
		// set the file content
		entity.addPart("doc", new InputStreamBody(doc2validate, docFileName));
		// set the QRDA category
		entity.addPart("category", new StringBody(category));

		post.setEntity(entity);

		HttpResponse relayResponse = client.execute(post);
		// create the handler
		ResponseHandler<String> handler = new BasicResponseHandler();

		int code = relayResponse.getStatusLine().getStatusCode();

		if (code != 200) {
			// do the error handling.
		}

		String body = handler.handleResponse(relayResponse);
		Gson gson = new Gson();
		return gson.fromJson(body, QRDAValidationResponse.class);
	}

}
