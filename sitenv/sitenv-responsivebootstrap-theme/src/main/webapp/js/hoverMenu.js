$(function($) {
 $('.nav .dropdown-parent').hover(function() {
 $(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();

 
}, function() {
 $(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();

 
});

 
$('.nav .dropdown-parent a').click(function(){
	if ($(this)[0].target == undefined || $(this)[0].target != "_blank")
	{
		location.href = this.href;
	}
	$('.dropdown-menu').stop(true, true).delay(100).slideUp();
 });

 
});