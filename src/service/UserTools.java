package service;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import Exception.emptyResultResearchException;
import Exception.friendshipAlreadyExistException;
import Exception.messageDoesntExistException;
import Exception.sessionDoesntExistException;
import Exception.sessionExpiredException;
import Exception.userDoesntExistException;
import Exception.wrongPasswordException;
import dbRessource.DBStatic;
import dbRessource.DBTools;
import dbRessource.Database;

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
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static boolean userExists(String log) throws userDoesntExistException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

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
	    throw new userDoesntExistException("le compte " + log + " n'existe pas");
	}
	res.close();
	st.close();
	conn.close();
	return retour;

    }

    public static boolean userExists(int id) throws userDoesntExistException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

	Connection conn = Database.getMySQLConnection();
	String query = "SELECT * from users WHERE id=" + id + ";";
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
	    throw new userDoesntExistException("le compte " + id + " n'existe pas");
	}
	res.close();
	st.close();
	conn.close();
	return retour;

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
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static boolean checkPassword(String login, String password) throws wrongPasswordException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

	Connection conn = Database.getMySQLConnection();
	String query = "SELECT * from users WHERE login='" + login + "';";
	Statement st = conn.createStatement();
	ResultSet res = st.executeQuery(query);
	String passw = "";

	if (res.next()) passw = res.getString("password");

	res.close();
	st.close();
	conn.close();

	boolean retour = true;
	if (!password.equals(passw)) {
	    retour = false;
	    throw new wrongPasswordException();

	}

	return retour;

    }

    /**
     * Permet d'inserrer un nouvel utilisateur dans la base de donnee.
     * 
     * @param login
     * @param password
     * @param prenom
     * @param nom
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void insertUser(String login, String password, String prenom, String nom) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

	Connection conn = Database.getMySQLConnection();
	String query = "INSERT INTO users VALUES(null,'" + login + "','" + password + "','" + nom + "','" + prenom + "')";
	Statement st = conn.createStatement();
	st.executeUpdate(query);
	st.close();
	conn.close();

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
     * @throws JSONException
     */
    public static JSONObject insertSession(String login) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, userDoesntExistException, JSONException {
	// checke si login est deja logger puis throw exception

	int id = DBTools.getIdFromLogin(login);

	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();

	UUID key = UUID.randomUUID();
	String query = "INSERT INTO sessions VALUES('" + key + "','" + id + "','" + getCurrentTimeStamp() + "',FALSE,FALSE);";
	st.executeUpdate(query);

	st.close();
	conn.close();
	JSONObject json = new JSONObject();
	json.put("id", id);
	json.put("key", key);

	return json;

    }

    private static boolean sessionExpire(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {
	long milliseconds1 = oldTime.getTime();
	long milliseconds2 = currentTime.getTime();

	long diff = milliseconds2 - milliseconds1;
	long diffMinutes = diff / (60 * 1000);
	long diffHours = diff / (60 * 60 * 1000);
	long diffDays = diff / (24 * 60 * 60 * 1000);

	if (diffMinutes > 30 || diffHours >= 1 || diffDays >= 1) return true;
	return false;

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
     * @throws sessionExpiredException
     */
    // TODO a tester en change�ant la duree de deconnexion
    public static void checkSession(String session_key) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, sessionDoesntExistException,
	    sessionExpiredException {
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "SELECT * FROM sessions WHERE session_key='" + session_key + "';";
	ResultSet res = st.executeQuery(query);
	if (res.next() == false) { throw new sessionDoesntExistException(); }

	boolean expi_res = res.getBoolean("expired");
	if (expi_res) throw new sessionExpiredException();

	Timestamp oldtimestamp = res.getTimestamp("timestamp");
	Timestamp curtimestamp = getCurrentTimeStamp();
	if (sessionExpire(curtimestamp, oldtimestamp)) {
	    query = "UPDATE sessions SET expired=true WHERE session_key='" + session_key + "';";
	    st.executeUpdate(query);

	    throw new sessionExpiredException();
	}
	else {
	    query = "UPDATE sessions SET timestamp ='" + curtimestamp + "' WHERE timestamp='" + oldtimestamp + "';";
	    st.executeUpdate(query);
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
     * @throws sessionExpiredException
     */
    public static void deleteSession(String session_key) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, sessionDoesntExistException,
	    sessionExpiredException {
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
     * @param id_usr
     * @param id_ami
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws userDoesntExistException
     */
    public static void insertFriend(int id_user, int id_ami) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
	Connection conn = Database.getMySQLConnection();
	String query = "INSERT INTO friends VALUES ('" + id_user + "','" + id_ami + "','" + getCurrentTimeStamp() + "');";
	Statement st = conn.createStatement();
	st.executeUpdate(query);
	query = "INSERT INTO friends VALUES ('" + id_ami + "','" + id_user + "','" + getCurrentTimeStamp() + "');";
	st.executeUpdate(query);
	st.close();
	conn.close();

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
    public static void deleteFriend(int id_user, int id_ami) throws userDoesntExistException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "DELETE FROM friends WHERE user='" + id_user + "' AND ami='" + id_ami + "';";
	st.executeUpdate(query);
	st.close();
	conn.close();
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
     * @throws JSONException
     * @throws userDoesntExistException
     * @throws friendshipAlreadyExistException
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void insertMessage(int author, int destination, String mess) throws UnknownHostException, MongoException, JSONException, InstantiationException, IllegalAccessException,
	    ClassNotFoundException, SQLException, userDoesntExistException {
	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("Comments");

	DBObject ajout = new BasicDBObject();

	GregorianCalendar calendar = new GregorianCalendar();
	calendar.add(Calendar.HOUR, -1);
	Date date = calendar.getTime();

	ajout.put("date", date.toString());

	DBObject authorjson = new BasicDBObject();

	authorjson.put("id", author);
	authorjson.put("login", DBTools.getLoginFromId(author));
	String friend = "Pas ami";
	if (DBTools.isFriend(author, destination)) friend = "ami";
	authorjson.put("contact", friend);

	ajout.put("author", authorjson);
	ajout.put("destinataire", destination);
	ajout.put("text", mess);
	ajout.put("nblike", 0);
	ajout.put("comments", new BasicDBList());

	coll.insert(ajout);

	m.close();

    }

    // TODO a tester
    public static void insertComment(String idmess, String mess, int author) throws UnknownHostException, MongoException, emptyResultResearchException, InstantiationException, IllegalAccessException,
	    ClassNotFoundException, SQLException, userDoesntExistException {

	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("Comments");

	DBObject recherche = new BasicDBObject("_id", new ObjectId(idmess));
	DBCursor res = coll.find(recherche);

	if (res.hasNext()) {
	    DBObject ajout = new BasicDBObject();

	    GregorianCalendar calendar = new GregorianCalendar();
	    calendar.add(Calendar.HOUR, -1);
	    Date date = calendar.getTime();

	    ajout.put("date", date.toString());

	    DBObject authorjson = new BasicDBObject();

	    authorjson.put("id", author);
	    authorjson.put("login", DBTools.getLoginFromId(author));

	    String friend = "A faire";
	    // TODO if (DBTools.isFriend(author,
	    // DBTools.getIdFromLogin(destination))) friend = "ami";
	    authorjson.put("contact", friend);

	    ajout.put("author", authorjson);
	    ajout.put("text", mess);
	    ajout.put("nblike", 0);

	    ajout.put("_idcomm", new ObjectId());

	    BasicDBObject upd = new BasicDBObject("$addToSet", new BasicDBObject("comments", ajout));
	    coll.update(recherche, upd);
	}
	else throw new emptyResultResearchException();

	// BasicDBList liste_com=(BasicDBList) dbobj.get("comments");
	// liste_com.add(ajout);

    }

    // TODO a tester
    public static void likeMessage(String idmess, int nb) throws UnknownHostException, MongoException, emptyResultResearchException {
	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("Comments");

	DBObject recherche = new BasicDBObject("_id", new ObjectId(idmess));
	DBObject dbobj = null;
	DBCursor res = coll.find(recherche);

	if (res.hasNext()) {
	    dbobj = new BasicDBObject("$inc", new BasicDBObject("nblike", nb));
	    coll.update(recherche, dbobj);
	}
	else {
	    throw new emptyResultResearchException();
	}

    }

    public static void likeComment(String idmess, String idcomm, int nb) throws UnknownHostException, MongoException, emptyResultResearchException, JSONException {
	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("Comments");

	DBObject recherche = new BasicDBObject("_id", new ObjectId(idmess));
	recherche.put("comments._idcomm", new ObjectId(idcomm));

	DBObject dbobj = null;
	DBCursor res = coll.find(recherche);

	if (res.hasNext()) {
	    DBObject looooolilol = res.next();
	    String troll = looooolilol.toString();
	    dbobj = new BasicDBObject("$inc", new BasicDBObject("comments.$.nblike", nb));
	    coll.update(recherche, dbobj);
	}
	else throw new emptyResultResearchException("" + recherche);

    }

    /**
     * @param usr
     * @return jsonobject avec la liste des amis de la forme ami1 : xxx ami2 :
     *         xxx
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws userDoesntExistException
     * @throws JSONException
     */
    public static JSONObject listFriend(String usr) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, userDoesntExistException, JSONException {
	int usr_id = DBTools.getIdFromLogin(usr);

	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "SELECT ami FROM friends WHERE user=" + usr_id + ";";

	ResultSet res = st.executeQuery(query);

	JSONObject json = new JSONObject();
	Connection conn2 = Database.getMySQLConnection();
	Statement st2 = conn2.createStatement();
	while (res.next()) {

	    String query2 = "SELECT * FROM users WHERE id=" + res.getInt("ami") + ";";
	    ResultSet res2 = st2.executeQuery(query2);

	    JSONObject json2 = new JSONObject();
	    json2.put("login", DBTools.getLoginFromId(res.getInt("ami")));
	    json2.put("id", res.getInt("ami"));
	    while (res2.next()) {

		json2.put("prenom", res2.getString("prenom"));
		json2.put("nom", res2.getString("nom"));
	    }

	    res2.close();

	    json.accumulate("friend", json2);
	}

	st2.close();
	conn2.close();
	res.close();
	st.close();
	conn.close();

	return json;

    }

    public static void deleteMessage(String idmess) throws UnknownHostException, MongoException, messageDoesntExistException {

	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("Comments");

	BasicDBObject query = new BasicDBObject();
	query.put("_id", new ObjectId(idmess));

	if (coll.find(query).hasNext()) coll.remove(query);
	else throw new messageDoesntExistException();
    }

    public static JSONObject listMessage(int author_id, String s, int destinataire) throws UnknownHostException, MongoException, JSONException {
	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("Comments");

	BasicDBObject query = new BasicDBObject();
	if (author_id >= 0) query.put("author.id", author_id);
	if (s != null && s.length() > 0) query.put("text", new BasicDBObject("$regex", s));
	if (destinataire >= 0) query.put("destinataire", destinataire);

	DBCursor res = coll.find(query);

	JSONObject json = new JSONObject();
	while (res.hasNext()) {
	    DBObject dbobj = res.next();
	    json.accumulate("message", dbobj);

	}

	return json;

    }

    // TODO a tester
    public static JSONObject listPerson(String prenom, String nom) throws SQLException, JSONException, InstantiationException, IllegalAccessException, ClassNotFoundException {
	Connection conn = Database.getMySQLConnection();
	String query = "";
	if (prenom.length() == 0 && nom.length() == 0) query = "SELECT * from users;";
	else if (prenom.length() == 0 && nom != null && nom.length() > 0) query = "SELECT * from users WHERE nom LIKE '%" + nom + "%';";
	else if (nom.length() == 0 && prenom != null && prenom.length() > 0) query = "SELECT * from users WHERE prenom LIKE '%" + prenom + "%';";
	else if (nom != null && prenom != null && prenom.length() > 0 && nom.length() > 0) query = "SELECT * from users WHERE prenom LIKE '%" + prenom + "%' AND nom LIKE '%" + nom + "%';";
	Statement st = conn.createStatement();
	ResultSet res = st.executeQuery(query);
	JSONObject retour = new JSONObject();

	while (res.next()) {
	    DBObject dbobj = new BasicDBObject("login", res.getString("login"));
	    dbobj.put("nom", res.getString("nom"));
	    dbobj.put("prenom", res.getString("prenom"));
	    dbobj.put("id", res.getInt("id"));
	    retour.accumulate("login", dbobj);
	}
	return retour;

    }

    public static void addPrivateMessage(int author_id, int destinataire_id, String message) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
		userDoesntExistException, UnknownHostException, MongoException {
	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("PrivateMessage");

	DBObject ajout = new BasicDBObject();

	GregorianCalendar calendar = new GregorianCalendar();
	calendar.add(Calendar.HOUR, -1);
	Date date = calendar.getTime();

	ajout.put("date", date.toString());

	DBObject authorjson = new BasicDBObject();

	authorjson.put("id", author_id);
	authorjson.put("login", DBTools.getLoginFromId(author_id));
	String friend = "Pas ami";
	if (DBTools.isFriend(author_id, destinataire_id)) friend = "ami";
	authorjson.put("contact", friend);

	ajout.put("author", authorjson);
	ajout.put("destinataire_id", destinataire_id);
	ajout.put("destinataire_login", DBTools.getLoginFromId(destinataire_id));
	ajout.put("text", message);

	coll.insert(ajout);

	m.close();

    }

    public static JSONObject showPrivateMessageFrom(int author_id, int my_id) throws UnknownHostException, MongoException, JSONException {
	DBObject query = new BasicDBObject("destinataire_id", my_id);
	query.put("author.id", author_id);

	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("PrivateMessage");

	DBCursor res = coll.find(query);

	JSONObject json = new JSONObject();
	while (res.hasNext()) {
	    DBObject dbobj = res.next();
	    json.accumulate("message_privee", dbobj);

	}

	return json;

    }

}
