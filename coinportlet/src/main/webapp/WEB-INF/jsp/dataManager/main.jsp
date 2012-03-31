<%@ include file="../include.jsp" %>

<portlet:renderURL var="uploadFile">
    <portlet:param name="action" value="uploadFile"/>
</portlet:renderURL>

<div class="coin-content">
	<span class="coin-top-dir">LOBCDER files</span>
	<div id="files" class="coin-file-list">Loading files ...</div>
	<portlet:resourceURL id="fileList" var="fileList"/>
	<script type="text/javascript">
	    jQuery(document).ready(function() {
	    	window.updateFiles = function () {
		    	jQuery.get('${fileList}', function(fileList) {
		    		var files = '';
		    		
		    		if(fileList !== "") {
			    		var list = fileList.split(';');
			    		
			    		for(var i = 0; file = list[i], i < list.length; i++) {
			    			var fileName = file.split('|')[0];
			    			var url = file.split('|')[1];
			    			files += '<a class="coin-link" href="' + url + '">' + fileName + '</a>';
			    		}
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
		        		data: {size: (document.body.scrollHeight + 20)}
		        	});
		    	});
	    	};
	    	updateFiles();
	    });
	</script>
	<div class="menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href="${uploadFile}">
					<spring:message code="data.manager.portlet.upload.file.link.label"/>
				</a>
			</li>
		</ul>
	</div>
</div>