var AI_Log; // AI情報配列Iname : Playersarray[0]
var Event_Log; // マップイベント情報配列
var Obst_Log; // 障害物情報配列
var Playersarray;

function LoadJson() {
	var data = document.location.search.substring(1);
	data = data.replace("+","");
	Playersarray = data.split("/");

	var Players = data.split("/").length - 1;

	document.write("<div class=box>");
	for (var i = 0; i < Players + 1; i++) {
		document.write("<font color=" + AI_color[i] + "><p>" + (i + 1) + ":    " + Playersarray[i] + "</p></font>");
	}
	document.write("</div>");
	
	
	document.write("<div class=box2 id=math_num>");
	for (var i = 0; i < Players + 1; i++) {
		document.write("<p>0</p>");
	}
	document.write("</div>");
	
	
	
	AI_num = Players+1;
	

	$(function() {
		$.ajax({
			type : "POST",
			url : "http://www.egba.top/zintori_ver2/",
			data : {
				AIname : Playersarray
			},
			dataType : "json",
			success : function(json) {
				AI_Log = json.data_AI;
				Event_Log = json.data_event;
				Obst_Log = json.data_obstacle;
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("エラーが発生しました：" + textStatus + ":\n" + errorThrown);
			}
		});
	});
}
