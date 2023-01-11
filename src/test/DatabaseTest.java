package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * This is just a simple source code for connecting to your database and
 * querying a table
 * 
 * @author soulet
 *
 */

public class DatabaseTest {

	public static void main(String[] args) {
		// load setting from property file properties/configuration.properties
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("properties/configuration.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// get the setting
		String host = properties.getProperty("db_host");
		String user = properties.getProperty("db_user");
		String pwd = properties.getProperty("db_pwd");
		String dbname = properties.getProperty("db_name");

		// connect to the database (see Serveur.Database.java class)
		Connection connection = Database.getConnection(host, user, pwd, dbname);

		// example of a simple SELECT query
		try {
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * FROM sae302_test");

			// read the result of the query (row by row)
			while (result.next())
				System.out.println(result.getInt("id_test") + " " + result.getString("description_test"));
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
