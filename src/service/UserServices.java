package service;

import java.net.UnknownHostException;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoException;
import com.mongodb.util.JSON;

import dbRessource.DBTools;
import Exception.emptyResultResearchException;
import Exception.ExcepStatic;
import Exception.friendshipAlreadyExistException;
import Exception.sessionDoesntExistException;
import Exception.loginAlreadyExistException;
import Exception.messageDoesntExistException;
import Exception.sessionExpiredException;
import Exception.userDoesntExistException;
import Exception.wrongPasswordException;

/**
 * @author Classe dont les methodes utilisent des intances de UserTools afin
 *         d'acceder a la base de donnee Elle sera utiliser par les Servlets
 *         pour la connexion au serveur.
 */
public class UserServices {

    /**
     * retourne le JSON objet de connexion utilis� par les servlets.
     * 
     * @param login
     * @param password
     * @return
     * @throws JSONException
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */

    private static boolean checkVar(Object[] tab) {
	for (int i = 0; i < tab.length; i++) {
	    if (tab[i] instanceof String) {
		String new_string = (String) tab[i];
		if (new_string == null || new_string.equals("")) return false;
	    }
	    else if (tab[i] instanceof Integer) {
		Integer new_int = (Integer) tab[i];
		if (new_int <= -1 || new_int == null) return false;
	    }
	}
	return true;
    }

    public static JSONObject login(String login, String password) throws JSONException {
	Object[] tab = { login, password };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("argument manquant ", ExcepStatic.ParameterProblem);

	try {

	    UserTools.userExists(login);
	    UserTools.checkPassword(login, password);
	    JSONObject json = UserTools.insertSession(login);
	    return json;

	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("login innexistant " + e.getMessage(), ExcepStatic.userDoesntExistException);
	}
	catch (wrongPasswordException e) {
	    return ServiceTools.ServiceRefused("mauvais password " + e.getMessage(), ExcepStatic.wrongPasswordException);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("mauvais Json " + e.getMessage(), ExcepStatic.JSONException);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Mauvais SQL " + e.getMessage(), ExcepStatic.SQLException);
	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("mauvaise Instantiation " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("Acces illegal " + e.getMessage(), ExcepStatic.IllegalAccessException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Classe inconnue " + e.getMessage(), ExcepStatic.ClassNotFoundException);
	}

    }

    /* faire la fonction avec les test de logout */

    /**
     * Deconnecte la session du serveur
     * 
     * @param key
     * @throws JSONException
     */
    public static JSONObject logout(String key) throws JSONException {

	Object[] tab = { key };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("argument manquant ", ExcepStatic.ParameterProblem);
	try {
	    UserTools.checkSession(key);
	    UserTools.deleteSession(key);
	    JSONObject json = new JSONObject();
	    json.put("session ferme", key);
	    return json;
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Mauvais SQL " + e.getMessage(), ExcepStatic.SQLException);
	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("mauvaise Instantiation " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("Acces illegal " + e.getMessage(), ExcepStatic.IllegalAccessException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Classe inconnue " + e.getMessage(), ExcepStatic.ClassNotFoundException);
	}
	catch (sessionDoesntExistException e) {
	    return ServiceTools.ServiceRefused("La session n'existe pas " + e.getMessage(), ExcepStatic.sessionDoesntExistException);
	}
	catch (sessionExpiredException e) {
	    return ServiceTools.ServiceRefused("La session est expiree " + e.getMessage(), ExcepStatic.sessionExpiredException);
	}

    }

    /**
     * Cree un nouvel utilisateur sur la base de donnee
     * 
     * @param login
     * @param password
     * @param prenom
     * @param nom
     * @return
     * @throws userDoesntExistException
     * @throws SQLException
     * @throws loginAlreadyExistException
     * @throws JSONException
     */
    public static JSONObject createUser(String login, String password, String prenom, String nom) throws JSONException {
	Object[] tab = { login, password, prenom, nom };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("argument manquant ", ExcepStatic.ParameterProblem);

	try {
	    UserTools.userExists(login);

	    return ServiceTools.ServiceRefused("login existe deja ", ExcepStatic.loginAlreadyExistException);

	}
	catch (userDoesntExistException e) {
	    try {
		UserTools.insertUser(login, password, prenom, nom);
	    }
	    catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e1) {
		return ServiceTools.ServiceRefused("PROOOBLEMe " + e1.getMessage(), -1000);
	    }
	    JSONObject json = new JSONObject();
	    json.put("User enregistree", "ok");
	    json.put("login", login);
	    json.put("password", password);
	    json.put("prenom", prenom);
	    json.put("nom", nom);
	    return json;
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("mauvais Json " + e.getMessage(), ExcepStatic.JSONException);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Mauvais SQL", ExcepStatic.SQLException);
	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("mauvaise Instantiation " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("acces illegal " + e.getMessage(), ExcepStatic.IllegalAccessException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Classe inconnue " + e.getMessage(), ExcepStatic.ClassNotFoundException);
	}

    }

    /**
     * Ajoute un lien d'amitie entre deux login represente par une paire
     * 'friends' dans la db
     * 
     * @param log_usr
     * @param log_ami
     * @return
     * @throws SQLException
     * @throws userDoesntExistException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws JSONException
     * @throws sessionDoesntExistException
     */
    public static JSONObject addFriend(String key_session, int id_ami) throws JSONException {
	Object[] tab = { key_session, id_ami };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("argument manquant ", ExcepStatic.ParameterProblem);

	try {
	    UserTools.checkSession(key_session);
	    int id_user = DBTools.getIdFromKeySession(key_session);

	    UserTools.userExists(id_ami);
	    UserTools.userExists(id_user);
	    DBTools.alreadyFriend(id_user, id_ami);

	    UserTools.insertFriend(id_user, id_ami);
	    JSONObject json = new JSONObject();
	    json.put("operation", "ok");
	    return json;
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("login innexistant " + e.getMessage(), ExcepStatic.userDoesntExistException);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("mauvais Json " + e.getMessage(), ExcepStatic.JSONException);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Mauvais SQL " + e.getMessage() + "\n" + e.getSQLState() + "\n" + e.getErrorCode() + "\n", ExcepStatic.SQLException);
	}
	catch (sessionDoesntExistException e) {
	    return ServiceTools.ServiceRefused("Session n'existe pas " + e.getMessage(), ExcepStatic.sessionDoesntExistException);
	}
	catch (friendshipAlreadyExistException e) {
	    return ServiceTools.ServiceRefused("Deja ami " + e.getMessage(), ExcepStatic.friendshipAlreadyExistException);
	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("mauvaise Instantiation " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("Acces illegal " + e.getMessage(), ExcepStatic.IllegalAccessException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Classe inconnue " + e.getMessage(), ExcepStatic.ClassNotFoundException);
	}
	catch (sessionExpiredException e) {
	    return ServiceTools.ServiceRefused("Session fermee " + e.getMessage(), ExcepStatic.sessionExpiredException);
	}

    }

    /**
     * Supprime un 'friends' dans la db
     * 
     * @param key_session
     * @param id_ami
     * @return
     * @throws SQLException
     * @throws userDoesntExistException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws JSONException
     */
    public static JSONObject removeFriend(String key_session, int id_ami) throws JSONException {

	Object[] tab = { key_session, id_ami };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("argument manquant ", ExcepStatic.ParameterProblem);
	int id_user = -1;
	try {
	    UserTools.checkSession(key_session);
	    id_user = DBTools.getIdFromKeySession(key_session);

	    UserTools.userExists(id_ami);
	    UserTools.userExists(id_user);
	    // TODO rajouter l'exception pas d'amitier
	    DBTools.alreadyFriend(id_user, id_ami);
	    return ServiceTools.ServiceRefused("Vous n'etes pas ami avec cette personne ", ExcepStatic.notFriend);
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("login innexistant " + e.getMessage(), ExcepStatic.userDoesntExistException);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("mauvais Json " + e.getMessage(), ExcepStatic.JSONException);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Mauvais SQL " + e.getMessage(), ExcepStatic.SQLException);
	}
	catch (sessionDoesntExistException e) {
	    return ServiceTools.ServiceRefused("Session n'existe pas " + e.getMessage(), ExcepStatic.sessionDoesntExistException);
	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("mauvaise instanciation " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("acces illegal " + e.getMessage(), ExcepStatic.IllegalAccessException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Classe inconnue " + e.getMessage(), ExcepStatic.ClassNotFoundException);
	}
	catch (sessionExpiredException e) {
	    return ServiceTools.ServiceRefused("Session Expire " + e.getMessage(), ExcepStatic.sessionExpiredException);
	}
	catch (friendshipAlreadyExistException e) {
	    try {
		UserTools.deleteFriend(id_user, id_ami);
	    }
	    catch (InstantiationException | IllegalAccessException | ClassNotFoundException | userDoesntExistException | SQLException e1) {
		return ServiceTools.ServiceRefused("PROOOBLEMe " + e1.getMessage(), -1000);
	    }
	    JSONObject json = new JSONObject();
	    json.put("amitie", "ok");
	    return json;
	}

    }

    public static JSONObject listFriend(String log_usr) throws JSONException {
	Object[] tab = { log_usr };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("argument manquant ", ExcepStatic.ParameterProblem);

	try {
	    if (UserTools.userExists(log_usr)) {
		JSONObject json = UserTools.listFriend(log_usr);
		return json;
	    }
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("login innexistant " + e.getMessage(), ExcepStatic.userDoesntExistException);
	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("mauvaise Instantiation " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("acces illegal " + e.getMessage(), ExcepStatic.IllegalAccessException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Classe inconnue " + e.getMessage(), ExcepStatic.ClassNotFoundException);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Erreur SQL " + e.getMessage(), ExcepStatic.SQLException);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("Erreur JSON " + e.getMessage(), ExcepStatic.JSONException);
	}
	return null;
    }

    /**
     * Ajoute un message dans la database etant identifie par son autheur via le
     * login et l'id dans la db.
     * 
     * @param key_session
     * @param author_name
     * @param mess
     * @return
     * @throws SQLException
     * @throws userDoesntExistException
     * @throws MongoException
     * @throws UnknownHostException
     * @throws JSONException
     * @throws sessionDoesntExistException
     */
    public static JSONObject addMessage(String key_session, int destination, String mess) throws JSONException {

	Object[] tab = { key_session, destination, mess };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("argument manquant ", ExcepStatic.ParameterProblem);

	try {
	    UserTools.checkSession(key_session);
	    int author_id = DBTools.getIdFromKeySession(key_session);
	    UserTools.userExists(destination);
	    UserTools.insertMessage(author_id, destination, mess);
	    JSONObject json = new JSONObject();
	    json.put("operation", "ok");
	    return json;

	}
	catch (sessionDoesntExistException e) {
	    return ServiceTools.ServiceRefused("Session n'existe pas " + e.getMessage(), ExcepStatic.sessionDoesntExistException);
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("login innexistant " + e.getMessage(), ExcepStatic.userDoesntExistException);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("mauvais Json " + e.getMessage(), ExcepStatic.JSONException);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Mauvais SQL " + e.getMessage(), ExcepStatic.SQLException);
	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("Mauvaise Instantiation " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("Acces illegal " + e.getMessage(), ExcepStatic.IllegalAccessException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Classe inconnue " + e.getMessage(), ExcepStatic.ClassNotFoundException);
	}
	catch (UnknownHostException e) {
	    return ServiceTools.ServiceRefused("Hote inconnue " + e.getMessage(), ExcepStatic.UnknownHostException);
	}
	catch (MongoException e) {
	    return ServiceTools.ServiceRefused("Mauvaiss Mongo " + e.getMessage(), ExcepStatic.MongoException);
	}
	catch (sessionExpiredException e) {
	    return ServiceTools.ServiceRefused("Session ferme " + e.getMessage(), ExcepStatic.sessionExpiredException);
	}

    }

    public static JSONObject deleteMessage(String idmess) throws JSONException {
	Object[] tab = { idmess };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("argument manquant ", ExcepStatic.ParameterProblem);

	try {
	    UserTools.deleteMessage(idmess);

	    // }catch (IllegalArgumentException e){
	    // return ServiceTools.ServiceRefused("Idmess n'est pas valide " +
	    // e.getMessage(), ExcepStatic.IllegalArgumentException);
	}
	catch (UnknownHostException e) {
	    return ServiceTools.ServiceRefused("Hote inconnue " + e.getMessage(), ExcepStatic.UnknownHostException);
	}
	catch (MongoException e) {

	    return ServiceTools.ServiceRefused("Mauvais Mongo " + e.getMessage(), ExcepStatic.MongoException);
	}
	catch (messageDoesntExistException e) {
	    return ServiceTools.ServiceRefused("message inexistant " + e.getMessage(), ExcepStatic.MessageDoesntExist);
	}
	JSONObject json = new JSONObject();
	json.put("operation", "ok");
	return json;

    }

    // TODO a tester
    public static JSONObject likeMessage(String idmess, int nb) throws JSONException {
	Object[] tab = { idmess };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("argument manquant ", ExcepStatic.ParameterProblem);

	try {
	    UserTools.likeMessage(idmess, nb);
	    JSONObject json = new JSONObject();
	    json.put("operation", "ok");
	    return json;
	}
	catch (emptyResultResearchException e) {
	    return ServiceTools.ServiceRefused("Nous n'avons pas trouver de correspondance " + e.getMessage(), ExcepStatic.emptyResultResearchException);
	}
	catch (UnknownHostException e) {
	    return ServiceTools.ServiceRefused("Hote inconnue " + e.getMessage(), ExcepStatic.UnknownHostException);
	}
	catch (MongoException e) {
	    return ServiceTools.ServiceRefused("Mauvais Mongo " + e.getMessage(), ExcepStatic.MongoException);
	}

    }

    public static JSONObject likeComment(String idmess, String idcomm, int nb) throws JSONException {
	Object[] tab = { idmess, idcomm };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("argument manquant ", ExcepStatic.ParameterProblem);

	try {
	    UserTools.likeComment(idmess, idcomm, nb);
	    JSONObject json = new JSONObject();

	    json.put("operation", "ok");
	    return json;
	}
	catch (emptyResultResearchException e) {
	    return ServiceTools.ServiceRefused("Nous n'avons pas trouver de correspondance " + e.getMessage(), ExcepStatic.emptyResultResearchException);
	}
	catch (UnknownHostException e) {
	    return ServiceTools.ServiceRefused("Hote inconnue " + e.getMessage(), ExcepStatic.UnknownHostException);
	}
	catch (MongoException e) {
	    return ServiceTools.ServiceRefused("Mauvais Mongo " + e.getMessage(), ExcepStatic.MongoException);
	}

    }

    public static JSONObject listPerson(String prenom, String nom) throws JSONException {
	if (nom == null || prenom == null) return ServiceTools.ServiceRefused("argument manquant", -1);
	try {
	    JSONObject jsonres = UserTools.listPerson(prenom, nom);
	    String descrip_recherche = " Personne ";
	    if (prenom != null && prenom.length() > 0) descrip_recherche += "dont le prenom contient " + prenom;
	    if (nom != null && nom.length() > 0) descrip_recherche += "dont le nom contient " + nom;
	    JSONObject json = new JSONObject();
	    json.put("Recherche", descrip_recherche);
	    json.put("Resultat", jsonres);
	    return json;

	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Sql exception " + e.getMessage(), ExcepStatic.SQLException);
	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("Instantiation exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("illegal acces Exception " + e.getMessage(), ExcepStatic.IllegalAccessException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Class not found Exception " + e.getMessage(), ExcepStatic.ClassNotFoundException);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("Json Exception " + e.getMessage(), ExcepStatic.JSONException);
	}

    }

    public static JSONObject listMessage(int author_id, String s, int destinataire) throws JSONException {
	try {

	    JSONObject jsonliste = UserTools.listMessage(author_id, s, destinataire);
	    JSONObject json = new JSONObject();
	    String descrip_recherche = "Message ";
	    if (author_id >= 0) descrip_recherche += "ecrit par " + DBTools.getLoginFromId(author_id) + " ";
	    if (s != null && s.length() > 0) descrip_recherche += "contenant " + s + " ";
	    if (destinataire >= 0) descrip_recherche += "destine a " + DBTools.getLoginFromId(destinataire);
	    json.put("recherche", descrip_recherche);
	    json.put("Resultat", jsonliste);

	    return json;

	}
	catch (UnknownHostException e) {
	    return ServiceTools.ServiceRefused("unknown host" + e.getMessage(), ExcepStatic.UnknownHostException);
	}
	catch (MongoException e) {
	    return ServiceTools.ServiceRefused("Mongo Exception " + e.getMessage(), ExcepStatic.MongoException);
	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("Instantiation exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("illegal acces Exception " + e.getMessage(), ExcepStatic.IllegalAccessException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Class not found Exception " + e.getMessage(), ExcepStatic.ClassNotFoundException);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("SQL Exception " + e.getMessage(), ExcepStatic.SQLException);
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused(" user doesn't exist Exception " + e.getMessage(), ExcepStatic.userDoesntExistException);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("Json Exception " + e.getMessage(), ExcepStatic.JSONException);
	}

    }

    public static JSONObject addComment(String idmess, String mess, String key_session) throws JSONException {
	Object[] tab = { idmess, mess, key_session };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("Argument Manquant", ExcepStatic.ParameterProblem);

	try {
	    UserTools.checkSession(key_session);
	    int author_id;
	    author_id = DBTools.getIdFromKeySession(key_session);
	    UserTools.insertComment(idmess, mess, author_id);
	    JSONObject json = new JSONObject();
	    json.put("operation", "ok");
	    return json;

	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("Instantiation Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("Illegal Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Class not found Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("SQL Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (sessionDoesntExistException e) {
	    return ServiceTools.ServiceRefused("session doesnt exist Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (UnknownHostException e) {
	    return ServiceTools.ServiceRefused("unknown host Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (MongoException e) {
	    return ServiceTools.ServiceRefused("Mongo Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (sessionExpiredException e) {
	    return ServiceTools.ServiceRefused("session expired Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (emptyResultResearchException e) {
	    return ServiceTools.ServiceRefused("empty result Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("user doesnt exist Exception " + e.getMessage(), ExcepStatic.userDoesntExistException);
	}
    }

    public static JSONObject addPrivateMessage(String key_session, int author_id, int destinataire_id, String message) throws JSONException {
	Object[] tab = { author_id, destinataire_id, message };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("Argument Manquant", ExcepStatic.ParameterProblem);

	try {
	    UserTools.checkSession(key_session);
	    UserTools.addPrivateMessage(author_id, destinataire_id, message);
	    return ServiceTools.ServiceAccepted();

	}
	catch (MongoException e) {
	    return ServiceTools.ServiceRefused("Mongo Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("Instantiation Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("Illegal Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Class not found Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("SQL Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (sessionDoesntExistException e) {
	    return ServiceTools.ServiceRefused("session doesnt exist Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (UnknownHostException e) {
	    return ServiceTools.ServiceRefused("unknown host Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (sessionExpiredException e) {
	    return ServiceTools.ServiceRefused("session expired Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("user doesnt exist Exception " + e.getMessage(), ExcepStatic.userDoesntExistException);
	}
    }

    public static JSONObject showPrivateMessage(String key_session, int author_id, int my_id) throws JSONException {
	Object[] tab = { author_id, my_id };
	if (!checkVar(tab)) return ServiceTools.ServiceRefused("Argument Manquant", ExcepStatic.ParameterProblem);

	try {
	    UserTools.checkSession(key_session);
	    JSONObject json = UserTools.showPrivateMessageFrom(author_id, my_id);
	    return json;
	}
	catch (MongoException e) {
	    return ServiceTools.ServiceRefused("Mongo Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (InstantiationException e) {
	    return ServiceTools.ServiceRefused("Instantiation Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (IllegalAccessException e) {
	    return ServiceTools.ServiceRefused("Illegal Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (ClassNotFoundException e) {
	    return ServiceTools.ServiceRefused("Class not found Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("SQL Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (sessionDoesntExistException e) {
	    return ServiceTools.ServiceRefused("session doesnt exist Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (UnknownHostException e) {
	    return ServiceTools.ServiceRefused("unknown host Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	catch (sessionExpiredException e) {
	    return ServiceTools.ServiceRefused("session expired Exception " + e.getMessage(), ExcepStatic.InstantiationException);
	}
	
    }
}
