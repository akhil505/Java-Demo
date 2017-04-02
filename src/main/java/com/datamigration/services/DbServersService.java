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

import com.datamigration.dao.ServerDAO;
import com.datamigration.model.ServerDetails;
import com.datamigration.util.FRDMException;

@Path("/Servers")
public class DbServersService {
	final Logger LOGGER = Logger.getLogger(DbServersService.class);
	int statusCode = 400;
	String response;

	@SuppressWarnings("unchecked")
	@GET
	@Path("/serverList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServers() {
		LOGGER.info("serverList service invocation started!!!");
		ServerDAO dao = new ServerDAO();
		List<ServerDetails> servers;
		JSONArray array = new JSONArray();
		try {
			servers = dao.getList();
			for (ServerDetails server : servers) {
				JSONObject serverJson = server.toJson(server);
				array.add(serverJson);
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
	@Path("/addServer")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUpdateServer(String json) {
		LOGGER.info("addServer service invocation started!!!");
		JSONParser jsonparse = new JSONParser();
		JSONObject jsonObj = null;
		ServerDAO dao = new ServerDAO();
		String error = "";
		try {
			jsonObj = (JSONObject) jsonparse.parse(json);
			ServerDetails server = new ServerDetails();
			server = server.fromJson(jsonObj);
			int flag = dao.isServerExists(server);
			if (flag == 1) {
				error = "Sorry, the Server Name already exists";
			} else if (flag == 2) {
				error = "Sorry, the Server with Same Host and Port already exists";
			} else {
				dao.addUpdate(server);
				jsonObj = server.toJson(server);
				statusCode = 200;
			}
		} catch (ParseException | FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			error = e.getMessage();
		}
		if (!error.isEmpty()) {
			jsonObj = new JSONObject();
			jsonObj.put("Message", error);
		}
		response = jsonObj.toString();
		return Response.status(statusCode).entity(response).build();
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("/serverById")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServerById(@QueryParam("id") int id) {
		LOGGER.info("serverById service invocation started!!!");
		JSONObject responseJson = new JSONObject();
		ServerDAO dao = new ServerDAO();
		ServerDetails server;
		String error = "";
		try {
			server = dao.getDetails(id);

			if (server.getServerid() == 0) {
				error = "Sorry, the id you entered is invalid.";
			} else {
				responseJson = server.toJson(server);
				statusCode = 200;
			}
		} catch (FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			error = e.getMessage();
		}
		if (!error.isEmpty()) {
			responseJson = new JSONObject();
			responseJson.put("Message", error);
		}
		response = responseJson.toString();
		return Response.status(statusCode).entity(response).build();
	}
}
