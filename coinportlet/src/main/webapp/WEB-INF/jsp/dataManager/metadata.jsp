<%@ include file="../include.jsp" %>

<portlet:renderURL var="goBack">
	<portlet:param name="path" value="${parentPath}"/>
</portlet:renderURL>
<portlet:renderURL var="updateMetadata">
	<portlet:param name="path" value="${path}"/>
	<portlet:param name="parentPath" value="${parentPath}"/>
</portlet:renderURL>

<div class="coin-content coin-content-no-tabs">
	<span class="coin-top-dir"><spring:message code="data.manager.portlet.metadata.header" arguments="${path}"/></span>
	<div>
		<form:form class="coin-form" action='${updateMetadata}' modelAttribute='metadata'>
			<div class="coin-form-input">
				<label for="lobcderWebDavMetadata.driSupervised">
					<spring:message code="data.manager.portlet.metadata.dri.supervised.label"/>
				</label>
				<form:input path="lobcderWebDavMetadata.driSupervised"/>
				<form:errors path="lobcderWebDavMetadata.driSupervised" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="lobcderWebDavMetadata.driChecksum">
					<spring:message code="data.manager.portlet.metadata.dri.checksum.label"/>
				</label>
				<form:input path="lobcderWebDavMetadata.driChecksum"/>
				<form:errors path="lobcderWebDavMetadata.driChecksum" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="lobcderWebDavMetadata.driLastValidationDateMs">
					<spring:message code="data.manager.portlet.metadata.dri.last.validation.data.ms.label"/>
				</label>
				<form:input path="lobcderWebDavMetadata.driLastValidationDateMs"/>
				<form:errors path="lobcderWebDavMetadata.driLastValidationDateMs" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-submit">
				<input type='submit' value='<spring:message code='data.manager.portlet.update.metadata.submit.label'/>'/>
			</div>
		</form:form>
	</div>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href="${goBack}">
					<spring:message code="data.manager.portlet.return.to.file.list.label"/>
				</a>
			</li>
		</ul>
	</div>
</div>