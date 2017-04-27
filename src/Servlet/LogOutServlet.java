package Servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import service.UserServices;

import com.mongodb.util.JSON;

import Exception.ExcepStatic;

public class LogOutServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String key_session = req.getParameter("key");
		try {
			JSONObject json = UserServices.logout(key_session);
			resp.getWriter().print(json.toString());

		} catch (IOException e) {

			try {
				resp.getWriter().print("IOException " + e.getMessage() + " " + ExcepStatic.JSONException);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (JSONException e) {
			try {
				resp.getWriter().print("JsonException " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
