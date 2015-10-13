<?php
session_start();


if(empty($_SESSION['username'])||empty($_SESSION['lastdate'])){
  header("Location: ./login.php");
}

if($_SESSION['username']==NULL||$_SESSION['lastdate']==NULL){

  header("Location: ./login.php");

}

?>


<html>
<head>

<title>EGBA</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="./css/default.css">
</head>
<body class="body4">


<div class="header">
<div class="EGBAbox">

<a href="fight.php"><img src="./css/picture/scammer2.jpg" alt="EGBA.picture" width="48px" height="48px" border="0"></a>
<p>EGBA</p>
</div>

<form>
<button class="logout" formaction="./logout.php" type="submit" name="Log-out">Log-out</button>
<button class="help" formaction="./help.php" type="submit" name="help">HELP</button>
<button class="ranking" formaction="./ranking.php" type="submit" name="Log-out">Ranking</button>
<button class="aisample" formaction="./licence.php" type="submit" name="help">README</button>
<button class="upload" formaction="./AI.php" type="submit" name="upload">UPLOAD</button>
</form>


</div>
<div class="helpmoji">

</br></br>

<h1>サンプルダウンロード</h1>
<p>sample.txt <a href="/Download/sample.txt">ダウンロード</a></p>
 <p>sample.txt <a href="/user/a1/a1.jar">ダウンロード</a></p>
<p>zip <a href="/Download/html.zip">ダウンロード</a></p>



<h1>ルール概要</h1>
  <p>AI はあらかじめ用意されたグリッド(大きさ未定)にランダムに配置され、決められたターン数(500 ターン)自由に動き回ることが出来る</p><p>AI が通った跡は AI の色に塗りつぶ されていく。最終的に、最も多くのマスを自分の色に塗りつぶした AI の勝利となる。</p>
<h1>戦闘</h1>
<p> 複数のAIが同じマスに移動した場合、戦闘が開始される。  </p>


</p><p>勝敗要素として、まずAIの方向を利用する。AIの前面と、AI の側面・後面がぶつかったとき、前面からぶつかったAIが勝利</p><p>が勝利となる。AI どうしが正面からぶつかった場合、その時に持っているマスが少ない方のAIの勝利となる。<p>
<p> AIの勝利となる。勝利したAIは周囲3マスを自分の色に塗りつぶすことが出来る。敗北したAIはランダムな地点に飛ばされる。  </p>

<h1>対戦環境 </h1>
<p>
対戦環境は20×20の正方形を組み合わせたグリッド。グリッドの端はもう片方の端とつながっており、ループするようになっている。</p>
<h1>イベントマス</h1>
<p>  イベントマスとはグリッド上にランダムに X ターン(未定)出現し、そのマスを通ったAIに何らかの作用をもたらすマスのことで゙ある</p>
<p>現段階で構想している、イベントマスに以下のようなものが</h1>
<h2>地雷マス</h2>
<p>周囲3マスを白に戻す。</p>
<h2>無敵マス</h2>
<p>
踏んだAIを20ターン無敵にし、戦闘で必ず勝利するようになる。
</p>

<h2>
ワーフマス
</h2>
<p>踏んだAIを別の場所に移動させる(ランダムな場所か、あらかじめ決めた場所かは未定)。
</p>


<h1></h1>
<h1></h1>



</body>
</html>
