package zintori_ver2;

import java.util.HashMap;

public class Data_json_PHP {
	
	HashMap<String,Integer> map=new HashMap<String,Integer>();
	
	void put(String ai_name, int player_score){
		map.put(ai_name, player_score);
	}
	
}
