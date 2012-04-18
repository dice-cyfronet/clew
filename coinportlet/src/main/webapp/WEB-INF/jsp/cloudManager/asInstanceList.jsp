<div class="coin-tabs-container">
	<div class="coin-tabs">
		<c:forEach var="atomicService" items="${activeAtomicServices}" varStatus="status">
			<c:choose>
				<c:when test="${atomicService.atomicServiceId == currentAtomicServiceId and status.first}">
					<span class="coin-tab coin-selected coin-first">
				</c:when>
				<c:when test="${atomicService.atomicServiceId == currentAtomicServiceId and status.last}">
					<span class="coin-tab coin-selected coin-last">
				</c:when>
				<c:when test="${atomicService.atomicServiceId == currentAtomicServiceId}">
					<span class="coin-tab coin-selected">
				</c:when>
				<c:when test="${status.first}">
					<span class="coin-tab coin-first">
				</c:when>
				<c:when test="${status.last}">
					<span class="coin-tab coin-last">
				</c:when>
				<c:otherwise>
					<span class="coin-tab">
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${atomicService.atomicServiceId == currentAtomicServiceId}">
					${atomicService.name}
				</c:when>
				<c:otherwise>
					<portlet:renderURL var="showAtomicService">
						<portlet:param name="action" value="${view}"/>
						<portlet:param name="currentAtomicService" value="${atomicService.atomicServiceId}"/>
					</portlet:renderURL>
					<a class="coin-link" href="${showAtomicService}">${atomicService.name}</a>
				</c:otherwise>
			</c:choose>
			<br/>
			<c:choose>
				<c:when test="${atomicService.published}">
					(<spring:message code="cloud.manager.portlet.atomic.service.type.label"/>,
				</c:when>
				<c:otherwise>
					(<spring:message code="cloud.manager.portlet.template.type.label"/>,
				</c:otherwise>
			</c:choose>
			<spring:message code="cloud.manager.portlet.number.of.instances.label" arguments="${fn:length(atomicServiceInstances)}"/>)
			</span>
		</c:forEach>
	</div>
	<c:choose>
		<c:when test="${view == 'development'}">
			<%@ include file="developmentInstanceItem.jsp" %>
		</c:when>
		<c:when test="${view == 'genericInvoker'}">
			<%@ include file="genericInvokerInstanceItem.jsp" %>
		</c:when>
		<c:otherwise>
			Unknown view mode!
		</c:otherwise>
	</c:choose>
</div>