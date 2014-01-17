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

<portlet:defineObjects />

<portlet:resourceURL var="runTestsUrl">
    <portlet:param name="mvcPath" value="/view.jsp" />
</portlet:resourceURL>

<script type="text/javascript">
  $(document).ready(function () {
	  $("#querySubmit").click(function () {
		  $("#resultsDialog").dialog({ "width": 640, "height": 480});
		  $("#resultsDialog").html("<div><strong>Sending Query...</strong></div>");
		  
		  $.ajax({
	          type: "POST",
	          url: "<%= runTestsUrl %>",
	          data: $("#<portlet:namespace/>testForm").serialize(),
	          success: function(data) {
	        	  $("#resultsDialog").html(data);
	          },
	          error: function(jqXHR, textStatus, errorThrown) {
	        	  $("#resultsDialog").html(jqXHR['responseText']);
	          }
	        });	  
	  });
  });
</script>

<article class="module width_full">
	<header><h3>Provider Directory Server Test Cases</h3></header>
	<div class="module_content">

  <aui:form action="<%= runTestsUrl %>" name="testForm" method="post" id="<portlet:namespace/>testForm">
      <aui:field-wrapper label="" name="wsdl">
        <aui:input inlineField="<%= true %>" checked="<%= true %>" inlineLabel="right" name="wsdl" type="radio" value="modSpec" label="ModSpec 1.0" />
      </aui:field-wrapper>
	
      <aui:input label="Enter your PD Endpoint URL" name="endpointUrl" type="text" value="" />
      <aui:input label="Base DN" name="baseDn" type="text" value="" />
      <aui:select label="Select Test Case" name="testCase">
        <aui:option value="run_all_test_cases">Run All Test Cases</aui:option>
        <aui:option value="search_provider_by_name">Search Provider by Name</aui:option>
        <aui:option value="search_org_by_id">Search Organization by Id</aui:option>
        <aui:option value="search_membership_by_provider">Search Membership by Id</aui:option>
        <aui:option value="search_service_by_id">Search Service by Id</aui:option>
        <aui:option value="search_credential_by_id">Search Credential by Id</aui:option>
        <aui:option value="Find_Individual">Find Individual</aui:option>
        <aui:option value="Find_Unique_Individual">Find Unique Individual</aui:option>
        <aui:option value="Find_Organization">Find Organization</aui:option>
        <aui:option value="Find_Unique_Organization">Find Unique Organization</aui:option>
        <aui:option value="Find_Organizations_for_Unique_Individual">Find Organizations for Unique Individual</aui:option>
        <aui:option value="Find_Individuals_for_Unique_Organization">Find Individuals for Unique Organization</aui:option>
        <aui:option value="Find_Individuals_and_Organizations">Find Individuals and Organizations</aui:option>
        <aui:option value="dup_req_id_federation_loop_test">Federation Loop Test</aui:option>
      </aui:select>
      
      <button id="querySubmit" type="submit" class="btn btn-primary start" onclick="return false;">
          <span>Run Test Case</span>
      </button>
  </aui:form> 

  </div>
</article>

<article class="module width_full">
	<header><h3>Provider Directory Client Testing</h3></header>
	<div class="module_content">
	PD clients that would like to verify their systems are generating conformant PD search requests following the ONC Modular Specifications can issue requests against the Provider Directory Test Implementation (PDTI) setup at the following WSDL:<br /><br />
http://54.201.181.21/pdti-server/Hpd_Plus_ProviderInformationDirectoryService?wsdl<br /><br />
The PDTI has the test data loaded as specified above, and clients can verify the results, based on their search requests, by manually cross-checking results against the test data.
  </div>
</article>

<div id="resultsDialog" title="Results">
</div>

<portlet:renderURL var="viewUrl">
        <portlet:param name="mvcPath" value="/view.jsp" />
</portlet:renderURL>
