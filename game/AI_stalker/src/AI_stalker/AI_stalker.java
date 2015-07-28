package AI_stalker;

import libraly.AI;

public class AI_stalker extends AI{
	/*
	//定数(一部例外あり)
	public final int GRID_X=20;//グリッドの大きさx
	public final int GRID_Y=20;//グリッドの大きさy
	public int ENEMY_NUM=1;//敵の数(開始時に決定)(実は変数)
	public final int UNDER=0;
	public final int UP=1;
	public final int RIGHT=2;
	public final int DOWN=3;
	public final int LEFT=4;
	//オブジェクト情報
	public final int WALL=-1;
	public final int FLOOR=0;
	public int PLAYER=1;//自分(実は変数)
	public int[] ENEMY=new int[10]; //敵　敵の数分用意→わからない・・・とりあえず多めに10(実は変数)
	//色情報
	public final int NO_COLOR=-1;//着色不可場所
	public final int NORMAL_COLOR=0;//無色
	public int MY_COLOR=1;//自分の色(実は変数)
	public int[] ENEMY_COLOR=new int[10]; //敵の色　敵の数分用意→わからない・・・とりあえず多めに10	(実は変数)
	//ステータス情報	
	public final int NOEVENT=0;//無し
	public final int LANDMINE=1;//地雷
	public final int WARP=2;//ワープ
	public final int MATCHLESS=3;//無敵
	
	//変数
	public int player_num = 2;
	public int[] player_x=new int[player_num+1]; //位置x
	public int[] player_y=new int[player_num+1]; //位置y
	public int[] player_color_num=new int[player_num+1]; //゙塗りつぶしたマスの数
	public int[] player_way=new int[player_num+1]; //方向
	public int player_status=0; //ステータス
	public int[][] object_map=new int[GRID_X][GRID_Y]; //オブジェクトデータ
	public int[][] color_map=new int[GRID_X][GRID_Y]; //色データ
	
	public int event_x=0;//イベントマスの位置x
	public int event_y=0;//イベントマスの位置y
*/
	public AI_stalker() {
		
	}

	@Override
	public void action(){
		if(Math.abs(player_x[ENEMY[0]]-player_x[PLAYER])>=Math.abs(player_y[ENEMY[0]]-player_y[PLAYER])){
			if(player_x[ENEMY[0]]>player_x[PLAYER]){
				player_way[PLAYER]=RIGHT;
			}else if(player_x[ENEMY[0]]<=player_x[PLAYER]){
				player_way[PLAYER]=LEFT;
			}
		}else{
			if(player_y[ENEMY[0]]>player_y[PLAYER]){
				player_way[PLAYER]=DOWN;
			}else if(player_y[ENEMY[0]]<=player_y[PLAYER]){
				player_way[PLAYER]=UP;
			}
		}
		
	}
	   
	}


