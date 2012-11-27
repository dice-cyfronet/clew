<c:forEach var="atomicServiceInstance" items="${atomicServiceInstances}" varStatus="status">
	<span class="coin-description">
		Instance ${status.index + 1}<br/>
		<c:set var="statusId">status-${atomicServiceInstance.id}</c:set>
		Name: ${atomicServiceInstance.name}<br/>
		Development Id: ${atomicServiceInstance.id}<br/>
		Status: <span id="${statusId}" style="font-weight: bold; color: #db7024;">${atomicServiceInstance.status}</span>
	</span>
	<span class="coin-actions">
		<portlet:renderURL var="saveAtomicService">
			<portlet:param name="action" value="saveAtomicService"/>
			<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
		</portlet:renderURL>
		<c:set var="saveLinkId">saveLink-${atomicServiceInstance.id}</c:set>
		<a id="${saveLinkId}" class="coin-link" href="${saveAtomicService}" style="visibility: hidden;">Save atomic service</a><br/>
		
		<c:set var="accessMethodsId">accessMethods-${atomicServiceInstance.id}</c:set>
		<a class="coin-link" id="${accessMethodsId}" href="#showAccessInfo" style="visibility: hidden;">Show access info</a>
		
		<portlet:actionURL var="shutdownAtomicServiceInstance">
			<portlet:param name="action" value="stopDevInstance"/>
			<portlet:param name="workflowId" value="${workflowId}"/>
			<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
		</portlet:actionURL>
		<c:set var="shutdownInstanceId">shutdownInstance-${atomicServiceInstance.id}</c:set>
		<a class="coin-link" id="${shutdownInstanceId}" href="${shutdownAtomicServiceInstance}" style="visibility: hidden;">Shut down</a>
	</span>
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
	    				if(jQuery('#${shutdownInstanceId}').css('visibility') === 'hidden') {
	    					jQuery('#${shutdownInstanceId}').css('visibility', 'visible');
	    				}
	    				
	    				if(jQuery('#${saveLinkId}').css('visibility') === 'hidden') {
	    					jQuery('#${saveLinkId}').css('visibility', 'visible');
	    				}
	    				
	    				if(jQuery('#${accessMethodsId}').css('visibility') === 'hidden') {
	    					jQuery.get('${accessMethodsLink}', function(accessMethods) {
	    						if(accessMethods !== '') {
	    							jQuery('#${accessMethodsId}').click(function() {
	    								var methods = accessMethods.split('|');
	    								var html = '';
		    							
	    								for(var i = 0; i < methods.length; i++) {
			    							var creds = methods[i].split(':');
		    								html += '<b>' + creds[0] + ':</b><br/>' +
			    								'<span style="padding-left: 10px;">Host: ' + creds[1] + '</span><br/>' +
			    								'<span style="padding-left: 10px;">Port: ' + creds[2] + '</span><br/>';
		    								
			    							if(creds.length > 3) {
		    									html += '<span style="padding-left: 10px;">Key: ' + creds[3] + '</span><br/>';
		    								}
			    							
			    							if(creds[0] == 'ssh') {
			    								html += '<span style="font-size: small; font-style: italic; display: block; margin-top: 10px;">' +
			    										'Login using the root account (e.g. <span style="font-style: normal; font-family: ' +
			    										'monospace; white-space: nowrap;">ssh root@{host} -i {private_key_file}</span>)</span><br/>'
			    							}
		    							}
		    							
		    							html += '<br/><a class="coin-link" href="#closeAccessInfoWindow" ' +
											'onclick="window.popup.dialog(\'close\'); window.popup = null; return false;">Close</a>';
	    								
	    								if(window.popup != null) {
	    									window.popup.dialog('close');
	    								}
	    								
	    	    						window.popup = jQuery('<div class="coin-content coin-content-no-tabs" style="padding: 10px;"></div>').html(html).dialog({
	    	    							closeText: '',
	    	    							modal: false,
	    	    							position: 'top',
	    	    							autoOpen: false,
	    	    							draggable: false
	    	    						});
	    	    						window.popup.dialog('open');
	    	    						
	    	    						return false;
	    	    					});
	    							jQuery('#${accessMethodsId}').css('visibility', 'visible');
	    							setTimeout("updates['${statusId}']('${statusLink}', '${statusId}')", 2000);
	    						}
	    					});
	    					timeoutSet = true;
	    				}
	    			} else {
	    				if(jQuery('#${saveLinkId}').css('visibility') === 'visible') {
	    					jQuery('#${saveLinkId}').css('visibility', 'hidden');
	    				}
	    				
						if(jQuery('#${accessMethodsId}').css('visibility') === 'visible') {
							jQuery('#${accessMethodsId}').css('visibility', 'hidden');
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