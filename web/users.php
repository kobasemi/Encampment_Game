<?php
function users($kazu){

 $_SERVER['battleusers'] = NULL;

  $username=$_SESSION['username'];


  require_once('./form/cfg.php');
  $mysqli = new mysqli($db['host'], $db['user'], $db['pass'],$db['dbname']);
  $mysqli->set_charset("utf-8");
  if ($mysqli->connect_error) {
    die('Connect-Error (' . $mysqli->connect_errno . ') ' . $mysqli->connect_error);
  }

  $mynum = 0;


  $result = $mysqli->query("SELECT num FROM all_users where username=\"$username\"");
 

  while ($row = $result->fetch_assoc()) {
    $mynum=$row["num"];
  } 
  
  $result = $mysqli->query("SELECT Max(num) as mx FROM all_users");
  $maxnum = 0;

  while ($row = $result->fetch_assoc()) {
    $maxnum=$row["mx"];
  }




  
  $battleusers = $username;
  $temp2 = 0;
  $temparray[]=$mynum;
  $temp =0;
  
  for($i=0 ; $i <= $kazu-1;){
    $temp2=0;
    $temp=0;
    $temp = mt_rand(1, $maxnum);
    echo "temp = ".$temp;
    
   var_export($temp);

    if(array_search($temp,$temparray)===false){      
      $result = $mysqli->query("SELECT username FROM all_users where num=$temp");
      
      //if($result){
        //1行ずつ取り出し
        while($row = $result->fetch_assoc()){
          //エスケープして表示
          $temp2 = $row["username"];
	}

	if($temp2){
	  $battleusers=$battleusers.".".$temp2;
	  $_SERVER['battleusers']=$battleusers;
	 var_export($temparray);
	 
	  array_push($temparray,$temp);
	  $i++;
	}
	 
	
 }
  }
  return  $battleusers;
  }
?>