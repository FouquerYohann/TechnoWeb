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
import Exception.userDoesntExistException;

public class AddMessageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
	int author_id= Integer.parseInt(req.getParameter("author_id"));
	String author = req.getParameter("author");
	String mess = req.getParameter("log_ami");
	try {

	    PrintWriter out = resp.getWriter();
	    JSONObject json = UserServices.addMessage(author_id, author, mess);
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

    }

}
