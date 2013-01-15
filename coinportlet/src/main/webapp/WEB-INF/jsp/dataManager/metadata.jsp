<%@ include file="../include.jsp" %>

<portlet:renderURL var="goBack">
	<portlet:param name="path" value="${parentPath}"/>
</portlet:renderURL>

<div class="coin-content coin-content-no-tabs">
	<span class="coin-top-dir"><spring:message code="data.manager.portlet.metadata.header" arguments="${path}"/></span>
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