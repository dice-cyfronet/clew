<%@ include file="../include.jsp" %>

<%@ include file="menu.jsp" %>

<portlet:renderURL var="startAtomicService">
    <portlet:param name="action" value="startAtomicService"/>
    <portlet:param name="workflowType" value="development"/>
</portlet:renderURL>

<div class="coin-content">
	<c:choose>
		<c:when test="${fn:length(developmentAtomicServiceInstances) > 0}">
			Number of development atomic services: ${fn:length(developmentAtomicServiceInstances)} 
		</c:when>
		<c:otherwise>
			<spring:message code="cloud.manager.portlet.no.development.instances"/>
		</c:otherwise>
	</c:choose>
	<div class="menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href="${startAtomicService}"><spring:message code="cloud.manager.portlet.start.atomic.service.instance.label"/></a>
			</li>
			<c:if test="${fn:length(developmentAtomicServiceInstances) > 0}">
				<li>
					<portlet:actionURL var="stopAtomicServices">
						<portlet:param name="action" value="stopWorkflow"/>
						<portlet:param name="workflowType" value="development"/>
					</portlet:actionURL>
					<a class="coin-link" href="${stopAtomicServices}"><spring:message code="cloud.manager.portlet.stop.portal.workflow.label"/></a>
				</li>
			</c:if>
		</ul>
	</div>
</div>