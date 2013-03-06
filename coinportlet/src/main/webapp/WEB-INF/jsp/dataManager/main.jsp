<%@ include file="../include.jsp" %>

<portlet:renderURL var="uploadFile">
    <portlet:param name="action" value="uploadFile"/>
    <portlet:param name="path" value="${path}"/>
</portlet:renderURL>
<portlet:actionURL var="createDirectory">
    <portlet:param name="action" value="createDirectory"/>
    <portlet:param name="path" value="${path}"/>
</portlet:actionURL>
<portlet:renderURL var="search">
    <portlet:param name="action" value="search"/>
</portlet:renderURL>

<div class="coin-content coin-content-no-tabs">
	<span class="coin-top-dir">LOBCDER files</span>
	<div>
		<form:form class="coin-form" action='${createDirectory}' modelAttribute='createDirectoryRequest'>
			<div class="coin-form-input">
				<label for="directoryName">
					<spring:message code="data.manager.portlet.create.directory.label"/>
				</label>
				<form:input path="directoryName"/>
				<form:errors path="directoryName" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-submit">
				<input type='submit' value='<spring:message code='data.manager.portlet.create.directory.submit.label'/>'/>
			</div>
		</form:form>
	</div>
	<div id="files" class="coin-file-list">Loading files ...</div>
	<portlet:resourceURL id="fileList" var="fileList">
		<portlet:param name="path" value="${path}"/>
	</portlet:resourceURL>
	<script type="text/javascript">
	    jQuery(document).ready(function() {
	    	window.updateFiles = function () {
		    	jQuery.get('${fileList}', function(fileList) {
		    		var files = '';
		    		
		    		if(fileList !== "") {
		    			files += '<ul>';
			    		var list = fileList.split(';');
			    		
			    		for(var i = 0; file = list[i], i < list.length; i++) {
			    			var fileName = file.split('|')[0];
			    			var url = file.split('|')[1];
			    			var size = file.split('|')[2];
			    			var deleteUrl = file.split('|')[3];
			    			var metadataUrl = file.split('|')[4];
			    			files += '<li><a href="' + url + '" class="coin-link">' + fileName + '</a>';
			    			
			    			if(deleteUrl !== "") {
			    				files += '<a href="' + deleteUrl + '" class="coin-link" style="margin-left: 10px; float: right;">Delete</a>';
			    			}
			    			
			    			if(metadataUrl !== "") {
			    				files += '<a href="' + metadataUrl + '" class="coin-link" style="margin-left: 10px; float: right;">Metadata</a>';
			    			}
			    			
			    			files += '<span class="coin-file-size">' + size + '</span>' +
			    			'</li>';
			    		}
			    		
			    		files += '</ul>';
		    		} else {
		    			files = 'no files';
		    		}
		    		
		    		jQuery('#files').html(files);
		    		setTimeout("updateFiles()", 2000);
		    		
		    		//the size of the page might have changed
		    		//so lets notify the parent window about the change
		    		pm({
		        		target: parent,
		        		type: 'resizeEvent',
		        		data: {size: (document.body.scrollHeight)}
		        	});
		    	});
	    	};
	    	updateFiles();
	    });
	</script>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href="${search}">
					<spring:message code="data.manager.portlet.search.link.label"/>
				</a>
			</li>
			<li>
				<a class="coin-link" href="${uploadFile}">
					<spring:message code="data.manager.portlet.upload.file.link.label"/>
				</a>
			</li>
		</ul>
	</div>
</div>