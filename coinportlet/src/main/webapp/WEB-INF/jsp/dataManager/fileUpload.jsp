<%@ include file="../include.jsp" %>

<portlet:actionURL var="uploadFile">
    <portlet:param name="action" value="uploadFile"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<span class="coin-top-dir">LOBCDER file upload</span>
	<form class="coin-form" method="post" action="${uploadFile}" enctype="multipart/form-data">
		<div class="coin-form-input">
			<label for="file"><spring:message code="data.manager.portlet.select.file.to.upload.label"/></label>
		    <input type="file" name="file"/>
	    </div>
	    <div class="coin-form-submit">
	    	<input type="submit" value="Upload"/>
	    </div>
	</form>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href="<portlet:renderURL/>">
					Go back to file list
				</a>
			</li>
		</ul>
	</div>
</div>