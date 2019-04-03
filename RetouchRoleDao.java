package com.intellica.retouch.dao;

import com.intellica.retouch.common.util.JPAUtil;
import com.intellica.retouch.entity.RetouchRoleAudEntity;
import com.intellica.retouch.entity.RetouchRoleEntity;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

public class RetouchRoleDao implements Dao {

    Timestamp timestampNow = new Timestamp(System.currentTimeMillis());

    @Override
    public Object get(String id) {
        EntityManager entityManager = null;
        try {
            entityManager = JPAUtil.getEntityManager();
            int idNumber = Integer.parseInt(id);
            RetouchRoleEntity rre = entityManager.find(RetouchRoleEntity.class, idNumber);
            entityManager.close();
            return rre;
        } catch (Exception e) {
            e.getMessage();
            return 0;
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    @Override
    public List<RetouchRoleEntity> getAll() {
        EntityManager entityManager = null;
        try {
            entityManager = JPAUtil.getEntityManager();
            TypedQuery<RetouchRoleEntity> query = entityManager.createNamedQuery("RetouchRole.findAll", RetouchRoleEntity.class);
            List<RetouchRoleEntity> roleList = query.getResultList();
            entityManager.close();
            return roleList;
        } catch (Exception e) {
            e.getMessage();
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public int insert(Object o) {
        EntityManager entityManager = null;
        try {
            entityManager = JPAUtil.getEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            RetouchRoleEntity role = (RetouchRoleEntity) o;

            role.setName(role.getName());
            role.setDescription(role.getDescription());
            role.setActive(role.isActive());
            role.setSystem(true);
            role.setAdmin(false);
            role.setDirectoryAdmin(false);
            role.setDirectoryId("0");
            role.setCreatedBy(role.getCreatedBy());
            role.setCreationDate(timestampNow);
            role.setModifiedBy(role.getModifiedBy());
            role.setModificationDate(timestampNow);
            entityManager.persist(role);
            transaction.commit();
            entityManager.close();
            insertRoleAud(role, 0);
            return role.getId();
        } catch (Exception e) {
            return 0;
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    @Override
    public int delete(Object o) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            RetouchRoleEntity rRole = (RetouchRoleEntity) o;
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            rRole = entityManager.find(RetouchRoleEntity.class, rRole.getId());
            entityManager.remove(rRole);
            transaction.commit();
            insertRoleAud(rRole, 2);
            entityManager.close();
            return 1;
        } catch (Exception e) {
            e.getMessage();
            return 0;
        } finally {
            if (entityManager != null)
                entityManager.close();
        }

    }

    public void insertRoleAud(RetouchRoleEntity role, int revType) {
        EntityManager entityManager = null;
        try {
            RetouchRoleAudEntity rRoleAud = new RetouchRoleAudEntity();
            entityManager = JPAUtil.getEntityManager();
            if (revType != 0) {
                Query query = entityManager.createQuery("SELECT c FROM RetouchRoleAudEntity c WHERE c.id=:rRoleId", RetouchRoleAudEntity.class).setParameter("rRoleId", role.getId());
                List<RetouchRoleAudEntity> rRoleList = query.getResultList();
                int size = rRoleList.size();
                rRoleAud.setRev(size);
            } else {
                rRoleAud.setRev(0);
            }
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            rRoleAud.setRev(role.getId());
            rRoleAud.setRevtype((byte) revType);
            rRoleAud.setName(role.getName());
            rRoleAud.setDescription(role.getDescription());
            rRoleAud.setActive(role.isActive());
            rRoleAud.setSystem(true);
            rRoleAud.setAdmin(false);
            rRoleAud.setDirectoryAdmin(false);
            rRoleAud.setDirectoryId("0");
            rRoleAud.setCreatedBy(role.getCreatedBy());
            rRoleAud.setCreationDate(timestampNow);
            rRoleAud.setModifiedBy(role.getModifiedBy());
            rRoleAud.setModificationDate(timestampNow);

            entityManager.persist(rRoleAud);
            transaction.commit();
            entityManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }


    @Override
    public int update(Object o) {
        EntityManager entityManager = null;
        try {
            entityManager = JPAUtil.getEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            RetouchRoleEntity rRole = (RetouchRoleEntity) o;
            entityManager.merge(rRole);
            transaction.commit();
            entityManager.close();
            insertRoleAud(rRole, 1);
            return 1;
        } catch (Exception e) {
            e.getMessage();
            return 0;
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    public List<RetouchRoleEntity> getRolesBySystem(boolean system) {
        EntityManager entityManager = null;
        try {
            entityManager = JPAUtil.getEntityManager();
            // int idNumber = Integer.parseInt(system);
            Query query = entityManager.createQuery("SELECT c FROM RetouchRoleEntity c WHERE system=:system", RetouchRoleEntity.class).
                    setParameter("system", system);
            List<RetouchRoleEntity> roleList = query.getResultList();
            entityManager.close();
            return roleList;
        } catch (Exception e) {
            e.getMessage();
            return null;
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

}