package test_AI_A;

import libraly.AI;

public class Player_A extends AI{
	
	  
	public Player_A() {
		
	}

	@Override
	  public void action(){
		way=DOWN;
		  /*ここにAIの処理を書きます
		  　・変数
			  UNDER プレイヤーの足下
			  UP    プレイヤーの上1マス
			  DOWN  プレイヤーの下1マス
			  LEFT  プレイヤーの左1マス
			  RIGHT プレイヤーの右1マス
		  
		    ・配列neighborはプレイヤー周囲の下記の情報を持っている
           	PLAYER   プレイヤー
	       	ENEMY    敵
	       	WALL     壁  
	       	FLOOR    床
	       	(例) if(neighbor[UP]==WALL)　//プレイヤー上1マスが壁ならば
	       	
       	  ・ 配列neighbor_colorはプレイヤー周囲の下記の色情報を持っている
       		PLAYER_COLOR   プレイヤーの色
       		ENEMY_COLOR    敵の色
       		NO_COLOR       着色不可 
       		NORMAL_COLOR   無着色
       		(例) if(neighbor_color[UP]==PLAYER_COLOR)　//プレイヤー上1マスがプレイヤーの色ならば
	       	
	            ・変数wayは次のターンに移動する方向です
	            (例)way=UP //次のターンに上に移動する	
	            (例)way=UNDER //1ターン停止	
	       */
	  }

	   
	}


