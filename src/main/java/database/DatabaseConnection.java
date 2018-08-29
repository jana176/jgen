package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

	private static String url = "jdbc:mysql://localhost:3306/my_schema?useSSL=false&zeroDateTimeBehavior=convertToNull&autoReconnect=true";    
    private static String driverName = "com.mysql.jdbc.Driver";   
    private static String username = "root";   
    private static String password = "root";
    
	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url,username,password); 
			System.out.println("Successfully connected to database.");
		} catch (Exception e) {
			System.out.println("Failed to create the database connection. " + e);
		}
		return connection;
	}
	
}
