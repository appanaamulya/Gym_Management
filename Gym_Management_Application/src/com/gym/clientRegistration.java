package com.gym;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
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
		urlPatterns = {"/clientRegistration"}, 
		initParams = { 
				@WebInitParam(name = "DB_DRIVER", value = "com.mysql.jdbc.Driver"), 
				@WebInitParam(name = "DB_URL", value = "jdbc:mysql://localhost:3306/gym_management"), 
				@WebInitParam(name = "DB_USER", value = "root"), 
				@WebInitParam(name = "DB_PASSWORD", value = "root"),
				@WebInitParam(name = "emailPort", value = "587"),
				@WebInitParam(name = "emailHost", value = "smtp.gmail.com"),
				@WebInitParam(name = "user", value = "acmsamazon"),
				@WebInitParam(name = "Pass", value = "Asaramal1")
		})
public class clientRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String host;
	private String port;
	private String user;
	private String pass;
	 Connection con=null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public clientRegistration() {
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
    			host = config.getInitParameter("emailHost");
    			port = config.getInitParameter("emailPort");
    			user = config.getInitParameter("user");
    			pass = config.getInitParameter("Pass");
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
		/*
		 * String username="amulya"; String password="amulya123$"; String
		 * actor="client";
		 */
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		HttpSession session=request.getSession(false);
		if(session!=null) {
			try {
				
				 String sql;
				  String name = request.getParameter("name");
				  String age = request.getParameter("age");
				  String gender = request.getParameter("gender");
				  int contact = Integer.parseInt(request.getParameter("phone"));
				  String email = request.getParameter("email");
				  String address = request.getParameter("address");
				  String password = request.getParameter("password");
				  String startdate = request.getParameter("startdate");
				  String enddate = request.getParameter("enddate");
				  int period = Integer.parseInt(request.getParameter("period"));
				  String workout = request.getParameter("workout");
				  
				  String actor = "client";
				  int paidamt = 560 * period;
				  
				 // session.setAttribute("clientEmail",email);
				  
			      sql = "Insert into client (cname,age,gender,contact,email,address,password) values (?,?,?,?,?,?,?)";
			      PreparedStatement ps=con.prepareStatement(sql);
			      ps.setString(1,name);
			      ps.setString(2,age);
			      ps.setString(3,gender);
			      ps.setInt(4,contact);
			      ps.setString(5,email);
			      ps.setString(6,address);
			      ps.setString(7, password);
				int i=ps.executeUpdate();
				if(i>0){
					System.out.println("Client insertion successful");
					String sql1 = "select * from client where email = ?";
					int cid = 0;
					PreparedStatement ps1 = con.prepareStatement(sql1);
					ps1.setString(1,email);
					ResultSet rs = ps1.executeQuery();
					while(rs.next()){
						cid = rs.getInt("cid");
					}
					String sql2 = "Insert into login values (?,?,?,?)";
					PreparedStatement ps2=con.prepareStatement(sql2);
				    ps2.setString(1,name);
				    ps2.setString(2,password);
				    ps2.setString(3,actor);
				    ps2.setInt(4,cid);
				    int j=ps2.executeUpdate();
				    if(j>0){
				    	System.out.println("Login insertion successful");
				    }
				  String  sql3 = "Insert into membership (cid,period,startdate,enddate,type_of_workout) values (?,?,?,?,?)";
				  PreparedStatement ps3 = con.prepareStatement(sql3);
					ps3.setInt(1,cid);
					ps3.setInt(2,period);
					ps3.setString(3,startdate);
					ps3.setString(4,enddate);
					ps3.setString(5,workout);
				    int z=ps3.executeUpdate();
				    if(z>0){
				    	System.out.println("Membership insertion successful");
				    	String sql4 = "select * from membership where cid = ?";
						int mid = 0;
						PreparedStatement ps4 = con.prepareStatement(sql4);
						ps4.setInt(1,cid);
						ResultSet rs1 = ps4.executeQuery();
						while(rs1.next()){
							mid = rs1.getInt("mid");
						}
						String sql5 = "Insert into payments (mid,paidamt) values (?,?)";
						PreparedStatement ps5 = con.prepareStatement(sql5);
						ps5.setInt(1,mid);
						ps5.setInt(2,paidamt);
						 int k=ps5.executeUpdate();
						    if(k>0){
						    	System.out.println("Payments insertion successful");
						    	String content ="Hi "+name+", your membership will start from "+startdate+" to "+enddate+".\n Enjoy your workout with CrossFit GYM \n ThankYou";
								SendEmail.setMailServerProperties(port);
								try {
									SendEmail.createEmailMessage(email, content);
								} catch (AddressException e) {
						// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (MessagingException e) {
						// TODO Auto-generated catch block
									e.printStackTrace();
								}
								try {
									SendEmail.sendEmail();
									request.getRequestDispatcher("./Endurance/admin.html").include(request, response);
								} catch (AddressException e) {
						// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (MessagingException e) {
						// TODO Auto-generated catch block
									e.printStackTrace();
								}

						    	//request.getRequestDispatcher("./Endurance/AddMemberships.html").include(request, response);
						    	//request.getRequestDispatcher("Email_for_SuccessfulRegistration.java?email="+email+"&name="+name+"&startdate="+startdate+"&enddate="+enddate).include(request, response);
						    }
				    }
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

}
