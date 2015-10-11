<?php
session_start();
$_SESSION = array();
session_destroy();

var_dump($_SESSION);

header("Location: ..logout.html/");
?>




