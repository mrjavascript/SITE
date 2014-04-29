<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>

<%@ page import="org.sitenv.portlets.singletest.SingleTestPortlet" %>

<portlet:defineObjects />

<portlet:resourceURL var="runTestsUrl">
    <portlet:param name="mvcPath" value="/view.jsp" />
</portlet:resourceURL>


<script type="text/javascript">
	window.runTestsUrl = "<%= runTestsUrl %>";

	window.currentContextPath = "<%=request.getContextPath()%>";
</script>

<div id="providerDirectoryWidget" class="panel panel-default">
      <div class="panel-heading"><h3 class="panel-title">Provider Directory Server Test Cases</h3></div>
  		<div class="panel-body">
	
	<h4>Directions</h4>
      		
     <p>To execute test cases against your provider directory server implementation, please enter the publicly available WSDL URL for your PD endpoint, enter the base DN for your PD implementation, and select the test case that you wish to execute.<br/><br/>Please note: the test cases may take up to one minute to run.</p>
      	
<div class="well">
  <form action="<%= runTestsUrl %>" name="testForm" method="post" id="providerDirectoryTestForm">
      
<p>
	  <label for="endpointUrl">Enter Your Endpoint URL:</label>
      <input id="endpointUrl" name="endpointUrl" type="text" value="" class="validate[required,custom[url]]"/>
</p>
<p>      
      <label for="baseDn">Enter Your Base DN:</label>
      <input id="baseDn" name="baseDn" type="text" value="" class="validate[required]"/>
</p>
<p>      
      <label for="testCase">Select a Test Case:</label>
      <select id="testCase" name="testCase" class="validate[required]">
        <option value="run_all_test_cases">Run All Test Cases</option>
        <% for (String testCase : SingleTestPortlet.testCaseNames) { %>
        	<option value="<%= testCase %>"><%= SingleTestPortlet.testCaseRealNames.get(testCase) %></option>
        <% } %>
      </select>
</p>
<p> 
	<hr/>     
      <button id="querySubmit" type="submit" class="btn btn-primary start" onclick="return false;">
      	<span class="glyphicon glyphicon-ok"></span>
          <span>Run Test Case</span>
      </button>

</p>
  </form> 

  </div>
  </div>
</div>


<div class="panel panel-default">
      <div class="panel-heading"><h3 class="panel-title">Provider Directory Client Testing</h3></div>
  		<div class="panel-body">

	PD clients that would like to verify their systems are generating conformant PD search requests following the ONC Modular Specifications can issue requests against the Provider Directory Test Implementation (PDTI) setup at the following WSDL:<br /><br />
http://54.201.181.21/pdti-server/Hpd_Plus_ProviderInformationDirectoryService?wsdl<br /><br />
The PDTI has the test data loaded as specified above, and clients can verify the results, based on their search requests, by manually cross-checking results against the test data.
  </div>
</div>


<div class="modal modal-wide fade" id="resultsDialog" tabindex="-1" role="dialog" aria-labelledby="resultModalLabel" aria-hidden="true">
	      	<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4>Provider Directory Server Test Results</h4>
				</div>
				<div class="modal-body" id="PDResult">
				</div>
				<div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close Results</button>
				</div>
			</div>
			</div>
			</div>

<portlet:renderURL var="viewUrl">
        <portlet:param name="mvcPath" value="/view.jsp" />
</portlet:renderURL>
