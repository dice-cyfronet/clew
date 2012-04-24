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
		<c:choose>
			<c:when test="${view == 'development'}">
				<li class="coin-development coin-selected-tab">
					<spring:message code="cloud.manager.portlet.menu.development.label"/>
				</li>
			</c:when>
			<c:otherwise>
				<li class="coin-development">
					<a class="coin-link" href="${development}"><spring:message code="cloud.manager.portlet.menu.development.label"/></a>
				</li>
			</c:otherwise>
		</c:choose>
	</c:if>
	<c:choose>
		<c:when test="${view == 'genericInvoker'}">
			<li class="coin-selected-tab">
				<spring:message code="cloud.manager.portlet.menu.generic.invoker.label"/>
			</li>
		</c:when>
		<c:otherwise>
			<li>
				<a class="coin-link" href="${genericInvoker}"><spring:message code="cloud.manager.portlet.menu.generic.invoker.label"/></a>
			</li>
		</c:otherwise>
	</c:choose>
	<!--
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
	-->
</ul>