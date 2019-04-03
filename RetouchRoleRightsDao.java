package com.intellica.retouch.dao;

import com.intellica.retouch.common.util.JPAUtil;
import com.intellica.retouch.entity.RetouchRoleRightsAudEntity;
import com.intellica.retouch.entity.RetouchRoleRightsEntity;
import com.intellica.retouch.entity.RetouchRoleAudEntity;
import com.intellica.retouch.entity.RetouchRoleEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

public class RetouchRoleRightsDao implements Dao{


    Timestamp timestampNow = new Timestamp(System.currentTimeMillis());


    @Override
    public Object get(String id) {
        EntityManager entityManager = null;
        try{
            entityManager= JPAUtil.getEntityManager();
            int idNumber=Integer.parseInt(id);
            RetouchRoleRightsEntity rfe = entityManager.find(RetouchRoleRightsEntity.class, idNumber);
            entityManager.close();
            return rfe;
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
    public List<RetouchRoleRightsEntity> getAll() {
        EntityManager entityManager=null;
        try{
            entityManager = JPAUtil.getEntityManager();
            TypedQuery<RetouchRoleRightsEntity> query=entityManager.createNamedQuery("RetouchRoleRights.findAll",RetouchRoleRightsEntity.class);
            List<RetouchRoleRightsEntity> roleRightsList=query.getResultList();
            entityManager.close();
            return roleRightsList;
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
            RetouchRoleRightsEntity roleRights = (RetouchRoleRightsEntity) o;

            roleRights.setRoleId(roleRights.getRoleId());
            roleRights.setDirectoryId(roleRights.getDirectoryId());
            roleRights.setFolderId(roleRights.getFolderId());
            roleRights.setFormId(roleRights.getFormId());
            roleRights.setCrt(roleRights.isCrt());
            roleRights.setRd(roleRights.isRd());
            roleRights.setUpd(roleRights.isUpd());
            roleRights.setIns(roleRights.isIns());
            roleRights.setDel(roleRights.isDel());
            roleRights.setImp(roleRights.isImp());
            roleRights.setExp(roleRights.isExp());
            roleRights.setCreatedBy(roleRights.getCreatedBy());
            roleRights.setCreationDate(timestampNow);
            roleRights.setModifiedBy(roleRights.getModifiedBy());
            roleRights.setModificationDate(timestampNow);

            entityManager.persist(roleRights);
            transaction.commit();
            entityManager.close();
            insertRoleRightsAud(roleRights, 0);
            return roleRights.getId();
        } catch (Exception e) {
            return 0;
        }finally {
            if(entityManager != null)
            entityManager.close();
        }
    }

    @Override
    public int delete(Object o) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            RetouchRoleRightsEntity rRoleRights = (RetouchRoleRightsEntity) o;
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            rRoleRights = entityManager.find(RetouchRoleRightsEntity.class, rRoleRights.getId());
            entityManager.remove(rRoleRights);
            transaction.commit();
            insertRoleRightsAud(rRoleRights, 2);
            entityManager.close();
            return 1;
        } catch (Exception e) {
            e.getMessage();
            return 0;
        } finally {
            if(entityManager != null)
                entityManager.close();
        }

    }

    public void insertRoleRightsAud(RetouchRoleRightsEntity roleRights, int revType) {
        EntityManager entityManager = null;
        try {
            RetouchRoleRightsAudEntity roleRightsAud = new RetouchRoleRightsAudEntity();
            entityManager = JPAUtil.getEntityManager();
            if (revType != 0) {
                Query query = entityManager.createQuery("SELECT c FROM RetouchRoleRightsAudEntity c WHERE c.id=:rRoleRightsId", RetouchRoleRightsAudEntity.class).setParameter("rRoleRightsId", roleRights.getId());
                List<RetouchRoleRightsAudEntity> rRoleRightsList = query.getResultList();
                int size = rRoleRightsList.size();
                roleRightsAud.setRev(size);
            } else {
                roleRightsAud.setRev(0);
            }
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            roleRightsAud.setRev(roleRights.getId());
            roleRightsAud.setRevtype((byte) revType);
            roleRightsAud.setRoleId(roleRights.getRoleId());
            roleRightsAud.setDirectoryId(roleRights.getDirectoryId());
            roleRightsAud.setFolderId(roleRights.getFolderId());
            roleRightsAud.setFormId(roleRights.getFormId());
            roleRightsAud.setCrt(roleRights.isCrt());
            roleRightsAud.setRd(roleRights.isRd());
            roleRightsAud.setUpd(roleRights.isUpd());
            roleRightsAud.setIns(roleRights.isIns());
            roleRightsAud.setDel(roleRights.isDel());
            roleRightsAud.setImp(roleRights.isImp());
            roleRightsAud.setExp(roleRights.isExp());
            roleRightsAud.setCreatedBy(roleRights.getCreatedBy());
            roleRightsAud.setCreationDate(timestampNow);
            roleRightsAud.setModifiedBy(roleRights.getModifiedBy());
            roleRightsAud.setModificationDate(timestampNow);

            entityManager.persist(roleRightsAud);
            transaction.commit();
            entityManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
            RetouchRoleRightsEntity rRoleRights =(RetouchRoleRightsEntity)o;
            entityManager.merge(rRoleRights);
            transaction.commit();
            entityManager.close();
            insertRoleRightsAud(rRoleRights, 1);
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
