/*
 *       AttachmentDAO.java
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

public class AttachmentDAO extends DAO {
    
    private String tableName;
    private String documentColumnName;


    public AttachmentDAO (Connection cnn, String tableName, String documentColumnName) {
        super(cnn);
        this.tableName = tableName;
        this.documentColumnName = documentColumnName; 
    }


    public List<Attachment> getAttachmentList(int documentId) throws SQLException {
        ArrayList<Attachment> result = new ArrayList<>();
        String select = String.format("SELECT "
                + "a.ID, "
                + "%s, "
                + "a.USER_ID, "
                + "b.COMPANY_ID, "
                + "b.SALUTATION, "
                + "b.FIRST_NAME, "
                + "b.LAST_NAME, "
                + "a.UPLOAD_TIME, "
                + "a.CONTENT_TYPE, "
                + "a.FILENAME "
                //+ "a.DATA "
                + "FROM %s a "
                + "LEFT JOIN \"USER\" b on b.ID = a.USER_ID "
                + "WHERE (a.%s = ?) "
                + "ORDER BY a.ID", this.documentColumnName, this.tableName, this.documentColumnName);
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, documentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Attachment a = new Attachment();
                    a.setId(rs.getInt("ID"));
                    a.setDocumentId(rs.getInt(this.documentColumnName));
                    User u = new User();
                    u.setId(rs.getInt("USER_ID"));
                    u.setCompanyId(rs.getInt("COMPANY_ID"));
                    u.setSalutation(rs.getString("SALUTATION"));
                    u.setFirstName(rs.getString("FIRST_NAME"));
                    u.setLastName(rs.getString("LAST_NAME"));
                    a.setUser(u);
                    a.setUploadTime(rs.getTimestamp("UPLOAD_TIME"));
                    a.setContentType(rs.getString("CONTENT_TYPE"));
                    a.setFileName(rs.getString("FILENAME"));
                    result.add(a);
                }
            }
        }
        return result;
    }
    
    
    public void insertAttachment(Attachment a) throws SQLException {
        String insert = String.format("INSERT INTO %s (%s, USER_ID, UPLOAD_TIME, CONTENT_TYPE, FILENAME, DATA) VALUES (?,?,?,?,?,?)", this.tableName, this.documentColumnName);
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, a.getDocumentId());
            ps.setInt(2, a.getUser().getId());
            ps.setTimestamp(3, new java.sql.Timestamp(a.getUploadTime().getTime()));
            ps.setString(4, a.getContentType());
            ps.setString(5, a.getFileName());
            ps.setBytes(6, a.getData());
            ps.execute();
        }
    }
    

    public Attachment getAttachment(int id) throws SQLException {
        Attachment result = null;
        String select = String.format("SELECT "
                + "a.ID, "
                + "a.%s, "
                + "a.USER_ID, "
                + "b.COMPANY_ID, "
                + "b.SALUTATION, "
                + "b.FIRST_NAME, "
                + "b.LAST_NAME, "
                + "a.UPLOAD_TIME, "
                + "a.CONTENT_TYPE, "
                + "a.FILENAME, "
                + "a.DATA "
                + "FROM %s a "
                + "LEFT JOIN \"USER\" b on b.ID = a.USER_ID "
                + "WHERE (a.ID = ?) ", this.documentColumnName, this.tableName);
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new Attachment();
                    result.setId(rs.getInt("ID"));
                    result.setDocumentId(rs.getInt(this.documentColumnName));
                    User u = new User();
                    u.setId(rs.getInt("USER_ID"));
                    u.setCompanyId(rs.getInt("COMPANY_ID"));
                    u.setSalutation(rs.getString("SALUTATION"));
                    u.setFirstName(rs.getString("FIRST_NAME"));
                    u.setLastName(rs.getString("LAST_NAME"));
                    result.setUser(u);
                    result.setUploadTime(rs.getTimestamp("UPLOAD_TIME"));
                    result.setContentType(rs.getString("CONTENT_TYPE"));
                    result.setFileName(rs.getString("FILENAME"));
                    java.sql.Blob blob;
                    blob = rs.getBlob("DATA");
                    result.setData(blob.getBytes(1, (int) blob.length()));
                }
            }
        }
        return result;
    }
    
    
    public void deleteAttachment(int id) throws SQLException {
        String delete = String.format("DELETE FROM %s a WHERE (a.ID = ?)", this.tableName);
        try (PreparedStatement ps = cnn.prepareStatement(delete)) {
            ps.setInt(1, id);
            ps.execute();
        }
    }
}
