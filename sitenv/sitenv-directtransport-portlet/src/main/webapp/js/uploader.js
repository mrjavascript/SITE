$(function() {
	'use strict';

	// Change this to the location of your server-side upload handler:
	$('#anchoruploadprogress').hide();
	$('#anchoruploadfile').fileupload({
		url : $( '#anchoruploadform' ).attr( 'action' ),
		dataType : 'json',
		autoUpload : false,
		type : 'POST',
		contenttype : false,
		replaceFileInput : false,
		done : function(e, data) {
			$.each(data.result.files, function(index, file) {
				$('#anchoruploadfiles').empty();
				$('#anchoruploadfiles').text(file.name);
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

			window.setTimeout(function() {
				$('#anchoruploadprogress').fadeOut(400, function() {
					$('#anchoruploadprogress .progress-bar').css('width', '0%');
					
				});

			}, 1000);
		}
	}).on('fileuploadadd', function(e, data) {
		$('#anchoruploadsubmit').unbind("click");
		$('#anchoruploadfiles').empty();
		data.context = $('<div/>').appendTo('#anchoruploadfiles');
		$.each(data.files, function(index, file) {

			var node = $('<p/>').append($('<span/>').text(file.name));

			node.appendTo(data.context);
		});

		data.context = $('#anchoruploadsubmit').click(function(e) {
			var jform = $('#anchoruploadform');
			jform.validationEngine('hideAll');
			if(jform.validationEngine('validate'))
			{
				
				
				blockAnchorUploadWidget();
						
				data.submit();
			}
			else
			{
				//jform.validationEngine({validateNonVisibleFields: true, updatePromptsPosition:true});
				
				$('#anchoruploadform .anchoruploadfileformError').prependTo('#anchoruploaderrorlock');
			}
		});
	}).prop('disabled', !$.support.fileInput).parent().addClass(
			$.support.fileInput ? undefined : 'disabled');

	$('#fileupload').bind('fileuploaddrop', function(e, data) {
		e.preventDefault();
	}).bind('fileuploaddragover', function(e) {
		e.preventDefault();
	});
	
	
	
	// Change this to the location of your server-side upload handler:
	$('#ccdauploadprogress').hide();
	$('#ccdauploadfile').fileupload({
		url : $( '#ccdauploadform' ).attr( 'action' ),
		dataType : 'json',
		autoUpload : false,
		type : 'POST',
		contenttype : false,
		replaceFileInput : false,
		error: function (e, data) {
			var iconurl = window.currentContextPath + "/images/icn_alert_error.png" ;
			
			$('#directreceivewidget .blockMsg .progressorpanel img').attr('src',iconurl);
        	
        	$('#directreceivewidget .blockMsg .progressorpanel .lbl').text('Error uploading file.');
			
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
				$('#ccdauploadfiles').empty();
				$('#ccdauploadfiles').text(file.name);
			});
			
			var results = data.result.body;
        	
        	var iconurl = (results.IsSuccess == "true")? window.currentContextPath + "/images/icn_alert_success.png" :
        									window.currentContextPath + "/images/icn_alert_error.png" ;
        	
        	$('#directreceivewidget .blockMsg .progressorpanel img').attr('src',iconurl);
        	
        	$('#directreceivewidget .blockMsg .progressorpanel .lbl').text(results.ErrorMessage);
        	
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

			window.setTimeout(function() {
				$('#ccdauploadprogress').fadeOut(400, function() {
					$('#ccdauploadprogress .progress-bar').css('width', '0%');
					
				});

			}, 1000);
		}
	}).on('fileuploadadd', function(e, data) {
		$('#ccdauploadsubmit').unbind("click");
		$('#ccdauploadfiles').empty();
		data.context = $('<div/>').appendTo('#ccdauploadfiles');
		$.each(data.files, function(index, file) {

			var node = $('<p/>').append($('<span/>').text(file.name));

			node.appendTo(data.context);
		});

		data.context = $('#ccdauploadsubmit').click(function(e) {
			var jform = $('#ccdauploadform');
			jform.validationEngine('hideAll');
			if(jform.validationEngine('validate'))
			{
				
				
				blockDirectReceiveWidget();
						
				data.submit();
			}
			else
			{
				//jform.validationEngine({validateNonVisibleFields: true, updatePromptsPosition:true});
				
				$('#ccdauploadform .ccdauploadfileformError').prependTo('#ccdauploaderrorlock');
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