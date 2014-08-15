package org.sitenv.portlets.providerdirectory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class SingleTestPortlet extends MVCPortlet {
    //private static Logger logger = Logger.getLogger(SingleTestPortlet.class);
	private final Log logger = LogFactoryUtil.getLog(SingleTestPortlet.class); 
	
    public static List<String> testCaseNames;
    public static Map<String, String> testCaseRealNames;
    static {
    	testCaseNames = new ArrayList<String> ();
    	testCaseNames.add("search_provider_by_name");
    	testCaseNames.add("search_org_by_id");
    	testCaseNames.add("search_membership_by_provider");
    	testCaseNames.add("search_service_by_id");
    	testCaseNames.add("search_credential_by_id");
    	testCaseNames.add("Find_Individual");
    	testCaseNames.add("Find_Unique_Individual");
    	testCaseNames.add("Find_Organization");
    	testCaseNames.add("Find_Unique_Organization");
    	testCaseNames.add("Find_Organizations_for_Unique_Individual");
    	testCaseNames.add("Find_Individuals_for_Unique_Organization");
    	testCaseNames.add("Find_Individuals_and_Organizations");
    	testCaseNames.add("dup_req_id_federation_loop_test");
    	
    	testCaseRealNames = new HashMap<String, String>();
    	testCaseRealNames.put("search_provider_by_name", "Search Provider by Name");
    	testCaseRealNames.put("search_org_by_id", "Search Organization by Id");
    	testCaseRealNames.put("search_membership_by_provider", "Search Membership by Id");
    	testCaseRealNames.put("search_service_by_id", "Search Service by Id");
    	testCaseRealNames.put("search_credential_by_id", "Search Credential by Id");
    	testCaseRealNames.put("Find_Individual", "Find Individual");
    	testCaseRealNames.put("Find_Unique_Individual", "Find Unique Individual");
    	testCaseRealNames.put("Find_Organization", "Find Organization");
    	testCaseRealNames.put("Find_Unique_Organization", "Find Unique Organization");
    	testCaseRealNames.put("Find_Organizations_for_Unique_Individual", "Find Organizations for Unique Individual");
    	testCaseRealNames.put("Find_Individuals_for_Unique_Organization", "Find Individuals for Unique Organization");
    	testCaseRealNames.put("Find_Individuals_and_Organizations", "Find Individuals and Organizations");
    	testCaseRealNames.put("dup_req_id_federation_loop_test", "Federation Loop Test");
    	testCaseRealNames.put("dup_req_id_federation_loop_test_hpdplus", "Federation Loop Test");
    	
    }

    
}
