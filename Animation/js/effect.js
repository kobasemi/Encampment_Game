
function start(){
		var click=0;
		$(this).blur();
		if($("#modal-overlay")[0]) $("#modal-overlay").remove();
		$("body").append('<div id="modal-overlay"></div>');
		$("#modal-overlay").fadeIn();
		$("body").append('<div id="modal-content">START!</div>');
		$("#modal-content").fadeIn("slow");
		$("#modal-content").unbind().click(function(){
			if(click == 0){
				$("#modal-content,#modal-overlay").fadeOut("slow",function(){
				$("#modal-content,#modal-overlay").remove();
				});
				initialize();
				click = 1;
			}
		});
}