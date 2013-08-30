package sit.portal.ccdavalidator.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.lowagie.text.DocumentException;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import sun.misc.BASE64Encoder;

public class DownloadResultReportServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		String path = this.getServletContext().getRealPath("") + File.separator;
	    out.println("CCDA save report result service v1.0 is running... Sample file path:" + path);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("application/force-download");
		//response.setContentLength((int)f.length());
		//response.setContentLength(-1);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition" , "attachment; filename=\"" + "test.pdf"+ "\"");
		response.setHeader("Set-Cookie", "fileDownload=true; path=/");
		
		String ResultContent = request.getParameter("reportContent");
		//ResultContent = URLDecoder.decode(ResultContent, "UTF-8");
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
		sb.append("<head><title>Report</title></head><body>");
		
		String logoImgPath = request.getScheme() + "://" + 
								request.getServerName() + ":" + 
								request.getServerPort() +
								request.getParameter("portletContextPath") + 
								request.getContextPath() + 
								"/css/SITP_Logo_Banner_ClearBG.png";
		
		String imageTag = "<img alt=\"\" src=\"{IMGPATH}\" />";
		imageTag = imageTag.replace("{IMGPATH}", logoImgPath);
		sb.append(imageTag);
		
		
		
		sb.append(ResultContent);
		sb.append("</body></html>");
		
		
		
		try {
        	
        	DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
       		fac.setNamespaceAware(false);
       		fac.setValidating(false);
       		fac.setFeature("http://xml.org/sax/features/namespaces", false);
       		fac.setFeature("http://xml.org/sax/features/validation", false);
       		fac.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
       		fac.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        	
       		org.jsoup.nodes.Document doc = Jsoup.parseBodyFragment(sb.toString());
       		String refinedHtml = doc.toString();
       		
       		DocumentBuilder builder = fac.newDocumentBuilder();
       	    Document refineddoc = builder.parse(new ByteArrayInputStream(refinedHtml.getBytes("UTF-8")));
       		
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(refineddoc, null);
            renderer.layout();
            OutputStream os = response.getOutputStream();
            renderer.createPDF(os);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	private String encodeToString(String imagepath, String type) {
	    
		String imageString = null;
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    try {
	    	BufferedImage image = ImageIO.read(new File(imagepath));
	        ImageIO.write(image, type, bos);
	        byte[] imageBytes = bos.toByteArray();

	        BASE64Encoder encoder = new BASE64Encoder();
	        imageString = encoder.encode(imageBytes);

	        bos.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return imageString;
	}
}


