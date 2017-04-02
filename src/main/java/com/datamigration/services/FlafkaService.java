package com.datamigration.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.omg.CORBA.DATA_CONVERSION;

import com.datamigration.dao.JobDao;
import com.datamigration.model.FileChannel;
import com.datamigration.model.FlumeBean;
import com.datamigration.model.FlumeChannel;
import com.datamigration.model.FlumeSinkBean;
import com.datamigration.model.FlumeSourceBean;
import com.datamigration.model.HDFSSink;
import com.datamigration.model.JobDetailsBean;
import com.datamigration.model.KafkaSink;
import com.datamigration.model.MemoryChannel;
import com.datamigration.model.ServerDetails;
import com.datamigration.model.SpoolDirectory;
import com.datamigration.services.RequestsLogService;
import com.datamigration.util.DataMigrationConstants;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@SuppressWarnings("serial")
@ManagedBean(name = "flafkaService")
@ViewScoped
public class FlafkaService implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(FlafkaService.class);

	@ManagedProperty(value = "#{flumeBean}")
	private FlumeBean flumeBean;

	String STR_EQUALS = DataMigrationConstants.SPACE + DataMigrationConstants.EQUAL + DataMigrationConstants.SPACE;
	ResourceBundle bundle = ResourceBundle.getBundle("flafka");

	public void runJob(String loginid, ServerDetails selectedServer)
			throws ClassNotFoundException, SQLException, IOException, JSchException, SftpException, URISyntaxException {
		LOGGER.info("Starting of runJob()");
		JobDetailsBean jobDetailsBean = new JobDetailsBean();
		jobDetailsBean.setConfFilePath(bundle.getString("JOB_CONF_FILE"));
		jobDetailsBean.setUserId(loginid);
		JobDao jobDao = new JobDao();
		long jobid = jobDao.getJobId(jobDetailsBean);
		createKafkaTopic("kafkatopic" + jobid, selectedServer);
		FlumeSourceBean flumeSourceBean = flumeBean.getFlumeSourceBean();
		FlumeSinkBean flumeSinkBean = flumeBean.getFlumeSinkBean();
		runFlumeAgent(jobid, selectedServer, flumeSourceBean, flumeSinkBean);
		runKafka("kafkatopic" + jobid, selectedServer, flumeSourceBean.isEncryptReqrd(), flumeSinkBean.isParquetReqrd(),
				flumeSinkBean.getHdfsSink().getPath());
		LOGGER.info("End of runJob()");
	}

	private void createKafkaTopic(String topicName, ServerDetails selectedServer) throws JSchException, IOException {
		JSch jsch = new JSch();
		Session session = jsch.getSession(bundle.getString("FCONF_USUERNAME"), selectedServer.getHostname(),
				Integer.parseInt(selectedServer.getPort()));
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(bundle.getString("FCONF_PASSWORD"));
		session.connect();
		String createKafkaTopic = "sh " + bundle.getString("KAFKA_BIN_PATH") + bundle.getString("TOPIC_PATH")
				+ " --create --zookeeper " + bundle.getString("KAFKA_HOST") + DataMigrationConstants.COLON
				+ bundle.getString("KAFKA_PORT") + " --replication-factor "
				+ bundle.getString("KAFKA_REPLICATION_FACTOR") + " --partitions " + bundle.getString("KAFKA_PARTIONS")
				+ " --topic " + topicName;

		ChannelExec kafkaExec = (ChannelExec) session.openChannel("exec");
		InputStream kafkaIn = kafkaExec.getInputStream();
		kafkaExec.setCommand(createKafkaTopic);
		kafkaExec.connect();
		LOGGER.info("Created topic with command" + createKafkaTopic);
		kafkaExec.disconnect();
		LOGGER.info("Session terminated" + createKafkaTopic);
	}

	private void runKafka(String topicName, ServerDetails selectedServer, boolean encryptRequired,
			boolean parquetRequired, String hdfsPath) throws IOException, URISyntaxException, JSchException {
		LOGGER.info("Starting of runKafka() with topic " + topicName + "Encryption" + encryptRequired
				+ "Parquet required" + parquetRequired);
		JSch jsch = new JSch();
		Session session = jsch.getSession(bundle.getString("FCONF_USUERNAME"), selectedServer.getHostname(),
				Integer.parseInt(selectedServer.getPort()));
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(bundle.getString("FCONF_PASSWORD"));
		session.connect();
		String kafkaCommand = "hadoop jar " + bundle.getString("consumer_Jar_path") + DataMigrationConstants.SPACE
				+ encryptRequired + DataMigrationConstants.SPACE + parquetRequired + DataMigrationConstants.SPACE
				+ hdfsPath + DataMigrationConstants.SPACE + topicName;
		LOGGER.info("kafka starting with command" + kafkaCommand);
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		InputStream in = channelExec.getInputStream();
		channelExec.setCommand(kafkaCommand);
		channelExec.connect();
		in.close();
		LOGGER.info("End of runKafka()");
	}

	private void runFlumeAgent(long jobId, ServerDetails selectedServer, FlumeSourceBean flumeSourceBean,
			FlumeSinkBean flumeSinkBean) throws IOException, JSchException, SftpException, URISyntaxException {
		LOGGER.info("Starting of runFlumeAgent() job id" + jobId);

		StringBuffer sb = new StringBuffer();
		String agntName = "agent_" + jobId;
		String srcName = "source_" + jobId;
		String sinkName = "sink_" + jobId;
		String chnlName = "channel_" + jobId;
		buildNames(sb, agntName, srcName, sinkName, chnlName);

		FlumeChannel flumeChannel = flumeBean.getFlumeChannel();

		buildSrcDtls(sb, flumeSourceBean, agntName, srcName);
		buildChnlDtls(sb, flumeChannel, agntName, chnlName);
		buildSinkDtls(sb, flumeSinkBean, agntName, sinkName, "kafkatopic" + jobId);
		mapChannel(sb, agntName, srcName, sinkName, chnlName);
		String fileName = bundle.getString("LOCAL_FPATH") + jobId;
		Files.write(Paths.get(fileName), sb.toString().getBytes());
		String fileNm = copyFileToRemote(fileName, agntName);
		runFlumeJob(fileNm, agntName, "kafkatopic" + jobId, selectedServer);
		LOGGER.info("End of runFlumeAgent() job id" + jobId);
	}

	private void runFlumeJob(String fileName, String agntName, String topicName, ServerDetails selectedServer)
			throws JSchException, IOException, URISyntaxException {
		LOGGER.info("Starting of runFlumeJob()" + agntName);
		JSch jsch = new JSch();
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
		in.close();
		LOGGER.info("Started flume with command" + flumeCommand);
		/*
		 * channelExec.disconnect(); session.disconnect();
		 */
		LOGGER.info("End of runFlumeJob() " + agntName);
	}

	private String copyFileToRemote(String fileName, String agntName) throws JSchException, SftpException, IOException {
		LOGGER.info("Starting of copyFileToRemote()" + agntName);
		JSch jsch = new JSch();
		Session session = jsch.getSession("root", "10.138.90.227", 22);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword("nextgen");
		session.connect();
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp sftpChannel = (ChannelSftp) channel;
		sftpChannel.cd(bundle.getString("FLUME_CONF_PATH"));
		File f = new File(fileName);
		sftpChannel.put(new FileInputStream(f), f.getName());
		sftpChannel.exit();
		session.disconnect();
		LOGGER.info("End of copyFileToRemote()" + agntName);
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
		LOGGER.info("Starting of buildNames()" + agntName);
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

		if (flumeSourceBean.isEncryptReqrd()) {
			buidEncryptInterecptor(sb, strSrc);
		}
		LOGGER.info("Starting of buildSrcDtls()" + agntName);
	}

	private void buidEncryptInterecptor(StringBuffer sb, String strSrc) {
		LOGGER.info("Starting of buildSrcDtls()" + strSrc);
		sb.append(strSrc).append("interceptors").append(STR_EQUALS).append("EncryptIntercptor")
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strSrc).append("interceptors").append(DataMigrationConstants.PERIOD).append("EncryptIntercptor")
				.append(DataMigrationConstants.PERIOD).append("type").append(STR_EQUALS)
				.append("com.flafka.custom.interceptor.EncryptionInterceptor$Builder")
				.append(DataMigrationConstants.LINE_SEP);
		LOGGER.info("Starting of buildSrcDtls()" + strSrc);
	}

	private void buildSinkDtls(StringBuffer sb, FlumeSinkBean flumeSinkBean, String agntName, String sinkName,
			String topicName) {
		LOGGER.info("Starting of buildSinkDtls()" + agntName);
		String strSink = agntName + DataMigrationConstants.PERIOD + DataMigrationConstants.STR_SINKS
				+ DataMigrationConstants.PERIOD + sinkName + DataMigrationConstants.PERIOD;
		buildKafkaSink(sb, flumeSinkBean.getKafkaSink(), agntName, strSink, topicName);
		LOGGER.info("Starting of buildSinkDtls()" + agntName);
	}

	private void buildKafkaSink(StringBuffer sb, KafkaSink kafkaSink, String agntName, String strSink,
			String topicName) {
		LOGGER.info("Starting of buildKafkaSink()" + agntName);
		sb.append(strSink).append("type").append(STR_EQUALS).append("org.apache.flume.sink.kafka.KafkaSink")
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(strSink).append("topic").append(STR_EQUALS).append(topicName).append(DataMigrationConstants.LINE_SEP);
		sb.append(strSink).append("brokerList").append(STR_EQUALS).append(kafkaSink.getBrokerList())
				.append(DataMigrationConstants.LINE_SEP);
		LOGGER.info("End of buildKafkaSink()" + agntName);
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

	private void mapChannel(StringBuffer sb, String agntName, String srcName, String sinkName, String chnlName) {
		LOGGER.info("Starting of mapChannel() job id" + agntName);
		sb.append(agntName).append(DataMigrationConstants.PERIOD).append(DataMigrationConstants.STR_SOURCES)
				.append(DataMigrationConstants.PERIOD).append(srcName).append(DataMigrationConstants.PERIOD)
				.append(DataMigrationConstants.STR_CHANNELS).append(STR_EQUALS).append(chnlName)
				.append(DataMigrationConstants.LINE_SEP);
		sb.append(agntName).append(DataMigrationConstants.PERIOD).append(DataMigrationConstants.STR_SINKS)
				.append(DataMigrationConstants.PERIOD).append(sinkName).append(DataMigrationConstants.PERIOD)
				.append(DataMigrationConstants.STR_CHANNEL).append(STR_EQUALS).append(chnlName)
				.append(DataMigrationConstants.LINE_SEP);
		LOGGER.info("End of mapChannel() job id" + agntName);
	}

	public FlumeBean getFlumeBean() {
		return flumeBean;
	}

	public void setFlumeBean(FlumeBean flumeBean) {
		this.flumeBean = flumeBean;
	}
}
