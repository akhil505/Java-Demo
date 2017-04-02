package com.datamigration.services;

import java.util.List;

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

import com.datamigration.dao.RequestLogDao;
import com.datamigration.model.RequestLogDetails;
import com.datamigration.util.FRDMException;

;

@Path("/RequestsLogServices")
public class RequestsLogService {

	final Logger LOGGER = Logger.getLogger(RequestsLogService.class);
	int statusCode = 400;
	String response;

	@SuppressWarnings("unchecked")
	@GET
	@Path("/requestlogList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReqestsLogServices(@QueryParam("id") int id) {
		LOGGER.info("requestlogList service invocation started!!!");
		RequestLogDao dao = new RequestLogDao();
		List<RequestLogDetails> requestLogs;
		JSONArray array = new JSONArray();
		try {
			requestLogs = dao.getList(id);
			for (RequestLogDetails requestlog : requestLogs) {
				JSONObject requestlogJson = requestlog.toJson(requestlog);
				array.add(requestlogJson);
			}
			statusCode = 200;
			response = array.toString();
		} catch (FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("Message", e.getMessage());
			response = jsonObj.toString();
		}
		return Response.status(statusCode).entity(response).build();

	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("/lastRunList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLastRunLogServices(@QueryParam("id") int id) {
		LOGGER.info("lastRunList service invocation started!!!");
		RequestLogDao dao = new RequestLogDao();
		List<RequestLogDetails> requestLogs;
		JSONArray array = new JSONArray();
		try {
			requestLogs = dao.getLastRunList(id);
			for (RequestLogDetails requestlog : requestLogs) {
				JSONObject requestlogJson = requestlog.toJson(requestlog);
				array.add(requestlogJson);
			}
			statusCode = 200;
			response = array.toString();
		} catch (FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("Message", e.getMessage());
			response = jsonObj.toString();
		}
		return Response.status(statusCode).entity(response).build();

	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/addRequestlog")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUpdateServer(String json) {
		LOGGER.info("addRequestlog service invocation started!!!");
		JSONParser jsonparse = new JSONParser();
		JSONObject jsonObj = null;
		RequestLogDao dao = new RequestLogDao();
		try {
			jsonObj = (JSONObject) jsonparse.parse(json);
			RequestLogDetails requestlog = new RequestLogDetails();
			requestlog = requestlog.fromJson(jsonObj);
			dao.addUpdate(requestlog);
			jsonObj = requestlog.toJson(requestlog);
			statusCode = 200;
		} catch (ParseException | FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			jsonObj = new JSONObject();
			jsonObj.put("Message", jsonObj);
			response = e.getMessage();
		}
		response = jsonObj.toString();
		return Response.status(statusCode).entity(response).build();
	}
}
