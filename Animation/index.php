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
<script src="lib/tweenjs-0.6.1.min.js"></script>
<script src="lib/preloadjs-0.6.1.min.js"></script>
<script src="lib/soundjs-0.6.1.min.js"></script>
<script src="js/effect.js"></script>
<script src="js/Animation.js"></script>
<script src="js/Data.js"></script>
<script> LoadJson(); </script>
</head>

<body onload="LoadBGM();">
	<div class="replay">
		<input type="button" id="btn" value="Replay" onclick="AnimeReplay();">
	</div>
	<div class="speed">
		<input type="button" id="btn" value="等速" onclick="Speed1();">
		<input type="button" id="btn" value="倍速" onclick="Speed2();">
	</div>
	<canvas id="BackGround" width="593" height="593"></canvas>
	<canvas id="Color_Field" width="593" height="593"></canvas>
	<canvas id="Event_Field" width="593" height="593"></canvas>
	<canvas id="AI_Field" width="593" height="593"></canvas>
	<canvas id="Obst_Field" width="593" height="593"></canvas>
</body>
</html>
