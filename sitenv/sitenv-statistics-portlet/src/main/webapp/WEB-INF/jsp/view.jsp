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

<div class="jumbotron">
	<h1>
		SITE Statistics</h1>
	<hr />
</div>

<div class="panel panel-default" id="ccdaStatistics">
    <div class="panel-heading"><h3 class="panel-title">C-CDA Statistics</h3></div>
		<div class="table-responsive">
			<table class="table table-striped">
				<thead>
					<tr>
						<th style="width:35%"></th>
						<th>Total Count</th>
						<th>30 Day Count</th>
						<th>60 Day Count</th>
						<th>90 Day Count</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Total C-CDA Validations</td>
						<td>${totalCcdas}</td>
						<td>${totalCcdas30}</td>
						<td>${totalCcdas60}</td>
						<td>${totalCcdas90}</td>
					</tr>
					<tr>
						<td>Successful C-CDA Validations</td>
						<td>${successfulCcdas}</td>
						<td>${successfulCcdas30}</td>
						<td>${successfulCcdas60}</td>
						<td>${successfulCcdas90}</td>
					</tr>
					<tr>
						<td>Failed C-CDA Validations</td>
						<td>${failedCcdas}</td>
						<td>${failedCcdas30}</td>
						<td>${failedCcdas60}</td>
						<td>${failedCcdas90}</td>
					</tr>
					<tr>
						<td>C-CDAs with Warnings</td>
						<td>${warningCcda}</td>
						<td>${warningCcda30}</td>
						<td>${warningCcda60}</td>
						<td>${warningCcda90}</td>
					</tr>
					<tr>
						<td>C-CDAs with Info Messages</td>
						<td>${infoCcda}</td>
						<td>${infoCcda30}</td>
						<td>${infoCcda60}</td>
						<td>${infoCcda90}</td>
					</tr>
					<tr>
						<td>Saved C-CDA Validations</td>
						<td>${ccdaDownloads}</td>
						<td>${ccdaDownloads30}</td>
						<td>${ccdaDownloads60}</td>
						<td>${ccdaDownloads90}</td>
					</tr>
					<tr>
						<td>Smart C-CDA Validations</td>
						<td>${smartCcdas}</td>
						<td>${smartCcdas30}</td>
						<td>${smartCcdas60}</td>
						<td>${smartCcdas90}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
<div class="panel panel-default" id="directStatistics">
    <div class="panel-heading"><h3 class="panel-title">Direct Statistics</h3></div>
		<div class="table-responsive">
			<table class="table table-striped">
				<thead>
					<tr>
						<th style="width:35%"></th>
						<th>Total Count</th>
						<th>30 Day Count</th>
						<th>60 Day Count</th>
						<th>90 Day Count</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Total Direct Receive Messages</td>
						<td>${totalDirectCount}</td>
						<td>${totalDirectCount30}</td>
						<td>${totalDirectCount60}</td>
						<td>${totalDirectCount90}</td>
					</tr>
					<tr>
						<td>Precanned Direct Receive Messages</td>
						<td>${precannedDirectCount}</td>
						<td>${precannedDirectCount30}</td>
						<td>${precannedDirectCount60}</td>
						<td>${precannedDirectCount90}</td>
					</tr>
					<tr>
						<td>Uploaded Direct Receive Messages</td>
						<td>${uploadedDirectCount}</td>
						<td>${uploadedDirectCount30}</td>
						<td>${uploadedDirectCount60}</td>
						<td>${uploadedDirectCount90}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
<div class="panel panel-default" id="pdStatistics">
    <div class="panel-heading"><h3 class="panel-title">Provider Directory Statistics</h3></div>
		<div class="table-responsive">
			<table class="table table-striped">
				<thead>
					<tr>
						<th style="width:35%"></th>
						<th>Total Count</th>
						<th>30 Day Count</th>
						<th>60 Day Count</th>
						<th>90 Day Count</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Total Executed Test Cases</td>
						<td>${totalPdtiCount}</td>
						<td>${totalPdtiCount30}</td>
						<td>${totalPdtiCount60}</td>
						<td>${totalPdtiCount90}</td>
					</tr>
					<tr>
						<td>Passed Test Cases</td>
						<td>${successPdtiCount}</td>
						<td>${successPdtiCount30}</td>
						<td>${successPdtiCount60}</td>
						<td>${successPdtiCount90}</td>
					</tr>
					<tr>
						<td>Failed Test Cases</td>
						<td>${failedPdtiCount}</td>
						<td>${failedPdtiCount30}</td>
						<td>${failedPdtiCount60}</td>
						<td>${failedPdtiCount90}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
<div class="panel panel-default" id="qrdaStatistics">
    <div class="panel-heading"><h3 class="panel-title">QRDA Statistics</h3></div>
		<div class="table-responsive">
			<table class="table table-striped">
				<thead>
					<tr>
						<th style="width:35%"></th>
						<th>Total Count</th>
						<th>30 Day Count</th>
						<th>60 Day Count</th>
						<th>90 Day Count</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Total QRDA Validations</td>
						<td>${totalQrda}</td>
						<td>${totalQrda30}</td>
						<td>${totalQrda60}</td>
						<td>${totalQrda90}</td>
					</tr>
					<tr>
						<td>Successful QRDA Validations</td>
						<td>${successQrda}</td>
						<td>${successQrda30}</td>
						<td>${successQrda60}</td>
						<td>${successQrda90}</td>
					</tr>
					<tr>
						<td>Failed QRDA Validations</td>
						<td>${failedQrda}</td>
						<td>${failedQrda30}</td>
						<td>${failedQrda60}</td>
						<td>${failedQrda90}</td>
					</tr>
					
					<tr>
						<td>Total Category I Validations</td>
						<td>${totalCat1Qrda}</td>
						<td>${totalCat1Qrda30}</td>
						<td>${totalCat1Qrda60}</td>
						<td>${totalCat1Qrda90}</td>
					</tr>
					<tr>
						<td>Successful Category I Validations</td>
						<td>${successCat1Qrda}</td>
						<td>${successCat1Qrda30}</td>
						<td>${successCat1Qrda60}</td>
						<td>${successCat1Qrda90}</td>
					</tr>
					<tr>
						<td>Failed Category I Validations</td>
						<td>${failedCat1Qrda}</td>
						<td>${failedCat1Qrda30}</td>
						<td>${failedCat1Qrda60}</td>
						<td>${failedCat1Qrda90}</td>
					</tr>
					
					<tr>
						<td>Total Category III Validations</td>
						<td>${totalCat3Qrda}</td>
						<td>${totalCat3Qrda30}</td>
						<td>${totalCat3Qrda60}</td>
						<td>${totalCat3Qrda90}</td>
					</tr>					
					<tr>
						<td>Successful Category III Validations</td>
						<td>${successCat3Qrda}</td>
						<td>${successCat3Qrda30}</td>
						<td>${successCat3Qrda60}</td>
						<td>${successCat3Qrda90}</td>
					</tr>					
					<tr>
						<td>Failed Category III Validations</td>
						<td>${failedCat3Qrda}</td>
						<td>${failedCat3Qrda30}</td>
						<td>${failedCat3Qrda60}</td>
						<td>${failedCat3Qrda90}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>