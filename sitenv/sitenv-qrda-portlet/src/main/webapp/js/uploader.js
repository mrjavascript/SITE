$(function() {
	'use strict';

	// Change this to the location of your server-side upload handler:
	$('#qrdauploadprogress').hide();
	$('#qrdauploadfile').fileupload({
		url : $( '#QRDAValidationForm' ).attr( 'action' ),
		dataType : 'json',
		autoUpload : false,
		type : 'POST',
		contenttype : false,
		replaceFileInput : false,
		done : function(e, data) {
			
			var result = data.result;
        	qrdaAjaxValidationResultHandler(result.body);
			/*
			var results = data.result.body;
        	
        	var iconurl = (results.IsSuccess == "true")? window.currentContextPath + "/images/icn_alert_success.png" :
        									window.currentContextPath + "/images/icn_alert_error.png" ;
        	
        	$('#qrdaWidget .blockMsg .progressorpanel img').attr('src',iconurl);
      
        	
        	$('#qrdaWidget .blockMsg .progressorpanel .lbl').text(results.ErrorMessage);
        	
        	if(window.qrdaWidget)
        	{
        		window.qrdaUploadTimeout = setTimeout(function(){
        				window.qrdaWidget.unbind("click");
        				window.qrdaWidget.unblock();
        			},10000);
        		
        		
        		window.qrdaWidget.bind("click", function() { 
        			window.qrdaWidget.unbind("click");
        			clearTimeout(window.qrdaUploadTimeout);
        			window.qrdaWidget.unblock(); 
        			window.qrdaWidget.attr('title','Click to hide this message.').click($.unblockUI); 
	            });
        		
        	}

			window.setTimeout(function() {
				$('#qrdauploadprogress').fadeOut(400, function() {
					$('#qrdauploadprogress .progress-bar').css('width', '0%');
					
				});

			}, 1000);*/
		},
		progressall : function(e, data) {
			var progressval = parseInt(data.loaded / data.total * 100, 10);
			//$('#progress').fadeIn();
			//$('#progress .progress-bar').css('width', progress + '%');
			
			if(progressval < 99)
		    {
		    	$('.blockMsg .progressorpanel .lbl').text('Uploading...');
		   		$('.blockMsg .progressorpanel .progressor').text( floorFigure(data.loaded/data.total*100,0).toString()+"%" );
		    }
		    else
		    {
		    	$('.blockMsg .progressorpanel .lbl').text('Validating...');
		    	$('.blockMsg .progressorpanel .progressor').text('');
		    }
		}
	}).on('fileuploadadd', function(e, data) {
		$('#qrdavalidate_btn').unbind("click");
		$('#qrdauploadfiles').empty();
		data.context = $('<div/>').appendTo('#qrdauploadfiles');
		$.each(data.files, function(index, file) {

			var node = $('<p/>').append($('<span/>').text(file.name));

			node.appendTo(data.context);
		});
		
		

		data.context = $('#qrdavalidate_btn').click(function(e) {
			var jform = $('#QRDAValidationForm');
			jform.validationEngine({promptPosition:"centerRight", validateNonVisibleFields: true, updatePromptsPosition:true});
			if(jform.validationEngine('validate'))
			{
				$('#QRDAValidationForm .formError').hide(0);
				
				BlockPortletUI('#qrdaWidget .well');
						
				data.submit();
			}
			else
			{
				//jform.validationEngine({validateNonVisibleFields: true, updatePromptsPosition:true});
				$('#QRDAValidationForm .formError').show(0);
				$('#QRDAValidationForm .qrdauploadfileformError').prependTo('#qrdauploaderrorlock');
			}
		});
	}).prop('disabled', !$.support.fileInput).parent().addClass(
			$.support.fileInput ? undefined : 'disabled');

	$('#qrdauploadfile').bind('fileuploaddrop', function(e, data) {
		e.preventDefault();
	}).bind('fileuploaddragover', function(e) {
		e.preventDefault();
	});
	
	
	
	$('#qrdauploadfile-btn').bind('click', function(e, data)
			{
				var dropdownvalue = $('#category').val();
				$('#QRDAValidationForm .formError').hide(0);
				$('#QRDAValidationForm').trigger('reset');
				$('#qrdavalidate_btn').unbind("click");
				
				$('#qrdauploadfiles').empty();
				
				$('#category').val(dropdownvalue);
				
			});
	
});