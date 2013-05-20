<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="uploadKey">
	<portlet:param name="action" value="uploadKey"/>
</portlet:actionURL>

<div>
	<c:choose>
		<c:when test="${fn:length(userKeyList) > 0}">
			<p class="lead"><spring:message code="cloud.manager.portlet.user.keys.label"/></p>
			<c:forEach var="userKey" items="${userKeyList}">
				<div class="row-fluid row-hover">
					<div class="span2 text-right" style="font-size: larger; word-wrap: break-word;">
						<strong>${userKey.keyName}</strong>
					</div>
					<div class="span8">
						<spring:message code="cloud.manager.portlet.user.key.fingerprint.label"/>:<br/>
						(${userKey.fingerprint})
					</div>
					<div class="span2">
						<portlet:resourceURL var="getUserKey" id="getUserKey">
							<portlet:param name="keyId" value="${userKey.id}"/>
						</portlet:resourceURL>
						<portlet:actionURL var="removeUserKey">
							<portlet:param name="action" value="removeUserKey"/>
							<portlet:param name="userKeyId" value="${userKey.id}"/>
						</portlet:actionURL>
						<a href='${getUserKey}'><spring:message code='cloud.manager.portlet.download.user.key.label'/></a>
						<br/>
						<c:set var="removeConfirmation">removeKey-${userKey.id}</c:set>
						<a id="${removeConfirmation}" href='${removeUserKey}'><spring:message code='cloud.manager.portlet.remove.user.key.label'/></a>
						<script type="text/javascript">
							jQuery(document).ready(function() {
								jQuery('#${removeConfirmation}').click(function() {
									if(!confirm("<spring:message code='cloud.manager.portlet.remove.key.confirmation.label'/>")) {
										return false;
									}
								});
	    					});
						</script>
					</div>
				</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<spring:message code="cloud.manager.portlet.no.user.keys.label"/>
		</c:otherwise>
	</c:choose>
	<form:form action='${uploadKey}' modelAttribute='uploadKeyRequest' enctype="multipart/form-data">
		<fieldset>
			<legend><spring:message code="cloud.manager.portlet.remove.upload.key.label"/></legend>
			<label for="keyName"><spring:message code="cloud.manager.portlet.upload.key.name.label"/></label>
			<form:input path="keyName"/>
			<form:errors path="keyName" cssClass="text-error"/>
			<label for="keyBody"><spring:message code="cloud.manager.portlet.upload.key.body.label"/></label>
			<form:input path="keyBody" type="file"/>
			<form:errors path="keyBody" cssClass="text-error"/>
			<br/>
			<button type='submit' class="btn"><spring:message code='cloud.manager.portlet.upload.key.submit.request'/></button>
		</fieldset>
	</form:form>
	<p><spring:message code="cloud.manager.portlet.key.generation.info"/></p>
	<div>
		<ul class="inline">
			<li>
				<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
			</li>
		</ul>
	</div>
</div>