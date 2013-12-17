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

<portlet:actionURL var="runTestsUrl">
    <portlet:param name="mvcPath" value="/view.jsp" />
</portlet:actionURL>

<aui:form action="<%= runTestsUrl %>" name="testForm" method="post">
  <aui:field-wrapper label="" name="wsdl">
    <aui:input inlineField="<%= true %>" checked="<%= true %>" inlineLabel="right" name="wsdl" type="radio" value="modSpec" label="ModSpec 1.0" />
    <aui:input inlineField="<%= true %>" inlineLabel="right" name="wsdl" type="radio" value="ihehpd" label="IHE HPD with CP 601" />
    <aui:input inlineField="<%= true %>" inlineLabel="right" name="wsdl" type="radio" value="ehriwg" label="EHR-IWG HPD+ 1.1" />
  </aui:field-wrapper>
	
  <aui:input label="Enter your PD Endpoint URL" name="endpointUrl" type="text" value="" />
  <aui:input label="Base DN" name="baseDn" type="text" value="" />
  <aui:select label="Select Test Case" name="testCase">
    <aui:option value="run_all_test_cases">Run All Test Cases</aui:option>
    <aui:option value="search_provider_by_name">Search Provider By Name</aui:option>
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
  <aui:button type="submit" value="Run Test Case"  /> 
</aui:form> 

<portlet:renderURL var="viewUrl">
        <portlet:param name="mvcPath" value="/view.jsp" />
</portlet:renderURL>
