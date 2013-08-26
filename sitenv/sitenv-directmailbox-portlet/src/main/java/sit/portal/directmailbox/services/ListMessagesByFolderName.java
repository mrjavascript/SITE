package sit.portal.directmailbox.services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.jtds.jdbc.DateTime;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import sit.portal.utl.DesEncrypter;


public class ListMessagesByFolderName extends HttpServlet{

	private static final String HOST_FLDNAME = "host";
	private static final String USERNAME_FLDNAME = "username";
	private static final String PASSWORD_FLDNAME = "password";
	private static final String PROVIDER_FLDNAME = "provider";
	private static final String FOLDER_FLDNAME = "foldername";
	private static final String ENCRYPTEDKEY = "sitplatform@1234";
	
	private static Logger _log = Logger.getLogger(HttpServlet.class);
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		String path = this.getServletContext().getRealPath("") + File.separator;
	    out.println("ListMessageByFolderName is running....");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Properties props = new Properties();
		ArrayList<EmailMetaData> emails = new ArrayList<EmailMetaData>();
		PrintWriter out = response.getWriter();
		
		try
	    {
			String passwordMask = request.getParameter(PASSWORD_FLDNAME);
			String host = request.getParameter(HOST_FLDNAME);
			String username = request.getParameter(USERNAME_FLDNAME);
			String password = new DesEncrypter(ENCRYPTEDKEY).decrypt(passwordMask);
			String provider =  request.getParameter(PROVIDER_FLDNAME);
			String foldername = request.getParameter(FOLDER_FLDNAME);
			
			
			  //Connect to the server
		      Session session = Session.getDefaultInstance(props, null);
		      Store store     = session.getStore(provider);
		      store.connect(host, username, password);
	      
	      /*
	      Folder[] f = store.getDefaultFolder().list();
	      for(Folder fd:f)
	          System.out.println(">> "+fd.getName());
	      */
	      //SimpleDateFormat df1 = new SimpleDateFormat( "MM/dd/yy" );
	      /*
	      Date currentDate = new Date();
	      Calendar c = Calendar.getInstance();
	      c.setTime(currentDate);
	      c.add(Calendar.HOUR, -12);
	      SearchTerm st = new ReceivedDateTerm(ComparisonTerm.EQ, c.getTime());
	        */
	      
	      //pop3 has only 1 inbox.
	      Folder inbox = store.getFolder("INBOX");
	      inbox.open(Folder.READ_ONLY);
	      
	      // get a list of javamail messages as an array of messages
	      //Message[] messages = inbox.search(st);
	      Message[] messages = inbox.getMessages();
	      ArrayList<Message> sortMessages = new ArrayList<Message>(Arrays.asList(messages));
	      
	      //find the timeline.
	      /*
	      Date currentDate = new Date();
	      Calendar c = Calendar.getInstance();
	      c.setTime(currentDate);
	      c.add(Calendar.DATE, -2);

	      for (Iterator<Message> it= sortMessages.iterator(); it.hasNext();) {
	    	   Message current = it.next();
	    	   Date d = current.getSentDate();
	    	   if(d!=null && d.before(c.getTime())) 
	    			it.remove();
	      }
	      */
	      Collections.sort(sortMessages, new MessageComparable());
	      
	      /*
	      for (Iterator<Message> it= sortMessages.iterator(); it.hasNext();) {
	    	   Message current = it.next();
	    	   String subject = current.getSubject();
	    	   if(subject!=null&&(subject.contains("Processed: SITE direct email test") || 
	    			   subject.contains("Undeliverable: SITE direct email test")))
	    	        it.remove();
			}
			*/
	      
	      
	      
	      for(int i = 0; i < Math.min(messages.length ,20) ; i++)
	      {
	    	EmailMetaData email = new EmailMetaData();
	    	
	    	//System.out.println("\r\n*****************************************************************************");
	        //System.out.println("MESSAGE " + (i + 1) + ":");
	        Message msg =  sortMessages.get(i);
	        //System.out.println(msg.getMessageNumber());
	        //Object String;
	        //System.out.println(folder.getUID(msg)
	
	        String subject = msg.getSubject();
	        //System.out.println("Subject: " + subject);
	        email.setSubject(subject);
	        //System.out.println("From: " + msg.getFrom()[0]);
	        email.setFrom(msg.getFrom()[0].toString());
	        //System.out.println("To: "+msg.getAllRecipients()[0]);
	        email.setTo(msg.getAllRecipients()[0].toString());
	        Date sentDate = msg.getSentDate();
	        String dateTimeStr = sentDate!=null?(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z")).format(msg.getSentDate()) : "N/A";
	        //System.out.println("Date received: " + dateTimeStr);
	        email.setDateReceived(dateTimeStr);
	        //System.out.println("Size: " + msg.getSize());
	        email.setSize(msg.getSize());
	        	        
	        String attachmentName = null;
	        //check the type of the content.
	        Object content = messages[i].getContent();
	        
	        ArrayList<String> attachmentNames = new ArrayList<String>();
	        
			if(content instanceof Multipart)
			{
				Multipart mp = (Multipart)content;
				//get the disposition.
				for (int j=0; j < mp.getCount(); j++) {
					Part part = mp.getBodyPart(j);
					String disposition = msg.getDisposition();
			        if (disposition == null) {
						// Check if plain
						MimeBodyPart mbp = (MimeBodyPart)part;
						if (mbp.isMimeType("text/plain")) {
							//do nothing.
						} else {
							//System.out.println("Attachment: " + part.getFileName());
							attachmentNames.add(part.getFileName());
						}
					} 
			        else if ((disposition != null) && (disposition.equals(Part.ATTACHMENT) || disposition.equals(Part.INLINE) )){
						// Check if plain
						MimeBodyPart mbp = (MimeBodyPart)part;
						if (mbp.isMimeType("text/plain")) {
							//do nothing..
						} else {
							//System.out.println("Attachment"+ (j+1) + ":" + part.getFileName());
							attachmentNames.add(part.getFileName());
						}
					}
				}//end loop attachment.
			}
			 
			if(attachmentNames.size()!=0)
				for (String name : attachmentNames)
				{
					if(name!=null)
						attachmentName = (attachmentName!=null)? (attachmentName + "," + name) : name;
				}	
		    //System.out.println(msg.getFlags());
	        //System.out.println("Body: \n"+ msg.getContent());
	        //System.out.println("Content type:" + msg.getContentType());
			//System.out.println("Attachment"+ attachmentName);
			email.setHasAttachment(attachmentName != null);
			email.setAttachmentName(attachmentName);
			emails.add(email);
	      }//end of loop message.
	      //close the inbox folder but do not remove the messages from the server
	      inbox.close(false);
	      store.close();
	      
	      
		}
	    catch (NoSuchProviderException nspe)
	    {
	    	_log.error("invalid provider name", nspe);
	    }
	    catch (MessagingException me)
	    {
	    	_log.error("messaging exception", me);
	    }
		catch(Exception exp)
		{
			_log.error("listing inbox service crashed", exp);
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(emails);
	    out.println(json);
	}
	
	private class MessageComparable implements Comparator<Message>{
		 
	    @Override
	    public int compare(Message o1, Message o2) {
	    	try{
	    		Date a = o1.getSentDate();
		    	Date b = o2.getSentDate();
		        return b.compareTo(a);
	    	}catch(Exception exp)
	    	{
	    		return 0;
	    	}
	    }
	} 
}
