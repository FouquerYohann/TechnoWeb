package Servlet;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Exception.ExcepStatic;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import dbRessource.DBStatic;
import dbRessource.Database;

public class ShowMongoServlet extends HttpServlet {

    
    @Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
	try {
	    Mongo m;
	    String result="";
	    m = Database.getMyMongoDb();
		
		DB db = m.getDB(DBStatic.mongoDB_username);
		DBCollection coll = db.getCollection("Comments");
		
		DBCursor res=coll.find();
		while(res.hasNext()){
		    DBObject dbobj = res.next();
		    result+=dbobj;
		}
		
		resp.getWriter().println(result);
		
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
