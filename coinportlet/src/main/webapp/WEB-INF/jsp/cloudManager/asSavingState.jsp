<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:resourceURL var="asSavingStatusLink" id="asSavingStatus"/>

<div class="coin-content">
	<p>Please wait patiently while the new Atomic Service is being saved. The current state of the process is the following:
	<span id="asSavingState" class="coin-as-status">saving</span></p>
	<a href='<portlet:renderURL/>' id="mainPage" style="visibility: hidden;">Go back to the main page</a>
	<script type="text/javascript">
	    jQuery(document).ready(function() {
	    	window.updateAsSavingStatus = function(statusLink) {
	    		jQuery.get(statusLink, function(status) {
	    			if(jQuery('#asSavingState').text() != status) {
	    				jQuery('#asSavingState').text(status);
	    			}
	    			
	    			jQuery('#asSavingState').css('opacity', '1.0');
	    			
	    			if(status != 'done') {
	    				jQuery('#asSavingState').animate({
	    					opacity: 0
	    				}, 2000, function() {
	    					//on animation completed
	    				});
    				}

	    			if(status == 'done') {
	    				jQuery('#mainPage').css('visibility', 'visible');
	    			} else {
	    				setTimeout("updateAsSavingStatus('${asSavingStatusLink}')", 2000);
	    			}
	    		});
	    	};
	    	
	    	updateAsSavingStatus('${asSavingStatusLink}');
	    });
	</script>
</div>