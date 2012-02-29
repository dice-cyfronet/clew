<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="menu.jsp" %>

<div class="coin-content">
	<p>
		<spring:message code='cloud.manager.portlet.available.atomic.services.list.header'/>
	</p>
	<c:forEach var='atomicService' items='${atomicServices}' varStatus='loopStatus'>
		<div class="coin-panel">
			<div class="coin-left-column">
				<span class="coin-header">${atomicService.name}</span><br/>
				<span class="coin-description">${atomicService.description}</span>
			</div>
			<span class="coin-actions">
				<portlet:actionURL var="startAs">
					<portlet:param name="action" value="startAtomicService"/>
					<portlet:param name="atomicServiceId" value="${atomicService.atomicServiceId}"/>
				</portlet:actionURL>
				<a class="coin-link" href="${startAs}"><spring:message code="cloud.manager.portlet.start.atomic.service"/></a>
			</span>
		</div>
	</c:forEach>
</div>
<div class="menu-bottom">
	<ul>
		<li>
			<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
		</li>
	</ul>
</div>