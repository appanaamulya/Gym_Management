package com.gym;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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
		urlPatterns = {"/renewMembership"}, 
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
public class renewMembership extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 Connection con=null;
	 private String host;
	 private String port;
	 private String user;
   	 private String pass;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public renewMembership() {
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
		int cid = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		int period = Integer.parseInt(request.getParameter("period"));
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		String workout = request.getParameter("workout");
		int paidamt = 560 * period;
		/*
		 * String username="amulya"; String password="amulya123$"; String
		 * actor="client";
		 */
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		HttpSession session=request.getSession(false);
		if(session!=null) {
		try {
			String sql = "select * from membership where cid = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, cid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
//				mid.add(rs.getInt("mid"));
				int i = rs.getInt("mid");
				int periodi = rs.getInt("period");
				String startdatei = rs.getString("startdate");
				String enddatei = rs.getString("enddate");
				String sql1 = "delete from membership where mid = ?";
				PreparedStatement ps1 = con.prepareStatement(sql1);
				ps1.setInt(1, i);
				int j = ps1.executeUpdate();
				if(j>0) {
					System.out.println("Deleted row from membership");
					String sql2 = "Insert into oldrecords values (?,?,?,?,?)";
					PreparedStatement ps2 = con.prepareStatement(sql2);
					ps2.setInt(1, cid);
					ps2.setInt(2, i);
					ps2.setInt(3,periodi);
					ps2.setString(4, startdatei);
					ps2.setString(5, enddatei);
					int k = ps2.executeUpdate();
					if(k>0) {
						System.out.println("Insertion into oldrecords");
					}
					
				}
			}
			  String  sql3 = "Insert into membership (cid,period,startdate,enddate,type_of_workout) values (?,?,?,?,?)";
			  PreparedStatement ps3 = con.prepareStatement(sql3);
				ps3.setInt(1,cid);
				ps3.setInt(2,period);
				ps3.setString(3,startdate);
				ps3.setString(4,enddate);
				ps3.setString(5,workout);
			    int z=ps3.executeUpdate();
			    if(z>0) {
			    	System.out.println("Inserted new membership details");
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
					 int kk=ps5.executeUpdate();
					    if(kk>0){
					    	System.out.println("Payments insertion successful");
					    	String sql6 = "select * from client where cid = ?";
							PreparedStatement ps6 = con.prepareStatement(sql6);
							ps6.setInt(1,cid);
							ResultSet rs6 = ps6.executeQuery();
							String email="";
							String cname="";
							while(rs6.next()) {
								email=rs6.getString("email");
								cname=rs6.getString("cname");
							}
							String content ="Hi "+cname+", your membership will start from "+startdate+" to "+enddate+".\n Enjoy your workout with CrossFit GYM \n ThankYou";
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
		catch(SQLException se) {
			se.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	}

}
