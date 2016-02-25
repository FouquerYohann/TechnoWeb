package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import Exception.userDoesntExistException;
import service.UserServices;

public class AddFriendServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
	String log_usr = req.getParameter("log_usr");
	String log_ami = req.getParameter("log_ami");
	try {

	    PrintWriter out = resp.getWriter();
	    JSONObject json = UserServices.addFriend(log_usr, log_ami);
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
	catch (userDoesntExistException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
