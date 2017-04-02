package com.datamigration.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.datamigration.model.JobDetailsBean;
import com.datamigration.util.DataMigrationConstants;

public class JobDao {
	final Logger LOGGER = Logger.getLogger(JobDao.class);

	public long getJobId(JobDetailsBean jobDetailsBean) throws ClassNotFoundException, SQLException {
		LOGGER.info("Starting of getJobId()" + jobDetailsBean.toString());
		long jobId = 0;
		DbConnection dbConn = DbConnection.getInstance();
		String query = "insert into " + DataMigrationConstants.STREAMMING_JOB_TABLE
				+ "(job_conf_file, job_status,user_id) values(?,?,?)";
		PreparedStatement stmt = dbConn.con.prepareStatement(query);
		stmt.setString(1, jobDetailsBean.getConfFilePath());
		stmt.setString(2, "STARTED");
		stmt.setString(3, jobDetailsBean.getUserId());

		int rowCnt = stmt.executeUpdate();
		if (rowCnt > 0) {
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			jobId = rs.getInt(1);
		}
		stmt.close();
		LOGGER.info("End of getJobId()");
		return jobId;
	}

	public List<JobDetailsBean> getJobDeatils(String loginid) throws ClassNotFoundException, SQLException {
		LOGGER.info("Starting of getJobDeatils()" + loginid);
		String selQuery = "select * from " + DataMigrationConstants.STREAMMING_JOB_TABLE + " where user_id='" + loginid + "'";
		DbConnection dbConn = DbConnection.getInstance();
		PreparedStatement stmt = dbConn.con.prepareStatement(selQuery);
		ResultSet rs = stmt.executeQuery(selQuery);
		List<JobDetailsBean> jobList = new ArrayList<JobDetailsBean>();
		while (rs.next()) {
			JobDetailsBean jobBean = new JobDetailsBean();
			jobBean.setConfFilePath(rs.getString("job_conf_file"));
			jobBean.setJobId(rs.getInt("job_id"));
			jobBean.setStatus(rs.getString("job_status"));
			jobBean.setUserId(rs.getString("user_id"));
			jobList.add(jobBean);
		}
		LOGGER.info("End of getJobDeatils()" + loginid);
		return jobList;
	}
}
