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
import java.util.ArrayList;
import java.util.List;

public class AdvertDAO extends DAO {
    
    private AttachmentDAO attDao;
    
    public enum AdvertListType {
        ADVERT_TYPE,
        USER
    }
    
    public static final int MY_ADVERTS_TYPE_ID = 99;
    
    private static final String ADVERT_SELECT = "SELECT %s \n" +
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
            "u.E_MAIL AS \"UE_MAIL\", \n" +
            "r.PHONE, \n" +
            "r.E_MAIL, \n" +
            "r.CREATION_DATE, \n" +
            "r.PUBLISHED \n" + 
            "FROM ADVERT r \n" + 
            "LEFT JOIN \"USER\" u ON u.ID = r.USER_ID \n" + 
            "LEFT JOIN ADVERT_TYPE t ON t.ID = r.TYPE_ID \n" + 
            "WHERE r.COMPANY_ID = ? %s\n";

    
    public AdvertDAO(Connection cnn) {
        super(cnn);
        attDao = new AttachmentDAO(cnn, "ADVERT_ATTACHMENT", "ADVERT_ID");
    }
    
    
    private Advert mapRsToAdvert(ResultSet rs) throws SQLException {
        Advert result = new Advert();
        result.setId(rs.getInt("ID"));
        result.setCompanyId(rs.getInt("COMPANY_ID"));
        result.getType().setId(rs.getInt("TYPE_ID"));
        result.getType().setDescription(rs.getString("TYPE_DESCRIPTION"));
        result.setHeader(rs.getString("HEADER"));
        result.setBody(rs.getString("BODY"));
        result.getUser().setId(rs.getInt("USER_ID"));
        result.getUser().setSalutation(rs.getString("SALUTATION"));
        result.getUser().setFirstName(rs.getString("FIRST_NAME"));
        result.getUser().setLastName(rs.getString("LAST_NAME"));
        result.getUser().setCellPhone(rs.getString("CELL_PHONE"));
        result.getUser().seteMail(rs.getString("UE_MAIL"));
        result.setPhone(rs.getString("PHONE"));
        result.seteMail(rs.getString("E_MAIL"));
        result.setCreationDate(rs.getTimestamp("CREATION_DATE"));
        result.setPublished(rs.getBoolean("PUBLISHED"));
        return result;
    }
    
    
    public Advert getAdvert(int companyId, int id) throws SQLException {
        Advert result = null;
        String select = String.format(ADVERT_SELECT, "", "and r.ID = ?");
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = mapRsToAdvert(rs);
                }
            }
        }
        
        if (result != null) {
            result.setAttachmentList(this.getAttachmentList(result.getId()));
        }
        
        return result;
    }
    
    
    public int getAdverListSize(AdvertListType listType, int companyId, int typeId) throws SQLException {
        int result = 0;
        
        String whereType = "r.TYPE_ID = ? AND PUBLISHED = 1 AND u.ENABLED = 1";
        if (listType == AdvertListType.USER) {
            whereType = "r.USER_ID = ?";
        }
        
        String select = "SELECT count(*) AS \"CNT\" FROM ADVERT r LEFT JOIN \"USER\" u ON u.ID = r.USER_ID WHERE r.COMPANY_ID = ? AND " + whereType;
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            ps.setInt(2, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("CNT");
                }
            }
        }
        
        return result;
    }
    
    
    public List<Advert> getAdvertList(int pageNo, int pageSize, AdvertListType listType, int companyId, int typeId) throws SQLException {
        ArrayList<Advert> result = new ArrayList<>();
        
        String whereType = "r.TYPE_ID = ? AND PUBLISHED = 1 AND u.ENABLED = 1";
        if (listType == AdvertListType.USER) {
            whereType = "r.USER_ID = ?";
        }
        
        String select = String.format(ADVERT_SELECT, "FIRST " + (pageNo * pageSize), "AND " + whereType + " ORDER BY r.CREATION_DATE DESC");
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            ps.setInt(2, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                
                int cPageNo = 1;
                int cArtNo = 0;
                
                while (rs.next()) {
                    if (cPageNo == pageNo) {
                        Advert a = mapRsToAdvert(rs);
                        result.add(a);
                    }
                    
                    cArtNo++;
                    if (cArtNo == pageSize) {
                        cPageNo++;
                        cArtNo = 0;
                    }
                }
            }
        }
        
        return result;
    }
    
    
    public int insertAdvert(Advert a) throws SQLException {
        int result = 0;
        String insert = "INSERT INTO ADVERT (COMPANY_ID, TYPE_ID, HEADER, \"BODY\", USER_ID, PHONE, E_MAIL, CREATION_DATE, PUBLISHED) VALUES (?,?,?,?,?,?,?,?,?) returning ID;";
        
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, a.getCompanyId());
            ps.setInt(2, a.getType().getId());
            ps.setString(3, a.getHeader());
            ps.setString(4, a.getBody());
            ps.setInt(5, a.getUser().getId());
            ps.setString(6, a.getPhone());
            ps.setString(7, a.geteMail());
            ps.setTimestamp(8, new java.sql.Timestamp(a.getCreationDate().getTime()));
            ps.setInt(9, (a.isPublished()) ? 1 : 0);
            
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("ID");
                }
            }
        }
        
        return result;
    }
    
    
    public void modifyAdvert(Advert a) throws SQLException {
        String update = "UPDATE ADVERT a SET a.TYPE_ID = ?, a.HEADER = ?, a.\"BODY\" = ?, a.USER_ID = ?, a.PHONE = ?, a.E_MAIL = ?, a.CREATION_DATE = ?, a.PUBLISHED = ? WHERE a.ID = ? AND a.COMPANY_ID = ?";
        
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, a.getType().getId());
            ps.setString(2, a.getHeader());
            ps.setString(3, a.getBody());
            ps.setInt(4, a.getUser().getId());
            ps.setString(5, a.getPhone());
            ps.setString(6, a.geteMail());
            ps.setTimestamp(7, new java.sql.Timestamp(a.getCreationDate().getTime()));
            ps.setInt(8, (a.isPublished()) ? 1 : 0);
            ps.setInt(9, a.getId());
            ps.setInt(10, a.getCompanyId());
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
    
    
    public List<AdvertType> getAdvertTypeList(int companyId) throws SQLException {
        ArrayList<AdvertType> result = new ArrayList<>();
        String select = "SELECT r.ID, r.DESCRIPTION, (SELECT count(*) FROM ADVERT a LEFT JOIN \"USER\" u ON u.ID = a.USER_ID WHERE a.TYPE_ID = r.ID AND a.COMPANY_ID = ? AND a.PUBLISHED = 1 AND u.ENABLED = 1) AS \"CNT\" \n" + 
                "FROM ADVERT_TYPE r \n" + 
                "ORDER BY r.ID";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AdvertType t = new AdvertType();
                    t.setId(rs.getInt("ID"));
                    t.setDescription(rs.getString("DESCRIPTION"));
                    t.setCnt(rs.getInt("CNT"));
                    result.add(t);
                }
            }
        }
        
        return result;
    }
    
    public AdvertType getMyAdvertType(int companyId, int userId) throws SQLException {
        AdvertType result = new AdvertType();
        String select = String.format("SELECT '%d' AS \"ID\", 'My adverts' AS \"DESCRIPTION\", (SELECT count(*) FROM ADVERT a WHERE a.USER_ID = ? AND a.COMPANY_ID = ?  AND a.PUBLISHED = 1) AS \"CNT\" \n" + 
                "FROM ADVERT_TYPE r \n" + 
                "ORDER BY r.ID", MY_ADVERTS_TYPE_ID);
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, userId);
            ps.setInt(2, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.setId(rs.getInt("ID"));
                    result.setDescription(rs.getString("DESCRIPTION"));
                    result.setCnt(rs.getInt("CNT"));
                }
            }
        }
        
        return result;
    }
    
    private ArrayList<Attachment> getAttachmentList(int advertId) throws SQLException {
        return attDao.getAttachmentList(advertId);
    }
    
    public void insertAttachment(Attachment a) throws SQLException {
        attDao.insertAttachment(a);
    }
    
    public Attachment getAttachment(int id) throws SQLException {
        return attDao.getAttachment(id);
    }
    
    public void deleteAttachment(int id) throws SQLException {
        attDao.deleteAttachment(id);
    }
}
