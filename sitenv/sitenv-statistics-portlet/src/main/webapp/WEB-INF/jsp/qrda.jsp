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


	<div class="row well">
	<div class="col-md-6" style="text-align: center;">
		<h2>${successQrda}</h2>
		<p>qrdas passed</p>
	</div>
	<div class="col-md-6" style="text-align: center;">
		<h2>${failedQrda}</h2>
		<p>qrdas failed</p>
	</div>
	<div style="width:100%">
		<a class="btn btn-success" href="statistics"  style="width: 100%;">See More Stats</a>
	</div>
	</div>