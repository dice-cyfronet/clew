<div class="coin-tabs-container">
	<div class="coin-tabs">
		<c:forEach var="atomicService" items="${activeAtomicServices}" varStatus="status">
			<c:choose>
				<c:when test="${atomicService.atomicServiceId == currentAtomicServiceId and status.first}">
					<span class="coin-tab coin-selected coin-first">
				</c:when>
				<c:when test="${atomicService.atomicServiceId == currentAtomicServiceId and status.last}">
					<span class="coin-tab coin-selected coin-last">
				</c:when>
				<c:when test="${atomicService.atomicServiceId == currentAtomicServiceId}">
					<span class="coin-tab coin-selected">
				</c:when>
				<c:when test="${status.first}">
					<span class="coin-tab coin-first">
				</c:when>
				<c:when test="${status.last}">
					<span class="coin-tab coin-last">
				</c:when>
				<c:otherwise>
					<span class="coin-tab">
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${atomicService.atomicServiceId == currentAtomicServiceId}">
					${atomicService.name}
				</c:when>
				<c:otherwise>
					<portlet:renderURL var="showAtomicService">
						<portlet:param name="action" value="genericInvoker"/>
						<portlet:param name="currentAtomicService" value="${atomicService.atomicServiceId}"/>
					</portlet:renderURL>
					<a class="coin-link" href="${showAtomicService}">${atomicService.name}</a>
				</c:otherwise>
			</c:choose>
			<br/>
			<c:choose>
				<c:when test="${atomicService.http or atomicService.vnc}">
					(<spring:message code="cloud.manager.portlet.atomic.service.type.label"/>,
				</c:when>
				<c:otherwise>
					(<spring:message code="cloud.manager.portlet.template.type.label"/>,
				</c:otherwise>
			</c:choose>
			<spring:message code="cloud.manager.portlet.number.of.instances.label" arguments="${fn:length(atomicServiceInstances)}"/>)
			</span>
		</c:forEach>
	</div>
	<div class="coin-tab-content">
		<c:forEach var="atomicServiceInstance" items="${atomicServiceInstances}" varStatus="status">
			<span class="coin-instance-label">
				<spring:message code="cloud.manager.portlet.instance.sequence.label" arguments="${status.index + 1}"/>
			</span>
			<c:set var="statusId">status-${atomicServiceInstance.id}</c:set>
			Name: ${atomicServiceInstance.name}<br/>
			Id: ${atomicServiceInstance.id}<br/>
			Status: <span id="${statusId}">${atomicServiceInstance.status}</span><br/>
			<c:if test="${currentAtomicService.http}">
				<portlet:renderURL var="invokeAtomicService">
					<portlet:param name="action" value="invokeAtomicService"/>
					<portlet:param name="atomicServiceId" value="${currentAtomicService.atomicServiceId}"/>
					<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.id}"/>
				</portlet:renderURL>
				<a class="coin-link" href="${invokeAtomicService}"><spring:message code="cloud.manager.portlet.invoke.atomic.service.label"/></a>
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
			
			<c:if test="${not status.last}">
				<hr/>
			</c:if>
		</c:forEach>
	</div>
</div>