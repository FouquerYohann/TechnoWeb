<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Leafter</title>
	<meta charset='utf-8' />
	<link href="css/accueil.css" rel='stylesheet' type='text/css' />
	<script src="js/main.js" type='text/javascript'></script>
	<script src="js/connexion.js" type='text/javascript'></script>
	<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
	<script src="lib_js/mustache.js" type="text/javascript"></script>
	<script src="js/box-friend.js" type="text/javascript"></script>
	<link href='https://fonts.googleapis.com/css?family=Indie+Flower' rel='stylesheet' type='text/css'>
	<link href='https://fonts.googleapis.com/css?family=Candal' rel='stylesheet' type='text/css'>
	<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet' type='text/css'>
	<link rel="icon" href="jpg/leaf.ico"/>
</head>
<body>

	<nav><!-- utilisation d'une balise nav pour la section "top", comporte en 
		general une barre de recherche, un menu et une zone de connexion -->
		<div id="top">
			<div id="logo">
				<img src='jpg/green_leaf.ico' style="width: 5em; height: 5em;" />
			</div>
			<div id="search">
				
				<form id='formulaire_private_message' action=''>
					<span> Message privee</span>
					<!--dans action, mettre package.nomServlet.truc renvoyant vers le service demander -->
					<input type='text' id='destinataire_search' placeholder='Id Destinataire' />
					<input type='text' id='author_search' placeholder='Id Author' />
					<input type='submit' value='ok'/> 
				</form>
			
				
				<form id='formulaire_recherche_person' action=''>
					<span> Vous cherchez quelqu'un ?</span>
					<!--dans action, mettre package.nomServlet.truc renvoyant vers le service demander -->
					<input type='text' id='prenom_search' placeholder='Prenom' />
					<input type='text' id='nom_search' placeholder='Nom' />
				</form>
			</div>
			<div id="connecte" ></div>
		</div>
	</nav>

	<script type="text/javascript">div_connexion();</script>

	<div id="left">
		<div class="listview" data-role="listview" id="box-friend">
			<div class="list-group">
				<span class="list-group-toggle"> Ma boite d'amis !</span>
				<img id="charger_box_friend" src='jpg/refresh_blue.ico'/>
				<ul id="list-friend">
				</ul>
			</div>
		</div>
	</div>



	<div id="main"> 
	
		<div id="search_message">  
			<form  onsubmit="list_post(this);return false;" action="">
				<input type="text" id="author" placeholder="ID Auteur"></input>
				<input type="text" id="destinataire" placeholder="ID Destinataire"></input>    
				<input type="text" id="recherche" placeholder="Contenant"></input>
				<input type='image'alt='Submit' value='' src="jpg/magnifying_glass.ico"></input>
			</form>
		</div>
		<div id="pourtout">
		</div>
		<div id="posts">
				
		</div>
	</div>
</body>
<script>
	document.getElementsByTagName('body').onclose='logout(env.key);';
</script>


</html>

