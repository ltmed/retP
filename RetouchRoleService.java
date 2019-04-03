package com.intellica.retouch.ws;

import com.intellica.retouch.common.util.HTTPHelper;
import com.intellica.retouch.dao.RetouchRoleDao;
import com.intellica.retouch.dao.RetouchRoleRightsDao;
import com.intellica.retouch.entity.RetouchRoleEntity;
import com.intellica.retouch.entity.RetouchRoleRightsEntity;
import com.intellica.retouch.model.CreateRoleViewModel;
import com.intellica.retouch.model.RetouchRoleRights;
import com.intellica.retouch.ws.RetouchRoleService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/RetouchRoleService")
public class RetouchRoleService extends Service {


    @GET
    @Path("/getRoleById")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleById(@QueryParam("id") String id) throws RuntimeException {
        RetouchRoleDao uDao = new RetouchRoleDao();
        return Response.ok()
                .entity(uDao.get(id))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
        //  (Folder)fDao.get(id);
    }

    @GET
    @Path("/getAllRoles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles() throws RuntimeException {
        RetouchRoleDao uDao = new RetouchRoleDao();
        return Response.ok()
                .entity(uDao.getAll())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }


    @POST
    @Path("/createRole")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRole(CreateRoleViewModel createRoleViewModel) throws RuntimeException {

        // Initiliaze Dao and Entity objects for Role and RoleRights
        RetouchRoleDao roleDao = new RetouchRoleDao();
        RetouchRoleRightsDao roleRightsDao = new RetouchRoleRightsDao();

        // Get role Id and set to use for RoleRights
        int insertedRoleId = 0;
        insertedRoleId = roleDao.insert(createRoleViewModel.getRole());

        for (RetouchRoleRightsEntity rr : createRoleViewModel.getRights()) {
            rr.setRoleId(String.valueOf(insertedRoleId));
            roleRightsDao.insert(rr);
        }

        return HTTPHelper.returnResponse(200, insertedRoleId);
    }


    @POST
    @Path("/updateRole")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRole(RetouchRoleEntity role) throws RuntimeException {
        RetouchRoleDao uDao = new RetouchRoleDao();
        return Response.ok()
                .entity(uDao.update(role))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

    @POST
    @Path("/deleteRole")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRole(RetouchRoleEntity role) throws RuntimeException {
        RetouchRoleDao uDao = new RetouchRoleDao();
        return Response.ok()
                .entity(uDao.delete(role))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

    @GET
    @Path("/getRolesBySystem")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRolesBySystem(@QueryParam("system") boolean system) throws RuntimeException {
        RetouchRoleDao uDao = new RetouchRoleDao();
        return Response.ok()
                .entity(uDao.getRolesBySystem(system))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }


}
