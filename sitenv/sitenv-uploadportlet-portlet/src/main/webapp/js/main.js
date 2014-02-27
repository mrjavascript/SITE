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

function fileSelected()
{
	$(".fakefile input").val($('#ccdafileChooser').val());
}

function BlockPortletUI()
{
	window.validationpanel = $('#CCDAValidationForm').closest('div[class="well"]');
	
	var ajaximgpath = window.currentContextPath + "/css/ajax-loader.gif";
	
	window.validationpanel.block({ 
		css: { 
	            border: 'none', 
	            padding: '15px', 
	            backgroundColor: '#000', 
	            '-webkit-border-radius': '10px', 
	            '-moz-border-radius': '10px', 
	            opacity: .5, 
	            color: '#fff',
	            width: '90%' 
		},
		message: '<div class="progressorpanel"><img src="'+ ajaximgpath + '" alt="loading">'+
				 '<div class="lbl">Uploading...</div>' +
				 '<div class="progressor">0%</div></div>',
	});
}

function BlockResultUI()
{
	window.resultPanel = $('#ValidationResult').closest('div[class="ui-dialog"]');
	
	var ajaximgpath = window.currentContextPath + "/css/ajax-loader.gif";
	
	window.resultPanel.block({ 
		css: { 
	            border: 'none', 
	            padding: '15px', 
	            backgroundColor: '#000', 
	            '-webkit-border-radius': '10px', 
	            '-moz-border-radius': '10px', 
	            opacity: .5, 
	            color: '#fff',
	            width: '90%'
		},
		message: '<div class="progressorpanel"><img src="'+ ajaximgpath + '" alt="loading">'+
				 '<div class="lbl">Uploading...</div>' +
				 '<div class="progressor">0%</div></div>',
	});
}



function errorHandler (request, status, error) {
    alert("error:"+ error);
    if(window.validationpanel)
    	window.validationpanel.unblock();
    $.unblockUI();
}

function progressHandlingFunction(e){
    if(e.lengthComputable){
    	var progressval = floorFigure(e.loaded/e.total*100,0);
    	if(progressval < 99)
    	{
    		$('.blockMsg .progressorpanel .lbl').text('Uploading...');
    		$('.blockMsg .progressorpanel .progressor').text( floorFigure(e.loaded/e.total*100,0).toString()+"%" );
    	}
    	else
    	{
    		$('.blockMsg .progressorpanel .lbl').text('Validating...');
    		$('.blockMsg .progressorpanel .progressor').text('');
    	}
    }
}

function floorFigure(figure, decimals){
    if (!decimals) decimals = 2;
    var d = Math.pow(10,decimals);
    return (parseInt(figure*d)/d).toFixed(decimals);
};

function smartCCDAValidation()
{
	var ajaximgpath = window.currentContextPath + "/css/ajax-loader.gif";
	
	$.blockUI({ 
		css: { 
	        border: 'none', 
	        padding: '15px', 
	        backgroundColor: '#000', 
	        '-webkit-border-radius': '10px', 
	        '-moz-border-radius': '10px', 
	        opacity: .5, 
	        color: '#fff' 
    	},
    	message: '<div class="progressorpanel"><img src="'+ ajaximgpath + '" alt="loading">'+
		          '<div class="lbl">Validating...</div></div>'
		
	});
	
	var formData = $('#CCDAValidationForm').serializefiles();
	var serviceUrl = $('#CCDAValidationForm').attr("relay");
	$.ajax({
        url: serviceUrl,
        type: 'POST',
        
        success: function(data){
        	var results = JSON.parse(data);
        	if(results.IsSuccess)
        	{
        		try{
	        		var tablehtml = [];
	        		var rubricLookup = results.RubricLookup;
	        		var rowtmp = '<tr><td>{label}</td><td>{score}</td><td>{scoreexplain}</td><td>{detail}</td></tr>';
	        		
	        		tablehtml.push('<table class="bordered">');
	        		tablehtml.push('<colgroup>');
	        		tablehtml.push('<col span="1" style="width: 15%;">');
	        		tablehtml.push('<col span="1" style="width: 50px;">');
	        		tablehtml.push('<col span="1" style="width: 15%;">');
	        		tablehtml.push('<col span="1" style="width: 67%;">');
	        		tablehtml.push('</colgroup>');
	        		
	        		tablehtml.push('<thead><tr>');
	        		tablehtml.push('<th>Rubric</th>');
	        		tablehtml.push('<th>Score</th>');
	        		tablehtml.push('<th>Comment</th>');
	        		tablehtml.push('<th>Details</th>');
	        		tablehtml.push('</tr></thead>');
	        		
	        		tablehtml.push('<tbody>');
	
	        		$.each(results.Results, function(i, result) {
	        			//look up the label
	        			var rowcache = rowtmp;
	        			var label = rubricLookup[result.rubric].description;
	        			rowcache = rowcache.replace(/{label}/g, label?label:'N/A');
	        			rowcache = rowcache.replace(/{score}/g, result.score?result.score:'N/A');
	        			var scoreexplaination = (rubricLookup[result.rubric])?(rubricLookup[result.rubric].points)?rubricLookup[result.rubric].points[result.score]:'N/A':'N/A';
	        			rowcache = rowcache.replace(/{scoreexplain}/g, scoreexplaination?scoreexplaination:'N/A');
	        			rowcache = rowcache.replace(/{detail}/g, result.detail?result.detail:'');
	        			tablehtml.push(rowcache);
		            });
	        		
	        		tablehtml.push('</tbody></table>');
	        		
	        		$("#resultModalTabs a[href='#tabs-3']").show();
	        		
	        		$("#resultModalTabs a[href='#tabs-3']").tab("show");
	        		
	        		$("#ValidationResult .tab-content #tabs-3" ).html(tablehtml.join(""));
        		
        		}
        		catch(exp)
        		{
        			alert('javascript crashed, please report this issue:'+ err.message);
        		}
        		$.unblockUI();
        	}
        	else
        	{
        		alert(results.Message);
        		$.unblockUI();
        	}
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

$(function(){
	
	
	
	$('#reportSaveAsQuestion button').button();
	
	
	
	$('#saveResultsBtn').on('click', function(e){
		e.preventDefault();
		
		var ajaximgpath = window.currentContextPath + "/css/ajax-loader.gif";
		
		$.blockUI({ css: { 
	        border: 'none', 
	        padding: '15px', 
	        backgroundColor: '#000', 
	        '-webkit-border-radius': '10px', 
	        '-moz-border-radius': '10px', 
	        opacity: .5, 
	        color: '#fff' 
    	},
    	message: '<div class="progressorpanel"><img src="'+ ajaximgpath + '" alt="loading">'+
		          '<div class="lbl">Preparing your report...</div></div>' });
		
		
		//set the value of the result and post back to server.
		
		var $tab = $('#resultTabContent'), $active = $tab.find('.tab-pane.active');
		
		$('#downloadtest textarea').val($active.html());
		//submit the form.
		
		$.fileDownload($('#downloadtest').attr('action'), {
			
			successCallback: function (url) {
				$.unblockUI(); 
            },
            failCallback: function (responseHtml, url) {
            	alert("Sever error:" + responseHtml);
            	$.unblockUI(); 
            },
	        httpMethod: "POST",
	        data: $('#downloadtest').serialize()
	    });
		
	});
	
	//ccdavalidator_callAjax();
	
	$("#ccdavalidate_btn").click(function(e){
	    
		//switch back to tab1.
		$( "#ValidationResult [href='#tabs-1']").trigger( "click" );
		
		//block the UI.
		//find the cloest panel content
		
		BlockPortletUI();
		
		var formData = $('#CCDAValidationForm').serializefiles();
	    
	    $.ajax({
	        url: $('#CCDAValidationForm').attr('action'),
	        type: 'POST',
	        
	        xhr: function() {  // custom xhr
	            myXhr = $.ajaxSettings.xhr();
	            if(myXhr.upload){ // check if upload property exists
	                myXhr.upload.addEventListener('progress',progressHandlingFunction, false); // for handling the progress of the upload
	            }
	            return myXhr;
	        },
	        
	        //Ajax events
	        //beforeSend: beforeSendHandler,
	        success: completeHandler,
	        error: errorHandler,
	        // Form data
	        data: formData,
	        //Options to tell JQuery not to process data or worry about content-type
	        cache: false,
	        contentType: false,
	        processData: false
	    });
	    return false;
	});
	
});

