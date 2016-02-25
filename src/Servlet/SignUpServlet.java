package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import service.UserServices;
import Exception.loginAlreadyExistException;
import Exception.userDoesntExistException;

public class SignUpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) {
	String login = request.getParameter("login");
	String password = request.getParameter("password");
	String prenom = request.getParameter("prenom");
	String nom = request.getParameter("nom");
	try {
	    PrintWriter out = resp.getWriter();
	    JSONObject json = UserServices.createUser(login, password, prenom, nom);
	    // resp.setContentType("text/plain");
	    resp.getWriter().print(json.toString());

	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (userDoesntExistException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (loginAlreadyExistException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
