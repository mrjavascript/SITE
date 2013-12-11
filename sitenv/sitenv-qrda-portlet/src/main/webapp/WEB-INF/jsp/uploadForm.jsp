<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html" isELIgnored="false" %>

<portlet:defineObjects />

<portlet:actionURL name="uploadFile" var="uploadactionurl"/>
<portlet:actionURL name="ajaxUploadFile" var="ajaxuploadactionurl"/>
<!--<portlet:resourceURL  id="ajaxUploadFile" var="ajaxuploadactionurl"/>-->


<script type="text/javascript">
		window.currentContextPath = "<%=request.getContextPath()%>";
		
		window.ie = (function(){

		    var undef,
		        v = 3,
		        div = document.createElement('div'),
		        all = div.getElementsByTagName('i');

		    while (
		        div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i><![endif]-->',
		        all[0]
		    );

		    return v > 4 ? v : undef;

		}());
</script>      


<article class="module width_full">
	<header>
		<h3>Schema-tron Validator</h3>
	</header>
	<!-- modelAttribute="uploadedFile"  -->
	<div class="module_content">
		<h3>Please select category of QRDA below. (validation may take up to one minute to run)</h3>
		<!-- modelAttribute="uploadedFile" -->
			<form:form id="QRDAValidationForm" method="post" enctype="multipart/form-data" 
				action="${ajaxuploadactionurl}">
			   <ol id="category">
				  <li value="categoryI" class="ui-widget-content">Category I</li>
				  <li value="categoryIII" class="ui-widget-content">Category III</li>
				</ol>
				<input type="hidden" name="category" value="NONE">
				<hr/>
				<div>
				<input placeholder="upload your QRDA document" id="qrdauploadfile" type="file" name="fileData" />
				</div>
				<button id="qrdavalidate_btn" type="submit" >Validate Document</button>
			</form:form>
	
			<!-- render UI if the java script disabled. -->
			<noscript>
				<form:form id="QRDAValidationForm" method="post" enctype="multipart/form-data" modelAttribute="uploadedFile"
				    action="${uploadactionurl}">
					<table>
				   	<tr>  
				     <td>Choose validation Category</td>  
				     <td>
				     	<select id="categorylist" name="category">
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
				     <td> </td>  
				     <td><input id="qrdavalidate_btn" type="submit" value="Upload" />  
				     </td>  
				     <td> </td>  
				    </tr>  
				   </table>
				</form:form>
			</noscript>	
	</div>
</article>


   
   
   
   
   
 
      	
   
 <noscript>  
   <h3>Result:</h3>  
	<!-- <textarea name="nowrap" cols="30" rows="20" wrap="off">${message}</textarea> -->
	  <ul style="color: red;font-weight: bold">
	  <c:forEach var="entry" items="${results}">
	  	<li>${entry}</li>
	  </c:forEach>
	  </ul>
  </noscript>

  