package com.datamigration.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.hive.jdbc.HiveConnection;
import org.apache.log4j.Logger;

import com.datamigration.model.MetaDataConnectionDetails;
import com.datamigration.util.DataMigrationConstants;

/**
 * @author DESS
 *
 */
public final class DbConnection {
	final static Logger LOGGER = Logger.getLogger(DbConnection.class);
	static Connection con = null;
	private static DbConnection dbConn = null;

	/**
	 * 
	 */
	private DbConnection() {
	}

	/**
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static DbConnection getInstance() throws ClassNotFoundException,
			SQLException {
		LOGGER.info("Creating DbConnection instance");
		if (dbConn == null) {
			dbConn = new DbConnection();
		}
		if (con == null || con.isClosed()) {
			con = getConnection();
		}
		return dbConn;
	}

	/**
	 * @throws SQLException
	 */
	public static void close() throws SQLException {
		if (con != null) {
			con.close();
		}
	}

	/**
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws ClassNotFoundException,
			SQLException {
		// logger.info("get connection");

		// load the Driver Class
		Class.forName(DataMigrationConstants.MYSQL_DRIVER_CLASS);
		final String connectionURL = DataMigrationConstants.MYSQL_CONNECTION_PREFIX
				+ DataMigrationConstants.MYSQL_HOSTNAME
				+ ":"
				+ DataMigrationConstants.MYSQL_PORT
				+ "/"
				+ DataMigrationConstants.METADATA_DATABASE_NAME;
		// create the connection now
		con = DriverManager.getConnection(connectionURL,
				DataMigrationConstants.USER_NAME,
				DataMigrationConstants.PASSWORD);
		// String schemaName = props.getProperty("db2.schema");
		// logger.info("connection established!!!!!!");
		return con;
	}

	/**
	 * @param dbName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static HiveConnection getHiveConnection(final String dbName)
			throws ClassNotFoundException, SQLException {
		Properties connProp = new Properties();
		connProp.setProperty("user", DataMigrationConstants.USER_NAME);
		connProp.setProperty("password", DataMigrationConstants.PASSWORD);
		Class.forName(DataMigrationConstants.HIVE_DRIVER_CLASS);

		String connectionURL = DataMigrationConstants.HIVE_CONNECTION_PREFIX
				+ DataMigrationConstants.HIVE_HOSTNAME + ":"
				+ DataMigrationConstants.HIVE_PORT + "/" + dbName
				+ ";principal=" + DataMigrationConstants.HIVE_PRINCIPAL;
		LOGGER.info("Hive Connection URL : " + connectionURL);
		// create the connection now
		HiveConnection conn = new HiveConnection(connectionURL, connProp);
		// String schemaName = props.getProperty("db2.schema");
		LOGGER.info("connection established!!!!!!");
		return conn;
	}

	/**
	 * @param details
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection(MetaDataConnectionDetails details)
			throws ClassNotFoundException, SQLException {

		String connectionString = "";
		String driverName = "";
		String serverType = details.getServertype();
		switch (serverType.toLowerCase()) {
		case "mysql":
			driverName = DataMigrationConstants.MYSQL_DRIVER_CLASS;
			connectionString = DataMigrationConstants.MYSQL_CONNECTION_PREFIX
					+ details.getHostname() + ":" + details.getPort() + "/";
			break;
		case "oracle":
			break;
		case "db2":
			break;
		default:
			LOGGER.info("in default of switch()");
			break;
		}
		if (details.getDbname() != null) {
			connectionString = connectionString + details.getDbname();
		}
		Class.forName(driverName);
		LOGGER.info("Connectionstring====" + connectionString);
		LOGGER.info("Username" + details.getUsername());
		LOGGER.info("Password" + details.getPassword());
		// create the connection now
		Connection conn = DriverManager.getConnection(connectionString,
				details.getUsername(), details.getPassword());
		LOGGER.info("connection established!!!!!!");
		return conn;

	}

	/**
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws SQLException,
			ClassNotFoundException {
		String query = "create table test.employees_20160620(id int) ";
		Connection dbConn = DbConnection.getHiveConnection("test");
		try {
			Statement stmt = dbConn.prepareStatement(query);
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
