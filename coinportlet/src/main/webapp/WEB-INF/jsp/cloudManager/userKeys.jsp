<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="uploadKey">
	<portlet:param name="action" value="uploadKey"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<c:choose>
		<c:when test="${fn:length(userKeyList) > 0}">
			<div><spring:message code="cloud.manager.portlet.user.keys.label"/></div>
			<c:forEach var="userKey" items="${userKeyList}">
				<div class="coin-panel">
					<span class="coin-header">${userKey.keyName}</span>
					<span class="coin-description">
						<spring:message code="cloud.manager.portlet.user.key.fingerprint.label"/>:<br/>
						(${userKey.fingerprint})
					</span>
					<span class="coin-actions">
						<portlet:resourceURL var="getUserKey" id="getUserKey">
							<portlet:param name="keyId" value="${userKey.id}"/>
						</portlet:resourceURL>
						<portlet:actionURL var="removeUserKey">
							<portlet:param name="action" value="removeUserKey"/>
							<portlet:param name="userKeyId" value="${userKey.id}"/>
						</portlet:actionURL>
						<a class="coin-link" href='${getUserKey}'><spring:message code='cloud.manager.portlet.download.user.key.label'/></a>
						<br/>
						<c:set var="removeConfirmation">removeKey-${userKey.id}</c:set>
						<a id="${removeConfirmation}" class="coin-link" href='${removeUserKey}'><spring:message code='cloud.manager.portlet.remove.user.key.label'/></a>
						<script type="text/javascript">
							jQuery(document).ready(function() {
								jQuery('#${removeConfirmation}').click(function() {
									if(!confirm("<spring:message code='cloud.manager.portlet.remove.key.confirmation.label'/>")) {
										return false;
									}
								});
	    					});
						</script>
					</span>
				</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<spring:message code="cloud.manager.portlet.no.user.keys.label"/>
		</c:otherwise>
	</c:choose>
	<div><spring:message code="cloud.manager.portlet.remove.upload.key.label"/></div>
	<form:form class="coin-form" action='${uploadKey}' modelAttribute='uploadKeyRequest'>
		<div class="coin-form-input">
			<label for="keyName">
				<spring:message code="cloud.manager.portlet.upload.key.name.label"/>
			</label>
			<form:input path="keyName"/>
			<form:errors path="keyName" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="keyBody">
				<spring:message code="cloud.manager.portlet.upload.key.body.label"/>
			</label>
			<form:textarea path="keyBody" cols="40" rows="10"/>
			<form:errors path="keyBody" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-submit">
			<input type='submit' value='<spring:message code='cloud.manager.portlet.upload.key.submit.request'/>'/>
		</div>
	</form:form>
	<div style="font-size: small;">
		<spring:message code="cloud.manager.portlet.key.generation.info"/>
	</div>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
			</li>
		</ul>
	</div>
</div>