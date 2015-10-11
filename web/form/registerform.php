<?php
  /* フォームからメールアドレスを取得 */

$email = $_POST["email"];
$password = $_POST["password"];
$username = $_POST["username"];
$email = mysql_escape_string($email);
$dirname = mysql_escape_string("/home/".$username);
$lastdate = (int)date("YmdGis");
/* エラーメッセージ配列 */
$error = array();
/* データベースに接続 */
//require_once("db.php");
if($email == "" || $password == "" || $username == "") {
  array_push($error, "form blank");
} else {
  require_once('cfg.php');
  $mysqli = new mysqli($db['host'], $db['user'], $db['pass'],$db['dbname']);
  $mysqli->set_charset("utf-8");
  if ($mysqli->connect_error) {
    die('Connect-Error (' . $mysqli->connect_errno . ') ' . $mysqli->connect_error);
  }
  $email = $mysqli ->real_escape_string($email);
  $preuser =  $mysqli->query("SELECT num FROM all_users where username = '".$username."'");
  $sql = "SELECT num FROM all_users where mail ='". $email . "'" ;
  $preemail = $mysqli -> query($sql);
  $row1 = $preemail->fetch_array(MYSQLI_NUM);
  $row2 = $preuser->fetch_array(MYSQLI_NUM);

  if($row1[0] != NULL ||  $row2[0] != NULL){
    echo "既にそのメールアドレス or ユーザネームは登録されています\n";


    echo '5秒後に元のページにジャンプします。<br />5秒経ってもジャンプしない場合は以下のURLをクリックしてください。<br /> <a href="../register.php">../register.php</a>';
    
    echo  '<meta http-equiv="refresh" content="5;URL=../register.php">';




  }else{
    $stmt = $mysqli->prepare("INSERT INTO all_users VALUES( ?, ?, ?, ?, ?, ?)");
    $result = $mysqli->query("SELECT MAX(num) as max  FROM all_users");
    $maxnum = 0;
    if($result){
      //1行ずつ取り出し
      while($row = $result->fetch_object()){
	//エスケープして表示
	$num = $row->max;
	$maxnum = $num+1;
      }
    }else{

      $maxnum=1;
    }

    $scoreseed = 1000;

    $stmt->bind_param('isssii',$maxnum, $username, $password,$email,$scoreseed,$lastdate);
    $stmt->execute();
    $mysqli->close();

    mkdir("/var/www/html/user/".$username."/", 0777,true);
    mkdir("/var/www/html/user/".$username."/algo/", 0777,true);
    exec("cp /var/www/html/user/default/AI.jar". " /var/www/html/user/".$username."/".$username.".jar");
    session_start();
    
    $_SESSION['username']=$username;
    $_SESSION['lastdate']=$lastdate;
    echo  '<meta http-equiv="refresh" content="1;URL=../fight.php">';

  }
}

?>
