$(function() {
	$('#formulaire_connexion').on('submit', function() {

		var login = $('#login').val();
		var password = $('#password').val();

		$.ajax({
			type : "GET",
			data : {
				login : login,
				password : password
			},
			url : "/0000Fouquer_Rudat/login",
			datatype : "json",
			success : function(data) {
				var json = JSON.parse(data);
				if (!isErreur(json)) {
					$('body').load('accueil.jsp');
					new User(json.id, login);
					env.key = json.key;
				}
			},
			error : function(jqXHR, textstatus, errorThrown) {
				alert(textstatus);
			}
		});
		return false;
	});

});
