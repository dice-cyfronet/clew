<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="addEndpoint">
	<portlet:param name="action" value="addEndpoint"/>
</portlet:actionURL>
<portlet:actionURL var="addRedirection">
	<portlet:param name="action" value="addRedirection"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<div>
		<div style="font-size: big; text-align: center;">Redirections</div>
		<c:choose>
			<c:when test="${fn:length(redirections) > 0}">
				<c:forEach var="redirection" items="${redirections}">
					<div class="coin-panel">
						<div style="width: 30%; float: left; text-align: right;">
							<span class="coin-header">${redirection.name} (${redirection.type})</span>
						</div>
						<div style="width: 70%; float: left;">
							<span class="coin-description">
								<span style="padding-left: 10px; display: block;">
									<spring:message code="cloud.manager.portlet.remove.redirection.description.template" arguments="${redirection.host},${redirection.fromPort}"/>
								</span>
							</span>
							<span class="coin-actions">
								<portlet:actionURL var="removeRedirection">
									<portlet:param name="action" value="removeRedirection"/>
									<portlet:param name="atomicServiceInstanceId" value="${addRedirectionRequest.atomicServiceInstanceId}"/>
									<portlet:param name="redirectionId" value="${redirection.id}"/>
									<portlet:param name="workflowId" value="${addRedirectionRequest.workflowId}"/>
								</portlet:actionURL>
								<c:set var="removeConfirmation">removeRedirection-${redirection.id}</c:set>
								<a id="${removeConfirmation}" class="coin-link" href='${removeRedirection}'><spring:message code='cloud.manager.portlet.remove.redirection.label'/></a>
								<script type="text/javascript">
									jQuery(document).ready(function() {
										jQuery('#${removeConfirmation}').click(function() {
											if(!confirm("<spring:message code='cloud.manager.portlet.remove.redirection.confirmation.label'/>")) {
												return false;
											}
										});
			    					});
								</script>
							</span>
						</div>
					</div>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<spring:message code="cloud.manager.portlet.no.redirections.label"/>
			</c:otherwise>
		</c:choose>
		<div style="margin-top: 20px;">
			<spring:message code="cloud.manager.portlet.define.redirection.form.label"/>
		</div>
		<form:form class="coin-form" action='${addRedirection}' modelAttribute='addRedirectionRequest'>
			<form:hidden path="atomicServiceInstanceId"/>
			<form:hidden path="workflowId"/>
			<div class="coin-form-input">
				<label for="name">
					<spring:message code="cloud.manager.portlet.add.redirection.name.label"/>
				</label>
				<form:input path="name"/>
				<form:errors path="name" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="toPort">
					<spring:message code="cloud.manager.portlet.add.redirection.to.port.label"/>
				</label>
				<form:input path="toPort"/>
				<form:errors path="toPort" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="type">
					<spring:message code="cloud.manager.portlet.add.redirection.type.label"/>
				</label>
				<form:select path="type" items="${redirectionTypes}"/>
				<form:errors path="type" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-submit">
				<input type='submit' value='<spring:message code='cloud.manager.portlet.add.redirection.submit.request'/>'/>
			</div>
		</form:form>
	</div>
	<div>
		<div style="font-size: big; text-align: center; margin-top: 20px;">Endpoints</div>
		<c:choose>
			<c:when test="${fn:length(endpoints) > 0}">
				<c:forEach var="endpoint" items="${endpoints}">
					<div class="coin-panel">
						<div style="width: 30%; float: left; text-align: right;">
							<span class="coin-header"><a class="coin-link" target="_blank" href="${endpointLinks[endpoint.id]}">${endpoint.type} (${endpoint.invocationPath})</a></span>
						</div>
						<div style="width: 70%; float: left;">
							<span class="coin-description">
								<span style="padding-left: 10px; display: block;">${endpoint.description}</span>
								<span style="padding-left: 10px; display: block; margin-top: 5px;">URL: ${breakingEndpointLinks[endpoint.id]}</span>
							</span>
							<span class="coin-actions">
								<portlet:actionURL var="removeEndpoint">
									<portlet:param name="action" value="removeEndpoint"/>
									<portlet:param name="atomicServiceInstanceId" value="${addEndpointRequest.atomicServiceInstanceId}"/>
									<portlet:param name="endpointId" value="${endpoint.id}"/>
									<portlet:param name="workflowId" value="${addEndpointRequest.workflowId}"/>
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
					</div>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<spring:message code="cloud.manager.portlet.no.endpoints.label"/>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${fn:length(redirectionSelection) > 0}">
				<div style="margin-top: 20px;">
					<spring:message code="cloud.manager.portlet.define.endpoint.form.label"/>
				</div>
				<form:form class="coin-form" action='${addEndpoint}' modelAttribute='addEndpointRequest'>
					<form:hidden path="atomicServiceInstanceId"/>
					<form:hidden path="workflowId"/>
					<div class="coin-form-input">
						<label for="type">
							<spring:message code="cloud.manager.portlet.add.endpoint.type.label"/>
						</label>
						<form:select path="type" items="${endpointTypes}"/>
						<form:errors path="type" cssClass="coin-error-panel"/>
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
						<form:select items="${redirectionSelection}" path="port"/>
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
			</c:when>
			<c:otherwise>
				<div>
					<spring:message code="cloud.manager.portlet.endpoint.creation.disabled.no.redirections"/>
				</div>
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