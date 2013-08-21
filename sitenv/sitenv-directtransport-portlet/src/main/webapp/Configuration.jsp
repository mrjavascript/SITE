<%@include file="/init.jsp" %>
 
<form action="<liferay-portlet:actionURL portletConfiguration="true" />"
method="post" name="<portlet:namespace />fm">
 
<input name="<portlet:namespace /><%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
 
<table class="lfr-table">
<tr>
    <td>Direct from email:</td>
	<td><input type="text" name="<portlet:namespace />fromendpoint"  value="<%=fromemail %>"/>
    </td>
</tr>
<tr>
    <td>SMTP host name:</td>
	<td><input type="text" name="<portlet:namespace />smtphost"  value="<%=SMTPServerHost %>"/>
    </td>
</tr>
<tr>
    <td>SMTP port:</td>
	<td><input type="text" name="<portlet:namespace />smtpport"  value="<%=SMTPServerPort %>"/>
    </td>
</tr>
<tr>
    <td>SMTP user name:</td>
	<td><input type="text" name="<portlet:namespace />smtpuser"  value="<%=SMTPAuthUserName %>"/>
    </td>
</tr>
<tr>
    <td>SMTP pass word:</td>
	<td><input type="text" name="<portlet:namespace />smptpswrd"  value="<%=SMTPAuthPassword %>"/>
    </td>
</tr>
<tr>
    <td>Enable SSL:</td>
	<td><input type="text" name="<portlet:namespace />enablessl"  value="<%=EnableSSL %>"/>
    </td>
</tr>
<tr>
       <td colspan="2">
            <input type="button" value="<liferay-ui:message key="save" />" onClick="submitForm(document.<portlet:namespace />fm);" />
       </td>
	</tr>
</table>

</form>