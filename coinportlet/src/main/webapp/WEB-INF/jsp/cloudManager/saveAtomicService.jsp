<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:choose>
	<c:when test="${saveAtomicServiceRequest.atomicServiceId != null}">
		<portlet:actionURL var="formAction">
			<portlet:param name="action" value="editAs"/>
			<portlet:param name="workflowType" value="${workflowType}"/>
		</portlet:actionURL>
	</c:when>
	<c:otherwise>
		<portlet:actionURL var="formAction">
			<portlet:param name="action" value="saveAtomicService"/>
		</portlet:actionURL>
	</c:otherwise>
</c:choose>

<div>
	<form:form action='${formAction}' modelAttribute='saveAtomicServiceRequest'>
		<form:hidden path="atomicServiceInstanceId"/>
		<form:hidden path="atomicServiceId"/>
		<fieldset>
			<legend>Atomic service details</legend>
			
			<label for="name"><spring:message code="cloud.manager.portlet.save.atomic.service.name.label"/></label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.name.tooltip" var="tooltip"/>
			<form:input path="name" title="${tooltip}"/>
			<form:errors path="name" cssClass="text-error"/>
			
			<label for="description"><spring:message code="cloud.manager.portlet.save.atomic.service.description.label"/></label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.description.tooltip" var="tooltip"/>
			<form:textarea path="description" title="${tooltip}"/>
			<form:errors path="description" cssClass="coin-error-panel"/>
			
			<spring:message code='cloud.manager.portlet.save.atomic.service.shared.flag.tooltip' var="tooltip"/>
			<label class="checkbox">
				<form:checkbox path="shared" title="${tooltip}"/>
				<spring:message code="cloud.manager.portlet.save.atomic.service.shared.flag.label"/>
			</label>
			<form:errors path="shared" cssClass="coin-error-panel"/>
			
			<spring:message code="cloud.manager.portlet.save.atomic.service.scalable.flag.tooltip" var="tooltip"/>
			<label class="checkbox">
				<form:checkbox path="scalable" title="${tooltip}"/>
				<spring:message code="cloud.manager.portlet.save.atomic.service.scalable.flag.label"/>
			</label>
			<form:errors path="scalable" cssClass="coin-error-panel"/>
			
			<spring:message code="cloud.manager.portlet.save.atomic.service.published.flag.tooltip" var="tooltip"/>
			<label class="checkbox">
				<form:checkbox path="published" title="${tooltip}"/>
				<spring:message code="cloud.manager.portlet.save.atomic.service.published.flag.label"/>
			</label>
			<form:errors path="published" cssClass="coin-error-panel"/>
			
			<label for="proxyConfiguration"><spring:message code="cloud.manager.portlet.save.atomic.service.proxy.configuration.label"/></label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.proxy.configuration.tooltip" var="tooltip"/>
			<form:input path="proxyConfiguration" title="${tooltip}"/>
			<form:errors path="proxyConfiguration" cssClass="coin-error-panel"/>
			
			<br/>
			<button type='submit' class="btn"><spring:message code='cloud.manager.portlet.submit.atomic.service.save.request'/></button>
		</fieldset>
	</form:form>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery('input#name').tooltip();
			jQuery('input#description').tooltip();
			jQuery('input#shared').tooltip();
			jQuery('input#scalable').tooltip();
			jQuery('input#published').tooltip();
			jQuery('input#proxyConfiguration').tooltip();
		});
	</script>
	<div>
		<ul class="inline">
			<li>
				<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
			</li>
		</ul>
	</div>
</div>