package sit.portal.directtransport.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class GetTrustBundle extends HttpServlet{

	
	private static Logger _log = Logger.getLogger(GetTrustBundle.class);
	
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
	
	private static final long serialVersionUID = 1L;
	
	private String filePath = null;
	
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException
    {
		// Prepare streams.
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        
		try {
			//get the file path from configuration.
			filePath = this.getServletContext().getInitParameter("trustBundleFile");
			
			_log.trace("read bundle file web.xml:" + filePath);
			
			// Decode the file name (might contain spaces and on) and prepare file object.
	        File file = new File(filePath);
	
	        // Check if file actually exists in filesystem.
	        if (!file.exists()) {
	            // Do your thing if the file appears to be non-existing.
	            // Throw an exception, or send 404, or show default/warning page, or just ignore it.
	            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
	            _log.error("Certificate bundle does not exist.");
	            return;
	        }
	
	        // Get content type by filename.
	        String contentType = getServletContext().getMimeType(file.getName());
	
	        // If content type is unknown, then set the default value.
	        // For all content types, see: http://www.w3schools.com/media/media_mimeref.asp
	        // To add new content types, add new mime-mapping entry in web.xml.
	        if (contentType == null) {
	            contentType = "application/octet-stream";
	        }
	
	        // Init servlet response.
	        response.reset();
	        response.setBufferSize(DEFAULT_BUFFER_SIZE);
	        response.setContentType(contentType);
	        response.setHeader("Content-Length", String.valueOf(file.length()));
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
	
	        // Open streams.
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            _log.trace("trust bundle file serverd.");
        } finally {
            // Gently close streams.
            close(output);
            close(input);
        }
    }

    // Helpers (can be refactored to public utility class) ----------------------------------------

    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it.
            	_log.error(e);
            }
        }
    }

}
