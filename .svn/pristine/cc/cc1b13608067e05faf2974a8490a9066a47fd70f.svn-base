package com.datamigration.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.datamigration.model.MetaDataConnectionDetails;
import com.datamigration.util.FRDMException;

/**
 * 
 * @author DESS
 *
 */
@SuppressWarnings("unchecked")
public class GetMetaDataDAO {
	static final Logger LOGGER = Logger.getLogger(GetMetaDataDAO.class);

	/**
	 * 
	 * @param obj
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ArrayList<String> getDataBases(MetaDataConnectionDetails metaDetails)
			throws FRDMException {
		final ArrayList<String> databaseList = new ArrayList<String>();
		Connection dbConn;
		try {
			dbConn = DbConnection.getConnection(metaDetails);
			final DatabaseMetaData databaseMetaData = dbConn.getMetaData();

			final ResultSet catlogsRs = databaseMetaData.getCatalogs();

			while (catlogsRs.next()) {
				databaseList.add(catlogsRs.getString("TABLE_CAT"));
			}
			catlogsRs.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not fetch database list: "
					+ e.getMessage());
		}

		return databaseList;

	}

	/**
	 * 
	 * @param obj
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ArrayList<String> getTables(MetaDataConnectionDetails metaDetails)
			throws FRDMException {
		LOGGER.info("getTables=====:" + metaDetails.getDbname());
		final ArrayList<String> tablesList = new ArrayList<String>();
		String databaseName = metaDetails.getDbname();
		Connection dbConn;
		try {
			dbConn = DbConnection.getConnection(metaDetails);
			final DatabaseMetaData databaseMetaData = dbConn.getMetaData();

			final ResultSet tablesRs = databaseMetaData.getTables(databaseName,
					null, "%", null);

			while (tablesRs.next()) {
				tablesList.add(tablesRs.getString(3));
			}
			tablesRs.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not fetch tables list: "
					+ e.getMessage());
		}

		return tablesList;

	}

	/**
	 * 
	 * @param obj
	 * @param tableName
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ArrayList<JSONObject> getTableDetails(
			MetaDataConnectionDetails metaDetails, String tableName)
			throws FRDMException {
		LOGGER.info("getColumns=====:" + tableName);
		final ArrayList<JSONObject> columnDetailsList = new ArrayList<JSONObject>();
		String primaryKey = null;

		Connection dbConn;
		try {
			dbConn = DbConnection.getConnection(metaDetails);
			final DatabaseMetaData databaseMetaData = dbConn.getMetaData();

			final ResultSet primarKeysRs = databaseMetaData.getPrimaryKeys(
					null, null, tableName);
			while (primarKeysRs.next()) {
				primaryKey = primarKeysRs.getString("COLUMN_NAME");
				break;
			}
			primarKeysRs.close();

			final ResultSet columnsResultSet = databaseMetaData.getColumns(
					null, null, tableName, null);

			while (columnsResultSet.next()) {
				final JSONObject jsonObj = new JSONObject();
				if (columnsResultSet.getString("COLUMN_NAME").equalsIgnoreCase(
						primaryKey)) {
					jsonObj.put("ColumnName",
							columnsResultSet.getString("COLUMN_NAME"));
					jsonObj.put("PrimaryKey", "YES");
					jsonObj.put("DataType",
							columnsResultSet.getString("TYPE_NAME"));
				}

				else {
					jsonObj.put("ColumnName",
							columnsResultSet.getString("COLUMN_NAME"));
					jsonObj.put("PrimaryKey", "NO");
					jsonObj.put("DataType",
							columnsResultSet.getString("TYPE_NAME"));
				}
				columnDetailsList.add(jsonObj);
			}

			columnsResultSet.close();
		} catch (SQLException | ClassNotFoundException e) {
			throw new FRDMException("Could not fetch table details: "
					+ e.getMessage());
		}

		return columnDetailsList;

	}

}
