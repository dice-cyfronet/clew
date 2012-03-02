<%@ include file="../include.jsp" %>

<div class="coin-content">
	<div id="files" class="coin-file-list"></div>
	<portlet:resourceURL id="fileList" var="fileList"/>
	<script type="text/javascript">
	    jQuery(document).ready(function() {
	    	window.updateFiles = function () {
		    	jQuery.get('${fileList}', function(fileList) {
		    		var files = '';
		    		var list = fileList.split(';');
		    		
		    		for(var i = 0; file = list[i], i < list.length; i++) {
		    			var fileName = file.split('|')[0];
		    			var url = file.split('|')[1];
		    			files += '<a class="coin-link" href="' + url + '">' + fileName + '</a>';
		    		}
		    		
		    		jQuery('#files').html(files);
		    	});
	    	};
	    	
	    	setInterval("updateFiles()", 5000);
	    });
	</script>
</div>