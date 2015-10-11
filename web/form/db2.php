<?php
  /* フォームからメールアドレスを取得 */

require("JSON.php");

$input = file_get_contents('php://input');

//JSON のデコード                                                                                                                                                                         
$json = new Services_JSON;
$data = $json->decode($input);

exec("touch unk.txt");


$username=array();
$score=array();

//以下確認用                                                                                                                                                                                
header('Content-type: text/plain; charset=utf-8');

foreach($data as $key => $val) {
  $username[]=$key;
  $score[]=$key;
}


$error = array();

  require_once('cfg.php');
  $mysqli = new mysqli($db['host'], $db['user'], $db['pass'],$db['dbname']);
  $mysqli->set_charset("utf-8");
  if ($mysqli->connect_error) {
    die('Connect-Error (' . $mysqli->connect_errno . ') ' . $mysqli->connect_error);
  }
 
for ($i = 0 ; $i < count($username); $i++) {
  $prescore =  $mysqli->query("SELECT num FROM all_users where username = '".$username[$i]."'");
  $row1 = $prescore->fetch_array(MYSQLI_NUM);
 
  if($row1[0] != NULL){

   $newscore = $row1[0]+$score[$i];
   $scoreup =  $mysqli->query("UPDATE all_users SET score=".$newscore." where username = '".$username[$i]."'");

  }else{

    echo "error";

  }
}


?>
