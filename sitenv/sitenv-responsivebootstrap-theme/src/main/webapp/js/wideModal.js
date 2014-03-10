// when .modal-wide opened, set content-body height based on browser height; 200 is appx height of modal padding, modal title and button bar

$(function($) {

	$(".modal-wide").on("show.bs.modal", function() {
	  var height = $(window).height() - 200;
	  $(this).find(".modal-body").css("max-height", height);
	});
	
	$('*[data-dismiss="modal"]').on("click", function() {
		$('.modal-body').scrollTop(0);
	});
	
	$('.modal *[data-toggle="tab"]').on("click", function() {
		$('.modal-body').scrollTop(0);
	});

});