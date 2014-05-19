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
		Direct Transport Test Tool</h1>
	<hr />
	<div class="row">
	<div class="col-md-4 col-md-offset-2">
		<h1>${precannedDirectCount}</h1>
		<p>precanned messages sent</p>
	</div>
	<div class="col-md-4">
		<h1>${uploadedDirectCount}</h1>
		<p>uploaded messages sent</p>
	</div>
</div>
<p>
	<a class="btn btn-lg btn-success" href="statistics">See more stats.</a>
</p>
</div>