<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="invokeAtomicService">
	<portlet:param name="action" value="invokeAtomicService"/>
</portlet:actionURL>

<div class="coin-content">
	<c:choose>
		<c:when test="${negativeMessage != null}">
			<p>${negativeMessage}</p>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${atomicServiceInvokable}">
					<a  class="coin-link" href='${atomicServiceWsdlEndpoint}'>WSDL</a><br/>
					<form:form action='${invokeAtomicService}' modelAttribute='invokeAtomicServiceRequest'>
						<form:hidden path="atomicServiceInstanceId"/>
						<form:hidden path="workflowId"/>
						<form:hidden path="configurationId"/>
						<form:hidden path="atomicServiceId"/>
						<div>
							<label for="method">
								<spring:message code="cloud.manager.portlet.submit.atomic.service.method.label"/>
							</label>
							<form:select path="method" items="${atomicServiceMethodList}"/>
						</div>
						<div>
							<label for="messageBody">
								<spring:message code="cloud.manager.portlet.submit.atomic.service.message.body.label"/>
							</label>
							<form:textarea path="messageBody" cols="40" rows="6"/>
						</div>
						<input type='submit' value='<spring:message code='cloud.manager.portlet.invoke.atomic.service.request'/>'/>
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
	
	<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
</div>