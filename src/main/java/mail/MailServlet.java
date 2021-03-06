package mail;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;

import dao.UserDAO;
import dao.entity.User;
import dao.factory.DAOFactory;
import dao.factory.EntityName;
import dao.superb.DAO;
import deprecated.DAOImpl;
import deprecated.UserController;
import mail.send.Sender;
import mail.validation.EmailValidation;

/**
 * This servlet is supposed to send mail with login and password . url-pattern =
 * '/register'
 */
public class MailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DAO<User, Integer> dao = null;

	public MailServlet() {
		super();
		dao = new DAOFactory().getDAO(EntityName.USER);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String toEmail = request.getParameter("mail");
		String passwordForClient = RandomStringUtils.randomAlphanumeric(6);
		boolean validation = EmailValidation.validate(toEmail == null ? "" : toEmail);
		boolean checkOnUnique = false;
		if (validation) {
			if(dao.findByCriteria("email", toEmail)==null) {
				checkOnUnique=true;
				Sender.send(toEmail, passwordForClient);
				saveInDataBase(toEmail, passwordForClient);
			}
		}

		doHtml(request, response, toEmail, validation, checkOnUnique);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
    /**
     * Saves a user in database
     * @param email
     * @param password
     * @return
     */
	private boolean saveInDataBase(String email, String password) {
		return dao.save(new User(email, password,2));
	}

	private void doHtml(HttpServletRequest request, HttpServletResponse response, String toEmail, boolean validation,
			boolean checkOnUnique) throws IOException {
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
			out.println("<script type=\"text/javascript\" src=\"jquery-3.2.1.min.js\"></script>");
			out.println("<script type=\"text/javascript\" src=\"sign.js\"></script>");
			// For style
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
			//
			out.println("<title>Registration</title></head>");
			out.println("<body>");
			out.println("<div class=\"box\">");
			out.println("<form method=\"GET\">");
			if (!validation || !checkOnUnique) {
				out.println("<h1>On this email will be sent the password</h1>");
				out.println("<input value =\"" + toEmail + "\" type=\"text\" name=\"mail\""
						+ "onFocus=\"field_focus(this, 'email');\""
						+ "onblur=\"field_blur(this, 'email');\" class=\"email\" />"
						+ (validation ? (checkOnUnique ? "" : "<br/><em style=\"color:red;\">This email already exists</em>") 
								                       : "<br/><em style=\"color:red;\">Please, write a valid email</em>"));
				out.println("<input type=\"submit\" value=\"Send\" id=\"btn2\"/>");
			} else {
				out.println("<h1>Message with password was sent on your email!</h1> <br/>");
				out.println("<a class=\"button\" href=\"/\">Go to Main page</a>");
			}
			out.println("</form>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
		}
	}

}
