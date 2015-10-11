<?php
session_start();


if(empty($_SESSION['username'])||empty($_SESSION['lastdate'])){
  header("Location: ./login.php");
}

if($_SESSION['username']==NULL||$_SESSION['lastdate']==NULL){

  header("Location: ./login.php");

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

<img src=\"./css/picture/scammer2.jpg\" alt=\"EGBA.picture\" width=\"48px\" height=\"48px\">
<p>EGBA</p>
</div>

<form>
<button class=\"logout\" formaction=\"./logout.php\" type=\"submit\" name=\"Log-out\">Log-out</button>
<button class=\"help\" formaction=\"./help.php\" type=\"submit\" name=\"help\">HELP</button>
<button class=\"ranking\" formaction=\"./ranking.php\" type=\"submit\" name=\"Log-out\">Ranking</button>
<button class=\"aisample\" formaction=\"./download.php\" type=\"submit\" name=\"help\">Sample</button>
<button class=\"upload\" formaction=\"./AI.php\" type=\"submit\" name=\"upload\">UPLOAD</button>
</form>


</div><div class=\"centerAI\">";


$dir="/var/www/html/user/".$_SESSION['username']."/";


if( is_dir( $dir ) && $handle = opendir( $dir ) )
  {
    while( ($file = readdir($handle)) !== false )
      {
	if( filetype( $path = $dir . $file ) == "file" )
	  {
	    

	    $stat = stat($dir.$file);

	    $aaa = date('Y/m/d H:i:s',  $stat['atime']);

	    echo "<p class=\"mainAI\">ファイル名:".$file."    更新日:".$aaa."</p>";
	  }
      }
  }



$dir="/var/www/html/user/".$_SESSION['username']."/algo/";


if( is_dir( $dir ) && $handle = opendir( $dir ) )
  {
    while( ($file = readdir($handle)) !== false )
      {
        if( filetype( $path = $dir . $file ) == "file" )
          {
            

            $stat = stat($dir.$file);

            $aaa= date('Y/m/d H:i:s',  $stat['atime']);

	    echo "<p class=\"subAI\">ファイル名:".$file."    更新日:".$aaa."</p>";

          }
      }
  }



echo "
<form>
<button class=\"upbutton\" formaction=\"./upload.php\" type=\"submit\" name=\"up\" value=\NEWUPLOAD\">NEW AI UPLOAD</button
>

</form>



</div>
<div class=\"centerconer\">


</div>

</body>
</html>";



?>