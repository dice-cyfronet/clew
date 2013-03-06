<%@ include file="../include.jsp" %>

<portlet:renderURL var="goBack">
	<portlet:param name="path" value="${parentPath}"/>
</portlet:renderURL>
<portlet:actionURL var="updateMetadata">
	<portlet:param name="action" value="updateMetadata"/>
	<portlet:param name="path" value="${path}"/>
	<portlet:param name="parentPath" value="${parentPath}"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<span class="coin-top-dir"><spring:message code="data.manager.portlet.metadata.header" arguments="${path}"/></span>
	<div>
		<form:form class="coin-form" action='${updateMetadata}' modelAttribute='metadata'>
			<div class="coin-form-input">
				<label for="owner">
					<spring:message code="data.manager.portlet.metadata.permissions.owner.label"/>
				</label>
				<form:hidden path="owner"/>
				<span style="margin-left: 5px;">${metadata.owner}</span>
				<form:errors path="owner" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="readPermissions">
					<spring:message code="data.manager.portlet.metadata.read.permissions.label"/>
				</label>
				<form:input path="readPermissions"/>
				<form:errors path="readPermissions" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="writePermissions">
					<spring:message code="data.manager.portlet.metadata.write.permissions.label"/>
				</label>
				<form:input path="writePermissions"/>
				<form:errors path="writePermissions" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="uid">
					<spring:message code="data.manager.portlet.metadata.uid.label"/>
				</label>
				<form:hidden path="uid"/>
				<span style="margin-left: 5px;">${metadata.uid}</span>
				<form:errors path="uid" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="lobcderWebDavMetadata.driSupervised">
					<spring:message code="data.manager.portlet.metadata.dri.supervised.label"/>
				</label>
				<form:checkbox path="lobcderWebDavMetadata.driSupervised"/>
				<form:errors path="lobcderWebDavMetadata.driSupervised" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="lobcderWebDavMetadata.driChecksum">
					<spring:message code="data.manager.portlet.metadata.dri.checksum.label"/>
				</label>
				<form:hidden path="lobcderWebDavMetadata.driChecksum"/>
				<span style="margin-left: 5px;">${metadata.lobcderWebDavMetadata.driChecksum}</span>
				<form:errors path="lobcderWebDavMetadata.driChecksum" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="lobcderWebDavMetadata.driLastValidationDateMs">
					<spring:message code="data.manager.portlet.metadata.dri.last.validation.data.ms.label"/>
				</label>
				<form:hidden path="lobcderWebDavMetadata.driLastValidationDateMs"/>
				<span style="margin-left: 5px;">${metadata.lobcderWebDavMetadata.driLastValidationDateMs}</span>
				<form:errors path="lobcderWebDavMetadata.driLastValidationDateMs" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="lobcderWebDavMetadata.creationDate">
					<spring:message code="data.manager.portlet.metadata.creation.date.label"/>
				</label>
				<form:hidden path="lobcderWebDavMetadata.creationDate"/>
				<span style="margin-left: 5px;">${metadata.lobcderWebDavMetadata.creationDate}</span>
				<form:errors path="lobcderWebDavMetadata.creationDate" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="lobcderWebDavMetadata.modificationDate">
					<spring:message code="data.manager.portlet.metadata.modification.date.label"/>
				</label>
				<form:hidden path="lobcderWebDavMetadata.modificationDate"/>
				<span style="margin-left: 5px;">${metadata.lobcderWebDavMetadata.creationDate}</span>
				<form:errors path="lobcderWebDavMetadata.modificationDate" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="lobcderWebDavMetadata.format">
					<spring:message code="data.manager.portlet.metadata.format.label"/>
				</label>
				<form:hidden path="lobcderWebDavMetadata.format"/>
				<span style="margin-left: 5px;">${metadata.lobcderWebDavMetadata.format}</span>
				<form:errors path="lobcderWebDavMetadata.format" cssClass="coin-error-panel"/>
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