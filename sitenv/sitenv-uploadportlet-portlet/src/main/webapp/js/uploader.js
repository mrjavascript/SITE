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
			
			if(window.directReceiveWdgt)
        	{
        		window.directReceiveUploadTimeout = setTimeout(function(){
        				window.directReceiveWdgt.unbind("click");
        				window.directReceiveWdgt.unblock();
        			},10000);
        		
        		
        		window.directReceiveWdgt.bind("click", function() { 
        			window.directReceiveWdgt.unbind("click");
        			clearTimeout(window.directReceiveUploadTimeout);
        			window.directReceiveWdgt.unblock(); 
        			window.directReceiveWdgt.attr('title','Click to hide this message.').click($.unblockUI); 
	            });
        		
        	}
        },
		done : function(e, data) {
			$.each(data.result.files, function(index, file) {
				$('#files').empty();
				$('#files').text(file.name);
			});
			
			
			
			$( "#ValidationResult #tabs #tabs-1" ).html(data.result.body);
			
			$( "#ValidationResult" ).dialog({
			      //hide the header bar.
				  open: function() { $(this).closest(".ui-dialog").find(".ui-dialog-titlebar:first").hide(); },
				  resizable: false,
				  draggable: false,
				  height: $(window).height() * 0.9,
				  width: $(window).width() * 0.9,
				  modal: true,
			      autoOpen: true,
			      show: {
			        effect: "blind",
			        duration: 1000
			      },
			      hide: {
			        effect: "blind",
			        duration: 1000
			      },
			      buttons: {
			    	"Smart CCDA Validation": function(){
			    		smartCCDAValidation();
			    	},
			        "Save Results": function() {
			        	$.blockUI({ message: $('#reportSaveAsQuestion'), css: { width: '275px' } }); 
			       },
			        "Close Results": function() {
			          $( this ).dialog( "close" );
			       }
			      }
			});
			
			
			$( "#ValidationResult #tabs" ).tabs();
			//disable smart ccda result tab.
		    $("#ValidationResult #tabs").tabs("disable", "tabs-3");
		    $("#ValidationResult #tabs").tabs("disable", "tabs-2");
			
		    //clean up the links
		    /*$("#ValidationResult #tabs #tabs-1 b:first, #ValidationResult #tabs #tabs-1 a:first").remove();*/
		    $("#ValidationResult #tabs #tabs-1 hr:lt(4)").remove();
		    
			if(typeof window.validationpanel != 'undefined')
				window.validationpanel.unblock();

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
		    	$('.blockMsg .progresspanel .lbl').text('Uploading...');
		   		$('.blockMsg .progresspanel .progressor').text( floorFigure(e.loaded/e.total*100,0).toString()+"%" );
		    }
		    else
		    {
		    	$('.blockMsg .progresspanel .lbl').text('Validating...');
		    	$('.blockMsg .progresspanel .progressor').text('');
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
			
			var jform = $('#CCDAValidationForm');
			jform.validationEngine({validateNonVisibleFields: true, updatePromptsPosition:true});
			jform.validationEngine('hideAll');
			
			if(jform.validationEngine('validate'))
			{
				//switch back to tab1.
				$( "#ValidationResult [href='#tabs-1']").trigger( "click" );
				
				BlockPortletUI();
				
				var selectedValue = $("input:radio[name='ccda_type_val']:checked").val();
				
				data.formData = { };
				
				if (selectedValue != undefined) {
					data.formData.ccda_type_val = selectedValue;
				}
				
				data.submit();
				

				window.lastFilesUploaded = data.files;
			}
			else
			{
				$('#CCDAValidationForm .fileuploadformError').prependTo('#ccdauploaderrorlock');
			}
			
			
			
		});
		
		
		
	}).prop('disabled', !$.support.fileInput).parent().addClass(
			$.support.fileInput ? undefined : 'disabled');

	$('#fileupload').bind('fileuploaddrop', function(e, data) {
		e.preventDefault();
	}).bind('fileuploaddragover', function(e) {
		e.preventDefault();
	});

});