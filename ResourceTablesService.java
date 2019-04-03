package com.intellica.retouch.ws;


import com.intellica.retouch.dao.ResourceTablesDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/ResourceTablesService")
public class ResourceTablesService {

    @GET
    @Path("/getSchemasByConnectionId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchemasByConnectionId(@QueryParam("id") String connectionId) throws SQLException {
        ResourceTablesDao resourceTablesDao = new ResourceTablesDao();
        return Response.ok()
                .entity(resourceTablesDao.getSchemasByConnectionId(connectionId))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

    @GET
    @Path("/getTablesBySchemaNameAndConnectionId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTablesBySchemaNameAndConnectionId(@QueryParam("name") String schemaName, @QueryParam("id") String connectionId) throws SQLException {
        ResourceTablesDao resourceTablesDao = new ResourceTablesDao();
        return Response.ok()
                .entity(resourceTablesDao.getTablesBySchemaNameAndConnectionId(schemaName, connectionId))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

    @GET
    @Path("/getTableById")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTable(@QueryParam("id") String id){
        ResourceTablesDao resourceTablesDao = new ResourceTablesDao();
        return Response.ok()
                .entity(resourceTablesDao.get(id))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }


    @GET
    @Path("/getAllResourceTableWithConnection")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllResourceTableWithConnection() throws SQLException, RuntimeException {
        ResourceTablesDao resourceTablesDao = new ResourceTablesDao();
        return Response.ok()
                .entity(resourceTablesDao.getAllResourceTableWithConnection())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

}
