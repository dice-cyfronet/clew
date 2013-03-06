<%@ include file="../include.jsp" %>

<portlet:renderURL var="goBack">
	<portlet:param name="path" value="${parentPath}"/>
</portlet:renderURL>
<portlet:actionURL var="search">
	<portlet:param name="action" value="search"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<div>
		<form:form class="coin-form" action='${searchRequest}' modelAttribute='metadata'>
			<div class="coin-form-input">
				<label for="name">
					<spring:message code="data.manager.portlet.search.name.label"/>
				</label>
				<form:input path="name"/>
				<form:errors path="name" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-submit">
				<input type='submit' value='<spring:message code='data.manager.portlet.search.submit.label'/>'/>
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