package Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import Exception.ExcepStatic;
import service.UserServices;

public class LikeCommentServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

		// TODO a tester
		String idmess = req.getParameter("idmess");
		String idcomm = req.getParameter("idcomm");
		int nb = Integer.parseInt(req.getParameter("nb"));
		try {

		
			JSONObject json = UserServices.likeComment(idmess, idcomm, nb);
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
