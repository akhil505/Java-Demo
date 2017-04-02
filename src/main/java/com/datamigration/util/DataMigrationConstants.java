package com.datamigration.util;

public interface DataMigrationConstants {
	final static String SERVERS_TABLE = "serverdetails";
	final static String REQUESTS_TABLE = "requestdetails";
	final static String REQUESTLOG_TABLE = "requestlog";
	final static int PORT = 22;

	final static String HIVE_DRIVER_CLASS = "org.apache.hive.jdbc.HiveDriver";
	final static String HIVE_CONNECTION_PREFIX = "jdbc:hive2://";
	final static String HIVE_HOSTNAME = "10.138.90.202";
	final static String HIVE_PORT = "10000";
	final static String HIVE_CONNECTION_DBNAME = "default";
	final static String SHELL_HOST_NAME = "10.138.90.224";
	final static String AVRO_SCHEMA_FILE_PATH = "avroschema/";
	final static String UNDERSCORE_DELIMITER = "_";
	static final String SHELL_USER = "498796";
	final static String SHELL_PASSWORD = "BzsKq5SicQB9o5PoNPF9uQ==";
	final static String SERVERS_TABLE_ID = "serverid";
	final static String SERVERS_TABLE_SERVER_TYPE = "servertype";
	final static String SERVERS_TABLE_HOST = "hostname";
	final static String SERVERS_TABLE_PORT = "port";
	final static String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
	final static String MYSQL_CONNECTION_PREFIX = "jdbc:mysql://";
	final static String MYSQL_HOSTNAME = "10.138.90.224";
	final static String MYSQL_PORT = "3306";
	final static String METADATA_DATABASE_NAME = "datamigration";
	final static String USER_NAME = "root";
	final static String PASSWORD = "mariadb";
	final static String LOG_DIR = "/tmp/Datamigration/logs";
	final static String SNAPPY_CODEC_CLASS = "org.apache.hadoop.io.compress.SnappyCodec";
	final static String HBASE_DEFAULT_COLUMN_FAMILY = "data";
	final static String DELIMITER = "|";

	final static String ADHOC_LOAD_TYPE_STRING = "adhoc";
	final static String FULL_LOAD_TYPE_STRING = "full";
	final static String DAILY_LOAD_TYPE_STRING = "daily";
	final static String MONTHLY_LOAD_TYPE_STRING = "monthly";
	final static String HOURLY_LOAD_TYPE_STRING = "hourly";
	static final String ENCRYPT_ALGORITHM = "AES/ECB/PKCS5Padding";
	static final String RUNNING = "RUNNING";
	static final String INACTIVE = "INACTIVE";
	static final String COMPLETED = "COMPLETED";
	static final String FAILED = "FAILED";
	static final String HIVE_PRINCIPAL = "hive/01hw507402.tcshydnextgen.com@TCSHYDNEXTGEN.COM";
	static final String KEYTAB = "/home/498796/498796.keytab";
	static final String PRINCIPAL = "498796@TCSHYDNEXTGEN.COM";
	static final String SUCCESS = "SUCCESS";
	static final String HIVE = "hive";
	static final String HDFS = "hdfs";
	static final String HBASE = "hbase";
	static final String MESSAGE = "Message";

	// flume constants

	final static String PERIOD = ".";
	final static String SPACE = " ";
	final static String EQUAL = "=";
	final static String LINE_SEP = System.lineSeparator();

	final static String STR_SOURCES = "sources";
	final static String STR_SINKS = "sinks";
	final static String STR_CHANNELS = "channels";
	final static String STR_CHANNEL = "channel";

	final static String JOB_STARTED = "STARTED";
	final static String SPOOL_DIR_SRC = "Spooling Directory";
	final static String EXEC_SRC = "Exec";
	final static String FILE_CHNL = "File";
	final static String MEM_CHNL = "Memory";
	final static String HDFS_SINK = "hdfs";
	final static String HIVE_SINK = "hive";
	final static String KAFKA_SINK = "Kafka";
	final static String COLON = ":";

	static final String STREAMMING_JOB_TABLE = "job_stream_tbl";
	static final String USERS_TABLE = "users";

}
