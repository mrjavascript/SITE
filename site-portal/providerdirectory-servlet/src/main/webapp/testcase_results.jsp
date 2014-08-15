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

<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%@ page import="org.sitenv.servlets.providerdirectory.TestCaseResultWrapper" %>
<%@ page import="org.sitenv.servlets.providerdirectory.SingleTestServlet" %>


<%
    HttpSession ps = request.getSession();
    List<TestCaseResultWrapper> testResultList = (List<TestCaseResultWrapper>)ps.getAttribute("resultList");
    
    if (testResultList != null) {
    	String accordionAttrs = "";
    	if (testResultList.size() > 1) {
        	accordionAttrs = "{ active: false, collapsible: true }";
    	}
%>
        


  
    
<%
	int i = 0;
        for (TestCaseResultWrapper wrapper : testResultList) {
%>
<div class="panel-group" id="accordion<%=i%>">
<div class="panel panel-default <%= (wrapper.getStatus().equals("PASSED")) ? "panel-success" : "panel-danger" %>">
    <div class="panel-heading">
    <h3 class="panel-title">
    <% if (wrapper.getStatus().equals("PASSED")) { %>
    	<span class="glyphicon glyphicon-ok"></span>
    	<% } else { %>
    	<span class="glyphicon glyphicon-remove"></span>
    	<% } %>
      	<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion<%=i%>" href="#collapse<%=i%>">
       		<%= SingleTestServlet.testCaseRealNames.get(wrapper.getName()) %> 
       		<span class="label <%= (wrapper.getStatus().equals("PASSED")) ? "label-success" : "label-danger" %>" style="float: right; padding: .4em .6em .3em; width: 75px;"><%= wrapper.getStatus() %></span>
		</a>
    </h3>
    </div>
    <div id="collapse<%=i%>" class="panel-collapse collapse">
      	<div class="panel-body">
      			
      				<strong>REQUEST</strong>
                	<pre><%= wrapper.getRequest() %></pre>
             
         		
         		<br/>
                <strong>RESPONSE</strong>
                	<pre><%= wrapper.getResponse() %></pre>
                	</div>
        </div>
    </div>
</div>
</div>
<%
		i++;
        }
%>    
        
<%
    } 
%>
