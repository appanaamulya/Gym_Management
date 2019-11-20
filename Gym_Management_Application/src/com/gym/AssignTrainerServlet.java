package com.gym;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
 * Servlet implementation class AssignTrainerServlet
 */
@WebServlet(
		urlPatterns = {"/AssignTrainerServlet"}, 
		initParams = { 
				@WebInitParam(name = "DB_DRIVER", value = "com.mysql.jdbc.Driver"), 
				@WebInitParam(name = "DB_URL", value = "jdbc:mysql://localhost:3306/gym_management"), 
				@WebInitParam(name = "DB_USER", value = "root"), 
				@WebInitParam(name = "DB_PASSWORD", value = "root")
		})
public class AssignTrainerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 Connection con=null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AssignTrainerServlet() {
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		int cid=Integer.parseInt(request.getParameter("cid"));
		int tid=Integer.parseInt(request.getParameter("tid"));
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		
		try {
			String  sql = "insert into r_client_trainer values(?,?)";
			  PreparedStatement ps = con.prepareStatement(sql);
			  ps.setInt(1,tid);
			  ps.setInt(2,cid);
			int r=ps.executeUpdate();
			if(r>0) {
				System.out.println("trainer assigned to client- Success");
						request.getRequestDispatcher("./Endurance/admin.html").include(request,response);
						
				}
			else {
				out.println("Sorry there was a problem in assigning a trainer. Please try again");
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
