package Servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import Exception.ExcepStatic;
import service.UserServices;

public class ListFriendServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String login = req.getParameter("login");

		try {

			JSONObject json = UserServices.listFriend(login);
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
				
				e1.printStackTrace();
			}
		}
	}
}
