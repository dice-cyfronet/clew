<portlet:renderURL var="development">
    <portlet:param name="action" value="development"/>
</portlet:renderURL>
<portlet:renderURL var="genericInvoker">
    <portlet:param name="action" value="genericInvoker"/>
</portlet:renderURL>
<portlet:renderURL var="workflows">
    <portlet:param name="action" value="workflows"/>
</portlet:renderURL>

<ul class="nav nav-tabs">
	<c:if test="${developerMode}">
		<c:choose>
			<c:when test="${view == 'development'}">
				<li class="active">
			</c:when>
			<c:otherwise>
				<li>
			</c:otherwise>
		</c:choose>
			<a href="${development}"><spring:message code="cloud.manager.portlet.menu.development.label"/></a>
		</li>
	</c:if>
	<c:choose>
		<c:when test="${view == 'genericInvoker'}">
			<li class="active">
		</c:when>
		<c:otherwise>
			<li>
		</c:otherwise>
	</c:choose>
		<a href="${genericInvoker}"><spring:message code="cloud.manager.portlet.menu.generic.invoker.label"/></a>
	</li>
	<c:choose>
		<c:when test="${view == 'workflows'}">
			<li class="active">
		</c:when>
		<c:otherwise>
			<li>
		</c:otherwise>
	</c:choose>
		<a href="${workflows}"><spring:message code="cloud.manager.portlet.menu.workflows.label"/></a>
	</li>
</ul>