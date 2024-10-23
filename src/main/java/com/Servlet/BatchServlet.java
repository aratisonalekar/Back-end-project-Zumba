package com.Servlet;
import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BatchServlet
 */
@WebServlet("/BatchServlet")
public class BatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Connection connection;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public BatchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	System.out.println("Connection is being created..");
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root", "aratisona@1695");
    	} catch (ClassNotFoundException e) {
    		e.printStackTrace();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		try {
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM javaproject.batch");
			
			out.println("<ul>");
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String name = rs.getString(2);
				String batch = rs.getString(3);
				out.println("<li> ID: "+id+"Name: "+name+"Batch: "+batch+"</li>");
			}
			out.println("</ul>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		
		if("create".equals(action)) {
			String name = request.getParameter("name");
			String batch = request.getParameter("batch");
			String sql = "insert into Batch (name, batch) values (?, ?)";
			
			try {
				PreparedStatement preparedStmt = connection.prepareStatement(sql);
				
				preparedStmt.setString(1, name);
				preparedStmt.setString(2, batch);
				
				preparedStmt.executeUpdate();
				out.println("Batch Record added succesfully!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if ("update".equals(action)) {
			
			int id = Integer.parseInt(request.getParameter("id"));
			String newName = request.getParameter("name");
			
			
			String sql = "UPDATE Batch SET name = ? WHERE id = ?";
			
			try {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				
				pstmt.setString(1, newName);
				pstmt.setInt(2, id);
				
				
								
				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected > 0) {
					out.println("<p> Batch update succesfully! </p>");
				} else {
					out.println("<p> Batch Not Found!! </p>");
					
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}
	
		} else if ("delete".equals(action)) {
			int id = Integer.parseInt(request.getParameter("id"));
			String sql = "DELETE FROM batch where id = ?";
			
			try {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, id);
				
				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected > 0) {
					out.println("<p> Batch deleted successfully </p>");
				} else {
					out.println("<p> Batch NOT Found!!</p>");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	@Override
	public void destroy() {
		System.out.println("Connection is closed!");
		try {
			this.connection.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
