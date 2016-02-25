package dbRessource;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Database implements DBStatic {
    /**
     * 
     * 
     */
    private DataSource      dataSource;
    private static Database database;

    /**
     * Constructeur de la classe Database
     * @param jndiname
     * @throws SQLException
     */
    public Database(String jndiname) throws SQLException {
	try {
	    dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + jndiname);
	}
	catch (NamingException e) {
	    throw new SQLException(jndiname + "is missing in JNDI ! : " + e.getMessage());
	}
    }

    /**
     * Renvois le resultat de la connection à partir de la dataSource
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
	return dataSource.getConnection();
    }

    /**
     * Renvois le resultat de la connection sur le serveur SQL indiquer dans DBStatic
     * @return
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static Connection getMySQLConnection() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
	if (!DBStatic.mysql_pooling) {
	    Class.forName("com.mysql.jdbc.Driver").newInstance();
	    return (DriverManager.getConnection("jdbc:mysql://" + DBStatic.mysql_host + "/" + DBStatic.mysql_db, DBStatic.mysql_username, DBStatic.mysql_password));
	}
	else return extracted();
    }

    /**
     * Si la database n'est pas initialiser on en crée une 
     * @return
     * @throws SQLException
     */
    private static Connection extracted() throws SQLException {

	if (database == null) {
	    database = new Database("jdbc/db");
	}
	return (database.getConnection());

    }

    /**
     * Renvois le resultat de la connection a mongoDB
     * @return
     * @throws UnknownHostException
     * @throws MongoException
     */
    public static Mongo getMyMongoDb() throws UnknownHostException, MongoException
    {
	return new Mongo(DBStatic.mongoDB_host,DBStatic.mongoDB_port);
    }
}
