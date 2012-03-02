<%@ include file="../include.jsp" %>

<div class="coin-content">
	<div id="files"></div>
	<portlet:resourceURL id="fileList" var="fileList"/>
	<portlet:resourceURL id="getFile" var="getFile">
		<portlet:param name="fileName" value=""/>
	</portlet:resourceURL>
	<script type="text/javascript">
	    jQuery(document).ready(function() {
	    	jQuery.get('${fileList}', function(fileList) {
	    		var files = '';
	    		console.log(fileList);
	    		console.log(fileList.split(';'));
	    		
	    		var list = fileList.split(';');
	    		
	    		for(var i = 0; file = list[i], i < list.length; i++) {
	    			files += '<a class="coin-link" href="${getFile}">' + file + '</a><br/>';
	    		}
	    		
	    		jQuery('#files').html(files);
	    	});
	    	
	    	//setInterval("updateStatus('${statusLink}', '${statusId}')", 5000);
	    });
	</script>
</div>