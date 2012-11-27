<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="invokeAtomicService">
	<portlet:param name="action" value="invokeAtomicService"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<c:if test="${negativeMessage != null}">
		<div class="coin-error-panel">${negativeMessage}</div>
	</c:if>
	<c:if test="${fn:length(webappEndpoints) > 0}">
		<span>Web Application endpoints:</span><br/><br/>
		<c:forEach var="endpoint" items="${webappEndpoints}">
			<a class="coin-link" target="_blank" href="${invocationBase}${endpoint.invocationPath}">${invocationBase}${endpoint.invocationPath}</a><br/>
		</c:forEach>
	</c:if>
	<c:if test="${atomicServiceInvokable}">
		<c:if test="${invocationPath != null}">
			<div>
				<span>Invocation URL template:</span><br/>
				<a class="coin-link" target="_blank" href="${invocationPath}" style="padding-left: 20px;">Invocation template</a>
			</div>
		</c:if>
		<form:form class="coin-form" action='${invokeAtomicService}' modelAttribute='invokeAtomicServiceRequest'>
			<form:hidden path="atomicServiceInstanceId"/>
			<form:hidden path="workflowId"/>
			<form:hidden path="configurationId"/>
			<form:hidden path="atomicServiceId"/>
			<form:hidden path="invocationPath"/>
			<form:hidden path="serviceId"/>
			<div class="coin-form-input">
				<label for="method">
					<spring:message code="cloud.manager.portlet.submit.atomic.service.method.label"/>
				</label>
				<form:select path="method" items="${atomicServiceMethodList}"/>
			</div>
			<c:forEach var="formField" items="${invokeAtomicServiceRequest.formFields}" varStatus="index">
				<div class="coin-form-input">
					<label for="formFields[${index.index}].value">
						Request parameter <i>${formField.name}</i>
					</label>
					<form:input path="formFields[${index.index}].value"/><br/>
					<form:hidden path="formFields[${index.index}].name"/>
				</div>
			</c:forEach>
			<div class="coin-form-submit">
				<input type='submit' value='<spring:message code='cloud.manager.portlet.invoke.atomic.service.request'/>'/>
			</div>
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
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
			</li>
		</ul>
	</div>
</div>