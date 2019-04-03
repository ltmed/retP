package com.intellica.retouch.dao;

import com.intellica.retouch.common.util.JPAUtil;
import com.intellica.retouch.entity.RetouchRoleUserAudEntity;
import com.intellica.retouch.entity.RetouchRoleUserEntity;

import javax.persistence.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class RoleUserDao implements Dao {

    Timestamp timestampNow = new Timestamp(System.currentTimeMillis());

    public boolean checkRoleUser(String userId,String roleId) {
        EntityManager entityManager = null;
        try{
            entityManager = JPAUtil.getEntityManager();

            Query query = entityManager.createQuery("SELECT c FROM RetouchRoleUserEntity c WHERE c.userId = ?1 AND c.roleId = ?2");
            query.setParameter(1, userId).setParameter(2, roleId);

            RetouchRoleUserEntity c = (RetouchRoleUserEntity) query.getSingleResult();
            return true;
        }
        catch (NoResultException e){
            e.getMessage();
            return false;
        }
        finally{
            if(entityManager != null)
                entityManager.close();
        }
    }


    @Override
    public Object get(String id) {
        EntityManager entityManager = null;
        try{
            entityManager=JPAUtil.getEntityManager();
        int idNumber=Integer.parseInt(id);
        RetouchRoleUserEntity rue = entityManager.find(RetouchRoleUserEntity.class, idNumber);
        entityManager.close();
        return rue;
        }
        catch (Exception e){
            e.getMessage();
            return 0;
        }
        finally {
            if(entityManager != null)
                entityManager.close();
        }
    }


    public List<RetouchRoleUserEntity> getRoleUserByUserId(String userId) throws SQLException {
        EntityManager entityManager=null;
        try{
            entityManager = JPAUtil.getEntityManager();
            Query query=entityManager.createQuery("SELECT c FROM RetouchRoleUserEntity c WHERE userId=:userId",RetouchRoleUserEntity.class).setParameter("userId",userId);
            List<RetouchRoleUserEntity> roleList=query.getResultList();
            entityManager.close();
            return roleList;
        }
        catch (Exception e){
            e.getMessage();
            return null;
        }
        finally {
            if(entityManager != null)
                entityManager.close();
        }
    }

    @Override
    public List<RetouchRoleUserEntity> getAll() {
        EntityManager entityManager=null;
        try{
            entityManager = JPAUtil.getEntityManager();
        TypedQuery<RetouchRoleUserEntity> query=entityManager.createNamedQuery("RoleUser.findAll",RetouchRoleUserEntity.class);
        List<RetouchRoleUserEntity> roleRoleUserList=query.getResultList();
        entityManager.close();
        return roleRoleUserList;
        }
        catch (Exception e){
            e.getMessage();
            return null;
        }
        finally {
            if(entityManager != null)
                entityManager.close();
        }
    }

    @Override
    public int insert(Object o) {
        EntityManager entityManager = null;

        try {
            entityManager = JPAUtil.getEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            RetouchRoleUserEntity roleUser = (RetouchRoleUserEntity) o;

            roleUser.setRoleId(roleUser.getRoleId());
            roleUser.setUserId(roleUser.getUserId());
            roleUser.setCreatedBy(roleUser.getCreatedBy());
            roleUser.setCreationDate(timestampNow);
            roleUser.setModificationDate(timestampNow);
            roleUser.setModifiedBy(roleUser.getModifiedBy());

            entityManager.persist(roleUser);
            transaction.commit();
            entityManager.close();
            insertRoleUserAud(roleUser, 0);
            return roleUser.getId();
        }
        catch (Exception e){
            return 0;
        }finally {
            if(entityManager != null)
            entityManager.close();
        }
    }


    @Override
    public int delete(Object o) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try{
            RetouchRoleUserEntity roleUser =(RetouchRoleUserEntity)o;
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            roleUser= entityManager.find(RetouchRoleUserEntity.class, roleUser.getId());
            entityManager.remove(roleUser);
            transaction.commit();
            insertRoleUserAud(roleUser,2);
            entityManager.close();
            return  1;
        }catch (Exception e){
            e.getMessage();
            return 0;
        }
        finally {
            if(entityManager != null)
                entityManager.close();
        }
    }


    public void insertRoleUserAud(RetouchRoleUserEntity roleUser,int revType) {
        EntityManager entityManager=null;
        try{
            RetouchRoleUserAudEntity roleUserAud = new RetouchRoleUserAudEntity();
            entityManager = JPAUtil.getEntityManager();
            if (revType != 0) {
                Query query = entityManager.createQuery("SELECT c FROM RetouchRoleUserAudEntity c WHERE c.id=:roleUserId", RetouchRoleUserAudEntity.class).setParameter("roleUserId", roleUser.getId());
                List<RetouchRoleUserAudEntity> roleRoleUserList = query.getResultList();
                int size = roleRoleUserList.size();
                roleUserAud.setRev(size);
            } else {
                roleUserAud.setRev(0);
            }
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            roleUserAud.setRev(roleUser.getId());
            roleUserAud.setRevtype((byte)revType);

            roleUserAud.setRoleId(roleUser.getRoleId());
            roleUserAud.setUserId(roleUser.getUserId());
            roleUserAud.setCreatedBy(roleUser.getCreatedBy());
            roleUserAud.setCreationDate(timestampNow);
            roleUserAud.setModificationDate(timestampNow);
            roleUserAud.setModifiedBy(roleUser.getModifiedBy());

            entityManager.persist(roleUserAud);
            transaction.commit();
            entityManager.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if(entityManager != null)
                entityManager.close();
        }
    }


    @Override
    public int update(Object o) {
        EntityManager entityManager =null;
        try{
            entityManager = JPAUtil.getEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            RetouchRoleUserEntity roleUser =(RetouchRoleUserEntity)o;
            entityManager.merge(roleUser);
            transaction.commit();
            entityManager.close();
            insertRoleUserAud(roleUser, 1);
            return  1;
        }catch (Exception e){
            e.getMessage();
            return 0;
        }
        finally {
            if(entityManager != null)
                entityManager.close();
        }
    }
}
