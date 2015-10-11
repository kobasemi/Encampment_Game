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
<button class="aisample" formaction="./download.php" type="submit" name="help">Sample</button>
<button class="upload" formaction="./AI.php" type="submit" name="upload">UPLOAD</button>
</form>

</div>


<div class="fight">
<form>


<button class="fightin1" formaction="./commingsoon.php" type="submit" name="help"
  value="help">TEAM VS
  (comming soon)</button>
<button class="fightin2" formaction="./fightin2.php" type="submit" name="help"
 value="help">1 VS 1</button>
<button class="fightin3" formaction="./specify.php" type="submit" name="help"
 value="help">Specify</button>
<button class="fightin4" formaction="./multi.php" type="submit" name="help"
 >Multi</button>
</form>
</div>

</body>
</html>
