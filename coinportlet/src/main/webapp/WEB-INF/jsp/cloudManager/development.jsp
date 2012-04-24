<%@ include file="../include.jsp" %>

<%@ include file="menu.jsp" %>

<portlet:renderURL var="startAtomicService">
    <portlet:param name="action" value="startAtomicService"/>
    <portlet:param name="workflowType" value="development"/>
</portlet:renderURL>

<div class="coin-content">
	<c:choose>
		<c:when test="${fn:length(activeAtomicServices) > 0}">
			<%@ include file="asInstanceList.jsp" %>
		</c:when>
		<c:otherwise>
			<spring:message code="cloud.manager.portlet.no.development.instances"/>
		</c:otherwise>
	</c:choose>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href="${startAtomicService}">Start new development instance</a>
			</li>
			<c:if test="${fn:length(activeAtomicServices) > 0}">
				<li>
					<portlet:actionURL var="stopAtomicServices">
						<portlet:param name="action" value="stopWorkflow"/>
						<portlet:param name="workflowType" value="development"/>
					</portlet:actionURL>
					<a class="coin-link" href="${stopAtomicServices}"><spring:message code="cloud.manager.portlet.stop.portal.workflow.label"/></a>
				</li>
			</c:if>
		</ul>
	</div>
</div>