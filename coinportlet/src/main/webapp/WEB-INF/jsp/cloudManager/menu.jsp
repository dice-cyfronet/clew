<portlet:renderURL var="genericInvoker">
    <portlet:param name="action" value="genericInvoker"/>
</portlet:renderURL>
<portlet:renderURL var="workflows">
    <portlet:param name="action" value="workflows"/>
</portlet:renderURL>

<ul>
	<li>
		<c:choose>
			<c:when test="${view == 'genericInvoker'}">
				<spring:message code="cloud.manager.portlet.menu.generic.invoker.label"/>
			</c:when>
			<c:otherwise>
				<a href="${genericInvoker}"><spring:message code="cloud.manager.portlet.menu.generic.invoker.label"/></a>
			</c:otherwise>
		</c:choose>
	</li>
	<li>
		<c:choose>
			<c:when test="${view == 'workflows'}">
				<spring:message code="cloud.manager.portlet.menu.workflows.label"/>
			</c:when>
			<c:otherwise>
				<a href="${workflows}"><spring:message code="cloud.manager.portlet.menu.workflows.label"/></a>
			</c:otherwise>
		</c:choose>
	</li>
</ul>