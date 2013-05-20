<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="menu.jsp" %>

<p class="lead"><spring:message code='cloud.manager.portlet.available.atomic.services.list.header'/></p>
<c:forEach var='atomicService' items='${atomicServices}'>
	<c:if test="${(workflowType == 'portal' and atomicService.active) or workflowType == 'development'}">
		<div class="row-fluid row-hover">
			<div class="span2 text-right" style="font-size: larger; word-wrap: break-word;">
				<strong>${atomicService.name}</strong>
			</div>
			<div class="span8">
				<c:choose>
					<c:when test="${atomicService.description == null or atomicService.description == ''}">
						<em>no description</em>
					</c:when>
					<c:otherwise>
						<p>${atomicService.description}</p>
					</c:otherwise>
				</c:choose>
				<p>Id: ${atomicService.atomicServiceId}</p>
			</div>
			<div class="span2">
				<c:choose>
					<c:when test="${atomicService.active}">
						<c:choose>
							<c:when test="${workflowType == 'development'}">
								<portlet:renderURL var="startDevAs">
									<portlet:param name="action" value="pickUserKey"/>
									<portlet:param name="atomicServiceId" value="${atomicService.atomicServiceId}"/>
									<portlet:param name="workflowType" value="${workflowType}"/>
								</portlet:renderURL>
								<a href="${startDevAs}">Start</a>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${fn:contains(runningAsIds, atomicService.atomicServiceId)}">
										<spring:message code="cloud.manager.portlet.as.already.running.in.invoker.mode"/>
									</c:when>
									<c:otherwise>
										<portlet:actionURL var="startAs">
											<portlet:param name="action" value="startAtomicService"/>
											<portlet:param name="atomicServiceId" value="${atomicService.atomicServiceId}"/>
											<portlet:param name="workflowType" value="${workflowType}"/>
										</portlet:actionURL>
										<a href="${startAs}">Start</a>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:set var="inactiveAtomicServiceLink">inactiveAS-link-${atomicService.atomicServiceId}</c:set>
						<c:set var="inactiveAtomicServiceLabel">inactiveAS-label-${atomicService.atomicServiceId}</c:set>
						<a id="${inactiveAtomicServiceLink}" href="${startAs}" style="visibility: hidden;">Start</a><br/>
						<span id="${inactiveAtomicServiceLabel}">Not active</span>
						<portlet:resourceURL var="checkAsStatus" id="asSavingStatus">
							<portlet:param name="atomicServiceId" value="${atomicService.atomicServiceId}"/>
						</portlet:resourceURL>
						<script type="text/javascript">
	    					jQuery(document).ready(function() {
	    						if(window.updates == null) {
	    							window.updates = {};
	    						}
	    						
	    						window.updates['${inactiveAtomicServiceLink}'] = function() {
	    							jQuery.get('${checkAsStatus}', function(status) {
	    								if(status === 'done') {
	    									jQuery('#${inactiveAtomicServiceLabel}').text('');
	    									jQuery('#${inactiveAtomicServiceLink}').css('visibility', 'visible');
	    								} else {
	    									setTimeout("updates['${inactiveAtomicServiceLink}']()", 2000);
	    								}
	    							});
	    						};
	    						
	    						updates['${inactiveAtomicServiceLink}']();
	    					});
	    				</script>
					</c:otherwise>
				</c:choose>
				<c:if test="${(atomicService.owner == userName or asCloudAdminFlag) and workflowType == 'development'}">
					<portlet:renderURL var="editAs">
						<portlet:param name="action" value="editAs"/>
						<portlet:param name="atomicServiceId" value="${atomicService.atomicServiceId}"/>
						<portlet:param name="workflowType" value="${workflowType}"/>
					</portlet:renderURL>
					<br/><a href="${editAs}">Edit</a>
					
					<c:set var="removeAsLink">removeAS-link-${atomicService.atomicServiceId}</c:set>
					<portlet:actionURL var="removeAs">
						<portlet:param name="action" value="removeAs"/>
						<portlet:param name="atomicServiceId" value="${atomicService.atomicServiceId}"/>
						<portlet:param name="workflowType" value="${workflowType}"/>
					</portlet:actionURL>
					<br/><a id="${removeAsLink}" href="${removeAs}">Remove</a>
					<script type="text/javascript">
						jQuery(document).ready(function() {
							jQuery('#${removeAsLink}').click(function() {
								if(!confirm("<spring:message code='cloud.manager.portlet.remove.as.confirmation.message'/>")) {
									return false;
								}
							});
						});
					</script>
				</c:if>
			</div>
		</div>
	</c:if>
</c:forEach>
<c:if test="${asRemovalError != null}">
	<script type="text/javascript">
		jQuery(document).ready(function() {
			alert('<spring:message code="cloud.manager.portlet.as.removal.error.message"/>');
		});
	</script>
</c:if>
<div>
	<ul class="inline">
		<li>
			<a href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
		</li>
	</ul>
</div>