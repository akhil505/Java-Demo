package com.datamigration.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.datamigration.model.RequestDetails;
import com.datamigration.model.ServerDetails;
import com.datamigration.util.DataMigrationConstants;
import com.datamigration.util.FRDMException;

@SuppressWarnings("static-access")
public class RequestDAO implements DaoUtil<RequestDetails> {

	final Logger LOGGER = Logger.getLogger(RequestDAO.class);

	@Override
	public RequestDetails addUpdate(RequestDetails obj) throws FRDMException {
		DbConnection dbConn = null;
		try {
			dbConn = DbConnection.getInstance();
			String query = null;
			PreparedStatement stmt = null;
			if (obj.getRequestid() == 0) {
				query = "insert into "
						+ DataMigrationConstants.REQUESTS_TABLE
						+ "(requestname, sourcedatabase, sourcetables, username, password, storagetarget, "
						+ "storageformat, storagecompression, targetdirectory, criteriacolumn, mappers, splitbycolumn,"
						+ " rowkey, provisionstatus, requestedby, serverid, loadtype) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				stmt = dbConn.con.prepareStatement(query);
			} else {
				query = "update "
						+ DataMigrationConstants.REQUESTS_TABLE
						+ " set requestname=?, sourcedatabase=?, sourcetables=?, username=?, password=?, storagetarget=?, "
						+ "storageformat=?, storagecompression=?, targetdirectory=?, criteriacolumn=?, mappers=?, splitbycolumn=?, "
						+ "rowkey=?, provisionstatus=?, requestedby=?, serverid=?, loadtype=? where requestid=?";
				stmt = dbConn.con.prepareStatement(query);
				stmt.setInt(18, (int) obj.getRequestid());
			}

			stmt.setString(1, obj.getRequestname());
			stmt.setString(2, obj.getSourcedatabase());
			stmt.setString(3, obj.getSourcetables());
			stmt.setString(4, obj.getUsername());
			stmt.setString(5, obj.getPassword());
			stmt.setString(6, obj.getStoragetarget());
			stmt.setString(7, obj.getStorageformat());
			stmt.setString(8, obj.getStoragecompression());
			stmt.setString(9, obj.getTargetdirectory());
			stmt.setString(10, obj.getCriteriacolumn());
			stmt.setInt(11, (int) obj.getMappers());
			stmt.setString(12, obj.getSplitbycolumn());
			stmt.setString(13, obj.getRowkey());
			stmt.setString(14, obj.getProvisionstatus());
			stmt.setString(15, obj.getRequestedby());
			stmt.setInt(16, (int) obj.getServerid());
			stmt.setString(17, obj.getLoadtype());
			System.out.println("insert Request Query:" + stmt.toString());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException(
					"Could not update/insert the request details: "
							+ e.getMessage(), e);
		}
		return obj;
	}

	@Override
	public int delete(Object id) throws FRDMException {
		int flag = 0;
		String query = null;
		DbConnection dbConn = null;
		try {
			dbConn = DbConnection.getInstance();
			long reqId = (long) id;
			query = "DELETE FROM " + DataMigrationConstants.REQUESTS_TABLE
					+ " where requestid=?";
			LOGGER.info(query);
			PreparedStatement stmt = dbConn.con.prepareStatement(query);
			stmt.setLong(1, reqId);
			flag = stmt.executeUpdate();

			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not delete the request details: "
					+ e.getMessage(), e);
		}
		return flag;
	}

	public List<RequestDetails> getList(String user) throws FRDMException {
		List<RequestDetails> requests = new ArrayList<RequestDetails>();
		String query = "select * from " + DataMigrationConstants.REQUESTS_TABLE;
		if ("admin".equalsIgnoreCase(user)) {
			query = query.concat(" where provisionstatus='INACTIVE'");
		} else {
			query = query.concat(" where requestedby='" + user + "'");
		}
		DbConnection dbConn = null;
		try {
			dbConn = DbConnection.getInstance();
			PreparedStatement stmt = dbConn.con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery(query);
			RequestDetails request = null;
			while (rs.next()) {
				request = new RequestDetails();
				request.setRequestid(rs.getInt("requestid"));
				request.setSourcedatabase(rs.getString("sourcedatabase"));
				int serverId = rs.getInt("serverid");
				request.setServerid(serverId);
				ServerDAO sdao = new ServerDAO();
				ServerDetails serverdetails = sdao.getServerDetails(serverId,
						dbConn);
				request.setServerdetails(serverdetails);
				request.setServerhost(serverdetails.getHostname());
				request.setServername(serverdetails.getServername());
				request.setType(serverdetails.getServertype());
				request.setServerport(serverdetails.getPort());
				request.setSourcetables(rs.getString("sourcetables"));
				request.setCriteriacolumn(rs.getString("criteriacolumn"));
				request.setStoragetarget(rs.getString("storagetarget"));
				request.setStorageformat(rs.getString("storageformat"));
				request.setStoragecompression(rs
						.getString("storagecompression"));
				request.setMappers(rs.getInt("mappers"));
				request.setSplitbycolumn(rs.getString("splitbycolumn"));
				request.setTargetdirectory(rs.getString("targetdirectory"));
				request.setRowkey(rs.getString("rowkey"));
				request.setRequestname(rs.getString("requestname"));
				System.out.println(rs.getString("requestname"));
				request.setProvisionstatus(rs.getString("provisionstatus"));
				request.setUsername(rs.getString("username"));
				request.setPassword(rs.getString("password"));
				request.setRequestedby(rs.getString("requestedby"));
				request.setLoadtype(rs.getString("loadtype"));
				requests.add(request);
			}

			rs.close();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not Fetch the request details: "
					+ e.getMessage(), e);
		}
		return requests;
	}

	@Override
	public RequestDetails getDetails(Object id) throws FRDMException {
		DbConnection dbConn = null;
		String query = null;
		RequestDetails resultRequest = null;
		try {
			dbConn = DbConnection.getInstance();
			int reqId = (int) id;
			LOGGER.info(reqId);
			query = "select * from " + DataMigrationConstants.REQUESTS_TABLE
					+ " where requestid=?";
			PreparedStatement stmt = dbConn.con.prepareStatement(query);
			stmt.setInt(1, reqId);
			LOGGER.info(stmt.toString() + "!!!!!!!!!!!!");
			System.out.println(stmt.toString() + "!!!!!!!!!!!!");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				resultRequest = new RequestDetails();
				resultRequest.setRequestid(rs.getInt("requestid"));
				resultRequest.setSourcedatabase(rs.getString("sourcedatabase"));
				int serverId = rs.getInt("serverid");
				resultRequest.setServerid(serverId);
				ServerDAO sdao = new ServerDAO();
				ServerDetails serverdetails = sdao.getServerDetails(serverId,
						dbConn);
				resultRequest.setServerdetails(serverdetails);
				resultRequest.setServerhost(serverdetails.getHostname());
				resultRequest.setServername(serverdetails.getServername());
				resultRequest.setType(serverdetails.getServertype());
				resultRequest.setServerport(serverdetails.getPort());
				resultRequest.setSourcetables(rs.getString("sourcetables"));
				resultRequest.setCriteriacolumn(rs.getString("criteriacolumn"));
				resultRequest.setStoragetarget(rs.getString("storagetarget"));
				resultRequest.setStorageformat(rs.getString("storageformat"));
				resultRequest.setStoragecompression(rs
						.getString("storagecompression"));
				resultRequest.setMappers(rs.getInt("mappers"));
				resultRequest.setSplitbycolumn(rs.getString("splitbycolumn"));
				if (resultRequest.getStoragetarget().equalsIgnoreCase("hive")
						|| resultRequest.getStoragetarget().equalsIgnoreCase(
								"hdfs")) {
					resultRequest.setTargetdirectory(rs
							.getString("targetdirectory"));
				} else {
					resultRequest.setRowkey(rs.getString("rowkey"));
				}
				resultRequest.setRequestname(rs.getString("requestname"));
				resultRequest.setProvisionstatus(rs
						.getString("provisionstatus"));
				resultRequest.setUsername(rs.getString("username"));
				resultRequest.setPassword(rs.getString("password"));
				resultRequest.setRequestedby(rs.getString("requestedby"));
				resultRequest.setLoadtype(rs.getString("loadtype"));
			}
			LOGGER.info(resultRequest.getRequestid() + "##########");

			rs.close();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not Fetch the request details: "
					+ e.getMessage(), e);
		}
		return resultRequest;
	}

	public int generateRequestId() throws FRDMException {
		DbConnection dbConn = null;
		int newRequestId = 0;
		try {
			dbConn = DbConnection.getInstance();
			String query = null;
			int maxRequestId = 0;
			query = "select max(requestid) from "
					+ DataMigrationConstants.REQUESTS_TABLE;
			PreparedStatement stmt = dbConn.con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				maxRequestId = rs.getInt(1);
			}
			newRequestId = maxRequestId + 1;
			rs.close();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not Fetch the request details: "
					+ e.getMessage(), e);
		}
		return newRequestId;

	}

	public boolean isRequestDetailsValid(RequestDetails request)
			throws FRDMException {
		boolean flag = false;
		DbConnection dbConn = null;
		try {
			dbConn = DbConnection.getInstance();
			String query = null;
			int count = 0;

			query = "select count(*) from "
					+ DataMigrationConstants.REQUESTS_TABLE
					+ " where requestname=? ";
			if (request.getRequestid() != 0) {
				query = query + " or requestid !=" + request.getRequestid();
			}
			LOGGER.info(query);
			PreparedStatement stmt = dbConn.con.prepareStatement(query);
			stmt.setString(1, request.getRequestname());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if (count == 0) {
				flag = true;
			} else {
				flag = false;
			}

			rs.close();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not Fetch the request details: "
					+ e.getMessage(), e);
		}
		return flag;

	}

	public int isRequestDetailsValid_HBase(RequestDetails request)
			throws FRDMException {
		int count = 0;
		DbConnection dbConn = null;
		try {
			dbConn = DbConnection.getInstance();
			String query = null;
			// boolean flag=false;
			query = "select count(*) as count from "
					+ DataMigrationConstants.REQUESTS_TABLE
					+ " where hbasetable=? AND columnfamily =? ";

			PreparedStatement stmt = dbConn.con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt("count");
			}

			rs.close();
			stmt.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not Fetch the request details: "
					+ e.getMessage(), e);
		}
		return count;
	}

	@Override
	public List<RequestDetails> getList() throws FRDMException {
		// TODO Auto-generated method stub
		return null;
	}
}
