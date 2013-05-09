<%@ include file="../include.jsp" %>

<div>
	<div>
		<portlet:renderURL var="manageUserKeys">
			<portlet:param name="action" value="userKeys"/>
			<portlet:param name="workflowType" value="development"/>
		</portlet:renderURL>
		<c:set var="keyManagerLink"><a href="${manageUserKeys}">link</a></c:set>
		<p>
			<spring:message code="cloud.manager.portlet.pick.user.key.info.message" arguments="${atomicServiceName},${keyManagerLink}"/>
		</p>
		<c:choose>
			<c:when test="${fn:length(userKeyList) > 0}">
				<portlet:actionURL var="startDevAtomicService">
					<portlet:param name="action" value="startAtomicService"/>
				</portlet:actionURL>
				<form:form action='${startDevAtomicService}' modelAttribute='startAtomicServiceRequest'>
					<form:hidden path="atomicServiceId"/>
					<form:hidden path="workflowType"/>
					<fieldset>
						<legend><spring:message code="cloud.manager.portlet.pick.user.key.label"/></legend>
						
						<label for="asiName"><spring:message code="cloud.manager.portlet.asi.optional.name.label"/></label>
						<form:input path="atomicServiceInstanceName"/>
						
						<c:forEach var="entry" items="${userKeyList}">
							<label class="radio">
								<form:radiobutton path="userKeyId" value="${entry.key}"/>
								${entry.value}
							</label>
						</c:forEach>
						<button class="btn" type='submit'><spring:message code='cloud.manager.portlet.start.atomic.service.submit.label'/></button>
					</fieldset>
				</form:form>
			</c:when>
			<c:otherwise>
				<p><spring:message code="cloud.manager.portlet.no.user.keys.label"/></p>
			</c:otherwise>
		</c:choose>
	</div>
	<div>
		<ul class="inline">
			<li>
				<a href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
			</li>
		</ul>
	</div>
</div>