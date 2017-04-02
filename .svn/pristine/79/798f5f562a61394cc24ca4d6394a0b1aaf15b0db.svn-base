

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ImpalaDao {

	public ArrayList<String> getList() throws Exception {
		ArrayList<String> sampleList = new ArrayList<String>();
		Connection con = DBConnection.getConnection();
		Statement stmt = con.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("show databases");
		System.out.println("\n== Begin Query Results ======================");
		// print the results to the console
		while (rs.next()) {
			// the example query returns one String column
			sampleList.add(rs.getString(1));
		}
		System.out.println("== End Query Results =======================\n\n");
		System.out.println("closing Connection");
		rs.close();
		stmt.close();
		con.close();
		return sampleList;
	}
	
	public static void main(String[] args) throws Exception {
		ImpalaDao dao = new ImpalaDao();
		System.out.println(dao.getList());
	}
}
