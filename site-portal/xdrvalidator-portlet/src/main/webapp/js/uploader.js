$(function() {
	'use strict';

	// Change this to the location of your server-side upload handler:
	$('#progress').hide();
	$('#fileupload').fileupload({
		url : url,
		dataType : 'json',
		autoUpload : false,
		type : 'POST',
		contenttype : false,
		replaceFileInput : false,
		error: function (e, data) {
			var iconurl = window.currentContextPath + "/images/icn_alert_error.png" ;
			
			$('.blockMsg .progressorpanel img').attr('src',iconurl);
        	
        	$('.blockMsg .progressorpanel .lbl').text('Error uploading file.');
			
			if(window.validationpanel)
        	{
        		window.validationPanelTimeout = setTimeout(function(){
        				window.anchorUploadWidget.unbind("click");
        				window.anchorUploadWidget.unblock();
        			},10000);
        		
        		
        		window.validationpanel.bind("click", function() { 
        			window.anchorUploadWidget.unbind("click");
        			clearTimeout(window.validationPanelTimeout);
        			window.anchorUploadWidget.unblock(); 
        			window.anchorUploadWidget.attr('title','Click to hide this message.').click($.unblockUI); 
	            });
        		
        	}
        },
		done : function(e, data) {
			$.each(data.result.files, function(index, file) {
				$('#files').empty();
				$('#files').text(file.name);
			});
			
			var results = data.result.body;
			
			var iconurl = (results.IsSuccess == "true")? window.currentContextPath + "/images/icn_alert_success.png" :
				window.currentContextPath + "/images/icn_alert_error.png" ;
			
			$('#anchoruploadwidget .blockMsg .progressorpanel img').attr('src',iconurl);
      
        	
        	$('#anchoruploadwidget .blockMsg .progressorpanel .lbl').text(results.ErrorMessage);
			
			if(window.anchorUploadWidget)
        	{
        		window.anchorUploadTimeout = setTimeout(function(){
        				window.anchorUploadWidget.unbind("click");
        				window.anchorUploadWidget.unblock();
        			},10000);
        		
        		
        		window.anchorUploadWidget.bind("click", function() { 
        			window.anchorUploadWidget.unbind("click");
        			clearTimeout(window.anchorUploadTimeout);
        			window.anchorUploadWidget.unblock(); 
        			window.anchorUploadWidget.attr('title','Click to hide this message.').click($.unblockUI); 
	            });
        		
        	}
			
		    Liferay.Portlet.refresh("#p_p_id_Statistics_WAR_siteportalstatisticsportlet_"); // refresh the counts
		    
		    
			window.setTimeout(function() {
				$('#progress').fadeOut(400, function() {
					$('#progress .progress-bar').css('width', '0%');
					
				});

			}, 1000);
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
		
		$('#formSubmit').unbind("click");
		$('#files').empty();
		data.context = $('<div/>').appendTo('#files');
		$.each(data.files, function(index, file) {

			var node = $('<p/>').append($('<span/>').text(file.name));

			node.appendTo(data.context);
		});

		
		
		data.context = $('#formSubmit').click(function(e) {
			
			var jform = $('#XDRValidationForm');
			jform.validationEngine({promptPosition:"centerRight", validateNonVisibleFields: true, updatePromptsPosition:true});
			//jform.validationEngine('hideAll');
			
			if(jform.validationEngine('validate'))
			{
				$('#XDRValidationForm .formError').hide(0);
				
				BlockPortletUI();
				
				var selectedValue = $("#testCases").val();
				
				data.formData = { };
				
				if (selectedValue != undefined) {
					data.formData.testCases = selectedValue;
				}
				
				data.submit();
				

				window.lastFilesUploaded = data.files;
			}
			else
			{
				$('#XDRValidationForm .formError').show(0);
				
				$('#XDRValidationForm .fileuploadformError').prependTo('#ccdauploaderrorlock');
			}
			
			
			
		});
		
		
		
	}).prop('disabled', !$.support.fileInput).parent().addClass(
			$.support.fileInput ? undefined : 'disabled');

	$('#fileupload').bind('fileuploaddrop', function(e, data) {
		e.preventDefault();
	}).bind('fileuploaddragover', function(e) {
		e.preventDefault();
	});
		
	
	$('#fileupload-btn').bind('click', function(e, data)
	{
		$('#XDRValidationForm .formError').hide(0);
		var urlEndpoint = $('#wsdlLocation').val();
		var selectedText = $("#testCases :selected").text();
		$("#testCases option").each(function() {
			  if($(this).text() == selectedText) {
			    $(this).attr('selected', 'selected');            
			  } else {
				$(this).removeAttr('selected');
			  }                    
			});
		
		$('#XDRValidationForm').trigger('reset');
		$('#wsdlLocation').val(urlEndpoint);
		$('#formSubmit').unbind("click");
		
		$('#files').empty();
		
		
	});
	

});