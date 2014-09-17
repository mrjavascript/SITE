package org.sitenv.portlets.ccdavalidator.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.sitenv.common.statistics.manager.StatisticsManager;
import org.sitenv.common.utilities.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.liferay.portal.kernel.servlet.HttpHeaders;


@Controller
@RequestMapping("VIEW")
public class DownloadIncorporationController extends BaseController {	
	
	@Autowired
	private StatisticsManager statisticsManager;

	
	public void copyStream(InputStream in, OutputStream out){
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = in.read(buffer)) != -1) {
			    out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@ResourceMapping("downloadIncorporation")
	public void serveResource(ResourceRequest resourceRequest, ResourceResponse res) throws PortletException, IOException {
		
		if (this.props == null)
		{
			this.loadProperties();
		}
		
		String downloadPath = resourceRequest.getParameter("incorpfilepath");
		
		String[] downloadPathTokens = downloadPath.split("[/\\\\]");
		String fileName = downloadPathTokens[downloadPathTokens.length-1];
		
		if (downloadPathTokens.length > 1){
			String folder = downloadPathTokens[downloadPathTokens.length-2];
			fileName = folder + " - " + fileName;
		}
		
		File downloadFile = new File(downloadPath);
		InputStream in = new FileInputStream(downloadFile);
		
		res.setContentType("application/xml");
		res.addProperty(HttpHeaders.CACHE_CONTROL,
				"max-age=3600, must-revalidate");
		res.addProperty(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
				+ fileName + "\"");
		// Use this to directly download the file
		res.addProperty("Set-Cookie", "fileDownload=true; path=/");
		
		
		OutputStream out = res.getPortletOutputStream();
		
		copyStream(in, out);
		out.flush();
		out.close();
		in.close();
		
	}
	
	
	public StatisticsManager getStatisticsManager() {
		return statisticsManager;
	}

	public void setStatisticsManager(StatisticsManager statisticsManager) {
		this.statisticsManager = statisticsManager;
	}
	
}
