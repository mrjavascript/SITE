<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %> 
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ page import="javax.portlet.PortletPreferences" %>  

<portlet:defineObjects />  

<liferay-ui:success key="success" message="Greeting saved successfully!" />

<% PortletPreferences prefs = renderRequest.getPreferences(); 
	String greeting = (String)prefs.getValue( "greeting", "Hello! Welcome to our portal."); %>  
<p><%= greeting %></p> 

<portlet:renderURL var="editGreetingURL"> 
	<portlet:param name="mvcPath" value="/edit.jsp" />  
</portlet:renderURL>  

<p>
	<a href="<%= editGreetingURL %>">Edit greeting</a>
</p>
