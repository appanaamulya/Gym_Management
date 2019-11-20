package com.gym;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet(
		urlPatterns = {"/loginServlet"}, 
		initParams = { 
				@WebInitParam(name = "DB_DRIVER", value = "com.mysql.jdbc.Driver"), 
				@WebInitParam(name = "DB_URL", value = "jdbc:mysql://localhost:3306/gym_management"), 
				@WebInitParam(name = "DB_USER", value = "root"), 
				@WebInitParam(name = "DB_PASSWORD", value = "root")
		})
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 Connection con=null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void init(ServletConfig config) throws ServletException{
    	try {
    		Class.forName(config.getInitParameter("DB_DRIVER"));
    		//Class.forName("com.mysql.jdbc.Driver");
    		System.out.println("My SQL driver loaded..");
    		con=DriverManager.getConnection(config.getInitParameter("DB_URL"),config.getInitParameter("DB_USER"),config.getInitParameter("DB_PASSWORD"));
    		if(con != null) {
    			System.out.println("connection established..");
    		}
    		else
    		{
    			System.out.println("connection failed..");
    		}
    	}
    	catch(SQLException se) {
    		se.printStackTrace();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("executed");
		String username=request.getParameter("name");
		String password=request.getParameter("password");
		String actor=request.getParameter("actor");
		String id=request.getParameter("idno");
		System.out.println(username+"  "+password+"  "+actor+" "+id);
		/*
		 * String username="amulya"; String password="amulya123$"; String
		 * actor="client";
		 */
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		HttpSession session=request.getSession();
		session.setAttribute("username",username);
		session.setAttribute("password",password);
		session.setAttribute("ID",id);
		try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from login");
			int c=0;
			while(rs.next()) {
				//System.out.println(rs.getString("uname")+" "+rs.getString("pass"));
				if(rs.getString("actor").equalsIgnoreCase(actor)) {
				if((rs.getString("id")).equals(id) && (rs.getString("uname")).equals(username) && (rs.getString("pass")).equals(password)) {
					c=1;
//					out.println("<br><h4>Welcome!</h4>");
					if(actor.equalsIgnoreCase("client"))
					request.getRequestDispatcher("./Endurance/clientdashboard.jsp").include(request,response);
					if(actor.equalsIgnoreCase("trainer"))
						request.getRequestDispatcher("./Endurance/trainer.jsp").include(request,response);
					if(actor.equalsIgnoreCase("admin"))
						request.getRequestDispatcher("./Endurance/admin.html").include(request,response);
						
				}
			}
			}
			if(c==0) {
				out.println("<br><h4>You are not registered/your membership is over.Please contact our admin fr further details</h4>");
				out.println("<a href=Endurance/index.html>Home</a>");
				out.println("<a href=Endurance/contact.html>Contact us</a>");
			}
		}
		catch(SQLException se) {
			se.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
