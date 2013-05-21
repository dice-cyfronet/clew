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
		<c:if test="${atomicService.published}">
			<portlet:renderURL var="invokeAtomicService">
				<portlet:param name="action" value="invokeAtomicService"/>
				<portlet:param name="atomicServiceId" value="${atomicService.atomicServiceId}"/>
				<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
			</portlet:renderURL>
			<c:set var="invokeId">invokeId-${atomicServiceInstance.id}</c:set>
			<a id="${invokeId}" class="coin-link" href="${invokeAtomicService}" style="visibility: hidden;"><spring:message code="cloud.manager.portlet.invoke.atomic.service.label"/></a><br/>
		</c:if>
		<portlet:actionURL var="shutdownAtomicServiceInstance">
			<portlet:param name="action" value="stopInvokerInstance"/>
			<portlet:param name="workflowId" value="${workflowId}"/>
			<portlet:param name="atomicServiceId" value="${atomicServiceInstance.atomicServiceId}"/>
		</portlet:actionURL>
		<c:set var="shutdownInstanceId">shutdownInstance-${atomicServiceInstance.id}</c:set>
		<a class="coin-link" id="${shutdownInstanceId}" href="${shutdownAtomicServiceInstance}" style="visibility: hidden;">Shut down</a>
	</div>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery('#${shutdownInstanceId}').click(function() {
				if(!confirm("<spring:message code='cloud.manager.portlet.stop.as.confirmation.label'/>")) {
					return false;
				}
			});
		});
	</script>
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
			    			
			    			if(jQuery('#${shutdownInstanceId}').css('visibility') === 'hidden') {
				    			jQuery('#${shutdownInstanceId}').css('visibility', 'visible');
				    		}
			    		} else {
			    			if(jQuery('#${invokeId}').css('visibility') === 'visible') {
				    			jQuery('#${invokeId}').css('visibility', 'hidden');
				    		}
			    			
			    			if(jQuery('#${shutdownInstanceId}').css('visibility') === 'visible') {
				    			jQuery('#${shutdownInstanceId}').css('visibility', 'hidden');
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