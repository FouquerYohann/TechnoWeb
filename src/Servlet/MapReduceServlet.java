package Servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import Exception.emptyResultResearchException;

import com.mongodb.MongoException;

import service.MapReduce;

public class MapReduceServlet extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String recherche = req.getParameter("recherche");
	String reduceDF = req.getParameter("reduceDF");
	String reduceTF = req.getParameter("reduceTF");
	
	try{
	    if(reduceDF!=null && reduceDF.length() >0 )
		MapReduce.mapreduceDF();
	    if(reduceTF!=null && reduceTF.length() >0 )
		MapReduce.mapreduceTF();
	    
	    if (recherche!=null && recherche.length() >0){
	    JSONObject json=MapReduce.returnBestResult(recherche);
	    resp.getWriter().println(json.toString());
	    }
	    else
		resp.getWriter().println("erreur argument");
	    
	}catch (JSONException e){
	    resp.getWriter().println(e);
	}
	catch (MongoException e) {
	    resp.getWriter().println(e);
	}
	catch (InstantiationException e) {
	    resp.getWriter().println(e);
	}
	catch (IllegalAccessException e) {
	    resp.getWriter().println(e);
	}
	catch (ClassNotFoundException e) {
	    resp.getWriter().println(e);
	}
	catch (SQLException e) {
	    resp.getWriter().println(e);
	}
	catch (emptyResultResearchException e) {
	    resp.getWriter().println(e);
	}
	
	
	
    }
}
