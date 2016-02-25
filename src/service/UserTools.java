package service;

import dbRessource.DBStatic;
import dbRessource.DBTools;
import dbRessource.Database;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import Exception.loginAlreadyExistException;
import Exception.sessionDoesntExistException;
import Exception.userDoesntExistException;
import Exception.wrongPasswordException;

/**
 * @author Classe permettant l'acces direct a la database pour la modifier. Elle
 *         sera utiliser par la classe de services utilisateurs.
 */
public class UserTools {

    /**
     * Verrifie si le user existe deja dans la base de donnee.
     * 
     * @param log
     * @return
     * @throws userDoesntExistException
     * @throws SQLException
     */
    public static boolean userExists(String log) throws userDoesntExistException, SQLException {

	try {
	    Connection conn = Database.getMySQLConnection();
	    String query = "SELECT id from users WHERE login='" + log + "';";
	    Statement st = conn.createStatement();
	    ResultSet res = st.executeQuery(query);
	    boolean retour;
	    if (res.next()) {
		retour = true;
	    }
	    else {
		retour = false;
		res.close();
		st.close();
		conn.close();
		throw new userDoesntExistException();
	    }
	    res.close();
	    st.close();
	    conn.close();
	    return retour;
	}
	catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}

    }

    /**
     * A l'etablissement d'une connexion on verrifie l'authenticite du mot de
     * passe associe au login demande. Renvois True si ils correspondent, False
     * dans le cas contraire.
     * 
     * @param login
     * @param password
     * @return
     * @throws wrongPasswordException
     * @throws SQLException
     */
    public static boolean checkPassword(String login, String password) throws wrongPasswordException, SQLException {

	try {
	    Connection conn = Database.getMySQLConnection();
	    String query = "SELECT id from users WHERE login='" + login + "';";
	    Statement st = conn.createStatement();
	    ResultSet res = st.executeQuery(query);
	    //
	    String passw = res.getString("password");

	    //
	    boolean retour;
	    if (password.equals(passw)) {
		retour = true;

	    }
	    else {
		retour = false;
	    }
	    res.close();
	    st.close();
	    conn.close();
	    return retour;
	}
	catch (Exception e) {
	    // TODO: handle exception
	    System.out.println("NAN MAIS OH LA HEIN!! TU... NON ... PAS COOL");
	    return false;
	}

    }

    /**
     * Permet d'inserrer un nouvel utilisateur dans la base de donnee.
     * 
     * @param login
     * @param password
     * @param prenom
     * @param nom
     * @throws SQLException
     */
    public static void insertUser(String login, String password, String prenom, String nom) throws SQLException {

	try {
	    Connection conn = Database.getMySQLConnection();
	    String query = "INSERT INTO users VALUES(null,'" + login + "','" + password + "','" + nom + "','" + prenom + "')";
	    Statement st = conn.createStatement();
	    st.executeUpdate(query);
	    st.close();
	    conn.close();

	}
	catch (Exception e) {
	    // TODO: handle exception
	    throw new SQLException("problem with insert user" + e.getMessage());
	}

    }

    /**
     * Cree une nouvelle session de connexion sur le serveur puis renvois la cle
     * de reference de cette session.
     * 
     * @param login
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws userDoesntExistException 
     */
    public static String insertSession(String login) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, userDoesntExistException {
	// checke si login est deja logger puis throw exception
	
	
	int id=DBTools.getIdFromLogin(login);
	
	
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	
	UUID key = UUID.randomUUID();
	String query = "INSERT INTO sessions VALUES('" + key + "','" + id + "','" + getCurrentTimeStamp() + "',FALSE,FALSE);";
	st.executeUpdate(query);

	st.close();
	conn.close();

	return "Session ouverte avec la clef" + key.toString();

    }

    /**
     * Verrifie l'etat de la connexion dont la cle est passe en argument.
     * 
     * @param session_key
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws sessionDoesntExistException
     */
    public static void checkSession(String session_key) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, sessionDoesntExistException {
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "SELECT expired FROM sessions WHERE session_key='" + session_key + "';";
	ResultSet res = st.executeQuery(query);
	if (res.next() == false) {
	    System.out.println("la session n'existe pas");
	    throw new sessionDoesntExistException();
	}

	boolean expi_res = res.getBoolean("expired");
	if (expi_res) {
	    System.out.println("La session est expirée.\n");
	}
	else {
	    System.out.println("La session est encore active.\n");
	}

	res.close();
	st.close();
	conn.close();
    }

    /**
     * Modifie la database pour mettre la valeur de l'argument 'expired' de la
     * session dont la cle est passe en argument a 'True'.
     * 
     * @param session_key
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws sessionDoesntExistException
     */
    public static void deleteSession(String session_key) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, sessionDoesntExistException {
	checkSession(session_key);
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "UPDATE sessions SET expired=true WHERE session_key='" + session_key + "';";
	st.executeUpdate(query);

	st.close();
	conn.close();
    }

    /**
     * Verrifie si la String login passe en argument n'est pas deja existant
     * dans la database.
     * 
     * @param login
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws loginAlreadyExistException
     */
    public static void loginExists(String login) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, loginAlreadyExistException {
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "SELECT * FROM users WHERE login='" + login + "';";
	ResultSet res = st.executeQuery(query);
	if (res.next() == false) {
	    System.out.println("Le login " + login + " existe déjà.\n");
	    throw new loginAlreadyExistException();
	}

	st.close();
	conn.close();

    }

    /**
     * Renvois la date et heure du jour sous forme d'un TimeStamp
     * 
     * @return
     */
    private static java.sql.Timestamp getCurrentTimeStamp() {

	java.util.Date today = new java.util.Date();
	return new java.sql.Timestamp(today.getTime());

    }

    /**
     * Insert un nouveau 'friends' dans la database. friends :
     * (log_usr,log_ami,timestamp)
     * 
     * @param log_usr
     * @param log_ami
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws userDoesntExistException
     */
    public static void insertFriend(String log_usr, String log_ami) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, userDoesntExistException {
	if ((userExists(log_usr) && userExists(log_ami))) {
	    
	    
	    
	    int id_usr=DBTools.getIdFromLogin(log_usr);
	    int id_ami=DBTools.getIdFromLogin(log_ami);
	   
	    
	    
	    Connection conn = Database.getMySQLConnection();
	    String query = "INSERT INTO friends VALUES ('" + id_usr + "','" + id_ami + "','" + getCurrentTimeStamp() + "');";
	    Statement st = conn.createStatement();
	    st.executeUpdate(query);
	    st.close();
	    conn.close();
	}
    }

    /**
     * Permet de supprimer un 'friends' dans la database en utilisant le login
     * des deux personnes concernee.
     * 
     * @param log_usr
     * @param log_ami
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws userDoesntExistException
     * @throws SQLException
     */
    public static void deleteFriend(String log_usr, String log_ami) throws InstantiationException, IllegalAccessException, ClassNotFoundException, userDoesntExistException, SQLException {
	if ((userExists(log_usr) && userExists(log_ami))) {
	    int id_usr, id_ami;
	    
	    
	    id_usr=DBTools.getIdFromLogin(log_usr);
	    id_ami=DBTools.getIdFromLogin(log_ami);

	    Connection conn = Database.getMySQLConnection();
	    Statement st = conn.createStatement();
	    String query = "DELETE FROM friends WHERE user='" + id_usr + "' AND ami='" + id_ami + "';";
	    st.executeUpdate(query);
	    st.close();
	    conn.close();
	}

    }

    /**
     * Insere un message dans la base de donnee en utilisant l'id et le login de
     * l'autheur.
     * 
     * @param author
     * @param author_name
     * @param mess
     * @throws UnknownHostException
     * @throws MongoException
     */
    public static void insertMessage(int author, String author_name, String mess) throws UnknownHostException, MongoException {
	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB("gr2_rudat_fouque"); // TODO ajouter dans DBSTATIC
	DBCollection coll = db.getCollection("Comments");

	BasicDBObject ajout = new BasicDBObject();

	GregorianCalendar calendar = new GregorianCalendar();
	calendar.add(Calendar.HOUR, -1);
	Date date = calendar.getTime();

	ajout.put("date", date.toString());
	ajout.put("author_id", author);
	ajout.put("author_name", author_name);
	ajout.put("text", mess);

	coll.insert(ajout);

	m.close();
    }

    public static void listFriend(String usr) {
	//Connection conn = Database.getMySQLConnection();
	//Statement st = conn.createStatement();
	//String query = "SELECT ami FROM friends WHERE usr='" + usr + "';";

    }

    public static void listMessage() {

    }

    public static void deleteMessage() {

    }
}
