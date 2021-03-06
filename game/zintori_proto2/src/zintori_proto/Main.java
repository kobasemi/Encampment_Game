package zintori_proto;

import java.awt.*;
import java.applet.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Random;

import javax.swing.JFrame;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

public class Main extends Applet implements Runnable {
	Image back;
	Graphics buffer;
	int player_num = 10;
	// //AIをつくるための入れ物たち////
	String AI_name[] = new String[player_num + 1];
	Object myPlayer[] = new Object[player_num + 1];
	Class<?> AI_class[] = new Class<?>[player_num + 1];
	Method set_info[] = new Method[player_num + 1];
	Method get_way[] = new Method[player_num + 1];
	Method set_object_map[] = new Method[player_num + 1];
	Method set_color_map[] = new Method[player_num + 1];
	Method set_status[] = new Method[player_num + 1];
	Method set_way[] = new Method[player_num + 1];
	Method set_color[] = new Method[player_num + 1];
	Method set_position_data[] = new Method[player_num + 1];
	Method set_event_data[] = new Method[player_num + 1];
	Method action[] = new Method[player_num + 1];

	URL[] urls = new URL[player_num];
	// //////////////////////////
	Thread t;
	Image field = getToolkit().getImage("field.gif");

	Random rnd = new Random();

	// 定数
	// 方向情報
	final int UNDER = 0;
	final int UP = 1;
	final int RIGHT = 2;
	final int DOWN = 3;
	final int LEFT = 4;
	// フィールド情報
	final int WALL = -1;
	final int FLOOR = 0;
	final int PLAYER_A = 1;
	final int PLAYER_B = 2;
	// 色情報（マイナスは着色不可場所）
	final int NO_COLOR = -1;
	final int NORMAL_COLOR = 0;
	final int A_COLOR = 1;
	final int B_COLOR = 2;
	// その他
	final int GRID_X = 20;
	final int GRID_Y = 20;
	final int TURN = 500;
	// イベント情報
	final int NOEVENT = 0;// 無し
	final int LANDMINE = 1;// 地雷
	final int WARP = 2;// ワープ
	final int MATCHLESS = 3;// 無敵
	//勝ち負け
	final int NO = 0;// 無し
	final int WIN = 1;// 勝ち
	final int LOSE = 2;// 負け
	//障害物
	final int OBSTACLE_NUM = 100;

	// 変数
	private int[] player_first_x = new int[player_num + 1];
	private int[] player_first_y = new int[player_num + 1];
	private int[] player_x = new int[player_num + 1];
	private int[] player_y = new int[player_num + 1];
	private int[] player_way = new int[player_num + 1];
	private int[] player_status = new int[player_num + 1];
	private int[] player_color_num = new int[player_num + 1];
	private int[] player_battle = new int[player_num + 1];
	private int[] player_rival = new int[player_num + 1];
	private boolean[] player_stop = new boolean[player_num + 1];
	

	private int event_x = 0;// 
	private int event_y = 0;// 
	private int event_type = 0;// 
	private int event_born = 0;// 
	private int event_count = 0;// 
	
	private int[] obstacle_x = new int[OBSTACLE_NUM];//障害物のx
	private int[] obstacle_y = new int[OBSTACLE_NUM];//障害物のy

	int turn = 0;
	boolean game_finish = false;
	boolean event_on=false;

	String event = "";
	
	int data_AI[][][]=new int[TURN+2][4][7];
	int data_event[][]=new int[TURN+2][4];
	int data_obstacle[][]=new int[100][2];

	int object_map[][] = { // マップ情報（プレイヤー位置等）
	{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	int color_map[][] = { // 色情報
	{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	public static void main(String args[]) {
		// //アプレットの設定等々///////////
		JFrame myFrame = new JFrame("zintori_proto");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
		Applet applet = new Main();
		applet.init();
		myFrame.add(applet);
		myFrame.setSize(820, 640);
		applet.start();
		// ///////////////////////////
	}

	// 初期化
	public void init() {
		player_num=2;//debug
		// ////内部のクラスであれば以下のような処理はいらないが、外部jarからAIを読み込む必要があるため、若干複雑な処理が必要//////
		try {
			urls[0] = new URL(
					"file:/Users/koba/Documents/workspace/zintori_proto2/hoge/osumi.jar");// AIのクラスが入っているjarファイルの位置指定
			urls[1] = new URL(
					"file:/Users/koba/Documents/workspace/zintori_proto2/hoge/test_AI_B.jar");// AIのクラスが入っているjarファイルの位置指定
			AI_name[1] = "osumi.AI_class";// パッケージ名.クラス名
			AI_name[2] = "test_AI_B.AI_class";// パッケージ名.クラス名
			
			ClassLoader parent = ClassLoader.getSystemClassLoader();// クラスローダ作成
			URLClassLoader urlClassLoader = new URLClassLoader(urls, parent);// 外部jarを読み込むためのＵＲＬクラスローダ作成

			// /playerの作成
			for (int i = 1; i < player_num + 1; i++) {
				AI_class[i] = urlClassLoader.loadClass(AI_name[i]);// AI_classにjarから読み込んだPlayerクラスを設定
				myPlayer[i] = AI_class[i].newInstance();// AI_classからインスタンス生成
				set_info[i] = AI_class[i].getMethod("set_info", int.class,
						int.class);// AI_classが持つinfo_setメソッドを設定
				get_way[i] = AI_class[i].getMethod("get_way");// AI_classが持つget_wayメソッドを設定
				set_object_map[i] = AI_class[i].getMethod("set_object_map",
						int[][].class);// AI_classが持つset_neighbor_dataメソッドを設定
				set_color_map[i] = AI_class[i].getMethod("set_color_map",
						int[][].class);// AI_classが持つset_neighbor_colorメソッドを設定
				set_status[i] = AI_class[i].getMethod("set_status", int.class);// AI_classが持つset_statusメソッドを設定
				set_way[i] = AI_class[i].getMethod("set_way", int[].class);// AI_classが持つset_wayメソッドを設定
				set_color[i] = AI_class[i].getMethod("set_color", int[].class);// AI_classが持つset_grid_colorメソッドを設定
				set_position_data[i] = AI_class[i].getMethod(
						"set_position_data", int[].class, int[].class);// AI_classが持つset_position_dataメソッドを設定
				set_event_data[i] = AI_class[i].getMethod("set_event_data",
						int.class, int.class);// AI_classが持つset_event_dataメソッドを設定
				action[i] = AI_class[i].getMethod("action");// AI_classが持つactionメソッドを設定

				set_info[i].invoke(myPlayer[i], i, player_num - 1);
				
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		// /////////////////////////////////////////////////////////////////////////////////////////////

		clear();
		for (int i = 1; i < player_num + 1; i++) {
			save_AI(turn,i);
		}
		save_event(turn);
		// スレッドを開始する
		t = new Thread(this);
		t.start();

	}

	// 実行
	public void run() {
		try {
			while (game_finish == false) {
				if (turn <= 500) {
					for (int i = 1; i < player_num + 1; i++) {
						if(player_status[i]!=MATCHLESS){
							player_status[i]=NO;					
						}else{
							event_count++;
						}
						if(player_status[i]==MATCHLESS&&event_count>=20){
							event_on=true;
							player_status[i]=NO;
						}
					}
					if(event_on==true){
						event_on=false;
						create_event();
					}
					
					for (int i = 1; i < player_num + 1; i++) {
	
					
						give_data(i);// 周辺のデータをプレイヤーに与える
						action[i].invoke(myPlayer[i]);// プレイヤーのAIの思考メソッド
						move(i);// プレイヤーA移動
						change_map();// マップ情報変更
						event_check(i);
						change_map();// マップ情報変更
						save_AI(turn+1,i);
						
					}
					save_event(turn+1);
					
					repaint();// 描画
					turn++;
					
					Thread.sleep(50);
				} else {	
					save_obstacle();
					write_AI_JSON();
					game_finish = true;
					Thread.sleep(100);
				}
			}
		} catch (Exception e) {
		}
	}

	// 描画更新
	public void update(Graphics g) {
		paint(g);
	}

	// 描画
	public void paint(Graphics g) {
		// バッファを描画する
		if (back == null) {
			back = createImage(800, 600);
		}
		buffer = back.getGraphics();
		buffer.setFont(new Font("Serif", Font.ITALIC, 20));
		buffer.setColor(Color.white);
		buffer.fillRect(0, 0, 800, 600);
		buffer.drawImage(field, 0, 0, this);
		color_draw(buffer);
		if (event_type == LANDMINE) {
			buffer.setColor(Color.blue);
			event = "爆";
		} else if (event_type == WARP) {
			buffer.setColor(Color.green);
			event = "飛";
		} else if (event_type == MATCHLESS) {
			buffer.setColor(Color.black);
			event = "強";
		}
		buffer.drawString("" + event, event_x * 20, event_y * 20);
		for (int i = 1; i < player_num + 1; i++) {
			player_draw(buffer, i);
		}

		buffer.drawString("turn=" + turn, 10, 20);
		for (int i = 1; i < player_num + 1; i++) {
			buffer.drawString("Player" + i + "=" + player_color_num[i], 10,
					20 + i * 20);
		}

		g.drawImage(back, 0, 0, this);// 一度backにすべて描くことでちらつきを防止
	}

	void player_draw(Graphics buffer, int ID) {// IDのplayerを描画
		Color color = new Color(220, 255, 0);
		if (ID == 1) {
			color = new Color(220, 255, 0);
		} else if (ID == 2) {
			color = new Color(255, 50, 50);
		}
		buffer.setColor(color);
		buffer.fillOval(player_x[ID] * 20, player_y[ID] * 20, 20, 20);

	}

	void color_draw(Graphics buffer) {
		Color color;
		for (int i = 0; i < color_map.length; i++) {
			for (int j = 0; j < color_map[i].length; j++) {
				if (color_map[i][j] == A_COLOR) {
					color = new Color(180, 215, 0);
					buffer.setColor(color);
					buffer.fillRect(i * 20, j * 20, 20, 20);
				} else if (color_map[i][j] == B_COLOR) {
					color = new Color(160, 50, 50);
					buffer.setColor(color);
					buffer.fillRect(i * 20, j * 20, 20, 20);
				}
			}
		}

	}

	int[] neighbor_data(int x, int y) {// xとyを引数に渡せば上下左右四方のマップ情報を返す
		int data[] = new int[5];
		if (y != 0) {
			data[1] = object_map[x][y - 1];
		} else {
			data[1] = object_map[x][GRID_Y - 1];
		}
		if (x != GRID_X - 1) {
			data[2] = object_map[x + 1][y];
		} else {
			data[2] = object_map[0][y];
		}
		if (y != GRID_Y - 1) {
			data[3] = object_map[x][y + 1];
		} else {
			data[3] = object_map[x][0];
		}
		if (x != 0) {
			data[4] = object_map[x - 1][y];
		} else {
			data[4] = object_map[GRID_X - 1][y];
		}
		return data;
	}

	void change_map() {// マップ更新
		for (int i = 0; i < object_map.length; i++) {
			for (int j = 0; j < object_map[i].length; j++) {
				if (object_map[i][j] > 0) {
					object_map[i][j]=0;
				}
			}
		}
		
		for (int i = 1; i < player_num + 1; i++) {
			player_color_num[i] = 0;
			color_map[player_x[i]][player_y[i]] = i;
			object_map[player_x[i]][player_y[i]] = i;
		}
		

		for (int i = 0; i < color_map.length; i++) {
			for (int j = 0; j < color_map[i].length; j++) {
				if (color_map[i][j] > 0) {
					player_color_num[color_map[i][j]]++;
				}
			}
		}
	}
	void give_data(int ID) {// IDのplayerにデータを与える
		try {
			set_object_map[ID].invoke(myPlayer[ID], object_map);
			set_color_map[ID].invoke(myPlayer[ID], color_map);
			set_status[ID].invoke(myPlayer[ID], player_status[ID]);
			set_way[ID].invoke(myPlayer[ID], player_way);
			set_color[ID].invoke(myPlayer[ID], player_color_num);
			set_position_data[ID].invoke(myPlayer[ID], player_x, player_y);
			set_event_data[ID].invoke(myPlayer[ID], event_x, event_y);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	void move(int ID) {// IDのplayerを移動させる

		int data[] = new int[5];
		int way = 0;
		try {
			way = (Integer) get_way[ID].invoke(myPlayer[ID]);// Aが次に動きたい方向をクラスmyPlayer_Aから教えてもらう
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		if (way != 0) {
			player_stop[ID]=false;
			player_way[ID] = way;
		}else{
			player_stop[ID]=true;
		}
		data = neighbor_data(player_x[ID], player_y[ID]);// dataにAの四方の情報を代入

		if (way == UP) {
			if (player_y[ID] == 0) {
				if (data[UP] == 0) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_y[ID] = GRID_Y - 1;
				}else if(data[UP] == -1){  
					player_stop[ID]=true;
				}else if (data[UP] >= 1) {
				
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_y[ID] = GRID_Y - 1;
					// 戦闘判定//
					battle(ID, player_way[ID], data[UP], player_way[data[UP]]);
					// ////////
				}
			} else {
				if (data[UP] == 0) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_y[ID]--;
				}else if(data[UP] == -1){  
					player_stop[ID]=true;
				} else if (data[UP] >= 1) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_y[ID]--;
					// 戦闘判定//
					battle(ID, player_way[ID], data[UP], player_way[data[UP]]);
					// ////////
				}
			}
		} else if (way == DOWN) {
			if (player_y[ID] == GRID_Y - 1) {
				if (data[DOWN] == 0) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_y[ID] = 0;
				}else if(data[DOWN] == -1){  
					player_stop[ID]=true;
				} else if (data[DOWN] >= 1) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_y[ID] = 0;
					// 戦闘判定//
					battle(ID, player_way[ID], data[DOWN],
							player_way[data[DOWN]]);
					// ////////
				}
			} else {
				if (data[DOWN] == 0) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_y[ID]++;
				}else if(data[DOWN] == -1){  
					player_stop[ID]=true;
				} else if (data[DOWN] >= 1) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_y[ID]++;
					// 戦闘判定//
					battle(ID, player_way[ID], data[DOWN],
							player_way[data[DOWN]]);
					// ////////
				}
			}

		} else if (way == LEFT) {
			if (player_x[ID] == 0) {
				if (data[LEFT] == 0) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_x[ID] = GRID_X - 1;
				}else if(data[LEFT] == -1){  
					player_stop[ID]=true;
				} else if (data[LEFT] >= 1) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_x[ID] = GRID_X - 1;
					// 戦闘判定//
					battle(ID, player_way[ID], data[LEFT],
							player_way[data[LEFT]]);
					// ////////
				}
			} else {

				if (data[LEFT] == 0) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_x[ID]--;
				}else if(data[LEFT] == -1){  
					player_stop[ID]=true;
				} else if (data[LEFT] >= 1) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_x[ID]--;
					// 戦闘判定//
					battle(ID, player_way[ID], data[LEFT],
							player_way[data[LEFT]]);
					// ////////
				}
			}
		} else if (way == RIGHT) {
			if (player_x[ID] == GRID_X - 1) {
				if (data[RIGHT] == 0) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_x[ID] = 0;
				}else if(data[RIGHT] == -1){  
					player_stop[ID]=true;
				} else if (data[RIGHT] >= 1) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_x[ID] = 0;
					// 戦闘判定//
					battle(ID, player_way[ID], data[RIGHT],
							player_way[data[RIGHT]]);
					// ////////
				}
			} else {
				if (data[RIGHT] == 0) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_x[ID]++;
				}else if(data[RIGHT] == -1){  
					player_stop[ID]=true;
				} else if (data[RIGHT] >= 1) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_x[ID]++;
					// 戦闘判定//
					battle(ID, player_way[ID], data[RIGHT],
							player_way[data[RIGHT]]);
					// ////////
				}
			}
		} else {// 1ターン停止
			player_stop[ID]=true;
		}
	}

	void battle(int player_A, int player_A_way, int player_B, int player_B_way) {// 戦闘判定
		player_rival[player_A]=player_B;
		if(player_status[player_A]==MATCHLESS){
			color_explode(player_x[player_A],player_y[player_A],player_A);
			player_battle[player_A]=WIN;
			player_x[player_B] = player_first_x[player_B];
			player_y[player_B] = player_first_y[player_B];
		}else if(player_status[player_B]==MATCHLESS){
			color_explode(player_x[player_B],player_y[player_B],player_B);
			player_battle[player_A]=LOSE;
			player_x[player_A] = player_first_x[player_A];
			player_y[player_A] = player_first_y[player_A];
		}
		else if (Math.abs(player_A_way - player_B_way) == 1||Math.abs(player_A_way - player_B_way) == 2||Math.abs(player_A_way - player_B_way) == 3) {// 正面衝突
			if (player_color_num[player_A] < player_color_num[player_B]) {//Aの勝ち
				color_explode(player_x[player_A],player_y[player_A],player_A);
				player_battle[player_A]=WIN;
				player_x[player_B] = player_first_x[player_B];
				player_y[player_B] = player_first_y[player_B];
			} else if (player_color_num[player_A] > player_color_num[player_B]) {//Bの勝ち
				color_explode(player_x[player_B],player_y[player_B],player_B);
				player_battle[player_A]=LOSE;
				player_x[player_A] = player_first_x[player_A];
				player_y[player_A] = player_first_y[player_A];
			} else {//Aの勝ち
				color_explode(player_x[player_A],player_y[player_A],player_A);
				player_battle[player_A]=WIN;
				player_x[player_B] = player_first_x[player_B];
				player_y[player_B] = player_first_y[player_B];
			}
		} else {// 背後から Aの勝ち
			color_explode(player_x[player_A],player_y[player_A],player_A);
			player_battle[player_A]=WIN;
			player_x[player_B] = player_first_x[player_B];
			player_y[player_B] = player_first_y[player_B];
		}
	}

	void color_explode(int x,int y,int color) {// xとyを中心に3マスをカラーで塗る
		for(int i=x-3;i<=x+3;i++){
			if(0<=i&&i<GRID_X){
				for(int j=y-3;j<=y+3;j++){
					if(0<=j&&j<GRID_Y){
						color_map[i][j]=color;
					}
				}
			}
		}
	}

	void create_event() {
		event_x = rnd.nextInt(GRID_X);
		event_y = rnd.nextInt(GRID_Y);
		event_type = rnd.nextInt(3) + 1;
		event_born=turn+1;
	}

	void event_check(int ID) {
		if (player_x[ID] == event_x && player_y[ID] == event_y) {
			if (event_type == LANDMINE) {
				color_explode(event_x,event_y,NO_COLOR);
				player_status[ID]=LANDMINE;
			event_on=true;
			} else if (event_type == WARP) {
				player_status[ID]=WARP;
				warp(ID);
				event_on=true;
			} else if (event_type == MATCHLESS) {
				event_count=0;
				player_status[ID]=MATCHLESS;
				event_x=-1;
				event_y=-1;
			}
			
		}
	}

	void warp(int ID) {
		object_map[player_x[ID]][player_y[ID]] = 0;
		player_x[ID] = rnd.nextInt(GRID_X);
		player_y[ID] = rnd.nextInt(GRID_Y);
		while(object_map[player_x[ID]][player_y[ID]]!=0){
			player_x[ID] = rnd.nextInt(GRID_X);
			player_y[ID] = rnd.nextInt(GRID_Y);
		}
		object_map[player_x[ID]][player_y[ID]] = ID;
	}

	void clear() {
		create_event();
		int check=0;
		player_x[1] = rnd.nextInt(GRID_X);
		player_y[1] = rnd.nextInt(GRID_Y);
		for (int i = 2; i < player_num + 1; i++) {
			check=0;
			while(check==0){
				check=0;
				player_x[i] = rnd.nextInt(GRID_X);
				player_y[i] = rnd.nextInt(GRID_Y);
				for (int j = 1; j < i; j++) {
					if(1>=Math.abs(player_x[i]-player_x[j])+Math.abs(player_y[i]-player_y[j])){
						check++;
					}
				}
			}
		}
		for (int i = 1; i < player_num + 1; i++) {
			player_first_x[i]=player_x[i];
			player_first_y[i]=player_y[i];
			player_way[i] = 0;
			player_color_num[i] = 0;
		}
		for (int i = 0; i < OBSTACLE_NUM; i++) {
			obstacle_x[i]=-1;
			obstacle_y[i]=-1;
			if(0<=obstacle_x[i]&&obstacle_x[i]<GRID_X&&0<=obstacle_y[i]&&obstacle_y[i]<GRID_Y){
				object_map[obstacle_x[i]][obstacle_y[i]]=-1;
			}
		}
		
	}
	
	void save_AI(int turn,int ID){
		data_AI[turn][ID-1][0]=player_x[ID];
		data_AI[turn][ID-1][1]=player_y[ID];
		if(player_stop[ID]==true){
			data_AI[turn][ID-1][2]=UNDER;
		}else{
			data_AI[turn][ID-1][2]=player_way[ID];
		}
		data_AI[turn][ID-1][3]=player_status[ID];
		data_AI[turn][ID-1][4]=player_battle[ID];
		if(player_battle[ID]==NO){
			data_AI[turn][ID-1][5]=-1;
		}else{
			data_AI[turn][ID-1][5]=player_rival[ID]-1;
		}
		data_AI[turn][ID-1][6]=player_color_num[ID];
		player_battle[ID]=NO;
	}
	
	void save_event(int turn){
		data_event[turn][0]=event_born;
		data_event[turn][1]=event_type;
		data_event[turn][2]=event_x;
		data_event[turn][3]=event_y;
	}

	void save_obstacle(){
		for (int i = 0; i < OBSTACLE_NUM; i++) {
			data_obstacle[i][0]=obstacle_x[i];
			data_obstacle[i][1]=obstacle_y[i];
		}
	}
	
	void write_AI_JSON() {
		System.out.println("終了");
		Gson gson = new Gson();
/*
		try (
			JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter("/Users/koba/Documents/workspace/zintori_proto2/hoge/AI.json")))) {     
			    gson.toJson(new JsonPrimitive(gson.toJson(data_AI)), writer);
			    
			} catch (IOException ex) {
			    ex.printStackTrace();
			}

		try (
			JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter("/Users/koba/Documents/workspace/zintori_proto2/hoge/Event.json")))) {     
			    gson.toJson(new JsonPrimitive(gson.toJson(data_event)), writer);
			    
			} catch (IOException ex) {
			    ex.printStackTrace();
			}
		
		try (
				JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter("/Users/koba/Documents/workspace/zintori_proto2/hoge/Obstacle.json")))) {     
				    gson.toJson(new JsonPrimitive(gson.toJson(data_obstacle)), writer);
				    
				} catch (IOException ex) {
				    ex.printStackTrace();
				}
		*/
		System.out.println("終了");
	 
	}

}
