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

import com.datamigration.dao.UserDAO;
import com.datamigration.model.UserDetails;
import com.datamigration.util.FRDMException;

@Path("/Users")
public class UserService {
	final Logger LOGGER = Logger.getLogger(UserService.class);
	int statusCode = 400;
	String response;

	@SuppressWarnings("unchecked")
	@GET
	@Path("/userList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		LOGGER.info("userList service invocation started!!!");
		UserDAO dao = new UserDAO();
		List<UserDetails> users;
		JSONArray array = new JSONArray();
		try {
			users = dao.getList();
			for (UserDetails user : users) {
				JSONObject userJson = user.toJson(user);
				array.add(userJson);
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
	@Path("/addUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUpdateUser(String json) {
		LOGGER.info("addUser service invocation started!!!");
		JSONParser jsonparse = new JSONParser();
		JSONObject jsonObj = null;
		UserDAO dao = new UserDAO();
		String error = "";
		try {
			jsonObj = (JSONObject) jsonparse.parse(json);
			UserDetails user = new UserDetails();
			user = user.fromJson(jsonObj);
			boolean flag = dao.isUserExists(user);
			if (flag) {
				error = "Sorry, the User Name already exists";
			} else {
				dao.addUpdate(user);
				jsonObj = user.toJson(user);
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
	@Path("userbyname")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByName(@QueryParam("username") String name) {
		LOGGER.info("userById service invocation started!!!");
		JSONObject responseJson = new JSONObject();
		UserDAO dao = new UserDAO();
		UserDetails user;
		String error = "";
		try {
			user = dao.getDetails(name);
			if (user.getUserid() == 0) {
				error = "Username doesnot exist";
			} else {
				responseJson = user.toJson(user);
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
