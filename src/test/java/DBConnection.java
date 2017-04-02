

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	final static String URL = "jdbc:hive2://10.138.90.202:21050/;principal=impala/01hw507402.tcshydnextgen.com@TCSHYDNEXTGEN.COM";
	final static String DRIVER = "org.apache.hive.jdbc.HiveDriver";

	public static Connection getConnection() throws Exception {
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(URL);
		return con;
	}
}
