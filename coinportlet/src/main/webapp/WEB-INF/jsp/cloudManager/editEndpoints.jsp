<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="addEndpoint">
	<portlet:param name="action" value="addEndpoint"/>
</portlet:actionURL>
<portlet:actionURL var="addRedirection">
	<portlet:param name="action" value="addRedirection"/>
</portlet:actionURL>

<h2><spring:message code='cloud.manager.portlet.external.interfaces.label'/></h2>
<c:choose>
	<c:when test="${fn:length(httpRedirections) > 0 or fn:length(natRedirections) > 0}">
		<c:forEach var="redirection" items="${httpRedirections}">
			<div class="row-fluid row-hover">
				<div class="span2 text-right" style="font-size: larger; word-wrap: break-word;"">
					<strong>${redirection.name} (<spring:message code='cloud.manager.portlet.redirection.HTTP_AND_HTTPS.label'/>)</strong>
				</div>
				<div class="span8">
					<c:forEach var="url" items="${redirection.urls}" varStatus="status">
						${url}
						<spring:message code="cloud.manager.portlet.http.redirection.description.template" arguments="${url},${redirection.toPort}"/>
						<c:if test="${ not status.last}">,</c:if>
					</c:forEach>
				</div>
				<div class="span2">
					<portlet:actionURL var="removeRedirection">
						<portlet:param name="action" value="removeRedirection"/>
						<portlet:param name="atomicServiceInstanceId" value="${addRedirectionRequest.atomicServiceInstanceId}"/>
						<portlet:param name="redirectionId" value="${redirection.id}"/>
						<portlet:param name="workflowId" value="${addRedirectionRequest.workflowId}"/>
					</portlet:actionURL>
					<c:set var="removeConfirmation">removeRedirection-${redirection.id}</c:set>
					<a id="${removeConfirmation}" href='${removeRedirection}'><spring:message code='cloud.manager.portlet.remove.redirection.label'/></a>
					<script type="text/javascript">
						jQuery(document).ready(function() {
							jQuery('#${removeConfirmation}').click(function() {
								if(!confirm("<spring:message code='cloud.manager.portlet.remove.redirection.confirmation.label'/>")) {
									return false;
								}
							});
    					});
					</script>
				</div>
			</div>
		</c:forEach>
		<c:forEach var="redirection" items="${natRedirections}">
			<div class="row-fluid row-hover">
				<div class="span2 text-right" style="font-size: larger; word-wrap: break-word;">
					<strong>${redirection.name} (${redirection.type})</strong>
				</div>
				<div class="span8">
					<spring:message code="cloud.manager.portlet.redirection.description.template" arguments="${redirection.host},${redirection.fromPort},${redirection.toPort}"/>
				</div>
				<div class="span2">
					<portlet:actionURL var="removeRedirection">
						<portlet:param name="action" value="removeRedirection"/>
						<portlet:param name="atomicServiceInstanceId" value="${addRedirectionRequest.atomicServiceInstanceId}"/>
						<portlet:param name="redirectionId" value="${redirection.id}"/>
						<portlet:param name="workflowId" value="${addRedirectionRequest.workflowId}"/>
					</portlet:actionURL>
					<c:set var="removeConfirmation">removeRedirection-${redirection.id}</c:set>
					<a id="${removeConfirmation}" href='${removeRedirection}'><spring:message code='cloud.manager.portlet.remove.redirection.label'/></a>
					<script type="text/javascript">
						jQuery(document).ready(function() {
							jQuery('#${removeConfirmation}').click(function() {
								if(!confirm("<spring:message code='cloud.manager.portlet.remove.redirection.confirmation.label'/>")) {
									return false;
								}
							});
    					});
					</script>
				</div>
			</div>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<p><spring:message code="cloud.manager.portlet.no.redirections.label"/></p>
	</c:otherwise>
</c:choose>
<form:form action='${addRedirection}' modelAttribute='addRedirectionRequest'>
	<form:hidden path="atomicServiceInstanceId"/>
	<form:hidden path="workflowId"/>
	<fieldset>
		<legend><spring:message code="cloud.manager.portlet.define.redirection.form.label"/></legend>
		<label for="name"><spring:message code="cloud.manager.portlet.add.redirection.name.label"/></label>
		<form:input path="name"/>
		<form:errors path="name" cssClass="text-error"/>
		
		<label for="toPort"><spring:message code="cloud.manager.portlet.add.redirection.to.port.label"/></label>
		<form:input path="toPort"/>
		<form:errors path="toPort" cssClass="text-error"/>
			
		<label for="type"><spring:message code="cloud.manager.portlet.add.redirection.type.label"/></label>
		<form:select path="type" items="${redirectionTypes}"/>
		<form:errors path="type" cssClass="coin-error-panel"/>
		
		<br/>
		<button type='submit' class="btn"><spring:message code='cloud.manager.portlet.add.redirection.submit.request'/></button>
	</fieldset>
</form:form>

<h2>Endpoints</h2>
<c:choose>
	<c:when test="${fn:length(endpoints) > 0}">
		<c:forEach var="endpoint" items="${endpoints}">
			<div class="row-fluid row-hover">
				<div class="span2 text-right" style="font-size: larger; word-wrap: break-word;"">
					<a target="_blank" href="${endpointLinks[endpoint.id]}">${endpoint.type} (${endpoint.invocationPath})</a>
				</div>
				<div class="span8">
					<span style="padding-left: 10px; display: block;">${endpoint.description}</span>
					<span style="padding-left: 10px; display: block; margin-top: 5px;">URL: ${breakingEndpointLinks[endpoint.id]}</span>
				</div>
				<div class="span2">
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
				</div>
			</div>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<p><spring:message code="cloud.manager.portlet.no.endpoints.label"/></p>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${fn:length(redirectionSelection) > 0}">
		<form:form action='${addEndpoint}' modelAttribute='addEndpointRequest'>
			<form:hidden path="atomicServiceInstanceId"/>
				<form:hidden path="workflowId"/>
			<fieldset>
				<legend><spring:message code="cloud.manager.portlet.define.endpoint.form.label"/></legend>
				
				<label for="type"><spring:message code="cloud.manager.portlet.add.endpoint.type.label"/></label>
				<form:select path="type" items="${endpointTypes}"/>
				<form:errors path="type" cssClass="text-error"/>
				
				<label for="invocationPath"><spring:message code="cloud.manager.portlet.add.endpoint.invocation.path.name.label"/></label>
				<form:input path="invocationPath"/>
				<form:errors path="invocationPath" cssClass="text-error"/>
				
				<label for="port"><spring:message code="cloud.manager.portlet.add.endpoint.port.name.label"/></label>
				<form:select items="${redirectionSelection}" path="port"/>
				<form:errors path="port" cssClass="text-error"/>
				
				<label for="description"><spring:message code="cloud.manager.portlet.add.endpoint.description.label"/></label>
				<form:textarea path="description"/>
				<form:errors path="description" cssClass="text-error"/>
				
				<label for="descriptor"><spring:message code="cloud.manager.portlet.add.endpoint.descriptor.label"/></label>
				<form:textarea path="descriptor"/>
				<form:errors path="descriptor" cssClass="text-error"/>
				
				<br/>
				<button type='submit' class="btn"><spring:message code='cloud.manager.portlet.add.endpoint.submit.request'/></button>
			</fieldset>
		</form:form>
	</c:when>
	<c:otherwise>
		<p><spring:message code="cloud.manager.portlet.endpoint.creation.disabled.no.redirections"/></p>
	</c:otherwise>
</c:choose>

<div>
	<ul class="inline">
		<li>
			<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
		</li>
	</ul>
</div>