/*
 *       CommentDAO.java
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

public class CommentDAO extends DAO  {

    private String tableName;
    private String documentColumnName;


    public CommentDAO (Connection cnn, String tableName, String documentColumnName) {
        super(cnn);
        this.tableName = tableName;
        this.documentColumnName = documentColumnName; 
    }
    
    
    public ArrayList<Comment> getCommentList(int documentId) throws SQLException {
        ArrayList<Comment> result = new ArrayList<>();
        String select = String.format("SELECT "
                + "a.ID, "
                + "a.%s, "
                + "a.USER_ID, "
                + "u.SALUTATION, "
                + "u.FIRST_NAME, "
                + "u.LAST_NAME, "
                + "a.INSERTION_TIME, "
                + "a.BODY "
                + "FROM %s a "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.USER_ID) "
                + "WHERE a.%s = ? "
                + "ORDER BY a.ID", this.documentColumnName, this.tableName, this.documentColumnName);
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, documentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comment a = new Comment();
                    a.setId(rs.getInt("ID"));
                    a.setDocumentId(rs.getInt(this.documentColumnName));
                    a.setUser(new User());
                    a.getUser().setId(rs.getInt("USER_ID"));
                    a.getUser().setSalutation(rs.getString("SALUTATION"));
                    a.getUser().setFirstName(rs.getString("FIRST_NAME"));
                    a.getUser().setLastName(rs.getString("LAST_NAME"));
                    a.setInsertionTime(rs.getTimestamp("INSERTION_TIME"));
                    a.setBody(rs.getString("BODY"));
                    result.add(a);
                }
            }
        }
        return result;
    }
    
    
    public void insertComment(Comment c) throws SQLException {
        String insert = String.format("INSERT INTO %s (%s, USER_ID, INSERTION_TIME, BODY) VALUES (?,?,?,?)", this.tableName, this.documentColumnName);
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, c.getDocumentId());
            ps.setInt(2, c.getUser().getId());
            ps.setTimestamp(3, new java.sql.Timestamp(c.getInsertionTime().getTime()));
            ps.setString(4, c.getBody());
            ps.execute();
        }
    }
}
