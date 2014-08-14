package org.sitenv.portlets.qrda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.portlet.PortletRequest;
import javax.portlet.ResourceResponse;
import javax.swing.tree.FixedHeightLayoutCache;
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
import org.sitenv.portlets.qrda.models.QRDAAjaxResponse;
import org.sitenv.portlets.qrda.models.QRDAFileResponse;
import org.sitenv.portlets.qrda.models.QRDASchemaError;
import org.sitenv.portlets.qrda.models.QRDAValidationEnhancedResult;
import org.sitenv.portlets.qrda.models.QRDAValidationResponse;
import org.sitenv.portlets.qrda.models.UploadedFile;
import org.sitenv.common.statistics.manager.StatisticsManager;
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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;

@Controller(value = "qrda")
@RequestMapping("VIEW")
public class QrdaValiationController {

	public static final String DEFAULT_PROPERTIES_FILE = "environment.properties";

	private static final Log logger = LogFactoryUtil
			.getLog(QrdaValiationController.class);
	

	@Autowired
	private StatisticsManager statisticsManager;
	
	protected Properties props;

	// default value.
	protected String QRDA_VALIDATOR_URL = "http://localhost:7080/QrdaValidatorServices/QRDA/Validate";
	
	public static final String UTF8_BOM = "\uFEFF";
	
	private static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }

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

		// Calculating the matrix.
		OutputStream outStream = null;
		QRDAValidationResponse r = new QRDAValidationResponse();
		QRDAAjaxResponse ajax = new QRDAAjaxResponse();
		ajax.setFiles(new ArrayList<QRDAFileResponse>());

		try {

			Date d1 = new Date();

			// cast the upload request.
			UploadPortletRequest uploadRequest = PortalUtil
					.getUploadPortletRequest(portletRequest);

			String selectedCategory = ParamUtil.getString(uploadRequest,
					"category");
			System.out
					.println("Responding ajax call, relay the request to url:"
							+ QRDA_VALIDATOR_URL);
			System.out.println("category1:" + selectedCategory);
			
			

			String fileName = uploadRequest.getFileName("qrdauploadfile");
			InputStream inputStream = uploadRequest.getFileAsStream("qrdauploadfile");
			
			QRDAFileResponse file = new QRDAFileResponse();
			file.setName(fileName);
			if (uploadRequest.getSize("qrdauploadfile") != null)
			{
				file.setSize(uploadRequest.getSize("qrdauploadfile").toString());
			}
			ajax.getFiles().add(file);

			// output the content of document to a string.
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputStream, writer, "UTF-8");
			String orgXml = writer.toString();

			Date d2 = new Date();

			inputStream.reset();
			long diff = d2.getTime() - d1.getTime();
			long diffExtractAttachment = diff / 1000 % 60;

			d1 = new Date();
			// do the validation then return the result.
			r = relayCCDAtoQRDAValidator(inputStream, fileName,
					selectedCategory);
			d2 = new Date();
			diff = d2.getTime() - d1.getTime();
			long diffRelayRequest = diff / 1000 % 60;

			// prepare the meta data for anchor injection.
			HashMap<String, String> xpathMapping = new HashMap<String, String>();
			d1 = new Date();
			if (r.getSchematronErrors() != null) {
				for (QRDAValidationEnhancedResult reslt : r
						.getSchematronErrors()) {
					String xpath = reslt.getXpath();
					String keyValue = reslt.getNavKey();

					if (!xpathMapping.containsKey(xpath)) {
						xpathMapping.put(xpath, keyValue);
					}
				}
			}

			if (r.getSchematronWarnings() != null) {
				for (QRDAValidationEnhancedResult reslt : r
						.getSchematronWarnings()) {
					String xpath = reslt.getXpath();
					String keyValue = reslt.getNavKey();

					if (!xpathMapping.containsKey(xpath)) {
						xpathMapping.put(xpath, keyValue);
					}
				}
			}

			//////////  FIX HERE!!!!!
			
			if (!xpathMapping.isEmpty()) {
				orgXml = StringEscapeUtils.escapeXml(InjectTags(removeUTF8BOM(orgXml),
						xpathMapping));
			} else {
				orgXml = StringEscapeUtils.escapeXml(orgXml);
			}
			d2 = new Date();
			diff = d2.getTime() - d1.getTime();
			long diffAnalyzingResult = diff / 1000 % 60;

			logger.info(String
					.format("Validate file:%s%n, %d seconds to extract the QRDA%n %d seconds to relay the request%n %d seconds to analyze the result.%n",
							fileName, diffExtractAttachment, diffRelayRequest,
							diffAnalyzingResult));

			// set the original QRDA on the response.
			r.setOrgXml(orgXml);
			r.setOrgFileName(fileName);
			r.setSelectedCategory(selectedCategory);

		} catch (Exception e) {
			r.setSuccess(false);
			r.setErrorMessage("error:" + e.getMessage());
			e.printStackTrace();
		}
		Gson gson = new Gson();
		
		ajax.setBody(r);
		
		outStream = response.getPortletOutputStream();
		// write the JSON serialized result back to client.		
		outStream.write(gson.toJson(ajax).getBytes());

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

		return "qrdaSandboxGUI";
	}

	@Autowired
	Validator fileValidator;

	// normal post back handler, if user disabled the javascript.
	@ActionMapping("uploadFile")
	public void fileUploaded(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result, Model model) {

		System.out.println("hit post for java disabled client.");

		InputStream inputStream = null;

		fileValidator.validate(uploadedFile, result);

		if (result.hasErrors()) {
			model.addAttribute("errors", result);
			return;
		}

		try {

			String fileName = uploadedFile.getFileData().getOriginalFilename();

			inputStream = uploadedFile.getFileData().getInputStream();

			// query the remote validator
			QRDAValidationResponse response = relayCCDAtoQRDAValidator(
					inputStream, fileName, uploadedFile.getCategory());

			boolean failed = false;
			String resultMsg = "";
			String rowCache = "";
			StringBuilder htmls = new StringBuilder();

			if (response.getSuccess()) {

				String rowtmp = "<b>Upload Results:</b><br/>"
						+ "The file: {filename} was uploaded successfully."
						+ "<br/><b>QRDA category selected: {category}</b><hr/><hr/><b>Validation Results</b><br/>"
						+ "<font style='color:{color}'><i>{result}</i><br/>{resultmessage}</font><hr/>";

				// schema error.
				if (response.getSchemaErrors().size() != 0) {
					failed = true;
					resultMsg += "The file has encountered schema errors.";
				}

				if (response.getSchematronErrors().size() != 0) {
					failed = true;
					resultMsg += "The file has encountered schema-tron warnings.";
				}

				if (response.getSchematronErrors().size() != 0) {
					failed = true;
					resultMsg += "The file has encountered schema-tron errors.";
				}

				// add the header.
				rowtmp = rowtmp.replace("{filename}", fileName);
				rowtmp = rowtmp.replace("{category}",
						uploadedFile.getCategory());
				rowtmp = rowtmp.replace("{color}", failed ? "color" : "green");
				rowtmp = rowtmp
						.replace("{result}", failed ? "Validation Failed."
								: "Validation Succeeded.");
				rowtmp = rowtmp.replace("{resultmessage}", resultMsg);
				htmls.append(rowtmp);

				// schema error.
				int i = 0;
				if (response.getSchemaErrors().size() != 0) {
					htmls.append("<font color='red'>Schema Errors Received:<hr/>");

					rowtmp = "Error {i}:<br/>{msg}<br/> ";

					for (QRDASchemaError error : response.getSchemaErrors()) {
						rowCache = rowtmp;
						rowCache = rowCache.replace("{i}",
								String.valueOf(i + 1));
						i++;
						rowCache = rowCache.replace("{msg}",
								error.getErrorMessage());
						htmls.append(rowCache);
					}

					htmls.append("</font>");
				}

				// schematron warnings.
				if (response.getSchematronWarnings().size() != 0) {

					i = 0;
					htmls.append("<font color='blue'>Schematron Warnings Received:<hr/>");
					rowtmp = "Warning {i}:<br/><div class='nav' navKey='{key}'><u>{msg}</u></div><br/><br/>";

					for (QRDAValidationEnhancedResult warning : response
							.getSchematronWarnings()) {
						rowCache = rowtmp;
						rowCache = rowCache.replace("{i}",
								String.valueOf(i + 1));
						i++;
						rowCache = rowCache.replace("{msg}",
								warning.getMessage());
						rowCache = rowCache.replace("{key}",
								warning.getNavKey());

						htmls.append(rowCache);
					}

					htmls.append("</font>");
				}

				// schematron errors.
				if (response.getSchematronErrors().size() != 0) {

					i = 0;
					htmls.append("<font color='red'>Schematron Errors Received:<hr/>");
					rowtmp = "Error {i}:<br/><div class='nav' navKey='{key}'><u>{msg}</u></div><br/><br/> ";

					for (QRDAValidationEnhancedResult error : response
							.getSchematronErrors()) {
						rowCache = rowtmp;
						rowCache = rowCache.replace("{i}",
								String.valueOf(i + 1));
						i++;
						rowCache = rowCache
								.replace("{msg}", error.getMessage());
						rowCache = rowCache.replace("{key}", error.getNavKey());

						htmls.append(rowCache);
					}

					htmls.append("</font>");
				}
			} else {
				htmls.append("<h2>Validation failed due to server errors</h2>");
				htmls.append("<div style='color: red;font-weight: bold'>");
				htmls.append(response.getErrorMessage());
				htmls.append("</div>");
			}

			model.addAttribute("results", htmls.toString());
		} catch (Exception e) {
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

	// post back handler for IE browsers.
	@ActionMapping("uploadFileIE")
	public void fileUploadedIE(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result, Model model) {

		System.out.println("hit post for IE9 and below.");

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

			// output the content of document to a string.
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputStream, writer, "UTF-8");
			String orgXml = writer.toString();
			inputStream.reset();

			response = relayCCDAtoQRDAValidator(inputStream, fileName,
					uploadedFile.getCategory());

			HashMap<String, String> xpathMapping = new HashMap<String, String>();
			if (response.getSchematronErrors() != null) {
				for (QRDAValidationEnhancedResult reslt : response
						.getSchematronErrors()) {
					String xpath = reslt.getXpath();
					String keyValue = reslt.getNavKey();

					if (!xpathMapping.containsKey(xpath)) {
						xpathMapping.put(xpath, keyValue);
					}
				}
			}
			
			//////  FIX HERE

			if (!xpathMapping.isEmpty()) {
				orgXml = StringEscapeUtils.escapeXml(InjectTags(removeUTF8BOM(orgXml),
						xpathMapping));
			}

			response.setOrgXml(orgXml);

		} catch (Exception e) {
			
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

		// de-serialize it to json.
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
			// System.out.println("Node list v1: " + nl.getLength());
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

		Integer cat = null;
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(QRDA_VALIDATOR_URL);

		MultipartEntity entity = new MultipartEntity();
		// set the file content
		entity.addPart("doc", new InputStreamBody(doc2validate, docFileName));
		// set the QRDA category
		entity.addPart("category", new StringBody(category));

		if (category.equalsIgnoreCase("categoryI"))
		{
			cat = StatisticsManager.QRDA_CATEGORY_I;
		}
		else
		{
			cat = StatisticsManager.QRDA_CATEGORY_III;
		}
		
		post.setEntity(entity);

		HttpResponse relayResponse = client.execute(post);
		// create the handler
		ResponseHandler<String> handler = new BasicResponseHandler();

		int code = relayResponse.getStatusLine().getStatusCode();

		if (code != 200) {
			
			statisticsManager.addQrdaValidation(cat, false, false, false, true);
			
			QRDAValidationResponse r = new QRDAValidationResponse();
			r.setSuccess(false);
			r.setErrorMessage(String
					.format("Remote validation service returned http status error code:%s. <br> This is usually caused by the service outage, service crashing or the encoding of the CCDA document is other than UTF8",
							String.valueOf(code)));
			return r;
		}

		String body = handler.handleResponse(relayResponse);
		Gson gson = new Gson();
		QRDAValidationResponse response = gson.fromJson(body, QRDAValidationResponse.class);
		
		statisticsManager.addQrdaValidation(cat, 
				(response.getSchemaErrors() != null && response.getSchemaErrors().size() > 0) ? true : false, 
				(response.getSchematronErrors() != null && response.getSchematronErrors().size() > 0) ? true : false, 
				(response.getSchematronWarnings() != null && response.getSchematronWarnings().size() > 0) ? true : false, 
				false);
		
		return response;
	}

	public StatisticsManager getStatisticsManager() {
		return statisticsManager;
	}

	public void setStatisticsManager(StatisticsManager statisticsManager) {
		this.statisticsManager = statisticsManager;
	}
	
	
	
}
