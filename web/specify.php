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

<img src="./css/picture/scammer2.jpg" alt="EGBA.picture" width="48px" height="48px">
<p>EGBA</p>
</div>

<form>
<button class="logout" formaction="./logout.php" type="submit" name="Log-out" value="Log-out">Log-out</button>
<button class="help" formaction="./help.php" type="submit" name="help"
 value="help">HELP</button>
</form>

</div>


<p class="specifyform">対戦相手のNAMEを記入してください</p>

<div class="centerconer">

<form method="post" action="specifypost.php">
<input type="text" name="user" >
<input type="submit" value="sent">
</form>

</div>

</body>
</html>
