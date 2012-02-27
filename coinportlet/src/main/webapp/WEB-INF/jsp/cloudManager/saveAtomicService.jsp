<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="saveAtomicService">
	<portlet:param name="action" value="saveAtomicService"/>
</portlet:actionURL>

<div class="coin-content">
	<form:form action='${saveAtomicService}' modelAttribute='saveAtomicServiceRequest'>
		<form:hidden path="atomicServiceInstanceId"/>
		<div>
			<label for="name">
				<spring:message code="cloud.manager.portlet.save.atomic.service.name.label"/>
			</label>
			<form:input path="name"/>
		</div>
		<div>
			<label for="description">
				<spring:message code="cloud.manager.portlet.save.atomic.service.description.label"/>
			</label>
			<form:textarea path="description"/>
		</div>
		<div>
			<label for="descriptionEndpoint">
				<spring:message code="cloud.manager.portlet.save.atomic.service.description.endpoint.label"/>
			</label>
			<form:input path="descriptionEndpoint"/>
		</div>
		<div>
			<label for="invocationEndpoint">
				<spring:message code="cloud.manager.portlet.save.atomic.service.invocation.endpoint.label"/>
			</label>
			<form:input path="invocationEndpoint"/>
		</div>
		<div>
			<label for="ports">
				<spring:message code="cloud.manager.portlet.save.atomic.service.ports.label"/>
			</label>
			<form:input path="ports"/>
		</div>
		<input type='submit' value='<spring:message code='cloud.manager.portlet.submit.atomic.service.save.request'/>'/>
	</form:form>
	
	<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
</div>