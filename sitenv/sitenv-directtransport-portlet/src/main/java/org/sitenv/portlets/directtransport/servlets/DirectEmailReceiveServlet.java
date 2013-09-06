package org.sitenv.portlets.directtransport.servlets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.sitenv.common.utilities.DesEncrypter;
import org.sitenv.portlets.directtransport.models.GenericResult;

import com.google.gson.Gson;


public class DirectEmailReceiveServlet extends HttpServlet{
	
	private static final int THRESHOLD_SIZE = 1024 * 1024 * 3;    // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 10;    // 10MB 
	private static final int REQUEST_SIZE = 1024 * 1024 * 11;    // 11MB
	
	private static final String CCDAUPLOADTYPE_FLDNAME = "filetype";
	private static final String RECIPIENT_FLDNAME = "endpontemail";
	private static final String SERVERFILEPATH_FLDNAME = "precannedfilepath";
	private static final String CUSTOMCCDAFILE_FLDNAME = "uploadccdafilecontent";
	
	private static final String DIRECTFORMENDPOINT_FLGNAME = "directfromendpoint";
	private static final String SMTPHOST_FLDNAME = "smtphostname";
	private static final String SMTPPORT_FLDNAME = "smtpport";
	private static final String SMTPUSER_FLDNAME = "smtpusername";
	private static final String SMTPPSWRD_FLDNAME = "smtppswrd";
	private static final String ENABLESSL_FLDNAME = "enablessl";
	
	private static final String ENCRYPTEDKEY = "sitplatform@1234";
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		String path = this.getServletContext().getRealPath("") + File.separator;
	    out.println("Direct email receive service 1.0 is running... Sample files path:" + path);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		GenericResult result = new GenericResult();
		
		if(!ServletFileUpload.isMultipartContent(request)) 
			return;
		
		org.apache.commons.fileupload.disk.DiskFileItemFactory factory = 
				new org.apache.commons.fileupload.disk.DiskFileItemFactory();
		factory.setSizeThreshold(THRESHOLD_SIZE);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		
		//prepare to recieve the file uploaded.
		org.apache.commons.fileupload.servlet.ServletFileUpload upload = 
				new org.apache.commons.fileupload.servlet.ServletFileUpload(factory);
		upload.setFileSizeMax(MAX_FILE_SIZE);
		upload.setSizeMax(REQUEST_SIZE);
		
		//String uploadedFileContent = null;
		
		Boolean uploadSuccess = false;
		
		List<FileItem> items;
		
		String fileType = null;
		String serverFilePath = null;
		String endPointEmail = null;
		InputStream stream = null;
		String fileName = null;
		
		String fromendpoint = null;
		String smtphostname = null;
		String smtpport = null;
		String smtpuser = null;
		String smtppswrd = null;
		String enableSSL = null;
		
		try{
			items = upload.parseRequest(request);
			
			Iterator<FileItem> iter = items.iterator();
			
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				
				if (item.isFormField()) {
					//verify the field name,
					String _fldName = item.getFieldName();
					
					if(_fldName.equals(CCDAUPLOADTYPE_FLDNAME))
						fileType = item.getString();
					else if(_fldName.equals(RECIPIENT_FLDNAME))
						endPointEmail = item.getString();
					else if(_fldName.equals(SERVERFILEPATH_FLDNAME))
						serverFilePath = item.getString();
					else if(_fldName.equals(SMTPHOST_FLDNAME))
						smtphostname = item.getString();
					else if(_fldName.equals(SMTPPORT_FLDNAME))
						smtpport = item.getString();
					else if(_fldName.equals(SMTPUSER_FLDNAME))
						smtpuser = item.getString();
					else if(_fldName.equals(SMTPPSWRD_FLDNAME))
						smtppswrd = item.getString();
					else if(_fldName.equals(DIRECTFORMENDPOINT_FLGNAME))
						fromendpoint = item.getString();
					else if(_fldName.equals(ENABLESSL_FLDNAME))
						enableSSL = item.getString();
				}else
				//data not related to an uploaded file
				{ 
					//verify the fieldname,
					String _fldName = item.getFieldName();
					if(!_fldName.equals(CUSTOMCCDAFILE_FLDNAME))
						continue;
					//Todo: write file to the attachment.
					
					stream = item.getInputStream();
					//System.out.println("est size:" + String.valueOf(stream.available()));
					fileName = new File(item.getName()).getName();
					//System.out.println("File name:" + fileName);
				}
			}
			
			uploadSuccess = true;
			
		} catch (FileUploadException e) {
			if(e.getMessage().endsWith("bytes.")) {
				result.setIsSuccess(false);
				result.setErrorMessage("Maxiumum file size exceeeded. " + 
						"Please return to the previous page and select a file that is less than "
							+ MAX_FILE_SIZE / 1024 / 1024 + "MB(s).");
			} else {
				result.setIsSuccess(false);
				result.setErrorMessage("There was an error uploading the file: " + e.getMessage());
			}
		} catch (Exception e) {
			result.setIsSuccess(false);
			result.setErrorMessage("There was an error saving the file: " + e.getMessage());
		}
		
		if(uploadSuccess)
		{
			try {
				
				MimeBodyPart ccdaAttachment = new MimeBodyPart();  
				
				if(fileType.equals("precanned"))
				{
					DataSource ccdaFile = new FileDataSource(serverFilePath);  
			        ccdaAttachment.setDataHandler(new DataHandler(ccdaFile));  
			        ccdaAttachment.setFileName(ccdaFile.getName()); 
				}
				else
				{
					DataSource ccdaFile = new InputStreamDataSource(fileName, "text/plain; charset=UTF-8", stream);
					ccdaAttachment.setDataHandler(new DataHandler(ccdaFile));  
			        ccdaAttachment.setFileName(fileName); 
				}
				
				Properties props = new Properties();
				
				//decrypt the password.
				
				smtppswrd = new DesEncrypter(ENCRYPTEDKEY).decrypt(smtppswrd);
				
				props.put("mail.smtp.host", smtphostname);
				if(enableSSL.toUpperCase().equals("TRUE")){
					props.put("mail.smtp.socketFactory.port", smtpport);
					props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
				}
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", smtpport);
		 		
				
				//java is stupid... java doesn't have true closure.
				final String user = smtpuser;
				final String passord = smtppswrd;
				
				Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(user, passord);
						}
					});
	 
				Message message = new MimeMessage(session);
				
				message.setFrom(new InternetAddress(fromendpoint));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(endPointEmail));
				message.setSubject("SITE direct email test");
				
				Multipart multiPart = new MimeMultipart();  
				  
		        MimeBodyPart messageText = new MimeBodyPart();  
		        messageText.setContent("Dear User," + "\r\nAttached is the C-CDA document you have selected.", "text/plain");  
		        multiPart.addBodyPart(messageText);  
		        
		        multiPart.addBodyPart(ccdaAttachment);  
		  
		        message.setContent(multiPart);  
		        
		        Transport.send(message);
	 
				result.setIsSuccess(true);
				result.setErrorMessage("Mail sent.");
				
			} catch (MessagingException e) {
				result.setIsSuccess(false);
				result.setErrorMessage("Failed to send email due to eror: " + e.getMessage());
			} 
			
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
		    String json = gson.toJson(result);
		    out.println(json);
			
		}
		
		
		
	}
	
	
	private class InputStreamDataSource implements DataSource {  
		   
        private String name;  
        private String contentType;  
        private ByteArrayOutputStream baos;  
          
        InputStreamDataSource(String name, String contentType, InputStream inputStream) throws IOException {  
            
        	this.name = name;  
            this.contentType = contentType;  
              
            baos = new ByteArrayOutputStream();  
              
            int read;  
            byte[] buff = new byte[2024];  
            while((read = inputStream.read(buff)) != -1) {  
                baos.write(buff, 0, read);  
            }  
        }  
          
        public String getContentType() {  
            return contentType;  
        }  
   
        public InputStream getInputStream() throws IOException {  
            return new ByteArrayInputStream(baos.toByteArray());  
        }  
   
        public String getName() {  
            return name;  
        }  
   
        public OutputStream getOutputStream() throws IOException {  
            throw new IOException("Cannot write to this read-only resource");  
        }  
    }  

	
}
