<%@ include file="../include.jsp" %>

<portlet:renderURL var="goBack">
	<portlet:param name="path" value="${parentPath}"/>
</portlet:renderURL>
<portlet:actionURL var="search">
	<portlet:param name="action" value="search"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<div>
		<form:form class="coin-form" action='${search}' modelAttribute='searchRequest'>
			<!--
			<div class="coin-form-input">
				<label for="name">
					<spring:message code="data.manager.portlet.search.name.label"/>
				</label>
				<form:input path="name"/>
				<form:errors path="name" cssClass="coin-error-panel"/>
			</div>
			-->
			<div class="coin-form-input">
				<label for="startModificationDate">
					<spring:message code="data.manager.portlet.search.start.modification.date.label"/>
				</label>
				<form:input path="startModificationDate"/>
				<form:errors path="startModificationDate" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-input">
				<label for="stopModificationDate">
					<spring:message code="data.manager.portlet.search.stop.modification.date.label"/>
				</label>
				<form:input path="stopModificationDate"/>
				<form:errors path="stopModificationDate" cssClass="coin-error-panel"/>
			</div>
			<div class="coin-form-submit">
				<input type='submit' value='<spring:message code='data.manager.portlet.search.submit.label'/>'/>
			</div>
		</form:form>
		<c:if test="${foundEntries != null}">
			<c:choose>
				<c:when test="${fn:length(foundEntries) > 0}">
					<div class="coin-file-list">
						<ul>
							<c:forEach var="entry" items="${foundEntries}">
								<portlet:resourceURL id="getFile" var="getFile">
									<portlet:param name="filePath" value="${entry.name}"/>
									<portlet:param name="size" value="${entry.bytes}"/>
								</portlet:resourceURL>
								<portlet:actionURL var="delete">
									<portlet:param name="action" value="deleteResource"/>
									<portlet:param name="path" value="${entry.name}"/>
								</portlet:actionURL>
								<portlet:renderURL var="metadata">
									<portlet:param name="action" value="metadata"/>
									<portlet:param name="path" value="${entry.name}"/>
									<portlet:param name="parentPath" value="${parentPaths[entry.name]}"/>
								</portlet:renderURL>
								<li>
									<a href="${getFile}" class="coin-link">${entry.name}</a>
									<a href="${delete}" class="coin-link" style="margin-left: 10px; float: right;">Delete</a>
									<a href="${metadata}" class="coin-link" style="margin-left: 10px; float: right;">Metadata</a>
									<span class="coin-file-size"><fmt:formatNumber value="${entry.bytes / 1024}" maxFractionDigits="0" groupingUsed="false"/> kB</span>
								</li>
							</c:forEach>
						</ul>
					</div>
				</c:when>
				<c:otherwise>
					<spring:message code="data.manager.portlet.no.found.entries.label"/>
				</c:otherwise>
			</c:choose>
		</c:if>
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
	<script type="text/javascript">
		jQuery(document).ready(function() {
			$("#startModificationDate").datepicker();
			$("#stopModificationDate").datepicker();
		});
	</script>
</div>