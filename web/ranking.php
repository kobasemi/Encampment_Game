<?php
session_start();


if(empty($_SESSION['username'])||empty($_SESSION['lastdate'])){
  header("Location: ./login.php");
}

if($_SESSION['username']==NULL||$_SESSION['lastdate']==NULL){

  header("Location: ./login.php");

}

require_once('./form/cfg.php');
$mysqli = new mysqli($db['host'], $db['user'], $db['pass'],$db['dbname']);
$mysqli->set_charset("utf-8");



if ($mysqli->connect_error) {
  echo "DB error"; 
}

echo "
<html>
<head>

<title>EGBA</title>
<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">
<link rel=\"stylesheet\" href=\"./css/default.css\">
</head>
<body class=\"body4\">


<div class=\"header\">
<div class=\"EGBAbox\">

<a href=\"fight.php\"><img src=\"./css/picture/scammer2.jpg\" alt=\"EGBA.picture\" width=\"48px\" height=\"48px\" border=\"0\"></a>
<p>EGBA</p>
</div>


<form>                                                                                                                                                   
<button class=\"logout\" formaction=\"./logout.php\" type=\"submit\" name=\"Log-out\">Log-out</button>                                                   
<button class=\"help\" formaction=\"./help.php\" type=\"submit\" name=\"help\">HELP</button>                                                             
<button class=\"ranking\" formaction=\"./ranking.php\" type=\"submit\" name=\"Log-out\">Ranking</button>                                                 
<button class=\"aisample\" formaction=\"./licence.php\" type=\"submit\" name=\"help\">README</button>                                                   
<button class=\"upload\" formaction=\"./AI.php\" type=\"submit\" name=\"upload\">UPLOAD</button>                                                         
</form>                                                                                                                                                  
                                                                                                                                                         

";



$scores=array("Null","Null","Null","Null","Null","Null","Null","Null","Null");
$usernames=array("Null","Null","Null","Null","Null","Null","Null","Null","Null");


$score =  $mysqli->query("SELECT score,username FROM all_users ORDER BY score DESC");


$i = 0;
while( $row = $score->fetch_assoc() ) {

  $usernames[$i] = $row[ 'username' ];
  //  echo $row[ 'username' ];                                                 echo "i=".$i."   $usernames[$i]"; 
  $scores[$i] = $row[ 'score' ];
  //echo $row[ 'score' ];                                                       
  $i = $i+1;
}








                                  

//ranking show                                                                  
echo "                                                                          
                                                                                
<div class=\"rankingbox\">                                                      
<p> 1位 : " .$usernames[0] ."さん:".$scores[0]."</p> 
<p>  2位 : " .$usernames[1] ."さん:".$scores[1]."   </p>
<p> 3位 : " .$usernames[2] ."さん:".$scores[2]."   </p>
<p> 4位 : " .$usernames[3] ."さん:".$scores[3]."</p>                            
<p>  5位 : " .$usernames[4] ."さん:".$scores[4]."   </p>  
<p>  6位 : " .$usernames[5] ."さん:".$scores[5]."   </p>  
<p> 7位 : " .$usernames[6] ."さん:".$scores[6]."</p>                           
<p>  8位 : " .$usernames[7] ."さん:".$scores[7]."   </p>                       
<p> 9位 : " .$usernames[8] ."さん:".$scores[8]."   </p>                         
                                                            
                                                                                
</div>";




echo "
</body>
</html>";



?>