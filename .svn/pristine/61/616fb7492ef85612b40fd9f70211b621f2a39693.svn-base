package com.datamigration.services;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.datamigration.dao.GetMetaDataDAO;
import com.datamigration.dao.ServerDAO;
import com.datamigration.model.MetaDataConnectionDetails;
import com.datamigration.model.ServerDetails;
import com.datamigration.util.FRDMException;

@SuppressWarnings("unchecked")
@Path("/MetaDataService")
public class GetMetaData {
	final Logger LOGGER = Logger.getLogger(GetMetaData.class);
	int statusCode = 400;
	String response;

	@POST
	@Path("/getDatabases")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDatabases(String json) {
		LOGGER.info("getDatabases service invocation started!!!");
		JSONParser jsonparse = new JSONParser();
		JSONObject jsonObj = null;
		try {
			jsonObj = (JSONObject) jsonparse.parse(json);

			GetMetaDataDAO dao = new GetMetaDataDAO();
			ArrayList<String> dataBaseList = new ArrayList<String>();
			System.out.println(jsonObj.get("serverid"));
			ServerDetails server = new ServerDAO().getDetails(jsonObj
					.get("serverid"));
			MetaDataConnectionDetails metaDataConDetails = new MetaDataConnectionDetails();
			metaDataConDetails.setServertype(server.getServertype());
			metaDataConDetails.setHostname(server.getHostname());
			metaDataConDetails.setPort(server.getPort());
			metaDataConDetails.setUsername((String) jsonObj.get("username"));
			metaDataConDetails.setPassword((String) jsonObj.get("password"));
			dataBaseList = dao.getDataBases(metaDataConDetails);
			JSONArray arr = new JSONArray();
			for (String dbname : dataBaseList) {
				arr.add(dbname);
			}
			jsonObj = new JSONObject();
			jsonObj.put("databases", arr);
			statusCode = 200;
		} catch (FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			jsonObj = new JSONObject();
			jsonObj.put("Message",
					"Can't fetch available databases: Invalid Credentials");
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
			jsonObj = new JSONObject();
			jsonObj.put("Message",
					"Can't fetch available databases: " + e.getMessage());
		}
		response = jsonObj.toString();
		return Response.status(statusCode).entity(response).build();

	}

	@POST
	@Path("/getTables")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getTables(String json) {
		LOGGER.info("getTables service invocation started!!!");
		JSONParser jsonparse = new JSONParser();
		JSONObject jsonObj = null;
		GetMetaDataDAO dao = new GetMetaDataDAO();
		ArrayList<String> tablesList = new ArrayList<String>();
		try {

			jsonObj = (JSONObject) jsonparse.parse(json);
			System.out.println(jsonObj.get("serverid") + "....."
					+ jsonObj.get("username"));
			ServerDetails server = new ServerDAO().getDetails(jsonObj
					.get("serverid"));
			MetaDataConnectionDetails metaDataConDetails = new MetaDataConnectionDetails();
			System.out.println(server.getHostname());
			metaDataConDetails.setServertype(server.getServertype());
			metaDataConDetails.setHostname(server.getHostname());
			metaDataConDetails.setPort(server.getPort());
			metaDataConDetails.setUsername((String) jsonObj.get("username"));
			metaDataConDetails.setPassword((String) jsonObj.get("password"));
			metaDataConDetails
					.setDbname((String) jsonObj.get("sourcedatabase"));
			tablesList = dao.getTables(metaDataConDetails);
			JSONArray arr = new JSONArray();
			for (String dbname : tablesList) {
				arr.add(dbname);
			}
			jsonObj = new JSONObject();
			jsonObj.put("tables", arr);
			statusCode = 200;
		} catch (ParseException | FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			jsonObj = new JSONObject();
			jsonObj.put("Message",
					"Can't Fetch available tables list: " + e.getMessage());
		}
		response = jsonObj.toString();
		return Response.status(statusCode).entity(response).build();
	}

	@POST
	@Path("/getTableDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getTableDetails(String json) {
		LOGGER.info("getTableDetails service invocation started!!!");
		JSONParser jsonparse = new JSONParser();
		JSONObject jsonObj = null;
		// logger.info("1");
		try {
			jsonObj = (JSONObject) jsonparse.parse(json);
			System.out.println(json);
			GetMetaDataDAO dao = new GetMetaDataDAO();
			ArrayList<JSONObject> tableDetailsList = new ArrayList<JSONObject>();
			ServerDetails server = new ServerDAO().getDetails(jsonObj
					.get("serverid"));
			MetaDataConnectionDetails metaDataConDetails = new MetaDataConnectionDetails();
			metaDataConDetails.setServertype(server.getServertype());
			metaDataConDetails.setHostname(server.getHostname());
			metaDataConDetails.setPort(server.getPort());
			metaDataConDetails.setUsername((String) jsonObj.get("username"));
			metaDataConDetails.setPassword((String) jsonObj.get("password"));
			metaDataConDetails
					.setDbname((String) jsonObj.get("sourcedatabase"));
			tableDetailsList = dao.getTableDetails(metaDataConDetails,
					(String) jsonObj.get("sourcetables"));
			JSONArray arr = new JSONArray();
			for (JSONObject tableDetail : tableDetailsList) {
				arr.add(tableDetail);
			}
			jsonObj = new JSONObject();
			jsonObj.put("TableColumns", arr);
			statusCode = 200;
		} catch (ParseException | FRDMException e) {
			LOGGER.error(e.getMessage(), e);
			jsonObj = new JSONObject();
			jsonObj.put("Message", "Cant fetch table metadata information: "
					+ e.getMessage());
		}
		response = jsonObj.toString();
		return Response.status(statusCode).entity(response).build();
	}

}
