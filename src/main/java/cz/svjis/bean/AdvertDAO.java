/*
 *       AdvertDAO.java
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

public class AdvertDAO extends DAO {
    
    private static final String generalSelect = "SELECT \n" +
            "r.ID, \n" +
            "r.COMPANY_ID, \n" +
            "r.TYPE_ID, \n" +
            "t.DESCRIPTION as \"TYPE_DESCRIPTION\", \n" +
            "r.HEADER, \n" +
            "r.\"BODY\", \n" +
            "r.USER_ID, \n" +
            "u.SALUTATION, \n" +
            "u.FIRST_NAME, \n" +
            "u.LAST_NAME, \n" +
            "u.CELL_PHONE, \n" +
            "u.E_MAIL, \n" +
            "r.CREATION_DATE, \n" +
            "r.PUBLISHED \n" + 
            "FROM ADVERT r \n" + 
            "LEFT JOIN \"USER\" u ON u.ID = r.USER_ID \n" + 
            "LEFT JOIN ADVERT_TYPE t ON t.ID = r.TYPE_ID \n" + 
            "WHERE r.COMPANY_ID = ? \n";

    
    public AdvertDAO(Connection cnn) {
        super(cnn);
    }
    
    
    private Advert mapRsToAdvert(ResultSet rs) throws SQLException {
        Advert result = new Advert();
        result.setId(rs.getInt("ID"));
        result.setCompanyId(rs.getInt("COMPANY_ID"));
        result.getType().setId(rs.getInt("TYPE_ID"));
        result.getType().setDescription("TYPE_DESCRIPTION");
        result.setHeader(rs.getString("HEADER"));
        result.setBody(rs.getString("BODY"));
        result.getUser().setId(rs.getInt("USER_ID"));
        result.getUser().setSalutation(rs.getString("SALUTATION"));
        result.getUser().setFirstName(rs.getString("FIRST_NAME"));
        result.getUser().setLastName(rs.getString("LAST_NAME"));
        result.getUser().setCellPhone(rs.getString("CELL_PHONE"));
        result.getUser().seteMail(rs.getString("E_MAIL"));
        result.setCreationDate(rs.getTimestamp("CREATION_DATE"));
        result.setPublished(rs.getBoolean("PUBLISHED"));
        return result;
    }
    
    
    public Advert getAdvert(int companyId, int id) throws SQLException {
        Advert result = null;
        String select = generalSelect + "and r.ID = ?";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = mapRsToAdvert(rs);
                }
            }
        }
        
        return result;
    }
    
    
    public List<Advert> getAdvertList(int companyId) throws SQLException {
        ArrayList<Advert> result = new ArrayList<>();
        String select = generalSelect + "ORDER BY r.CREATION_DATE DESC";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Advert a = mapRsToAdvert(rs);
                    result.add(a);
                }
            }
        }
        
        return result;
    }
    
    
    public int insertAdvert(Advert a) throws SQLException {
        int result = 0;
        String insert = "INSERT INTO ADVERT (COMPANY_ID, TYPE_ID, HEADER, \"BODY\", USER_ID, CREATION_DATE, PUBLISHED) VALUES (?,?,?,?,?,?,?) returning ID;";
        
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, a.getCompanyId());
            ps.setInt(2, a.getType().getId());
            ps.setString(3, a.getHeader());
            ps.setString(4, a.getBody());
            ps.setInt(5, a.getUser().getId());
            ps.setTimestamp(6, new java.sql.Timestamp(a.getCreationDate().getTime()));
            ps.setInt(7, (a.isPublished()) ? 1 : 0);
            
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("ID");
                }
            }
        }
        
        return result;
    }
    
    
    public void modifyAdvert(Advert a) throws SQLException {
        String update = "UPDATE ADVERT a SET a.TYPE_ID = ?, a.HEADER = ?, a.\"BODY\" = ?, a.USER_ID = ?, a.CREATION_DATE = ?, a.PUBLISHED = ? WHERE a.ID = ? AND a.COMPANY_ID = ?";
        
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, a.getType().getId());
            ps.setString(2, a.getHeader());
            ps.setString(3, a.getBody());
            ps.setInt(4, a.getUser().getId());
            ps.setTimestamp(5, new java.sql.Timestamp(a.getCreationDate().getTime()));
            ps.setInt(6, (a.isPublished()) ? 1 : 0);
            ps.setInt(7, a.getId());
            ps.setInt(8, a.getCompanyId());
            ps.execute();
        }
    }
    
    
    public void deleteAdvert(Advert a) throws SQLException {
        String update = "UPDATE FROM ADVERT a WHERE a.ID = ? AND a.COMPANY_ID = ?";
        
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, a.getId());
            ps.setInt(2, a.getCompanyId());
            ps.execute();
        }
    }
    
    
    public List<AdvertType> getAdvertTypeList() throws SQLException {
        ArrayList<AdvertType> result = new ArrayList<>();
        String select = "SELECT r.ID, r.DESCRIPTION FROM ADVERT_TYPE r ORDER BY r.ID";
        
        try (Statement st = cnn.createStatement(); ResultSet rs = st.executeQuery(select)) {
            
            while (rs.next()) {
                AdvertType t = new AdvertType();
                t.setId(rs.getInt("ID"));
                t.setDescription(rs.getString("DESCRIPTION"));
                result.add(t);
            }
        }
        
        return result;
    }
}
