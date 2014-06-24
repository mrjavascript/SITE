package org.sitenv.servlets.providerdirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.Servlet;
//import javax.portlet.PortletSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.sitenv.statistics.dto.PdtiTestCase;
import org.sitenv.statistics.manager.StatisticsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

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

/**
 * Servlet implementation class SingleTestServlet
 */
//@WebServlet("/SingleTestServlet")
public class SingleTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String BASE_DN_PROPERTY = "project.test.server.dsml.dn.base";
    private static final String URL_PROPERTY = "project.test.server.wsdl.url";
    private static final String MSPD_SOAPUI_PROJECT_FILE = "soapui-project_hpdplus.xml";
    private static final String IHE_SOAPUI_PROJECT_FILE = "soapui-project.xml";

    private static WsdlProject IHE_WSDL_PROJECT;
    private static WsdlProject MSPD_WSDL_PROJECT;

	private static Logger logger = Logger.getLogger(SingleTestServlet.class);
	//private final Log logger = LogFactoryUtil.getLog(SingleTestServlet.class); 
	
	@Autowired
	private StatisticsManager statisticsManager;
	
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
    
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleTestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);  // call this first, any failure here will stop the SoapUI from being initialized
		
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
		
		SoapUI.initDefaultCore();

    	SoapUI.getSettings().setBoolean(HttpSettings.LEAVE_MOCKENGINE, false);
    	
		SoapUI.getThreadPool().setCorePoolSize(0); // allocate a core pool size of 0
		SoapUI.getThreadPool().setMaximumPoolSize(25);  // as threads are needed we can allocate up to 25 threads
		SoapUI.getThreadPool().setKeepAliveTime(0, TimeUnit.SECONDS);  // any threads over the core pool size will be deallocated immediately upon completion.
		
		IHE_WSDL_PROJECT = getWsdlProject(getFileUrl(IHE_SOAPUI_PROJECT_FILE));
		MSPD_WSDL_PROJECT = getWsdlProject(getFileUrl(MSPD_SOAPUI_PROJECT_FILE));
		
	
	}


	 

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		serveResource(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		serveResource(request, response);
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
        return SingleTestServlet.class.getClassLoader().getResource("/" + fileName).getPath();
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
    
    public void serveResource(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		WsdlProject project = null;
		String wsdl = "modSpec";

		
		
		
		
		// we are only dealing with ModSpec for this version...
		//if (wsdl.equals("modSpec")) {
			project = MSPD_WSDL_PROJECT;			
		//} else if (wsdl.equals("ihehpd")) {
		//    project = IHE_WSDL_PROJECT;			
		//} else {
		//	project = null;
		//}
		
		String testCaseName = request.getParameter("testCase").trim();

		String wsdlUrl = request.getParameter("endpointUrl").trim();
		if (wsdlUrl == null || wsdlUrl.equals("")) {
			logger.info("The endpoint URL was not provided");
    		//PortletSession session = request.getPortletSession();
			HttpSession session = request.getSession();
			
    	    List<TestCaseResultWrapper> results = new ArrayList<TestCaseResultWrapper> ();
    	    TestCaseResultWrapper wrapper = new TestCaseResultWrapper();
    	    wrapper.setName(testCaseName);
    	    wrapper.setStatus("FAILED");
    	    wrapper.setRequest("");
    	    wrapper.setResponse("You must provide an endpoint URL");
    	    results.add(wrapper);
    	    
    	    //session.setAttribute("LIFERAY_SHARED_resultList", results, PortletSession.APPLICATION_SCOPE);
    	    session.setAttribute("resultList", results);
    	    
            //getPortletContext().getRequestDispatcher("/testcase_results.jsp").include(request, response);
            getServletContext().getRequestDispatcher("/testcase_results.jsp").include(request, response);
    	    return;
		}
		
		String baseDn = request.getParameter("baseDn").trim();
		if (baseDn == null || baseDn.equals("")) {
			logger.info("The Base DN was not provided");
			
    		//PortletSession session = request.getPortletSession();
			HttpSession session = request.getSession();
    		
    		
    		List<TestCaseResultWrapper> results = new ArrayList<TestCaseResultWrapper> ();
    	    TestCaseResultWrapper wrapper = new TestCaseResultWrapper();
    	    wrapper.setName(testCaseName);
    	    wrapper.setStatus("FAILED");
    	    wrapper.setRequest("");
            wrapper.setResponse("You must provide a Base DN");
            results.add(wrapper);
    	    
            //session.setAttribute("LIFERAY_SHARED_resultList", results, PortletSession.APPLICATION_SCOPE);
            session.setAttribute("resultList", results);
            
            //getPortletContext().getRequestDispatcher("/testcase_results.jsp").include(request, response);
            getServletContext().getRequestDispatcher("/testcase_results.jsp").include(request, response);
            
            return;
		}
		
        project.setPropertyValue(URL_PROPERTY, wsdlUrl);

    	project.setPropertyValue(BASE_DN_PROPERTY, baseDn);		

    	if (testCaseName.equals("run_all_test_cases")) {
    			runAllTests(wsdl, request, project);
    			
    	} else {
    			try {
    	            List<TestCaseResultWrapper> testResultList = new ArrayList<TestCaseResultWrapper> ();
    	            PdtiTestCase testCase = new PdtiTestCase();
    	 
    								
    				Map<String, TestCase> testCases = buildTestCaseMap(project);
    				
    				if (testCaseName.equals("dup_req_id_federation_loop_test") && wsdl.equals("modSpec")) {
    					testCaseName = "dup_req_id_federation_loop_test_hpdplus";
    				}
    				

    				TestCase tc = testCases.get(testCaseName);
    				
    				// default values for the test case
    				testCase.setTestCaseName(testCaseName);
    				testCase.setPass(false);
    				testCase.setHttpError(true);
    				
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
    	                    	    testCase.setHttpError(true);
	                        		testCase.setPass(false);
    	                    	}
    	                    	
    	                    	if (responseContent != null) {
    	                    		logger.info(responseContent);
    	                        	wrapper.setResponse(responseContent.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));                      	
    	                        	if (responseContent.contains("S:Fault") || responseContent.contains("dsml:errorResponse") || responseContent.contains("hpdplus:hpdPlusError")) {
    	                        	    logger.info("An error response was returned");
    	                        		wrapper.setStatus("FAILED");
    	                        		testCase.setHttpError(false);
    	                        		testCase.setPass(false);
    	                        	} else {
    		            	            if (wsdlTestRequestStepResult.getResponseStatusCode() != 200) {
    		            	            	logger.info("The server returned a http status of " + testCaseRunner.getStatus());
    		                        	    wrapper.setStatus("FAILED");
    		                        	    testCase.setHttpError(true);
    		                        	    testCase.setPass(false);
    		            	            } else {
    		                        	    wrapper.setStatus("PASSED");
    		                        	    
    		                        	    // we got back a successful response
    		                        	    testCase.setPass(true);
    		                        	    testCase.setHttpError(false);
    		            	            }
    	                        	}

    	                    	} else {
    	                    		logger.info("The response is null");
    	                    	    wrapper.setStatus("FAILED");
    	                        	wrapper.setResponse("Response is null");  

	                        	    testCase.setHttpError(true);
	                        	    testCase.setPass(false);
    	                    	}
    	                    	testResultList.add(wrapper);
    	                    }
    	                }
    				}
    	    		
    	    		//PortletSession session = request.getPortletSession();
    				HttpSession session = request.getSession();
    	    	    //session.setAttribute("LIFERAY_SHARED_resultList", testResultList, PortletSession.APPLICATION_SCOPE);
    				session.setAttribute("resultList", testResultList);
    				
    				ArrayList<PdtiTestCase> testList = new ArrayList<PdtiTestCase>();
    				testList.add(testCase);
    				statisticsManager.addPdtiTest(wsdlUrl,testList);

    			} catch (Exception e) {
    				e.printStackTrace();
                	TestCaseResultWrapper wrapper = new TestCaseResultWrapper();
            	    wrapper.setStatus("FAILED");
            	    wrapper.setRequest("");
            	    wrapper.setName(testCaseName);
                	wrapper.setResponse(e.getMessage());
    	            List<TestCaseResultWrapper> testResultList = new ArrayList<TestCaseResultWrapper> ();
    	            testResultList.add(wrapper);
    	            
    	    		//PortletSession session = request.getPortletSession();
    	            HttpSession session = request.getSession();
    	            
    	    	    //session.setAttribute("LIFERAY_SHARED_resultList", testResultList, PortletSession.APPLICATION_SCOPE);
    	            session.setAttribute("resultList", testResultList);
    		}
    	}
        //getPortletContext().getRequestDispatcher("/testcase_results.jsp").include(request, response);
    	
    	//ServletContext context = getServletContext();
    	
    	//getServletContext().getRequestDispatcher("/testcase_results.jsp").include(request, response);
    	request.getRequestDispatcher("/testcase_results.jsp").forward(request, response);
    }
    
    public void runAllTests(String wsdl, HttpServletRequest request, WsdlProject project)
    		throws IOException, ServletException {
		
		Map<String, TestCase> testCases = buildTestCaseMap(project);
		
		List<TestCaseResultWrapper> testResultList = new ArrayList<TestCaseResultWrapper>();
		List<PdtiTestCase> pdtiCases = new ArrayList<PdtiTestCase>();
		
		for (String testCaseName : testCaseNames) {
			PdtiTestCase pdtiCase = new PdtiTestCase();
			if (testCaseName.equals("dup_req_id_federation_loop_test") && wsdl.equals("modSpec")) {
				testCaseName = "dup_req_id_federation_loop_test_hpdplus";
			}
			pdtiCase.setTestCaseName(testCaseName);
			pdtiCase.setPass(false);
			pdtiCase.setHttpError(true);
			
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
	                	    pdtiCase.setHttpError(true);
                    		pdtiCase.setPass(false);
	                	}
	                	
	                	if (responseContent != null) {
	                		logger.info(responseContent);
	                    	wrapper.setResponse(responseContent.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));

	                    	if (responseContent.contains("S:Fault") || responseContent.contains("dsml:errorResponse") || responseContent.contains("hpdplus:hpdPlusError")) {
                        	    logger.info("An error response was returned");
	                    		wrapper.setStatus("FAILED");
	                    		pdtiCase.setHttpError(false);
                        		pdtiCase.setPass(false);
	                    	} else {
		        	            if (wsdlTestRequestStepResult.getResponseStatusCode() != 200) {
		        	            	logger.info("The server returned a http status of " + testCaseRunner.getStatus());
		                    	    wrapper.setStatus("FAILED");
		                    	    pdtiCase.setHttpError(true);
	                        		pdtiCase.setPass(false);
		        	            } else {
		                    	    wrapper.setStatus("PASSED");
		                    	    pdtiCase.setHttpError(false);
	                        		pdtiCase.setPass(true);
		        	            }
	                    	}
	                	} else {
	                		logger.info("The response is null");
	                	    wrapper.setStatus("FAILED");
	                    	wrapper.setResponse("Response is null");
	                    	pdtiCase.setHttpError(true);
                    		pdtiCase.setPass(false);
	                	}
	                	testResultList.add(wrapper);
	                	pdtiCases.add(pdtiCase);
	                }
	            }
			}
		}
		//PortletSession session = request.getPortletSession();
		HttpSession session = request.getSession();
	    //session.setAttribute("LIFERAY_SHARED_resultList", testResultList, PortletSession.APPLICATION_SCOPE);
        session.setAttribute("resultList", testResultList);
        
        statisticsManager.addPdtiTest(project.getPropertyValue(URL_PROPERTY),pdtiCases);
    }

	public StatisticsManager getStatisticsManager() {
		return statisticsManager;
	}

	public void setStatisticsManager(StatisticsManager statisticsManager) {
		this.statisticsManager = statisticsManager;
	}

	

	
	
}
