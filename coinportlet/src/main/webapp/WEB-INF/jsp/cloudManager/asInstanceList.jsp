<c:forEach var="entry" items="${activeAtomicServices}" varStatus="status">
	<c:set var="atomicService" value="${entry.key}"/>
	<div class="coin-panel">
		<div style="width: 30%; float: left; text-align: right;">
			<c:choose>
				<c:when test="${view == 'development'}">
					<span class="coin-header">
						<span style="padding-right: 10px; display: block;">${atomicService.name}</span>
						<span style="padding-right: 10px; font-style: italic; display: block;">
							<c:choose>
								<c:when test="${atomicService.published}">
									(type: atomic service)
								</c:when>
								<c:otherwise>
									(type: template)
								</c:otherwise>
							</c:choose>
						</span>
					</span>
				</c:when>
				<c:otherwise>
					<span class="coin-header">
						<span style="padding-right: 10px; display: block;">${atomicService.name}</span>
					</span>
				</c:otherwise>
			</c:choose>
		</div>
		<c:set var="atomicServiceInstances" value="${entry.value}"/>
		<div style="width: 70%; float: left;">
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
	</div>
</c:forEach>