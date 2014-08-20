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

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />



<div id="DirectToolBoxPanel">
	<div id="tabs">
	  <ul>
	    <li><a href="#tabs-1">Received messages</a></li>
	    <li><a href="#tabs-2">Sent messages</a></li>
	    <li><a href="#tabs-3">James2 log</a></li>
	  </ul>
	  <div id="tabs-1">
	  	<form id="messagelistingsettings" action="http://localhost:8080/delegate/ListMessages">
	  		<input name="host" type="hidden" value="direct.sitenv.org" />
			<input name="username" type="hidden" value="provider1" />
			<input name="password" type="hidden" value="8sUNwy/nLdJVkowQnbnztQ==" />
			<input name="provider" type="hidden" value="pop3" />
			<input name="foldername" type="hidden" value="INBOX" />
	  	</form>
	   	<article class="module width_full">
	   		<header><h3>Provider1@direct.sitenv.org Inbox</h3></header>
			<div class="message_list">
				<div class="module_content">
				</div>
			</div>
			<footer>
				<div class="submit_link">
				<!-- 
						<select>
							<option>Draft</option>
							<option>Published</option>
						</select>
				-->
					<input type="submit" value="Refresh" class="alt_btn">
				</div>
			</footer>	
	   	</article> 
	  </div>
	  <div id="tabs-2">
	    <p>Under construction.</p>
	  </div>
	  <div id="tabs-3">
	  	<p>Under construction.</p>
	  </div>
	</div>
</div>

	
