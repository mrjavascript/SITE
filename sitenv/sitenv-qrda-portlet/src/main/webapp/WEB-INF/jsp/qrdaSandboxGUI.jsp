<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html" isELIgnored="false" %>

<portlet:defineObjects />

<!-- links for the regular ajax call -->
<portlet:actionURL name="uploadFile" var="uploadactionurl"/>
<!-- action url for browser version lower than IE9 -->
<portlet:actionURL name="uploadFileIE" var="uploadactionieurl"/>
<portlet:resourceURL  id="ajaxUploadFile" var="ajaxuploadactionurl"/>

<script type="text/javascript">
		window.currentContextPath = "<%=request.getContextPath()%>";
</script>
<noscript> 
	<p style="color:red">Javascript is disabled. Please enable javascript for optimal usability.</p>
</noscript>

<div class="panel panel-default" id="qrdaWidget">
      <div class="panel-heading"><h3 class="panel-title">Schematron Validator</h3></div>
  		<div class="panel-body">
		<p>
			1.Select QRDA Category I or III for validating your input file
			<br>
			2.Select a QRDA document
			<br>
			3.Click on Validate Document button			
			<br>
		</p>
		<div class="well">
			<!-- modelAttribute="uploadedFile" -->
			<form:form id="QRDAValidationForm" method="post" class="scriptenabled" enctype="multipart/form-data" 
				action="${ajaxuploadactionurl}">
				
				<label for="category">Select a QRDA Category:</label><br/>
				<select id="category" name="category">
					<option value="categoryI">Category I</option>
					<option value="categoryIII">Category III</option>      				
				</select>

				<hr/>
				<!-- div>
				<input placeholder="upload your QRDA document" 
						id="qrdauploadfile" 
						type="file" 
						name="fileData"
						data-errormessage-value-missing="QRDA document is required!"
						data-prompt-position="topLeft:0"
						 />
				</div -->
				
				<noscript><input type="hidden" name="redirect" value="true" /></noscript>
				<label for="qrdauploadfile">Select a QRDA File: </label><br/>
				<div id="qrdauploaderrorlock" style="position:relative;">
					<div class="row">
						<div class="col-md-12">
					
					<span class="btn btn-success fileinput-button" id="qrdauploadfile-btn"> <i
								class="glyphicon glyphicon-plus"></i>&nbsp;<span>Select a QRDA...</span>
								<!-- The file input field used as target for the file upload widget -->
								<input id="qrdauploadfile" type="file" name="qrdauploadfile" class="validate[required, custom[xmlfileextension[xml|XML]], custom[maxCCDAFileSize]]" />
						</span>
						<div id="qrdauploadfiles" class="files"></div>
				</div>
				</div>
				</div>
				<hr/>
				<button id="qrdavalidate_btn" type="submit"
					class="btn btn-primary start" onclick="return false;">
					<i class="glyphicon glyphicon-ok"></i> <span>Validate Document</span>
				</button>
			</form:form>
			</div>
			
		</div>
</div>

<div id="postbackValidationResult" style="display:none" >
	<c:out escapeXml="false" value="${validationResultJson}"/>
</div>


      	<div class="modal modal-wide fade" id="resultModal" tabindex="-1" role="dialog" aria-labelledby="resultModalLabel" aria-hidden="true">
	      	<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<ul class="nav nav-tabs" id="resultModalTabs">
					  <li><a href="#tabs-1" data-toggle="tab">Validation Result</a></li>
					  <li><a href="#tabs-2" data-toggle="tab">Original QRDA</a></li>
					</ul>
				</div>
				<div class="modal-body">  
					<div id="ValidationResult">
						<div class="tab-content" id="resultTabContent">
							<div class="tab-pane" id="tabs-1">
								<h1 align="center">QRDA Document Validation Results</h1>
								<p></p>
							</div>
							<div class="tab-pane" id="tabs-2">
								<h1 align="center">Original QRDA Document</h1>
								<p></p>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close Results</button>
				</div>
			</div>
			</div>
			</div>

<!-- Result panel for the regular postback. -->   
 <noscript>  
  <h3>Result:</h3>  
  <p>${results}</p>
  <!-- <ul style="color: red;font-weight: bold">
  <c:forEach var="entry" items="${results}">
  	<li>${entry}</li>
  </c:forEach>
  </ul>
   -->
</noscript>

  