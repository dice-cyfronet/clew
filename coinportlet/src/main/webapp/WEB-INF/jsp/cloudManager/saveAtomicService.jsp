<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="saveAtomicService">
	<portlet:param name="action" value="saveAtomicService"/>
</portlet:actionURL>

<div class="coin-content">
	<form:form class="coin-form" action='${saveAtomicService}' modelAttribute='saveAtomicServiceRequest'>
		<form:hidden path="atomicServiceInstanceId"/>
		<div>
			<label for="name">
				<spring:message code="cloud.manager.portlet.save.atomic.service.name.label"/>
			</label>
			<form:input path="name"/>
			<form:errors path="name" cssClass="portlet-msg-error"/>
		</div>
		<div>
			<label for="description">
				<spring:message code="cloud.manager.portlet.save.atomic.service.description.label"/>
			</label>
			<form:textarea path="description"/>
			<form:errors path="description" cssClass="portlet-msg-error"/>
		</div>
		<!-- 
		<div>
			<label for="descriptionEndpoint">
				<spring:message code="cloud.manager.portlet.save.atomic.service.description.endpoint.label"/>
			</label>
			<form:input path="descriptionEndpoint"/>
		</div>
		-->
		<div>
			<label for="invocationName">
				<spring:message code="cloud.manager.portlet.save.atomic.service.invocation.name.label"/>
			</label>
			<form:input path="invocationName"/>
			<form:errors path="invocationName" cssClass="portlet-msg-error"/>
		</div>
		<div>
			<label for="invocationEndpoint">
				<spring:message code="cloud.manager.portlet.save.atomic.service.invocation.endpoint.label"/>
			</label>
			<form:input path="invocationEndpoint"/>
			<form:errors path="invocationEndpoint" cssClass="portlet-msg-error"/>
		</div>
		<form:hidden path="invocationPort"/>
		<!--
		<div>
			<label for="invocationPort">
				<spring:message code="cloud.manager.portlet.save.atomic.service.invocation.port.label"/>
			</label>
			<form:input path="invocationPort"/>
			<form:errors path="invocationPort" cssClass="portlet-msg-error"/>
		</div>
		-->
		<!-- 
		<div>
			<label for="ports">
				<spring:message code="cloud.manager.portlet.save.atomic.service.ports.label"/>
			</label>
			<form:input path="ports"/>
		</div>
		-->
		<input type='submit' value='<spring:message code='cloud.manager.portlet.submit.atomic.service.save.request'/>'/>
	</form:form>
	
	<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
</div>