<%@ include file="../include.jsp" %>

<portlet:actionURL var="uploadFile">
    <portlet:param name="action" value="uploadFile"/>
</portlet:actionURL>

<div class="coin-content">
	<p><spring:message code="data.manager.portlet.select.file.to.upload.label"/>
	<form method="post" action="${uploadFile}" enctype="multipart/form-data">
	    <input type="file" name="file"/>
	    <input type="submit"/>
	</form>
</div>