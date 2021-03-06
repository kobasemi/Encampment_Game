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
		public int player_num = 10;
		public int[] player_x=new int[player_num+1]; //位置x
		public int[] player_y=new int[player_num+1]; //位置y
		public int[] player_color_num=new int[player_num+1]; //゙塗りつぶしたマスの数
		public int[] player_way=new int[player_num+1]; //プレイヤーの方向(自分の方向に値を代入することで次のターンに移動する方向を決める)
		public int player_status=0; //ステータス
		public int[][] object_map=new int[GRID_X][GRID_Y]; //オブジェクトデータ
		public int[][] color_map=new int[GRID_X][GRID_Y]; //色データ
	
		public int event_x=0;//イベントマスの位置x
		public int event_y=0;//イベントマスの位置y
	
	
	

	
	
	public Random rnd = new Random();


	public  AI(){  
	}
	
	/*引数によって敵の数とプレイヤーIDや色の番号をセット 
	**(例)自分がプレイヤー2でプレイヤー数が4の場合は
	**PLAYER=2
	**ENEMY[0]=1
	**ENEMY[1]=3
	**ENEMY[2]=4
	**カラーも同様
	*/
	public void set_info(int ID,int enemy_num){
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

	/*AIの処理を書く (以下はランダム移動の例)
	**actionの処理終了時のplayer_way[PLAYER]の値によって
	**次にプレイヤーが移動する方向を決定する
	*/
	
	//ランダム処理
	public void action(){
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
	
	//その場
	public void stay() {
		player_way[PLAYER] = UNDER;
	}

	//右
	public void right() {
		player_way[PLAYER] = RIGHT;
	}
	
	//左
	public void left() {
		player_way[PLAYER] = LEFT;
	}
	
	//上
	public void up() {
		player_way[PLAYER] = UP;
	}
	
	//下
	public void down() {
		player_way[PLAYER] = DOWN;
	}
	
	//相手に向かって進む
	public void approach(int ID) {
		
		//初期化
		player_way[PLAYER] = UNDER;
		
		//x
		if(player_x[PLAYER] > player_x[ID]) {
			if(Math.abs((player_x[ID]+GRID_X)-player_x[PLAYER]) >= Math.abs(player_x[ID]-player_x[PLAYER])){
				player_way[PLAYER] = LEFT;
			} else {
				player_way[PLAYER] = RIGHT;
			}
		} else if(player_x[PLAYER] < player_x[ID]) {
			if(Math.abs(player_x[ID]-player_x[PLAYER]) > Math.abs((player_x[ID]-GRID_X)-player_x[PLAYER])) {
				player_way[PLAYER] = LEFT;
			}else{
				player_way[PLAYER] = RIGHT;
			}
		} else { }
		
		//y
		if(player_way[PLAYER] == UNDER) {
			if(player_y[PLAYER] >= player_y[ID]) {
				if(Math.abs(player_y[ID]-player_y[PLAYER]) > Math.abs((player_y[ID]+GRID_Y)-player_y[PLAYER])) {
					player_way[PLAYER] = DOWN;
				} else {
					player_way[PLAYER] = UP;
				}
			} else {
				if(Math.abs((player_y[ID]-GRID_Y)-player_y[PLAYER]) >= Math.abs(player_x[ID]-player_x[PLAYER])) {
					player_way[PLAYER] = DOWN;
				} else {
					player_way[PLAYER] = UP;
				}
			}
		} else { }
	}
		
	
	//相手から逃げる
	public void escape(int ID) {
		
		//初期化
		player_way[PLAYER] = UNDER;
		
		//x
		if(player_x[PLAYER] > player_x[ID]) {
			if(Math.abs((player_x[ID]+GRID_X)-player_x[PLAYER]) < Math.abs(player_x[ID]-player_x[PLAYER])) {
				player_way[PLAYER] = LEFT;
			} else {
				player_way[PLAYER] = RIGHT;
			}
		} else if(player_x[PLAYER] < player_x[ID]) {
			if(Math.abs(player_x[ID]-player_x[PLAYER]) < Math.abs((player_x[ID]-GRID_X)-player_x[PLAYER])) {
				player_way[PLAYER] = LEFT;
			}else{
				player_way[PLAYER] = RIGHT;
			}
		} else { }
		
		//y
		if(player_way[PLAYER] == UNDER) {
			if(player_y[PLAYER] >= player_y[ID]) {
				if(Math.abs(player_y[ID]-player_y[PLAYER]) < Math.abs((player_y[ID]+GRID_Y)-player_y[PLAYER])) {
					player_way[PLAYER] = DOWN;
				} else if(Math.abs(player_y[ID]-player_y[PLAYER]) > Math.abs((player_y[ID]+GRID_Y)-player_y[PLAYER])) {
					player_way[PLAYER] = UP;
				}
			} else {
				if(Math.abs((player_y[ID]-GRID_Y)-player_y[PLAYER]) < Math.abs(player_x[ID]-player_x[PLAYER])) {
					player_way[PLAYER] = DOWN;
				} else if(Math.abs((player_y[ID]-GRID_Y)-player_y[PLAYER]) > Math.abs(player_x[ID]-player_x[PLAYER])) {
					player_way[PLAYER] = UP;
				}
			}
		} else { }
	}

	//イベントに向かう
	public void getmatchless() {
			
		//初期化
		player_way[PLAYER] = UNDER;
		
		//x
		if(player_x[PLAYER] > event_x) {
			if(Math.abs((event_x+GRID_X)-player_x[PLAYER]) >= Math.abs(event_x-player_x[PLAYER])){
				player_way[PLAYER] = LEFT;
			} else {
				player_way[PLAYER] = RIGHT;
			}
		} else if(player_x[PLAYER] < event_x) {
			if(Math.abs(event_x-player_x[PLAYER]) > Math.abs((event_x-GRID_X)-player_x[PLAYER])) {
				player_way[PLAYER] = LEFT;
			}else{
				player_way[PLAYER] = RIGHT;
			}
		} else { }
		
		//y
		if(player_way[PLAYER] == UNDER) {
			if(player_y[PLAYER] >= event_y) {
				if(Math.abs(event_y-player_y[PLAYER]) > Math.abs((event_y+GRID_Y)-player_y[PLAYER])) {
					player_way[PLAYER] = DOWN;
				} else {
					player_way[PLAYER] = UP;
				}
			} else {
				if(Math.abs((event_y-GRID_Y)-player_y[PLAYER]) >= Math.abs(event_y-player_x[PLAYER])) {
					player_way[PLAYER] = DOWN;
				} else {
					player_way[PLAYER] = UP;
				}
			}
		} else { }
	}
	
	//指定位置に向かう
		public void to(int x,int y) {
				
			//初期化
			player_way[PLAYER] = UNDER;
			
			//x
			if(player_x[PLAYER] > x) {
				if(Math.abs((x+GRID_X)-player_x[PLAYER]) >= Math.abs(x-player_x[PLAYER])){
					player_way[PLAYER] = LEFT;
				} else {
					player_way[PLAYER] = RIGHT;
				}
			} else if(player_x[PLAYER] < x) {
				if(Math.abs(x-player_x[PLAYER]) > Math.abs((x-GRID_X)-player_x[PLAYER])) {
					player_way[PLAYER] = LEFT;
				}else{
					player_way[PLAYER] = RIGHT;
				}
			} else { }
			
			//y
			if(player_way[PLAYER] == UNDER) {
				if(player_y[PLAYER] >= y) {
					if(Math.abs(y-player_y[PLAYER]) > Math.abs((y+GRID_Y)-player_y[PLAYER])) {
						player_way[PLAYER] = DOWN;
					} else {
						player_way[PLAYER] = UP;
					}
				} else {
					if(Math.abs((y-GRID_Y)-player_y[PLAYER]) >= Math.abs(y-player_x[PLAYER])) {
						player_way[PLAYER] = DOWN;
					} else {
						player_way[PLAYER] = UP;
					}
				}
			} else { }
		}
		
		//敵との距離を算出
		public int distance(int ID) {
			
			//初期化
			int dis=0;
			//x
			if(player_x[PLAYER] > player_x[ID]) {
				if(Math.abs((player_x[ID]+GRID_X)-player_x[PLAYER]) >= Math.abs(player_x[ID]-player_x[PLAYER])){
					dis= Math.abs(player_x[PLAYER]-player_x[ID]);
				} else {
					dis=Math.abs((player_x[ID]+GRID_X)-player_x[PLAYER]);
				}
			} else if(player_x[PLAYER] < player_x[ID]) {
				if(Math.abs(player_x[ID]-player_x[PLAYER]) > Math.abs((player_x[ID]-GRID_X)-player_x[PLAYER])) {
					dis=Math.abs((player_x[ID]-GRID_X)-player_x[PLAYER]);
				}else{
					dis=Math.abs(player_x[ID]-player_x[PLAYER]);
				}
			} else { }
			
			//y
				if(player_y[PLAYER] >= player_y[ID]) {
					if(Math.abs(player_y[ID]-player_y[PLAYER]) > Math.abs((player_y[ID]+GRID_Y)-player_y[PLAYER])) {
						dis=dis+Math.abs((player_y[ID]+GRID_Y)-player_y[PLAYER]);
					} else {
						dis=dis+Math.abs(player_y[ID]-player_y[PLAYER]);
					}
				} else {
					if(Math.abs((player_y[ID]-GRID_Y)-player_y[PLAYER]) >= Math.abs(player_x[ID]-player_x[PLAYER])) {
						dis=dis+Math.abs(player_x[ID]-player_x[PLAYER]);
					} else {
						dis=dis+Math.abs((player_y[ID]-GRID_Y)-player_y[PLAYER]);
					}
				}
				return dis;
		}
		
		int neighbor_color(int way) {// 方向を引数に渡せばその方向の色情報を返す
			if(way==UP){
				if (player_y[PLAYER] != 0) {
					return color_map[player_x[PLAYER]][player_y[PLAYER] - 1];
				} else {
					return color_map[player_x[PLAYER]][GRID_Y - 1];
				}
			}else if(way==RIGHT){
				if (player_x[PLAYER] != GRID_X - 1) {
					return color_map[player_x[PLAYER] + 1][player_y[PLAYER]];
				} else {
					return color_map[0][player_y[PLAYER]];
				}
			}else if(way==DOWN){
				if (player_y[PLAYER] != GRID_Y - 1) {
					return color_map[player_x[PLAYER]][player_y[PLAYER] + 1];
				} else {
					return color_map[player_x[PLAYER]][0];
				}
			}else if(way==LEFT){
				if (player_x[PLAYER] != 0) {
					return color_map[player_x[PLAYER] - 1][player_y[PLAYER]];
				} else {
					return color_map[GRID_X - 1][player_y[PLAYER]];
				}
			}
			return 0;
		}
		
		int neighbor_object(int way) {// 方向を引数に渡せばその方向の色情報を返す
			if(way==UP){
				if (player_y[PLAYER] != 0) {
					return object_map[player_x[PLAYER]][player_y[PLAYER] - 1];
				} else {
					return object_map[player_x[PLAYER]][GRID_Y - 1];
				}
			}else if(way==RIGHT){
				if (player_x[PLAYER] != GRID_X - 1) {
					return object_map[player_x[PLAYER] + 1][player_y[PLAYER]];
				} else {
					return object_map[0][player_y[PLAYER]];
				}
			}else if(way==DOWN){
				if (player_y[PLAYER] != GRID_Y - 1) {
					return object_map[player_x[PLAYER]][player_y[PLAYER] + 1];
				} else {
					return object_map[player_x[PLAYER]][0];
				}
			}else if(way==LEFT){
				if (player_x[PLAYER] != 0) {
					return object_map[player_x[PLAYER] - 1][player_y[PLAYER]];
				} else {
					return object_map[GRID_X - 1][player_y[PLAYER]];
				}
			}
			return 0;
		}
		
		
		
}
