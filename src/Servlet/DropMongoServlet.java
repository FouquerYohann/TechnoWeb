package Servlet;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import Exception.ExcepStatic;
import dbRessource.DBStatic;
import dbRessource.Database;

public class DropMongoServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {

		Mongo m;
		try {
			m = Database.getMyMongoDb();

			DB db = m.getDB(DBStatic.mongoDB_username);
			DBCollection coll = db.getCollection("Comments");

			coll.drop();
			resp.getWriter().println("operation ok ");
			
		} catch (UnknownHostException | MongoException e) {
			 try {
					resp.getWriter().print("Mongo exception "+e.getMessage()+" "+ExcepStatic.JSONException);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}