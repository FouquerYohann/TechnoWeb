package service;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoException;

import dbRessource.Database;
import Exception.loginAlreadyExistException;
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
    public static JSONObject login(String login, String password) throws JSONException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
	try {
	    if (login == null || password == null || login.equals("") || password.equals("")) return ServiceTools.ServiceRefused("argument manquant", -1);

	    UserTools.userExists(login);
	    UserTools.checkPassword(login, password);
	    String key = UserTools.insertSession(login);
	    JSONObject json = new JSONObject();
	    json.put("key", key);
	    return json;
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("login innexistant", 1);
	}
	catch (wrongPasswordException e) {
	    return ServiceTools.ServiceRefused("mauvais password", 2);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("mauvais Json", 100);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Mauvais SQL", 1000);
	}

    }

    /* faire la fonction avec les test de logout */

    /**
     * Deconnecte la session du serveur
     * 
     * @param key
     * @throws JSONException
     */
    public static void logout(String key) throws JSONException {

	try {
	    Connection conn = Database.getMySQLConnection();
	    String query = "UPDATE Session SET expired='True' WHERE key=" + key + ";";
	    Statement st = conn.createStatement();
	    st.executeUpdate(query);
	    st.close();
	    conn.close();
	}
	catch (Exception e) {
	    e.printStackTrace();
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
    public static JSONObject createUser(String login, String password, String prenom, String nom) throws userDoesntExistException, SQLException, loginAlreadyExistException, JSONException {
	if (login == null || password == null || prenom == null || nom == null) { return ServiceTools.ServiceRefused("argument manquant", -1); }
	try {
	    if (!UserTools.userExists(login)) {
		UserTools.insertUser(login, password, prenom, nom);
		JSONObject json = new JSONObject();
		json.put("operation", "ok");
		return json;
	    }
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("login innexistant", 1);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("mauvais Json", 100);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Mauvais SQL", 1000);
	}
	return null;
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
     */
    public static JSONObject addFriend(String log_usr, String log_ami) throws InstantiationException, IllegalAccessException, ClassNotFoundException, userDoesntExistException, SQLException,
	    JSONException {
	if (log_usr == null || log_ami == null) { return ServiceTools.ServiceRefused("argument manquant", -1); }
	try {
	    if (UserTools.userExists(log_ami) && (UserTools.userExists(log_usr))) {
		UserTools.insertFriend(log_usr, log_ami);
		JSONObject json = new JSONObject();
		json.put("operation", "ok");
		return json;
	    }
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("login innexistant", 1);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("mauvais Json", 100);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Mauvais SQL", 1000);
	}
	return null;
    }

    /**
     * Supprime un 'friends' dans la db
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
     */
    public static JSONObject removeFriend(String log_usr, String log_ami) throws InstantiationException, IllegalAccessException, ClassNotFoundException, userDoesntExistException, SQLException,
	    JSONException {
	if (log_usr == null || log_ami == null) return ServiceTools.ServiceRefused("argument manquant", -1);
	try {
	    if (UserTools.userExists(log_ami) && (UserTools.userExists(log_usr))) {
		UserTools.deleteFriend(log_usr, log_ami);
		JSONObject json = new JSONObject();
		json.put("operation", "ok");
		return json;
	    }
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("login innexistant", 1);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("mauvais Json", 100);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Mauvais SQL", 1000);
	}
	return null;
    }

    /**
     * Ajoute un message dans la database etant identifie par son autheur via le
     * login et l'id dans la db.
     * 
     * @param author_id
     * @param author_name
     * @param mess
     * @return
     * @throws SQLException
     * @throws userDoesntExistException
     * @throws MongoException
     * @throws UnknownHostException
     * @throws JSONException
     */
    public static JSONObject addMessage(int author_id, String author_name, String mess) throws UnknownHostException, MongoException, userDoesntExistException, SQLException, JSONException {
	// on suppose que l'id est toujours passer en argument.
	if (author_name == null || mess == null) { return ServiceTools.ServiceRefused("argument manquant", -1); }
	try {
	    if (UserTools.userExists(author_name)) {
		UserTools.insertMessage(author_id, author_name, mess);
		JSONObject json = new JSONObject();
		json.put("operation", "ok");
		return json;
	    }
	}
	catch (userDoesntExistException e) {
	    return ServiceTools.ServiceRefused("login innexistant", 1);
	}
	catch (JSONException e) {
	    return ServiceTools.ServiceRefused("mauvais Json", 100);
	}
	catch (SQLException e) {
	    return ServiceTools.ServiceRefused("Mauvais SQL", 1000);
	}
	return null;
    }

    public static void listMessage() {

    }
    
}
