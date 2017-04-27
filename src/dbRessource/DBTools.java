package dbRessource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Exception.friendshipAlreadyExistException;
import Exception.sessionDoesntExistException;
import Exception.loginAlreadyExistException;
import Exception.userDoesntExistException;

public class DBTools {

    public static int getIdFromLogin(String login) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, userDoesntExistException {

	int id = -1;
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "SELECT id FROM users WHERE login='" + login + "';";
	ResultSet res = st.executeQuery(query);
	if (res.next() != false) id = res.getInt("id");
	else throw new userDoesntExistException();

	res.close();
	st.close();
	conn.close();

	return id;
    }

    public static String getLoginFromId(int id) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, userDoesntExistException {

	String login = "";
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "SELECT login FROM users WHERE id=" + id + ";";
	ResultSet res = st.executeQuery(query);
	if (res.next() != false) login = res.getString("login");
	else throw new userDoesntExistException();

	res.close();
	st.close();
	conn.close();

	return login;
    }

    public static int getIdFromKeySession(String key) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, sessionDoesntExistException {

	int id = -1;
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "SELECT * FROM sessions WHERE session_key='" + key + "';";
	ResultSet res = st.executeQuery(query);
	if (res.next()) id = res.getInt("user_id");
	else throw new sessionDoesntExistException();

	res.close();
	st.close();
	conn.close();

	return id;
    }

    public static void loginExists(String login) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, loginAlreadyExistException {
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "SELECT * FROM users WHERE login='" + login + "';";
	ResultSet res = st.executeQuery(query);
	if (res.next() == false) {
	    res.close();
	    st.close();
	    conn.close();
	    throw new loginAlreadyExistException();
	}
	res.close();
	st.close();
	conn.close();

    }

    public static void alreadyFriend(int user_id, int user_ami) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, friendshipAlreadyExistException {
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "SELECT * FROM friends WHERE user=" + user_id + " AND ami=" + user_ami + ";";
	ResultSet res = st.executeQuery(query);

	if (res.next() != false) {
	    res.close();
	    st.close();
	    conn.close();
	    throw new friendshipAlreadyExistException("deja ami");
	}
	res.close();
	st.close();
	conn.close();
	return;
    }

    public static boolean isFriend(int user_id, int user_ami) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
	Connection conn = Database.getMySQLConnection();
	Statement st = conn.createStatement();
	String query = "SELECT * FROM friends WHERE user=" + user_id + " AND ami=" + user_ami + ";";
	ResultSet res = st.executeQuery(query);
	boolean ret = false;
	if (res.next()) ret = true;

	res.close();
	st.close();
	conn.close();
	return ret;
    }

}
