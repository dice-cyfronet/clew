<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="invokeAtomicService">
	<portlet:param name="action" value="invokeAtomicService"/>
</portlet:actionURL>

<c:if test="${negativeMessage != null}">
	<div class="text-error">${negativeMessage}</div>
</c:if>
<c:if test="${asiRedirections != null and fn:length(asiRedirections) > 0}">
	<span style="font-style: italic; margin-top: 10px; display: block;"><spring:message code="cloud.manager.portlet.invocation.redirections.label"/></span>
	<dl>
		<c:forEach var="red" items="${asiRedirections}">
			<dt><spring:message code="cloud.manager.portlet.invocation.redirection.host" arguments="${red.name}"/></dt>
			<dd>${red.host}</dd>
			<dt><spring:message code="cloud.manager.portlet.invocation.redirection.port" arguments="${red.name}"/></dt>
			<dd>${red.fromPort}</dd>
		</c:forEach>
	</dl>
</c:if>
<c:if test="${fn:length(webappEndpoints) > 0}">
	<span>Web Application endpoints:</span><br/><br/>
	<c:forEach var="endpoint" items="${webappEndpoints}">
		<a target="_blank" href="${authEndpointLinks[endpoint.id]}${additionalQuery}">${endpointLinks[endpoint.id]}${additionalQuery}</a><br/>
	</c:forEach>
</c:if>
<c:if test="${fn:length(wsEndpoints) > 0}">
	<span style="font-style: italic; margin-top: 10px; display: block;"><spring:message code="cloud.manager.portlet.ws.endpoint.list.label"/></span>
	<c:forEach var="ws" items="${wsEndpoints}">
		<span style="margin-left: 20px;">URL: <span style="font-family: monospace; font-size: smaller;">${endpointLinks[ws.id]}</span></span><br/>
		<button type="button" class="btn btn-link" data-toggle="collapse" data-target="#${ws.id}">Show/Hide descriptor</button>
		<div id="${ws.id}" class="collapse">
			<c:if test="${ws.descriptor == null}">
				No descriptor available
			</c:if>
			<pre>
${fn:escapeXml(ws.descriptor)}
			</pre>
		</div>
	</c:forEach>
</c:if>
<c:if test="${atomicServiceInvokable}">
	<c:if test="${invocationPath != null}">
		<div>
			<span>Invocation URL template:</span><br/>
			<a target="_blank" href="${invocationPath}" style="padding-left: 20px;">Invocation template</a>
		</div>
	</c:if>
	<form:form action='${invokeAtomicService}' modelAttribute='invokeAtomicServiceRequest'>
		<form:hidden path="atomicServiceInstanceId"/>
		<form:hidden path="workflowId"/>
		<form:hidden path="configurationId"/>
		<form:hidden path="atomicServiceId"/>
		<form:hidden path="invocationPath"/>
		<form:hidden path="postfix"/>
		<form:hidden path="port"/>
		<form:hidden path="host"/>
		<fieldset>
			<legend>Invocation details</legend>
			
			<label for="method"><spring:message code="cloud.manager.portlet.submit.atomic.service.method.label"/></label>
			<form:select path="method" items="${atomicServiceMethodList}"/>
			
			<c:forEach var="formField" items="${invokeAtomicServiceRequest.formFields}" varStatus="index">
				<label for="formFields[${index.index}].value">Request parameter <em>${formField.name}</em></label>
				<form:input path="formFields[${index.index}].value"/><br/>
				<form:hidden path="formFields[${index.index}].name"/>
			</c:forEach>
		
			<br/>
			<button class='btn' type='submit'><spring:message code='cloud.manager.portlet.invoke.atomic.service.request'/></button>
		</fieldset>
	</form:form>
</c:if>
<c:if test="${not empty atomicServiceInvocationResult}">
	<c:choose>
		<c:when test="${atomicServiceInvocationCode != '200 - OK'}">
			<c:set var="style">margin-top: 20px; margin-bottom: 20px; margin-left: auto; margin-right: auto; padding: 10px; background-color: #efc09f;</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="style">margin-top: 20px; margin-bottom: 20px; margin-left: auto; margin-right: auto; padding: 10px;</c:set>
		</c:otherwise>
	</c:choose>
	<div style="${style}">
		<span>Response code: </span><br/><span style="font-weight: bold; padding: 10px; display: block;">${atomicServiceInvocationCode}</span>
		<br/>
		<span>Response body: </span><br/><span style="font-weight: bold; padding: 10px; display: block;">${atomicServiceInvocationResult}</span>
	</div>
</c:if>
<div>
	<ul class="inline">
		<li>
			<a href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
		</li>
	</ul>
</div>