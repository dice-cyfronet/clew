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

<div class="coin-content coin-content-no-tabs">
	<form:form class="coin-form" action='${formAction}' modelAttribute='saveAtomicServiceRequest'>
		<form:hidden path="atomicServiceInstanceId"/>
		<form:hidden path="atomicServiceId"/>
		<div class="coin-form-input">
			<label for="name">
				<spring:message code="cloud.manager.portlet.save.atomic.service.name.label"/>
			</label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.name.tooltip" var="tooltip"/>
			<form:input path="name" title="${tooltip}"/>
			<form:errors path="name" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="description">
				<spring:message code="cloud.manager.portlet.save.atomic.service.description.label"/>
			</label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.description.tooltip" var="tooltip"/>
			<form:textarea path="description" title="${tooltip}"/>
			<form:errors path="description" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="shared">
				<spring:message code="cloud.manager.portlet.save.atomic.service.shared.flag.label"/>
			</label>
			<spring:message code='cloud.manager.portlet.save.atomic.service.shared.flag.tooltip' var="tooltip"/>
			<form:checkbox path="shared" title="${tooltip}"/>
			<form:errors path="shared" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="scalable">
				<spring:message code="cloud.manager.portlet.save.atomic.service.scalable.flag.label"/>
			</label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.scalable.flag.tooltip" var="tooltip"/>
			<form:checkbox path="scalable" title="${tooltip}"/>
			<form:errors path="scalable" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="published">
				<spring:message code="cloud.manager.portlet.save.atomic.service.published.flag.label"/>
			</label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.published.flag.tooltip" var="tooltip"/>
			<form:checkbox path="published" title="${tooltip}"/>
			<form:errors path="published" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="proxyConfiguration">
				<spring:message code="cloud.manager.portlet.save.atomic.service.proxy.configuration.label"/>
			</label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.proxy.configuration.tooltip" var="tooltip"/>
			<form:input path="proxyConfiguration" title="${tooltip}"/>
			<form:errors path="proxyConfiguration" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-submit">
			<input type='submit' value='<spring:message code='cloud.manager.portlet.submit.atomic.service.save.request'/>'/>
		</div>
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
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
			</li>
		</ul>
	</div>
</div>