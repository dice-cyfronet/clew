<%@ include file="../include.jsp" %>
<%@ include file="menu.jsp" %>

<portlet:renderURL var="startAtomicService">
    <portlet:param name="action" value="startAtomicService"/>
    <portlet:param name="workflowType" value="portal"/>
</portlet:renderURL>

<div>
	<c:choose>
		<c:when test="${fn:length(activeAtomicServices) > 0}">
			<%@ include file="asInstanceList.jsp" %>
		</c:when>
		<c:otherwise>
			<div><spring:message code="cloud.manager.portlet.no.atomic.service.instances"/></div>
		</c:otherwise>
	</c:choose>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href="${startAtomicService}"><spring:message code="cloud.manager.portlet.start.atomic.service.instance.label"/></a>
			</li>
			<c:if test="${fn:length(activeAtomicServices) > 0}">
				<li>
					<portlet:actionURL var="stopPortalAtomicServices">
						<portlet:param name="action" value="stopWorkflow"/>
						<portlet:param name="workflowType" value="portal"/>
					</portlet:actionURL>
					<a id="stopPortalWorkflow" class="coin-link" href="${stopPortalAtomicServices}"><spring:message code="cloud.manager.portlet.stop.portal.workflow.label"/></a>
				</li>
				<script type="text/javascript">
					jQuery(document).ready(function() {
						jQuery('#stopPortalWorkflow').click(function() {
							if(!confirm("<spring:message code='cloud.manager.portlet.stop.portal.workflow.confirmation.label'/>")) {
								return false;
							}
						});
   					});
				</script>
			</c:if>
		</ul>
	</div>
</div>