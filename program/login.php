<?php
$password = $_POST["password"];
$username = $_POST["username"];
$lastdate = (int)date("YmdGis");
$error = array();

if($password == "" || $username == "") {

  array_push($error, "form blank");
} else {


  require_once('cfg.php');

  $mysqli = new mysqli($db['host'], $db['user'], $db['pass'],$db['dbname']);

    $mysqli->set_charset("utf-8");
    if ($mysqli->connect_error) {
    die('Connect Error (' . $mysqli->connect_errno . ') '
        . $mysqli->connect_error);
    }

  $passquery =  $mysqli->query("SELECT pass FROM all_users where username = '".$username."'");
   $row = $passquery->fetch_array(MYSQLI_ASSOC);

  if(!array_key_exists('pass', $row)){
    array_push($error, "1No match Username or Password");
  }else{
    if($row['pass'] == $password){
 /*OK*/
      $stmt = $mysqli->prepare("UPDATE all_users SET last = ? where username = ?");

      $stmt->bind_param('is',$lastdate,$username);
       $stmt->execute();

       session_start();

      $_SESSION['username'] = $username;
      $_SESSION['lastdate'] = $lastdate;

      var_dump($_SESSION);


      //     header("Location: "."./logout.html");

    }else{
  /*パスワード不一致*/

      array_push($error, "2No match Username or Password");


    }


  }

}

?>
