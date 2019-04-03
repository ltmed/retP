package com.intellica.retouch.dao;

import com.google.common.base.Strings;
import com.intellica.retouch.common.util.JPAUtil;
import com.intellica.retouch.common.util.CryptoUtil;
import com.intellica.retouch.entity.RetouchUserAudEntity;
import com.intellica.retouch.entity.RetouchUserEntity;

import javax.persistence.*;
import java.security.CryptoPrimitive;
import java.sql.*;
import java.util.List;


public class UserDao implements Dao {


    Timestamp timestampNow = new Timestamp(System.currentTimeMillis());


    public RetouchUserEntity isUserExist(String username, String password) throws Exception {

        EntityManager entityManager = null;
        String ePassword = CryptoUtil.encryptPass(password);
        try {
            entityManager = JPAUtil.getEntityManager();

            Query query = entityManager.createQuery("SELECT c FROM RetouchUserEntity c WHERE c.name = ?1 AND c.password = ?2");
            query.setParameter(1, username).setParameter(2, ePassword);

            try {
                return (RetouchUserEntity) query.getSingleResult();
            } catch (NoResultException e) {
                e.getMessage();
                return null;
            }
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }


    @Override
    public Object get(String id) {
        EntityManager entityManager = null;
        try {
            entityManager = JPAUtil.getEntityManager();
            int idNumber = Integer.parseInt(id);
            RetouchUserEntity ue = entityManager.find(RetouchUserEntity.class, idNumber);
            entityManager.close();
            return ue;
        } catch (Exception e) {
            e.getMessage();
            return 0;
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    @Override
    public List<RetouchUserEntity> getAll() {
        EntityManager entityManager = null;
        try {
            entityManager = JPAUtil.getEntityManager();
            TypedQuery<RetouchUserEntity> query = entityManager.createNamedQuery("User.findAll", RetouchUserEntity.class);
            List<RetouchUserEntity> userList = query.getResultList();
            entityManager.close();
            return userList;
        } catch (Exception e) {
            e.getMessage();
            return null;
        } finally {
            if (entityManager != null)
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
            RetouchUserEntity user = (RetouchUserEntity) o;

            user.setName(user.getName());
            user.setFirstName(user.getFirstName());
            user.setLastName(user.getLastName());
            user.setEmail(user.getEmail());
            user.setActive(user.isActive());
            user.setLoginType(user.getLoginType());
            user.setLocked(user.isLocked());
            String password = ((RetouchUserEntity) o).getPassword();
            user.setPassword(Strings.isNullOrEmpty(password) ? CryptoUtil.encryptPass(CryptoUtil.getRandomPass()) : CryptoUtil.encryptPass(password));
            user.setCreatedBy(user.getCreatedBy());
            user.setCreationDate(timestampNow);
            user.setModificationDate(timestampNow);
            user.setModifiedBy(user.getModifiedBy());

            entityManager.persist(user);
            transaction.commit();
            entityManager.close();
            insertUserAud(user, 0);
            return user.getId();
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
            RetouchUserEntity user = (RetouchUserEntity) o;
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            user = entityManager.find(RetouchUserEntity.class, user.getId());
            entityManager.remove(user);
            transaction.commit();
            insertUserAud(user, 2);
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


    public void insertUserAud(RetouchUserEntity user, int revType) {
        EntityManager entityManager = null;
        try {
            RetouchUserAudEntity userAud = new RetouchUserAudEntity();
            entityManager = JPAUtil.getEntityManager();
            if (revType != 0) {
                Query query = entityManager.createQuery("SELECT c FROM RetouchUserAudEntity c WHERE c.id=:userId", RetouchUserAudEntity.class).setParameter("userId", user.getId());
                List<RetouchUserAudEntity> userList = query.getResultList();
                int size = userList.size();
                userAud.setRev(size);
            } else {
                userAud.setRev(0);
            }
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            userAud.setRev(user.getId());
            userAud.setRevtype((byte) revType);
            userAud.setName(user.getName());
            userAud.setFirstName(user.getFirstName());
            userAud.setLastName(user.getLastName());
            userAud.setEmail(user.getEmail());
            userAud.setLocked(user.isLocked());
            userAud.setLoginType(user.getLoginType());
            userAud.setPassword(user.getPassword());
            userAud.setActive(user.isActive());
            userAud.setCreatedBy(user.getCreatedBy());
            userAud.setCreationDate(timestampNow);
            userAud.setModificationDate(timestampNow);
            userAud.setModifiedBy(user.getModifiedBy());

            entityManager.persist(userAud);
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
            RetouchUserEntity user = (RetouchUserEntity) o;
            entityManager.merge(user);
            transaction.commit();
            entityManager.close();
            insertUserAud(user, 1);
            return 1;
        } catch (Exception e) {
            e.getMessage();
            return 0;
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    public static void main(String[] args) throws Exception {

        // QH02XF+3vXTmanJtON2ZFg==
        System.out.println(CryptoUtil.encryptPass("tolga"));
        System.out.println(CryptoUtil.decryptPass("WlYFVLS5jDQ0mSp4F95kjA=="));

        Timestamp timestampNow = new Timestamp(System.currentTimeMillis());
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + timestampNow);



    }
}
