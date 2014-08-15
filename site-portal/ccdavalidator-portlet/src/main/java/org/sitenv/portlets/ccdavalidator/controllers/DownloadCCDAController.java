package org.sitenv.portlets.ccdavalidator.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.sitenv.common.utilities.controller.BaseController;
import org.sitenv.common.statistics.manager.StatisticsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.liferay.portal.kernel.servlet.HttpHeaders;

@Controller
@RequestMapping("VIEW")
public class DownloadCCDAController extends BaseController {

	@Autowired
	private StatisticsManager statisticsManager;
	
	@ResourceMapping("saveAsPDF")
	public void serveResource(ResourceRequest resourceRequest, ResourceResponse res) throws PortletException, IOException {

		if (this.props == null)
		{
			this.loadProperties();
		}
		
		String reportContent = resourceRequest.getParameter("reportContent");

		String logoImagePath = resourceRequest.getScheme() + "://"
				+ resourceRequest.getServerName() + ":"
				+ resourceRequest.getServerPort()
				+ resourceRequest.getContextPath()
				+ "/css/SITP_Logo_Banner_ClearBG.png";

		// ResultContent = URLDecoder.decode(ResultContent, "UTF-8");
		
		System.out.println("logoImagePath = " + logoImagePath);

		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
		sb.append("<head><title>Report</title></head><body>");

		
		
		String imageTag = "<img alt=\"\" src=\"{IMGPATH}\" />";
		imageTag = imageTag.replace("{IMGPATH}", logoImagePath);
		sb.append(imageTag);

		sb.append(reportContent);
		sb.append("</body></html>");

		try {

			DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
			fac.setNamespaceAware(false);
			fac.setValidating(false);
			fac.setFeature("http://xml.org/sax/features/namespaces", false);
			fac.setFeature("http://xml.org/sax/features/validation", false);
			fac.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
					false);
			fac.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd",
					false);

			org.jsoup.nodes.Document doc = Jsoup.parseBodyFragment(sb
					.toString());
			String refinedHtml = doc.toString();

			DocumentBuilder builder = fac.newDocumentBuilder();
			Document refineddoc = builder.parse(new ByteArrayInputStream(
					refinedHtml.getBytes("UTF-8")));

			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(refineddoc, null);
			renderer.layout();
			
			
		
		res.setContentType("application/pdf");
		res.addProperty(HttpHeaders.CACHE_CONTROL,
				"max-age=3600, must-revalidate");
		res.addProperty(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
				+ "test.pdf" + "\"");
		// Use this to directly download the file
		res.addProperty("Set-Cookie", "fileDownload=true; path=/");
		OutputStream out = res.getPortletOutputStream();
		// out.write(pdfContentVO.getPdfData());
		renderer.createPDF(out);
		out.flush();
		
		out.close();
		
		statisticsManager.addCcdaDownload();
		
		}catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public StatisticsManager getStatisticsManager() {
		return statisticsManager;
	}

	public void setStatisticsManager(StatisticsManager statisticsManager) {
		this.statisticsManager = statisticsManager;
	}

	
}
