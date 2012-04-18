<div class="coin-tab-content">
	<c:forEach var="atomicServiceInstance" items="${atomicServiceInstances}" varStatus="status">
		<span class="coin-instance-label">
			<spring:message code="cloud.manager.portlet.instance.sequence.label" arguments="${status.index + 1}"/>
		</span>
		<c:set var="statusId">status-${atomicServiceInstance.id}</c:set>
		Name: ${atomicServiceInstance.name}<br/>
		Development Id: ${atomicServiceInstance.id}<br/>
		Status: <span id="${statusId}">${atomicServiceInstance.status}</span><br/>
		
		<c:if test="${atomicServiceInstance.redirections != null and fn:length(atomicServiceInstance.redirections) > 0}">
			Access methods:
			<c:forEach var="redirection" items="${atomicServiceInstance.redirections}">
				<c:if test="${redirection.name == 'ssh'}">
				Type: ${redirection.name}, host: ${redirection.host}, port: ${redirection.fromPort}<br/>
				<c:if test="${atomicServiceInstance.credential != null}">
					Credentials: ${atomicServiceInstance.credential.username}:${atomicServiceInstance.credential.password}<br/>
				</c:if>
				</c:if>
			</c:forEach>
		</c:if>
		
		<c:if test="${currentAtomicService.http}">
			<portlet:renderURL var="invokeAtomicService">
				<portlet:param name="action" value="invokeAtomicService"/>
				<portlet:param name="atomicServiceId" value="${currentAtomicService.atomicServiceId}"/>
				<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
			</portlet:renderURL>
			<a class="coin-link" href="${invokeAtomicService}"><spring:message code="cloud.manager.portlet.invoke.atomic.service.label"/></a><br/>
		</c:if>
		
		<portlet:renderURL var="saveAtomicService">
			<portlet:param name="action" value="saveAtomicService"/>
			<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
		</portlet:renderURL>
		<a class="coin-link" href="${saveAtomicService}">Save atomic service</a><br/>
		
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
		
		<c:if test="${not status.last}">
			<hr/>
		</c:if>
	</c:forEach>
</div>