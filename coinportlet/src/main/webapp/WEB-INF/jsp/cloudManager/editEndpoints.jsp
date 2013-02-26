<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="addEndpoint">
	<portlet:param name="action" value="addEndpoint"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<c:choose>
		<c:when test="${fn:length(endpoints) > 0}">
			<c:forEach var="endpoint" items="${endpoints}">
				<div class="coin-panel">
					<span class="coin-header">${endpoint.type} (${endpoint.invocationPath})</span>
					<span class="coin-description">
						${endpoint.description}
					</span>
					<span class="coin-actions">
						<portlet:actionURL var="removeEndpoint">
							<portlet:param name="action" value="removeEndpoint"/>
							<portlet:param name="atomicServiceInstanceId" value="${addEndpointRequest.atomicServiceInstanceId}"/>
							<portlet:param name="endpointId" value="${endpoint.id}"/>
							<portlet:param name="workflowId" value="${workflowId}"/>
						</portlet:actionURL>
						<c:set var="removeConfirmation">removeEndpoint-${endpoint.id}</c:set>
						<a id="${removeConfirmation}" class="coin-link" href='${removeEndpoint}'><spring:message code='cloud.manager.portlet.remove.endpoint.label'/></a>
						<script type="text/javascript">
							jQuery(document).ready(function() {
								jQuery('#${removeConfirmation}').click(function() {
									if(!confirm("<spring:message code='cloud.manager.portlet.remove.endpoint.confirmation.label'/>")) {
										return false;
									}
								});
	    					});
						</script>
					</span>
				</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<spring:message code="cloud.manager.portlet.no.endpoints.label"/>
		</c:otherwise>
	</c:choose>
	<div style="margin-top: 20px;">
		<spring:message code="cloud.manager.portlet.define.endpoint.form.label"/>
	</div>
	<form:form class="coin-form" action='${addEndpoint}' modelAttribute='addEndpointRequest'>
		<form:hidden path="atomicServiceInstanceId"/>
		<form:hidden path="workflowId"/>
		<div class="coin-form-input">
			<label for="type">
				<spring:message code="cloud.manager.portlet.add.endpoint.service.name.label"/>
			</label>
			<form:select path="type" items="${endpointTypes}"/>
			<form:errors path="type" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="serviceName">
				<spring:message code="cloud.manager.portlet.add.endpoint.service.name.label"/>
			</label>
			<form:input path="serviceName"/>
			<form:errors path="serviceName" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="invocationPath">
				<spring:message code="cloud.manager.portlet.add.endpoint.invocation.path.name.label"/>
			</label>
			<form:input path="invocationPath"/>
			<form:errors path="invocationPath" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="port">
				<spring:message code="cloud.manager.portlet.add.endpoint.port.name.label"/>
			</label>
			<form:input path="port"/>
			<form:errors path="port" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="description">
				<spring:message code="cloud.manager.portlet.add.endpoint.description.label"/>
			</label>
			<form:textarea path="description"/>
			<form:errors path="description" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="descriptor">
				<spring:message code="cloud.manager.portlet.add.endpoint.descriptor.label"/>
			</label>
			<form:textarea path="descriptor"/>
			<form:errors path="descriptor" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-submit">
			<input type='submit' value='<spring:message code='cloud.manager.portlet.add.endpoint.submit.request'/>'/>
		</div>
	</form:form>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
			</li>
		</ul>
	</div>
</div>