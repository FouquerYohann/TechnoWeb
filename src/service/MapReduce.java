package service;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import Exception.emptyResultResearchException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mysql.jdbc.PreparedStatement;

import dbRessource.DBStatic;
import dbRessource.Database;

public class MapReduce {

    
    
    public static void mapreduceDF() throws UnknownHostException, MongoException, JSONException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
	
	
	
	String map="function map(){"
		+ "var words=this.text.toLowerCase().match(/\\w+/g);"
		+ "var df=[];"
		+ "for (var i=0; i<words.length ;i++){"
		+ "df[words[i]]=1;"
		+ "}"
		+ "for (var w in df){"
		+ "emit(w,df[w]);"
		+ "}"
		+ "}";
	
	String reduce="function reduce(key,values){"
		+ "total=0;"
		+ "for(var i=0;i<values.length;i++){"
		+ "total+=values[i];"
		+ "}"
		+ "return (total);"
		+ "}";
	
	
	
	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("Comments");
	
	MapReduceCommand cmd=new MapReduceCommand(coll,map,reduce,null,MapReduceCommand.OutputType.INLINE,null);
	MapReduceOutput out=coll.mapReduce(cmd);
	
	
	java.sql.PreparedStatement ps;
	
	String updat="INSERT INTO dftable "
	    	+ "(mot, df) "
	    	+ "VALUES (? , ?)"
	    	+ "ON DUPLICATE KEY UPDATE df = ?";
	
	Connection conn = Database.getMySQLConnection();
	
	ps=conn.prepareStatement(updat);
	
	for (DBObject obj: out.results()){
	   ps.setString(1, (String) obj.get("_id"));
	   ps.setDouble(2, (Double) obj.get("value"));
	   ps.setDouble(3, (Double) obj.get("value"));
	   ps.executeUpdate();
	}
	ps.close();
	conn.close();
	return ;
	
    }
    
    public static void mapreduceTF() throws UnknownHostException, MongoException, JSONException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
	
	String map="function map(){"
		+ "var words=this.text.toLowerCase().match(/\\w+/g);"
		+ "var tf=[];"
		+ "for (var i=0; i<words.length; i++){"
			+ "if(tf[words[i]] == null){"
				+ "tf[words[i]]=1;"
			+ "}else{"
				+ "tf[words[i]]++;"
			+ "}"
		+ "}"
		+ "for (var w in tf){"
			+ "var freq=tf[w]/words.length;"
			+ "emit(this._id,{mot:w,nb:freq});"
		+ "}"
		+ "}";
	
	
	String reduce="function reduce(key,values){"
			+"return ({liste_mot:values});"
			+ "}";
	
	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("Comments");
	
	MapReduceCommand cmd=new MapReduceCommand(coll,map,reduce,null,MapReduceCommand.OutputType.INLINE,null);
	MapReduceOutput out=coll.mapReduce(cmd);
	
	
	

	java.sql.PreparedStatement ps;
	
	String del="DELETE from tftable;";
	
	String updat="INSERT INTO tftable "
	    	+ "(doc, mot, freq) "
	    	+ "VALUES (? , ?, ?)";
	
	Connection conn = Database.getMySQLConnection();
	Statement delete=conn.createStatement();
	delete.executeUpdate(del);
	delete.close();
	
	ps=conn.prepareStatement(updat);
	
	
	for (DBObject obj: out.results()){
	    ObjectId doc= (ObjectId) obj.get("_id");
	    
	    
		DBObject value= (DBObject) obj.get("value");
		if(value.containsField("liste_mot")){
			BasicDBList dblist=(BasicDBList) value.get("liste_mot");
			for (int i = 0; i < dblist.size(); i++) {
			    DBObject dbobj=(DBObject) dblist.get(i);
			       
			     
			    ps.setString(1, doc.toString());
			    ps.setString(2, (String) dbobj.get("mot"));
			    ps.setDouble(3,(double) dbobj.get("nb"));
			    
			    ps.executeUpdate();
			}
		}else{
		    ps.setString(1, doc.toString());
		    ps.setString(2, (String) value.get("mot"));
		    ps.setDouble(3,(double) value.get("nb"));
		    
		    ps.executeUpdate();
		}
	}
	ps.close();
	conn.close();
	return ;
	
	
	
    }
    
    
    public static ArrayList<MyTuple> bestresult(String s) throws UnknownHostException, MongoException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
	ArrayList<MyTuple> retour=new ArrayList<MyTuple>();
	String firstword=s.split(" ",2)[0];
	
	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("Comments");
	
	int nb_tweet = (int) coll.getCount();
	
	m.close();
	
	String query="SELECT df FROM dftable where mot='"+firstword+"';";
	String query2="SELECT doc,freq FROM tftable where mot='"+firstword+"';";
	Connection conn = Database.getMySQLConnection();
	
	Statement st = conn.createStatement();
	ResultSet res=st.executeQuery(query);
	double df=0.0;
	if(res.next()){
	    df=res.getDouble("df");
	}else{
	    return null;
	}
	res.close();
	res=st.executeQuery(query2);
	
	while(res.next()){
	    ObjectId objid=new ObjectId(res.getString("doc"));
	    double weight=res.getDouble("freq")*(df/nb_tweet);
	    retour.add(new MyTuple(objid,weight));
	}
	
	return retour;
	
    }
    
    public static JSONObject returnBestResult(String s) throws UnknownHostException, MongoException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, JSONException, emptyResultResearchException{
	ArrayList<MyTuple> retour=bestresult(s);
	
	if(retour ==null){
	    throw new emptyResultResearchException();
	}
	
	Collections.sort(retour,Collections.reverseOrder());
	

	
	JSONObject json=new JSONObject();
	
	

	Mongo m = Database.getMyMongoDb();
	DB db = m.getDB(DBStatic.mongoDB_username);
	DBCollection coll = db.getCollection("Comments");
	
	BasicDBObject query=new BasicDBObject("_id",new ObjectId(retour.get(0).getObid()));
	
	DBCursor res=coll.find(query);
	
	if(res.hasNext()){
	    DBObject dbobj=res.next();
	    json.accumulate("message", dbobj);
	}
	
	
	return json;
	
    }
    
    
}


