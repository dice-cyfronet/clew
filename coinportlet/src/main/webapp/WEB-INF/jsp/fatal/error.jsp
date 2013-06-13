<div class="alert alert-block alert-error">
	<h4>Whoops!</h4>
	<p>It looks like an unforseen error occurred. Please, try again to submit your request and if the error
	still occurs contact the service maintainers. Error details are the following:</p>
	<p><strong>Message: </strong><em>${errorMessage}</em></p>
	<button type="button" class="btn btn-danger" data-toggle="collapse" data-target="#stackTrace" style="margin-top: 20px;">
		Show/Hide stack trace
	</button>
	<div id="stackTrace" class="collapse" style="margin-top: 20px;">
		<pre>
${errorStacktrace}
		</pre>
	</div>
</div>
<script type="text/javascript">
	jQuery(document).ready(function() {
	    $('#stackTrace').on('shown', function() {
	    	pm({
        		target: parent,
        		type: 'resizeEvent',
        		data: {size: (document.body.scrollHeight)}
        	});
		});
	    $('#stackTrace').on('hidden', function() {
	    	pm({
        		target: parent,
        		type: 'resizeEvent',
        		data: {size: (document.body.scrollHeight)}
        	});
		});
	});
</script>