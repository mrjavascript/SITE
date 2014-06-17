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
<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>

<portlet:defineObjects />

<portlet:actionURL var="directReceiveWeeklyCounts" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
    <portlet:param name="javax.portlet.action" value="directReceiveWeeklyCounts"/>
</portlet:actionURL>

<portlet:actionURL var="directSendWeeklyCounts" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
    <portlet:param name="javax.portlet.action" value="directSendWeeklyCounts"/>
</portlet:actionURL>

<script>
	
	$(function() {
		
		d3.csv("${directSendWeeklyCounts}").get().on("load", function(data) {
			loadStatistics(data, "Direct Send Weekly Statistics", "#canvas-svg-directsend", 330, 300);
			d3.csv("${directReceiveWeeklyCounts}").get(function(error,data) {
				loadStatistics(data, "Direct Receive Weekly Statistics", "#canvas-svg-directreceive", 330, 300);
			});
		});

		
	});

</script>
		
<div class="row well">
	<!--div class="col-md-6" style="text-align: center;">
		<h2>${successfulCcdas}</h2>
		<p>c-cdas passed</p>
	</div>
	<div class="col-md-6" style="text-align: center;">
		<h2>${failedCcdas}</h2>
		<p>c-cdas failed</p>
	</div-->
	<div id="canvas-svg-directsend"></div>
</div>
<div class="row well">
	<div id="canvas-svg-directreceive"></div>
</div>
<% 
	if (renderRequest.isUserInRole("administrator")) {
	%>
	<div style="width:100%">
		<a class="btn btn-success" href="statistics"  style="width: 100%;">See More Stats</a>
	</div>
	<%
	}
	%>