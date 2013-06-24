<c:forEach var="atomicServiceInstance" items="${atomicServiceInstances}" varStatus="status">
	<c:choose>
		<c:when test="${status.index > 0}">
			<div class="span8 offset2">
		</c:when>
		<c:otherwise>
			<div class="span8">
		</c:otherwise>
	</c:choose>
		<c:set var="statusId">status-${atomicServiceInstance.id}</c:set>
		Development Id: ${atomicServiceInstance.id}<br/>
		Site: ${atomicServiceInstance.siteId}<br/>
		Status: <span id="${statusId}" style="font-weight: bold; color: #db7024;">${atomicServiceInstance.status}</span>
	</div>
	<div class="span2">
		<portlet:renderURL var="saveAtomicService">
			<portlet:param name="action" value="saveAtomicService"/>
			<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
		</portlet:renderURL>
		<c:set var="saveLinkId">saveLink-${atomicServiceInstance.id}</c:set>
		<a id="${saveLinkId}" href="${saveAtomicService}" style="visibility: hidden;">Save atomic service</a><br/>
		<c:set var="accessMethodsId">accessMethods-${atomicServiceInstance.id}</c:set>
		<a id="${accessMethodsId}" href="#" style="visibility: hidden;" data-toggle="popover" data-original-title="Access info" title="" data-placement="left">Show access info</a><br/>
		
		<portlet:actionURL var="shutdownAtomicServiceInstance">
			<portlet:param name="action" value="stopDevInstance"/>
			<portlet:param name="workflowId" value="${workflowId}"/>
			<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
		</portlet:actionURL>
		<c:set var="shutdownInstanceId">shutdownInstance-${atomicServiceInstance.id}</c:set>
		<a id="${shutdownInstanceId}" href="${shutdownAtomicServiceInstance}" style="visibility: hidden;">Shut down</a><br/>
		
		<portlet:renderURL var="editEndpoints">
			<portlet:param name="action" value="editEndpoints"/>
			<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
			<portlet:param name="workflowId" value="${workflowId}"/>
		</portlet:renderURL>
		<c:set var="editEndpointsId">editEndpoints-${atomicServiceInstance.id}</c:set>
		<a id="${editEndpointsId}" href="${editEndpoints}" style="visibility: hidden;">Installed applications and endpoints</a>
	</div>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery('#${shutdownInstanceId}').click(function() {
				if(!confirm("<spring:message code='cloud.manager.portlet.stop.instance.confirmation.label'/>")) {
					return false;
				}
			});
		});
	</script>
	<c:if test="${not status.last}">
		<hr style="margin-left: 20px; margin-right: 20px;"/>
	</c:if>
	<portlet:resourceURL var="statusLink" id="instanceStatus">
		<portlet:param name="workflowId" value="${workflowId}"/>
		<portlet:param name="atomicServiceId" value="${atomicServiceInstance.atomicServiceId}"/>
		<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
	</portlet:resourceURL>
	<portlet:resourceURL var="accessMethodsLink" id="accessMethods">
		<portlet:param name="workflowId" value="${workflowId}"/>
		<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
	</portlet:resourceURL>
	<script type="text/javascript">
	    jQuery(document).ready(function() {
	    	if(window.updates == null) {
	    		window.updates = {};
	    	}
	    	
	    	window.updates['${statusId}'] = function(statusLink, elementId) {
	    		var timeoutSet = false;
	    		jQuery.get(statusLink, function(status) {
	    			if(jQuery('#' + elementId).text() != status) {
	    				jQuery('#' + elementId).text(status);
	    			}
	    			
	    			if(status === 'running') {
	    				if(jQuery('#${saveLinkId}').css('visibility') === 'hidden') {
	    					jQuery('#${saveLinkId}').css('visibility', 'visible');
	    				}
	    				
	    				if(jQuery('#${accessMethodsId}').css('visibility') === 'hidden') {
	    					jQuery.get('${accessMethodsLink}', function(accessMethods) {
	    						if(accessMethods !== '') {
	    							var methods = accessMethods.split('|');
    								var html = '';
	    							
    								for(var i = 0; i < methods.length; i++) {
		    							var creds = methods[i].split(':');
	    								html += '<strong><small>' + creds[0] + ': </small></strong>' +
		    								'<small>' + creds[1] + ':' + creds[2] + '</small>';
	    								
		    							if(creds.length > 3) {
	    									html += '<small> (' + creds[3] + ')</small>';
	    								}
		    							
		    							if(creds[0] == 'ssh') {
		    								html += '<span style="font-size: small; font-style: italic; display: block; margin-top: 10px;">' +
		    										'Login using the root account (e.g. <span style="font-style: normal; font-family: ' +
		    										'monospace; ">ssh root@' + creds[1] + ' -p ' + creds[2] + ' -i {private_key_file}</span>)</span>';
		    							}
	    							}
    	    						
	    							jQuery('#${accessMethodsId}').popover({
	    								html: true,
	    								content: html
	    							});
	    							jQuery('#${accessMethodsId}').css('visibility', 'visible');
	    							setTimeout("updates['${statusId}']('${statusLink}', '${statusId}')", 2000);
	    						}
	    					});
	    					timeoutSet = true;
	    				}
	    				
	    				if(jQuery('#${editEndpointsId}').css('visibility') === 'hidden') {
	    					jQuery('#${editEndpointsId}').css('visibility', 'visible');
	    				}
	    			} else {
	    				if(jQuery('#${saveLinkId}').css('visibility') === 'visible') {
	    					jQuery('#${saveLinkId}').css('visibility', 'hidden');
	    				}
	    				
						if(jQuery('#${accessMethodsId}').css('visibility') === 'visible') {
							jQuery('#${accessMethodsId}').css('visibility', 'hidden');
	    				}
						
						if(jQuery('#${editEndpointsId}').css('visibility') === 'visible') {
							jQuery('#${editEndpointsId}').css('visibility', 'hidden');
	    				}
	    			}
	    			
	    			if (status === 'running' || status === 'booting') {
	    				if(jQuery('#${shutdownInstanceId}').css('visibility') === 'hidden') {
	    					jQuery('#${shutdownInstanceId}').css('visibility', 'visible');
	    				}
	    			} else {
	    				if(jQuery('#${shutdownInstanceId}').css('visibility') === 'visible') {
			    			jQuery('#${shutdownInstanceId}').css('visibility', 'hidden');
			    		}
	    			}
	    			
	    			if(!timeoutSet) {
		    			setTimeout("updates['${statusId}']('${statusLink}', '${statusId}')", 2000);
		    		}
	    		});
	    	};
	    	
	    	updates['${statusId}']('${statusLink}', '${statusId}');
	    });
	</script>
</c:forEach>