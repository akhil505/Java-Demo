package com.datamigration.dao;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import com.datamigration.model.RequestDetails;
import com.datamigration.model.RequestLogDetails;
import com.datamigration.util.DataMigrationConstants;
import com.datamigration.util.FRDMException;
import com.datamigration.util.HDFSUtil;
import com.datamigration.util.ShellUtil;
import com.jcraft.jsch.JSchException;

public class ReconDAO {
	final Logger LOGGER = Logger.getLogger(ReconDAO.class);

	public String executeReconProcess(String logPath, String parameters,
			int reqLogId) throws FRDMException {
		String statusCode = DataMigrationConstants.FAILED;
		try {
			HDFSUtil util = new HDFSUtil();
			util.writeToOutPutStream(logPath, new Date()
					+ " INFO: Recon process started" + "\n");
			String sparkJobCommand = "spark-submit "
					+ "--class com.recon.main.Working "
					+ "--master local --files /etc/hive/conf/hive-site.xml --driver-class-path hive-site.xml"
					+ " /home/498796/Reconciliation.jar " + parameters;
			ArrayList<String> commands = new ArrayList<String>();
			commands.add(0, "kinit -kt " + DataMigrationConstants.KEYTAB + " "
					+ DataMigrationConstants.PRINCIPAL);
			commands.add(1, sparkJobCommand);
			statusCode = ShellUtil.executeShellCommands(commands, logPath);
		} catch (IOException | JSchException | InvalidKeyException
				| NoSuchAlgorithmException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException
				| InvalidKeySpecException e) {
			throw new FRDMException("Could not execute Recon process: "
					+ e.getMessage(), e);
		}
		LOGGER.info("Recon Status: " + statusCode);
		return statusCode;

	}

	public String getReconParameters(RequestDetails request,
			RequestLogDetails requestLogDetails) {

		String misMatchTable = requestLogDetails.getMismatchtable();
		String tableName = requestLogDetails.getTablename();
		if (misMatchTable.isEmpty()) {
			misMatchTable = tableName;
		}
		return request.getServerhost() + "," + request.getServerport() + ","
				+ request.getType() + "," + request.getSourcedatabase() + ","
				+ tableName + "," + requestLogDetails.getRequesttype() + ","
				+ request.getCriteriacolumn() + ","
				+ requestLogDetails.getStartdate() + ","
				+ requestLogDetails.getEnddate() + ","
				+ request.getStoragetarget() + "," + request.getUsername()
				+ "," + request.getPassword() + ","
				+ requestLogDetails.getReconfile() + "," + request.getRowkey()
				+ "," + requestLogDetails.getRequestlogid() + ","
				+ misMatchTable;

	}
}
