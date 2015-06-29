/**
 *
 */


var i=0,e=0; //カウント用
var event_timer=0; //イベントが出現してから何ターン経ったか
var turn = 1; //ターン数カウント用
var AI_Log; //AI情報配列
var Event_Log; //マップイベント情報配列
var Obst_Log; //障害物情報配列
var stage; //EaselJSのStageオブジェクト
var colx; //Color_Fieldのコンテキスト
var evex; //Event_Fieldのコンテキスト
var AI = new Array(8); //AIのオブジェクト
var AI_col = new Array("red.png","blue.png","purple.png","green.png","yellow.png","pink.png","orange.png","black.png"); //AIの色を格納
var AI_Tween = new Array(8); //AIのTweenオブジェクト
var AI_num = 4; //AIの数を格納
var MutekiMan = [1000,1000] //無敵の人
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



/* 背景の描画関数 */
function drawBackGround() {
	var cnvs = document.getElementById("BackGround");
	var ctx = cnvs.getContext("2d");
	var img = new Image();
	img.src = "img/field3.png";
	img.onload = function() {
		ctx.drawImage(img, 0, 0, 572, 624);
	}
}

/* 色々準備関数 */
function initialize() {
	$.ajaxSetup({ async : false});
	$.getJSON("json/AI.json", function(data) { AI_Log = data; }); //json拾ってくる処理
	$.getJSON("json/Event.json", function(data) { Event_Log = data; });
	$.ajaxSetup({ async : true });

	var queue = new LoadQueue(false); //sound系の処理
	queue.installPlugin(Sound);
	queue.loadManifest(manifest, true);
	queue.addEventListener("fileload", soundLoaded);

	var AICanvas = document.getElementById("AI_Field"); //AI描画キャンバスの宣言
	stage = new Stage(AICanvas);
	var ColorCanvas = document.getElementById("Color_Field"); //色描画キャンバスの宣言
	colx = ColorCanvas.getContext("2d");
	var EventCanvas = document.getElementById("Event_Field"); //イベント描画キャンバスの宣言
	evex = EventCanvas.getContext("2d");

	for(i=0;i<AI_num;i++){ AI[i] = new Shape(); stage.addChild(AI[i]); }
	Ticker.addEventListener("tick", function(){ stage.update(); });
	for(i=0;i<AI_num;i++){ //AIの初期座標の指定と描画とTweenの設定
		AI[i].x = 39.5+(26*AI_Log[0][i][0]); //x座標
		AI[i].y = 66+(26*AI_Log[0][i][1]); //y座標
		AI_Tween[i] = new Tween(AI[i], {loop:false}); //Tweenオブジェクトの作成
		drawAI(); //描画
		drawColorField(); //色の描画
	}
	alert("Hello");
	drawAnimation();
}

/* AIの描画関数(はじめの一回だけ) */
function drawAI() {
	AI[i].graphics.beginStroke("black");
	AI[i].graphics.beginFill("#b3b3b3");
	AI[i].graphics.drawPolyStar(0, 0, 11, 5, 0.6, -90);
	stage.update();
}

/* BGMの再生と，SEのインスタンスの作成関数 */
function soundLoaded() {
	bgmMusic = Sound.createInstance("bgm");
	explosionSE = Sound.createInstance("explode");
	battleSE = Sound.createInstance("battle");
	mutekiSE = Sound.createInstance("muteki");
	warpSE = Sound.createInstance("warp");

	bgmMusic.play("none", 0, 0, -1, 0.3, 0);

}


/* マスの色の描画関数 */
function drawColorField() {
	var cimg = new Image();
	cimg.src = "img/" + AI_col[i];
	var dx = AI[i].x-13.2;
	var dy = AI[i].y-14;
	cimg.onload = function() {
		colx.drawImage(cimg,dx,dy);
	}
}

/* 戦闘結果のマスの色の描画関数 */
function BdrawColorField() {
	var count_x, count_y; //カウント用
	var dx = AI[i].x - (26 * 3) -13.2;
	var dy = AI[i].y - (26 * 3) -14;
	var dxc = dx; //dx避難用
	var cimg = new Image();
	cimg.src = "img/" + AI_col[i];
	cimg.onload = function() {
		for (count_y = 0; count_y < 7; count_y++) {
			dx = dxc;
			for (count_x = 0; count_x < 7; count_x++) {
				if(dx >= 26 && dx <= 26*21 && dy >= 26*2 && dy <= 26*22){ colx.drawImage(cimg, dx, dy); }
				dx += 26;
			}
			dy +=26;
		}
	}
}

/* アニメーション描画用関数 */
function drawAnimation() {
	var timer = setInterval(function() {
		drawEvent(); //イベントマス描画
		MutekiClear(); //無敵になって20ターン経ったAIを元に戻す
		for (i = 0; i < AI_num; i++) {
			drawWalk(); //AIの移動処理
			drawColorField(); //色の描画
			eventArise(); //イベントが発生していたら，その処理
		}
		for (i = 0; i < AI_num; i++) {
			Battle(); //戦闘が発生していたらその処理
		}
		turn++;
		if (turn == 33) { //33ターン経ったら止まる(本当は500ターン)
			clearInterval(timer);
		}
	}, 200);
}

/* イベントマス描画関数(テスト用) */
function drawEvent() {
	if(turn - event_timer >= 20){
		evex.clearRect(0,0,572,624); //20ターン経ったらイベントマス消滅
	}

	if (Event_Log[e][0] == turn) {
		event_timer = turn;
		var eimg = new Image();
		if(Event_Log[e][1] == 1){
			eimg.src = "img/landmine.png"; //地雷マス画像
		}else if(Event_Log[e][1] == 2){
			eimg.src = "img/warp.png"; //ワープマス画像
		}else if(Event_Log[e][1] == 3){
			eimg.src = "img/muteki.png"; //無敵マス画像
		}
		var x = 39.5 + (26 * Event_Log[e][2])-12.8;
		var y = 66 + (26 * Event_Log[e][3])-14.5;
		eimg.onload = function(){
			evex.drawImage(eimg, x, y);
		}
		e++;
	}
}

/* AIの移動処理関数 */
function drawWalk() {
	if (AI_Log[turn][i][2] == 1) { //上に移動するときの処理
		if (AI[i].y == 66) {
			AI_Tween[i].to({y : 66 + (26 * 19)}, 0, Ease.linear);
			AI[i].y = 66 + (26 * 19);
		} else {
			AI_Tween[i].to({y : AI[i].y - 26}, 0, Ease.linear);
			AI[i].y -= 26;
		}
	} else if (AI_Log[turn][i][2] == 2) { //右に移動するときの処理
		if (AI[i].x == 39.5 + (26 * 19)) {
			AI_Tween[i].to({x : 39.5}, 0, Ease.linear);
			AI[i].x = 39.5;
		} else {
			AI_Tween[i].to({x : AI[i].x + 26}, 0, Ease.linear);
			AI[i].x += 26;
		}
	} else if (AI_Log[turn][i][2] == 3) { //下に移動するときの処理
		if (AI[i].y == 66 + (26 * 19)) {
			AI_Tween[i].to({y : 66}, 0, Ease.linear);
			AI[i].y = 66;
		} else {
			AI_Tween[i].to({y : AI[i].y + 26}, 0, Ease.linear);
			AI[i].y += 26;
		}
	} else if (AI_Log[turn][i][2] == 4) { //左に移動するときの処理
		if (AI[i].x == 39.5) {
			AI_Tween[i].to({x : 39.5 + (26 * 19)}, 0, Ease.linear);
			AI[i].x = 39.5 + (26 * 19);
		} else {
			AI_Tween[i].to({x : AI[i].x - 26}, 0, Ease.linear);
			AI[i].x -= 26;
		}
	}
}

/* AIがイベントを踏んだ時の処理関数 */
function eventArise() {
	if (AI_Log[turn][i][3] == 0) {
		//何もしない
	} else if (AI_Log[turn][i][3] == 1) { //地雷を踏んだ時の処理
		//alert("地雷を踏みました");
		drawEffect(1);
		explosionSE.play("none", 0, 0, 0, 1, 0);
		var x = AI[i].x - (26 * 3.5);
		var y = AI[i].y - (26 * 3.5);
		colx.clearRect(x, y, 182, 182); //周囲三マスの色を消去
		evex.clearRect(x, y, 182, 182); //イベントマスを消去
	} else if (AI_Log[turn][i][3] == 2) { //ワープを踏んだ時の処理
		//alert("ワープを踏みました");
		drawEffect(3);
		warpSE.play("none", 0, 0, 0, 1, 0);
		AI[i].x = 39.5 + (26 * AI_Log[turn][i][0]);
		AI[i].y = 66 + (26 * AI_Log[turn][i][1]);
		drawColorField();
	} else if (AI_Log[turn][i][3] == 3) { //無敵を踏んだ時の処理
		//alert("無敵を踏みました");
		var x = AI[i].x - (26 * 3.5);
		var y = AI[i].y - (26 * 3.5);
		drawEffect(4);
		mutekiSE.play("none", 0, 0, 0, 1, 0);
		evex.clearRect(x, y, 182, 182); //イベントマスを消去
		AI[i].graphics.beginFill("#ffff00");
		AI[i].graphics.drawPolyStar(0, 0, 11, 5, 0.6, -90);
		MutekiMan[0] = i;
		MutekiMan[1] = turn;
	}
}

/* 戦闘描画関数 */
function Battle() {
	if (AI_Log[turn][i][4] == 0) {
		//何もしない
	} else if (AI_Log[turn][i][4] == 1) { //戦闘に勝った時の処理

		BdrawColorField();
	} else if (AI_Log[turn][i][4] == 2) { //戦闘に負けたときの処理
		drawEffect(0);
		battleSE.play("none", 0, 0, 0, 1, 0);
		AI[i].x = 39.5 + (26 * AI_Log[turn][i][0]);
		AI[i].y = 66 + (26 * AI_Log[turn][i][1]);
		drawColorField();
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
		AI[MutekiMan[0]].graphics.beginFill("#b3b3b3");
		AI[MutekiMan[0]].graphics.drawPolyStar(0, 0, 11, 5, 0.6, -90);
	}
}
