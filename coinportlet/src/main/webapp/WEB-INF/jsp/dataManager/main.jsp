<%@ include file="../include.jsp" %>

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
		    	});
	    	};
	    	
	    	setInterval("updateFiles()", 5000);
	    });
	</script>
</div>