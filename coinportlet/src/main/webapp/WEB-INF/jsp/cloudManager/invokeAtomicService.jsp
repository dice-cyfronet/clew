<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="invokeAtomicService">
	<portlet:param name="action" value="invokeAtomicService"/>
</portlet:actionURL>

<div class="coin-content coin-content-no-tabs">
	<c:if test="${negativeMessage != null}">
		<div class="coin-error-panel">${negativeMessage}</div>
	</c:if>
	<c:if test="${asiRedirections != null and fn:length(asiRedirections) > 0}">
		<span style="font-style: italic; margin-top: 10px; display: block;"><spring:message code="cloud.manager.portlet.invocation.redirections.label"/></span>
		<c:forEach var="red" items="${asiRedirections}">
			<span style="margin-left: 20px;"><spring:message code="cloud.manager.portlet.invocation.redirection.host" arguments="${red.name},${red.host}"/></span><br/>
			<span style="margin-left: 20px;"><spring:message code="cloud.manager.portlet.invocation.redirection.port" arguments="${red.name},${red.fromPort}"/></span><br/>
		</c:forEach>
	</c:if>
	<c:if test="${fn:length(webappEndpoints) > 0}">
		<span>Web Application endpoints:</span><br/><br/>
		<c:forEach var="endpoint" items="${webappEndpoints}">
			<a class="coin-link" target="_blank" href="${authEndpointLinks[endpoint.id]}${additionalQuery}">${endpointLinks[endpoint.id]}${additionalQuery}</a><br/>
		</c:forEach>
	</c:if>
	<c:if test="${fn:length(wsEndpoints) > 0}">
		<span style="font-style: italic; margin-top: 10px; display: block;"><spring:message code="cloud.manager.portlet.ws.endpoint.list.label"/></span>
		<c:forEach var="ws" items="${wsEndpoints}">
			<span style="margin-left: 20px;">URL: <span style="font-family: monospace; font-size: smaller;">${endpointLinks[ws.id]}</span></span><br/>
			<div style="margin-left: 20px;" id="accordion">
				<h5 style="color: #37b2d1; cursor: pointer; margin-top: 0px; margin-bottom: 0px;">Show/Hide descriptor</h5>
				<div style="font-family: monospace; font-size: smaller; overflow: auto;">
					<c:if test="${ws.descriptor == null}">
						No descriptor available
					</c:if>
					<pre>
${fn:escapeXml(ws.descriptor)}
					</pre>
				</div>
			</div>
			<script type="text/javascript">
				jQuery(document).ready(function() {
					jQuery('#accordion').accordion({
			            collapsible: true,
			            active: false,
			            change: function(event, ui) {
			            	//in case the size of the page changes...
		    				pm({
				        		target: parent,
				        		type: 'resizeEvent',
				        		data: {size: (document.body.scrollHeight)}
				        	});
			            }
					});
				});
			</script>
		</c:forEach>
	</c:if>
	<c:if test="${atomicServiceInvokable}">
		<c:if test="${invocationPath != null}">
			<div>
				<span>Invocation URL template:</span><br/>
				<a class="coin-link" target="_blank" href="${invocationPath}" style="padding-left: 20px;">Invocation template</a>
			</div>
		</c:if>
		<form:form class="coin-form" action='${invokeAtomicService}' modelAttribute='invokeAtomicServiceRequest'>
			<form:hidden path="atomicServiceInstanceId"/>
			<form:hidden path="workflowId"/>
			<form:hidden path="configurationId"/>
			<form:hidden path="atomicServiceId"/>
			<form:hidden path="invocationPath"/>
			<form:hidden path="postfix"/>
			<form:hidden path="port"/>
			<form:hidden path="host"/>
			<div class="coin-form-input">
				<label for="method">
					<spring:message code="cloud.manager.portlet.submit.atomic.service.method.label"/>
				</label>
				<form:select path="method" items="${atomicServiceMethodList}"/>
			</div>
			<c:forEach var="formField" items="${invokeAtomicServiceRequest.formFields}" varStatus="index">
				<div class="coin-form-input">
					<label for="formFields[${index.index}].value">
						Request parameter <i>${formField.name}</i>
					</label>
					<form:input path="formFields[${index.index}].value"/><br/>
					<form:hidden path="formFields[${index.index}].name"/>
				</div>
			</c:forEach>
			<div class="coin-form-submit">
				<input type='submit' value='<spring:message code='cloud.manager.portlet.invoke.atomic.service.request'/>'/>
			</div>
		</form:form>
	</c:if>
	<c:if test="${not empty atomicServiceInvocationResult}">
		<c:choose>
			<c:when test="${atomicServiceInvocationCode != '200 - OK'}">
				<c:set var="style">margin-top: 20px; margin-bottom: 20px; margin-left: auto; margin-right: auto; padding: 10px; background-color: #efc09f;</c:set>
			</c:when>
			<c:otherwise>
				<c:set var="style">margin-top: 20px; margin-bottom: 20px; margin-left: auto; margin-right: auto; padding: 10px;</c:set>
			</c:otherwise>
		</c:choose>
		<div style="${style}">
			<span>Response code: </span><br/><span style="font-weight: bold; padding: 10px; display: block;">${atomicServiceInvocationCode}</span>
			<br/>
			<span>Response body: </span><br/><span style="font-weight: bold; padding: 10px; display: block;">${atomicServiceInvocationResult}</span>
		</div>
	</c:if>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href='<portlet:renderURL/>'><spring:message code='cloud.manager.portlet.return.to.main.view.label'/></a>
			</li>
		</ul>
	</div>
</div>