/*
 *       MiniNewsDAO.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author berk
 */
public class MiniNewsDAO extends DAO {

    public MiniNewsDAO (Connection cnn) {
        super(cnn);
    }
    
    public int getMiniNewsSize(User u, boolean publishedOnly) throws SQLException {
        int result = 0;
        
        String filter = "";
        if (publishedOnly) {
            filter = "AND (a.PUBLISHED = 1) ";
        }
        
        String select = "SELECT "
                + "count(*) AS CNT "
                + "FROM MINI_NEWS a "
                + "LEFT JOIN LANGUAGE l ON (l.ID = a.LANGUAGE_ID) "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.CREATED_BY_USER_ID) "
                + "WHERE (a.COMPANY_ID = " + u.getCompanyId() + ") "
                + filter;
        
        try (Statement st = cnn.createStatement()) {
            try (ResultSet rs = st.executeQuery(select)) {
                if (rs.next()) {
                    result = rs.getInt("CNT");
                }
            }
        }
        
        return result;
    }
    
    public List<MiniNews> getMiniNewsList(int pageNo, int pageSize, User u, boolean publishedOnly) throws SQLException {
        ArrayList<MiniNews> result = new ArrayList<>();
        
        String filter = "";
        if (publishedOnly) {
            filter = "AND (a.PUBLISHED = 1) ";
        }
        
        String select = "SELECT FIRST " + (pageNo * pageSize)
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.LANGUAGE_ID, "
                + "l.DESCRIPTION AS LANGUAGE, "
                + "a.BODY, "
                + "a.NEWS_TIME, "
                + "a.CREATED_BY_USER_ID, "
                + "u.SALUTATION AS CREATED_BY_SALUTATION, "
                + "u.FIRST_NAME AS CREATED_BY_FIRST_NAME, "
                + "u.LAST_NAME AS CREATED_BY_LAST_NAME, "
                + "a.PUBLISHED "
                + "FROM MINI_NEWS a "
                + "LEFT JOIN LANGUAGE l ON (l.ID = a.LANGUAGE_ID) "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.CREATED_BY_USER_ID) "
                + "WHERE (a.COMPANY_ID = " + u.getCompanyId() + ") "
                + filter
                + "ORDER BY a.NEWS_TIME DESC";
        
        try (Statement st = cnn.createStatement(); ResultSet rs = st.executeQuery(select)) {
            
            int cPageNo = 1;
            int cArtNo = 0;
            
            while (rs.next()) {
                if (cPageNo == pageNo) {
                    MiniNews mn = new MiniNews();
                    mn.setId(rs.getInt("ID"));
                    mn.setCompanyId(rs.getInt("COMPANY_ID"));
                    mn.setLanguageId(rs.getInt("LANGUAGE_ID"));
                    mn.setLanguage(rs.getString("LANGUAGE"));
                    mn.setBody(rs.getString("BODY"));
                    mn.setTime(rs.getTimestamp("NEWS_TIME"));
                    User p = new User();
                    p.setId(rs.getInt("CREATED_BY_USER_ID"));
                    p.setSalutation(rs.getString("CREATED_BY_SALUTATION"));
                    p.setFirstName(rs.getString("CREATED_BY_FIRST_NAME"));
                    p.setLastName(rs.getString("CREATED_BY_LAST_NAME"));
                    mn.setCreatedBy(p);
                    mn.setPublished(rs.getBoolean("PUBLISHED"));
                    result.add(mn);
                }
                
                cArtNo++;
                if (cArtNo == pageSize) {
                    cPageNo++;
                    cArtNo = 0;
                }
            }
        }
        
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
                + "u.SALUTATION AS CREATED_BY_SALUTATION, "
                + "u.FIRST_NAME AS CREATED_BY_FIRST_NAME, "
                + "u.LAST_NAME AS CREATED_BY_LAST_NAME, "
                + "a.PUBLISHED "
                + "FROM MINI_NEWS a "
                + "LEFT JOIN LANGUAGE l ON (l.ID = a.LANGUAGE_ID) "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.CREATED_BY_USER_ID) "
                + "WHERE (a.ID = " + id + ") AND (a.COMPANY_ID = " + u.getCompanyId() + ") ";
        
        try (Statement st = cnn.createStatement(); ResultSet rs = st.executeQuery(select)) {
            if (rs.next()) {
                result.setId(rs.getInt("ID"));
                result.setCompanyId(rs.getInt("COMPANY_ID"));
                result.setLanguageId(rs.getInt("LANGUAGE_ID"));
                result.setLanguage(rs.getString("LANGUAGE"));
                result.setBody(rs.getString("BODY"));
                result.setTime(rs.getTimestamp("NEWS_TIME"));
                User p = new User();
                p.setId(rs.getInt("CREATED_BY_USER_ID"));
                p.setSalutation(rs.getString("CREATED_BY_SALUTATION"));
                p.setFirstName(rs.getString("CREATED_BY_FIRST_NAME"));
                p.setLastName(rs.getString("CREATED_BY_LAST_NAME"));
                result.setCreatedBy(p);
                result.setPublished(rs.getBoolean("PUBLISHED"));
            }
        }
        
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
        
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, n.getCompanyId());
            ps.setInt(2, n.getLanguageId());
            ps.setString(3, n.getBody());
            ps.setTimestamp(4, new java.sql.Timestamp(n.getTime().getTime()));
            ps.setInt(5, n.getCreatedBy().getId());
            ps.setBoolean(6, n.isPublished());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("ID");
                }
            }
        }
        
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
        
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, n.getCompanyId());
            ps.setInt(2, n.getLanguageId());
            ps.setString(3, n.getBody());
            ps.setTimestamp(4, new java.sql.Timestamp(n.getTime().getTime()));
            ps.setInt(5, n.getCreatedBy().getId());
            ps.setBoolean(6, n.isPublished());
            ps.setInt(7, n.getId());
            ps.execute();
        }
    }
    
    public void deleteMiniNews(MiniNews n) throws SQLException {
        String update = "DELETE FROM MINI_NEWS WHERE (ID = ?) AND (COMPANY_ID = ?) ";
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, n.getId());
            ps.setInt(2, n.getCompanyId());
            ps.execute();
        }
    }
    
}
