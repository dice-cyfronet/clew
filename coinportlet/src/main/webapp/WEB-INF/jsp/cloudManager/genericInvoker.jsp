<%@ include file="../include.jsp" %>

<%@ include file="menu.jsp" %>

<portlet:renderURL var="startAtomicService">
    <portlet:param name="action" value="startAtomicService"/>
</portlet:renderURL>

<c:if test="${developerMode}">
	<spring:message code="cloud.manager.portlet.developer.mode.label"/>
</c:if>

<c:choose>
	<c:when test="${fn:length(activeAtomicServices) > 0}">
		<div>
			<c:forEach var="atomicService" items="${activeAtomicServices}">
				<span class="tab">
					<c:choose>
						<c:when test="${atomicService.atomicServiceId == currentAtomicServiceId}">
							${atomicService.name}
						</c:when>
						<c:otherwise>
							<portlet:renderURL var="showAtomicService">
								<portlet:param name="action" value="genericInvoker"/>
								<portlet:param name="currentAtomicService" value="${atomicService.atomicServiceId}"/>
							</portlet:renderURL>
							<a href="${showAtomicService}">${atomicService.name}</a>
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
					<spring:message code="cloud.manager.portlet.number.of.instances.label" arguments="1"/>)
				</span>
			</c:forEach>
		</div>
		<div>
			<c:forEach var="atomicServiceInstance" items="${atomicServiceInstances}" varStatus="status">
				<span class="instance-label">
					<spring:message code="cloud.manager.portlet.instance.sequence.label" arguments="${status.index + 1}"/>
					${atomicServiceInstance.name}
				</span>
			</c:forEach>
		</div>
	</c:when>
	<c:otherwise>
		<spring:message code="cloud.manager.portlet.no.atomic.service.instances"/>
	</c:otherwise>
</c:choose>

<a href="${startAtomicService}"><spring:message code="cloud.manager.portlet.start.atomic.service.instance.label"/></a>