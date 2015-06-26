package zintori_proto;

import java.awt.*;
import java.applet.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JFrame;

public class Main extends Applet implements Runnable {
	Image back;
	Graphics buffer;
	int player_num = 2;
	// //AIをつくるための入れ物たち////
	String AI_name[] = new String[player_num];
	Object myPlayer[] = new Object[player_num];
	Class<?> AI_class[] = new Class<?>[player_num];
	Method set_info[] = new Method[player_num];
	Method get_way[] = new Method[player_num];
	Method set_neighbor_data[] = new Method[player_num];
	Method set_neighbor_color[] = new Method[player_num];
	Method set_neighbor_status[] = new Method[player_num];
	Method set_grid_color[] = new Method[player_num];
	Method set_my_data[] = new Method[player_num];
	Method set_event_data[] = new Method[player_num];
	Method action[] = new Method[player_num];
	
	URL[] urls = new URL[3];
	// //////////////////////////
	Thread t;
	Image field = getToolkit().getImage("field.gif");
//定数
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
	//その他
	final int GRID_X=20;
	final int GRID_Y=20;
	
//変数
	private int[] player_x = new int[player_num];
	private int[] player_y = new int[player_num];
	private int[] player_status = new int[player_num];
	private int[] player_color_num = new int[player_num+1];
	
	private int event_x=0;//未実装
	private int event_y=0;//未実装

	int object_map[][] = { // マップ情報（プレイヤー位置等）
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1 } };

	int color_map[][] = { //色情報
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1 } };
	
	int status_map[][] = { //ステータス情報
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
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }};
			

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

		// ////内部のクラスであれば以下のような処理はいらないが、外部jarからAIを読み込む必要があるため、若干複雑な処理が必要//////
		try {
			urls[0] = new URL("file:/Users/koba/Documents/workspace/zintori_proto2/hoge/test_AI_A.jar");// AIのクラスが入っているjarファイルの位置指定
			urls[1] = new URL("file:/Users/koba/Documents/workspace/zintori_proto2/hoge/test_AI_B.jar");// AIのクラスが入っているjarファイルの位置指定
			AI_name[0] = "test_AI_A.Player_A";// パッケージ名.クラス名
			AI_name[1] = "test_AI_B.Player_B";// パッケージ名.クラス名
			ClassLoader parent = ClassLoader.getSystemClassLoader();// クラスローダ作成
			URLClassLoader urlClassLoader = new URLClassLoader(urls, parent);// 外部jarを読み込むためのＵＲＬクラスローダ作成

			// /playerの作成
			for (int i = 0; i < player_num; i++) {
				AI_class[i] = urlClassLoader.loadClass(AI_name[i]);// AI_classにjarから読み込んだPlayerクラスを設定
				myPlayer[i] = AI_class[i].newInstance();// AI_classからインスタンス生成
				set_info[i] = AI_class[i].getMethod("set_info", int.class,int.class);// AI_classが持つinfo_setメソッドを設定
				get_way[i] = AI_class[i].getMethod("get_way");// AI_classが持つget_wayメソッドを設定
				set_neighbor_data[i] = AI_class[i].getMethod(
						"set_neighbor_data", int[].class);// AI_classが持つset_neighbor_dataメソッドを設定
				set_neighbor_color[i] = AI_class[i].getMethod(
						"set_neighbor_color", int[].class);// AI_classが持つset_neighbor_colorメソッドを設定
				set_neighbor_status[i] = AI_class[i].getMethod(
						"set_neighbor_status", int[].class);// AI_classが持つset_neighbor_statusメソッドを設定
				set_grid_color[i] = AI_class[i].getMethod("set_grid_color",int[].class);// AI_classが持つset_grid_colorメソッドを設定
				set_my_data[i] = AI_class[i].getMethod("set_my_data",int.class,int.class,int.class);// AI_classが持つset_my_dataメソッドを設定
				set_event_data[i] = AI_class[i].getMethod("set_event_data",int.class,int.class);// AI_classが持つset_event_dataメソッドを設定
				action[i] = AI_class[i].getMethod("action");// AI_classが持つactionメソッドを設定
				
				set_info[i].invoke(myPlayer[i], i+1,player_num-1);
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

		// スレッドを開始する
		t = new Thread(this);
		t.start();
		
		player_x[0]=1;
		player_y[0]=1;
		player_x[1]=18;
		player_y[1]=13;

	}

	// 実行
	public void run() {
		try {
			while (true) {
				for(int i=0;i<player_num;i++){
				give_data(i);// 周辺のデータをプレイヤーに与える
				action[i].invoke(myPlayer[i]);// プレイヤーのAIの思考メソッド
				move(i);// プレイヤーA移動
				change_map();// マップ情報変更
				//System.out.println("eeeeee");
				}

				repaint();// 描画
				Thread.sleep(100);
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
		buffer.drawImage(field, 0, 0, this);
		color_draw(buffer);
		for(int i=0;i<player_num;i++){
			player_draw(buffer,i);
		}

		g.drawImage(back, 0, 0, this);// 一度backにすべて描くことでちらつきを防止

	}

	int[] neighbor_data(int x, int y) {// xとyを引数に渡せば上下左右四方のマップ情報を返す
		int data[] = new int[5];
		data[1] = object_map[x][y-1];
		data[2] = object_map[x+1][y];
		data[3] = object_map[x][y+1];
		data[4] = object_map[x-1][y];
		return data;
	}

	int[] neighbor_color(int x, int y) {// xとyを引数に渡せば上下左右四方の色情報を返す
		int data[] = new int[5];
		data[1] = color_map[x][y-1];
		data[2] = color_map[x+1][y];
		data[3] = color_map[x][y+1];
		data[4] = color_map[x-1][y];
		return data;
	}
	
	int[] neighbor_status(int x, int y) {// xとyを引数に渡せば上下左右四方のステータス情報を返す
		int data[] = new int[5];
		data[1] = status_map[x][y-1];
		data[2] = status_map[x+1][y];
		data[3] = status_map[x][y+1];
		data[4] = status_map[x-1][y];
		return data;
	}

	void change_map() {// マップ更新
		for (int i = 0; i < player_color_num.length; i++) {
				player_color_num[i]=0;
		}
		color_map[player_x[0]][player_y[0]] = A_COLOR;
		color_map[player_x[1]][player_y[1]] = B_COLOR;

		object_map[player_x[0]][player_y[0]] = PLAYER_A;
		object_map[player_x[1]][player_y[1]] = PLAYER_B;
		
		for (int i = 0; i < color_map.length; i++) {
			for (int j = 0; j < color_map[i].length; j++) {
				if(color_map[i][j]>0){
					player_color_num[color_map[i][j]]++;
				}
			}
		}
		System.out.println(""+player_color_num[1]);
	}

	void give_data(int ID) {// IDのplayerにデータを与える
		try {
			set_neighbor_data[ID].invoke(myPlayer[ID],
					neighbor_data(player_x[ID], player_y[ID]));
			set_neighbor_color[ID].invoke(myPlayer[ID],
					neighbor_color(player_x[ID], player_y[ID]));
			set_neighbor_status[ID].invoke(myPlayer[ID],
					neighbor_status(player_x[ID], player_y[ID]));
			set_grid_color[ID].invoke(myPlayer[ID],
					player_color_num);
			set_my_data[ID].invoke(myPlayer[ID],
					player_x[ID],player_y[ID],player_status[ID]);
			set_event_data[ID].invoke(myPlayer[ID],
					event_x,event_y);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	void move(int ID) {//IDのplayerを移動させる
		int data[] = new int[5];
		int a = 0;
		try {
			a = (Integer) get_way[ID].invoke(myPlayer[ID]);// Aが次に動きたい方向をクラスmyPlayer_Aから教えてもらう
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		data = neighbor_data(player_x[ID], player_y[ID]);// dataにAの四方の情報を代入

		if (a == UP) {
			if(data[UP] == 0){
				object_map[player_x[ID]][player_y[ID]] = 0;
				player_y[ID]--;
			}else if(player_y[ID]==1&&object_map[player_x[ID]][GRID_Y-2] == 0){
				object_map[player_x[ID]][player_y[ID]] = 0;
				player_y[ID]=GRID_Y-2;
			}
		} else if (a == DOWN) {
			if(data[DOWN] == 0){
				object_map[player_x[ID]][player_y[ID]] = 0;
				player_y[ID]++;
			}else if(player_y[ID]==GRID_Y-2&&object_map[player_x[ID]][1] == 0){
				object_map[player_x[ID]][player_y[ID]] = 0;
				player_y[ID]=1;
			}
		} else if (a == LEFT) {
			if(data[LEFT] == 0){
				object_map[player_x[ID]][player_y[ID]] = 0;
				player_x[ID]--;
			}else if(player_x[ID]==1&&object_map[GRID_X-2][player_y[ID]] == 0){
				object_map[player_x[ID]][player_y[ID]] = 0;
				player_x[ID]=GRID_X-2;
			}
		} else if (a == RIGHT) {
			if(data[RIGHT] == 0){
				object_map[player_x[ID]][player_y[ID]] = 0;
				player_x[ID]++;
			}else if(player_x[ID]==GRID_X-2&&object_map[1][player_y[ID]] == 0){
				object_map[player_x[ID]][player_y[ID]] = 0;
				player_x[ID]=1;
			}
		} else {// 1ターン停止

		}
	}

	void player_draw(Graphics buffer,int ID) {//IDのplayerを描画
		Color color = new Color(220, 255, 0);
		if(ID==0){
			color = new Color(220, 255, 0);
		}else if(ID==1){
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

}
