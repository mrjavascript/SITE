<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html" isELIgnored="false" %>

<portlet:defineObjects />

<portlet:actionURL var="urlAction">
    <portlet:param name="action" value="addUser"/>
</portlet:actionURL>

<portlet:resourceURL var="urlResource">
	<portlet:param name="action" value="addUser"/>
</portlet:resourceURL>

<script type="text/javascript">

$( document ).ready(function() {
	

$("#<%=renderResponse.getNamespace()%>_samplePortlet").submit(function(event) {
	event.preventDefault();
	
	var sample = {
			'firstName' : 'Bob',
			'lastName' : 'BobLast',
			'username' : 'BobUser'
		};
		
		$.ajax({
	        url: '<%= urlResource %>',
	        type: 'GET',
	        dataType: 'json',
	        data : { 'sample' : sample },
	        contentType: 'application/json; charset=utf-8',
	        success: function (results)
	        {
	        	window.alert("firstName " + results["firstName"]);
	        },
	        error: function (results)
	        {
	            jQuery.error(String.format("Status Code: {0}, ", results.status, results.statusText));
	        }
	    });
});

});

</script>

<form:form id="<%= renderResponse.getNamespace() + \"_samplePortlet\" %>" action="<%= urlAction %>" method="POST" modelAttribute="sample">  
   <form:label path="firstName">First Name</form:label> 
	<form:input path="firstName"/> 
	<br/>
	<form:label path="lastName">Last Name</form:label> 
	<form:input path="lastName" /> 
	<br/>
	<form:label path="username">User Name</form:label> 
	<form:input path="username" /> 
      
    <input type="submit" value="OK" />  
</form:form>  
