<%@ include file="../include.jsp" %>

<%@ include file="menu.jsp" %>

<portlet:renderURL var="startAtomicService">
    <portlet:param name="action" value="startAtomicService"/>
    <portlet:param name="workflowType" value="development"/>
</portlet:renderURL>

<div>
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
				<a class="coin-link" href="${startAtomicService}"><spring:message code="cloud.manager.portlet.start.atomic.service.instance.label"/></a>
			</li>
			<c:if test="${fn:length(activeAtomicServices) > 0}">
				<li>
					<portlet:actionURL var="stopAtomicServices">
						<portlet:param name="action" value="stopWorkflow"/>
						<portlet:param name="workflowType" value="development"/>
					</portlet:actionURL>
					<a id="stopDevWorkflow" class="coin-link" href="${stopAtomicServices}"><spring:message code="cloud.manager.portlet.stop.dev.workflow.label"/></a>
				</li>
				<script type="text/javascript">
					jQuery(document).ready(function() {
						jQuery('#stopDevWorkflow').click(function() {
							if(!confirm("<spring:message code='cloud.manager.portlet.stop.dev.workflow.confirmation.label'/>")) {
								return false;
							}
						});
					});
				</script>
			</c:if>
			<li>
				<portlet:renderURL var="manageUserKeys">
					<portlet:param name="action" value="userKeys"/>
					<portlet:param name="workflowType" value="development"/>
				</portlet:renderURL>
				<a class="coin-link" href="${manageUserKeys}"><spring:message code="cloud.manager.portlet.manage.user.keys.label"/></a>
			</li>
		</ul>
	</div>
</div>