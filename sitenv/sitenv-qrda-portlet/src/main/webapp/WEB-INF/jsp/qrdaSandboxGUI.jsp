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

<article id="schematronValidationPanel" class="module width_full">
	<header>
		<h3>Schematron Validator</h3>
	</header>
	<div class="module_content">
		<p>
			1.Select QRDA Category I or III for validating your input file
			<br>
			2.Select QRDA document
			<br>
			3.Click on Validate Document button			
			<br>
		</p>
		
			<!-- modelAttribute="uploadedFile" -->
			<form:form id="QRDAValidationForm" method="post" class="scriptenabled" enctype="multipart/form-data" 
				action="${ajaxuploadactionurl}" post="${uploadactionieurl}">
				<input type="text" style="visibility: hidden;height:0.1px"
						value=""
						name="category" 
						class="validate[required]" 
						data-errormessage-value-missing="Please select an category !"
						data-prompt-position="topLeft:0">
						
			   <ol id="category" style="clear:both">
				  <li value="categoryI" class="ui-widget-content">Category I</li>
				  <li value="categoryIII" class="ui-widget-content">Category III</li>
				</ol>
				
				<hr/>
				<div>
				<input placeholder="upload your QRDA document" 
						id="qrdauploadfile" 
						type="file" 
						name="fileData"
						data-errormessage-value-missing="QRDA document is required!"
						data-prompt-position="topLeft:0"
						 />
				</div>
				<button id="qrdavalidate_btn" type="submit" >Validate Document</button>
			</form:form>
			
			<!-- render UI to render if the java script disabled. -->
			<noscript>
				<form:form id="QRDAValidationForm" method="post" enctype="multipart/form-data" modelAttribute="uploadedFile"
				    action="${uploadactionurl}">
					<table>
				   	<tr>  
				     <td>Choose validation Category</td>  
				     <td>
				     	<select id="categorylist" name="category" style="width:100%">
				     		<option value="categoryI">Category I</option>
				     		<option value="categoryIII">Category III</option>
				     	</select>
				     </td>
				     <td style="color: red; font-style: italic;"><form:errors  
				       path="category" />  
				     </td>  
				    </tr> 
				    <tr>  
				     <td>Upload File: </td>  
				     <td>
				     	<input type="file" name="fileData"/>
				     </td>  
				     <td style="color: red; font-style: italic;"><form:errors  
				       path="fileData" />
				     </td>  
				    </tr>  
				    <tr>  
				     <td colspan=2>
				     	<input id="qrdavalidate_btn" type="submit" value="Validate Document" />  
				     </td>  
				     <td> </td>  
				    </tr>  
				   </table>
				</form:form>
			</noscript>	
		</div>
</article>

<div id="postbackValidationResult" style="display:none" >
	<c:out escapeXml="false" value="${validationResultJson}"/>
</div>


<!-- Result panels for the ajax result. -->   
<div id="ValidationResult" style="display:none" class="jscriptenabled">
	<div id="tabs">
	  <ul>
	    <li><a href="#tabs-1">Validation Result</a></li>
	    <li><a href="#tabs-2">Original QRDA</a></li>
	    <!-- <li><a href="#tabs-3">Smart CCDA Result</a></li> -->
	  </ul>
	  
		<div id="tabs-1">
		  <p>
		  	place holder.
		  </p>
		</div>
		<div id="tabs-2">
		  <h2>Original Document</h2>
		  <p>Under construction.</p>
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

  