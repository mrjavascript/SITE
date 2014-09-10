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

<div class="panel panel-default">
      <div class="panel-heading"><h3 class="panel-title">Hosting - Verify your certificate can be discovered</h3></div>
  		<div class="panel-body">
  		<h4>Directions</h4>
      		<p>
				Step 1: Determine the required test cases for your SUT (System Under Test). Notice that there are two options for storage of address-bound and domain-bound certificates. 
			</p>
			<p>
				Step 2: Select a test case that reflects the SUT from the dropdown. 
			</p>
		
			<p>
				Step 3: Read the Description and Instructions for the selected test case. Then enter the Direct address and submit. Your SUT configuration may require that you select more than one test case. If so, then select one test case at a time, following the instructions to execute the test after each selection.  
			</p>
      		
 	<div class="well">
       	<form id="DCDTHostingForm" action="urlAction" method="POST" enctype="multipart/form-data">
      		
      		<div id="testcase_radioboxgroup" class="btn-group-vertical">
      			<label for="testcase">Select a Test Case:</label><br/>
      			<select id="testcase" name="testcase" class="form-control" tabindex="1">
      				<option value="">-- No testcase selected --</option>
      				<option value="H1_DNS_AB_Normal">H1 - Normal address-bound certificate search in DNS</option>
      				<option value="H2_DNS_DB_Normal">H2 - Normal domain-bound certificate search in DNS</option>
      				<option value="H3_LDAP_AB_Normal">H3 - Normal address-bound certificate search in LDAP</option>
      				<option value="H4_LDAP_DB_Normal">H4 - Normal domain-bound certificate search in LDAP</option>
      			</select>
      		</div>
      		<br/><br/><!-- Description of the test case will go here: -->
      		<p>
			<label for="directAddress">Enter Your Endpoint Name:</label><br/>
					<input id="directAddress"
						class="validate[required,custom[email]] form-control" 
						data-errormessage-value-missing="Direct Address is required!"
						data-errormessage-custom-error="Direct Address format is invalid (hint:example@test.org)"
						name="directAddress"
						placeholder="recipient direct email address"
						style="display: inline;" type="text"  tabindex="1"/>
				</p>
			
			
			<hr/>
			<button id="formSubmit" type="submit" class="btn btn-primary start" onclick="return false;"  tabindex="1">
							<i class="glyphicon glyphicon-ok"></i> <span>Validate Document</span>
						</button>
			
			
      	</form>
      	</div>
      </div>
    </div>
    
 <div class="panel panel-default">
      <div class="panel-heading"><h3 class="panel-title">Discover DCDT's Certificates</h3></div>
  		<div class="panel-body">
  		<h4>Directions</h4>
  			<p>
				Before executing any Discovery test cases, use the following form to associate your email address so you receive results of certificate discovery tests executed by your direct account. Enter the Direct Address, that will be used to execute the certificate discovery tests, enter the email address where the test case results will be sent, and submit the mapping.  Note: a mapping only needs to be entered once per direct address, not with every test case.
			</p>
			<div class="well">
       	<form id="DCDTEmailMappingForm" action="urlAction" method="POST" enctype="multipart/form-data">
      		
      		<p>
			<label for="directAddress">Enter Your Direct Address:</label><br/>
					<input id="directAddress"
						class="validate[required,custom[email]] form-control" 
						data-errormessage-value-missing="Direct Address is required!"
						data-errormessage-custom-error="Direct Address format is invalid (hint:example@test.org)"
						name="directAddress"
						placeholder="recipient direct email address"
						style="display: inline;" type="text"  tabindex="1"/>
				</p>
      		<p>
			<label for="emailAddress">Enter Your Email Address (for results):</label><br/>
					<input id="directAddress"
						class="validate[required,custom[email]] form-control" 
						data-errormessage-value-missing="Email Address is required!"
						data-errormessage-custom-error="Email Address format is invalid (hint:example@test.org)"
						name="emailAddress"
						placeholder="recipient email address"
						style="display: inline;" type="text"  tabindex="1"/>
				</p>
			
			
			<hr/>
			<button id="formSubmit" type="submit" class="btn btn-primary start" onclick="return false;"  tabindex="1">
							<i class="glyphicon glyphicon-ok"></i> <span>Map Addresses</span>
						</button>
			
			
      	</form>
      	</div>
      		<p>
				Step 1:Download the Testing Tool's trust anchor. 
Download Trust Anchor
			</p>
			<p>
Step 2: Upload the anchor to your Direct instance. This will allow you to send messages to our tool. 
</p>
<p>
Step 3: Choose a test case from the drop down menu below. Read the test case description below the "Direct Address" field, copy the displayed Direct address and proceed to step 4. You should run all of the tests in order to verify that your system can correctly discover certificates in either DNS CERT records or LDAP servers. (Note: your system MUST NOT already contain a certificate for the address selected or the test case will not be valid). 
</p>
<div class="well">
       	<form id="DCDTDiscoveryForm" action="urlAction" method="POST" enctype="multipart/form-data">
      		
      		<div id="testcase_radioboxgroup" class="btn-group-vertical">
      			<label for="testcase">Select a Test Case:</label><br/>
      			<select id="testcase" name="testcase" class="form-control" tabindex="1">
      				<option value="">-- No testcase selected --</option>
      				<option value="D1_DNS_AB_Valid">D1 - Valid address-bound certificate discovery in DNS</option>
      				<option value="D2_DNS_DB_Valid">D2 - Valid domain-bound certificate discovery in DNS</option>
      				<option value="D3_LDAP_AB_Valid">D3 - Valid address-bound certificate discovery in LDAP</option>
      				<option value="D4_LDAP_DB_Valid">D4 - Valid domain-bound certificate discovery in LDAP</option>
      				<option value="D5_DNS_AB_Invalid">D5 - Invalid address-bound certificate discovery in DNS</option>
      				<option value="D6_DNS_DB_Invalid">D6 - Invalid domain-bound certificate discovery in DNS</option>
      				<option value="D7_LDAP_AB_Invalid">D7 - Invalid address-bound certificate discovery in LDAP</option>
      				<option value="D8_LDAP_DB_Invalid">D8 - Invalid domain-bound certificate discovery in LDAP</option>
      				<option value="D9_DNS_AB_SelectValid">D9 - Select valid address-bound certificate over invalid certificate in DNS</option>
      				<option value="D10_LDAP_AB_UnavailableLDAPServer">D10 - Certificate discovery in LDAP with one unavailable LDAP server</option>
      				<option value="D11_DNS_NB_NoDNSCertsorSRV">D11 - No certificates discovered in DNS CERT records and no SRV records</option>
      				<option value="D12_LDAP_NB_UnavailableLDAPServer">D12 - No certificates found in DNS CERT records and no available LDAP servers</option>
      				<option value="D13_LDAP_NB_NoCerts">D13 - No certificates discovered in DNS CERT records or LDAP servers</option>
      				<option value="D14_DNS_AB_TCPLargeCert">D14 - Discovery of certificate larger than 512 bytes in DNS</option>
      				<option value="D15_LDAP_AB_SRVPriority">D15 - Certificate discovery in LDAP based on SRV priority value</option>
      				<option value="D16_LDAP_AB_SRVWeight">D16 - Certificate discovery in LDAP based on SRV weight value</option>
      			</select>
      		</div>
      		<br/><br/>
      		
			
			
      	</form>
      	</div>
<p>
Step 4: Attempt to send a message to the Direct address that you've just copied. Please only send to one address at a time. The test case results message will indicate the test case results. See the test case instructions for additional information. 
</p>
      		
  		
      	
      </div>
    </div>