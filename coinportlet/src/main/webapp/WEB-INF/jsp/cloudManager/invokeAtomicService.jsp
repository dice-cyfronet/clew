<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="invokeAtomicService">
	<portlet:param name="action" value="invokeAtomicService"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<c:choose>
		<c:when test="${negativeMessage != null}">
			<div class="coin-error-panel">${negativeMessage}</div>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${atomicServiceInvokable}">
					<c:if test="${invocationPath != null}">
						<div>
							<span>Invocation URL template: <span style="font-family: monospace; font-size: larger;">${invocationPath}</span></span>
						</div>
					</c:if>
					<form:form class="coin-form" action='${invokeAtomicService}' modelAttribute='invokeAtomicServiceRequest'>
						<form:hidden path="atomicServiceInstanceId"/>
						<form:hidden path="workflowId"/>
						<form:hidden path="configurationId"/>
						<form:hidden path="atomicServiceId"/>
						<form:hidden path="invocationPath"/>
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
				</c:when>
				<c:otherwise>
					<spring:message code="cloud.manager.portlet.as.not.invokable" arguments="${currentAtomicService.name}"/>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
	<c:if test="${not empty atomicServiceInvocationResult}">
		<div style="margin-top: 20px; margin-bottom: 20px; margin-left: auto; margin-right: auto;">
			<spring:message code="cloud.manager.portlet.atomic.service.invocation.result.label"/>
			<br/>
			<pre style="font-size: small;">${atomicServiceInvocationResult}</pre>
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