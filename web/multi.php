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


<p class="multiform">何人対戦ですか？</p>

<div class="centerconer">
<form action="./multipost.php" method="post">
<select name="team">
<option value="2">2</option>
<option value="3">3</option>
<option value="4">4</option>
<option value="5">5</option>
<option value="6">6</option>
<option value="7">7</option>
<option value="8">8</option>
<option value="9">9</option>
<option value="10">10</option>

</select>

<input type="submit" value="Go" >

</div>

</body>
</html>
