(function($) {
$.fn.serializefiles = function() {
    var obj = $(this);
    /* ADD FILE TO PARAM AJAX */
    var formData = new FormData();
    $.each($(obj).find("input[type='file']"), function(i, tag) {
        $.each($(tag)[0].files, function(i, file) {
            formData.append(tag.name, file);
        });
    });
    var params = $(obj).serializeArray();
    $.each(params, function (i, val) {
        formData.append(val.name, val.value);
    });
    return formData;
};
})(jQuery);

function progressHandlingFunction(e){
    if(e.lengthComputable){
    	//var progressval = floorFigure(e.loaded/e.total*100,0);
    	/*
    	if(progressval < 99)
    	{
    		$('.blockMsg .progresspanel .lbl').text('Uploading...');
    		$('.blockMsg .progresspanel .progress').text( floorFigure(e.loaded/e.total*100,0).toString()+"%" );
    	}
    	else
    	{
    		$('.blockMsg .progresspanel .lbl').text('Validating...');
    		$('.blockMsg .progresspanel .progress').text('');
    	}
    	*/
    }
}

function blockAnchorUploadWidget()
{
	var ajaximgpath = window.currentContextPath + "/images/ajax-loader.gif";
	window.anchorUploadWidget = $('#anchoruploadwidget');
	window.anchorUploadWidget.block({ 
		css: { 
	            border: 'none', 
	            padding: '15px', 
	            backgroundColor: '#000', 
	            '-webkit-border-radius': '10px', 
	            '-moz-border-radius': '10px', 
	            opacity: .5, 
	            color: '#fff' 
		},
		message: '<div class="progresspanel">' +
				 '<img src="'+ ajaximgpath + '" alt="loading">'+
				 '<div class="lbl">Uploading the certificate...</div></div>',
	});
}

function unblockAnchorUploadWidget()
{
	if(window.anchorUploadWidget)
    	window.anchorUploadWidget.unblock();
}

function blockDirectReceiveWidget()
{
	var ajaximgpath = window.currentContextPath + "/images/ajax-loader.gif";
	window.directReceiveWdgt = $('#directreceivewidget');
	window.directReceiveWdgt.block({ 
		css: { 
	            border: 'none', 
	            padding: '15px', 
	            backgroundColor: '#000', 
	            '-webkit-border-radius': '10px', 
	            '-moz-border-radius': '10px', 
	            opacity: .5, 
	            color: '#fff' 
		},
		message: '<div class="progresspanel">' +
				 '<img src="'+ ajaximgpath + '" alt="loading">'+
				 '<div class="lbl">Contacting SMTP Server...</div></div>',
	});
}

function unblockDirectReceiveWidget()
{
	if(window.directReceiveWdgt)
    	window.directReceiveWdgt.unblock();
}

function precannedRequired(field, rules, i, options){
	var uploadccdatype = $("#directreceiveform").find("input[name=filetype]:checked").val();
	if(uploadccdatype == 'precanned' && field.val()== '')
	{
		return "Please select a precanned C-CDA sample";
	}
}

function customccdaRequired(field, rules, i, options){
	var uploadccdatype = $("#directreceiveform").find("input[name=filetype]:checked").val();
	if(uploadccdatype == 'custom' && field.val()== '')
	{
		return "Please upload your C-CDA sample";
	}
}

$(function() {
	
	$(".module_content #anchoruploadfile").filestyle({ 
		image: window.currentContextPath + "/images/uploadcertificate.png",
		imageheight : 24,
		imagewidth : 130,
		width : 220,
		isdisabled: false,
		validationclass: "validate[required,custom[derencncodedfileextension]]"
	});
	
	$('.module_content #uploadccdainput').filestyle({ 
		image: window.currentContextPath + "/images/button_upload.png",
		imageheight : 24,
		imagewidth : 115,
		width : 250,
		isdisabled: true,
		validationclass: "validate[funcCall[customccdaRequired]]"
	});
	
	
	$("#ccdafiletreepanel").jstree({
		 "json_data" : {
			      "ajax" : {
				      "url" : window.serviceContextPath +"/GetCCDASamplesTree",
				      "type" : "get",
				      /*"data" : function (n) {
				    	 return { id : n.attr ? n.attr("id") : 0 };
				      }*/
				  }
	      },
	      
	      "types" : {
	    	  "valid_children" : [ "all" ],
	    	  "type_attr" : "ref",
	    	  "types" : {
	    		  "root" : {
		    	      "icon" : {
		    	    	  "image" : window.currentContextPath + "/images/root.png"
		    	      },
		    	      "valid_children" : [ "file","folder" ],
		    	      "max_depth" : 2,
		    	      "hover_node" : false,
		    	      "select_node" : function (e) {

		    	    	  this.toggle_node(e);
		    	    	  return false;
		    	      }
		    	      
		    	  	},
		    	  "file" : {
		    		  "icon" : {
		    	    	  "image" : window.currentContextPath + "/images/file.png"
		    	      },
		    		  "valid_children" : [ "none" ],
		    		  "select_node" : function (node,e) {
		    			  //populate the textbox
		    			  $("#directreceiveform input[name='precannedfilepath']").val(node.data("serverpath"));
		    	    	  //hide the drop down panel
		    			  $('#precannedccdaselectbutton').dropdown('hide');
		    			  //hide all the errors
		    			  $('#directreceivesubmit').validationEngine('hideAll');
		    			  
		    		  }
		    	  },
		    	  "folder" : {
		    		  "icon" : {
		    	    	  "image" : window.currentContextPath + "/images/folder.png"
		    	      },
		    		  "valid_children" : [ "file" ],
		    		  "select_node" : function (e) {
		    	    	  this.toggle_node(e);
		    	    	  return false;
		    	      }
		    	  }
	    	 }
	    },
	    "plugins" : [ "themes", "json_data", "ui", "types" ]
	}).bind('loaded.jstree', function(e, data) {
		isfiletreeloaded = true;
	});
	
	$('#directreceiveform input[name="filetype"]').change(function(){
		var $checked = $('#directreceiveform input[name="filetype"]:checked');
		$("#directreceiveform .ccdainputfile").attr('disabled','disabled');
		$checked.closest('tr').find("td input").removeAttr('disabled');
    });
	
	$('#precannedccdaselectbutton').dropdown({width:'400px', maxHeight:'500px'}); 
	
	
	$("#directreceivesubmit").click(function(e){
	    
		var jform = $('#directreceiveform');
		jform.validationEngine('hideAll');
		if(jform.validationEngine('validate'))
		{
			//block ui..
			blockDirectReceiveWidget();
			
			var formData = $('#directreceiveform').serializefiles();
		    
		    $.ajax({
		        url: $('#directreceiveform').attr('action'),
		        
		        type: 'POST',
		        
		        xhr: function() {  // custom xhr
		            myXhr = $.ajaxSettings.xhr();
		            if(myXhr.upload){ // check if upload property exists
		                myXhr.upload.addEventListener('progress', progressHandlingFunction, false); // for handling the progress of the upload
		            }
		            return myXhr;
		        },
		        
		        success: function(data){
		        	var results = JSON.parse(data);
		        	var iconurl = results.IsSuccess? window.currentContextPath + "/images/icn_alert_success.png" :
		        									window.currentContextPath + "/images/icn_alert_error.png" ;
		        	$('#directreceivewidget .blockMsg .progresspanel img').attr('src',iconurl);
		        	$('#directreceivewidget .blockMsg .progresspanel .lbl').text(results.ErrorMessage);
		        	setTimeout(function(){
		        		unblockDirectReceiveWidget();
		        	},1800);
		        },
		        
		        error: function (request, status, error) {
		        	alert("ajax error:"+ error);
		        },
		        // Form data
		        data: formData,
		        //Options to tell JQuery not to process data or worry about content-type
		        cache: false,
		        contentType: false,
		        processData: false
		    });
		}
		return false;
	});
	
	
$("#anchorsubmit").click(function(e){
	    
		var jform = $('#anchoruploadform');
		jform.validationEngine('hideAll');
		if(jform.validationEngine('validate'))
		{
			//block ui..
			blockAnchorUploadWidget();
			
			var formData = $('#anchoruploadform').serializefiles();
		    
		    $.ajax({
		        url: $('#anchoruploadform').attr('action'),
		        
		        type: 'POST',
		        
		        xhr: function() {  // custom xhr
		            myXhr = $.ajaxSettings.xhr();
		            if(myXhr.upload){ // check if upload property exists
		                myXhr.upload.addEventListener('progress', progressHandlingFunction, false); // for handling the progress of the upload
		            }
		            return myXhr;
		        },
		        
		        success: function(data){
		        	var results = JSON.parse(data);
		        	
		        	var iconurl = results.IsSuccess? window.currentContextPath + "/images/icn_alert_success.png" :
		        									window.currentContextPath + "/images/icn_alert_error.png" ;
		        	
		        	$('#anchoruploadwidget .blockMsg .progresspanel img').attr('src',iconurl);
		        	
		        	$('#anchoruploadwidget .blockMsg .progresspanel .lbl').text(results.ErrorMessage);
		        	
		        	if(window.anchorUploadWidget)
		        	{
		        		window.anchorUploadWidget.unbind("click");
		        		window.anchorUploadWidget.click(function() { 
		        			window.anchorUploadWidget.unblock(); 
			                $('#anchoruploadwidget .blockOverlay').attr('title','Click to hide this message.').click($.unblockUI); 
			            });
		        		setTimeout(function(){
		        			window.anchorUploadWidget.unblock();
		        		},10000);
		        	}
		        },
		        
		        error: function (request, status, error) {
		        	alert("ajax error:"+ error);
		        },
		        // Form data
		        data: formData,
		        //Options to tell JQuery not to process data or worry about content-type
		        cache: false,
		        contentType: false,
		        processData: false
		    });
		}
	    return false;
	});
});