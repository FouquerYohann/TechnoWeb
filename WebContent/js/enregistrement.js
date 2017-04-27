$(function() {

	var prenom_ready = false;
	var nom_ready = false;
	var login_ready = false;
	var password_ready = false;
	var conf_pass_ready = false;

	$('#enregistre').on(
			'click',
			function() {

				if (prenom_ready == true && nom_ready == true
						&& login_ready == true && password_ready == true
						&& conf_pass_ready == true) {

					var login = $('#login').val();
					var prenom = $('#prenom').val();
					var nom = $('#nom').val();
					var password = $('#password').val();
					var conf_pass = $('#conf_pass').val();

					$.ajax({
						type : "POST",
						data : {
							login : login,
							prenom : prenom,
							nom : nom,
							password : password
						},
						url : "/0000Fouquer_Rudat/signup",
						datatype : "json",

						success : function(json) {
							if (json.error == undefined) {
								$('body').load('connexion.jsp');
							} else {
								alert(" Erreur :" + json.error + " number :"
										+ json.error_number);
							}
						},
						error : function(jqXHR, textstatus, errorThrown) {
							alert(textstatus);
						}
					});
					return false;

				} else {
					alert("Remplissez tout le formulaire correctement");
				}
			});
	
	

	$('#login').on(
			'focusout',
			function() {
				var login = $('#login').val();
				var pattern = new RegExp('^[a-z_0-9]{1,20}$', "i");
				if (!pattern.test(login)) {
					$('#login').css("border-color", "#FF0000");
					$('#login').next(".error").show().text("Invalid Input : caracteres speciaux interdit et taille de 1-20");
					login_ready = false;
				} else {
					$('#login').next(".error").hide();
					$('#login').css("border-color", "#00FF00");
					login_ready = true;
				}
			});

	$('#prenom').on(
			'focusout',
			function() {
				var prenom = $('#prenom').val();
				var pattern = new RegExp('^[a-z-]{1,20}$', "i");
				if (!pattern.test(prenom)) {
					$('#prenom').css("border-color", "#FF0000");
					$('#prenom').next(".error").show().text(
							"Invalid Input : caracteres speciaux interdit");
					prenom_ready = false;
				} else {
					$('#prenom').next(".error").hide();
					$('#prenom').css("border-color", "#00FF00");
					prenom_ready = true;
				}
			});

	$('#nom').on(
			'focusout',
			function() {
				var nom = $('#nom').val();
				var pattern = new RegExp('^[a-z-]{1,20}$', "i");
				if (!pattern.test(nom)) {
					$('#nom').css("border-color", "#FF0000");
					$('#nom').next(".error").show().text(
							"Invalid Input : caracteres speciaux interdit");
					nom_ready = false;
				} else {
					$('#nom').next(".error").hide();
					$('#nom').css("border-color", "#00FF00");
					nom_ready = true;
				}
			});
	
	$('#password').on(
			'focusout',
			function() {
				var password = $('#password').val();
				var pattern = new RegExp('^[a-z_0-9-]{8,20}$', "i");
				if (!pattern.test(password)) {
					$('#password').css("border-color", "#FF0000");
					$('#password').next(".error").show().text(
							"Invalid Input : Doit etre compris entre 8 et 20 pas de caracters speciaux");
					password_ready = false;
				} else {
					$('#password').next(".error").hide();
					$('#password').css("border-color", "#00FF00");
					password_ready = true;
				}
			});

	$('#conf_pass').on(
			'focusout',
			function() {
				var password = $('#password').val();
				var conf_pass = $('#conf_pass').val();

				if (password.localeCompare(conf_pass) != 0) {
					$('#conf_pass').css("border-color", "#FF0000");
					$('#conf_pass').next(".error").show().text(
							"Deux mot de pass different");
					conf_pass_ready = false;
				} else {
					$('#conf_pass').next(".error").hide();
					$('#conf_pass').css("border-color", "#00FF00");
					conf_pass_ready = true;
				}
			});

});

//# SourceURL=dynamic-enregistrement.js
