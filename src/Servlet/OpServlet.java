package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpServlet extends HttpServlet implements Servlet {

    /**
     * Servlet servant a gerer les operations a travers une connexion au
     * serveur.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	Map<String, String[]> parse = req.getParameterMap();
	String division_zero = "";
	if (parse.containsKey("op") && parse.containsKey("a") && parse.containsKey("b")) {
	    String operation = req.getParameter("op");
	    int a = Integer.parseInt(req.getParameter("a"));
	    int b = Integer.parseInt(req.getParameter("b"));
	    double res = 0.;
	    if (operation.equals("add")) {
		res = a + b;
	    }
	    else if (operation.equals("mul")) {
		res = a * b;
	    }
	    else if (operation.equals("div") && b != 0) {
		res = ((double) a) / ((double) b);
	    }
	    else {
		division_zero = "On divise pas par zero bordel !";
	    }

	    PrintWriter out = resp.getWriter();
	    out.println("<html><head><title>OPSERVLET MA GUEULE !!</title></head><body>");
	    out.print(a + " " + operation + " " + b + " = ");
	    if (division_zero.equals("")) out.println(res);
	    else out.println(division_zero);
	    out.println("</body></html>");

	}

    }

}
