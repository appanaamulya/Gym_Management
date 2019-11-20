package com.gym;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Email_for_SuccessfulRegistration
 */
@WebServlet(urlPatterns = {"/Email_for_SuccessfulRegistration"},
initParams = { @WebInitParam(name = "emailPort", value = "587"),
		@WebInitParam(name = "emailHost", value = "smtp.gmail.com"),
		@WebInitParam(name = "user", value = "appanaamy"),
		@WebInitParam(name = "Pass", value = "qwerty!@#$"), })
public class Email_for_SuccessfulRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String host;
	private String port;
	private String user;
	private String pass;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Email_for_SuccessfulRegistration() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public void init(ServletConfig config) throws ServletException {
// TODO Auto-generated method stub
//super.init(config);
		host = config.getInitParameter("emailHost");
		port = config.getInitParameter("emailPort");
		user = config.getInitParameter("user");
		pass = config.getInitParameter("Pass");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/* doGet(request, response); */
		
		String rec = request.getParameter("email");
		String name = request.getParameter("name");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		/*
		 * for (String i : rec) System.out.println(i);
		 */
		String content ="Hi "+name+", your membership will start from "+startdate+" to "+enddate+".\n Enjoy your workout with CrossFit GYM \n ThankYou";
		SendEmail.setMailServerProperties(port);
		try {
			SendEmail.createEmailMessage(rec, content);
		} catch (AddressException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			SendEmail.sendEmail();
			request.getRequestDispatcher("./Endurance/AddMemberships.html").include(request, response);
		} catch (AddressException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
		
	}


