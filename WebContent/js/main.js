function main() {
	env = {};
	env.users = [];
	env.current_user = [];
}

function isErreur(data) {
	
	if (data.error != undefined) {
		alert(data.error + " " + data.error_number);
		return true;
	}
	return false;
}

function User(id, login, contact) {
	this.id = id;
	this.login = login;
	this.contact = false;
	if (contact != undefined) {
		this.contact = contact;
	}
	env.users[this.id] = this;
	env.current_user[0] = this;
	this.actif = false;

}

User.prototype.modifStatus = function() {
	this.contact = !this.contact;
}

function Commentaire(id, auteur, texte, date, score, idcom) {
	this.id = id;
	this.auteur = auteur;
	this.texte = texte;
	this.date = date;
	this.score = score;
	this.idcom = idcom;
}

function Post(id, auteur, texte, date, score, idpost) {
	this.id = id;
	this.auteur = auteur;
	this.texte = texte;
	this.date = date;
	this.score = score;
	this._id = idpost;
}

function div_connexion() {
	var div_conn = document.getElementById('connecte');
	var s = "<div id='connecte'>";
	if (env.current_user.length != 0) {
		if (env.current_user[0].actif = true) {
			tmp_login = env.current_user[0].login;
			s += "<p>" + tmp_login + " est connect&eacute;(e).</p>";
			s += "<img src='jpg/power_png_v2.ico' onclick=logout(env.key); onmouseover=this.style.cursor='pointer';/>";
		} else {
			s += "<a href='connexion.jsp'>Connexion</a>"
			s += "<a href='enregistrement.jsp'>Enregistrement</a>"
		}
	} else {
		s += "<a href='connexion.jsp'>Connexion</a>"
		s += "<a href='enregistrement.jsp'>Enregistrement</a>"
	}
	s += "</div>";
	div_conn.outerHTML = s;
}

function list_post(form) {
	document.getElementById('pourtout').innerHTML="";

	var author = this.author.value;
	var destinataire = form.destinataire.value;
	var recherche = form.recherche.value;

	var template = " <h1>{{recherche}}</h1>"
			+ "<table>"
			+ "<tr>"
			+ "	<th>Destinaire</th>"
			+ "	<th>Author</th>"
			+ "	<th>Text</th>"
			+ "	<th>Date</th>"
			+ "	<th>Score</th>"
			+ "</tr>"
			+ "<tr>{{#Resultat.message}}"
			+ " <td> <strong>{{destinataire}}</strong></td>"
			+ " <td><ul>"
			+ " <li> {{author.login}} </li>"
			+ " <li> {{author.contact}} </li>"
			+ " </ul></td>"
			+ "<td><p> {{text}}</p></td>"
			+ "<td class='date'> <i>{{date}}</i></td>"
			+ "<td class='td_score'><img class='button_like' src='jpg/thumb_up.ico' onclick='likemessage(&quot;{{_id}}&quot;,1);return false;'/>"
			+ "<img class='button_dislike' src='jpg/thumb_down.ico' onclick='likemessage(&quot;{{_id}}&quot;,-1);return false;'/>"
			+ "<p id='{{_id}}p'>{{nblike}}</p>"
			+ " </td>"
			+ "<td>"
			+ "<img class='montrer_commentaire' src='jpg/eye.ico' onclick='showhidecomments(&quot;{{_id}}tr&quot;,&quot;show&quot;);return false;' />"
			+ "<img class='montrer_commentaire' src='jpg/eye-hidden.png' onclick='showhidecomments(&quot;{{_id}}tr&quot;,&quot;none&quot;);return false;' />"
			+ "<img class='ajoute_commentaire' src='jpg/add.ico' onclick='form_comment(&quot;{{_id}}&quot;); return false;' />"
			+ "<button onclick='deletemessage(&quot;{{_id}}&quot;);return false;'>Supprimer message</button>"
			+ "</td>"
			+ "</tr>{{#comments}}"
			+ "<tr id='les_commentaires_text'class='{{_id}}tr' style='display:none;'>"
			+ "<td> {{author.login}} {{author.contact}}</td>"
			+ "<td> {{text}}</td>"
			+ "<td class='td_score'><img class='button_like' src='jpg/thumb_up.ico' onclick='likecomment(&quot;{{_id}}&quot;,&quot;{{_idcomm}}&quot;,1);return false;'/>"
			+ "<img class='button_dislike' src='jpg/thumb_down.ico' onclick='likecomment(&quot;{{_id}}&quot;,&quot;{{_idcomm}}&quot;,-1);return false;'/>"
			+ "<p id='{{_idcomm}}p'>{{nblike}}</p>" + " </td>"
			+ "</tr> {{/comments}}" + "{{/Resultat.message}}" + "</table>";

	$.ajax({
		type : "GET",
		url : "/0000Fouquer_Rudat/listmessage",
		dataType : "json",
		data : {
			recherche : recherche,
			destinataire : destinataire,
			author_id : author
		},
		success : function(data) {

			if (!isErreur(data)) {
				var html = Mustache.to_html(template, data);

				$("#posts").html(html);
			}
		},
		error : function(jqXHR, textstatus, errorThrown) {
			alert(" le probleme est " + textstatus)

		}

	});

}

function likemessage(idmess, nb) {
	document.getElementById('pourtout').innerHTML="";
	$.ajax({
		type : "GET",
		url : "/0000Fouquer_Rudat/likemessage",
		data : {
			idmess : idmess,
			nb : nb
		},
		dataType : "json",
		success : function(data) {

			if (!isErreur(data)) {
				$('#' + idmess + "p").text(
						parseInt($('#' + idmess + "p").text()) + nb);
			}
		},
		error : function(jqXHR, textstatus, errorThrown) {
			alert(" le probleme est " + textstatus);

		}
	});
}

function likecomment(idmess, idcomm, nb) {

	$.ajax({
		type : "GET",
		url : "/0000Fouquer_Rudat/likecomment",
		data : {
			idmess : idmess,
			idcomm : idcomm,
			nb : nb
		},
		dataType : "json",
		success : function(data) {
			if (!isErreur(data)) {
				$('#' + idcomm + "p").text(
						parseInt($('#' + idcomm + "p").text()) + nb);
			}
		},
		error : function(jqXHR, textstatus, errorThrown) {
			alert(" le probleme est " + textstatus);

		}
	});
}

function addcomments(id) {
	alert("not implemented yet");
}

function showhidecomments(id, type) {
	$('.' + id).css("display", type);
}

function form_message(person_id) {
	var template = "<textarea rows='5' cols='28' maxlength='140' id='le_text' placeholder='Tapez votre message ici. Limite 140 lettres'></textarea>"
			+ "<button id='bouton_pourtout' onclick='add_post(&quot;"
			+ env.key
			+ "&quot;,"
			+ person_id + ");return false;'>Poster</button>";

	document.getElementById("pourtout").innerHTML = template;

}

function form_comment(idmess) {
	var template = "<textarea rows='5' cols='28' maxlength='140' id='le_text' placeholder='Tapez votre message ici. Limite 140 lettres'></textarea>"
			+ "<button id='bouton_pourtout' onclick='add_comments(&quot;"
			+ env.key
			+ "&quot;,&quot;"
			+ idmess
			+ "&quot;);return false;'>Poster</button>";

	document.getElementById("pourtout").innerHTML = template;

}

function add_post(key_session, destinataire) {
	var text = document.getElementById("le_text").value;
	$.ajax({
		type : "GET",
		url : "/0000Fouquer_Rudat/addmessage",
		data : {
			key_session : key_session,
			destinataire : destinataire,
			mess : text
		},
		dataType : "json",
		success : function(data) {
			if (!isErreur(data)) {

				$('body').load('accueil.jsp');
			}
		},
		error : function(jqXHR, textstatus, errorThrown) {
			alert(" le probleme est " + textstatus);

		}
	});
}

function add_comments(key_session, idmess) {

	var text = document.getElementById("le_text").value;

	$.ajax({
		type : "GET",
		url : "/0000Fouquer_Rudat/addcomment",
		data : {
			key_session : key_session,
			idmess : idmess,
			mess : text
		},
		dataType : "json",
		success : function(data) {
			if (!isErreur(data)) {
				$('body').load('accueil.jsp');
			}
		},
		error : function(jqXHR, textstatus, errorThrown) {
			alert(" le probleme est " + textstatus);

		}
	});
}

function add_as_friend(key_session, person_id) {

	$.ajax({
		type : "GET",
		url : "/0000Fouquer_Rudat/addfriend",
		data : {
			key_session : key_session,
			id_ami : person_id
		},
		dataType : "json",
		success : function(data) {

			if (!isErreur(data)) {
				$('body').load('accueil.jsp');
			}
		},
		error : function(jqXHR, textstatus, errorThrown) {
			alert(" le probleme est " + textstatus);

		}
	});
}

function deletefriend(key_session, person_id) {

	$
			.ajax({
				type : "GET",
				url : "/0000Fouquer_Rudat/removefriend",
				data : {
					key_session : key_session,
					id_ami : person_id
				},
				dataType : "json",
				success : function(data) {

					if (!isErreur(data)) {
						// TODO alert("friendship destroyed moreover you should
						// look into something to dynamically change the
						// output");
					}
				},
				error : function(jqXHR, textstatus, errorThrown) {
					alert("le probleme est " + textstatus);
				}
			});

}

function deletemessage(idmess) {
	$
			.ajax({
				type : "GET",
				url : "/0000Fouquer_Rudat/removemessage",
				data : {
					idmess : idmess
				},
				dataType : "json",
				success : function(data) {

					if (!isErreur(data)) {
						// TODO alert("message bien supprimer + chercher pour
						// faire l'affichage dynamic");
					}
				},
				error : function(jqXHR, textstatus, errorThrown) {
					alert("le probleme est " + textstatus);
				}
			});
}



function profil_friend(prenom,nom,login,id){
	
	var mainy=document.getElementById("pourtout");
	
	var tmp_str="<div id='div_profil'>"
		+"<img src='http://babelang.com/wp-content/uploads/2014/09/ffffff.pngtext100x100.png'/>"
		+"<h2>Profil de "+login+" : </h2>"
		+"<p>Pr&eacute;nom :<br/>"
		+prenom
		+"<br/>Nom :<br/>"
		+nom
		+"<br/>ID :<br/>"
		+id
		+"<br/>Login :<br/>"
		+login
		+"</p>"
		+"</div>";
	
	
	mainy.innerHTML=tmp_str;
	document.getElementById("posts").innerHTML="";
	var template = " <h1>{{recherche}}</h1>"
			+ "<table>"
			+ "<tr>"
			+ "	<th>Destinaire</th>"
			+ "	<th>Author</th>"
			+ "	<th>Text</th>"
			+ "	<th>Date</th>"
			+ "	<th>Score</th>"
			+ "</tr>"
			+ "<tr>{{#Resultat.message}}"
			+ " <td> <strong>{{destinataire}}</strong></td>"
			+ " <td><ul>"
			+ " <li> {{author.login}} </li>"
			+ " <li> {{author.contact}} </li>"
			+ " </ul></td>"
			+ "<td><p> {{text}}</p></td>"
			+ "<td class='date'> <i>{{date}}</i></td>"
			+ "<td class='td_score'><img class='button_like' src='jpg/thumb_up.ico' onclick='likemessage(&quot;{{_id}}&quot;,1);return false;'/>"
			+ "<img class='button_dislike' src='jpg/thumb_down.ico' onclick='likemessage(&quot;{{_id}}&quot;,-1);return false;'/>"
			+ "<p id='{{_id}}p'>{{nblike}}</p>"
			+ " </td>"
			+ "<td>"
			+ "<img class='montrer_commentaire' src='jpg/eye.ico' onclick='showhidecomments(&quot;{{_id}}tr&quot;,&quot;show&quot;);return false;' />"
			+ "<img class='montrer_commentaire' src='jpg/eye-hidden.png' onclick='showhidecomments(&quot;{{_id}}tr&quot;,&quot;none&quot;);return false;' />"
			+ "<img class='ajoute_commentaire' src='jpg/add.ico' onclick='form_comment(&quot;{{_id}}&quot;); return false;' />"
			+ "<button onclick='deletemessage(&quot;{{_id}}&quot;);return false;'>Supprimer message</button>"
			+ "</td>"
			+ "</tr>{{#comments}}"
			+ "<tr id='les_commentaires_text'class='{{_id}}tr' style='display:none;'>"
			+ "<td> {{author.login}} {{author.contact}}</td>"
			+ "<td> {{text}}</td>"
			+ "<td class='td_score'><img class='button_like' src='jpg/thumb_up.ico' onclick='likecomment(&quot;{{_id}}&quot;,&quot;{{_idcomm}}&quot;,1);return false;'/>"
			+ "<img class='button_dislike' src='jpg/thumb_down.ico' onclick='likecomment(&quot;{{_id}}&quot;,&quot;{{_idcomm}}&quot;,-1);return false;'/>"
			+ "<p id='{{_idcomm}}p'>{{nblike}}</p>" + " </td>"
			+ "</tr> {{/comments}}" + "{{/Resultat.message}}" + "</table>";

	$.ajax({
		type : "GET",
		url : "/0000Fouquer_Rudat/listmessage",
		dataType : "json",
		data : {
			author_id : id
		},
		success : function(data) {

			if (!isErreur(data)) {
				var html = Mustache.to_html(template, data);

				$("#posts").html(html);
			}
		},
		error : function(jqXHR, textstatus, errorThrown) {
			alert(" le probleme est " + textstatus)

		}

	});
	
}

function logout(key_sess){
	$.ajax({
		type : "GET",
		url : "/0000Fouquer_Rudat/logout",
		dataType : "json",
		data : {
			key : key_sess
		},
		success : function(data) {

			if (!isErreur(data)) {
				$('body').load('connexion.jsp');
			}
		},
		error : function(jqXHR, textstatus, errorThrown) {
			alert(" le probleme est " + textstatus)

		}

	});
}



function form_privatemessage(destinataire_id) {
	var template = "<textarea rows='5' cols='28' maxlength='140' id='le_text' placeholder='Tapez votre message ici. Limite 140 lettres'></textarea>"
			+ "<button id='bouton_pourtout' onclick='addPrivateMessage(&quot;"
			+ env.key
			+ "&quot;,"
			+ env.current_user[0].id
			+ ","+destinataire_id+");return false;'>Poster</button>";

	document.getElementById("pourtout").innerHTML = template;

}

function addPrivateMessage(key_session,author_id,destinataire_id){
	var text = document.getElementById("le_text").value;

	$.ajax({
		type : "GET",
		url : "/0000Fouquer_Rudat/addprivatemessage",
		data : {
			key_session : key_session,
			author_id: author_id,
			destinataire_id:destinataire_id,
			message:text
		},
		dataType : "json",
		success : function(data) {
			if (!isErreur(data)) {
				$('body').load('accueil.jsp');
			}
		},
		error : function(jqXHR, textstatus, errorThrown) {
			alert(" le probleme est " + textstatus);

		}
	});
	
	
}

function showprivateMessage(author_id,destinataire_id){
	
	var template="<h1>Private Message</h1>"
		+ "<table>"
		+ "<tr>"
		+ "	<th>Destinaire</th>"
		+ "	<th>Author</th>"
		+ "	<th>Text</th>"
		+ "	<th>Date</th>"
		+ "</tr>"
		+ "<tr>{{#message_privee}}"
		+ " <td> <strong>{{destinataire_login}}</strong></td>"
		+ " <td><ul>"
		+ " <li> {{author.login}} </li>"
		+ " <li> {{author.contact}} </li>"
		+ " </ul></td>"
		+ "<td><p> {{text}}</p></td>"
		+ "<td class='date'> <i>{{date}}</i></td>"
		+ " </tr>{{/message_privee}}</table>";
	
	var key_session=env.key;
	
	
	$.ajax({
		type : "GET",
		url : "/0000Fouquer_Rudat/showprivatemessage",
		data : {
			key_session : key_session,
			author_id: author_id,
			destinataire_id:destinataire_id,
		},
		dataType : "json",
		success : function(data) {
			if (!isErreur(data)) {
				var html = Mustache.to_html(template, data);

				$("#posts").html(html);
			}
		},
		error : function(jqXHR, textstatus, errorThrown) {
			alert(" le probleme est " + textstatus);

		}
	});
		
}

//# SourceURL=dynamic-main.js
