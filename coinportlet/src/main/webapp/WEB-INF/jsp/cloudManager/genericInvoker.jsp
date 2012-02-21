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
				<span>${atomicService.name}<br/>
					<c:if test="${atomicService.http or atomicService.vnc}">
					(<spring:message code=""/>
					</c:if>
				</span>
			</c:forEach>
		</div>
	
	<!-- 
		<ul>
			<c:forEach var="atomicServiceInstance" items="${atomicServiceInstances}">
				<portlet:renderURL var="saveAtomicService">
					<portlet:param name="action" value="saveAtomicService"/>
					<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.instanceId}"/>
				</portlet:renderURL>
				<portlet:renderURL var="invokeAtomicService">
					<portlet:param name="action" value="invokeAtomicService"/>
					<portlet:param name="atomicServiceInstanceId" value="${atomicServiceInstance.instanceId}"/>
				</portlet:renderURL>
				<li>
					<span>${atomicServiceInstance.name} (template ID: ${atomicServiceInstance.atomicServiceId},
							status: ${atomicServiceInstance.status})</span>
					<br/>
					Actions: <a href="${saveAtomicService}">Save as AS...</a>
					<c:if test="${atomicServiceInstance.atomicService}">
						, <a href="TODO">VNC</a>, <a href="${invokeAtomicService}">Invoker</a>
					</c:if>
				</li>
			</c:forEach>
		</ul>
		-->
	</c:when>
	<c:otherwise>
		<spring:message code="cloud.manager.portlet.no.atomic.service.instances"/>
	</c:otherwise>
</c:choose>

<a href="${startAtomicService}"><spring:message code="cloud.manager.portlet.start.atomic.service.instance.label"/></a>