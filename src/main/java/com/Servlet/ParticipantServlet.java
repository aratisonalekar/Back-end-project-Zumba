package com.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class ParticipantServlet
 */
@WebServlet("/ParticipantServlet")
public class ParticipantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Connection connection;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ParticipantServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	@Override
	public void init() throws ServletException {
		System.out.println("Connection is being created..");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject", "root",
					"aratisona@1695");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}if (connection != null) {
		    System.out.println("Database connected!");
		} else {
		    System.out.println("Failed to make connection!");
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {
			Statement stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM javaproject.participant");

			out.println("<ul>");
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String name = rs.getString(2);
				Integer age = rs.getInt(3);
				String email = rs.getString(4);
				String mobileNo = rs.getString(5);
				String gender = rs.getString(6);
				out.println("<li> Id : " + id + "Name : " + name + "Age : " + age + "Email : " + email+ "Mobile No : " + mobileNo +" Gender : "+gender+ "</li>");
			}
			out.println("</ul>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String action = request.getParameter("action");

		if ("create".equals(action)) {
			String name = request.getParameter("name");
			int age = Integer.parseInt(request.getParameter("age"));
			String email = request.getParameter("email");
			String mobileNo = request.getParameter("mobileNo");
			String gender = request.getParameter("gender");
			String sql = "INSERT INTO participant (name, age, email, mobileNo, gender) values (?, ?, ?, ?, ?)";

			try {
				PreparedStatement preparedStmt = connection.prepareStatement(sql);

				preparedStmt.setString(1, name);
				preparedStmt.setInt(2, age);
				preparedStmt.setString(3, email);
				preparedStmt.setString(4, mobileNo);
				preparedStmt.setString(5, gender);

				preparedStmt.executeUpdate();
				out.println("Participant Record added Successfully!!");
			} catch (SQLException e) {
				e.printStackTrace();
				out.println("Error executing query: " + e.getMessage());
			}
		} else if ("update".equals(action)) {
			String newName = request.getParameter("name");
			int newAge = Integer.parseInt(request.getParameter("age"));
			String newEmail = request.getParameter("email");
			String newMobileNo = request.getParameter("mobileNo");
			String newGender = request.getParameter("gender");
			int id = Integer.parseInt(request.getParameter("id"));

			String sql = "UPDATE participant SET name = ?, age = ?, email = ?, mobileNo = ?, gender = ? WHERE id = ?";

			try {
				PreparedStatement pstmt = connection.prepareStatement(sql);

				pstmt.setString(1, newName);
				pstmt.setInt(2, newAge);
				pstmt.setString(3, newEmail);
				pstmt.setString(4, newMobileNo);
				pstmt.setString(5, newGender);
				pstmt.setInt(6, id);

				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected > 0) {
					out.println("<p> Participant Update Successfully!! </p>");
				} else {
					out.println("<p> Participant Not Found!! </p>");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if ("delete".equals(action)) {

			int id = Integer.parseInt(request.getParameter("id"));
			String sql = "DELETE FROM participant where id = ?";

			try {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, id);

				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected > 0) {
					out.println("<p> Participant Delete Successfully!! </p>");
				} else {
					out.println("<p> Participant Not Found!! </p>");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	} 
	@Override
	public void destroy() {
		System.out.println("Connection is Closed! ");
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
