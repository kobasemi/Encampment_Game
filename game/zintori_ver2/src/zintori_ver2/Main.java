package zintori_ver2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

/**
 * Servlet implementation class hello
 */
@WebServlet("/Main")
public class Main extends HttpServlet {

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

	URL[] urls = new URL[player_num + 1];
	// //////////////////////////
	Thread t;

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
	// 色情報（マイナスは着色不可場所）
	final int NO_COLOR = -1;
	final int NORMAL_COLOR = 0;
	// その他
	final int GRID_X = 20;
	final int GRID_Y = 20;
	final int TURN = 500;
	// イベント情報
	public final int NOEVENT = 0;// 無し
	public final int LANDMINE = 1;// 地雷
	public final int WARP = 2;// ワープ
	public final int MATCHLESS = 3;// 無敵
	// 勝ち負け
	public final int NO = 0;// 無し
	public final int WIN = 1;// 勝ち
	public final int LOSE = 2;// 負け
	// 障害物
	final int OBSTACLE_NUM = 1;

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
	private int[] player_ranking = new int[player_num + 1];
	private int[] player_score = new int[player_num + 1];

	private int event_x = 0;//
	private int event_y = 0;//
	private int event_type = 0;//
	private int event_born = 0;//
	private int event_count = 0;//

	private int[] obstacle_x = new int[OBSTACLE_NUM];// 障害物のx
	private int[] obstacle_y = new int[OBSTACLE_NUM];// 障害物のy

	boolean game_finish = false;
	boolean event_on = false;

	String event = "";

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

	Data_json json;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");

		// 追加/////
		request.setCharacterEncoding("UTF-8");
		String ai_name[] = request.getParameterValues("AIname[]");
		player_num = ai_name.length;
		json = new Data_json();
		// ///////

		start(ai_name);

		// 追加////
		response.setContentType("application/json; charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "http://10.1.4.166"); // ここは個人のサーバ環境によって異なる．
		PrintWriter out_json = response.getWriter();
		
		out_json.println(JSON.encode(json));
		// ////////
		out_json.close();

		// ///DBに接続/////
				java.sql.Connection con = null;
				try {
					Class.forName("com.mysql.jdbc.Driver");

					con = DriverManager.getConnection(
							"jdbc:mysql://127.0.0.1/all_tables", "root", "asdf4jkl;8");

					java.sql.Statement stmt = con.createStatement();
					String sql;
					ResultSet rs = null;
					for(int i=0;i<player_num;i++){
						sql = "SELECT score FROM all_users where username='"+ai_name[i]+"'";
						rs = stmt.executeQuery(sql);
						// テーブル照会結果を取得
						while (rs.next()) {
							player_score[i+1] = rs.getInt("score");
						}
					}
					
					for(int i=0;i<500;i++){
						json.data_AI[i][1][6] =100;
					}
					
					// データ加工
					int max_num_player = 0;
					for (int i = 1; i < player_num + 1; i++) {
						max_num_player = i;
						for (int j = 1; j < player_num + 1; j++) {
							if (player_color_num[max_num_player] < player_color_num[j]) {
								max_num_player = j;
							}
						}
						player_color_num[max_num_player] = 0;
						player_ranking[i] = max_num_player;
					}
					score_write(ai_name.length);
					
					//データ更新
					for(int i=0;i<player_num;i++){
						if(player_score[i+1]<=0){
							player_score[i+1]=0;
						}else if(player_score[i+1]>=9999){
							player_score[i+1]=9999;
						}
						sql = "UPDATE all_users SET score="+player_score[i+1]+" where username='"+ai_name[i]+"'";
			            stmt.executeUpdate(sql);
					}
					
					for(int i=0;i<500;i++){
						json.data_AI[i][1][6] =200;
					}
					
					// データベースのクローズ
					rs.close();
					stmt.close();
					con.close();
				} catch (SQLException e) {
				} catch (ClassNotFoundException e) {
				}

				// ///////DBここまで

	}
	
	void score_write(int player_num){
		if(player_num==2){
			player_score[player_ranking[1]]=player_score[player_ranking[1]]+2;
			player_score[player_ranking[2]]=player_score[player_ranking[2]]-1;
		}else if(player_num==3){
			player_score[player_ranking[1]]=player_score[player_ranking[1]]+2;
			player_score[player_ranking[2]]=player_score[player_ranking[2]]+0;
			player_score[player_ranking[3]]=player_score[player_ranking[3]]-1;
		}else if(player_num==4){
			player_score[player_ranking[1]]=player_score[player_ranking[1]]+3;
			player_score[player_ranking[2]]=player_score[player_ranking[2]]+2;
			player_score[player_ranking[3]]=player_score[player_ranking[3]]-1;
			player_score[player_ranking[4]]=player_score[player_ranking[4]]-2;
		}else if(player_num==5){
			player_score[player_ranking[1]]=player_score[player_ranking[1]]+3;
			player_score[player_ranking[2]]=player_score[player_ranking[2]]+2;
			player_score[player_ranking[3]]=player_score[player_ranking[3]]+0;
			player_score[player_ranking[4]]=player_score[player_ranking[4]]-1;
			player_score[player_ranking[5]]=player_score[player_ranking[5]]-2;
		}else if(player_num==6){
			player_score[player_ranking[1]]=player_score[player_ranking[1]]+4;
			player_score[player_ranking[2]]=player_score[player_ranking[2]]+3;
			player_score[player_ranking[3]]=player_score[player_ranking[3]]+2;
			player_score[player_ranking[4]]=player_score[player_ranking[4]]-1;
			player_score[player_ranking[5]]=player_score[player_ranking[5]]-2;
			player_score[player_ranking[6]]=player_score[player_ranking[6]]-3;
		}else if(player_num==7){
			player_score[player_ranking[1]]=player_score[player_ranking[1]]+4;
			player_score[player_ranking[2]]=player_score[player_ranking[2]]+3;
			player_score[player_ranking[3]]=player_score[player_ranking[3]]+2;
			player_score[player_ranking[4]]=player_score[player_ranking[4]]+0;
			player_score[player_ranking[5]]=player_score[player_ranking[5]]-1;
			player_score[player_ranking[6]]=player_score[player_ranking[6]]-2;
			player_score[player_ranking[7]]=player_score[player_ranking[7]]-3;
		}else if(player_num==8){
			player_score[player_ranking[1]]=player_score[player_ranking[1]]+5;
			player_score[player_ranking[2]]=player_score[player_ranking[2]]+4;
			player_score[player_ranking[3]]=player_score[player_ranking[3]]+3;
			player_score[player_ranking[4]]=player_score[player_ranking[4]]+2;
			player_score[player_ranking[5]]=player_score[player_ranking[5]]-1;
			player_score[player_ranking[6]]=player_score[player_ranking[6]]-2;
			player_score[player_ranking[7]]=player_score[player_ranking[7]]-3;
			player_score[player_ranking[8]]=player_score[player_ranking[8]]-4;
		}else if(player_num==9){
			player_score[player_ranking[1]]=player_score[player_ranking[1]]+5;
			player_score[player_ranking[2]]=player_score[player_ranking[2]]+4;
			player_score[player_ranking[3]]=player_score[player_ranking[3]]+3;
			player_score[player_ranking[4]]=player_score[player_ranking[4]]+2;
			player_score[player_ranking[5]]=player_score[player_ranking[5]]+0;
			player_score[player_ranking[6]]=player_score[player_ranking[6]]-1;
			player_score[player_ranking[7]]=player_score[player_ranking[7]]-2;
			player_score[player_ranking[8]]=player_score[player_ranking[8]]-3;
			player_score[player_ranking[9]]=player_score[player_ranking[9]]-4;
		}else if(player_num==10){
			player_score[player_ranking[1]]=player_score[player_ranking[1]]+6;
			player_score[player_ranking[2]]=player_score[player_ranking[2]]+5;
			player_score[player_ranking[3]]=player_score[player_ranking[3]]+4;
			player_score[player_ranking[4]]=player_score[player_ranking[4]]+3;
			player_score[player_ranking[5]]=player_score[player_ranking[5]]+2;
			player_score[player_ranking[6]]=player_score[player_ranking[6]]-1;
			player_score[player_ranking[7]]=player_score[player_ranking[7]]-2;
			player_score[player_ranking[8]]=player_score[player_ranking[8]]-3;
			player_score[player_ranking[9]]=player_score[player_ranking[9]]-4;
			player_score[player_ranking[10]]=player_score[player_ranking[10]]-5;
		}
	}

	// Reader でのテキスト読み込み(※最後に改行が付くので編集用には向かない。エンコードもシステム依存)
	public static final String readLinesText(InputStream is) throws IOException {
		final StringBuilder str = new StringBuilder();
		final String sep = System.getProperty("line.separator"); // 改行コード
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			while (br.ready()) {
				str.append(br.readLine()); // 終端文字は含まない
				str.append(sep); // 改行コード追加
			}
			return str.toString();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				// IOException
			}
			str.setLength(0);
		}
	}

	// 初期化
	public void start(String ai[]) {

		// ////内部のクラスであれば以下のような処理はいらないが、外部jarからAIを読み込む必要があるため、若干複雑な処理が必要//////
		try {

			for (int i = 1; i < player_num + 1; i++) {
				urls[i - 1] = new URL("file:/var/www/html/user/" + ai[i - 1]
						+ "/" + ai[i - 1] + ".jar");// AIのクラスが入っているjarファイルの位置指定
				AI_name[i] = ai[i - 1] + ".AI_class";// パッケージ名.クラス名
			}

			ClassLoader parent = this.getClass().getClassLoader(); // クラスローダ作成
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
			save_AI(0, i);
		}
		save_event(0);
		// ゲームを開始する
		for (int i = 0; i < TURN; i++) {
			game_play(i);

		}
		save_obstacle();

	}

	// 実行
	public void game_play(int turn) {

		for (int i = 1; i < player_num + 1; i++) {
			if (player_status[i] != MATCHLESS) {
				player_status[i] = NO;
			} else {
				event_count++;
			}
			if (player_status[i] == MATCHLESS && event_count >= 20) {
				event_on = true;
				player_status[i] = NO;
			}
		}
		if (event_on == true) {
			event_on = false;
			create_event(turn + 1);
		}
		for (int i = 1; i < player_num + 1; i++) {

			try {

				give_data(i);// 周辺のデータをプレイヤーに与える
				action[i].invoke(myPlayer[i]);// プレイヤーのAIの思考メソッド
				move(i);// プレイヤーA移動
				change_map();// マップ情報変更
				event_check(i);
				change_map();// マップ情報変更
				save_AI(turn + 1, i);
				save_event(turn + 1);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		turn++;

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
					object_map[i][j] = 0;
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
			player_stop[ID] = false;
			player_way[ID] = way;
		} else {
			player_stop[ID] = true;
		}
		data = neighbor_data(player_x[ID], player_y[ID]);// dataにAの四方の情報を代入

		if (way == UP) {
			if (player_y[ID] == 0) {
				if (data[UP] == 0) {
					object_map[player_x[ID]][player_y[ID]] = 0;
					player_y[ID] = GRID_Y - 1;
				} else if (data[UP] == -1) {
					player_stop[ID] = true;
				} else if (data[UP] >= 1) {

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
				} else if (data[UP] == -1) {
					player_stop[ID] = true;
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
				} else if (data[DOWN] == -1) {
					player_stop[ID] = true;
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
				} else if (data[DOWN] == -1) {
					player_stop[ID] = true;
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
				} else if (data[LEFT] == -1) {
					player_stop[ID] = true;
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
				} else if (data[LEFT] == -1) {
					player_stop[ID] = true;
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
				} else if (data[RIGHT] == -1) {
					player_stop[ID] = true;
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
				} else if (data[RIGHT] == -1) {
					player_stop[ID] = true;
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
			player_stop[ID] = true;
		}
	}

	void battle(int player_A, int player_A_way, int player_B, int player_B_way) {// 戦闘判定
		player_rival[player_A] = player_B;
		if (player_status[player_A] == MATCHLESS) {
			color_explode(player_x[player_A], player_y[player_A], player_A);
			player_battle[player_A] = WIN;
			player_x[player_B] = player_first_x[player_B];
			player_y[player_B] = player_first_y[player_B];
		} else if (player_status[player_B] == MATCHLESS) {
			color_explode(player_x[player_B], player_y[player_B], player_B);
			player_battle[player_A] = LOSE;
			player_x[player_A] = player_first_x[player_A];
			player_y[player_A] = player_first_y[player_A];
		} else if (Math.abs(player_A_way - player_B_way) == 1
				|| Math.abs(player_A_way - player_B_way) == 2
				|| Math.abs(player_A_way - player_B_way) == 3) {// 正面衝突
			if (player_color_num[player_A] < player_color_num[player_B]) {// Aの勝ち
				color_explode(player_x[player_A], player_y[player_A], player_A);
				player_battle[player_A] = WIN;
				player_x[player_B] = player_first_x[player_B];
				player_y[player_B] = player_first_y[player_B];
			} else if (player_color_num[player_A] > player_color_num[player_B]) {// Bの勝ち
				color_explode(player_x[player_B], player_y[player_B], player_B);
				player_battle[player_A] = LOSE;
				player_x[player_A] = player_first_x[player_A];
				player_y[player_A] = player_first_y[player_A];
			} else {// Aの勝ち
				color_explode(player_x[player_A], player_y[player_A], player_A);
				player_battle[player_A] = WIN;
				player_x[player_B] = player_first_x[player_B];
				player_y[player_B] = player_first_y[player_B];
			}
		} else {// 背後から Aの勝ち
			color_explode(player_x[player_A], player_y[player_A], player_A);
			player_battle[player_A] = WIN;
			player_x[player_B] = player_first_x[player_B];
			player_y[player_B] = player_first_y[player_B];
		}
	}

	void color_explode(int x, int y, int color) {// xとyを中心に3マスをカラーで塗る
		for (int i = x - 3; i <= x + 3; i++) {
			if (0 <= i && i < GRID_X) {
				for (int j = y - 3; j <= y + 3; j++) {
					if (0 <= j && j < GRID_Y) {
						color_map[i][j] = color;
					}
				}
			}
		}
	}

	void create_event(int turn) {
		event_x = rnd.nextInt(GRID_X);
		event_y = rnd.nextInt(GRID_Y);
		event_type = rnd.nextInt(3) + 1;
		event_born = turn;
	}

	void event_check(int ID) {
		if (player_x[ID] == event_x && player_y[ID] == event_y) {
			if (event_type == LANDMINE) {
				color_explode(event_x, event_y, NO_COLOR);
				player_status[ID] = LANDMINE;
				event_on = true;
			} else if (event_type == WARP) {
				player_status[ID] = WARP;
				warp(ID);
				event_on = true;
			} else if (event_type == MATCHLESS) {
				event_count = 0;
				player_status[ID] = MATCHLESS;
				event_x = -1;
				event_y = -1;
			}

		}
	}

	void warp(int ID) {
		object_map[player_x[ID]][player_y[ID]] = 0;
		player_x[ID] = rnd.nextInt(GRID_X);
		player_y[ID] = rnd.nextInt(GRID_Y);
		while (object_map[player_x[ID]][player_y[ID]] != 0) {
			player_x[ID] = rnd.nextInt(GRID_X);
			player_y[ID] = rnd.nextInt(GRID_Y);
		}
		object_map[player_x[ID]][player_y[ID]] = ID;
	}

	void clear() {
		event_on = false;
		create_event(0);
		int check = 0;
		player_x[1] = rnd.nextInt(GRID_X);
		player_y[1] = rnd.nextInt(GRID_Y);
		for (int i = 2; i < player_num + 1; i++) {
			check = 0;
			while (check == 0) {
				check = 0;
				player_x[i] = rnd.nextInt(GRID_X);
				player_y[i] = rnd.nextInt(GRID_Y);
				for (int j = 1; j < i; j++) {
					if (1 <= Math.abs(player_x[i] - player_x[j])
							+ Math.abs(player_y[i] - player_y[j])) {
						check++;
					}
				}
			}
		}
		for (int i = 0; i < color_map.length; i++) {
			for (int j = 0; j < color_map[i].length; j++) {
				color_map[i][j] = 0;
			}
		}
		for (int i = 0; i < object_map.length; i++) {
			for (int j = 0; j < object_map[i].length; j++) {
				object_map[i][j] = 0;
			}
		}

		for (int i = 1; i < player_num + 1; i++) {
			player_first_x[i] = player_x[i];
			player_first_y[i] = player_y[i];
			player_way[i] = 0;
			player_status[i] = NO;
			player_color_num[i] = 0;
			player_battle[i] = NO;
			player_ranking[i] = 0;
			player_score[i] = 0;
		}
		for (int i = 0; i < OBSTACLE_NUM; i++) {
			obstacle_x[i] = -1;
			obstacle_y[i] = -1;
			if (0 <= obstacle_x[i] && obstacle_x[i] < GRID_X
					&& 0 <= obstacle_y[i] && obstacle_y[i] < GRID_Y) {
				object_map[obstacle_x[i]][obstacle_y[i]] = -1;
				color_map[obstacle_x[i]][obstacle_y[i]] = -1;
			}
		}

	}

	void save_AI(int turn, int ID) {
		json.data_AI[turn][ID - 1][0] = player_x[ID];
		json.data_AI[turn][ID - 1][1] = player_y[ID];
		if (player_stop[ID] == true) {
			json.data_AI[turn][ID - 1][2] = UNDER;
		} else {
			json.data_AI[turn][ID - 1][2] = player_way[ID];
		}
		json.data_AI[turn][ID - 1][3] = player_status[ID];
		json.data_AI[turn][ID - 1][4] = player_battle[ID];
		if (player_battle[ID] == NO) {
			json.data_AI[turn][ID - 1][5] = -1;
		} else {
			json.data_AI[turn][ID - 1][5] = player_rival[ID] - 1;
		}
		json.data_AI[turn][ID - 1][6] = player_color_num[ID];
		player_battle[ID] = NO;
	}

	void save_event(int turn) {
		json.data_event[turn][0] = event_born;
		json.data_event[turn][1] = event_type;
		json.data_event[turn][2] = event_x;
		json.data_event[turn][3] = event_y;
	}

	void save_obstacle() {
		for (int i = 0; i < OBSTACLE_NUM; i++) {
			json.data_obstacle[i][0] = obstacle_x[i];
			json.data_obstacle[i][1] = obstacle_y[i];
		}

	}

}