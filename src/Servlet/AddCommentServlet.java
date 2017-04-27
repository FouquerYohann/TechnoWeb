package Servlet;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import Exception.ExcepStatic;
import Exception.sessionDoesntExistException;
import Exception.userDoesntExistException;

import com.mongodb.MongoException;

import service.UserServices;

public class AddCommentServlet extends HttpServlet {

	// TODO a tester

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String key_session = req.getParameter("key_session");
		String idmess = req.getParameter("idmess");
		String mess = req.getParameter("mess");

		try {
			JSONObject json = UserServices.addComment(idmess, mess, key_session);
			resp.getWriter().print(json.toString());
		} catch (JSONException e) {
			try {
				resp.getWriter().print("JsonException " + e.getMessage() + " " + ExcepStatic.JSONException);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			try {
				resp.getWriter().print("IOException " + e.getMessage() + " " + ExcepStatic.JSONException);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
}
