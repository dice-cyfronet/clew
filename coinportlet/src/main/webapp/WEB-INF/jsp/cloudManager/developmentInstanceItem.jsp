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
		<a class="coin-link" href="${saveAtomicService}">Save atomic service</a><br/>
		<c:if test="${atomicServiceInstance.redirections != null and fn:length(atomicServiceInstance.redirections) > 0}">
			<c:forEach var="redirection" items="${atomicServiceInstance.redirections}">
				<c:if test="${redirection.name == 'ssh'}">
					<c:set var="sshDetailsId">ssh-${atomicServiceInstance.id}</c:set>
					<a class="coin-link" id="${sshDetailsId}" href="">Show SSH details</a>
					<script type="text/javascript">
	    				jQuery(document).ready(function() {
	    					var popupId = '#${sshDetailsId}';
	    					jQuery(popupId).click(function() {
	    						jQuery('<div class="coin-content coin-content-no-tabs"></div>').html(
	    								'Host: ${redirection.host}<br/>' +
	    								'Port: ${redirection.fromPort}<br/>' +
	    								'Login: ${atomicServiceInstance.credential.username}<br/>' +
	    								'Password: ${atomicServiceInstance.credential.password}<br/>' +
	    								'<br/><span style="font-size: small;">(hit the Escape button to close this window)</span>'
	    						).dialog({
	    							closeText: '',
	    							modal: true,
	    							position: 'top'
	    						});
	    						return false;
	    					});
	    				});
	    			</script>
				</c:if>
			</c:forEach>
		</c:if>
	</span>
	<c:if test="${not status.last}">
		<hr/>
	</c:if>
	<portlet:resourceURL var="statusLink" id="instanceStatus">
		<portlet:param name="workflowId" value="${workflowId}"/>
		<portlet:param name="atomicServiceId" value="${atomicServiceInstance.atomicServiceId}"/>
		<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
	</portlet:resourceURL>
	<script type="text/javascript">
	    jQuery(document).ready(function() {
	    	window.updateStatus = function(statusLink, elementId) {
	    		jQuery.get(statusLink, function(status) {
	    			if(jQuery('#' + elementId).text() != status) {
	    				jQuery('#' + elementId).text(status);
	    			}
	    		});
	    		
	    		setTimeout("updateStatus('${statusLink}', '${statusId}')", 2000);
	    	};
	    	
	    	updateStatus('${statusLink}', '${statusId}');
	    });
	</script>
</c:forEach>