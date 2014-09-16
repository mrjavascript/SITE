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

<portlet:defineObjects />


<div class="panel panel-default" id="directStatistics">
    <div class="panel-heading"><h3 class="panel-title">Direct Statistics</h3></div>
		<div class="table-responsive">
			<table class="table table-striped">
				<thead>
					<tr>
						<th style="width:65%"></th>
						<th>Total Count</th>
					</tr>
				</thread>
				<tbody>
					<tr>
						<td>Direct Sent Messages</td>
						<td>${directSendCountData.totalDirectMessageCount }</td>
					</tr>
					<tr>
						<td>Direct Received Messages</td>
						<td>${directReceiveCountData.totalDirectMessageCount }</td>
					</tr>
				</tbody>
			</table>
		</div>
</div>

	
	
	