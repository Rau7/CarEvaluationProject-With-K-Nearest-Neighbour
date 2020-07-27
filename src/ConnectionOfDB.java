import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class ConnectionOfDB {
	
	Connection con;
	Statement stmt;
	ResultSet rs;
	
	
	public ConnectionOfDB() throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/careval?autoReconnect=true&useSSL=false", "root", "root"); 
		stmt=con.createStatement();
		
	}

}
