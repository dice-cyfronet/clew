<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="uploadPolicy">
	<portlet:param name="action" value="uploadPolicy"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<c:choose>
		<c:when test="${fn:length(policies) > 0}">
			<c:forEach var="policy" items="${policies}" varStatus="status">
				<portlet:actionURL var="deletePolicy">
					<portlet:param name="action" value="deletePolicy"/>
					<portlet:param name="policyName" value="${policy.key}"/>
				</portlet:actionURL>
				<c:set var="deleteConfirmation">deletePolicy-${status.index}</c:set>
				<div class="coin-panel">
					<div style="width: 20%; float: left;">
						<span class="coin-header" style="width: 100%; display: block;">${policy.key}</span>
					</div>
					<div style="width: 80%; float: left;">
						<c:choose>
							<c:when test="${empty policy.value}">
								<span class="coin-description" style="font-style: italic;"><spring:message code="policy.manager.portlet.policy.body.empty.label"/></span>
							</c:when>
							<c:otherwise>
								<span class="coin-description">
									<pre>
${fn:escapeXml(policy.value)}
									</pre>
								</span>
							</c:otherwise>
						</c:choose>
						<span class="coin-actions">
							<a class="coin-link" id="${deleteConfirmation}" href="${deletePolicy}"><spring:message code="policy.manager.portlet.delete.policy.label"/></a>
						</span>
					</div>
				</div>
				<script type="text/javascript">
					jQuery(document).ready(function() {
						jQuery('#${deleteConfirmation}').click(function() {
							if(!confirm("<spring:message code='policy.manager.portlet.delete.policy.confirmation.message'/>")) {
								return false;
							}
						});
   					});
				</script>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<spring:message code="policy.manager.portlet.no.policies.message"/>
		</c:otherwise>
	</c:choose>
	<div style="margin-top: 15px;">
		<spring:message code="policy.manager.portlet.upload.policy"/>
	</div>
	<form:form class="coin-form" action='${uploadPolicy}' modelAttribute='uploadPolicyRequest'>
		<div class="coin-form-input">
			<label for="policyName">
				<spring:message code="policy.manager.portlet.policy.name.label"/>
			</label>
			<form:input path="policyName"/>
			<form:errors path="policyName" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-input">
			<label for="policyBody">
				<spring:message code="policy.manager.portlet.policy.body.label"/>
			</label>
			<form:textarea path="policyBody"/>
			<form:errors path="policyBody" cssClass="coin-error-panel"/>
		</div>
		<div class="coin-form-submit">
			<input type='submit' value='<spring:message code='policy.manager.portlet.policy.upload.submit'/>'/>
		</div>
	</form:form>
</div>