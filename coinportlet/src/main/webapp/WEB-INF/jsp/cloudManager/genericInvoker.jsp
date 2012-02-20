<%@ include file="../include.jsp" %>

<%@ include file="menu.jsp" %>

<portlet:renderURL var="startAtomicService">
    <portlet:param name="action" value="startAtomicService"/>
</portlet:renderURL>

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

<a href="${startAtomicService}"><spring:message code="cloud.manager.portlet.start.atomic.service.instance.label"/></a>