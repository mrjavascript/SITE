package org.sitenv.portlets.singletest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;

import com.eviware.soapui.SoapUI;
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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class SingleTestPortlet extends MVCPortlet {
    private static final String BASE_DN_PROPERTY = "project.test.server.dsml.dn.base";
    private static final String URL_PROPERTY = "project.test.server.wsdl.url";
    private static final String MSPD_SOAPUI_PROJECT_FILE = "soapui-project_hpdplus.xml";
    private static final String IHE_SOAPUI_PROJECT_FILE = "soapui-project.xml";

    private WsdlProject IHE_WSDL_PROJECT;
    private WsdlProject MSPD_WSDL_PROJECT;

	//private static Logger logger = Logger.getLogger(SingleTestPortlet.class);
	private final Log logger = LogFactoryUtil.getLog(SingleTestPortlet.class); 
	
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
    	testCaseNames.add("dup_req_id_federation_loop_test");
    }

    private WsdlProject getWsdlProject(final String projectFile) {
        WsdlProject wsdlProject = null;
        try {
            wsdlProject = new WsdlProject("file:" + projectFile);
            XmlBeansSettingsImpl xmlBeansSettingsImpl = wsdlProject.getSettings();
            xmlBeansSettingsImpl.setString(HttpSettings.CLOSE_CONNECTIONS, Boolean.TRUE.toString());
            xmlBeansSettingsImpl.setString(HttpSettings.INCLUDE_REQUEST_IN_TIME_TAKEN, Boolean.TRUE.toString());
            xmlBeansSettingsImpl.setString(HttpSettings.INCLUDE_RESPONSE_IN_TIME_TAKEN, Boolean.TRUE.toString());
            xmlBeansSettingsImpl.setString(WsdlSettings.CACHE_WSDLS, Boolean.FALSE.toString());
            xmlBeansSettingsImpl.setString(WsdlSettings.PRETTY_PRINT_PROJECT_FILES, Boolean.TRUE.toString());
            xmlBeansSettingsImpl.setString(WsdlSettings.PRETTY_PRINT_RESPONSE_MESSAGES, Boolean.TRUE.toString());
        } catch (XmlException e) {
			e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		} catch (SoapUIException e) {
			e.printStackTrace();
		}
        return wsdlProject;
    }

    private static String getFileUrl(String fileName) {
        return SingleTestPortlet.class.getClassLoader().getResource("/" + fileName).getPath();
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
    public void init() {
    	try {
    	    SoapUI.initDefaultCore();
    	    
    	    SoapUI.getThreadPool().setCorePoolSize(0); // allocate a core pool size of 0
            SoapUI.getThreadPool().setMaximumPoolSize(25);  // as threads are needed we can allocate up to 25 threads
            SoapUI.getThreadPool().setKeepAliveTime(0, TimeUnit.SECONDS);  // any threads over the core pool size will be deallocated immediately upon completion.

    	    IHE_WSDL_PROJECT = getWsdlProject(getFileUrl(IHE_SOAPUI_PROJECT_FILE));
    	    MSPD_WSDL_PROJECT = getWsdlProject(getFileUrl(MSPD_SOAPUI_PROJECT_FILE));
			super.init();
		} catch (PortletException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public void destroy() {
    	IHE_WSDL_PROJECT.release();
    	MSPD_WSDL_PROJECT.release();
    	
    	SoapUI.getThreadPool().shutdown();
        try {
            SoapUI.getThreadPool().awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SoapUI.shutdown();
    	super.destroy();
    }
    
    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {

		WsdlProject project = null;
   	
		String wsdl = request.getParameter("wsdl").trim();
		if (wsdl.equals("modSpec")) {
			project = MSPD_WSDL_PROJECT;			
		} else if (wsdl.equals("ihehpd")) {
		    project = IHE_WSDL_PROJECT;			
		} else {
			project = null;
		}
		
		String testCaseName = request.getParameter("testCase").trim();

		String wsdlUrl = request.getParameter("endpointUrl").trim();
		if (wsdlUrl == null || wsdlUrl.equals("")) {
			logger.info("The endpoint URL was not provided");
    		PortletSession session = request.getPortletSession();
    	    List<TestCaseResultWrapper> results = new ArrayList<TestCaseResultWrapper> ();
    	    TestCaseResultWrapper wrapper = new TestCaseResultWrapper();
    	    wrapper.setName(testCaseName);
    	    wrapper.setStatus("FAILED");
    	    wrapper.setRequest("");
    	    wrapper.setResponse("You must provide an endpoint URL");
    	    results.add(wrapper);
    	    session.setAttribute("LIFERAY_SHARED_resultList", results, PortletSession.APPLICATION_SCOPE);
            getPortletContext().getRequestDispatcher("/testcase_results.jsp").include(request, response);
    	    return;
		}
		
		String baseDn = request.getParameter("baseDn").trim();
		if (baseDn == null || baseDn.equals("")) {
			logger.info("The Base DN was not provided");
    		PortletSession session = request.getPortletSession();
    	    List<TestCaseResultWrapper> results = new ArrayList<TestCaseResultWrapper> ();
    	    TestCaseResultWrapper wrapper = new TestCaseResultWrapper();
    	    wrapper.setName(testCaseName);
    	    wrapper.setStatus("FAILED");
    	    wrapper.setRequest("");
            wrapper.setResponse("You must provide a Base DN");
            results.add(wrapper);
    	    session.setAttribute("LIFERAY_SHARED_resultList", results, PortletSession.APPLICATION_SCOPE);
            getPortletContext().getRequestDispatcher("/testcase_results.jsp").include(request, response);
    	    return;
		}
		
        project.setPropertyValue(URL_PROPERTY, wsdlUrl);
        
        /*String origBaseDn = project.getPropertyValue(BASE_DN_PROPERTY);
        if (!origBaseDn.equals(baseDn)) {
        	TestCaseResultWrapper wrapper = new TestCaseResultWrapper();
    	    wrapper.setStatus("FAILED");
    	    wrapper.setRequest("");
    	    wrapper.setName(testCaseName);
        	wrapper.setResponse("The server returned a null test case");                		
            List<TestCaseResultWrapper> testResultList = new ArrayList<TestCaseResultWrapper> ();
            testResultList.add(wrapper);
    		PortletSession session = request.getPortletSession();
    	    session.setAttribute("LIFERAY_SHARED_resultList", testResultList, PortletSession.APPLICATION_SCOPE);
        } else {*/

    		project.setPropertyValue(BASE_DN_PROPERTY, baseDn);		

    		if (testCaseName.equals("run_all_test_cases")) {
    			runAllTests(wsdl, request, project);
    			
    		} else {
    			try {
    	            List<TestCaseResultWrapper> testResultList = new ArrayList<TestCaseResultWrapper> ();
    								
    				Map<String, TestCase> testCases = buildTestCaseMap(project);
    				
    				if (testCaseName.equals("dup_req_id_federation_loop_test") && wsdl.equals("modSpec")) {
    					testCaseName = "dup_req_id_federation_loop_test_hpdplus";
    				}

    				TestCase tc = testCases.get(testCaseName);
    				if (tc == null) {
    					logger.info("Unable to locate the specified test case");
    	            	TestCaseResultWrapper wrapper = new TestCaseResultWrapper();
    	        	    wrapper.setStatus("FAILED");
    	        	    wrapper.setRequest("");
    	        	    wrapper.setName(testCaseName);
    	            	wrapper.setResponse("Unable to locate the specified test case");                		
    		            testResultList.add(wrapper);
    				} else {
    					TestCaseRunner testCaseRunner = tc.run(null, false);

    		            List<TestStepResult> testStepResultList = testCaseRunner.getResults();
    		            
    	                String requestContent = null;
    	                String responseContent = null;
    	                for(TestStepResult testStepResult : testStepResultList) {
    	                    WsdlTestRequestStepResult wsdlTestRequestStepResult = null;
    	                    if(testStepResult instanceof WsdlTestRequestStepResult) {
    	                        wsdlTestRequestStepResult = (WsdlTestRequestStepResult)testStepResult;
    	        	            
    	                        requestContent = wsdlTestRequestStepResult.getRequestContentAsXml();
    	                        responseContent = wsdlTestRequestStepResult.getResponseContentAsXml();
    	                    	TestCaseResultWrapper wrapper = new TestCaseResultWrapper();
    	                    	wrapper.setName(testCaseName);

    	                    	if (requestContent != null) {
    	                    		logger.info(requestContent);
    	                    	    wrapper.setRequest(requestContent.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
    	                    	} else {
    	                    		logger.info("The request was null");
    	                    	    wrapper.setStatus("FAILED");
    	                    	    wrapper.setRequest("Request is null");                		
    	                    	}
    	                    	
    	                    	if (responseContent != null) {
    	                    		logger.info(responseContent);
    	                        	wrapper.setResponse(responseContent.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));                      	
    	                        	if (responseContent.contains("S:Fault") || responseContent.contains("dsml:errorResponse") || responseContent.contains("hpdplus:hpdPlusError")) {
    	                        	    logger.info("An error response was returned");
    	                        		wrapper.setStatus("FAILED");
    	                        	} else {
    		            	            if (wsdlTestRequestStepResult.getResponseStatusCode() != 200) {
    		            	            	logger.info("The server returned a http status of " + testCaseRunner.getStatus());
    		                        	    wrapper.setStatus("FAILED");            	            	
    		            	            } else {
    		                        	    wrapper.setStatus("PASSED");            	            	
    		            	            }
    	                        	}

    	                    	} else {
    	                    		logger.info("The response is null");
    	                    	    wrapper.setStatus("FAILED");
    	                        	wrapper.setResponse("Response is null");                		
    	                    	}
    	                    	testResultList.add(wrapper);
    	                    }
    	                }
    				}
    	    		
    	    		PortletSession session = request.getPortletSession();
    	    	    session.setAttribute("LIFERAY_SHARED_resultList", testResultList, PortletSession.APPLICATION_SCOPE);

    			} catch (Exception e) {
    				e.printStackTrace();
                	TestCaseResultWrapper wrapper = new TestCaseResultWrapper();
            	    wrapper.setStatus("FAILED");
            	    wrapper.setRequest("");
            	    wrapper.setName(testCaseName);
                	wrapper.setResponse(e.getMessage());                		
    	            List<TestCaseResultWrapper> testResultList = new ArrayList<TestCaseResultWrapper> ();
    	            testResultList.add(wrapper);
    	    		PortletSession session = request.getPortletSession();
    	    	    session.setAttribute("LIFERAY_SHARED_resultList", testResultList, PortletSession.APPLICATION_SCOPE);
    			}
    		}
        //}
        getPortletContext().getRequestDispatcher("/testcase_results.jsp").include(request, response);
    }

    public void runAllTests(String wsdl, ResourceRequest request, WsdlProject project)
    		throws IOException, PortletException {
		
		Map<String, TestCase> testCases = buildTestCaseMap(project);
		
		List<TestCaseResultWrapper> testResultList = new ArrayList<TestCaseResultWrapper> ();
		for (String testCaseName : testCaseNames) {
			if (testCaseName.equals("dup_req_id_federation_loop_test") && wsdl.equals("modSpec")) {
				testCaseName = "dup_req_id_federation_loop_test_hpdplus";
			}
			TestCase tc = testCases.get(testCaseName);	
			if (tc == null) {
            	TestCaseResultWrapper wrapper = new TestCaseResultWrapper();
				logger.info("Unable to locate the specified test case");
        	    wrapper.setStatus("FAILED");
        	    wrapper.setRequest("");
        	    wrapper.setName(testCaseName);
            	wrapper.setResponse("Unable to locate the specified test case");                		
	            testResultList.add(wrapper);
			} else {
				TestCaseRunner testCaseRunner = tc.run(null, false);
	            List<TestStepResult> testStepResultList = testCaseRunner.getResults();
	            
	            String requestContent = null;
	            String responseContent = null;
	            for(TestStepResult testStepResult : testStepResultList) {
	                WsdlTestRequestStepResult wsdlTestRequestStepResult = null;
	                if(testStepResult instanceof WsdlTestRequestStepResult) {
	                    wsdlTestRequestStepResult = (WsdlTestRequestStepResult)testStepResult;
	                    requestContent = wsdlTestRequestStepResult.getRequestContentAsXml();
	                    responseContent = wsdlTestRequestStepResult.getResponseContentAsXml();
	                	TestCaseResultWrapper wrapper = new TestCaseResultWrapper();
	                	wrapper.setName(testCaseName);
	                	
	                	if (requestContent != null) {
	                		logger.info(requestContent);
	                	    wrapper.setRequest(requestContent.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
	                	} else {
	                		logger.info("The request is null");
	                	    wrapper.setStatus("FAILED");
	                	    wrapper.setRequest("Request is null");                		
	                	}
	                	
	                	if (responseContent != null) {
	                		logger.info(responseContent);
	                    	wrapper.setResponse(responseContent.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));

	                    	if (responseContent.contains("S:Fault") || responseContent.contains("dsml:errorResponse") || responseContent.contains("hpdplus:hpdPlusError")) {
                        	    logger.info("An error response was returned");
	                    		wrapper.setStatus("FAILED");
	                    	} else {
		        	            if (wsdlTestRequestStepResult.getResponseStatusCode() != 200) {
		        	            	logger.info("The server returned a http status of " + testCaseRunner.getStatus());
		                    	    wrapper.setStatus("FAILED");            	            	
		        	            } else {
		                    	    wrapper.setStatus("PASSED");            	            	
		        	            }
	                    	}
	                	} else {
	                		logger.info("The response is null");
	                	    wrapper.setStatus("FAILED");
	                    	wrapper.setResponse("Response is null");                		
	                	}
	                	testResultList.add(wrapper);
	                }
	            }
			}
		}
		
		PortletSession session = request.getPortletSession();
	    session.setAttribute("LIFERAY_SHARED_resultList", testResultList, PortletSession.APPLICATION_SCOPE);
    }
}
