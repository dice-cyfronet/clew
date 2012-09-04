<%@ include file="../include.jsp" %>

<div class="coin-content coin-content-no-tabs">
	Authorizing and retrieving content...
	<form id="loginForm" action="${loginServletPath}" style="visibility: hidden;" autocomplete="off">
		<input type="text" name="${loginParameterName}" value="${userLogin}"/>
		<input type="password" name="${passwordParameterName}" value="${userToken}"/>
		<input type="hidden" name="${destinationParameterName}" value="${userDestination}"/>
	</form>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery("#loginForm").submit();
		});
	</script>
</div>