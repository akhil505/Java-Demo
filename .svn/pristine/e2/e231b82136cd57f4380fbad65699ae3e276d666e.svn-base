package com.datamigration.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import com.datamigration.dao.JobDao;
import com.datamigration.dao.ServerDAO;
import com.datamigration.model.FileChannel;
import com.datamigration.model.FlumeBean;
import com.datamigration.model.FlumeChannel;
import com.datamigration.model.FlumeSinkBean;
import com.datamigration.model.FlumeSourceBean;
import com.datamigration.model.HDFSSink;
import com.datamigration.model.JobDetailsBean;
import com.datamigration.model.MemoryChannel;
import com.datamigration.model.ServerDetails;
import com.datamigration.model.SpoolDirectory;
import com.datamigration.services.FlumeService;
import com.datamigration.util.DataMigrationConstants;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@SuppressWarnings("serial")
@ManagedBean(name = "streamingUtil")
@ViewScoped
public class StreamingUtil implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(StreamingUtil.class);

	public static List<JobDetailsBean> getUserRequests(String loginid) throws ClassNotFoundException, SQLException {
		LOGGER.info("Starting of getUserRequests()" + loginid);
		JobDao jobDao = new JobDao();
		List<JobDetailsBean> jobList = jobDao.getJobDeatils(loginid);
		LOGGER.info("End of getUserRequests()" + loginid);
		return jobList;
	}

	public static List<ServerDetails> getServerDetails() throws FRDMException {
		LOGGER.info("Starting of getServetDetails()");
		ServerDAO serverDAO = new ServerDAO();
		List<ServerDetails> serverList = null;
		try {
			serverList = serverDAO.getList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("End of getUserRequests()");
		return serverList;
	}

}
