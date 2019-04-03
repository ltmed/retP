package com.intellica.retouch.ws;


import com.intellica.retouch.dao.RetouchConnectionDao;
import com.intellica.retouch.entity.RetouchConnectionEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/RetouchConnectionService")

public class RetouchConnectionService extends  Service{



    @GET
    @Path("/getAllConnections")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllConnections() throws RuntimeException {
        RetouchConnectionDao uDao = new RetouchConnectionDao();
        return Response.ok()
                .entity( uDao.getAll())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }



    @GET
    @Path("/getConnectionById")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConnectionById(@QueryParam("id") String id) throws RuntimeException {
        RetouchConnectionDao uDao = new RetouchConnectionDao();
        return Response.ok()
                .entity(uDao.get(id))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();

    }


    @POST
    @Path("/createConnection")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createConnection(RetouchConnectionEntity connection) throws RuntimeException {
        RetouchConnectionDao uDao = new RetouchConnectionDao();
        return   Response.ok()
                .entity( uDao.insert(connection))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }


    @POST
    @Path("/updateConnection")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateConnection(RetouchConnectionEntity connection) throws RuntimeException {
        RetouchConnectionDao uDao = new RetouchConnectionDao();
        return  Response.ok()
                .entity( uDao.update(connection))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

    @POST
    @Path("/deleteConnection")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteConnection(RetouchConnectionEntity connection) throws RuntimeException {
        RetouchConnectionDao uDao = new RetouchConnectionDao();
        return Response.ok()
                .entity(uDao.delete(connection))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

}
