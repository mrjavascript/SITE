package org.sitenv.portlets.qrda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.portlet.PortletRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
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
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.google.gson.Gson;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;

@Controller(value = "qrda")
@RequestMapping("VIEW")
public class QrdaValiationController {

	public static final String DEFAULT_PROPERTIES_FILE = "environment.properties";

	protected Properties props;

	protected String QRDA_VALIDATOR_URL = "http://localhost:7080/QrdaValidatorServices/QRDA/Validate";

	protected void loadProperties() throws IOException {
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream(DEFAULT_PROPERTIES_FILE);

		if (in == null) {
			props = null;
			throw new FileNotFoundException(
					"Environment Properties File not found in class path.");
		} else {
			props = new Properties();
			props.load(in);
		}
	}

	public QrdaValiationController() throws IOException {
		loadProperties();
		QRDA_VALIDATOR_URL = props.getProperty("qrdaValidatorUrl");
	}

	@ResourceMapping("ajaxUploadFile")
	public void fileUploaded(PortletRequest portletRequest,
			ResourceResponse response) throws IOException {

		OutputStream outStream = null;
		QRDAValidationResponse r = new QRDAValidationResponse();

		try {

			// cast the upload request.
			UploadPortletRequest uploadRequest = PortalUtil
					.getUploadPortletRequest(portletRequest);

			String category = ParamUtil.getString(uploadRequest, "category");
			System.out
					.println("Responding ajax call, relay the request to url:"
							+ QRDA_VALIDATOR_URL);
			System.out.println("category1:" + category);

			String fileName = uploadRequest.getFileName("fileData");
			InputStream inputStream = uploadRequest.getFileAsStream("fileData");

			StringWriter writer = new StringWriter();
			IOUtils.copy(inputStream, writer, "UTF-8");
			String orgXml = writer.toString();
			orgXml = StringEscapeUtils.escapeXml(orgXml);
			inputStream.reset();
			r = relayCCDAtoQRDAValidator(inputStream, fileName, category);
			r.parse();

			r.setOrgXml(orgXml);

		} catch (Exception e) {
			r.setSuccess(false);
			r.setErrorMessage("error:" + e.getMessage());
			e.printStackTrace();
		}

		Gson gson = new Gson();
		outStream = response.getPortletOutputStream();
		outStream.write(gson.toJson(r).getBytes());

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

		System.out.println("hit post");

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

			QRDAValidationResponse response = relayCCDAtoQRDAValidator(
					inputStream, fileName, uploadedFile.getCategory());
			response.parse();

			model.addAttribute("message", response.getValidationResult());
			model.addAttribute("results", response.getValidationResults());
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

	// post back handler
	@ActionMapping("uploadFileIE")
	public void fileUploadedIE(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result, Model model) {

		System.out.println("hit post for IE");

		InputStream inputStream = null;

		fileValidator.validate(uploadedFile, result);

		if (result.hasErrors()) {

			// return new ModelAndView("uploadForm");

			model.addAttribute("errors", result);
			return;
		}

		String fileName = uploadedFile.getFileData().getOriginalFilename();

		QRDAValidationResponse response = new QRDAValidationResponse();
		try {
			inputStream = uploadedFile.getFileData().getInputStream();

			response = relayCCDAtoQRDAValidator(inputStream, fileName,
					uploadedFile.getCategory());
			response.parse();
			// parse to json string, and set on the data model.

		} catch (IOException e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			response.setSuccess(false);
			response.setErrorMessage(e.getMessage() + sw.toString());
			response.setNote(QRDA_VALIDATOR_URL);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
				}
			}
		}

		Gson gson = new Gson();
		model.addAttribute("validationResultJson", gson.toJson(response));

		return;
	}

	private QRDAValidationResponse relayCCDAtoQRDAValidator(
			InputStream doc2validate, String docFileName, String category)
			throws ClientProtocolException, IOException {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(QRDA_VALIDATOR_URL);

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
