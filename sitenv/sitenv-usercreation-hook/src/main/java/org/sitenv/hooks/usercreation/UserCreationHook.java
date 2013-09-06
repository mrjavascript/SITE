package org.sitenv.hooks.usercreation;


import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.model.BaseModelListener;
import com.liferay.portal.model.User;
import com.liferay.util.mail.MailEngine;
import com.liferay.util.mail.MailEngineException;

public class UserCreationHook extends BaseModelListener<User> {
	
	private static Logger _log = Logger.getLogger(UserCreationHook.class);
	
	public void onAfterCreate(User model) throws ModelListenerException {
			_log.trace("intercept new user request.");
			User newUser = model;
	       
			StringBuilder templatesb = new StringBuilder();
			templatesb.append("<h1><span style=\"font-size:14px;\">Dear Administrators,</span></h1><p>");
			templatesb.append("A new user has applied for membership of SIT portal,</p>");
			templatesb.append("<p>User Email:{@EMAIL}</p>");
			templatesb.append("<p>User Screen Name:{@USERNAME}</p>");
			templatesb.append("<p>User First Name:{@FIRSTNAME}</p>");
			templatesb.append("<p>User Last Name:{@LASTNAME}</p>");
			templatesb.append("<p>Please review the request and add user to the SIT platform site.</p>");
	       
			
			MailMessage mailMessage = new MailMessage();
	       
			mailMessage.setHTMLFormat(true);
	        mailMessage.setBody(
	     		   templatesb.toString()
	     		   		.replace("{@EMAIL}", newUser.getEmailAddress())
	     		   		.replace("{@USERNAME}",newUser.getScreenName())
	     		   		.replace("{@FIRSTNAME}", newUser.getFirstName())
	     		   		.replace("{@LASTNAME}", newUser.getLastName())
	     		   );
	        mailMessage.setHTMLFormat(true);
	        try {
	     	  mailMessage.setFrom(new InternetAddress("tangyedong@gmail.com","SIT SYS"));
	     	  mailMessage.setSubject("New user has been created(SITPlatform).");
	     	  mailMessage.setTo(new InternetAddress[]{
	     			 new InternetAddress("nagesh.bashyam@drajer.com"),
	     			 new InternetAddress("bdyer@pyramedresearch.com"),
	     			 new InternetAddress("etang@ainq.com")
	     	  });
	     	  _log.trace("message constructed");
        }
        catch (Exception e)
        {
        	_log.error("failed to generate email", e);
        }
       	 
        try {
			MailEngine.send(mailMessage);
			_log.trace("Email sent");
		} catch (MailEngineException e) {
			// TODO Auto-generated catch block
			_log.error("Error when sending email", e);
		}
	}
}

