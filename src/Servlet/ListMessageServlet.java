package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpRetryException;
import java.net.UnknownHostException;
import java.security.Provider.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import Exception.ExcepStatic;
import dbRessource.DBStatic;
import dbRessource.Database;
import service.ServiceTools;
import service.UserServices;
import service.UserTools;

public class ListMessageServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {

		// on mettra -1 a l'author id si on veut faire une recherche sans
		// author_id

		int author_id = -1;
		if (req.getParameter("author_id") != null && !req.getParameter("author_id").equals(""))
			author_id = Integer.parseInt(req.getParameter("author_id"));
		String s = req.getParameter("recherche");
		int destinataire = -1;
		if (req.getParameter("destinataire") != null && !req.getParameter("destinataire").equals(""))
			destinataire = Integer.parseInt(req.getParameter("destinataire"));

		try {

			JSONObject json = UserServices.listMessage(author_id, s, destinataire);

			resp.getWriter().print(json.toString());

		}

		catch (IOException e) {
			try {
				resp.getWriter().print("IOException " + e.getMessage());
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
