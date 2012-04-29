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
	</span>
	<c:if test="${not status.last}">
		<hr/>
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
	    							var creds = accessMethods.split(':');
	    							jQuery('#${accessMethodsId}').click(function() {
	    								var html = '<b>' + creds[0] + ':</b><br/>' +
		    								'<span style="padding-left: 10px;">Host: ' + creds[1] + '</span><br/>' +
		    								'<span style="padding-left: 10px;">Port: ' + creds[2] + '</span><br/>';
	    								
		    							if(creds.length > 3) {
	    									html += '<span style="padding-left: 10px;">Login: ' + creds[3] + '</span><br/>' +
	    									'<span style="padding-left: 10px;">Password: ' + creds[4] + '</span><br/>';
	    								}
	    								
	    								html += '<br/><span style="font-size: small;">(hit the Escape button to close this window)</span>';
	    	    						jQuery('<div class="coin-content coin-content-no-tabs" style="padding: 10px;"></div>').html(html).dialog({
	    	    							closeText: '',
	    	    							modal: false,
	    	    							position: 'top'
	    	    						});
	    	    						
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