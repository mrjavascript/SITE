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

<portlet:actionURL var="aggregateWeeklyCounts" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
    <portlet:param name="javax.portlet.action" value="aggregateWeeklyCounts"/>
</portlet:actionURL>


<script>
	
	$(function() {
		
		
		d3.csv("${aggregateWeeklyCounts}").get().on("load", function(data) {
			loadStatistics(data, "SITE Weekly Statistics", "#canvas-svg", 768, 300);
			
		});
		
		$('#openStatsModal').unbind('click');
		
		$('#openStatsModal').on('click', function () {
			$('#statsModal').modal('show');
		});
		
		window.totalGoogleSessions = '${GoogleAnalyticsSessions}';
		window.totalJiraResolved = '${jiraIssuesResolved}';
		window.totalGooglePages = '${GoogleAnalyticsPageViews}';
		
		$('#totalGoogleSessions').text(window.totalGoogleSessions);
		$('#totalJiraResolved').text(window.totalJiraResolved);
		$('#totalGooglePages').text(window.totalGooglePages);
	});

</script>
		



<div class="modal fade" id="statsModal" tabindex="-1" role="dialog" aria-labelledby="statsModalLabel" aria-hidden="true">
	      <div class="modal-dialog" style="width:800px;">
			<div class="modal-content">
				<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4>SITE Weekly Statistics</h4>
			</div>
			<div class="modal-body" style="text-align: center;">
				<div id="canvas-svg"></div>
			</div>
			<div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close Results</button>
			</div>
		</div>
	</div>
</div>



