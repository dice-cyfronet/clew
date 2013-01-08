<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="saveAtomicService">
	<portlet:param name="action" value="saveAtomicService"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<form:form class="coin-form" action='${saveAtomicService}' modelAttribute='saveAtomicServiceRequest'>
		<form:hidden path="atomicServiceInstanceId"/>
		<div class="coin-form-input">
			<label for="name">
				<spring:message code="cloud.manager.portlet.save.atomic.service.name.label"/>
			</label>
			<form:input path="name"/>
			<form:errors path="name" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="description">
				<spring:message code="cloud.manager.portlet.save.atomic.service.description.label"/>
			</label>
			<form:textarea path="description"/>
			<form:errors path="description" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-submit">
			<input type='submit' value='<spring:message code='cloud.manager.portlet.submit.atomic.service.save.request'/>'/>
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