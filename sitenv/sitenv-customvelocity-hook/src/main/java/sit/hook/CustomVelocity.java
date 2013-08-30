package sit.hook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.model.Layout;

import java.util.HashMap;
import java.util.Map;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.staging.StagingUtil;

public class CustomVelocity extends Action {

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ActionException {
		
		//System.out.println("CustomVelocity.run()");
		Map<String, Object> vmVariables = new HashMap<String, Object>();
		vmVariables.put("beap_1", "Pre service hook is working...");
		request.setAttribute(WebKeys.VM_VARIABLES, vmVariables);
		
		Boolean showDockBar = false;
		PermissionChecker pchecker = PermissionThreadLocal.getPermissionChecker();
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
		 if (themeDisplay != null) {        
			 Layout layout = themeDisplay.getLayout(); 
			 if (layout != null) {
				 
				 long groupId = layout.getGroupId();
				 //System.out.println("Group ID:" + Long.toString(groupId));
				 try {
					if(GroupLocalServiceUtil.getGroup(groupId).isStagingGroup()){
						 groupId = StagingUtil.getLiveGroupId(groupId);
					}
					
					if(pchecker.isGroupAdmin(groupId) || pchecker.isGroupOwner(groupId) || pchecker.isOmniadmin()) {
						showDockBar = true; 
					}
				} catch (PortalException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				} catch (SystemException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			 	 
			 }
		 }
		 
		 vmVariables.put("showDockBar", showDockBar);
		
			 
		 		 
		
	}

}
