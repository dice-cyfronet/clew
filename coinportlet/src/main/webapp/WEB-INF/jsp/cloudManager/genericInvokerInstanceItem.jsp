<c:forEach var="atomicServiceInstance" items="${atomicServiceInstances}" varStatus="status">
	<span class="coin-description">
		Instance ${status.index + 1}<br/>
		<c:set var="statusId">status-${atomicServiceInstance.id}</c:set>
		Name: ${atomicServiceInstance.name}<br/>
		Development Id: ${atomicServiceInstance.id}<br/>
		Status: <span id="${statusId}" style="font-weight: bold; color: #db7024;">${atomicServiceInstance.status}</span>
	</span>
	<span class="coin-actions">
		<c:if test="${atomicService.http and atomicService.published}">
			<portlet:renderURL var="invokeAtomicService">
				<portlet:param name="action" value="invokeAtomicService"/>
				<portlet:param name="atomicServiceId" value="${atomicService.atomicServiceId}"/>
				<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
			</portlet:renderURL>
			<c:set var="invokeId">invokeId-${atomicServiceInstance.id}</c:set>
			<a id="${invokeId}" class="coin-link" href="${invokeAtomicService}" style="visibility: hidden;"><spring:message code="cloud.manager.portlet.invoke.atomic.service.label"/></a>
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
	    	if(window.updates == null) {
	    		window.updates = {};
	    	}
	    	
	    	window.updates['${statusId}'] = function(statusLink, elementId) {
	    		jQuery.get(statusLink, function(status) {
	    			if(jQuery('#' + elementId).text() != status) {
	    				jQuery('#' + elementId).text(status);
	    			}
	    			
	    			if(jQuery("#${invokeId}")) {
			    		if(status === 'running') {
			    			if(jQuery('#${invokeId}').css('visibility') === 'hidden') {
				    			jQuery('#${invokeId}').css('visibility', 'visible');
				    		}
			    		} else {
			    			if(jQuery('#${invokeId}').css('visibility') === 'visible') {
				    			jQuery('#${invokeId}').css('visibility', 'hidden');
				    		}
			    		}
		    		}
	    			
	    			setTimeout("updates['${statusId}']('${statusLink}', '${statusId}')", 2000);
	    		});
	    	};
	    	
	    	updates['${statusId}']('${statusLink}', '${statusId}');
	    });
	</script>
</c:forEach>