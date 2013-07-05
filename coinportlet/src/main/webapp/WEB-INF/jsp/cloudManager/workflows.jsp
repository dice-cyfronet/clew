<%@ include file="../include.jsp" %>
<%@ include file="menu.jsp" %>

<div>
	<p id="workflowsPanel">Loading workflow information. Please wait ...</p>
	<portlet:resourceURL var="workflowsLink" id="workflows">
		<portlet:param name="workflowId" value="${workflowId}"/>
		<portlet:param name="atomicServiceId" value="${atomicServiceInstance.atomicServiceId}"/>
		<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
	</portlet:resourceURL>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			window.updateStatus = function() {
	    		jQuery.get('${workflowsLink}', function(workflows) {
	    			if(workflows === '') {
	    				jQuery('#workflowsPanel').html('No workflows');
	    			} else {
	    				var html = '';
	    				var workflowSections = workflows.split('|');
	    				
	    				for(var i = 0; i < workflowSections.length; i++) {
	    					var workflowData = workflowSections[i].split(';');
	    					html += '<div style="float: left; width: 100%;"><span style="display: block; float: left;"><b>Workflow with id ' + workflowData[0] + '</b></span>' +
	    							'<span style="float: right; display: block;"><a href="' + workflowData[1] + '">Stop</a></span></div>';
	    					
	    					if (workflowData.length > 2) {
		    					for (var j = 2; j < workflowData.length; j++) {
		    						var instanceData = workflowData[j].split(':');
		    						html += '<div style="clear: both;"></div><span style="padding-left: 10px; display: block; margin-top: 15px;"><em>' + instanceData[0] + ' instances</em></span>';
		    						html += '<span style="padding-left: 20px; display: block;">' + instanceData[1] + ': <span style="color: #db7024;">' + instanceData[2] + '</span></span>';
		    					}
	    					} else {
	    						html += '<span style="padding-top: 10px; display: block;">No instances</span>';
	    					}
	    				}
	    				
	    				jQuery('#workflowsPanel').html(html);
	    				
	    				//in case the size of the page changes...
	    				pm({
			        		target: parent,
			        		type: 'resizeEvent',
			        		data: {size: (document.body.scrollHeight)}
			        	});
	    			}
	    			
	    			setTimeout("updateStatus()", 2000);
	    		});
			};
			
			updateStatus();
		});
	</script>
</div>