<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="menu.jsp" %>

<div class="coin-content">
	<p>
		<spring:message code='cloud.manager.portlet.available.atomic.services.list.header'/>
	</p>
	<c:forEach var='atomicService' items='${atomicServices}'>
		<c:if test="${(workflowType == 'portal' and atomicService.active) or workflowType == 'development'}">
			<div class="coin-panel">
				<div style="width: 30%; float: left; text-align: right;">
					<span class="coin-header">${atomicService.name}</span>
				</div>
				<div style="width: 70%; float: left;">
					<span class="coin-description">
						<span style="padding-left: 10px; display: block;">
							<c:choose>
								<c:when test="${atomicService.description == null or atomicService.description == ''}">
									<i>no description</i>
								</c:when>
								<c:otherwise>
									${atomicService.description}
								</c:otherwise>
							</c:choose>
						</span>
					</span>
					<span class="coin-actions">
						<c:choose>
							<c:when test="${atomicService.active}">
								<c:choose>
									<c:when test="${workflowType == 'development'}">
										<portlet:renderURL var="startDevAs">
											<portlet:param name="action" value="pickUserKey"/>
											<portlet:param name="atomicServiceId" value="${atomicService.atomicServiceId}"/>
											<portlet:param name="workflowType" value="${workflowType}"/>
										</portlet:renderURL>
										<a class="coin-link" href="${startDevAs}">Start</a>
									</c:when>
									<c:otherwise>
										<portlet:actionURL var="startAs">
											<portlet:param name="action" value="startAtomicService"/>
											<portlet:param name="atomicServiceId" value="${atomicService.atomicServiceId}"/>
											<portlet:param name="workflowType" value="${workflowType}"/>
										</portlet:actionURL>
										<a class="coin-link" href="${startAs}">Start</a>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:set var="inactiveAtomicServiceLink">inactiveAS-link-${atomicService.atomicServiceId}</c:set>
								<c:set var="inactiveAtomicServiceLabel">inactiveAS-label-${atomicService.atomicServiceId}</c:set>
								<a id="${inactiveAtomicServiceLink}" class="coin-link" href="${startAs}" style="visibility: hidden;">Start</a>
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
					</span>
				</div>
			</div>
		</c:if>
	</c:forEach>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
			</li>
		</ul>
	</div>
</div>