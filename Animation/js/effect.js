var GameOver = 0; //
var speed = 200; //drawAnimationのsetIntervalの早さ

/*アニメのスタート画面*/
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

function restart(){
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
				initialize2();
				click = 1;
			}
		});
}

/*Replayボタンが押された時の処理*/
function AnimeReplay(){
	GameOver = 1;
	restart();
}

/*通常速度*/
function Speed1(){
	speed = 200;
	GameOver = 2;
}
/*倍速*/
function Speed2(){
	speed = 100;
	GameOver = 2;
}