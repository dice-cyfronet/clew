<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="menu.jsp" %>

<div class="coin-content">
	<p>
		<spring:message code='cloud.manager.portlet.available.atomic.services.list.header'/>
	</p>
	<c:forEach var='atomicService' items='${atomicServices}'>
		<div class="coin-panel">
			<span class="coin-header">${atomicService.name}</span>
			<span class="coin-description">
				<c:choose>
					<c:when test="${atomicService.description == null or atomicService.description == ''}">
						<i>no description</i>
					</c:when>
					<c:otherwise>
						${atomicService.description}
					</c:otherwise>
				</c:choose>
			</span>
			<span class="coin-actions">
				<c:choose>
					<c:when test="${atomicService.active}">
						<portlet:actionURL var="startAs">
							<portlet:param name="action" value="startAtomicService"/>
							<portlet:param name="atomicServiceId" value="${atomicService.atomicServiceId}"/>
							<portlet:param name="workflowType" value="${workflowType}"/>
						</portlet:actionURL>
						<a class="coin-link" href="${startAs}">Start</a>
					</c:when>
					<c:otherwise>
						Not active
					</c:otherwise>
				</c:choose>
			</span>
		</div>
	</c:forEach>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
			</li>
		</ul>
	</div>
</div>