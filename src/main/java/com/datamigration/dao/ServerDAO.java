package com.datamigration.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.datamigration.model.ServerDetails;
import com.datamigration.util.DataMigrationConstants;
import com.datamigration.util.FRDMException;

@SuppressWarnings("static-access")
public class ServerDAO implements DaoUtil<ServerDetails> {
	final Logger LOGGER = Logger.getLogger(ServerDAO.class);

	@Override
	public ServerDetails addUpdate(ServerDetails obj) throws FRDMException {
		DbConnection dbConn = null;
		try {
			dbConn = DbConnection.getInstance();
			String query = null;

			PreparedStatement stmt;
			if (obj.getServerid() == 0) {
				query = "insert into "
						+ DataMigrationConstants.SERVERS_TABLE
						+ " (servertype, hostname, port, servername) values(?,?,?,?)";
				stmt = dbConn.con.prepareStatement(query);
			} else {
				query = "update "
						+ DataMigrationConstants.SERVERS_TABLE
						+ " set servertype=?,hostname=?,port=?,servername=? where serverid=?";
				stmt = dbConn.con.prepareStatement(query);
				stmt.setInt(5, (int) obj.getServerid());
			}

			stmt.setString(1, obj.getServertype());
			stmt.setString(2, obj.getHostname());
			stmt.setString(3, obj.getPort());
			stmt.setString(4, obj.getServername());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not insert/update server details: "
					+ e.getMessage());
		}
		return obj;
	}

	@Override
	public int delete(Object id) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public List<ServerDetails> getList() throws FRDMException {
		List<ServerDetails> servers = new ArrayList<ServerDetails>();
		String query = "select * from " + DataMigrationConstants.SERVERS_TABLE;

		LOGGER.info("Get List Method:" + query);
		LOGGER.info("Get List Method:" + query);
		DbConnection dbConn = null;
		try {
			dbConn = DbConnection.getInstance();
			PreparedStatement stmt = dbConn.con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery(query);
			ServerDetails server = null;
			while (rs.next()) {
				server = new ServerDetails();
				server.setServerid(rs.getInt("serverid"));
				server.setServertype(rs.getString("servertype"));
				server.setHostname(rs.getString("hostname"));
				server.setPort(rs.getString("port"));
				server.setServername(rs.getString("servername"));
				servers.add(server);
			}
			rs.close();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not fetch server details: "
					+ e.getMessage());
		}
		return servers;
	}

	@Override
	public ServerDetails getDetails(Object id) throws FRDMException {

		DbConnection dbConn = null;
		ServerDetails server = new ServerDetails();
		try {
			dbConn = DbConnection.getInstance();
			server = getServerDetails(id, dbConn);
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not fetch server details: "
					+ e.getMessage());
		}
		return server;

	}

	public ServerDetails getServerDetails(Object id, DbConnection dbConn)
			throws FRDMException {

		ServerDetails server = new ServerDetails();
		try {
			String query = "select * from "
					+ DataMigrationConstants.SERVERS_TABLE + " where "
					+ DataMigrationConstants.SERVERS_TABLE_ID + " = " + id;
			System.out.println("Get Details Method:" + query);
			LOGGER.info("Get Details Method:" + query);
			PreparedStatement stmt = dbConn.con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				server.setServerid(rs.getInt("serverid"));
				server.setServertype(rs.getString("servertype"));
				server.setHostname(rs.getString("hostname"));
				server.setPort(rs.getString("port"));
				server.setServername(rs.getString("servername"));
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new FRDMException("Could not fetch server details: "
					+ e.getMessage());
		}
		return server;

	}

	public int isServerExists(ServerDetails obj) throws FRDMException {
		DbConnection dbConn = null;
		int flag = 0;
		try {
			dbConn = DbConnection.getInstance();
			String query = null;
			ResultSet rs = null;

			PreparedStatement stmt;
			query = "select * from " + DataMigrationConstants.SERVERS_TABLE
					+ " where servername='" + obj.getServername() + "' or ("
					+ DataMigrationConstants.SERVERS_TABLE_HOST + " ='"
					+ obj.getHostname() + "' and "
					+ DataMigrationConstants.SERVERS_TABLE_PORT + " ='"
					+ obj.getPort() + "')";

			LOGGER.info("isServerExists Method:" + query);
			stmt = dbConn.con.prepareStatement(query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				if (obj.getServername().equalsIgnoreCase(
						rs.getString("servername"))) {
					flag = 1;
				} else {
					flag = 2;
				}
				break;
			}
			rs.close();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not verify server details: "
					+ e.getMessage());
		}
		return flag;
	}
}
