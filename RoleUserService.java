package com.intellica.retouch.ws;

import com.intellica.retouch.dao.RoleUserDao;
import com.intellica.retouch.entity.RetouchRoleUserEntity;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



import javax.ws.rs.Path;
import java.sql.SQLException;

@Path("/RoleUserService")

public class RoleUserService extends Service {


    @POST
    @Path("/checkRoleUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkUserRole(RetouchRoleUserEntity roleUser) throws SQLException {
        RoleUserDao roleUserDao = new RoleUserDao();
        String userId= roleUser.getUserId();
        String roleId= roleUser.getRoleId();
        return   Response.ok()
                .entity(roleUserDao.checkRoleUser(userId, roleId))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

    @GET
    @Path("/getRoleUserByUserId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleUserByUserId(@QueryParam("userId") String userId) throws SQLException{
        RoleUserDao uDao = new RoleUserDao();
        return Response.ok()
                .entity(uDao.getRoleUserByUserId(userId))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();

    }


    @GET
    @Path("/getAllRoleUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoleUsers() {
        RoleUserDao uDao = new RoleUserDao();
        return Response.ok()
                .entity( uDao.getAll())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }



    @POST
    @Path("/createRoleUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoleUser(RetouchRoleUserEntity roleUser) throws RuntimeException{
        RoleUserDao rUDao = new RoleUserDao();
        return   Response.ok()
                .entity( rUDao.insert(roleUser))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }


    @POST
    @Path("/updateRoleUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoleUser(RetouchRoleUserEntity roleUser) throws RuntimeException {
        RoleUserDao uDao = new RoleUserDao();
        return  Response.ok()
                .entity( uDao.update(roleUser))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

    @POST
    @Path("/deleteRoleUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoleUser(RetouchRoleUserEntity roleUser) throws RuntimeException {
        RoleUserDao uDao = new RoleUserDao();
        return Response.ok()
                .entity(uDao.delete(roleUser))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

}
