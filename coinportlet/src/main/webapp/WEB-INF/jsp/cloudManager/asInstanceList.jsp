<c:forEach var="entry" items="${activeAtomicServices}" varStatus="status">
	<c:set var="atomicService" value="${entry.key}"/>
	<div class="coin-panel">
		<c:choose>
			<c:when test="${view == 'development'}">
				<span class="coin-header">
					${atomicService.name}<br/>
					<c:choose>
						<c:when test="${atomicService.published}">
							<i>(type: atomic service)</i>
						</c:when>
						<c:otherwise>
							<i>(type: template)</i>
						</c:otherwise>
					</c:choose>
				</span>
			</c:when>
			<c:otherwise>
				<span class="coin-header">${atomicService.name}</span>
			</c:otherwise>
		</c:choose>
		<c:set var="atomicServiceInstances" value="${entry.value}"/>
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
</c:forEach>