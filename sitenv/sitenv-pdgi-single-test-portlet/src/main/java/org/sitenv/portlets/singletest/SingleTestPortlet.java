package org.sitenv.portlets.singletest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;

import org.apache.xmlbeans.XmlException;

import com.eviware.soapui.impl.settings.XmlBeansSettingsImpl;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestRequestStepResult;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.settings.HttpSettings;
import com.eviware.soapui.settings.WsdlSettings;
import com.eviware.soapui.support.SoapUIException;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class SingleTestPortlet extends MVCPortlet {
    private static final String PASSED = "PASSED";
    private static final String FAILED = "FAILED";
    private static final String FINISHED = "FINISHED";
    private static final String BASE_DN_PROPERTY = "project.test.server.dsml.dn.base";
    private static final String URL_PROPERTY = "project.test.server.wsdl.url";
    private static final String NONE = "none";
    private static final String MSPD_SOAPUI_PROJECT_FILE = "soapui-project_hpdplus.xml";
    private static final String IHE_SOAPUI_PROJECT_FILE = "soapui-project.xml";

    private static final WsdlProject IHE_WSDL_PROJECT;
    static {
    	IHE_WSDL_PROJECT = getWsdlProject(IHE_SOAPUI_PROJECT_FILE);
    }
    private static final WsdlProject MSPD_WSDL_PROJECT;
    static {
    	MSPD_WSDL_PROJECT = getWsdlProject(MSPD_SOAPUI_PROJECT_FILE);
    }

    private static List<String> testCaseNames;
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
    	//testCaseNames.add("dup_req_id_federation_loop_test");
    }

    private static WsdlProject getWsdlProject(final String projectFile) {
        WsdlProject wsdlProject = null;
        try {
            wsdlProject = new WsdlProject(getFileUrl(projectFile));
            XmlBeansSettingsImpl xmlBeansSettingsImpl = wsdlProject.getSettings();
            xmlBeansSettingsImpl.setString(HttpSettings.CLOSE_CONNECTIONS, Boolean.TRUE.toString());
            xmlBeansSettingsImpl.setString(HttpSettings.INCLUDE_REQUEST_IN_TIME_TAKEN, Boolean.TRUE.toString());
            xmlBeansSettingsImpl.setString(HttpSettings.INCLUDE_RESPONSE_IN_TIME_TAKEN, Boolean.TRUE.toString());
            xmlBeansSettingsImpl.setString(WsdlSettings.CACHE_WSDLS, Boolean.FALSE.toString());
            xmlBeansSettingsImpl.setString(WsdlSettings.PRETTY_PRINT_PROJECT_FILES, Boolean.TRUE.toString());
            xmlBeansSettingsImpl.setString(WsdlSettings.PRETTY_PRINT_RESPONSE_MESSAGES, Boolean.TRUE.toString());
        } catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SoapUIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return wsdlProject;
    }

    private static String getFileUrl(final String fileName) {
        return SingleTestPortlet.class.getClassLoader().getResource(fileName).toString();
    }
	
    private Map<String, TestCase> buildTestCaseMap(final WsdlProject wsdlProject) {
        Map<String, TestCase> testCaseMap = new HashMap<String, TestCase>();
        List<TestSuite> testSuitesList = wsdlProject.getTestSuiteList();
        for(TestSuite testSuite : testSuitesList) {
            List<TestCase> testCaseList = testSuite.getTestCaseList();
            for(TestCase testCase : testCaseList) {
                testCaseMap.put(testCase.getName(), testCase);
            }
        }
        return testCaseMap;
    }

    @Override
	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
    		throws IOException, PortletException {

		WsdlProject project = null;
   	
		String wsdl = actionRequest.getParameter("wsdl");
		if (wsdl.equals("modSpec")) {
			project = MSPD_WSDL_PROJECT;			
		} else if (wsdl.equals("ihehpd")) {
		    project = IHE_WSDL_PROJECT;			
		} else {
			project = null;
		}
		
		String wsdlUrl = actionRequest.getParameter("endpointUrl");
		if (wsdlUrl == null || wsdlUrl.equals("")) {
    		PortletSession session = actionRequest.getPortletSession();
    	    session.setAttribute("LIFERAY_SHARED_testStatus", "FAILED", PortletSession.APPLICATION_SCOPE);
    	    List<String> results = new ArrayList<String> ();
    	    results.add("You must provide an endpoint URL");
    	    session.setAttribute("LIFERAY_SHARED_whichPage", "error", PortletSession.APPLICATION_SCOPE);
    	    session.setAttribute("LIFERAY_SHARED_resultList", results, PortletSession.APPLICATION_SCOPE);
    	    return;
		}
		
		String baseDn = actionRequest.getParameter("baseDn");
		if (baseDn == null || baseDn.equals("")) {
    		PortletSession session = actionRequest.getPortletSession();
    	    session.setAttribute("LIFERAY_SHARED_testStatus", "FAILED", PortletSession.APPLICATION_SCOPE);
    	    List<String> results = new ArrayList<String> ();
    	    results.add("You must provide an base DN");
    	    session.setAttribute("LIFERAY_SHARED_whichPage", "error", PortletSession.APPLICATION_SCOPE);
    	    session.setAttribute("LIFERAY_SHARED_resultList", results, PortletSession.APPLICATION_SCOPE);
    	    return;
		}
		
        project.setPropertyValue(URL_PROPERTY, wsdlUrl);
		project.setPropertyValue(BASE_DN_PROPERTY, baseDn);		
		
		String testCaseName = actionRequest.getParameter("testCase");
		if (testCaseName.equals("run_all_test_cases")) {
			runAllTests(actionRequest, project);
			
		} else {
		
			try {
	            List<String> testResultList = new ArrayList<String> ();
								
				Map<String, TestCase> testCases = buildTestCaseMap(project);

				TestCase tc = testCases.get(testCaseName);
				TestCaseRunner testCaseRunner = tc.run(null, false);
				
	            String testStatus = testCaseName + " - " + FAILED;
	            if(FINISHED.equals(testCaseRunner.getStatus().toString())) {
	                testStatus = testCaseName + " - " + PASSED;
	            }

	            List<TestStepResult> testStepResultList = testCaseRunner.getResults();
	            
                String requestContent = null;
                String responseContent = null;
                for(TestStepResult testStepResult : testStepResultList) {
                    WsdlTestRequestStepResult wsdlTestRequestStepResult = null;
                    if(testStepResult instanceof WsdlTestRequestStepResult) {
                        wsdlTestRequestStepResult = (WsdlTestRequestStepResult)testStepResult;
                        requestContent = wsdlTestRequestStepResult.getRequestContentAsXml();
                        responseContent = wsdlTestRequestStepResult.getResponseContentAsXml();
                    }
                    String[] messages = testStepResult.getMessages();
                    for(String message : messages) {
                    	testResultList.add(message);
                    }
                    if(0 == testResultList.size()) {
                    	testResultList.add(NONE);
                    }
                }
	    		
	    		PortletSession session = actionRequest.getPortletSession();
	    	    session.setAttribute("LIFERAY_SHARED_whichPage", "server", PortletSession.APPLICATION_SCOPE);
	    	    session.setAttribute("LIFERAY_SHARED_testStatus", testStatus, PortletSession.APPLICATION_SCOPE);
	    	    session.setAttribute("LIFERAY_SHARED_resultList", testResultList, PortletSession.APPLICATION_SCOPE);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        super.processAction(actionRequest, actionResponse);
    }

    public void runAllTests(ActionRequest actionRequest, WsdlProject project)
    		throws IOException, PortletException {
		
		Map<String, TestCase> testCases = buildTestCaseMap(project);
		
		Map<String, List<String>> testResults = new HashMap<String, List<String>> ();
		for (String testCaseName : testCaseNames) {
			System.out.print(testCaseName + " - ");
			TestCase tc = testCases.get(testCaseName);				
			TestCaseRunner testCaseRunner = tc.run(null, false);
            String testStatus = FAILED;
            System.out.println (testCaseRunner.getStatus());
            if(FINISHED.equals(testCaseRunner.getStatus().toString())) {
                testStatus = PASSED;
            }
            List<TestStepResult> testStepResultList = testCaseRunner.getResults();
            
            List<String> testResultList = new ArrayList<String> ();
            testResultList.add(testStatus);
            for (TestStepResult res : testStepResultList) {
            	String[] mes = res.getMessages();
            	for (String m : mes) {
            		testResultList.add(m);
            	}
            }			
            testResults.put(testCaseName, testResultList);
		}
		
		PortletSession session = actionRequest.getPortletSession();
	    //session.setAttribute("LIFERAY_SHARED_testStatus", testStatus, PortletSession.APPLICATION_SCOPE);
	    session.setAttribute("LIFERAY_SHARED_whichPage", "server", PortletSession.APPLICATION_SCOPE);
	    session.setAttribute("LIFERAY_SHARED_testResultMap", testResults, PortletSession.APPLICATION_SCOPE);
    }
}
