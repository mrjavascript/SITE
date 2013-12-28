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

<%@ page import="javax.portlet.PortletSession" %>
<%@ page import="java.util.List" %>
<%@ page import="org.sitenv.portlets.singletest.TestCaseResultWrapper" %>

<portlet:defineObjects />

<%
    HttpSession ps = request.getSession();
    List<TestCaseResultWrapper> testResultList = (List<TestCaseResultWrapper>)ps.getAttribute("LIFERAY_SHARED_resultList");
    
    if (testResultList != null) {
    	String accordionAttrs = "";
    	if (testResultList.size() > 1) {
        	accordionAttrs = "{ active: false, collapsible: true }";
    	}
%>
         <script type="text/javascript">
             $(document).ready(function () {
	             $( "#accordion" ).accordion(<%= accordionAttrs %>);
             });
        </script>

        <div id="accordion">
<%
        for (TestCaseResultWrapper wrapper : testResultList) {
%>
            <h3><%= wrapper.getName() %> - <%= wrapper.getStatus() %></h3>
            <div>
                <div class="requestHeader"><strong>Request:</strong></div>
                <div><pre><%= wrapper.getRequest() %></pre></div>
                <div class="responseHeader"><strong>Response:</strong></div>
                <div><pre><%= wrapper.getResponse() %></pre></div>
            </div>
<%
        }
%>
        </div>
<%
    } 
%>
