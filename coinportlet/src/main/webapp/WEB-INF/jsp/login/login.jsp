<%@ include file="../include.jsp" %>

<div class="coin-content">
	Authorizing and retrieving content...
	<form id="loginForm" action="${loginServletPath}?${destinationParameterName}=${userDestination}" style="visibility: hidden;">
		<input type="text" name="${loginParameterName}" value="${userLogin}"/>
		<input type="password" name="${passwordParameterName}" value="${userToken}"/>
	</form>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery("#loginForm").submit();
		});
	</script>
</div>