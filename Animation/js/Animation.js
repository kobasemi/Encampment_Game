/**
 *
 */
var math = 27; //一マスの大きさ
var i=0,e=0; //カウント用
var turn = 0; //ターン数カウント用
var stage; //EaselJSのStageオブジェクト
var colx; //Color_Fieldのコンテキスト
var evex; //Event_Fieldのコンテキスト
var obsx; //Obst_Fieldのコンテキスト
var AI = new Array(10); //AIのオブジェクト
var AI_num; //AIの数を格納
var AI_color = new Array("#FF3562","#20A8D7","#A64A97","#009E96","#FFF67F","#EF858C","#E8AC51","#66ff66","#4B5A66","#F2D8DF"); //AIのマス色を格納
var AI_Color2 = new Array("#cc3333","#0066ff","#663366","#669966","#ffff00","#ff3399","#cc6600","#66ff33","#8C8C8C","#EB7988");
var MutekiMan = [-1,1000]; //無敵の人
var file1 = "img/battle.png"; //戦闘スプライトシート
var file2 = "img/fire.png"; //爆発スプライトシート(再配布禁止)
//https://mrbubblewand.files.wordpress.com/2010/01/fire_001.pngからダウンロード
var file3 = "img/warp-effect.png"; //ワープスプライトシート(再配布禁止)
//https://mrbubblewand.files.wordpress.com/2010/12/light_004.pngからダウンロード
var file4 = "img/muteki-effect.png"; //無敵スプライトシート
var manifest = [ {id : "bgm",src : "sound/cyber6.mp3"}, //音楽ファイルの宣言
                 {id : "explode",src : "sound/explosion05.mp3"},
                 {id : "battle",src : "sound/battle3.mp3"},
                 {id : "muteki",src : "sound/muteki.mp3"},
                 {id : "warp",src : "sound/warp.mp3"}
                 ];
var bgmMusic;
var explosionSE;
var battleSE;
var mutekiSE;
var warpSE;

var GameOver = 0; //
var speed = 200; //drawAnimationのsetIntervalの早さ

/*BGMとSEのロード*/
function LoadBGM(){
	var queue = new LoadQueue(false);
	queue.installPlugin(Sound);
	queue.loadManifest(manifest, true);
	queue.addEventListener("complete", soundComplete);
}

/* BGMの再生と，SEのインスタンスの作成関数(ファイルのロードが終わったら，背景の描画) */
function soundComplete() {
	explosionSE = Sound.createInstance("explode");
	battleSE = Sound.createInstance("battle");
	mutekiSE = Sound.createInstance("muteki");
	warpSE = Sound.createInstance("warp");
	bgmMusic = Sound.createInstance("bgm");

	drawBackGround();
	start();
}

/* 背景の描画関数 */
function drawBackGround() {
	var cnvs = document.getElementById("BackGround");
	var ctx = cnvs.getContext("2d");
	var img = new Image();
	img.src = "img/field5.png";
	img.onload = function() {
		ctx.drawImage(img, 0, 0, 593, 593);
	}
}

/* 障害物の描画関数*/
function drawObstacle(){
	var j=0;
	var x,y;
	var cnvs = document.getElementById("Obst_Field");
	obsx = cnvs.getContext("2d");
	var img = new Image();
	img.src = "img/kabe.png";
	img.onload = function(){
		while(j<100 && Obst_Log[j][0] != -1){
			x = 39.5 + (math * Obst_Log[j][0])-13.5;
			y = 39.5 + (math * Obst_Log[j][1])-13.5;
			obsx.drawImage(img, x, y, 26, 26);
			j++;
		}
	}
}

/* 色々準備関数 */
function initialize() {
	var AICanvas = document.getElementById("AI_Field"); //AI描画キャンバスの宣言
	stage = new Stage(AICanvas);
	var ColorCanvas = document.getElementById("Color_Field"); //色描画キャンバスの宣言
	colx = ColorCanvas.getContext("2d");
	var EventCanvas = document.getElementById("Event_Field"); //イベント描画キャンバスの宣言
	evex = EventCanvas.getContext("2d");
	Ticker.addEventListener("tick", function(){ stage.update(); });
	initialize2();
}

function initialize2(){
	drawObstacle();
	for(i=0;i<AI_num;i++){ AI[i] = new Shape(); stage.addChild(AI[i]); }
	for(i=0;i<AI_num;i++){ //AIの初期座標の指定と描画とTweenの設定
		AI[i].x = 39.5+(math*AI_Log[0][i][0]); //x座標
		AI[i].y = 39.5+(math*AI_Log[0][i][1]); //y座標
		drawAI(i); //描画
		drawColorField(); //色の描画
	}
	bgmMusic.play("none", 0, 0, -1, 0.3, 0);
	LogMessage("試合開始");
	drawEvent();
	turn = 1;
	drawAnimation();
}

/* AIの描画関数(はじめの一回だけ) */
function drawAI(i) {
	AI[i].graphics.beginStroke("black");
	AI[i].graphics.beginFill(AI_Color2[i]);
	AI[i].graphics.drawPolyStar(0, 0, 11, 5, 0.6, -90);
	stage.update();
}


/* マスの色の描画関数 */
function drawColorField(){
	colx.fillStyle = AI_color[i];
	var x = 39.5 + (math * AI_Log[turn][i][0])-13.5;
	var y = 39.5 + (math * AI_Log[turn][i][1])-13.5;
	colx.fillRect(x, y, 26, 26);
}

/* 戦闘結果のマスの色の描画関数 */
function BdrawColorField(A) {
	var count_x, count_y; //カウント用
	var x = AI[A].x - (math * 3) -13.5;
	var y = AI[A].y - (math * 3) -13.5;

	var xc = x; //x避難用
	colx.fillStyle = AI_color[A];
	for (count_y = 0; count_y < 7; count_y++) {
		x = xc;
		for (count_x = 0; count_x < 7; count_x++) {
			if(x >= 26 && x <= 26 * 21 && y >= 26 && y <= 26 * 21){colx.fillRect(x, y, 26, 26); }
			x += math;
		}
		y +=math;
	}
}

/* アニメーション描画用関数 */
function drawAnimation() {
	GameOver = 0;
	var timer = setInterval(function() {

		if(turn < 501){
			drawEvent(); //イベントマス描画
			MutekiClear(); //無敵になって20ターン経ったAIを元に戻す
			for (i = 0; i < AI_num; i++) {
				drawWalk(); //AIの移動処理
				Battle(); //戦闘が発生していたらその処理
				drawColorField(); //色の描画
				eventArise(); //イベントが発生していたら，その処理
			}
			LogMath(); //AIたちのマスの数の表示
		}
		turn++;

		if(GameOver == 1){ //Replayの処理
			clearInterval(timer);
			/*----------リプレイ時のため初期化----------*/
			turn = 0;
			e = 0;
			bgmMusic.stop();
			colx.clearRect(0, 0, 593, 593);
			evex.clearRect(0, 0, 593, 593);
			obsx.clearRect(0, 0, 593, 593);
			stage.removeAllChildren();
			stage.update();
			event_timer=0;
			document.auto.automes.value = "";
			Logstr = "";
			Logstf = [];
			/*-----------------------------------------*/
		}

		if(GameOver == 2){ //一時停止の処理
			clearInterval(timer);
			drawAnimation();
		}

		if (turn == 501) {
			for(i=0; i<AI_num; i++){
				drawColorField(); //色の描画
			}
			resultReport();
			clearInterval(timer);
		}
	}, speed);
}

/* イベントマス描画関数(テスト用) */
function drawEvent() {

	if (Event_Log[turn][0] == turn) {
		var eimg = new Image();
		if(Event_Log[turn][1] == 1){
			eimg.src = "img/landmine.png"; //地雷マス画像
			LogMessage("X:"+Event_Log[turn][2]+"Y:"+Event_Log[turn][3]+"に地雷出現");
		}else if(Event_Log[turn][1] == 2){
			eimg.src = "img/warp.png"; //ワープマス画像
			LogMessage("X:"+Event_Log[turn][2]+"Y:"+Event_Log[turn][3]+"にワープマス出現");
		}else if(Event_Log[turn][1] == 3){
			eimg.src = "img/muteki.png"; //無敵マス画像
			LogMessage("X:"+Event_Log[turn][2]+"Y:"+Event_Log[turn][3]+"に無敵マス出現");
		}
		var x = 39.5 + (math * Event_Log[turn][2])-13.6;
		var y = 39.5 + (math * Event_Log[turn][3])-13.6;
		eimg.onload = function(){
			evex.drawImage(eimg, x, y);
		}
	}
}

/* AIの移動処理関数 */
function drawWalk() {
	if (AI_Log[turn][i][2] == 1) { //上に移動するときの処理
		if (AI[i].y <= 40) {
			AI[i].y = 39.5 + (math * 19);
		} else {
			AI[i].y -= math;
		}
	} else if (AI_Log[turn][i][2] == 2) { //右に移動するときの処理
		if (AI[i].x >= 39 + (math * 19)) {
			AI[i].x = 39.5;
		} else {
			AI[i].x += math;
		}
	} else if (AI_Log[turn][i][2] == 3) { //下に移動するときの処理
		if (AI[i].y >= 39 + (math * 19)) {
			AI[i].y = 39.5;
		} else {
			AI[i].y += math;
		}
	} else if (AI_Log[turn][i][2] == 4) { //左に移動するときの処理
		if (AI[i].x <= 40) {
			AI[i].x = 39.5 + (math * 19);
		} else {
			AI[i].x -= math;
		}
	}
}

/* AIがイベントを踏んだ時の処理関数 */
function eventArise() {
	if (AI_Log[turn][i][3] == 0) {
		//何もしない
	} else if (AI_Log[turn][i][3] == 1) { //地雷を踏んだ時の処理
		drawEffect(1);
		explosionSE.play("none", 0, 0, 0, 1, 0);
		var x = AI[i].x - (math * 3.5);
		var y = AI[i].y - (math * 3.5);
		var xx = AI[i].x - (math * 0.5);
		var yy = AI[i].y - (math * 0.5);
		colx.clearRect(x, y, 189, 189); //周囲三マスの色を消去
		evex.clearRect(xx, yy, 27, 27); //イベントマスを消去
		LogMessage(Playersarray[i] +"が地雷を踏んでしまった！");

	} else if (AI_Log[turn][i][3] == 2) { //ワープを踏んだ時の処理
		drawEffect(3);
		warpSE.play("none", 0, 0, 0, 1, 0);
		var x = AI[i].x - (math * 3.5);
		var y = AI[i].y - (math * 3.5);
		var xx = AI[i].x - (math * 0.5);
		var yy = AI[i].y - (math * 0.5);
		AI[i].x = 39.5 + (math * AI_Log[turn][i][0]);
		AI[i].y = 39.5 + (math * AI_Log[turn][i][1]);
		evex.clearRect(xx, yy, 27, 27); //イベントマスを消去
		LogMessage(Playersarray[i] +"はX:"+AI_Log[turn][i][0]+"Y:"+AI_Log[turn][i][1]+"にワープした");
		
	} else if (AI_Log[turn][i][3] == 3 && MutekiMan[0] == -1) { //無敵を踏んだ時の処理
		var x = AI[i].x - (math * 3.5);
		var y = AI[i].y - (math * 3.5);
		var xx = AI[i].x - (math * 0.5);
		var yy = AI[i].y - (math * 0.5);
		drawEffect(4);
		mutekiSE.play("none", 0, 0, 0, 1, 0);
		evex.clearRect(xx, yy, 27, 27); //イベントマスを消去
		AI[i].graphics.beginFill("#ffffff");
		AI[i].graphics.drawPolyStar(0, 0, 11, 5, 0.6, -90);
		MutekiMan[0] = i;
		MutekiMan[1] = turn;
		LogMessage(Playersarray[i] +"は無敵になった");
	}
}

/*戦闘で負けて，とばされた時の戻り処理*/
function BdrawWalk(t,A,x,y) {
	if (AI_Log[t][A][2] == 3) { //上に移動するときの処理
		if (y <= 40) {
			y = 39.5 + (math * 19);
		} else {
			y -= math;
		}
	} else if (AI_Log[t][A][2] == 4) { //右に移動するときの処理
		if (x >= 39 + (math * 19)) {
			x = 39.5;
		} else {
			x += math;
		}
	} else if (AI_Log[t][A][2] == 1) { //下に移動するときの処理
		if (y >= 39 + (math * 19)) {
			y = 39.5;
		} else {
			y += math;
		}
	} else if (AI_Log[t][A][2] == 2) { //左に移動するときの処理
		if (x <= 40) {
			x = 39.5 + (math * 19);
		} else {
			x -= math;
		}
	}
	AI[A].x = x;
	AI[A].y = y;
}

/* 戦闘描画関数 */
function Battle() {
	var x,y;
	var A = AI_Log[turn][i][5]; //プレイヤーiの戦った相手

	if (AI_Log[turn][i][4] == 0) {
		//何もしない
	} else if (AI_Log[turn][i][4] == 1) { //戦闘に勝った時の処理

		drawEffect(0);
		battleSE.play("none", 0, 0, 0, 1, 0);
		if(i > A){
			x = 39.5 + (math * AI_Log[turn+1][A][0]);
			y = 39.5 + (math * AI_Log[turn+1][A][1]);
			BdrawWalk(turn+1,A,x,y);
		}else{
			x = 39.5 + (math * AI_Log[turn][A][0]);
			y = 39.5 + (math * AI_Log[turn][A][1]);
			BdrawWalk(turn,A,x,y);
		}
		BdrawColorField(i);
		LogMessage(Playersarray[i] +"が"+Playersarray[AI_Log[turn][i][5]]+"を撃破！");

	} else if (AI_Log[turn][i][4] == 2) { //戦闘に負けたときの処理
		drawEffect(0);
		battleSE.play("none", 0, 0, 0, 1, 0);
		AI[i].x = 39.5 + (math * AI_Log[turn][i][0]);
		AI[i].y = 39.5 + (math * AI_Log[turn][i][1]);
		BdrawColorField(A);
		LogMessage(Playersarray[AI_Log[turn][i][5]]+"が"+Playersarray[i]+"を撃破！");
	}
}

/* 演出効果描画関数 */
function drawEffect(effect_num){
	var data = {};

	if(effect_num == 0){ //戦闘描写
		data.images = [file1];
		data.frames = {width:320, height:240, regX:0, regY:0};
		data.animations = {kill:[0,9,false,1]};
		var mySpriteSheet = new SpriteSheet(data);
		var animation = new Sprite(mySpriteSheet);
		stage.addChild(animation); //AI_Fieldに描画
		animation.x = AI[i].x-160;
		animation.y = AI[i].y-120;
		animation.gotoAndPlay("kill");
	}else if(effect_num == 1){ //地雷描写
		data.images = [file2];
		data.frames = {width:192, height:192, regX:0, regY:0};
		data.animations = {explode:[0,20,false,1]};
		var mySpriteSheet = new SpriteSheet(data);
		var animation = new Sprite(mySpriteSheet);
		stage.addChild(animation); //AI_Fieldに描画
		animation.x = AI[i].x-96;
		animation.y = AI[i].y-96;
		animation.gotoAndPlay("explode");
	}else if(effect_num == 3){ //ワープ描写
		data.images = [file3];
		data.frames = {width:192, height:192, regX:0, regY:0};
		data.animations = {warp:[0,24,false,1]};
		var mySpriteSheet = new SpriteSheet(data);
		var animation = new Sprite(mySpriteSheet);
		stage.addChild(animation); //AI_Fieldに描画
		animation.x = AI[i].x-96;
		animation.y = AI[i].y-96;
		animation.gotoAndPlay("warp");
	}else if(effect_num == 4){ //無敵描写
		data.images = [file4];
		data.frames = {width:120, height:120, regX:0, regY:0};
		data.animations = {muteki:[0,10,false,1]};
		var mySpriteSheet = new SpriteSheet(data);
		var animation = new Sprite(mySpriteSheet);
		stage.addChild(animation); //AI_Fieldに描画
		animation.x = AI[i].x-60;
		animation.y = AI[i].y-60;
		animation.gotoAndPlay("muteki");
	}
}

/* 無敵解除関数 */
function MutekiClear(){
	if(turn - MutekiMan[1] == 20){
		AI[MutekiMan[0]].graphics.beginFill(AI_Color2[MutekiMan[0]]);
		AI[MutekiMan[0]].graphics.drawPolyStar(0, 0, 11, 5, 0.6, -90);
		MutekiMan[0] = -1;
	}
}

/* 最終結果表示関数 */
function resultReport(){
	var array = new Array(AI_num);
	var result = "";
	var ic;
	var flag = 0;
	for(var i=0; i<AI_num; i++){
		array[i] = new Array(2);
		array[i][0] = i;
		array[i][1] = AI_Log[500][i][6];
	}

	array = array.sort(function(a,b){return(b[1]-a[1]);});
	result += "\n\n\n\n\n-----------------\n\n\n";
	
	for(i=0,ic=0; i<AI_num; i++,ic++){
		if(i>0){
			if(array[i][1] == array[i-1][1]) ic--;
		}
		result += ic+1 + "位  " + Playersarray[array[i][0]] + "\n\n";
		
		if(array[i][0]==0){
			flag = ic;
		}
	}
	
	result += "\n-----------------\n\n";
	
	if(flag == 0){
		result += "YOU WIN!\n\n";
	}else{
		result += "YOU LOSE...\n\n\n";
	}
	
	if(AI_num/2 < flag){
		result += "　　　　　 ＿＿＿　　　　 　 \n　　　　／＿＿＿ ＼　　　 　 \n　　　　| |⌒　 ⌒| |　　 きみ弱いなぁ\n　　　　ヽ ￣￣￣　/　　 \n　　 　 　 ￣□￣ 　　　 \n　　 ／￣￣ハ￣￣＼　 \n";
	}
	
LogMessage(result);
	
}

/*Replayボタンが押された時の処理*/
function AnimeReplay(){
	GameOver = 1;
	if(turn==501){
		/*----------リプレイ時のため初期化----------*/
		turn = 0;
		e = 0;
		bgmMusic.stop();
		colx.clearRect(0, 0, 593, 593);
		evex.clearRect(0, 0, 593, 593);
		obsx.clearRect(0, 0, 593, 593);
		stage.removeAllChildren();
		stage.update();
		event_timer=0;
		document.auto.automes.value = "";
		Logstr = "";
		Logstf = [];
		/*-----------------------------------------*/
	}
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



