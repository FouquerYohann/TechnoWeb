package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import service.UserServices;

public class LoginServlet extends HttpServlet {
    /**
     * Servlet servant a gerer la connexion via login/password sur le serveur.
     * 
     * @param request
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
	String login = request.getParameter("login");
	String password = request.getParameter("password");
	try {
	    PrintWriter out = resp.getWriter();
	    JSONObject json = UserServices.login(login, password);
	    resp.getWriter().print(json.toString());
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (InstantiationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (IllegalAccessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
