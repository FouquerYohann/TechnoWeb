package dbRessource;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.mongodb.MongoException;

import Exception.loginAlreadyExistException;
import Exception.sessionDoesntExistException;
import Exception.userDoesntExistException;
import service.UserServices;
import service.UserTools;

public class DBTest {

	
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, userDoesntExistException, SQLException, loginAlreadyExistException, sessionDoesntExistException, UnknownHostException, MongoException {
		/*	try{
				Connection conn=Database.getMySQLConnection();
				String query="CREATE TABLE sessions ( session_key VARCHAR(32) PRIMARY KEY, user_id INTEGER , timestamp TIMESTAMP, root BOOLEAN, expired BOOLEAN );";
				
				Statement st=conn.createStatement();
				//st.execute(query);
				st.close();
				
				st=conn.createStatement();
				query="CREATE TABLE friends(user INTEGER,ami INTEGER,time TIMESTAMP);";
				st.executeUpdate(query);
				st.close();
				
				st=conn.createStatement();
				query="CREATE TABLE users(id INTEGER PRIMARY KEY auto_increment,login VARCHAR(32) UNIQUE,password VARCHAR(255),nom VARCHAR(255));";
				st.executeUpdate(query);
				st.close();
				conn.close();
				
			}catch (SQLException e){
				e.printStackTrace();
			}
			
			*/
			//TODO! tester les userservice et usertools et database tools
	    
			
			UserTools.insertSession("justina");
			//UserTools.checkSession("38fda8c3-ee3f-4bc9-b048-d9752c69db0d");
			//UserTools.deleteSession("38fda8c3-ee3f-4bc9-b04-d9752c69db0d");
			//UserTools.checkSession("38fd8c3-ee3f-4bc9-b048-d9752c69db0d");
			//UserTools.insertMessage(1, "yohann", "trolololol");
			
		}
		
	
	}


