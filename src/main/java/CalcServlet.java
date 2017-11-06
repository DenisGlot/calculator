import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CalcServlet
 */
@WebServlet("/calc")
public class CalcServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Double result;
	private Double first;
	private Double second;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CalcServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Logic below
		String act;
		String pattern = "[-+]?\\d+?[.]?[0-9]*?";
		String error = "Please, write correct number. Example -12.34";
		boolean er1 = false, er2 = false;

		if (request.getParameter("first") != null && request.getParameter("first") != ""
				&& request.getParameter("first").matches(pattern)) {
			first = Double.parseDouble(request.getParameter("first"));
		} else {
			er1 = true;
			first = null;
		}
		if (request.getParameter("second") != null && request.getParameter("second") != ""
				&& request.getParameter("first").matches(pattern)) {
			second = Double.parseDouble(request.getParameter("second"));
		} else {
			er2 = true;
			second = null;
		}
		result = null;
		if (request.getParameter("action") == null) {
			act = "+";
		} else {
			act = request.getParameter("action");
		}
		

		if (first != null && second != null) {
			switch (act) {
			case "+":
				result = first + second;
				break;
			case "-":
				result = first - second;
				break;
			case "*":
				result = first * second;
				break;
			case ":":
				result = first / second;
				break;
			case "sqrt first":
				result = Math.sqrt(first);
				break;
			case "sqrt second":
				result = Math.sqrt(second);
				break;
			}
		}

		// Set the response message's MIME type
		response.setContentType("text/html;charset=UTF-8");
		// Allocate a output writer to write the response message into the network
		// socket
		PrintWriter out = response.getWriter();

		// Write the response message, in an HTML page
		try {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
//			// Well, I tried to use ajax with JS.
//			out.println("<script type=\"text/javascript\" src=\"jquery-3.2.1.min.js\"></script>");
//			out.println("<script type=\"text/javascript\" src=\"calculate.js\"></script>");
			//
			out.println("<title>Calculator</title></head>");
			out.println("<body style=\"width : 500px ; margin : 0 auto ; background-color : #FFFACD;\">");
			out.println("<div id=\"mydiv\">");
			out.println(
					"<h1>Calculator Pro</h1>"); // Username : <input type=\"text\" style=\"width : 80px\" name=\"user\" value=\""
						   // user
						//	+ "\"> Password : <input type=\"text\" style=\"width : 80px\" name=\"password\" value=\""
						//	+ AuthFilter.password + "\" />"); // says Hello
			// Echo client's request information
			out.println("<form id=\"myform\">");
			out.println(
					"<p>First part</p><input style=\"border-radius: 6px; border: 1px solid #ccc;\" name=\"first\" type=\"text\" value=\""
							+ first + "\"><em style=\"color:red;\"> " + (er1 ? error : "") + "</em>");
			out.println(
					"<p>Second part</p><input style=\"border-radius: 6px; border: 1px solid #ccc;\" name=\"second\" type=\"text\" value = \" "
							+ second + " \"> <em style=\"color:red;\">" + (er2 ? error : "") + "</em>");
			out.println("<p>Action : <strong> " + act
					+ "</strong> </p><select name=\"action\"><option value=\"+\">Plus</option><option value=\"-\">Minus</option><option value=\"*\">Multiply</option>"
					+ "<option value=\":\">Divide</option><option value=\"sqrt first\">Sqrt of First part</option><option value=\"sqrt second\">Sqrt of Second part</option></select> <br/><br/>");
			out.println(
					"<button id=\"btn\" style=\"width : 170px; height : 35px; border-radius: 15px; border: 3px solid 	#228B22; \" type=\"submit\">Calculate</button><br/>");
			out.println("<div id=\"result\"><strong> Your result is : <h2>" + result + "</h2></strong></div>");
			out.println("</form>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
		} finally {
			out.close(); // Always close the output writer
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
