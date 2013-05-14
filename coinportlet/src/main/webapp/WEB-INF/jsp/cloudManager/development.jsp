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
			<p class="lead"><spring:message code="cloud.manager.portlet.no.development.instances"/></p>
		</c:otherwise>
	</c:choose>
	<div>
		<ul class="inline">
			<li>
				<a href="${startAtomicService}"><spring:message code="cloud.manager.portlet.manage.atomic.service.instance.label"/></a>
			</li>
			<c:if test="${fn:length(activeAtomicServices) > 0}">
				<li>
					<portlet:actionURL var="stopAtomicServices">
						<portlet:param name="action" value="stopWorkflow"/>
						<portlet:param name="workflowType" value="development"/>
					</portlet:actionURL>
					<a id="stopDevWorkflow" href="${stopAtomicServices}"><spring:message code="cloud.manager.portlet.stop.dev.workflow.label"/></a>
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
				<a href="${manageUserKeys}"><spring:message code="cloud.manager.portlet.manage.user.keys.label"/></a>
			</li>
		</ul>
	</div>
</div>