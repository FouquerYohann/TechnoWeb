package Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import service.UserServices;

import com.mongodb.util.JSON;

public class ShowPrivateMessageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	 int author_id=Integer.parseInt(req.getParameter("author_id"));
	 int destinataire_id=Integer.parseInt(req.getParameter("destinataire_id"));
	 String key_session=req.getParameter("key_session");
	 
	 try{
	     JSONObject json=UserServices.showPrivateMessage(key_session, author_id, destinataire_id);
	     resp.getWriter().println(json.toString());
	 }catch (JSONException e){
	     resp.getWriter().println(e.getMessage());
	 }
    }
}
