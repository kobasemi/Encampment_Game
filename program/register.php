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

  var_dump($row1);
  var_dump($row2);


  if($row1[0] != NULL ||  $row2[0] != NULL){
    array_push($error, "Already Email or UserName");
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
  }

  $stmt->bind_param('issssi',$maxnum, $username, $password,$email,$dirname,$lastdate);
  $stmt->execute();

  $mysqli->close();
  }

}


if(count($error) > 0) {
  foreach($error as $value) {
?>
<table>
  <caption>Error</caption>
  <tr>
    <td class="item">Error：</td>
      <td><?php print $value; ?></td>
  </tr>
</table>
<?php
                                    }
} else {

  echo "登録完了";

}
?>
