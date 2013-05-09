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
			<form:input path="name" title="${tooltip}" cssClass="input-xxlarge"/>
			<form:errors path="name" cssClass="text-error"/>
			
			<label for="description"><spring:message code="cloud.manager.portlet.save.atomic.service.description.label"/></label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.description.tooltip" var="tooltip"/>
			<form:textarea path="description" title="${tooltip}" cssClass="input-xxlarge"/>
			<form:errors path="description" cssClass="text-error"/>
			
			<spring:message code='cloud.manager.portlet.save.atomic.service.shared.flag.tooltip' var="tooltip"/>
			<label class="checkbox">
				<form:checkbox path="shared" title="${tooltip}"/>
				<spring:message code="cloud.manager.portlet.save.atomic.service.shared.flag.label"/>
			</label>
			<form:errors path="shared" cssClass="text-error"/>
			
			<spring:message code="cloud.manager.portlet.save.atomic.service.scalable.flag.tooltip" var="tooltip"/>
			<label class="checkbox">
				<form:checkbox path="scalable" title="${tooltip}"/>
				<spring:message code="cloud.manager.portlet.save.atomic.service.scalable.flag.label"/>
			</label>
			<form:errors path="scalable" cssClass="text-error"/>
			
			<spring:message code="cloud.manager.portlet.save.atomic.service.published.flag.tooltip" var="tooltip"/>
			<label class="checkbox">
				<form:checkbox path="published" title="${tooltip}"/>
				<spring:message code="cloud.manager.portlet.save.atomic.service.published.flag.label"/>
			</label>
			<form:errors path="published" cssClass="text-error"/>
			
			<label for="proxyConfiguration"><spring:message code="cloud.manager.portlet.save.atomic.service.proxy.configuration.label"/></label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.proxy.configuration.tooltip" var="tooltip"/>
			<form:input path="proxyConfiguration" title="${tooltip}"/>
			<form:errors path="proxyConfiguration" cssClass="text-error"/>
			
			<label for"cores"><spring:message code="cloud.manager.portlet.save.atomic.service.cores.label"/></label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.cores.tooltip" var="tooltip"/>
			<form:select path="cores" title="${tooltip}" items="${coreItems}">
				<form:option value="hello"></form:option>
			</form:select>
			<form:errors path="cores" cssClass="text-error"/>
			
			<label for"memory"><spring:message code="cloud.manager.portlet.save.atomic.service.memory.label"/></label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.memory.tooltip" var="tooltip"/>
			<form:select path="memory" title="${tooltip}" items="${memoryItems}"/>
			<form:errors path="memory" cssClass="text-error"/>
			
			<label for"disk"><spring:message code="cloud.manager.portlet.save.atomic.service.disk.label"/></label>
			<spring:message code="cloud.manager.portlet.save.atomic.service.disk.tooltip" var="tooltip"/>
			<form:select path="disk" title="${tooltip}" items="${diskItems}"/>
			<form:errors path="disk" cssClass="text-error"/>
			
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
			jQuery('select#cores').tooltip();
			jQuery('select#memory').tooltip();
			jQuery('select#disk').tooltip();
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