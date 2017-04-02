import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.datamigration.dao.DbConnection;
import com.datamigration.dao.HiveDAO;
import com.datamigration.model.MetaDataConnectionDetails;
import com.datamigration.util.DataMigrationConstants;
import com.datamigration.util.FRDMException;
import com.datamigration.util.HDFSUtil;
import com.datamigration.util.PasswordCryption;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Test2 {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		String tempTableName = "CAPACITY_DAY_HIST";
		MetaDataConnectionDetails details = new MetaDataConnectionDetails();
		details.setDbname("DBS_Invensys");
		details.setHostname("10.138.90.224");
		details.setPassword("mariadb");
		details.setPort("3306");
		details.setServertype("MYSQL");
		details.setUsername("root");
		Connection dbConn = DbConnection.getConnection(details);
		ArrayList<String> columnDetails = new ArrayList<String>();
		ArrayList<String> col_name = new ArrayList<String>();
		ArrayList<String> col_type = new ArrayList<String>();
		ArrayList<String> hive_col_type = new ArrayList<String>();
		HiveDAO hivedao = new HiveDAO();
		PreparedStatement stmt = null;
		String query = "";
		ResultSet rs = null;
		String column = "";
		int col_count = 0;
		String hivestmt = "";
		String storageFormat = "";
		try {
			query = "select * from DBS_Invensys.CAPACITY_DAY_HIST";
			stmt = dbConn.prepareStatement(query);
			rs = stmt.executeQuery();
			col_count = rs.getMetaData().getColumnCount();

			for (int i = 1; i <= col_count; i++) {
				col_name.add(rs.getMetaData().getColumnName(i));
				col_type.add(rs.getMetaData().getColumnTypeName(i));
			}

			for (int i = 0; i < col_name.size(); i++) {
				columnDetails.add(col_name.get(i) + "\t" + col_type.get(i));
			}
			for (int i = 0; i < col_type.size(); i++) {
				switch (col_type.get(i).toLowerCase()) {

				case "bigint":
					hive_col_type.add(i, "bigint");
					break;
				case "tinyint":
				case "smallint":
				case "mediumint":
				case "int":
					hive_col_type.add(i, "int");
					break;
				case "double":
					hive_col_type.add(i, "double");
				case "float":
					hive_col_type.add(i, "float");
				case "decimal":
					hive_col_type.add(i, "decimal");
				case "long":
					hive_col_type.add(i, "long");
					break;
				default:
					hive_col_type.add(i, "string");
					break;

				}
			}

			for (int j = 0; j < col_name.size(); j++) {
				if (j == 0) {
					column = col_name.get(j) + " " + hive_col_type.get(j);
				} else {
					column = column + "," + col_name.get(j) + " "
							+ hive_col_type.get(j);
				}
			}

			storageFormat = "textfile";
			if (tempTableName.isEmpty()) {
				tempTableName = "CAPACITY_DAY_HIST";
			}
			String targetDirectory = "/user/hdfs/datamigration/out" + "/"
					+ tempTableName;
			if (storageFormat.equalsIgnoreCase("textfile")) {
				hivestmt = " CREATE external table if not exists "
						+ "DBS_Invensys" + "." + tempTableName + "("
						+ column + ")"
						+ " row format delimited fields terminated by '" + ","
						+ "' lines terminated by " + "'\\n'" + " location "
						+ "'" + targetDirectory + "'";
			} else {
				if (storageFormat.equalsIgnoreCase("avro")) {}
			}

		} finally {
			if (dbConn != null)
				dbConn.close();
		}
		System.out.println(hivestmt);
	}
}
