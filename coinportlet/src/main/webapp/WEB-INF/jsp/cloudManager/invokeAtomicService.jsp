<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="invokeAtomicService">
	<portlet:param name="action" value="invokeAtomicService"/>
</portlet:actionURL>

<form:form action='${invokeAtomicService}' modelAttribute='invokeAtomicServiceRequest'>
	<form:hidden path="atomicServiceInstanceId"/>
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
		<form:textarea path="messageBody"/>
	</div>
	<input type='submit' value='<spring:message code='cloud.manager.portlet.invoke.atomic.service.request'/>'/>
</form:form>

<c:if test="${not empty atomicServiceInvocationResult}">
	<div>
		<spring:message code="cloud.manager.portlet.atomic.service.invocation.result.label"/>
		<br/>
		<span>${atomicServiceInvocationResult}</span>
	</div>
</c:if>

<a href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>