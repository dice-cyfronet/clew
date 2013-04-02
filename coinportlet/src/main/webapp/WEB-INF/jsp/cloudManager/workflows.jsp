<%@ include file="../include.jsp" %>
<%@ include file="menu.jsp" %>

<div class="coin-content">
	<div id="workflowsPanel" style="padding: 20px;">
		Loading workflow information. Please wait ...
	</div>
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
	    							'<span style="float: right; display: block;"><a class="coin-link" href="' + workflowData[1] + '">Stop</a></span></div>';
	    					
	    					if(workflowData.length > 2) {
	    						var instances = workflowData[2].split(':');
	    						html += '<span style="padding-left: 10px; display: block;">' + instances[0] + ' instances</span><br/>';
	    						
	    						if(instances.length > 1) {
	    							for(var j = 1; j < instances.length; j += 2) {
	    								html += '<span style="padding-left: 20px; display: block;">' + instances[j] + ': <span style="color: #db7024;">' + instances[j + 1] + '</span></span><br/>';
	    							}
	    						} else {
	    							html += '<span style="padding-left: 20px; display: block;">No instances</span><br/>';
	    						}
	    					} else {
	    						html += '<span style="padding-top: 10px; display: block;">No instances</span><br/>';
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