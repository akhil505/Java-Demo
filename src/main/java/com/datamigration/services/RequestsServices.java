package com.datamigration.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.datamigration.dao.ReconDAO;
import com.datamigration.dao.RequestDAO;
import com.datamigration.dao.RequestLogDao;
import com.datamigration.dao.SchemaCheckDAO;
import com.datamigration.dao.SqoopDAO;
import com.datamigration.model.RequestDetails;
import com.datamigration.model.RequestLogDetails;
import com.datamigration.util.DataMigrationConstants;
import com.datamigration.util.FRDMException;
import com.datamigration.util.HDFSUtil;

/**
 * @author DESS Web service for accessing Requests
 */
@SuppressWarnings("unchecked")
@Path("/RequestsServices")
public class RequestsServices {
	/** logger **/
	private static final Logger LOGGER = Logger
			.getLogger(RequestsServices.class);
	/***/
	private static int statusCode = 400;
	/***/
	private static String response;
	/***/
	private static SimpleDateFormat dateTimeSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	/***/
	private static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd",
			Locale.ENGLISH);

	/**
	 * Web Service to get requests created by a user
	 * 
	 * @param user
	 * @return
	 */
	@GET
	@Path("/requestList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestsServices(@QueryParam("user") final String user) {
		LOGGER.info("requestList service invocation started!!!");
		List<RequestDetails> requests;
		try {
			requests = new RequestDAO().getList(user);
			JSONArray array = new JSONArray();
			for (RequestDetails request : requests) {
				JSONObject requestJson = request.toJson(request);
				array.add(requestJson);
			}
			statusCode = 200;
			response = array.toString();
		} catch (FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put(DataMigrationConstants.MESSAGE, e.getMessage());
			response = jsonObj.toString();
		}
		return Response.status(statusCode).entity(response).build();
	}

	/**
	 * Web service to add/update a request
	 * 
	 * @param json
	 * @return
	 */
	@POST
	@Path("/addRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUpdateRequest(final String json) {
		LOGGER.info("addRequest service invocation started!!!");
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			RequestDetails request = new RequestDetails().fromJson(jsonObj);
			boolean flag = new RequestDAO().isRequestDetailsValid(request);
			final int requestId = request.getRequestid();
			if (flag && requestId == 0 || !flag && requestId != 0) {
				new RequestDAO().addUpdate(request);
				jsonObj = request.toJson(request);
				response = jsonObj.toString();
				statusCode = 200;
			} else {
				jsonObj = new JSONObject();
				jsonObj.put(DataMigrationConstants.MESSAGE,
						"Request Name already exists for other request");
				response = jsonObj.toString();
			}

		} catch (ParseException | FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put(DataMigrationConstants.MESSAGE, e.getMessage());
			response = jsonObj.toString();
		}
		return Response.status(statusCode).entity(response).build();
	}

	/**
	 * Web Service to delete a request
	 * 
	 * @param requestId
	 * @return
	 */
	@GET
	@Path("/deleteRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteRequest(@QueryParam("id") final int requestId) {
		LOGGER.info("deleteRequest service invocation started!!!");
		JSONObject jsonobject = new JSONObject();
		try {
			final int deleteStatus = new RequestDAO().delete((long) requestId);
			LOGGER.info(deleteStatus);
			if (deleteStatus > 0) {
				jsonobject.put(DataMigrationConstants.MESSAGE, "RequestId:"
						+ requestId + " has been deleted Successfully");
				statusCode = 200;
			} else {
				jsonobject.put(DataMigrationConstants.MESSAGE, "RequestId:"
						+ requestId + " could not be deleted");
			}
		} catch (FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			jsonobject = new JSONObject();
			jsonobject.put(DataMigrationConstants.MESSAGE, e.getMessage());
		}
		response = jsonobject.toString();
		return Response.status(statusCode).entity(response).build();
	}

	/**
	 * Web Service to get request details based on requestId
	 * 
	 * @param requestId
	 * @return
	 */
	@GET
	@Path("/getRequestById")
	public Response getRequestById(@QueryParam("id") final int requestId) {
		LOGGER.info("getRequestById service invocation started!!!");
		JSONObject jsonobj = new JSONObject();
		try {
			RequestDetails request = new RequestDAO().getDetails(requestId);
			if (request == null) {
				jsonobj.put(DataMigrationConstants.MESSAGE,
						"The " + jsonobj.get("RequestId")
								+ " RequestId does not exist.");
			} else {
				jsonobj.putAll(request.toJson(request));
				statusCode = 200;
			}
		} catch (FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			jsonobj = new JSONObject();
			jsonobj.put(DataMigrationConstants.MESSAGE, e.getMessage());
		}
		response = jsonobj.toString();
		return Response.status(statusCode).entity(response).build();
	}

	/**
	 * @param requestId
	 * @param logId
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/runSqoop")
	public Response runSqoop(@QueryParam("id") final int requestId,
			@QueryParam("logId") final int logId) {
		LOGGER.info("runSqoop service invocation started!!!");
		String status = DataMigrationConstants.RUNNING;
		JSONArray responseJson = new JSONArray();
		try {
			RequestDetails request = new RequestDetails();
			RequestLogDetails requestLogDetails = new RequestLogDetails();
			String[] tables;
			// Fetch request details
			LOGGER.info("Fetching request details");
			if (logId != 0) { // Rerun Request
				LOGGER.info("Rerunning the request log id:" + logId);
				requestLogDetails = new RequestLogDao().getDetails(logId);
				request = new RequestDAO().getDetails(requestLogDetails
						.getRequestid());
				tables = requestLogDetails.getTablename().split(",");
			} else {
				request = new RequestDAO().getDetails(requestId);
				tables = request.getSourcetables().split(",");
			}
			LOGGER.info("Updating the status of request as RUNNING");
			request.setProvisionstatus(status); // Update status of request as
			new RequestDAO().addUpdate(request); // RUNNING.
			for (String table : tables) {
				String reconStatus = "Not Started";
				try {
					String hdfspath = ResourceBundle.getBundle("parameters")
							.getString("hdfspath");
					String datepart = dateSdf.format(new Date(System
							.currentTimeMillis()));
					HDFSUtil util = new HDFSUtil();
					String path = hdfspath.concat("/logs/" + table + "_"
							+ datepart + ".log");
					requestLogDetails.setLogfile(path);
					LOGGER.info("Log file path: " + path);
					util.writeToOutPutStream(path,
							"----------\n" + new Date()
									+ " INFO: Data load started for table "
									+ request.getSourcedatabase() + "." + table
									+ "\n");
					request.setSourcetable(table);
					if (logId == 0) {
						if (DataMigrationConstants.HIVE
								.equalsIgnoreCase(request.getStoragetarget())) {
							requestLogDetails = new SchemaCheckDAO()
									.runSchemaCheck(path, requestLogDetails,
											request);
						} else if (DataMigrationConstants.HDFS
								.equalsIgnoreCase(request.getStoragetarget())) {
							String tarDir = request.getTargetdirectory() + "/"
									+ table;
							requestLogDetails.setTargetdirectory(tarDir);
						}
						requestLogDetails.setRequestid(request.getRequestid());
						requestLogDetails.setTablename(table);
						requestLogDetails.setRequesttype(request.getLoadtype());
						String startDate = requestLogDetails.getStartdate();
						String endDate = requestLogDetails.getEnddate();
						if (startDate == null) {
							requestLogDetails = new RequestLogDao().getDates(
									requestLogDetails, request);
						} else {
							requestLogDetails.setEnddate(endDate);
							requestLogDetails.setStartdate(startDate);
						}
						requestLogDetails.setJobstatus(status);
					}
					requestLogDetails.setExecutiontime(dateTimeSdf
							.format(new Date(System.currentTimeMillis())));
					requestLogDetails = new RequestLogDao()
							.addUpdate(requestLogDetails);
					status = new SqoopDAO().executeSqoopScripts(path, request,
							requestLogDetails);
					requestLogDetails.setExecutiontime(dateTimeSdf
							.format(new Date(System.currentTimeMillis())));
					if (DataMigrationConstants.HIVE
							.equalsIgnoreCase(request.getStoragetarget())) {
					requestLogDetails.setReconfile(hdfspath + "/recon/" + table
							+ "_" + datepart);
					ReconDAO recondao = new ReconDAO();
					util.writeToOutPutStream(path, new Date()
							+ " INFO: Getting parameters to execute Recon"
							+ "\n");
					String reconParams = recondao.getReconParameters(request,
							requestLogDetails);
					reconStatus = recondao.executeReconProcess(path,
							reconParams, requestLogDetails.getRequestlogid());
					}
					JSONObject jsonMsg = new JSONObject();
					jsonMsg.put("logid", requestLogDetails.getRequestlogid());
					jsonMsg.put("status", status);
					responseJson.add(jsonMsg);
				} catch (FRDMException | IOException e) {
					e.printStackTrace();
					JSONObject jsonobject = new JSONObject();
					jsonobject.put(DataMigrationConstants.MESSAGE,
							e.getMessage());
					responseJson.add(jsonobject);
					status = DataMigrationConstants.FAILED;
				}
				requestLogDetails.setJobstatus(status);
				requestLogDetails.setReconstatus(reconStatus);
				new RequestLogDao().addUpdate(requestLogDetails);
			}
			request.setProvisionstatus(DataMigrationConstants.COMPLETED);
			new RequestDAO().addUpdate(request);
			statusCode = 200;
		} catch (FRDMException e) {
			e.printStackTrace();
			JSONObject jsonobject = new JSONObject();
			jsonobject.put(DataMigrationConstants.MESSAGE, e.getMessage());
			responseJson.add(jsonobject);
		}
		response = responseJson.toString();
		return Response.status(statusCode).entity(response).build();
	}
}
