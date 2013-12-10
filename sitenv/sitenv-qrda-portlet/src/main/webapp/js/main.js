$(function(){
	
	$("#qrdavalidate_btn").button({ 
		icons: {primary: "ui-icon-check"  } 
	});
	
	
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
		imagewidth : 130,
		width : 220
	});
	
	$("#qrdavalidate_btn").button({ 
		icons: {primary: "ui-icon-check"  } 
	});
	
});
	