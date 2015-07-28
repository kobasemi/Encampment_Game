package libraly;



import java.util.Random;

public class   AI {
	
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
	
	
	

	
	
	public Random rnd = new Random();


	public  AI(){  
	}
	
	public void set_info(int ID,int enemy_num){//引数によって敵の数とプレイヤーや色の番号をセット(例)プレイヤー1→1、プレイヤー2→2
		PLAYER=ID;
		for(int i=0,j=1;i<ENEMY.length;i++){
			if(j!=PLAYER){
				ENEMY[i]=j;
			}else{
				i--;
			}
			j++;
		}
		MY_COLOR=ID;
		for(int i=0,j=1;i<ENEMY_COLOR.length;i++){
			if(j!=PLAYER){
				ENEMY_COLOR[i]=j;
			}else{
				i--;
			}
			j++;
		}
		ENEMY_NUM=enemy_num;
	}
	
	public int get_way(){//行き先の値を渡す
		return player_way[PLAYER];
	}

	public void set_object_map(int data[][]){//オブジェクトマップ情報をもらう
		for (int i = 0; i < object_map.length; i++) {
			for (int j = 0; j < object_map[i].length; j++) {
				object_map[i][j]=data[i][j];
			}
		}
	}

	public void set_color_map(int data[][]){//色マップ情報をもらう
		for (int i = 0; i < color_map.length; i++) {
			for (int j = 0; j < color_map[i].length; j++) {
				color_map[i][j]=data[i][j];
			}
		}
	}
	
	public void set_status(int data){//ステータス情報をもらう
			player_status=data;		
	}
	
	public void set_way(int data[]){//方向情報をもらう
		for(int i=1;i<player_num+1;i++){
			player_way[i]=data[i];		
		}
	}
	
	public void set_color(int data[]){//自分の色数と敵の色数をもらう
		for(int i=1;i<player_num+1;i++){
			player_color_num[i]=data[i];		
		}
	}
	
	public void set_position_data(int[] data_x,int[] data_y){//位置情報をもらう
		for(int i=1;i<player_num+1;i++){
			player_x[i]=data_x[i];		
			player_y[i]=data_y[i];		
		}
	}
	
	public void set_event_data(int x,int y){//イベントマスの位置をもらう
		event_x=x;
		event_y=y;
	}


	//処理
	public void action(){//AIの処理を書く (以下はランダム移動の例)
		int walk_ran =UP;
		walk_ran=rnd.nextInt(4)+1;

		//上
		if(walk_ran==UP){
			player_way[PLAYER]=UP;
		}
		//下
		else if(walk_ran==DOWN){
			player_way[PLAYER]=DOWN;
		}
		//左
		else if(walk_ran==LEFT){
			player_way[PLAYER]=LEFT;
		}
		//右
		else if(walk_ran==RIGHT){
			player_way[PLAYER]=RIGHT;
		}
	}
	
	public void right(){
		player_way[PLAYER]=RIGHT;
	}

}


