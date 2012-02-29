<%@ include file="../include.jsp" %>
<%@ include file="menu.jsp" %>

<portlet:renderURL var="startAtomicService">
    <portlet:param name="action" value="startAtomicService"/>
</portlet:renderURL>

<div class="coin-content">
	<c:choose>
		<c:when test="${fn:length(activeAtomicServices) > 0}">
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
						<c:set var="statusId">status-${atomicServiceInstance.instanceId}</c:set>
						Name: ${atomicServiceInstance.name}<br/>
						Id: ${atomicServiceInstance.instanceId}<br/>
						Status: <span id="${statusId}">${atomicServiceInstance.status}</span>
						
						<portlet:resourceURL var="statusLink" id="instanceStatus">
							<portlet:param name="workflowId" value="${workflowId}"/>
							<portlet:param name="atomicServiceId" value="${atomicServiceInstance.atomicServiceId}"/>
							<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.instanceId}"/>
						</portlet:resourceURL>
						<script type="text/javascript">
						    jQuery(document).ready(function() {
						    	window.updateStatus = function(statusLink, elementId) {
						    		jQuery.get(statusLink, function(status) {
						    			jQuery('#' + elementId).text(status);
						    		});
						    	};
						    	
						    	setInterval("updateStatus('${statusLink}', '${statusId}')", 5000);
						    });
						</script>
						
						
						<c:if test="${not status.last}">
							<hr/>
						</c:if>
					</c:forEach>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<spring:message code="cloud.manager.portlet.no.atomic.service.instances"/>
		</c:otherwise>
	</c:choose>
	<div class="menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href="${startAtomicService}"><spring:message code="cloud.manager.portlet.start.atomic.service.instance.label"/></a>
			</li>
			<c:if test="${fn:length(activeAtomicServices) > 0}">
				<li>
					<portlet:actionURL var="stopPortalAtomicServices">
						<portlet:param name="action" value="stopWorkflow"/>
						<portlet:param name="workflowType" value="portal"/>
					</portlet:actionURL>
					<a class="coin-link" href="${stopPortalAtomicServices}"><spring:message code="cloud.manager.portlet.stop.portal.workflow.label"/></a>
				</li>
			</c:if>
		</ul>
	</div>
</div>