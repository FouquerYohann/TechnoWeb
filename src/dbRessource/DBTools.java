package dbRessource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Exception.userDoesntExistException;

public class DBTools {

    public static int getIdFromLogin(String login) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, userDoesntExistException {
	
	int id=-1;
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

}
