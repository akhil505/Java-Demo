package com.datamigration.services;

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
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.datamigration.dao.JobDao;
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
import com.datamigration.util.DataMigrationConstants;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@SuppressWarnings("serial")
@ManagedBean(name = "flumeService")
@ViewScoped
public class FlumeService implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(FlumeService.class);

	@ManagedProperty(value = "#{flumeBean}")
	private FlumeBean flumeBean;

	String STR_EQUALS = DataMigrationConstants.SPACE + DataMigrationConstants.EQUAL + DataMigrationConstants.SPACE;

	ResourceBundle bundle = ResourceBundle.getBundle("flafka");

	public void runJob(String loginId, ServerDetails selectedServer)
			throws ClassNotFoundException, SQLException, IOException, JSchException, SftpException {
		LOGGER.info("Starting of runJob()" + loginId);
		JobDetailsBean jobDetailsBean = new JobDetailsBean();
		jobDetailsBean.setConfFilePath(bundle.getString("JOB_CONF_FILE"));
		jobDetailsBean.setUserId(loginId);
		JobDao flumeDao = new JobDao();
		long jobid = flumeDao.getJobId(jobDetailsBean);
		LOGGER.info("End of runJob()" + loginId);
		runFlumeAgent(jobid, selectedServer);
	}

	private void runFlumeAgent(long jobId, ServerDetails selectedServer)
			throws IOException, JSchException, SftpException {
		LOGGER.info("Starting of runFlumeAgent()" + jobId);
		StringBuffer sb = new StringBuffer();
		String agntName = "agent_" + jobId;
		String srcName = "source_" + jobId;
		String sinkName = "sink_" + jobId;
		String chnlName = "channel_" + jobId;
		buildNames(sb, agntName, srcName, sinkName, chnlName);

		FlumeChannel flumeChannel = flumeBean.getFlumeChannel();
		FlumeSourceBean flumeSourceBean = flumeBean.getFlumeSourceBean();
		FlumeSinkBean flumeSinkBean = flumeBean.getFlumeSinkBean();

		buildSrcDtls(sb, flumeSourceBean, agntName, srcName);
		buildChnlDtls(sb, flumeChannel, agntName, chnlName);
		buildSinkDtls(sb, flumeSinkBean, agntName, sinkName);
		mapChannel(sb, agntName, srcName, sinkName, chnlName);
		String fileName = bundle.getString("LOCAL_FPATH") + jobId;
		Files.write(Paths.get(fileName), sb.toString().getBytes());
		String fileNm = copyFileToRemote(fileName, agntName, selectedServer);
		runFlumeJob(fileNm, agntName, selectedServer);
		LOGGER.info("End of runJob()" + jobId);
	}

	private void mapChannel(StringBuffer sb, String agntName, String srcName, String sinkName, String chnlName) {
		LOGGER.info("Starting of mapChannel()" + agntName);
		sb.append(agntName).append(DataMigrationConstants.PERIOD).append(DataMigrationConstants.STR_SOURCES)
				.append(DataMigrationConstants.PERIOD).append(srcName).append(DataMigrationConstants.PERIOD)
				.append(DataMigrationConstants.STR_CHANNELS).append(STR_EQUALS).append(chnlName)
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(agntName).append(DataMigrationConstants.PERIOD).append(DataMigrationConstants.STR_SINKS)
				.append(DataMigrationConstants.PERIOD).append(sinkName).append(DataMigrationConstants.PERIOD)
				.append(DataMigrationConstants.STR_CHANNEL).append(STR_EQUALS).append(chnlName)
				.append(DataMigrationConstants.LINE_SEP);
		LOGGER.info("End of mapChannel()" + agntName);
	}

	private void runFlumeJob(String fileName, String agntName, ServerDetails selectedServer)
			throws JSchException, IOException {
		LOGGER.info("Starting of runFlumeJob()" + fileName);
		JSch jsch = new JSch();
		StringBuilder outputBuffer = new StringBuilder();
		Session session = jsch.getSession(bundle.getString("FCONF_USUERNAME"), selectedServer.getHostname(),
				Integer.parseInt(selectedServer.getPort()));
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(bundle.getString("FCONF_PASSWORD"));
		session.connect();
		String flumeCommand = "sh " + bundle.getString("FLUME_NG_PATH") + " agent -n " + agntName + " -c  "
				+ bundle.getString("FLUME_NG_CONF") + " -f " + bundle.getString("FLUME_CONF_PATH") + fileName;
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		InputStream in = channelExec.getInputStream();
		channelExec.setCommand(flumeCommand);
		channelExec.connect();
		int readByte = in.read();
		while (readByte != 0xffffffff) {
			outputBuffer.append((char) readByte);
			readByte = in.read();
		}
		LOGGER.info("Log generated while executing flume" + outputBuffer.toString());

		LOGGER.info("Started flume job with query" + flumeCommand);

		channelExec.disconnect();

		session.disconnect();
		LOGGER.info("End of runFlumeJob()" + fileName);
	}

	private String copyFileToRemote(String fileName, String agntName, ServerDetails selectedServer)
			throws JSchException, SftpException, IOException {
		LOGGER.info("Starting of copyFileToRemote()" + fileName);
		JSch jsch = new JSch();
		Session session = jsch.getSession(bundle.getString("FCONF_USUERNAME"), selectedServer.getHostname(),
				Integer.parseInt(selectedServer.getPort()));
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(bundle.getString("FCONF_PASSWORD"));
		session.connect();
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp sftpChannel = (ChannelSftp) channel;
		sftpChannel.cd(bundle.getString("FLUME_CONF_PATH"));
		File f = new File(fileName);
		sftpChannel.put(new FileInputStream(f), f.getName());
		sftpChannel.exit();
		session.disconnect();
		LOGGER.info("End of copyFileToRemote()" + fileName);
		return f.getName();
	}

	private void buildNames(StringBuffer sb, String agntName, String srcName, String sinkName, String chnlName) {
		LOGGER.info("Starting of buildNames()" + agntName);
		String agnt = agntName + DataMigrationConstants.PERIOD;
		sb.append(agnt).append(DataMigrationConstants.STR_SOURCES).append(STR_EQUALS).append(srcName)
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(agnt).append(DataMigrationConstants.STR_SINKS).append(STR_EQUALS).append(sinkName)
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(agnt).append(DataMigrationConstants.STR_CHANNELS).append(STR_EQUALS).append(chnlName)
				.append(DataMigrationConstants.LINE_SEP);
		LOGGER.info("End of buildNames()" + agntName);
	}

	private void buildSrcDtls(StringBuffer sb, FlumeSourceBean flumeSourceBean, String agntName, String srcName) {
		LOGGER.info("Starting of buildSrcDtls()" + agntName);
		String srcSel = flumeSourceBean.getSrcSelected();
		String strSrc = agntName + DataMigrationConstants.PERIOD + DataMigrationConstants.STR_SOURCES
				+ DataMigrationConstants.PERIOD + srcName + DataMigrationConstants.PERIOD;
		switch (srcSel) {
		case DataMigrationConstants.SPOOL_DIR_SRC:
			buildSpoolDir(sb, flumeSourceBean.getSpoolDirectory(), agntName, srcName, strSrc);
			break;

		default:
			break;
		}
		LOGGER.info("End of buildSrcDtls()" + agntName);
	}

	private void buildSinkDtls(StringBuffer sb, FlumeSinkBean flumeSinkBean, String agntName, String sinkName) {
		LOGGER.info("Starting of buildSinkDtls()" + agntName);
		String sinkSel = flumeSinkBean.getSinkSelected();
		String strSink = agntName + DataMigrationConstants.PERIOD + DataMigrationConstants.STR_SINKS
				+ DataMigrationConstants.PERIOD + sinkName + DataMigrationConstants.PERIOD;
		switch (sinkSel) {
		case DataMigrationConstants.HDFS_SINK:
			buildHdfsSink(sb, flumeSinkBean.getHdfsSink(), agntName, strSink);
			break;

		default:
			break;
		}
		LOGGER.info("End of buildSinkDtls()" + agntName);
	}

	private void buildChnlDtls(StringBuffer sb, FlumeChannel flumeChannel, String agntName, String chnlName) {
		LOGGER.info("Starting of buildChnlDtls()" + agntName);
		String chnlSel = flumeChannel.getChnlSelected();
		String strChnl = agntName + DataMigrationConstants.PERIOD + DataMigrationConstants.STR_CHANNELS
				+ DataMigrationConstants.PERIOD + chnlName + DataMigrationConstants.PERIOD;
		switch (chnlSel) {
		case DataMigrationConstants.FILE_CHNL:
			buildFileChnl(sb, flumeChannel.getFileChannel(), agntName, strChnl);
			break;
		case DataMigrationConstants.MEM_CHNL:
			buildMemChnl(sb, flumeChannel.getMemChannel(), agntName, strChnl);
			break;
		default:
			break;
		}
		LOGGER.info("End of buildChnlDtls()" + agntName);
	}

	private void buildSpoolDir(StringBuffer sb, SpoolDirectory spoolDirectory, String agntName, String srcName,
			String strSrc) {
		LOGGER.info("Starting of buildSpoolDir()" + agntName);
		sb.append(strSrc).append("type").append(STR_EQUALS).append(spoolDirectory.getSrcType())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strSrc).append("spoolDir").append(STR_EQUALS).append(spoolDirectory.getSpoolDir())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strSrc).append("maxBackoff").append(STR_EQUALS).append(spoolDirectory.getMaxBackoff())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strSrc).append("batchSize").append(STR_EQUALS).append(spoolDirectory.getBatchSize())
				.append(DataMigrationConstants.LINE_SEP);
		LOGGER.info("End of buildSpoolDir()" + agntName);
	}

	private void buildHdfsSink(StringBuffer sb, HDFSSink hdfsSink, String agntName, String strSink) {
		LOGGER.info("Starting of buildHdfsSink()" + agntName);
		sb.append(strSink).append("type").append(STR_EQUALS).append(hdfsSink.getSinkType())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strSink).append("hdfs.path").append(STR_EQUALS).append(hdfsSink.getPath())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strSink).append("hdfs.rollInterval").append(STR_EQUALS).append(hdfsSink.getRollInterval())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strSink).append("hdfs.rollSize").append(STR_EQUALS).append(hdfsSink.getRollSize())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strSink).append("hdfs.rollCount").append(STR_EQUALS).append(hdfsSink.getRollCount())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strSink).append("hdfs.batchSize").append(STR_EQUALS).append(hdfsSink.getBatchSize())
				.append(DataMigrationConstants.LINE_SEP);
		if (hdfsSink.getMinBlockReplicas() > 0) {
			sb.append(strSink).append("hdfs.minBlockReplicas").append(STR_EQUALS).append(hdfsSink.getMinBlockReplicas())
					.append(DataMigrationConstants.LINE_SEP);
		}
		if (StringUtils.isNotBlank(hdfsSink.getFileSuffix())) {
			sb.append(strSink).append("hdfs.fileSuffix").append(STR_EQUALS).append(hdfsSink.getFileSuffix())
					.append(DataMigrationConstants.LINE_SEP);
		}
		if (StringUtils.isNotBlank(hdfsSink.getInUsePrefix())) {
			sb.append(strSink).append("hdfs.inUsePrefix").append(STR_EQUALS).append(hdfsSink.getInUsePrefix())
					.append(DataMigrationConstants.LINE_SEP);
		}
		if (StringUtils.isNotBlank(hdfsSink.getCodeC())) {
			sb.append(strSink).append("hdfs.codeC").append(STR_EQUALS).append(hdfsSink.getCodeC())
					.append(DataMigrationConstants.LINE_SEP);
		}

		LOGGER.info("End of buildHdfsSink()" + agntName);
	}

	private void buildMemChnl(StringBuffer sb, MemoryChannel memChannel, String agntName, String strChnl) {
		LOGGER.info("Starting of buildMemChnl()" + agntName);
		sb.append(strChnl).append("type").append(STR_EQUALS).append(memChannel.getChannelType())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strChnl).append("capacity").append(STR_EQUALS).append(memChannel.getCapacity())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strChnl).append("transactionCapacity").append(STR_EQUALS).append(memChannel.getTransactionCapacity())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strChnl).append("keep-alive").append(STR_EQUALS).append(memChannel.getKeepAlive())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strChnl).append("byteCapacityBufferPercentage").append(STR_EQUALS)
				.append(memChannel.getByteCapacityBufferPercentage()).append(DataMigrationConstants.LINE_SEP);
		LOGGER.info("End of buildMemChnl()" + agntName);
	}

	private void buildFileChnl(StringBuffer sb, FileChannel fileChannel, String agntName, String strChnl) {
		LOGGER.info("Starting of buildFileChnl()" + agntName);
		sb.append(strChnl).append("type").append(STR_EQUALS).append(fileChannel.getChannelType())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strChnl).append("transactionCapacity").append(STR_EQUALS).append(fileChannel.getTransactionCapacity())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strChnl).append("minimumRequiredSpace").append(STR_EQUALS)
				.append(fileChannel.getMinimumRequiredSpace()).append(DataMigrationConstants.LINE_SEP);
		sb.append(strChnl).append("capacity").append(STR_EQUALS).append(fileChannel.getCapacity())
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strChnl).append("keep-alive").append(STR_EQUALS).append(fileChannel.getKeepAlive())
				.append(DataMigrationConstants.LINE_SEP);
		LOGGER.info("End of buildFileChnl()" + agntName);
	}

	public FlumeBean getFlumeBean() {
		return flumeBean;
	}

	public void setFlumeBean(FlumeBean flumeBean) {
		this.flumeBean = flumeBean;
	}
}
