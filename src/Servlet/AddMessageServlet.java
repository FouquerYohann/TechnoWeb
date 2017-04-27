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
import Exception.ExcepStatic;
import Exception.userDoesntExistException;

public class AddMessageServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

		String key_session = req.getParameter("key_session");
		int destinataire = Integer.parseInt(req.getParameter("destinataire"));
		String mess = req.getParameter("mess");
		try {

			
			JSONObject json = UserServices.addMessage(key_session, destinataire, mess);
			resp.getWriter().print(json.toString());

		} catch (JSONException e) {
			try {
				resp.getWriter().print("JsonException " + e.getMessage() + " " + ExcepStatic.JSONException);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			try {
				resp.getWriter().print("IOException " + e.getMessage() );
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

}
