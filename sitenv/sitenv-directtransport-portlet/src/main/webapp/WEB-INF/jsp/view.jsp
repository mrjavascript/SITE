<%
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
%>



<%@include file="/init.jsp" %>

<%@ page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@ page import="com.liferay.portal.kernel.util.Validator"%>
<%@ page import="com.liferay.portlet.PortletPreferencesFactoryUtil" %>
<%@ page import="javax.portlet.PortletPreferences"%>
<%@ page import="com.liferay.util.PwdGenerator"%>
<%@ page import="com.liferay.portal.service.PortletPreferencesLocalServiceUtil" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>

<portlet:defineObjects />

<portlet:actionURL var="sampleCCDATree" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
    <portlet:param name="javax.portlet.action" value="sampleCCDATree"/>
</portlet:actionURL>

<portlet:resourceURL id="getTrustBundle" var="getTrustBundleResource"/>

<portlet:actionURL var="uploadTrustAnchor" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
    <portlet:param name="javax.portlet.action" value="uploadTrustAnchor"/>
</portlet:actionURL>

<portlet:actionURL var="uploadCCDADirectReceive" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
    <portlet:param name="javax.portlet.action" value="uploadCCDADirectReceive"/>
</portlet:actionURL>

<portlet:actionURL var="precannedCCDADirectReceive" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
    <portlet:param name="javax.portlet.action" value="precannedCCDADirectReceive"/>
</portlet:actionURL>

<%
	//String serviceContext = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/delegate";
	String serviceContext = ServiceContext;
%>

<portlet:renderURL var="endpointcerturl"> 
	<portlet:param name="mvcPath" value="/Certificates/PublicKeys/direct.sitenv.org_ca.der" />  
</portlet:renderURL>  

<script type="text/javascript">
	window.currentContextPath = "<%=request.getContextPath()%>";
	window.serviceContextPath = "<%=serviceContext%>"; 
	var sampleCCDATreeURL = '${sampleCCDATree}';
</script>

<div class="panel panel-default" id="anchoruploadwidget">
      <div class="panel-heading"><h3 class="panel-title">Trust Anchor Exchange</h3></div>
  		<div class="panel-body">
				
			<span>Trust Anchor Exchange can be accomplished via two different mechanisms.</span>
			<ol>
				<li>
	  				<span style="text-decoration: underline;">
	  					Trust Anchor Exchange using BlueButton Trust Bundles:
					</span>
					<ul>
						<li>
							SITE's Direct instantiation synchronizes with the <a href="https://secure.bluebuttontrust.org/" target="_blank">BlueButton</a>
							 Patient and Provider Test bundles every minute. 
							The SITE's Trust Anchor is already part of the <a href="https://secure.bluebuttontrust.org/" target="_blank">BlueButton</a> Patient and Provider Test bundles.
							<ul>
								<li>Implementers can download the SITE Trust Anchor from the <a href="https://secure.bluebuttontrust.org/" target="_blank">BlueButton</a> bundles.</li> 
								<li>Implementers can submit their Trust Anchors on the <a href="https://secure.bluebuttontrust.org/" target="_blank">BlueButton</a> website.</li>
							</ul>
						</li>
					</ul>
				</li>
				<li>
					<span style="text-decoration: underline;">
	  					Trust Anchor Exchange using "SITE Upload Trust Anchor":
					</span>
					<ul>
						<li>
							Download the Trust Anchor for the Sandbox 
							<a href="<%=request.getContextPath()%>/Certificates/PublicKeys/direct.sitenv.org_ca.der">(direct.sitenv.org Certificate)</a> and import the trust anchor into your trust store.
						</li>
						<li>
							Please upload your Trust Anchor by selecting your Trust Anchor. 
							If you need to replace the Trust Anchor, just perform another upload and the previous one will be replaced.
						</li>
						<li>
							Uploading the Trust Anchor causes an update to the 
							<a href="/trustBundle/TrustBundle.p7b">Trust Bundle</a> of 
							<a href="http://direct.sitenv.org" target="_blank">direct.sitenv.org</a> 
							which is refreshed every five minutes and is only used for testing purposes. Once a Trust Anchor is uploaded, users can test with the Direct sandbox after five minutes. 
						</li>
					</ul>
				</li>
	  		</ol>
	  	<div class="well">
	  	<form id="anchoruploadform" action="${uploadTrustAnchor}" method="POST" enctype="multipart/form-data">
      		
			<!-- The fileinput-button span is used to style the file input field as button -->
			
			<noscript><input type="hidden" name="redirect" value="true" /></noscript>
			<div id="anchoruploaderrorlock" style="position:relative;">
				<div class="row">
					<div class="col-md-12">
				<label for="anchoruploadfile">Select a Local Trust Anchor Certificate (binary or PEM encoded): </label><br/>
				<span class="btn btn-success fileinput-button" id="anchoruploadfile-btn"> <i
							class="glyphicon glyphicon-plus"></i>&nbsp;<span>Select a Certificate...</span>
							<!-- The file input field used as target for the file upload widget -->
							<input id="anchoruploadfile" type="file" name="anchoruploadfile" class="validate[required, custom[derencncodedfileextension[der|crt|cer|pem|DER|CRT|CER|PEM]], custom[maxCertFileSize]]"/>
					</span>
					<div id="anchoruploadfiles" class="files"></div>
			</div>
					
				</div>
			</div>
			<hr/>
			<button id="anchoruploadsubmit" type="submit" class="btn btn-primary start" onclick="return false;">
				<i class="glyphicon glyphicon-ok"></i> <span>Submit Anchor</span>
			</button>
			
			
      	</form>
      	
      	</div>
		<!-- 
		<form id="anchoruploadform" enctype="multipart/form-data" action="<%=serviceContext %>/UploadTrustAnchorService">
			<table border="0" cellpadding="0" cellspacing="0"  id="id-form">
			<colgroup>
	       		<col span="1" style="width:150px;">
	       		<col span="1" style="width:380px;">
	       		<col span="1" style="width:70%;">
			</colgroup>
			<tr>
				<td>Upload Trust Anchor:</td>
				<td>
					<input placeholder="Your CA Certificate" id="anchoruploadfile" type="file" name="anchoruploadfile" />
				</td>
				<td>
					<div class="tooltip-wrapper">
						<div class="bubble-left"></div>
						<div class="bubble-inner">binary or base64-encoded certificates</div>
						<div class="bubble-right"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td valign="top">
					<input id="anchorsubmit" type="button" value="" class="form-submit" />
				</td>
				<td></td>
			</tr>
			</table>
		</form>-->
		<div class="clear"></div>
	</div>
</div>

<div class="panel panel-default" id="anchoruploadwidget">
      <div class="panel-heading"><h3 class="panel-title">Direct Send</h3></div>
  		<div class="panel-body">


		<p>
		Send messages from your implementation to the end points listed below:
		<ul>
			<li>
				Provider1@direct.sitenv.org
			</li>			
		</ul>
		Upon successful receipt of the message, the Direct Sandbox will send an MDN(Message Disposition Notification) back to the sender. The content of the message can be anything and is not validated or used by the SITE.
		</p>
	</div>
</div>

<div class="panel panel-default" id="directreceivewidget">
      <div class="panel-heading"><h3 class="panel-title">Direct Receive</h3></div>
  		<div class="panel-body">


			<p>
				Receive messages from the Sandbox to your system. 
				
				<ul>
					<li>
						<u>Choose your own content:</u> Developers can use their own files as the payload of the Direct message sent from the Sandbox. 
									This provides the ability to verify the file they chosen and that the contents were decrypted appropriately.
					</li>
					<p style='text-indent:0.25;'>
						<u>Choose pre-canned content:</u> Provides a list of files that you can choose from as the payload of the Direct message.
					</p>
					<li>
						<u>Enter your end point name:</u> The name of the Direct address where you would like to receive the message. Ensure that the Trust Anchor corresponding to the end point has already been uploaded.
					</li>
					<li>
						Once the above fields are populated, hit the send message button.<br/>
						You will receive a message from provider1@direct.sitenv.org to your system with the content you have uploaded.			
					</li>
				</ul>
			</p>
			<div class="well">
			
			<label for="directMessageType">Select C-CDA Content Source:</label><br/>
			
			<div class="btn-group" data-toggle="buttons">
				<label class="btn btn-default active">
				<input type="radio" id="precanned" name="directMessageType" value="precanned" checked/>Choose Precanned Content</label>
				<label class="btn btn-default">
				<input type="radio" id="choosecontent" name="directMessageType" value="choosecontent"/>Choose Your Own Content</label>
			</div>
			<br/><br/>
		
		<div id="precannedFormWrapper">
			<form id="precannedForm"  action="${precannedCCDADirectReceive}" method="POST">
				<p>
				<label for="precannedemail">Enter Your Endpoint Name:</label><br/>
					<input id="precannedemail"
						class="validate[required,custom[email]]"
						data-errormessage-value-missing="end point is required!"
						data-errormessage-custom-error="end point format is invalid (hint:example@test.org)"
						name="precannedemail"
						placeholder="recipient direct email address"
						style="display: inline;" type="text" />
				</p>
				<br />
				<noscript><input type="hidden" name="redirect" value="true" /></noscript>
				<div id="precannederrorlock" style="position: relative;">
					<div class="row">
					<div class="col-md-12">
					<label for="dLabel">Select a Precanned Sample C-CDA File to Send:</label><br/>
									<div class="dropdown">
										<button id="dLabel" data-toggle="dropdown"
											class="btn btn-success dropdown-toggle validate[funcCall[precannedRequired]]" type="button">
											Pick Sample <i class="glyphicon glyphicon-play"></i>
										</button>

										<ul class="dropdown-menu rightMenu" role="menu" aria-labelledby="dLabel">
											<li>
												<div id="ccdafiletreepanel"></div>
											</li>
										</ul>
									</div>
									<div>
									<span id="precannedfilePathOutput"></span>
									</div>
					</div>
					</div>
				</div>
				<hr />
				<button id="precannedCCDAsubmit" type="submit"
					class="btn btn-primary start" onclick="return false;">
					<i class="glyphicon glyphicon-envelope"></i> <span>Send
						Message</span>
				</button>
				<input id="precannedfilepath"
						name="precannedfilepath" type="hidden">
			</form>
		</div>
		<div id="uploadFormWrapper">
			<form id="ccdauploadform" action="${uploadCCDADirectReceive}" method="POST" enctype="multipart/form-data">
				<p>
					<label for="ccdauploademail">Enter Your Endpoint Name:</label><br/>
					 <input id="ccdauploademail"
						class="validate[required,custom[email]]"
						data-errormessage-value-missing="end point is required!"
						data-errormessage-custom-error="end point format is invalid (hint:example@test.org)"
						name="ccdauploademail"
						placeholder="recipient direct email address"
						style="display: inline;" type="text" />
				</p>
				<br />
				<noscript><input type="hidden" name="redirect" value="true" /></noscript>
				<div id="ccdauploaderrorlock" style="position: relative;">
					<div class="row">
					<div class="col-md-12">
						<label for="ccdauploadfile">Select a Local C-CDA File to Send:</label><br/>
							<span
								class="btn btn-success fileinput-button" id="ccdauploadfile-btn"> <i
									class="glyphicon glyphicon-plus"></i>&nbsp;<span>Upload C-CDA</span> <!-- The file input field used as target for the file upload widget -->
									<input id="ccdauploadfile" type="file"
									name="ccdauploadfile" class="validate[required, custom[maxCCDAFileSize]]"/>
							</span>
							
								<div id="ccdauploadfiles" class="files"></div>
					</div>
					</div>
				</div>
				<hr />
				<button id="ccdauploadsubmit" type="submit"
					class="btn btn-primary start" onclick="return false;">
					<i class="glyphicon glyphicon-envelope"></i> <span>Send Message</span>
				</button>
			</form>
		</div>
		</div>
		<br/>
			
			<div class="clear"></div>
		</div>
</div>

