package Servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import Exception.ExcepStatic;
import service.UserServices;

public class SignUpServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) {
		// resp.setContentType("json");
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String prenom = request.getParameter("prenom");
		String nom = request.getParameter("nom");
		try {

			JSONObject json = UserServices.createUser(login, password, prenom, nom);
			resp.getWriter().print(json.toString());

		}

		catch (JSONException e) {
			try {
				resp.getWriter().print("JsonException " + e.getMessage() + " " + ExcepStatic.JSONException);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			try {
				resp.getWriter().print("IOException " + e.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse resp) {
		resp.setContentType("json");
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String prenom = request.getParameter("prenom");
		String nom = request.getParameter("nom");
		try {

			JSONObject json = UserServices.createUser(login, password, prenom, nom);
			resp.getWriter().print(json.toString());

		}

		catch (JSONException e) {
			try {
				resp.getWriter().print("JsonException " + e.getMessage() + " " + ExcepStatic.JSONException);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			try {
				resp.getWriter().print("IOException " + e.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
