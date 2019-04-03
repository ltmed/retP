package com.intellica.retouch.dao;

import com.intellica.retouch.common.util.JPAUtil;
import com.intellica.retouch.entity.RetouchConnectionAudEntity;
import com.intellica.retouch.entity.RetouchConnectionEntity;

import javax.persistence.*;
import java.sql.*;
import java.util.List;

public class RetouchConnectionDao implements Dao {
    Date currentDate = new Date(System.currentTimeMillis());

    @Override
    public Object get(String id) {
        EntityManager entityManager = null;
        try{
            entityManager=JPAUtil.getEntityManager();
        int idNumber=Integer.parseInt(id);
        RetouchConnectionEntity rce = entityManager.find(RetouchConnectionEntity.class, idNumber);
        entityManager.close();
        return rce;
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

    @Override
    public List<RetouchConnectionEntity> getAll() {
        EntityManager entityManager=null;
        try{
            entityManager = JPAUtil.getEntityManager();
        TypedQuery<RetouchConnectionEntity> query=entityManager.createNamedQuery("Connection.findAll",RetouchConnectionEntity.class);
        List<RetouchConnectionEntity> connectionList=query.getResultList();
        entityManager.close();
        return connectionList;
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
            RetouchConnectionEntity connection = (RetouchConnectionEntity) o;

            connection.setPassword(connection.getPassword());
            connection.setUrl(connection.getUrl());
            connection.setUsername(connection.getUsername());
            connection.setActive(connection.getActive());
            connection.setCreatedBy(connection.getCreatedBy());
            connection.setCreationDate(currentDate);
            connection.setDescription(connection.getDescription());
            connection.setModificationDate(currentDate);
            connection.setModifiedBy(connection.getModifiedBy());
            connection.setName(connection.getName());
            entityManager.persist(connection);
            transaction.commit();
            entityManager.close();
            insertConnectionAud(connection, 0);
            return connection.getId();
        }
        catch (NoResultException e){
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
            RetouchConnectionEntity connection =(RetouchConnectionEntity)o;
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            connection= entityManager.find(RetouchConnectionEntity.class, connection.getId());
            entityManager.remove(connection);
            transaction.commit();
            insertConnectionAud(connection,2);
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


    public void insertConnectionAud(RetouchConnectionEntity connection,int revType) {
        EntityManager entityManager=null;
        try{
            RetouchConnectionAudEntity connectionAud = new RetouchConnectionAudEntity();
            entityManager = JPAUtil.getEntityManager();
            if (revType != 0) {
                Query query = entityManager.createQuery("SELECT c FROM RetouchConnectionAudEntity c WHERE c.id=:connectionId", RetouchConnectionAudEntity.class).setParameter("connectionId", String.valueOf(connection.getId()));
                List<RetouchConnectionAudEntity> connectionList = query.getResultList();
                int size = connectionList.size();
                connectionAud.setRev(size);
            } else {
                connectionAud.setRev(0);
            }
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            connectionAud.setRevtype((byte)revType);
            connectionAud.setPassword(connection.getPassword());
            connectionAud.setUrl(connection.getUrl());
            connectionAud.setUsername(connection.getUsername());
            connectionAud.setActive(connection.getActive());
            connectionAud.setCreatedBy(connection.getCreatedBy());
            connectionAud.setCreationDate(currentDate);
            connectionAud.setDescription(connection.getDescription());
            connectionAud.setModificationDate(currentDate);
            connectionAud.setModifiedBy(connection.getModifiedBy());
            connectionAud.setName(connection.getName());
            connectionAud.setId(connection.getId());
            entityManager.persist(connectionAud);
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
            RetouchConnectionEntity connection =(RetouchConnectionEntity)o;
            entityManager.merge(connection);
            transaction.commit();
            entityManager.close();
            insertConnectionAud(connection, 1);
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
