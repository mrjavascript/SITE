package com.liferay.config;
 
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
 
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
 
/**
 * <a href="ConfigurationActionImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Eric Tang
 *
 */
public class ConfigurationActionImpl implements ConfigurationAction {
 
    public void processAction(PortletConfig portletConfig,
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {
 
    	System.out.println("HIT");
    	
        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);
 
        if (!cmd.equals(Constants.UPDATE)) {
            return;
        }
 
        String email = ParamUtil.getString(actionRequest, "fromendpoint");
        String hostname= ParamUtil.getString(actionRequest, "smtphost");
        String port = ParamUtil.getString(actionRequest, "smtpport");
        String username = ParamUtil.getString(actionRequest, "smtpuser");
        String password = ParamUtil.getString(actionRequest, "smptpswrd");
        String enablessl = ParamUtil.getString(actionRequest, "enablessl");
        String portletResource = ParamUtil.getString(actionRequest, "portletResource");
        String serviceContext = ParamUtil.getString(actionRequest, "serviceContext");
        PortletPreferences preferences = PortletPreferencesFactoryUtil.getPortletSetup(actionRequest, portletResource);
 
        preferences.setValue("Directfromendpoint", email);
        preferences.setValue("SMTPserverhostname", hostname);
        preferences.setValue("SMTPserverport", port);
        preferences.setValue("SMTPauthusername", username);
        preferences.setValue("SMTPauthpassword", password);
        preferences.setValue("EnableSSL", enablessl);
        preferences.setValue("ServiceContext", serviceContext);
        
        preferences.store();
 
        PortletSession portletSession = actionRequest.getPortletSession();
        
        SessionMessages.add(actionRequest, portletConfig.getPortletName()+ ".doConfigure");
    }
 
    public String render(PortletConfig portletConfig,
            RenderRequest renderRequest, RenderResponse renderResponse)
            throws Exception {
 
        return "/Configuration.jsp";
    }
 
}