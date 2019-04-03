package com.intellica.retouch.dao;

import com.intellica.retouch.common.util.JPAUtil;
import com.intellica.retouch.entity.RetouchResourcetablesEntity;
import org.json.JSONArray;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.*;
import java.util.List;

public class ResourceTablesDao implements Dao {

    @Override
    public Object get(String id) {
        EntityManager entityManager = null;
        try {
            entityManager = JPAUtil.getEntityManager();
            TypedQuery<RetouchResourcetablesEntity> query = entityManager.createNamedQuery("ResourceTables.findById", RetouchResourcetablesEntity.class).setParameter("id", id);
            RetouchResourcetablesEntity retouchResourcetablesEntity = query.getSingleResult();
            return retouchResourcetablesEntity;
        } catch (Exception e) {
            e.getMessage();
            return 0;
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    public List<RetouchResourcetablesEntity> getTablesBySchemaNameAndConnectionId(String schemaName, String connectionId) throws SQLException {
        EntityManager entityManager= null;
        JSONArray jsonArray=new JSONArray();
        try  {
            entityManager=JPAUtil.getEntityManager();
            Query q1=entityManager.createQuery("select tableId from RetouchTableConnectionEntity a where connectionId=:connectionId",String.class).
                 setParameter("connectionId",connectionId);
            List<String> tableIdList=q1.getResultList();
            Query q2=entityManager.createQuery("SELECT c FROM RetouchResourcetablesEntity c WHERE schema=:schema and id in (:ids)",RetouchResourcetablesEntity.class).
                    setParameter("schema",schemaName).setParameter("ids",tableIdList);
            List<RetouchResourcetablesEntity> rt=q2.getResultList();
            return rt;
        } catch (Exception e) {
            e.getMessage();
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

//    public List<ResourceTables> getTablesBySchemaNameAndConnectionId(String schemaName, String connectionId) throws SQLException {
//        String sql = "select * from RETOUCH_RESOURCETABLES where schema=? and id in (select table_id from RETOUCH_TABLE_CONNECTION where connection_id=?)";
//        List<ResourceTables> tableList = new ArrayList<>();
//        ResourceTables rt = null;
//        ResultSet rs = null;
//        try (Connection con = ConnectionManager.getInstance().getConnection();
//             PreparedStatement ps = con.prepareStatement(sql);
//        ) {
//            ps.setString(1, schemaName);
//            ps.setString(2, connectionId);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                rt = new ResourceTables();
//                rt.setId(rs.getString(1));
//                rt.setSchema(rs.getString(2));
//                rt.setTable_name(rs.getString(3));
//                rt.setUrl(rs.getString(4));
//                rt.setTemp_table_name(rs.getString(5));
//                rt.setHistory_table_name(rs.getString(6));
//                rt.setCreated_by(rs.getString(7));
//                rt.setCreation_date(rs.getString(8));
//                tableList.add(rt);
//            }
//        } catch (SQLException se) {
//            se.printStackTrace();
//        } finally {
//            if (rs != null) {
//                rs.close();
//            }
//        }
//        return tableList;
//    }


    public List<String> getSchemasByConnectionId(String connectionId) {
        String sql = "select schema from RETOUCH_RESOURCETABLES r, RETOUCH_TABLE_CONNECTION tc where r.id=tc.TABLE_ID and tc.CONNECTION_ID=" + connectionId;
        EntityManager entityManager = null;
        try {
            entityManager = JPAUtil.getEntityManager();
            Query query = entityManager.createNativeQuery(sql);
            List<String> schemaList = query.getResultList();
            return schemaList;
        } catch (Exception e) {
            e.getMessage();
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<String> getAllResourceTableWithConnection() {
        String sql = "SELECT r.*, c.NAME, t.CONNECTION_ID, t.TABLE_ID\n" +
                "FROM RETOUCH_RESOURCETABLES r,\n" +
                "     RETOUCH_CONNECTION c,\n" +
                "     RETOUCH_TABLE_CONNECTION t\n" +
                "WHERE t.CONNECTION_ID = c.ID\n" +
                "AND   t.TABLE_ID = r.ID";

        EntityManager entityManager = null;
        try {
            entityManager = JPAUtil.getEntityManager();
            Query query = entityManager.createNativeQuery(sql);
            List<String> resourceTableList = query.getResultList();
            entityManager.close();
            return resourceTableList;

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
    public List getAll() {
        return null;
    }

    @Override
    public int insert(Object o) {
        return 0;
    }

    @Override
    public int update(Object o) {
        return 0;
    }

    @Override
    public int delete(Object o) {
        return 0;
    }
}
