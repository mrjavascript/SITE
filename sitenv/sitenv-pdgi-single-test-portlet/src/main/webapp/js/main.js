$(document).ready(function () {
	  $("#querySubmit").click(function () {
		  
		  var jform = $('#providerDirectoryTestForm');
			jform.validationEngine('hideAll');
			jform.validationEngine({promptPosition:"centerRight", validateNonVisibleFields: true, updatePromptsPosition:true});
			if(jform.validationEngine('validate'))
			{
		  
		  blockProviderDirectoryWidget();
		  
		  
		  $.ajax({
	          type: "POST",
	          url: window.runTestsUrl,
	          data: $("#providerDirectoryTestForm").serialize(),
	          success: function(data) {
	        	  $("#PDResult").html(data);
	        	  
	        	  $("#resultsDialog").modal("show");
	        	  
	        	  unblockProviderDirectoryWidget();
	          },
	          error: function(jqXHR, textStatus, errorThrown) {
	        	  var iconurl = window.currentContextPath + "/images/icn_alert_error.png" ;
					
					$('#providerDirectoryWidget .blockMsg .progressorpanel img').attr('src',iconurl);
		        	
		        	$('#providerDirectoryWidget .blockMsg .progressorpanel .lbl').text('Error executing test cases.');
					
					if(window.directReceiveWdgt)
		        	{
		        		window.providerDirectoryTimeout = setTimeout(function(){
		        				window.providerDirectoryWidget.unbind("click");
		        				window.providerDirectoryWidget.unblock();
		        			},10000);
		        		
		        		
		        		window.providerDirectoryWidget.bind("click", function() { 
		        			window.providerDirectoryWidget.unbind("click");
		        			clearTimeout(window.providerDirectoryTimeout);
		        			window.providerDirectoryWidget.unblock(); 
		        			window.providerDirectoryWidget.attr('title','Click to hide this message.').click($.unblockUI); 
			            });
		        		
		        	}
	          }
	        });	
			}
	  });
  });


function blockProviderDirectoryWidget()
{
	var ajaximgpath = window.currentContextPath + "/images/ajax-loader.gif";
	window.providerDirectoryWidget = $('#providerDirectoryWidget .well');
	window.providerDirectoryWidget.block({ 
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
				 '<div class="lbl">Executing Test Cases...</div></div>',
	});
}

function unblockProviderDirectoryWidget()
{
	if(window.providerDirectoryWidget)
		window.providerDirectoryWidget.unblock();
}