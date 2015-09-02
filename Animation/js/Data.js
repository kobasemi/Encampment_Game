var AI_Log; // AI情報配列
var Event_Log; // マップイベント情報配列
var Obst_Log; // 障害物情報配列

function LoadJson() {

	var data = document.location.search.substring(1);
	alert(data);
	var Playersarray = data.split("/");
	alert(Playersarray[0]);

	var Players = data.split("/").length - 1;
	alert(Players);

	document.write("<div class=box>");

	for (var i = 0; i < Players + 1; i++) {
		document.write("<p>" + (i + 1) + ":    " + Playersarray[i] + "</p>");
	}

	document.write("</div>");

	$(function() {
		$.ajax({
			type : "POST",
			url : "http://10.1.4.166/zintori2",
			data : {
				AIname : Playersarray
			},
			dataType : "json",
			success : function(json) {
				AI_Log = json.AI_Log;
				Event_Log = json.Event_Log;
				Obst_Log = json.Obst_Log;
				LoadBGM();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("エラーが発生しました：" + textStatus + ":\n" + errorThrown);
			}
		});
	});
}