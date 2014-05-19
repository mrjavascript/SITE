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

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>


<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%>
<%@ page import="com.liferay.portal.kernel.util.*"%>
<%@ page import="com.liferay.portal.kernel.util.*"%>
<%@ page import="com.liferay.portlet.*"%>

<%@ page import="javax.portlet.*"%>

<portlet:defineObjects />
<div class="panel panel-default" id="qrdaWidget">
	<div class="panel-heading">
		<h3 class="panel-title">SITE Statistics Configuration Editor</h3>
	</div>
	<div class="panel-body">
		<p>
			Select the type of Statistics portlet to display
		</p>
		<div class="well">
			<form
				action="<liferay-portlet:actionURL portletConfiguration="true" />"
				method="post" name="<portlet:namespace />fm">
				<label for="<portlet:namespace />viewPage">Statistics Type:</label><br/>
				 <select  name="<portlet:namespace />viewPage">
				 	<option value="all-stats" >All Statistics</option>
				 	<option value="ccda-stats" <%= (request.getAttribute("viewPage").equals("ccda-stats") ? "selected=\"selected\"" : "") %>>C-CDA Validator Statistics</option>
				 	<option value="qrda-stats" <%= (request.getAttribute("viewPage").equals("qrda-stats") ? "selected=\"selected\"" : "") %>>QRDA Validator Statistics</option>
				 	<option value="pdti-stats" <%= (request.getAttribute("viewPage").equals("pdti-stats") ? "selected=\"selected\"" : "") %>>Provider Directory Test Tool Statistics</option>
				 	<option value="direct-stats" <%= (request.getAttribute("viewPage").equals("direct-stats") ? "selected=\"selected\"" : "") %>>Direct Test Tool Statistics</option>
				 </select>
				 <br />
				<br /> 
				
				
				
				<input type="hidden" name="action" value="save">
				
				<button id="qrdavalidate_btn" type="submit"
					class="btn btn-primary start" onclick="submitForm(document.<portlet:namespace />fm);"">
					<i class="glyphicon glyphicon-ok"></i> <span>Save Config</span>
				</button>
			
			</form>
		</div>
	</div>
</div>
