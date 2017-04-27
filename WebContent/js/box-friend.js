/**
 * 
 */

$(function() {

	$('#formulaire_recherche_person input[type=text]')
			.on(
					'keyup',
					function() {
						document.getElementById("pourtout").innerHTML="";
						var prenom = $('#prenom_search').val();
						var nom = $('#nom_search').val();

						var template = "<table>"
								+ "<tr>"
								+ "<th>prenom</th>"
								+ "<th>nom</th>"
								+ "<th>id</th>"
								+ "<th>login</th>"
								+ "<th>options</th>"
								+ "</tr>"
								+ "<tr>{{#Resultat.login}}"
								+ "<td>{{prenom}}</td>"
								+ "<td>{{nom}}</td>"
								+ "<td>{{id}}</td>"
								+ "<td>{{login}}</td>"
								+ "<td><button onclick='form_message({{id}});return false;'id='bouton_add_message'>Message</button><button onclick='add_as_friend(&quot;"
								+ env.key
								+ "&quot;,{{id}});return false;' id='bouton_add_friend'>Ajouter ami</button>" +
										"<button onclick='form_privatemessage({{id}});return false;'>Message Privee</button></td>"
								+ "</tr>{{/Resultat.login}}" + "</table>";

						$
								.ajax({
									type : "GET",
									url : "/0000Fouquer_Rudat/listperson",
									data : {
										prenom : prenom,
										nom : nom
									},
									dataType : "json",
									success : function(data) {

										if (!isErreur(data)) {
											var html = Mustache.to_html(
													template, data);
											$("#posts").html(html);
										}
									},
									error : function(jqXHR, textstatus,
											errorThrown) {
										alert(" le probleme est " + textstatus);

									}
								});

					});

	$('#charger_box_friend')
			.on(
					'click',
					function() {

						var login = env.current_user[0].login;

						var url_img = "http://babelang.com/wp-content/uploads/2014/09/ffffff.pngtext100x100.png";
						var template = "{{#friend}}"
								+ "<li>"
								+ "<img onclick='profil_friend(&quot;{{prenom}}&quot;,&quot;{{nom}}&quot;,&quot;{{login}}&quot;,&quot;{{id}}&quot;);return false;' src='"
								+ url_img
								+ "'/>"
								+ "<h3> {{login}}</h3>"
								+ "<p> {{prenom}} {{nom}}</p>"
								+ "<p> JE sais toujours pas quoi mettre</p>"
								+ "<button onclick='form_message(&quot;{{id}}&quot;);return false;'> envoyer message </button>"
								+ "<button onclick='deletefriend(&quot;"
								+ env.key
								+ "&quot;,&quot;{{id}}&quot;);return false;'>Supprimer ami </button>"
								+ "</li>" + "{{/friend}}";
						$.ajax({
							type : "GET",
							url : "/0000Fouquer_Rudat/listfriend",
							data : {
								login : login
							},
							datatype : "json",
							success : function(data) {
								if (!isErreur(data)) {
									var retour = JSON.parse(data);
									var html = Mustache.to_html(template,
											retour);
									$('#list-friend').html(html);
								}
							},
							error : function(jqXHR, textstatus, errorThrown) {
								alert("le probeleme est " + textstatus);
							}
						});
					});
	
	$('#formulaire_private_message').on('submit',function(){
		
		var destinataire=$('#destinataire_search').val();
		var author=$('#author_search').val();
		alert("sa marche ?");
		showprivateMessage(author,destinataire);
		return false;
	});
});

//# sourceURL=dynamicScript.js
