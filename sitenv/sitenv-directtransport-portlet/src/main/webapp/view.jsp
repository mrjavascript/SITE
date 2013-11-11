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

<portlet:defineObjects />

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
</script>

<article class="module width_full" id="anchoruploadwidget">
	<header><h3>Trust Anchor Exchange</h3></header>
	<div class="module_content">
		<!-- anchor -->
		
		
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
							<a href="<%=serviceContext %>/GetTrustBundle">Trust Bundle</a> of 
							<a href="http://direct.sitenv.org" target="_blank">direct.sitenv.org</a> 
							which is refreshed every five minutes and is only used for testing purposes. Once a Trust Anchor is uploaded, users can test with the Direct sandbox after five minutes. 
						</li>
					</ul>
				</li>
	  		</ol>
		
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
					<input placeholder="Your CA Certificate" accept="application/x-x509-ca-cert" id="anchoruploadfile" type="file" name="anchoruploadfile" />
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
		</form>
		<div class="clear"></div>
	</div>
</article>


<article class="module width_full">
	<header><h3>Direct Send</h3></header>
	<div class="module_content">
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
</article>


<article class="module width_full" id="directreceivewidget">
		<header><h3>Direct Receive</h3></header>
		
		<div class="module_content">
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
			<form id="directreceiveform" enctype="multipart/form-data" action="<%=serviceContext %>/DirectEmailReceiveService">
			
			<input name="directfromendpoint" type="hidden" value="<%= fromemail %>" />
			<input name="smtphostname" type="hidden" value="<%= SMTPServerHost %>" />
			<input name="smtpport" type="hidden" value="<%= SMTPServerPort %>" />
			<input name="smtpusername" type="hidden" value="<%= SMTPAuthUserName %>" />
			<input name="smtppswrd" type="hidden" value="<%= SMTPAuthPassword %>" />
			<input name="enablessl" type="hidden" value="<%= EnableSSL %>" />
			<table border="0" cellpadding="0" cellspacing="0"  id="id-form">
			<colgroup>
        		<col span="1" style="width:150px;">
        		<col span="1" style="width:380px;">
        		<col span="1" style="width:60%;">
			</colgroup>
			<tr>
				<td>
					<input  type="radio" name="filetype" id="customccda_rdbtn" value="custom">
					<label for="customccda_rdbtn">Choose Your Own Content:</label>
				</td>
				<td>
					<input id="uploadccdainput" 
						data-prompt-position="topLeft:0" 
						name="uploadccdafilecontent" 
						type="file" 
						
						class="ccdainputfile"/>
				</td>
				<td>
					<div class="tooltip-wrapper">
					<div class="bubble-left"></div>
					<div class="bubble-inner">CCDA, 3MB max size</div>
					<div class="bubble-right"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<input checked="checked"  type="radio" name="filetype" id="chooseprecannedccda_rdbtn" value="precanned" data-prompt-position="topLeft:0">
					<label for="chooseprecannedccda_rdbtn">Choose Precanned Content:</label>
				</td>
				<td>  
					<input id="ccdainputfile"
						class="ccdainputfile validate[funcCall[precannedRequired]]" 
						data-prompt-position="topLeft:0" 
						name="precannedfilepath" 
						style="display: inline; width: 250px;" type="text">
					<div id="precannedccdaselectbutton" class="dropdown">
					    <a class="dropdown-button button">Pick Sample<img src="<%=request.getContextPath()%>/images/dropdown.png" class="dropdown-icon"/></a>
						<div class="dropdown-panel">
							<div id="ccdafiletreepanel">
							</div>	
						</div>
					</div>
					
				</td>
				<td>
				</td>
			</tr>
			<tr>
				<td>Enter your end point name:</td>
				<td>
					<input class="validate[required,custom[email]]" 
						data-errormessage-value-missing="end point is required!"
						data-errormessage-custom-error="end point format is invalid (hint:example@test.org)"
						data-prompt-position="topLeft:0"
						name="endpontemail" 
						placeholder="recipient direct email address"
						style="display: inline; width: 250px;" type="text"></td>
				<td>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td valign="top">
					<input id="directreceivesubmit" type="button" value="" class="form-submit" />
				</td>
				<td></td>
			</tr>
			</table>
        </form>
			
			<div class="clear"></div>
		</div>
</article>

