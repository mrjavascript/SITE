function errorHandler (request, status, error) {
    alert("error:"+ error);
}

function refreshInbox(isblocking)
{
	var ajaximgpath = window.currentContextPath + "/images/ajax-loader.gif";
	
	if(isblocking)
	{
		$("#DirectToolBoxPanel #tabs-1").block({ 
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
		          '<div class="lbl">Loadinig messages...</div></div>'
		
		});
	}
	
	var $form = $('#DirectToolBoxPanel #tabs-1 #messagelistingsettings');
	var editserialize = $form.serialize();
	editserialize = decodeURIComponent(editserialize.replace(/%2F/g, "/"));
	var serviceUrl = $form.attr("action");
	
	$.ajax({
        url: serviceUrl,
        type: 'POST',
        data: editserialize,
        //content is query string.
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function(data){
        	var messages = JSON.parse(data);
        	if(messages.length>=1)
        	{
        		try{
	        		var messagehtml = [];
	        		
	        		var msgTmp = [];
	        		
	        		msgTmp.push('<div class="message">');
	        		msgTmp.push('<div style="overflow:hidden; margin-bottom: 5px;">');
	        		msgTmp.push('<div class="subject">{subject}</div>');
	        		msgTmp.push('<div class="messagedate">{datetime}</div>');
	        		msgTmp.push('</div>');
	        		msgTmp.push('<div style="overflow:hidden">');
	        		msgTmp.push('<span class="from">From:</span> ');
	        		msgTmp.push('<strong>{from}</strong>');
	        		msgTmp.push('<div class="attachment" title="{attachment}" {imgstyle}></div>');
	        		msgTmp.push('</div>');
	        		msgTmp.push('</div>');
	        		
	        		var msgtmp = msgTmp.join('');         		
	        		$.each(messages, function(i, message) {
	        			//look up the label
	        			var rowcache = msgtmp;
	        			rowcache = rowcache.replace(/{subject}/g, message.Subject? message.Subject:'N/A');
	        			rowcache = rowcache.replace(/{datetime}/g, message.DateTimeStr?message.DateTimeStr:'N/A');
	        			rowcache = rowcache.replace(/{imgstyle}/g, message.HasAttachment?'':'style="display:none"');
	        			rowcache = rowcache.replace(/{from}/g, message.From?message.From:'N/A');
	        			rowcache = rowcache.replace(/{attachment}/g, message.AttachmentName?message.AttachmentName:'N/A');
	        			messagehtml.push(rowcache);
		            });
	        		
	        		$("#DirectToolBoxPanel #tabs-1 .message_list .module_content" ).html(messagehtml.join(""));
        		}
        		catch(exp)
        		{
        			alert('javascript crashed, please report the issue:'+ err.message);
        		}
        		if(isblocking)
        			$("#DirectToolBoxPanel #tabs-1").unblock();
        	}
        	else
        	{
        		alert(results.Message);
        	}
        },
        error: errorHandler,
        //Options to tell JQuery not to process data or worry about content-type
        cache: false,
    });
}

$(function(){
	$("#DirectToolBoxPanel #tabs").tabs();
	$("#DirectToolBoxPanel #tabs").tabs("disable", "tabs-2");
	$("#DirectToolBoxPanel #tabs").tabs("disable", "tabs-3");
	$("#DirectToolBoxPanel #tabs .alt_btn").click(function(){refreshInbox(true);});
	setInterval(function(){refreshInbox(false); }, 60000);
	refreshInbox(true);
});
