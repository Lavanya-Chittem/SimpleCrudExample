package com.crud.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.crud.bean.Student;


public class StudentDAO {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;
	
	public StudentDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	protected void connect() throws SQLException {
		if (jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(
										jdbcURL, jdbcUsername, jdbcPassword);
		}
	}
	
	protected void disconnect() throws SQLException {
		if (jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}
	
	public boolean insertBook(Student student) throws SQLException {
		String sql = "INSERT INTO student (id, name, email, password, phone) VALUES (?, ?, ?, ?, ?)";
		connect();
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, student.getId());
		statement.setString(2, student.getName());
		statement.setString(3, student.getEmail());
		statement.setString(4, student.getPassword());
		statement.setString(5, student.getPhone());
		
		boolean rowInserted = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowInserted;
	}
	
	public List<Student> listAllStudnets() throws SQLException {
		List<Student> listStudent = new ArrayList<>();
		
		String sql = "SELECT * FROM student";
		
		connect();
		
		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			String email = resultSet.getString("email");
			String password = resultSet.getString("password");
			String phone =resultSet.getString("phone");
			
			
			Student student = new Student(id, name, email, password, phone);
			listStudent.add(student);
		}
		
		resultSet.close();
		statement.close();
		
		disconnect();
		
		return listStudent;
	}
	
	public boolean deleteStudent(Student student) throws SQLException {
		String sql = "DELETE FROM student where id = ?";
		
		connect();
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, student.getId());
		
		boolean rowDeleted = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowDeleted;		
	}
	
	public boolean updateStudent(Student student) throws SQLException {
		String sql = "UPDATE student SET name = ?, email = ?, password = ?, phone = ?";
		sql += " WHERE id = ?";
		connect();
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		
		statement.setString(1, student.getName());
		statement.setString(2,student.getEmail());
		statement.setString(3, student.getPassword());
		 statement.setString(4, student.getPhone());
		 statement.setInt(5, student.getId());
		 
		
		boolean rowUpdated = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowUpdated;		
	}
	
	public Student getStudent(int id) throws SQLException {
		Student student = null;
		String sql = "SELECT * FROM student WHERE id = ?";
		
		connect();
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, id);
		
		ResultSet resultSet = statement.executeQuery();
		
		if (resultSet.next()) {
			String name = resultSet.getString("name");
			String email = resultSet.getString("email");
			String password = resultSet.getString("password");
			String phone =resultSet.getString("phone");
			
			student = new Student(id, name, email, password, phone);
		}
		
		resultSet.close();
		statement.close();
		
		return student;
	}


}
