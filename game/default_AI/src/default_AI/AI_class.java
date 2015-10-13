package default_AI;

import java.util.Random;

import libraly.AI;

public class AI_class extends AI{
	
	/*
	定数
	定数名:説明(定数値)
		GRID_X:グリッドの大きさx(20)
		GRID_Y:グリッドの大きさy(20)
		ENEMY_NUM:敵の数(開始時に決定)
	方向情報
		UNDER:足下(0)
		UP:上(1)
		RIGHT:右(2)
		DOWN:下(3)
		LEFT:左(4)
	オブジェクト情報
		WALL:壁(-1)
		FLOOR:無(0)
		PLAYER:自分のID(ゲームごとに変動)
		ENEMY[]:敵のID(ゲームごとに変動)
	色情報
		NO_COLOR:着色不可場所(-1)
		NORMAL_COLOR:無色(0)
		MY_COLOR:自分の色(ゲームごとに変動)
		ENEMY_COLOR[]:敵の色(ゲームごとに変動)
	ステータス情報	
		NOEVENT:無し(0)
		LANDMINE:地雷(1)
		WARP:ワープ(2)
		MATCHLESS:無敵(3)
		
	変数
	変数名:説明
		player_num:キャラクターの人数
		player_x[]:キャラクターの位置x
		player_y[]:キャラクターの位置y
			(例)player_x[PLAYER],player_x[ENEMY[0]]
		player_color_num[]:塗りつぶしたマスの数
		player_way[]:プレイヤーの方向
		player_status:自分のステータス
		object_map[][]:オブジェクトデータ[X][Y](キャラクターや障害物) 
		color_map[][]:色データ[X][Y](地面の色)
		event_x:イベントマスの位置x
		event_y:イベントマスの位置y
		
	メソッド
		up():このターン動かす方向を上に設定するためのメソッド(移動メソッドを複数実行した場合は，最後に実行された移動メソッドのみが有効となる)
		right():このターン動かす方向を右に設定するためのメソッド(移動メソッドを複数実行した場合は，最後に実行された移動メソッドのみが有効となる)
		left():このターン動かす方向を左に設定するためのメソッド(移動メソッドを複数実行した場合は，最後に実行された移動メソッドのみが有効となる)
		down():このターン動かす方向を下に設定するためのメソッド(移動メソッドを複数実行した場合は，最後に実行された移動メソッドのみが有効となる)
		stay():このターンそのマスに留まる設定をするためのメソッド(移動メソッドを複数実行した場合は，最後に実行された移動メソッドのみが有効となる)
		approach(ID):このターン動かす方向を指定した敵の方向に設定するためのメソッド(引数は敵ID→ENEMY[hoge])
		escape(ID):このターン動かす方向を指定した敵の逆方向に設定するためのメソッド(引数は敵ID→ENEMY[hoge])
		getmatchless():このターン動かす方向をイベントの方向に設定するためのメソッド
		to(x,y):このターン動かす方向を指定した位置の方向に設定するためのメソッド(引数は座標xと座標y)
		distance(ID):指定した敵との距離(x+y)を算出しint型で返すメソッド(引数は敵ID→ENEMY[hoge])
		neighbor_object(way):指定した方向のオブジェクトID(敵ID(ENEMY[hoge])や障害物(-1)など)をint型で返すメソッド．端にいる場合は反対側の端の情報を返す(引数は方向→UP)
		neighbor_color(way):指定した方向の色データ(PLAYERやENEMY[hoge])をint型で返すメソッド．端にいる場合は反対側の端の情報を返す(引数は方向→UP)

*/
	
	public AI_class() {
		
	}
	
	@Override
	public void action(){//このメソッド内にAIの処理を書き込んでください．新たにメソッドを追加することは可能です．
		
		/* (例)ランダムに上下左右に動くAI
		Random rand = new Random();
		int random=rand.nextInt(4)+1;
		
		if(random==UP){//randomの値がUP(1)の時
			up();//上に進む
		}else if(random==RIGHT){//randomの値がRIGHT(2)の時
			right();//右に進む
		}else if(random==DOWN){//randomの値がDOWN(3)の時
			down();//下に進む
		}else if(random==LEFT){//randomの値がLEFT(4)の時
			left();//左に進む
		}else{
			stay();//その場にとどまる
		}*/
		
		
	}
	   
}


