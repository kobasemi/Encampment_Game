<?php



if($_SERVER["REQUEST_METHOD"] === "POST"){
  if($_FILES["userfile"]["error"] == UPLOAD_ERR_OK){

    var_dump($_FILES["userfile"]);
    $tempfile = $_FILES["userfile"]["tmp_name"];
    $filename = $_FILES["userfile"]["name"];
    //   $filename = mb_convert_encoding($filename, "cp932", "utf8"); //�����R�[�h�ϊ�
    $result = move_uploaded_file($tempfile, "./test/".$filename); //�t�@�C���ۑ�
    if($result == TRUE){
      $message ="�t�@�C���̃A�b�v���[�h�ɐ������܂���";
    }
    else{
      $message ="�t�@�C���̈ړ��Ɏ��s���܂���";
    }
  }
  elseif($_FILES["userfile"]["error"] == UPLOAD_ERR_NO_FILE) {
    $message ="�t�@�C�����A�b�v���[�h����܂���ł���";
  }
  else {
    $message ="�t�@�C���̃A�b�v���[�h�Ɏ��s���܂���";
  }
  echo $message;
}
?>





