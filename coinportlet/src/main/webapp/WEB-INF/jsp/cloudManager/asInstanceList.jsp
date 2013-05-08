<c:forEach var="entry" items="${activeAtomicServices}" varStatus="status">
	<c:set var="atomicService" value="${entry.key}"/>
	<div class="row-fluid row-hover" style="margin-bottom: 10px;">
		<div class="span2 text-right" style="font-size: larger;">
			<strong>
				<c:choose>
					<c:when test="${view == 'development'}">
							${atomicService.name}<br/>
							<em>
								<c:choose>
									<c:when test="${atomicService.published}">
										(type: atomic service)
									</c:when>
									<c:otherwise>
										(type: template)
									</c:otherwise>
								</c:choose>
							</em>
					</c:when>
					<c:otherwise>
						${atomicService.name}
					</c:otherwise>
				</c:choose>
			</strong>
		</div>
		<c:set var="atomicServiceInstances" value="${entry.value}"/>
		<c:choose>
			<c:when test="${view == 'development'}">
				<%@ include file="developmentInstanceItem.jsp" %>
			</c:when>
			<c:when test="${view == 'genericInvoker'}">
				<%@ include file="genericInvokerInstanceItem.jsp" %>
			</c:when>
			<c:otherwise>
				<p>Unknown view mode!</p>
			</c:otherwise>
		</c:choose>
	</div>
</c:forEach>