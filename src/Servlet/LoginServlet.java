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

import Exception.ExcepStatic;
import Exception.wrongPasswordException;
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
			JSONObject json = UserServices.login(login, password);
			resp.getWriter().print(json.toString());
		}

		catch (JSONException e) {
			try {
				resp.getWriter().print("JsonException " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			resp.getWriter().print("IOException " + e.getMessage());
		}

	}
}
