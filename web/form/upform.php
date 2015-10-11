<?php
session_start();


if(empty($_SESSION['username'])||empty($_SESSION['lastdate'])){
  header("Location: ./login.php");

}

if($_SESSION['username']==NULL||$_SESSION['lastdate']==NULL){

  header("Location: ./login.php");
  
}


if($_SERVER["REQUEST_METHOD"] === "POST"){
  if($_FILES["userfile"]["error"] == UPLOAD_ERR_OK){

    
    $tempfile = $_FILES["userfile"]["tmp_name"];
    $filename = $_FILES["userfile"]["name"];

    /*
    $dir_s="/var/www/html/user/".$_SESSION['username']."/";
 
    //ディレクトリ内のファイルを取り出す
    $filelist=scandir($dir_s);
 
    //ファイル数をチェック
    $count=count($filelist);
 
    for($i=0; $i<$count; $i++){
     
      
      $file=pathinfo($filelist[$i]);
      $file_name=$file["basename"];


      if($file_name!="."||$file_name!=".."){
	$file_ext=$file["extension"];
 
     
	if($file_ext=="jar"){
	  $cpfile = $file_name;
	}

      }
    }
    */
    $time = time();

    exec('cp /var/www/html/user/'.$_SESSION['username'].'/'.$_SESSION['username'].".jar".' /var/www/html/user/'.$_SESSION['username'].'/algo/'.$time.'.jar' );
    exec('rm -rf /var/www/html/user/'.$_SESSION['username'].'/'.$_SESSION['username'].".jar");
    $result = move_uploaded_file($tempfile, "/var/www/html/user/".$_SESSION['username']."/".$_SESSION['username'].".jar"); //ファイル保存
    if($result == TRUE){
      header("Location: ../AI.php");
    }
    else{
      echo "ファイルの移動に失敗しました";
    }
  }
  elseif($_FILES["userfile"]["error"] == UPLOAD_ERR_NO_FILE) {
    echo "ファイルがアップロードされませんでした";
  }
  else {
    echo "ファイルのアップロードに失敗しました";
    
  }
  
}


?>


