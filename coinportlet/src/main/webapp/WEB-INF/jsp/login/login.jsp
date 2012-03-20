<%@ include file="../include.jsp" %>

<div class="coin-content">
	Authorizing and retrieving content...
	<form id="loginForm" action="${action}" style="visibility: hidden;">
		<input type="text" value="${login}"/>
		<input type="password" value="${password}"/>
	</form>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery("#loginForm").submit();
		});
	</script>
</div>