<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Leafter</title>
		<link href='css/connexion.css' rel='stylesheet' type='text/css'/>
		<script src="http://code.jquery.com/jquery-1.11.0.min.js" type="text/javascript"></script>
		<script src="js/connexion.js" type='text/javascript'></script>
		<script src="js/main.js" type="text/javascript"></script>
		<script type="text/javascript">main();</script>
		<link rel="icon" href="jpg/leaf.ico"/>
	</head>
	
	<body>
		<div id="connect">
			<h1>Ouvrir une session</h1>
			<form id="formulaire_connexion" method="post" >
			<div class="ids" ><span>Login</span><input type="text" name="login" id="login" /></div><!-- faire un onfocus joli-->
			<div class="ids" ><span>Mot de passe</span><input type="password" name="pass" id="password" /></div>
			<div class="buttons">
				<input type="submit" value="Connexion" id="connexion" />
				
				<input type="button" value="SignUp" onclick="window.location='enregistrement.jsp'" />
			</div>
			</form>	
		</div>
	</body>
	</html>