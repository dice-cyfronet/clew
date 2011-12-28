<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:renderURL var="startAtomicService">
    <portlet:param name="action" value="startAtomicService"/>
</portlet:renderURL>

<p><spring:message code="cloud.manager.portlet.atomic.service.list.header"/></p>
<ul>
	<c:forEach var="atomicServiceInstance" items="${atomicServiceInstances}">
		<li>${atomicServiceInstance.name}</li>
	</c:forEach>
</ul>

<a href="${startAtomicService}"><spring:message code="cloud.manager.portlet.start.atomic.service.label"/></a>