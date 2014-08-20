$(function($) {

	$('.interactive-thumbnail').hover(function(e) {
		e.preventDefault();
		 
		var myData = $(this).data("linked-thumbnail-base");
		$("[data-linked-thumbnail-base=" + myData + "]").toggleClass("active");

	});
	
	$('.interactive-thumbnail').click(function(e) {
		$(this).blur();
	});
	


 
});