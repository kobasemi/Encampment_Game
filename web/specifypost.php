<?php
session_start();
$_SERVER['battleusers'] = NULL;

$battleusers = $_SESSION["username"];

$searchusername = $_POST["user"];

  require_once('./form/cfg.php');
  $mysqli = new mysqli($db['host'], $db['user'], $db['pass'],$db['dbname']);
  $mysqli->set_charset("utf-8");
  if ($mysqli->connect_error) {
    die('Connect-Error (' . $mysqli->connect_errno . ') ' . $mysqli->connect_error);
  }

  $mynum = 0;


  $result = $mysqli->query("SELECT num FROM all_users where username=\"$searchusername\"");
 


$aaaaa = null;
  while ($row = $result->fetch_assoc()) {
    $aaaaa=$row["num"];
  } 
  
 
if($aaaaa==NULL){
  
  $battleusers="USER NOT FOUND";
  
  echo "<script type=\"text/javascript\">";
echo "if (window.confirm(\"BattleUSER SET $battleusers\")) {
location.href = \"specify.php\";
}";
echo "</script>";



}else{

  $battleusers=$battleusers."/".$searchusername;
  $_SERVER['battleusers']=$battleusers;
  echo "<script type=\"text/javascript\">";
echo "if (window.confirm(\"BattleUSER SET $battleusers\")) {
location.href = \"index.html\";
}";

echo "</script>";



}





?>