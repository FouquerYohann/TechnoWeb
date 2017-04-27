<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>

		<title>Leafter</title>
		<link href="css/enregistrement.css" rel='stylesheet' type='text/css'/>
		<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
		<script src="js/main.js" type="text/javascript"></script>
		<link rel="icon" href="jpg/leaf.ico"/>
	</head>
	<body>

		<div id="formu">
		<h1>Inscription</h1>
			<form method="POST" action="javascript:function(){return;}">
				<div class="champs">
					<span>Pr&eacute;nom</span><input type='text' name='prenom' id='prenom'/>
					<div class="error" style='display:none;'></div>
				</div>
				<div class="champs">
					<span>Nom</span><input type='text' name='nom' id='nom'/>
					<div class="error" style='display:none;'></div>
				</div>
				<div class="champs">
					<span>Login</span><input type='text' name='login' id='login'/>
					<div class="error" style='display:none;'></div>
				</div>
				<div class="champs">
					<span>Mot de Passe</span><input type='password' name='pass' id='password'/>
					<div class="error" style='display:none;'></div>
				</div>
				<div class="champs">
					<span>Confirmation</span><input type='password' name='conf_pass' id='conf_pass'/>
					<div class="error" style='display:none;'></div>
				</div>
				<div id='button'>
					<input type='submit' value='Enregistrer' id='enregistre'  />
				
					<input type="button" value="Annuler" onclick="window.location='connexion.jsp'"/>
				</div>
				
				
								
			</form>
			
			
		</div>
		
			<script src='js/enregistrement.js' type='text/javascript'></script>
	</body>