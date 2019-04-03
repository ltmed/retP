package com.intellica.retouch.ws;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellica.retouch.common.util.HTTPHelper;
import com.intellica.retouch.common.util.JWTUtil;
import com.intellica.retouch.common.util.LDAPUtil;
import com.intellica.retouch.common.util.PropertyUtil;
import com.intellica.retouch.dao.RetouchRoleDao;
import com.intellica.retouch.dao.RoleUserDao;
import com.intellica.retouch.dao.UserDao;
import com.intellica.retouch.entity.RetouchRoleUserEntity;
import com.intellica.retouch.entity.RetouchUserEntity;
import com.intellica.retouch.model.CreateUserViewModel;
import com.intellica.retouch.model.LoginViewModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.HTTP;
import org.json.JSONObject;


import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import javax.ws.rs.Path;

@Path("/UserService")

public class UserService extends Service {

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(LoginViewModel loginViewModel) throws Exception {
        UserDao uDao = new UserDao();
        String userName = loginViewModel.getName();
        String password = loginViewModel.getPassword();
        String roleId = loginViewModel.getRoleId();
        String authMode = loginViewModel.getAuthMode();

        // LDAP
        if (PropertyUtil.getProperty("auth.mode.2").equals(authMode)) {
            try {
                //authenticate
                LDAPUtil.authenticate(userName, password);
                RetouchUserEntity user = uDao.isUserExist(userName, password);

                //create if not exist
                if (user == null) {
                    user = new RetouchUserEntity();
                    user.setName(userName);
                    user.setPassword(password);
                    user.setLoginType("2");
                    user.setActive(true);
                    user.setCreatedBy("LDAP");
                    user.setModifiedBy("LDAP");
                    user.setId(uDao.insert(user));
                }
                JSONObject item = new JSONObject();
                item.put("result", true);
                item.put("message", JWTUtil.generateToken(user));
                return HTTPHelper.returnResponse(200, item.toString());
            } catch (Exception e) {
                JSONObject item = new JSONObject();
                item.put("result", false);
                item.put("message", "LDAP auth failed!");
                return HTTPHelper.returnResponse(401, item.toString());
            }
            // DB
        } else if (PropertyUtil.getProperty("auth.mode.1").equals(authMode)) {
            //is user exist
            RetouchUserEntity userEntity = uDao.isUserExist(userName, password);

            if (userEntity == null) {
                JSONObject item = new JSONObject();
                item.put("result", false);
                item.put("message", "Invalid Credentials!");
                return HTTPHelper.returnResponse(401, item.toString());
            }
            //is role correct
            boolean isRoleCorrect = new RoleUserDao().checkRoleUser(String.valueOf(userEntity.getId()), roleId);

            if (!isRoleCorrect) {
                JSONObject item = new JSONObject();
                item.put("result", false);
                item.put("message", "Role is not matched!");
                return HTTPHelper.returnResponse(401, item.toString());
            }

            ObjectMapper mapper = new ObjectMapper();
            JSONObject item = new JSONObject();
            item.put("result", true);
            item.put("message", JWTUtil.generateToken(userEntity));
            return HTTPHelper.returnResponse(200, item.toString());
        }
        JSONObject item = new JSONObject();
        item.put("result", false);
        item.put("message", "Auth failed!");
        return HTTPHelper.returnResponse(401, item.toString());
    }


    @GET
    @Path("/getUserById")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@QueryParam("id") String id) throws RuntimeException {
        UserDao uDao = new UserDao();
        return Response.ok()
                .entity(uDao.get(id))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();

    }

    @GET
    @Path("/getAllUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() throws RuntimeException {
        UserDao uDao = new UserDao();
        return Response.ok()
                .entity(uDao.getAll())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }


    @POST
    @Path("/createUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(CreateUserViewModel createUserViewModel) throws RuntimeException {

        // Initiliaze Dao and Entity objects for User and Role User
        UserDao userDao = new UserDao();
        RoleUserDao roleUserDao = new RoleUserDao();
        RetouchUserEntity userEntity = new RetouchUserEntity();
        RetouchRoleUserEntity roleUserEntity = null;

        // Set user data
        userEntity = createUserViewModel.getUser();
        int insertedUserId = 0;
        insertedUserId = userDao.insert(userEntity);

        // Set each role for the user
        for (String roleId : createUserViewModel.getRoleIdList()) {
            roleUserEntity = new RetouchRoleUserEntity();
            roleUserEntity.setRoleId(roleId);
            roleUserEntity.setUserId(String.valueOf(insertedUserId));
            roleUserEntity.setCreatedBy(createUserViewModel.getUser().getCreatedBy());
            roleUserEntity.setModifiedBy(createUserViewModel.getUser().getModifiedBy());
            roleUserDao.insert(roleUserEntity);
        }

        return HTTPHelper.returnResponse(200, insertedUserId);
    }


    @POST
    @Path("/updateUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(CreateUserViewModel createUserViewModel) throws RuntimeException, SQLException {
        // Initiliaze Dao and Entity objects for User and Role User
        UserDao userDao = new UserDao();
        RoleUserDao roleUserDao = new RoleUserDao();
        RetouchRoleUserEntity roleUserEntity = null;
        RetouchUserEntity userEntity = new RetouchUserEntity();

        // Set user data
        userEntity = createUserViewModel.getUser();
        userDao.update(userEntity);

        // Set each role for the user
        String userId = String.valueOf(createUserViewModel.getUser().getId());

        for (String roleId : createUserViewModel.getRoleIdList()) {
            roleUserEntity = new RetouchRoleUserEntity();
            roleUserEntity.setRoleId(roleId);
            roleUserEntity.setUserId(String.valueOf(createUserViewModel.getUser().getId()));
            roleUserEntity.setCreatedBy(createUserViewModel.getUser().getCreatedBy());
            roleUserEntity.setModifiedBy(createUserViewModel.getUser().getModifiedBy());
            roleUserEntity.setCreationDate(userEntity.getCreationDate());
            roleUserEntity.setModificationDate(userEntity.getModificationDate());
            roleUserDao.getRoleUserByUserId(userId);
            roleUserDao.update(roleUserEntity);
        }

        return HTTPHelper.returnResponse(200, createUserViewModel.getUser().getId());
    }

    @POST
    @Path("/deleteUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(CreateUserViewModel createUserViewModel) throws RuntimeException, SQLException {
        UserDao userDao = new UserDao();
        RoleUserDao roleUserDao = new RoleUserDao();
        RetouchRoleUserEntity roleUserEntity = null;
        RetouchUserEntity userEntity = new RetouchUserEntity();

        userEntity = createUserViewModel.getUser();
        userDao.delete(userEntity);

        // Set each role for the user
        String userId = String.valueOf(createUserViewModel.getUser().getId());

        for (String roleId : createUserViewModel.getRoleIdList()) {
            roleUserEntity = new RetouchRoleUserEntity();
            roleUserEntity.setRoleId(roleId);
            roleUserEntity.setUserId(String.valueOf(userId));
            roleUserEntity.setCreatedBy(createUserViewModel.getUser().getCreatedBy());
            roleUserEntity.setModifiedBy(createUserViewModel.getUser().getModifiedBy());
            roleUserEntity.setCreationDate(userEntity.getCreationDate());
            roleUserEntity.setModificationDate(userEntity.getModificationDate());
            roleUserDao.getRoleUserByUserId(userId);

        }

        return Response.ok()
                .entity(roleUserDao.delete(roleUserEntity))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();

    }


    @POST
    @Path("/isAdminUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response isAdminUser(RetouchUserEntity user) throws RuntimeException {
        String userId = String.valueOf(user.getId());
        return Response.ok()
                .entity(new RoleUserDao().checkRoleUser(userId, "1"))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }

}
