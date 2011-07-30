/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author berk
 */
public class MiniNewsDAO {
    
    private Connection cnn;
    
    public MiniNewsDAO (Connection cnn) {
        this.cnn = cnn;
    }
    
    public ArrayList<MiniNews> getMiniNews(User u, boolean publishedOnly) throws SQLException {
        ArrayList<MiniNews> result = new ArrayList<MiniNews>();
        
        String filter = "";
        if (publishedOnly) {
            filter = "AND (a.PUBLISHED = 1) ";
        }
        
        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.LANGUAGE_ID, "
                + "l.DESCRIPTION AS LANGUAGE, "
                + "a.BODY, "
                + "a.NEWS_TIME, "
                + "a.CREATED_BY_USER_ID, "
                + "u.FIRST_NAME || ' ' || u.LAST_NAME AS CREATED_BY_USER, "
                + "a.PUBLISHED "
                + "FROM MINI_NEWS a "
                + "LEFT JOIN LANGUAGE l ON (l.ID = a.LANGUAGE_ID) "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.CREATED_BY_USER_ID) "
                + "WHERE (a.COMPANY_ID = " + u.getCompanyId() + ") "
                + filter
                + "ORDER BY a.NEWS_TIME DESC";
        
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(select);
        while (rs.next()) {
            MiniNews mn = new MiniNews();
            mn.setId(rs.getInt("ID"));
            mn.setCompanyId(rs.getInt("COMPANY_ID"));
            mn.setLanguageId(rs.getInt("LANGUAGE_ID"));
            mn.setLanguage(rs.getString("LANGUAGE"));
            mn.setBody(rs.getString("BODY"));
            mn.setTime(rs.getTimestamp("NEWS_TIME"));
            mn.setCreatedById(rs.getInt("CREATED_BY_USER_ID"));
            mn.setCreatedBy(rs.getString("CREATED_BY_USER"));
            mn.setPublished(rs.getBoolean("PUBLISHED"));
            result.add(mn);
        }
        rs.close();
        st.close();
        
        return result;        
    }
    
    public MiniNews getMiniNews(User u, int id) throws SQLException {
        MiniNews result = new MiniNews();
        
        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.LANGUAGE_ID, "
                + "l.DESCRIPTION AS LANGUAGE, "
                + "a.BODY, "
                + "a.NEWS_TIME, "
                + "a.CREATED_BY_USER_ID, "
                + "u.FIRST_NAME || ' ' || u.LAST_NAME AS CREATED_BY_USER, "
                + "a.PUBLISHED "
                + "FROM MINI_NEWS a "
                + "LEFT JOIN LANGUAGE l ON (l.ID = a.LANGUAGE_ID) "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.CREATED_BY_USER_ID) "
                + "WHERE (a.ID = " + id + ") AND (a.COMPANY_ID = " + u.getCompanyId() + ") ";
        
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(select);
        if (rs.next()) {
            result.setId(rs.getInt("ID"));
            result.setCompanyId(rs.getInt("COMPANY_ID"));
            result.setLanguageId(rs.getInt("LANGUAGE_ID"));
            result.setLanguage(rs.getString("LANGUAGE"));
            result.setBody(rs.getString("BODY"));
            result.setTime(rs.getTimestamp("NEWS_TIME"));
            result.setCreatedById(rs.getInt("CREATED_BY_USER_ID"));
            result.setCreatedBy(rs.getString("CREATED_BY_USER"));
            result.setPublished(rs.getBoolean("PUBLISHED"));
        }
        rs.close();
        st.close();
        
        return result;
    }
    
    public int insertMiniNews(MiniNews n) throws SQLException {
        
        int result = 0;
        
        String insert = "INSERT INTO MINI_NEWS ("
                + "COMPANY_ID, "
                + "LANGUAGE_ID, "
                + "BODY, "
                + "NEWS_TIME, "
                + "CREATED_BY_USER_ID, "
                + "PUBLISHED"
                + ") VALUES (?,?,?,?,?,?) returning ID";
        
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, n.getCompanyId());
        ps.setInt(2, n.getLanguageId());
        ps.setString(3, n.getBody());
        ps.setTimestamp(4, new java.sql.Timestamp(n.getTime().getTime()));
        ps.setInt(5, n.getCreatedById());
        ps.setBoolean(6, n.isPublished());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getInt("ID");
        }
        rs.close();
        ps.close();
        
        return result;
    }
    
    public void modifyMiniNews(MiniNews n) throws SQLException {
       
        String update = "UPDATE MINI_NEWS SET "
                + "COMPANY_ID = ?, "
                + "LANGUAGE_ID = ?, "
                + "BODY = ?, "
                + "NEWS_TIME = ?, "
                + "CREATED_BY_USER_ID = ?, "
                + "PUBLISHED = ? "
                + "WHERE (ID = ?) ";
        
        PreparedStatement ps = cnn.prepareStatement(update);
        ps.setInt(1, n.getCompanyId());
        ps.setInt(2, n.getLanguageId());
        ps.setString(3, n.getBody());
        ps.setTimestamp(4, new java.sql.Timestamp(n.getTime().getTime()));
        ps.setInt(5, n.getCreatedById());
        ps.setBoolean(6, n.isPublished());
        ps.setInt(7, n.getId());
        ps.execute();
        ps.close();
    }
    
    public void deleteMiniNews(MiniNews n) throws SQLException {
        String update = "DELETE FROM MINI_NEWS WHERE (ID = ?) AND (COMPANY_ID = ?) ";
        PreparedStatement ps = cnn.prepareStatement(update);
        ps.setInt(1, n.getId());
        ps.setInt(2, n.getCompanyId());
        ps.execute();
        ps.close();
    }
    
}
