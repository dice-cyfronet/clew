<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="startAtomicService">
	<portlet:param name="action" value="startAtomicService"/>
</portlet:actionURL>

<p><spring:message code='cloud.manager.portlet.available.atomic.services.list.header'/></p>

<form:form action='${startAtomicService}' modelAttribute='startAtomicServiceRequest'>
	<table>
		<tr>
			<th><spring:message code='cloud.manager.portlet.atomic.service.name.label'/></th>
			<th><spring:message code='cloud.manager.portlet.atomic.service.description.label'/></th>
		</tr>
		<c:forEach var='atomicService' items='${atomicServices}' varStatus='loopStatus'>
			<tr>
				<td>${atomicService.name}</td>
				<td>${atomicService.description}</td>
				<c:choose>
					<c:when test='${loopStatus.first}'>
						<td><form:radiobutton path='atomicServiceId'
								value='${atomicService.atomicServiceId}' checked='true'/></td>
					</c:when>
					<c:otherwise>
						<td><form:radiobutton path='atomicServiceId'
								value='${atomicService.atomicServiceId}'/></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</c:forEach>
	</table>
	<form:input path='atomicServiceName'/>
	<input type='submit' value='<spring:message code='cloud.manager.portlet.submit.atomic.service.start.request'/>'/>
</form:form>

<a href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>