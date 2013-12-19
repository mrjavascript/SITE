package org.sitenv.portlets.qrda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;

import javax.portlet.PortletRequest;
import javax.portlet.ResourceResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
import org.sitenv.portlets.qrda.models.QRDAValidationEnhancedResult;
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
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
		System.out.println("init portlet class");
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

			inputStream.reset();
			r = relayCCDAtoQRDAValidator(inputStream, fileName, category);
			r.parse();

			HashMap<String, String> xpathMapping = new HashMap<String, String>();

			if (r.getEnhancedResults() != null) {
				int seed = 0;

				for (QRDAValidationEnhancedResult reslt : r
						.getEnhancedResults()) {
					String keyValue = null;

					String xpath = reslt.getXpath() + "]";

					if (!xpathMapping.containsKey(xpath)) {
						seed++;
						keyValue = "000" + seed;
						xpathMapping.put(xpath, keyValue);
					} else {
						keyValue = xpathMapping.get(xpath);
					}
					System.out.print("added xpath:" + xpath);
					System.out.println(" added key:" + keyValue);
					reslt.setNavKey(keyValue);
				}
			}

			if (!xpathMapping.isEmpty()) {
				orgXml = StringEscapeUtils.escapeXml(InjectTags(orgXml,
						xpathMapping));
			}

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
			response.setOrgXml(response.getOrgXml());

		} catch (IOException e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getMessage() + PrintStackStrace(e));
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

	private String PrintStackStrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	private String InjectTags(String RawXml,
			HashMap<String, String> XpathMapping)
			throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(RawXml)));
		XPath xpath = XPathFactory.newInstance().newXPath();

		for (String xpathexpr : XpathMapping.keySet()) {
			XPathExpression expr = xpath.compile(xpathexpr);

			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			System.out.println("Node list v1: " + nl.getLength());
			for (int i = 0; i < nl.getLength(); i++) {
				Node childNode = nl.item(i);
				setAttribute(childNode, "anchor", XpathMapping.get(xpathexpr));
			}
		}

		DOMImplementationLS domImplementation = (DOMImplementationLS) doc
				.getImplementation();
		LSSerializer lsSerializer = domImplementation.createLSSerializer();
		return lsSerializer.writeToString(doc);
	}

	private void setAttribute(Node node, String attName, String val) {
		NamedNodeMap attributes = node.getAttributes();
		Node attNode = node.getOwnerDocument().createAttribute(attName);
		attNode.setNodeValue(val);
		attributes.setNamedItem(attNode);
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
