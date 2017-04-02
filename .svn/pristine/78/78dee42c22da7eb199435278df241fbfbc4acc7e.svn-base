package com.datamigration.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.datamigration.model.UserDetails;
import com.datamigration.util.DataMigrationConstants;
import com.datamigration.util.FRDMException;

@SuppressWarnings("static-access")
public class UserDAO implements DaoUtil<UserDetails> {
	final Logger LOGGER = Logger.getLogger(UserDAO.class);

	@Override
	public UserDetails addUpdate(UserDetails obj) throws FRDMException {
		DbConnection dbConn = null;
		try {
			dbConn = DbConnection.getInstance();
			String query = null;

			PreparedStatement stmt;
			if (obj.getUserid() == 0) {
				query = "insert into " + DataMigrationConstants.USERS_TABLE
						+ " (username, password, role) values(?,?,?)";
				stmt = dbConn.con.prepareStatement(query);
			} else {
				query = "update " + DataMigrationConstants.USERS_TABLE
						+ " set username=?, password=?, role=? where userid=?";
				stmt = dbConn.con.prepareStatement(query);
				stmt.setInt(4, (int) obj.getUserid());
			}

			stmt.setString(1, obj.getUsername());
			stmt.setString(2, obj.getPassword());
			stmt.setString(3, obj.getRole());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not insert/update User details: "
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
	public List<UserDetails> getList() throws FRDMException {
		List<UserDetails> users = new ArrayList<UserDetails>();
		String query = "select * from " + DataMigrationConstants.USERS_TABLE;

		LOGGER.info("Get List Method:" + query);
		DbConnection dbConn = null;
		try {
			dbConn = DbConnection.getInstance();
			PreparedStatement stmt = dbConn.con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery(query);
			UserDetails user = null;
			while (rs.next()) {
				user = new UserDetails();
				user.setUserid(rs.getInt("userid"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				users.add(user);
			}
			rs.close();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not fetch User details: "
					+ e.getMessage());
		}
		return users;
	}

	@Override
	public UserDetails getDetails(Object name) throws FRDMException {

		DbConnection dbConn = null;
		UserDetails user = new UserDetails();
		try {
			dbConn = DbConnection.getInstance();
			user = getUserDetails(name, dbConn);
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not fetch user details: "
					+ e.getMessage());
		}
		return user;

	}

	public UserDetails getUserDetails(Object name, DbConnection dbConn)
			throws FRDMException {

		UserDetails user = new UserDetails();
		try {
			String query = "select * from "
					+ DataMigrationConstants.USERS_TABLE
					+ " where username = '" + name + "'";
			System.out.println("Get Details Method:" + query);
			LOGGER.info("Get Details Method:" + query);
			PreparedStatement stmt = dbConn.con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				user.setUserid(rs.getInt("userid"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new FRDMException("Could not fetch user details: "
					+ e.getMessage());
		}
		return user;

	}

	public boolean isUserExists(UserDetails obj) throws FRDMException {
		DbConnection dbConn = null;
		boolean flag = false;
		try {
			dbConn = DbConnection.getInstance();
			String query = null;
			ResultSet rs = null;

			PreparedStatement stmt;
			query = "select * from " + DataMigrationConstants.USERS_TABLE
					+ " where username='" + obj.getUsername() + "'";

			LOGGER.info("isUserExists Method:" + query);
			stmt = dbConn.con.prepareStatement(query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				flag = true;
				break;
			}
			rs.close();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not verify user details: "
					+ e.getMessage());
		}
		return flag;
	}
}
