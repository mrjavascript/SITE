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

<%@ page import="javax.portlet.PortletSession" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<portlet:defineObjects />

<%
    PortletSession ps = renderRequest.getPortletSession();
    String whichPage = (String)ps.getAttribute("LIFERAY_SHARED_whichPage", PortletSession.APPLICATION_SCOPE);
    
    if (whichPage != null) {
        if (whichPage.equals("server") || whichPage.equals("error")) {
            String testStatus = (String)ps.getAttribute("LIFERAY_SHARED_testStatus", PortletSession.APPLICATION_SCOPE);
    
            if (testStatus != null) {
%>
                <%= testStatus %><br />
<%  
            } 
    
            if (testStatus.contains("FAILED")) {
                List<String> testResultList = (List<String>)ps.getAttribute("LIFERAY_SHARED_resultList", PortletSession.APPLICATION_SCOPE);
    
                if (testResultList != null) {
                    for (String res : testResultList) {
%>
                        <%= res %><br /><br />
<%
	                }
                }   
            }
        } else if (whichPage.equals("client")) {
        
    	    Map<String, Map<String, String>> searchResultHeaders = (Map<String, Map<String, String>>)ps.getAttribute("LIFERAY_SHARED_searchResultHeaders", PortletSession.APPLICATION_SCOPE);    	    
    	    Map<String, Map<String, List<String>>> searchResults = (Map<String, Map<String, List<String>>>)ps.getAttribute("LIFERAY_SHARED_searchResults", PortletSession.APPLICATION_SCOPE);    	    
    	    List<String> searchErrors = (List<String>)ps.getAttribute("LIFERAY_SHARED_searchErrors", PortletSession.APPLICATION_SCOPE);    	    
    	    
    	    if (searchErrors != null && searchErrors.size () > 0) {
%>
                <div class="container">
                    <h3>Error:</h3>
                    <table class="table table-bordered">
<%
                    for (String err : searchErrors) {
%>
                       <tr class="error"><td><%= err %></td></tr>
<%
                    }
%>
                    </table>
                </div>
<%
    	    } else if (searchResults != null && searchResultHeaders != null) {
%>
                <div style="width:100%;">
<%
                if (searchResultHeaders.size() > 0) {
                    for (String key : searchResultHeaders.keySet()) {
                	    Map<String, String> headers = searchResultHeaders.get(key);
                	    Map<String, List<String>> results = searchResults.get(key);
                	
%>
                        <table class="table table-bordered">
                            <tr class="info"><td><strong>DIRECTORY ID: <%= headers.get("directory_id") %></strong></td></tr>
                            <tr class="info"><td><strong>DIRECTORY URI: <%= headers.get("directory_uri") %></strong></td></tr>
                            <tr class="info"><td><strong>DN: <%= headers.get("dn") %></strong></td></tr>
                        </table>
                        <table class="table table-bordered">
<%
                        for (String k : results.keySet()) {
%>
                            <tr><td><strong><%= k %></strong></td></tr>
<%
                            for (String val : results.get(k)) {
%>
                                <tr><td><div style="margin-left:50px;"><%= val %></div></td></tr>
<%
                            }
                        }
%>
                        </table>
<%
                    }
                } else {
%>
                    <table class="table table-bordered">
                        <tr><td><strong>No Results Found</strong></td></tr>
                    </table>
<%
            	}
%>
                </div>
<%
            }
        }
    }
%>
