<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ログイン</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="./css/default.css">
</head>
<body class="body1">
<div class="header">

<div class="EGBAbox">


<a href="index.php"><img src="./css/picture/scammer2.jpg" alt="EGBA.picture" width="48px" height="48px" border="0"></a>

<p>EGBA</p>
</div>

<form>
<button class="signin" formaction="./login.php" type="submit" name="Sign-up" value="Sign-in">Sign-in</button>
<button class="signup" formaction="./register.php" type="submit" name="Sign-in" value="Sign-up">Sign-up</button>
<button class="help" formaction="./help.php" type="submit" name="help" value="help">HELP</button>
<button class="aisample" formaction="./licence.php" type="submit" name="ai" value="up">README</button>

</form>

</div>

<form class="form1" action="./form/loginform.php" method="post"><input type="hidden" name="mode" value="email_regist" />
  
  <h1>Login Form</h1>
  <p class="white">UserName</p><input type="text" name="username" size="40" required/></br>
  <p class="white">PassWord</p>      <input type="password" name="password" size="40" required/></br></br>
	<input class="btn" type="submit" name="submit" value="ログイン" />
</form>


</body>
</html>
