<?php
session_start();
require "users.php";

$kazu=3;

$users = users($kazu);

print<<<AAAA


<html>
<head>

<title>EGBA</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="./css/default.css">
   <script type="text/javascript" src="js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="js/loading.js"></script>
</head>
<body class="body7">


<div id="loading"><img src="css/picture/loading.gif"></div>
<div id="container">

<div class="header">

<div class="EGBAbox">

<img src="./css/picture/scammer2.jpg" alt="EGBA.picture" width="48px" height="48px">
<p>EGBA</p>
</div>

</div>





AAAA;




$users=str_replace(".","/",$users);

echo "<script type=\"text/javascript\">";

/*echo "window.alert(\"BattleUSER SET $users\")";*/


echo "if (window.confirm(\"BattleUSER SET $users\")) {
location.href = \"index.html\";

}";

echo "</script>";
echo "</html>";

 
?>