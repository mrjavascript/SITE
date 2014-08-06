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
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@ page import="com.liferay.portal.kernel.util.Validator"%>
<%@ page import="com.liferay.portlet.PortletPreferencesFactoryUtil" %>
<%@ page import="javax.portlet.PortletPreferences"%>
<%@ page import="com.liferay.util.PwdGenerator"%>
<%@ page import="com.liferay.portal.service.PortletPreferencesLocalServiceUtil" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>

<portlet:defineObjects />

<portlet:actionURL var="urlAction" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
    <portlet:param name="javax.portlet.action" value="uploadXDR"/>
</portlet:actionURL>

<script type="text/javascript">
	var url = '${urlAction}';
	window.currentContextPath = "<%=request.getContextPath()%>";
</script>

<div class="panel panel-default" id="anchoruploadwidget">
      <div class="panel-heading"><h3 class="panel-title">XDR Receive</h3></div>
  		<div class="panel-body">
			<p>
				Receive messages from the Sandbox to your system. 
			</p>
			<br/><br/>
			<div class="well">
			<form id="XDRValidationForm" action="${urlAction}" method="POST" enctype="multipart/form-data">
      	
			
				<label for="wsdlLocation">Enter Your Endpoint URL:</label>
				<input type="text" name="wsdlLocation" id="wsdlLocation" class="validate[required,custom[url]] form-control" tabindex="1"/>
				<br/>
				<label for="testCases">Select a Test Scenario:</label>
				<select name="testCases" id="testCases" class="validate[required] form-control" tabindex="1">
					<option value="test1">Test 1</option>
					<option value="test2">Test 2</option>
					<option value="test3">Test 3</option>
				</select>
				
				<br/><br/>
			
			<noscript><input type="hidden" name="redirect" value="true" /></noscript>
			<div id="ccdauploaderrorlock" style="position: relative;">
				<div class="row">
					<div class="col-md-12">
						<label for="fileupload">Select a Local C-CDA File to Send:</label><br/>
						<span class="btn btn-success fileinput-button" id="fileupload-btn"> <i
								class="glyphicon glyphicon-plus"></i>&nbsp;<span>Upload C-CDA...</span>
								<input id="fileupload" type="file" name="file"  class="validate[required, custom[xmlfileextension[xml|XML]], custom[maxCCDAFileSize]]"  tabindex="1"/>
						</span>
						<div id="files"></div>
						
					</div>
					
					
				</div>
					
					
			</div>
			<hr/>
			<button id="formSubmit" type="submit" class="btn btn-primary start" onclick="return false;"  tabindex="1">
							<i class="glyphicon glyphicon-ok"></i> <span>Validate Document</span>
						</button>
			
			</form>
			</div>
			
		</div>
</div>

<div class="panel panel-default" id="anchoruploadwidget">
	<div class="panel-heading"><h3 class="panel-title">XDR Send</h3></div>
	<div class="panel-body">
		<p>
			Send messages from your implementation to the endpoint listed below:
			<ul>
				<li>
					WSDL LOCATION HERE
				</li>			
			</ul>
		</p>
	</div>
</div>