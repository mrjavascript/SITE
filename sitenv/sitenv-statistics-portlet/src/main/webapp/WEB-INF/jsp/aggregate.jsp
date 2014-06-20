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
			loadStatistics(data, "Weekly Statistics for SITE", "#canvas-svg", 730, 300);
			
		});
		
		$('#openStatsModal').unbind('click');
		
		$('#openStatsModal').on('click', function () {
			$('#statsModal').modal('show');
		});
		
		window.totalGoogleSessions = '${GoogleAnalyticsSessionsFormat}';
		window.totalJiraResolved = '${jiraIssuesResolvedFormat}';
		window.totalGooglePages = '${GoogleAnalyticsPageViewsFormat}';
		
		$('#totalGoogleSessions').text(window.totalGoogleSessions);
		$('#totalJiraResolved').text(window.totalJiraResolved);
		$('#totalGooglePages').text(window.totalGooglePages);
	});

</script>
		



<div class="modal modal-wide fade" id="statsModal" tabindex="-1" role="dialog" aria-labelledby="statsModalLabel" aria-hidden="true">
	      <div class="modal-dialog" style="width:800px;">
			<div class="modal-content">
				<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4>SITE Statistics Dashboard</h4>
			</div>
			<div class="modal-body" style="text-align: center;">
				<div class="well">
					<div id="canvas-svg"></div>
				</div>
				<br/>
				<div class="panel panel-default" id="GoogleAnalyticsStatistics" style="text-align:left;">
			    <div class="panel-heading"><h3 class="panel-title">Community Activity Statistics</h3></div>
					<div class="table-responsive">
						<table class="table table-striped">
							<thead>
								<tr>
									<th style="width:35%;"></th>
									<th>Total</th>
									<th>Last 30 Days</th>
									<th>Last 60 Days</th>
									<th>Last 90 Days</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>Unique User Visits</td>
									<td>${GoogleAnalyticsSessions}</td>
									<td>${GoogleAnalyticsSessions30}</td>
									<td>${GoogleAnalyticsSessions60}</td>
									<td>${GoogleAnalyticsSessions90}</td>
								</tr>
								<tr>
									<td>Individual Page Views</td>
									<td>${GoogleAnalyticsPageViews}</td>
									<td>${GoogleAnalyticsPageViews30}</td>
									<td>${GoogleAnalyticsPageViews60}</td>
									<td>${GoogleAnalyticsPageViews90}</td>
								</tr>
								<tr>
									<td>Issues Created</td>
									<td>${jiraIssuesCreated}</td>
									<td>${jiraIssuesCreated30}</td>
									<td>${jiraIssuesCreated60}</td>
									<td>${jiraIssuesCreated90}</td>
								</tr>
								<tr>
									<td>Issues Resolved</td>
									<td>${jiraIssuesResolved}</td>
									<td>${jiraIssuesResolved30}</td>
									<td>${jiraIssuesResolved60}</td>
									<td>${jiraIssuesResolved90}</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close Results</button>
			</div>
		</div>
	</div>
</div>



