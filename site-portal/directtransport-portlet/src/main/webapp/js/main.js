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

function progressorHandlingFunction(e){
    if(e.lengthComputable){
    	//var progressorval = floorFigure(e.loaded/e.total*100,0);
    	/*
    	if(progressorval < 99)
    	{
    		$('.blockMsg .progressorpanel .lbl').text('Uploading...');
    		$('.blockMsg .progressorpanel .progressor').text( floorFigure(e.loaded/e.total*100,0).toString()+"%" );
    	}
    	else
    	{
    		$('.blockMsg .progressorpanel .lbl').text('Validating...');
    		$('.blockMsg .progressorpanel .progressor').text('');
    	}
    	*/
    }
}

function blockAnchorUploadWidget()
{
	var ajaximgpath = window.currentContextPath + "/images/ajax-loader.gif";
	window.anchorUploadWidget = $('#anchoruploadwidget .well');
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
		message: '<div class="progressorpanel">' +
				 '<img src="'+ ajaximgpath + '" alt="loading">'+
				 '<div class="lbl">Uploading...</div></div>',
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
	window.directReceiveWdgt = $('#directreceivewidget  .well');
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
		message: '<div class="progressorpanel">' +
				 '<img src="'+ ajaximgpath + '" alt="loading">'+
				 '<div class="lbl">Sending...</div></div>',
	});
}

function unblockDirectReceiveWidget()
{
	if(window.directReceiveWdgt)
    	window.directReceiveWdgt.unblock();
}

function precannedRequired(field, rules, i, options){
	if($('#precannedfilepath').val()== '')
	{
		return "Please select a precanned C-CDA sample";
	}
}


$(function() {
	
	$('.dropdown-menu').click(function (e) {
		e.stopPropagation();
	});
	
	
	$('#directMessageType a').click(function (e) {
		  e.preventDefault();
		  $(this).tab('show');

		    $('#precannedCCDAsubmit').validationEngine('hideAll');
		    $('#ccdauploadsubmit').validationEngine('hideAll');
		});
	
	
	$('#precannedemail').bind('change',function(){
	    $('#ccdauploademail').val($(this).val());
	 });
	
	$('#ccdauploademail').bind('change',function(){
	    $('#precannedemail').val($(this).val());
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
				      "url" : sampleCCDATreeURL,
				      "type" : "post",
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
		    		  "deselect_node" : function (node,e) {
		    			  var jform = $('#precannedForm');
		    			  $('#precannedForm .formError').hide(0);
		    				
		    			  
		    			var textValue = $('#precannedemail').val();
		  				$('#precannedForm').trigger('reset');
		  				$('#precannedCCDAsubmit').unbind("click");
		  				
		  				$('#precannedfilePathOutput').empty();
		  				$('#precannedfilepath').val('');
		  				
		  				$('#precannedemail').val(textValue);
		    			  
		    		  },
		    		  "select_node" : function (node,e) {
		    			  var jform = $('#precannedForm');
		    			  //jform.validationEngine('hideAll');
		    			  $('#precannedForm .formError').hide(0);
		    			  //populate the textbox
		    			  $("#precannedfilepath").val(node.data("serverpath"));
		    			  $("#precannedfilePathOutput").text($("#precannedfilepath").val());
		    	    	  //hide the drop down panel
		    			  $('[data-toggle="dropdown"]').parent().removeClass('open');
		    			  //hide all the errors
		    			  //$('#precannedCCDAsubmit').validationEngine('hideAll');
		    			   
		    			  $('#dLabel').focus();
		    			  $('#dLabel').dropdown("toggle");
		    			  
		    			  $("#precannedCCDAsubmit").click(function(e){
		    				    
		    					var jform = $('#precannedForm');
		    					jform.validationEngine({promptPosition:"centerRight", validateNonVisibleFields: true, updatePromptsPosition:true});
		    					if(jform.validationEngine('validate'))
		    					{
		    						$('#precannedForm .formError').hide(0);
		    						
		    						//block ui..
		    						blockDirectReceiveWidget();
		    						
		    						var formData = $('#precannedForm').serializefiles();
		    					    
		    					    $.ajax({
		    					        url: $('#precannedForm').attr('action'),
		    					        
		    					        type: 'POST',
		    					        
		    					        xhr: function() {  // custom xhr
		    					            myXhr = $.ajaxSettings.xhr();
		    					            if(myXhr.upload){ // check if upload property exists
		    					                myXhr.upload.addEventListener('progressor', progressorHandlingFunction, false); // for handling the progressor of the upload
		    					            }
		    					            return myXhr;
		    					        },
		    					        
		    					        success: function(data){
		    					        	var results = JSON.parse(data);
		    					        	
		    					        	
		    					        	var iconurl = (results.body.IsSuccess == "true")? window.currentContextPath + "/images/icn_alert_success.png" :
		    					        									window.currentContextPath + "/images/icn_alert_error.png" ;
		    					        	

		    					        	$('#directreceivewidget .blockMsg .progressorpanel img').attr('src',iconurl);
		    					        	
		    					        	$('#directreceivewidget .blockMsg .progressorpanel .lbl').text(results.body.ErrorMessage);

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
		    					        	
		    					        	Liferay.Portlet.refresh("#p_p_id_Statistics_WAR_siteportalstatisticsportlet_"); // refresh the counts
		    					        	
		    					        },
		    					        
		    					        error: function (request, status, error) {
		    					        	var iconurl = window.currentContextPath + "/images/icn_alert_error.png" ;
		    								
		    								$('#directreceivewidget .blockMsg .progressorpanel img').attr('src',iconurl);
		    					        	
		    					        	$('#directreceivewidget .blockMsg .progressorpanel .lbl').text('Error sending sample C-CDA file.');
		    								
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
		    					        // Form data
		    					        data: formData,
		    					        //Options to tell JQuery not to process data or worry about content-type
		    					        cache: false,
		    					        contentType: false,
		    					        processData: false
		    					    });
		    					}
		    					else
		    					{
		    						$('#precannedForm .formError').show(0);
		    						
		    						$('#precannedform .precannedfilepathformError').prependTo('#precannederrorlock');
		    					}
		    					return false;
		    				});
		    			  
		    		  }
		    	  },
		    	  "folder" : {
		    		  "icon" : {
		    	    	  "image" : window.currentContextPath + "/images/folder.png"
		    	      },
		    		  "valid_children" : [ "file" ],
		    		  "select_node" : function (e) {
		    	    	  e.find('a:first').focus();
		    			  this.toggle_node(e);
		    	    	  return false;
		    	      }
		    	  }
	    	 }
	    },
	    "plugins" : [ "themes", "json_data", "ui", "types" ]
	}).bind('loaded.jstree', function(e, data) {
		isfiletreeloaded = true;
		
		$('#ccdafiletreepanel').find('a').each(function() {
		    $(this).attr('tabindex', '1');
		});
	});
	
	
	
	
	

});

/*
 * 	Parsley Validation
 */
$("button#precannedCCDAsubmit, button#ccdauploadsubmit").click(
		function()
		{
			var $form = $(this).closest('form');
			if (! $form.parsley().validate())
			{
				return false;
			}
		}
);


/*
 * 	Get Direct Certificate Utility
 */
var formTestcasesHosting;
var testcaseHostingResults;
var testcaseHostingResultsAccordion;
$(function() {

	//Create the accordian
	formTestcasesHosting = $("form[name=\"form-getdc\"]");
	testcaseHostingResults = $("div#testcase-results", formTestcasesHosting);
	testcaseHostingResultsAccordion = $("div#testcase-results-accordion", testcaseHostingResults);
    testcaseHostingResultsAccordion.accordion({
        "collapsible": true,
        "heightStyle": "content",
        "icons": {
            "activeHeader": "",
            "header": ""
        }
    });
    testcaseHostingResultsAccordion.empty();

	/*
	 * Submit the form
	 */
	$('form#form-getdc').parsley().subscribe('parsley:form:validate', function (formInstance) {

		if (formInstance.isValid())
		{
			// Show the div
	    	$("#testcase-results-accordion").removeClass("hide");
	    	$("#testcase-results-accordion").attr("aria-hidden", "false");
			
			// Submit the form
			$.post(URL_GETDC_ACTION, {
				directAddress : $("#directAddress").val()
			}, function(data, status) {
				appendAccordian(data, $("#directAddress").val());
			}, 'json');
			
			return;
		}

		// else stop form submission
	    formInstance.submitEvent.preventDefault();
		
		return;

	});

	/*
	 * Reset parsley
	 */
	$("button#getdc-reset").click(function() {
		$('form#form-getdc').parsley().reset();
		
		// And empty the accordian
		testcaseHostingResultsAccordion.empty();
	});

});

function appendAccordian(data, value)
{
	var testcaseHostingResultHeaderElem = $("<h3/>");
	testcaseHostingResultHeaderElem.enableClass("testcase-hosting-result-header");
	var testcaseHostingResultBodyElem = $("<div/>");
	if (data.error !== undefined)
	{
		testcaseHostingResultHeaderElem.enableClass(("testcase-hosting-result-header-error"));
		testcaseHostingResultHeaderElem.append(buildTestcaseItem("Value", value));
		testcaseHostingResultBodyElem.append(buildTestcaseItem("Error", data.error));
	}
	else 
	{
		if (data.is_found !== undefined && data.is_found === true)
		{
			testcaseHostingResultHeaderElem.enableClass(("testcase-hosting-result-header-success"));
		}
		else
		{
			testcaseHostingResultHeaderElem.enableClass(("testcase-hosting-result-header-error"));
		}
		testcaseHostingResultHeaderElem.append(buildTestcaseItem("Value", value));
		
		// LDAP
		if (data.ldap !== undefined)
		{
			var ldapFound = (data.ldap.is_found !== undefined && data.ldap.is_found === true)
				? "LDAP Found" : "LDAP Not Found";
			testcaseHostingResultBodyElem.append(buildTestcaseItem(ldapFound, data.ldap.message));
		}
		
		// DNS
		if (data.dns !== undefined)
		{
			var dnsFound = (data.dns.is_found !== undefined && data.dns.is_found === true)
				? "DNS Found" : "DNS Not Found";
			testcaseHostingResultBodyElem.append(buildTestcaseItem(dnsFound, data.dns.message));
		}
	}

	testcaseHostingResultsAccordion.append(testcaseHostingResultHeaderElem);
	testcaseHostingResultsAccordion.append(testcaseHostingResultBodyElem);
	refreshAccordian();
}

function refreshAccordian()
{
	testcaseHostingResultsAccordion.accordion("refresh");
	testcaseHostingResultsAccordion.accordion({"active" : -1});
	$("h3.testcase-hosting-result-header", testcaseHostingResultsAccordion).each(
		function() {
			var testcaseHostingResultHeaderElem = $(this);

			var testcaseHostingResultHeaderIcon = $(
					"span.ui-accordion-header-icon",
					testcaseHostingResultHeaderElem);
			testcaseHostingResultHeaderIcon
					.disableClass("ui-icon");
			testcaseHostingResultHeaderIcon
					.enableClass("glyphicon");

			if (testcaseHostingResultHeaderElem
					.hasClass("testcase-hosting-result-header-success")) {
				testcaseHostingResultHeaderElem.addClass("panel-success");
				
				testcaseHostingResultHeaderIcon
						.enableClass("glyphicon-ok");
				testcaseHostingResultHeaderIcon
						.enableClass("glyphicon-type-success");
			} else {
				testcaseHostingResultHeaderElem.addClass("panel-danger");
				
				testcaseHostingResultHeaderIcon
						.enableClass("glyphicon-remove");
				testcaseHostingResultHeaderIcon
						.enableClass("glyphicon-type-error");
			}
		});	
	
	// Show the div
	$("#testcase-results").removeClass("hide");
	$("#testcase-results").attr("aria-hidden", "false");
}

function buildTestcaseItem(testcaseItemLbl, testcaseItemValues) {	
    var testcaseItemElem = $("<div/>"), testcaseItemLblElem = $("<span/>");
    testcaseItemLblElem.append($("<strong/>").text(testcaseItemLbl), ": ");
    testcaseItemElem.append(testcaseItemLblElem);
    
    if (!$.isBoolean(testcaseItemValues) && !$.isNumeric(testcaseItemValues) && (!testcaseItemValues || $.isEmptyObject(testcaseItemValues))) {
        testcaseItemLblElem.append($("<i/>").text("None"));
    } else if ($.isArray(testcaseItemValues)) {
        var testcaseItemValuesList = $("<ul/>");
        
        testcaseItemValues.forEach(function (testcaseItemValue) {
            testcaseItemValuesList.append($("<li/>").append(testcaseItemValue));
        });
        
        testcaseItemElem.append(testcaseItemValuesList);
    } else {
        testcaseItemLblElem.append(($.isBoolean(testcaseItemValues) || $.isNumeric(testcaseItemValues)) ? testcaseItemValues.toString()
            : testcaseItemValues);
    }

    return testcaseItemElem;
}



