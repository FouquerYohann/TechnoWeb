package Servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import Exception.ExcepStatic;
import service.UserServices;

public class ListPersonServlet extends HttpServlet {

	// TODO a tester
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {

		String prenom = req.getParameter("prenom");
		String nom = req.getParameter("nom");

		try {

			JSONObject json = UserServices.listPerson(prenom, nom);
		    
			resp.getWriter().print(json);

		}

		catch (IOException e) {
			try {
				resp.getWriter().print("IOException " + e.getMessage() );
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (JSONException e) {
			try {
				resp.getWriter().print("JsonException " + e.getMessage() + " " + ExcepStatic.JSONException);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}
}
