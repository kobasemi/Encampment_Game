package libraly;



import java.util.Random;

public class   AI {
	
	//定数(一部例外あり)
		public final int GRID_X=20;//グリッドの大きさx
		public final int GRID_Y=20;//グリッドの大きさy
		public int ENEMY_NUM=2;//敵の数(開始時に決定)(実は変数)
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
	public int x=0;//自分の位置x
	public int y=0;//自分の位置y
	public int my_color_num=0;//自分が塗りつぶしたマスの数
	public int[] enemy_color_num=new int[10]; //敵が塗りつぶしたマスの数→わからない・・・とりあえず多めに10
	public int my_status=0;//自分のステータス
	public int[] neighbor_object=new int[5]; //オブジェクトデータ
	public int[] neighbor_color=new int[5]; //色データ
	public int[] neighbor_status=new int[5];//ステータスデータ
	public int way=0;//次に自分の進む方向
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
			}
			j++;
		}
		MY_COLOR=ID;
		for(int i=0,j=1;i<ENEMY_COLOR.length;i++){
			if(j!=PLAYER){
				ENEMY_COLOR[i]=j;
			}
			j++;
		}
		ENEMY_NUM=enemy_num;
	}
	
	public int get_way(){//行き先の値を渡す
		return way;
	}

	public void set_neighbor_data(int data[]){//周辺のマップ情報をもらう
		neighbor_object[UP]=data[UP];
		neighbor_object[RIGHT]=data[RIGHT];
		neighbor_object[DOWN]=data[DOWN];
		neighbor_object[LEFT]=data[LEFT];
	}

	public void set_neighbor_color(int data[]){//周辺の色情報をもらう
		neighbor_color[UP]=data[UP];
		neighbor_color[RIGHT]=data[RIGHT];
		neighbor_color[DOWN]=data[DOWN];
		neighbor_color[LEFT]=data[LEFT];
	}
	
	public void set_neighbor_status(int data[]){//周辺のステータス情報をもらう
		neighbor_status[UP]=data[UP];
		neighbor_status[RIGHT]=data[RIGHT];
		neighbor_status[DOWN]=data[DOWN];
		neighbor_status[LEFT]=data[LEFT];
	}
	
	public void set_grid_color(int color[]){//自分の色数と敵の色数をもらう
		for(int i=1,j=0;i<color.length;i++){
			if(i==PLAYER){
				my_color_num=color[i];
			}else{
				enemy_color_num[j]=color[i];
				j++;
			}
		}
	}
	
	public void set_my_data(int data_x,int data_y,int status){//自分の位置情報とステータス情報をもらう
		x=data_x;
		y=data_y;
		my_status=status;
	}
	
	public void set_event_data(int x,int y){//イベントマスの位置をもらう
		event_x=x;
		event_y=y;
	}


	//処理
	public void action(){//AIの処理を書く (以下はランダム移動の例)
		int walk_ran =0 ;
		int check=0;
		walk_ran=rnd.nextInt(4)+1;
		while(neighbor_object[walk_ran]<0&&check<30){//30回で適切な値が出なければ,１ターン停止
			check++;
			walk_ran=rnd.nextInt(4)+1;
		}

		//移動処理
		if(check>=30){
			way=UNDER;
		}
		//上
		else if(walk_ran==UP){
			way=UP;
		}
		//下
		else if(walk_ran==DOWN){
			way=DOWN;
		}
		//左
		else if(walk_ran==LEFT){
			way=LEFT;
		}
		//右
		else if(walk_ran==RIGHT){
			way=RIGHT;
		}
	}
	
	public void right(){
		way=RIGHT;
	}

}


