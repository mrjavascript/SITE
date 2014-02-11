//The serialization function to transform the form into the FormData (which is not supported by IE9)
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

function errorHandler (request, status, error) {
    alert("error:"+ error);
}

//block the portlet panel, showing the progress of uploading.
//overlay the 'validationpanel' on the module content.
function BlockPortletUI(formID)
{
	//find the module content.
	window.validationpanel = $('#' + formID);
	
	//get the loading image.
	var ajaximgpath = window.currentContextPath + "/images/ajax-loader.gif";
	
	//block the module panel, displaying the loading image.
	//padding: '15px', 
	window.validationpanel.block({ 
		css: { 
	            border: 'none', 
	            backgroundColor: '#000', 
	            '-webkit-border-radius': '10px', 
	            '-moz-border-radius': '10px', 
	            padding: '15px', 
	            opacity: .5, 
	            color: '#fff' 
		},
		message: '<div class="progresspanel"><img src="'+ ajaximgpath + '" alt="loading">'+
				 '<div class="lbl">Uploading...</div>' +
				 '<div class="progress">0%</div></div>',
	});
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}

function floorFigure(figure, decimals){
    if (!decimals) decimals = 2;
    var d = Math.pow(10,decimals);
    return (parseInt(figure*d)/d).toFixed(decimals);
};

function progressHandlingFunction(e){
    if(e.lengthComputable){
    	var progressval = floorFigure(e.loaded/e.total*100,0);
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
    }
}




function qrdaValidationHandler()
{
	var ajaximgpath = window.currentContextPath + "/images/ajax-loader.gif";
	
	BlockPortletUI('schematronValidationPanel');
	
	var formData = $('#QRDAValidationForm').serializefiles();
	//formData.attr('modelAttribute','uploadedFile');
	var serviceUrl = $('#QRDAValidationForm').attr("action");
	
	$.ajax({
        url: serviceUrl,
        type: 'POST',
        //show the progress of file uploading.
        xhr: function() {  // custom xhr
            myXhr = $.ajaxSettings.xhr();
            if(myXhr.upload){ // check if upload property exists
            	// for handling the progress of the upload
                myXhr.upload.addEventListener('progress',progressHandlingFunction, false); 
            }
            return myXhr;
        },
        
        success: function(data){
        	var result = JSON.parse(data);
        	qrdaAjaxValidationResultHandler(result);
        }, 
        error: errorHandler,
        // Form data
        data: formData,
        //Options to tell JQuery not to process data or worry about content-type
        cache: false,
        contentType: false,
        processData: false
    });
}	

//parse the response data model.
function qrdaAjaxValidationResultHandler(result)
{
	var html = [];
	if(result.success)
	{
		var rowtmp;
		var rowcache;
		var resultMsg = '';
		var failed = false;
		
		rowtmp = '<b>Upload Results:</b><br/>'+
				 'The file: {filename} was uploaded successfully.' +
				 '<br/><b>QRDA category selected: {category}</b><hr/><hr/><b>Validation Results</b><br/>'+
				 '<font style="color:{color}"><b><i>{result}</i></b><br/>{resultmessage}</font><hr/>';
		
		
		//schema error.
		if('schemaErrors' in result && result.schemaErrors.length>0)
		{
			failed = true;
			resultMsg += "The file has encountered schema errors.";
		}
		
		if('schematronWarnings' in result && result.schematronWarnings.length>0)
		{
			failed = true;
			resultMsg += "The file has encountered schema-tron warnings.";
		}
		
		if('schematronErrors' in result && result.schematronErrors.length>0)
		{
			failed = true;
			resultMsg += "The file has encountered schema-tron errors.";
		}
		
		rowtmp = rowtmp.replace(/{filename}/g, result.orgFileName);
		rowtmp = rowtmp.replace(/{category}/g, result.selectedCategory);
		rowtmp = rowtmp.replace(/{color}/g, failed?'red':'green');
		rowtmp = rowtmp.replace(/{result}/g, failed?'Validation Failed.':'Validation Succeeded.');
		rowtmp = rowtmp.replace(/{resultmessage}/g, resultMsg);
		html.push(rowtmp);
		
		//schema error.
		if('schemaErrors' in result && result.schemaErrors.length>0)
		{
			html.push("<font color='red'>Schema Errors Received:<hr/>");
			
			rowtmp = 'Error {i}:<br/>{msg}<br/> ';
			
			$.each(result.schemaErrors, function(i ,rslt) {
				//look up the label
				rowcache = rowtmp;
				rowcache = rowcache.replace(/{i}/g, i + 1 );
				rowcache = rowcache.replace(/{msg}/g, rslt.errorMessage);
				html.push(rowcache);
	        });
			
			html.push('</font>');
		}
		
		if('schematronErrors' in result && result.schematronErrors.length>0)
		{
			html.push("<font color='red'>Schematron Errors Received:<hr/>");
			
			rowtmp = "Error {i}:<br/><div class='nav' navKey='{key}'><u><b>{msg}</b></u></div>(xpath:{xpath})<br/><br/> ";
			
			$.each(result.schematronErrors, function(i ,rslt) {
				//look up the label
				var rowcache = rowtmp;
				rowcache = rowcache.replace(/{i}/g, i + 1);
				rowcache = rowcache.replace(/{msg}/g, rslt.message);
				rowcache = rowcache.replace(/{key}/g, rslt.navKey);
				rowcache = rowcache.replace(/{xpath}/g, rslt.xpath);
				html.push(rowcache);
	        });
			
			html.push('</font>');
		}
		
		if('schematronWarnings' in result && result.schematronWarnings.length>0)
		{
			html.push("<font color='blue'>Schematron Warnings Received:<hr/>");
			
			rowtmp = "Warning {i}:<br/><div class='nav' navKey='{key}'><u><b>{msg}</b></u></div>(xpath:{xpath})<br/><br/>";
			
			$.each(result.schematronWarnings, function(i ,rslt) {
				//look up the label
				var rowcache = rowtmp;
				rowcache = rowcache.replace(/{i}/g, i + 1);
				rowcache = rowcache.replace(/{msg}/g, rslt.message);
				rowcache = rowcache.replace(/{key}/g, rslt.navKey);
				rowcache = rowcache.replace(/{xpath}/g, rslt.xpath);
				html.push(rowcache);
	        });
			
			html.push('</font>');
		}
		
		$("#ValidationResult #tabs #tabs-1 p" ).html(html.join(""));
		
		//make the links click, if the errors have been found.
		$("#ValidationResult #tabs-1 .nav").unbind().click(function(e){
			e.preventDefault();
			//switch to tab2
			$( "#ValidationResult [href='#tabs-2']").trigger( "click" );
			var parentTag = $("#ValidationResult");
			var anchorID = $(this).attr('navKey');
			var target = parentTag.find("#tabs-2 span[class='tag'] span[class='value']:contains('" + anchorID +"')").filter(function(){
				return $(this).text() == "\"" + anchorID + "\"";
			});
			target.parent().css("background-color", "yellow");
			parentTag.stop().scrollTo( target.parent() , 800 , {offset: {top: -50, left:-50} });
			
			//set this as last clicked link
			window.lastclickederror = $(this);
		});
		
	}
	else
	{
		html.push('<h2>Validation failed due to server errors:</h2><div style="color: red;font-weight: bold">');
		html.push(result.errorMessage);
		html.push('</div>');
		$( "#ValidationResult #tabs #tabs-1 p" ).html(html.join(""));
	}
	
	//post the or original on the second tab.
	$("#ValidationResult #tabs #tabs-2 p" ).html('<pre class="xml">' + result.orgXml + '</pre>');
	
	//high light the xml.
	$("#ValidationResult #tabs #tabs-2 p pre").each(function (i, e) {
	    hljs.highlightBlock(e);
	});
	
	//pop the dialog box
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
	    	"Close Result": function() {
	          $( this ).dialog( "close" );
	       }
	      }
	});
	
	//resize the navigation bar.
	var newWidth = $('.ui-tabs-nav').parent().width();
	$('.ui-tabs-nav').width(newWidth);
	
	//unblock the panel.
	if(typeof window.validationpanel != 'undefined')
	{
		window.validationpanel.unblock();
	}
}

//jquery init code.
$(function(){
	
	//tabify the validation result dialog box
	$( "#ValidationResult #tabs" ).tabs({
		activate: function(event, ui) {
			var tabidx = ui.newTab.index();
			if(tabidx == 0)
	        {
	        	if(window.lastclickederror != 'undefined')
        		{
	        		//scroll to the target.
	        		var parentTag = $("#ValidationResult");
	        		parentTag.stop().scrollTo( window.lastclickederror , 500 , {offset: {top: -50, left:-50} });
        		}
	        }
	    }
	});
	
	
	
	//if javascript is enabled, then display the ajaxifide page,
	$(".scriptenabled").removeClass('scriptenabled');
	//and remove the noscript dom.
	$("noscript").remove();
	
	$("#category").selectable({
		selected : function(event,ui)
		{
			var _selectedValue = $(ui.selected).attr('value');
			//set the hidden value.
			$("input[name='category']").val(_selectedValue);
		}
	});
	
	//make the upload box pretty
	$("#qrdauploadfile").filestyle({ 			    
		image: window.currentContextPath + "/images/uploadqrdabutton.png",
		imageheight : 24,
		imagewidth : 110,
		width : 220,
		validationclass: "validate[required]"
	});
	
	//if the formdata is supported by the borwser then fall back to the post back.
	if( window.FormData !== undefined ){
		$("#qrdavalidate_btn").button({ 
			icons: {primary: "ui-icon-check"  } 
		}).click(function(e){
			e.preventDefault();
			//validate the form;
			var jform = $('#QRDAValidationForm');
			jform.validationEngine({validateNonVisibleFields: true, updatePromptsPosition:true});
			jform.validationEngine('hideAll');
			if(jform.validationEngine('validate'))
			{	
				qrdaValidationHandler();
			}
			else
			{ 
				return false;
			}
		});
	}else
	//to support old browser and IE version below 10.
	{
		//alert("form data not supported");
		$("#QRDAValidationForm").attr("action",$("#QRDAValidationForm").attr("post"));
	}
	
	var validationResultJson = null;
	//see if the response coming from post back.
	try{
		validationResultJson = JSON.parse($('#postbackValidationResult').html());
	}
	catch(err){}
	
	if(validationResultJson!==undefined && validationResultJson!== null)
	{
		//pop the dialogbox.
		qrdaAjaxValidationResultHandler(window.validationResultJson);
	}
});