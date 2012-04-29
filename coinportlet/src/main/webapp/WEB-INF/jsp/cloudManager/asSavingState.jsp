<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:resourceURL var="asSavingStatusLink" id="asSavingStatus"/>

<div class="coin-content coin-content-no-tabs">
	<p>Please wait patiently while the new Atomic Service is being saved. The current state of the process is the following:
	<span id="asSavingState" style="font-weight: bold; color: #db7024;">saving</span></p>
	<div class="coin-menu-bottom">
		<ul>
			<li>
				<a class="coin-link" href='<portlet:renderURL/>' id="mainPage" style="visibility: hidden;">Go back to the main page</a>
			</li>
		</ul>
	</div>
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
	    					opacity: 0.5
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