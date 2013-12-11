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

$(function(){
	
	$("#category").selectable({
		selected : function(event,ui)
		{
			var _selectedValue = $(ui.selected).attr('value');
			//set the hidden value.
			$("input[type='hidden'][name='category']").val(_selectedValue);
		}
	});
	
	$("#qrdauploadfile").filestyle({ 			    
		image: window.currentContextPath + "/images/uploadqrdabutton.png",
		imageheight : 24,
		imagewidth : 110,
		width : 220
	});
	
	//if the formdata is supported. 
	if( window.FormData !== undefined ){
		$("#qrdavalidate_btn").button({ 
			icons: {primary: "ui-icon-check"  } 
		}).click(function(e){
			e.preventDefault(); 
			smartCCDAValidation();
		});
	}
	
});


function smartCCDAValidation()
{
	var ajaximgpath = window.currentContextPath + "/css/ajax-loader.gif";
	
	/*
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
    	message: '<div class="progresspanel"><img src="'+ ajaximgpath + '" alt="loading">'+
		          '<div class="lbl">Validating...</div></div>'
		
	});
	*/
	
	var formData = $('#QRDAValidationForm').serializefiles();
	var serviceUrl = $('#QRDAValidationForm').attr("action");
	
	$.ajax({
        url: serviceUrl,
        type: 'POST',
        /*
        xhr: function() {  // custom xhr
            myXhr = $.ajaxSettings.xhr();
            if(myXhr.upload){ // check if upload property exists
                myXhr.upload.addEventListener('progress',progressHandlingFunction, false); // for handling the progress of the upload
            }
            return myXhr;
        },
        */
        success: function(data){
        	var results = JSON.parse(data);
        	if(results.success)
        	{
        		alert(result.validationResult);
        		/*
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
	        		
	        		$("#ValidationResult #tabs").tabs("enable", "tabs-3");
	        		
	        		$( "#ValidationResult [href='#tabs-3']").trigger( "click" );
	        		
	        		$("#ValidationResult #tabs #tabs-3" ).html(tablehtml.join(""));
        		
        		}
        		catch(exp)
        		{
        			alert('javascript crashed, please report this issue:'+ err.message);
        		}
        		$.unblockUI();
        			*/
        	}
        	else
        	{
        		alert("worked");
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