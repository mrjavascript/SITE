<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>

<portlet:defineObjects />



<div class="panel panel-default" id="ccdaStatistics">
    <div class="panel-heading"><h3 class="panel-title">C-CDA Statistics</h3></div>
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
						<td>C-CDA Validations</td>
						<td>${ccdaLogCountData }</td>
					</tr>
				</tbody>
			</table>
		</div>
</div>