<portlet:renderURL var="development">
    <portlet:param name="action" value="development"/>
</portlet:renderURL>
<portlet:renderURL var="genericInvoker">
    <portlet:param name="action" value="genericInvoker"/>
</portlet:renderURL>
<portlet:renderURL var="workflows">
    <portlet:param name="action" value="workflows"/>
</portlet:renderURL>

<ul class="coin-menu">
	<c:if test="${developerMode}">
		<li class="coin-development">
			<c:choose>
				<c:when test="${view == 'development'}">
					<spring:message code="cloud.manager.portlet.menu.development.label"/>
				</c:when>
				<c:otherwise>
					<a class="coin-link" href="${development}"><spring:message code="cloud.manager.portlet.menu.development.label"/></a>
				</c:otherwise>
			</c:choose>
		</li>
	</c:if>
	<li>
		<c:choose>
			<c:when test="${view == 'genericInvoker'}">
				<spring:message code="cloud.manager.portlet.menu.generic.invoker.label"/>
			</c:when>
			<c:otherwise>
				<a class="coin-link" href="${genericInvoker}"><spring:message code="cloud.manager.portlet.menu.generic.invoker.label"/></a>
			</c:otherwise>
		</c:choose>
	</li>
	<li>
		<c:choose>
			<c:when test="${view == 'workflows'}">
				<spring:message code="cloud.manager.portlet.menu.workflows.label"/>
			</c:when>
			<c:otherwise>
				<a class="coin-link" href="${workflows}"><spring:message code="cloud.manager.portlet.menu.workflows.label"/></a>
			</c:otherwise>
		</c:choose>
	</li>
</ul>