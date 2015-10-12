<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Encampment Game by AI</title>
<link rel="stylesheet" type="text/css" href="css/anime.css">

<script>
	var createjs = window;
</script>

<script src="lib/jquery-1.11.3.min.js"></script>
<script src="lib/easeljs-0.8.1.min.js"></script>
<script src="lib/preloadjs-0.6.1.min.js"></script>
<script src="lib/soundjs-0.6.1.min.js"></script>
<script src="js/effect.js"></script>
<script src="js/Animation.js"></script>
<script src="js/Data.js"></script>
<script> LoadJson(); </script>
<script>
Logstf = new Array(); //ログメッセージ格納配列
var Logstr = ""; //ログメッセージ文字列
var z=0; //ログメッセージボックスのスクロールパラメータ

/* ログの表示関数 */
function LogMessage(message){
	var scroll = document.getElementById( "anime_log" );
	if(turn == 501){
		Logstf[z] = message;
	}else{
		Logstf[z] = "Turn"+turn+": "+message+"\n---\n";
	}
	Logstr += Logstf[z];
	z++;
	document.auto.automes.value = Logstr;
	scroll.scrollTop = z*100;
}

/* 各AIの持ちマス数 */
function LogMath(){
	Mathstr = "";
	for(var i=0; i<AI_num; i++){
		Mathstr += "<p>" + AI_Log[turn][i][6]+"\n</p>";
	}
	document.getElementById("math_num").innerHTML = Mathstr;
}

</script>

</head>

<body onload="LoadBGM();">

<div id="wrapper">
	<div class="replay">
		<input type="button" id="btn" value="Replay" onclick="AnimeReplay();">
	</div>
	<div class="speed">
		<input type="button" id="btn" value="等速" onclick="Speed1();">
		<input type="button" id="btn" value="倍速" onclick="Speed2();">
	</div>
	
	<form name="auto">
	<textarea id="anime_log" name="automes" rows="50" cols="40" wrap="physical">
	</textarea>
	</form>
	
	<canvas id="BackGround" width="593" height="593"></canvas>
	<canvas id="Color_Field" width="593" height="593"></canvas>
	<canvas id="Event_Field" width="593" height="593"></canvas>
	<canvas id="AI_Field" width="593" height="593"></canvas>
	<canvas id="Obst_Field" width="593" height="593"></canvas>
</div>
</body>
</html>
