package org.sitenv.portlets.qrda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
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
import org.json.JSONObject;
import org.sitenv.portlets.qrda.models.QRDAValidationResponse;
import org.sitenv.portlets.qrda.models.SamplePortletModel;
import org.sitenv.portlets.qrda.models.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.google.gson.Gson;

@Controller(value = "samplePortletController")
@RequestMapping("VIEW")
public class SamplePortletController {

	// RESOURCE MAPPING DEFINITION FOR addUser (This is the operation called via
	// the AJAX Request)

	// resource mapping is responding to the get requests.
	@ResourceMapping("addUser")
	public void addUser(ResourceRequest request, ResourceResponse response)
			throws IOException {

		// serve resource here

		OutputStream outStream = response.getPortletOutputStream();

		StringBuffer buffer = new StringBuffer();

		SamplePortletModel sample = new SamplePortletModel();

		sample.setFirstName(request.getParameter("sample[firstName]"));

		sample.setLastName(request.getParameter("sample[lastName]"));

		sample.setUsername(request.getParameter("sample[username]"));

		System.out.println("First Name " + sample.getFirstName());

		String test = new JSONObject(sample).toString();

		buffer.append(test);

		System.out.println(buffer.toString());

		outStream.write(buffer.toString().getBytes());

	}

	// MODEL ATTRIBUTE DEFINITION

	@ModelAttribute("sample")
	public SamplePortletModel getCommandObject() {

		return new SamplePortletModel();

	}

	// PROCESS CODE FOR THE addUser ACTION PARAMETER (This is the operation
	// called via the normal form submit)

	// response to post.
	@ActionMapping(params = "action=addUser")
	public void processUserAdd(ActionRequest arg0, ActionResponse arg1,

	@ModelAttribute(value = "sample") SamplePortletModel sample)

	throws Exception {

		// TODO Auto-generated method stub

		System.out.println("first name" + sample.getFirstName());

		arg1.setRenderParameter("action", "addUser"); // redirect the render to
														// renderUserAdd()

	}

	// RENDER MAPPING FOR THE addUser ACTION PARAMETER

	@RenderMapping(params = "action=addUser")
	public ModelAndView renderUserAdd(RenderRequest arg0, RenderResponse arg1,

	@ModelAttribute(value = "sample") SamplePortletModel sample)

	throws Exception {

		System.out.println("user Added render!!");

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("sample");

		modelAndView.addObject("sample", sample);

		return modelAndView;

	}

	// DEFAULT RENDERMAPPING FOR THE VIEW

	@RenderMapping()
	public String handleRenderRequest(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			Model model) throws Exception {

		// ModelAndView modelAndView = new ModelAndView();

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

	/*
	 * @RequestMapping("/fileUploadForm") public ModelAndView getUploadForm(
	 * 
	 * @ModelAttribute("uploadedFile") UploadedFile uploadedFile, BindingResult
	 * result) { return new ModelAndView("uploadForm"); }
	 */

	// @ActionMapping(params = "action=fileUpload")
	// RequestMapping
	@ActionMapping("uploadFile")
	public void fileUploaded(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result, Model model
	// @RequestParam("file") MultipartFile file2, BindingResult result)
	// {
	// @RequestParam("uploadedFile") MultipartFile file) {
	) {

		InputStream inputStream = null;
		OutputStream outputStream = null;

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
