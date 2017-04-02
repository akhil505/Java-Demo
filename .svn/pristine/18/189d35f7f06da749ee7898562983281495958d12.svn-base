package com.datamigration.dao;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.SchemaBuilder.FieldAssembler;
import org.apache.log4j.Logger;

import com.datamigration.model.MetaDataConnectionDetails;
import com.datamigration.model.RequestDetails;
import com.datamigration.util.DataMigrationConstants;
import com.datamigration.util.FRDMException;
import com.datamigration.util.PasswordCryption;

public class HiveDAO {
	final Logger LOGGER = Logger.getLogger(HiveDAO.class);

	public String generateHiveCreateQuery(RequestDetails sb,
			String tempTableName) throws SQLException, ClassNotFoundException,
			InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeySpecException {
		MetaDataConnectionDetails details = new MetaDataConnectionDetails();
		details.setDbname(sb.getSourcedatabase());
		details.setHostname(sb.getServerhost());
		ResourceBundle bundle = ResourceBundle.getBundle("parameters");
		String decryptPwd = PasswordCryption.decrypt(sb.getPassword(),
				bundle.getString("key"));
		details.setPassword(decryptPwd);
		details.setPort(sb.getServerport());
		details.setServertype(sb.getType());
		details.setUsername(sb.getUsername());
		LOGGER.info(details.getServertype());
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
			query = "select * from " + sb.getSourcedatabase() + "."
					+ sb.getSourcetable();
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

			storageFormat = sb.getStorageformat();
			if (tempTableName.isEmpty()) {
				tempTableName = sb.getSourcetable();
			}
			String targetDirectory = sb.getTargetdirectory() + "/"
					+ tempTableName;
			if (storageFormat.equalsIgnoreCase("text")) {
				hivestmt = " CREATE external table if not exists "
						+ sb.getSourcedatabase() + "." + tempTableName + "("
						+ column + ")"
						+ " row format delimited fields terminated by '" + ","
						+ "' lines terminated by " + "'\\n'" + " location "
						+ "'" + targetDirectory + "'";
			} else {
				if (storageFormat.equalsIgnoreCase("avro")) {
					String avscFilePath = hivedao.generateAvroSchema(sb,
							columnDetails);
					hivestmt = "create external table if not exists "
							+ sb.getSourcedatabase()
							+ "."
							+ tempTableName
							+ " row format serde 'org.apache.hadoop.hive.serde2.avro.AvroSerDe'stored as"
							+ "\n"
							+ " inputformat 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat'"
							+ "\n"
							+ " outputformat 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'"
							+ "\n" + " location " + "'" + targetDirectory + "'"
							+ "\n" + " tblproperties ('avro.schema.literal'='"
							+ avscFilePath + "')" + "\n";

				}
			}

			LOGGER.info(hivestmt + "!!!!!!!!!!!!!!!!!");
		} finally {
			if (dbConn != null)
				dbConn.close();
		}

		return hivestmt;

	}

	public String createHiveTable(RequestDetails sqoop, String tableName)
			throws FRDMException {
		String query;
		String flag = DataMigrationConstants.FAILED;
		HiveDAO hivedao = new HiveDAO();
		Connection hiveconnection = null;
		Statement stmt = null;
		try {
			query = hivedao.generateHiveCreateQuery(sqoop, tableName);
			hiveconnection = DbConnection.getHiveConnection(sqoop
					.getSourcedatabase());
			String createDB = "CREATE DATABASE IF NOT EXISTS "
					+ sqoop.getSourcedatabase();
			LOGGER.info("Create Database statement: " + createDB);
			stmt = hiveconnection.createStatement();
			try {
				stmt.execute(createDB);
				LOGGER.info("Create Table statement: " + query);
				stmt.execute(query);
				flag = DataMigrationConstants.SUCCESS;
			} catch (SQLException e) {
				LOGGER.info("Hive table not created", e);
			}
			LOGGER.info("Created Table " + flag);
			stmt.close();
		} catch (SQLException | InvalidKeyException | ClassNotFoundException
				| NoSuchAlgorithmException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException
				| InvalidKeySpecException e) {
			throw new FRDMException("Could not create hive table:"
					+ e.getMessage(), e);
		} finally {
			if (hiveconnection != null) {
				try {
					hiveconnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	public String generateAvroSchema(RequestDetails sb, ArrayList<String> Column) {

		Schema recordSchema = null;
		FieldAssembler<Schema> fieldAssembler = null;
		String colName = "";
		String colType = "";
		String tableName = sb.getSourcetable();
		String dbName = sb.getSourcedatabase();
		fieldAssembler = SchemaBuilder.record(tableName)
				.namespace("org.apache.avro." + dbName.toLowerCase()).fields();
		for (String colSplit : Column) {
			colType = colSplit.split("\t")[1].toLowerCase().trim();
			colName = colSplit.split("\t")[0].trim();
			switch (colType) {
			case "bigint":
			case "smallint":
			case "mediumint":
			case "int":
			case "tinyint":
				fieldAssembler.name(colName).type().nullable().intType()
						.noDefault();
				break;
			case "bit":
				fieldAssembler.name(colName).type().nullable().booleanType()
						.noDefault();
				break;
			case "real":
			case "numeric":
			case "decimal":
			case "double":
				fieldAssembler.name(colName).type().nullable().doubleType()
						.noDefault();
				break;
			case "float":
				fieldAssembler.name(colName).type().nullable().floatType()
						.noDefault();
				break;
			case "long":
				fieldAssembler.name(colName).type().nullable().longType()
						.noDefault();
				break;

			default:

				fieldAssembler.name(colName).type().nullable().stringType()
						.noDefault();
				break;
			}
		}
		recordSchema = fieldAssembler.endRecord();

		return recordSchema.toString();
	}

	public boolean isTableExists(String dbname, String tableName) {
		boolean flag = false;
		String query = "select * from " + tableName;
		Connection dbConn = null;
		try {
			dbConn = DbConnection.getHiveConnection(dbname);
			Statement stmt = dbConn.prepareStatement(query);
			stmt.executeQuery(query);
			flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		} finally {
			if (dbConn != null) {
				try {
					dbConn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
				}
			}
		}
		return flag;
	}

}
