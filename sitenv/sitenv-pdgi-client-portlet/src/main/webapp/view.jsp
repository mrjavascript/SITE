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

<portlet:actionURL var="sendQueryUrl">
    <portlet:param name="mvcPath" value="/view.jsp" />
</portlet:actionURL>

<aui:form action="<%= sendQueryUrl %>" method="post">
  <aui:input label="Override WSDL URL" name="endpointUrl" type="text" value="" />
  
  <aui:select label="" name="wsdl">
     <aui:option value="modSpec">ModSpec 1.0</aui:option>
     <aui:option value="ihehpd">IHE HPD with CP 601</aui:option>
     <aui:option value="ehriwg">EHR-IWG HPD+ 1.1</aui:option>
  </aui:select>
 
  <aui:input label="Base DN" name="baseDn" type="text" value="" />
 
  <aui:select label="Select Test Case" name="searchType">
    <aui:option value="Providers">Search Provider By Name</aui:option>
    <aui:option value="Organizations">Search Organization by Id</aui:option>
    <aui:option value="Memberships">Search Membership by Id</aui:option>
    <aui:option value="Services">Search Service by Id</aui:option>
    <aui:option value="Credentials">Search Credential by Id</aui:option>
  </aui:select>
  
  <aui:input label="Search Attribute" name="searchAttribute" type="text" value="" />
  
  <aui:input label="Search String" name="searchString" type="text" value="" />
  
  <aui:button type="submit" value="Send Query" /> 
</aui:form> 

<portlet:renderURL var="viewUrl">
        <portlet:param name="mvcPath" value="/view.jsp" />
</portlet:renderURL>
