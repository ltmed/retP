package com.intellica.retouch.ws;

import com.intellica.retouch.dao.RetouchRoleRightsDao;
import com.intellica.retouch.entity.RetouchRoleRightsEntity;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import javax.ws.rs.Path;

@Path("/RetouchRoleRightsService")
public class RetouchRoleRightsService extends Service
{


    @POST
    @Path("/createRoleRights")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoleRights(RetouchRoleRightsEntity roleRights) {
        RetouchRoleRightsDao rRightsDao = new RetouchRoleRightsDao();
        return   Response.ok()
                .entity( rRightsDao.insert(roleRights))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }



    @GET
    @Path("/getRoleRightsById")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleRightsById(@QueryParam("id") String id) throws SQLException {
        RetouchRoleRightsDao rRightsDao = new RetouchRoleRightsDao();
        return Response.ok()
                .entity(rRightsDao.get(id))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

    @GET
    @Path("/getAllRoleRights")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoleRights() throws RuntimeException {
        RetouchRoleRightsDao rRightsDao = new RetouchRoleRightsDao();
        return Response.ok()
                .entity( rRightsDao.getAll())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }



    @POST
    @Path("/updateRoleRights")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoleRights(RetouchRoleRightsEntity roleRights) throws RuntimeException {
        RetouchRoleRightsDao rRightsDao = new RetouchRoleRightsDao();
        return  Response.ok()
                .entity( rRightsDao.update(roleRights))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

    @POST
    @Path("/deleteRoleRights")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoleRights(RetouchRoleRightsEntity roleRights) throws RuntimeException {
        RetouchRoleRightsDao rRightsDao = new RetouchRoleRightsDao();
        return Response.ok()
                .entity(rRightsDao.delete(roleRights))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }
}
