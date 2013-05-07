<%@ include file="../include.jsp" %>

<div>
	<div>
		<portlet:renderURL var="manageUserKeys">
			<portlet:param name="action" value="userKeys"/>
			<portlet:param name="workflowType" value="development"/>
		</portlet:renderURL>
		<c:set var="keyManagerLink"><a class="coin-link" href="${manageUserKeys}">link</a></c:set>
		<spring:message code="cloud.manager.portlet.pick.user.key.info.message"
				arguments="${atomicServiceName},${keyManagerLink}"/>
		
		<c:choose>
			<c:when test="${fn:length(userKeyList) > 0}">
				<portlet:actionURL var="startDevAtomicService">
					<portlet:param name="action" value="startAtomicService"/>
				</portlet:actionURL>
				<form:form class="coin-form" action='${startDevAtomicService}' modelAttribute='startAtomicServiceRequest'>
					<form:hidden path="atomicServiceId"/>
					<form:hidden path="workflowType"/>
					<div>
						<label for="userKeyId" style="display: inline; font-weight: bold;">
							<spring:message code="cloud.manager.portlet.pick.user.key.label"/>
						</label>
						<form:radiobuttons path="userKeyId" items="${userKeyList}" element="div" cssStyle="display: inline; margin-left: 10px; line-height: 15px; float: left;" />
						<form:errors path="userKeyId" cssClass="coin-error-panel"/>
					</div>
					<div class="coin-form-submit">
						<input type='submit' value='<spring:message code='cloud.manager.portlet.start.atomic.service.submit.label'/>'/>
					</div>
				</form:form>
			</c:when>
			<c:otherwise>
				<spring:message code="cloud.manager.portlet.no.user.keys.label"/>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
			</li>
		</ul>
	</div>
</div>