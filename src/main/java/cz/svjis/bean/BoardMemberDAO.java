/*
 *       BoardMemberDAO.java
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
 * @author jarberan
 */
public class BoardMemberDAO extends DAO {
    
    public BoardMemberDAO (Connection cnn) {
        super(cnn);
    }
    
    
    public List<BoardMemberType> getBoardMemberTypes() throws SQLException {
        ArrayList<BoardMemberType> result = new ArrayList<>();
        String select = "SELECT a.ID, a.DESCRIPTION FROM BOARD_MEMBER_TYPE a ORDER BY a.ID";
        
        try (
                Statement st = cnn.createStatement(); 
                ResultSet rs = st.executeQuery(select)
            ) {
            
            while (rs.next()) {
                BoardMemberType bmt = new BoardMemberType();
                bmt.setId(rs.getInt("ID"));
                bmt.setDescription(rs.getString("DESCRIPTION"));
                result.add(bmt);
            }
        }
        
        return result;
    }
    
    
    public List<BoardMember> getBoardMembers(int companyId) throws SQLException {
        ArrayList<BoardMember> result = new ArrayList<>();
        String select = "SELECT a.USER_ID,\n" +
                        "       c.FIRST_NAME,\n" +
                        "       c.LAST_NAME,\n" +
                        "       c.SALUTATION,\n" +
                        "       a.BOARD_MEMBER_TYPE_ID,\n" +
                        "       b.DESCRIPTION AS BOARD_MEMBER_TYPE\n" +
                        "FROM BOARD_MEMBER a\n" +
                        "LEFT JOIN BOARD_MEMBER_TYPE b ON b.ID = a.BOARD_MEMBER_TYPE_ID\n" +
                        "LEFT JOIN \"USER\" c ON c.ID = a.USER_ID\n" +
                        "WHERE c.COMPANY_ID = ?\n" +
                        "ORDER BY a.BOARD_MEMBER_TYPE_ID, c.LAST_NAME collate UNICODE_CI_AI";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BoardMemberType bmt = new BoardMemberType();
                    bmt.setId(rs.getInt("BOARD_MEMBER_TYPE_ID"));
                    bmt.setDescription(rs.getString("BOARD_MEMBER_TYPE"));
                    User u = new User();
                    u.setId(rs.getInt("USER_ID"));
                    u.setFirstName(rs.getString("FIRST_NAME"));
                    u.setLastName(rs.getString("LAST_NAME"));
                    u.setSalutation(rs.getString("SALUTATION"));
                    BoardMember bm = new BoardMember();
                    bm.setBoardMemberType(bmt);
                    bm.setUser(u);
                    result.add(bm);
                }
            }
        }
        
        return result;
    }
    
    
    public BoardMember getBoardMember(int companyId, int userId, int typeId) throws SQLException {
        BoardMember result = new BoardMember();
        String select = "SELECT a.USER_ID,\n" +
                "       c.FIRST_NAME,\n" +
                "       c.LAST_NAME,\n" +
                "       c.SALUTATION,\n" +
                "       a.BOARD_MEMBER_TYPE_ID,\n" +
                "       b.DESCRIPTION AS BOARD_MEMBER_TYPE\n" +
                "FROM BOARD_MEMBER a\n" +
                "LEFT JOIN BOARD_MEMBER_TYPE b ON b.ID = a.BOARD_MEMBER_TYPE_ID\n" +
                "LEFT JOIN \"USER\" c ON c.ID = a.USER_ID\n" +
                "WHERE c.COMPANY_ID = ? AND a.USER_ID = ? AND a.BOARD_MEMBER_TYPE_ID = ?\n" +
                "ORDER BY a.BOARD_MEMBER_TYPE_ID, c.LAST_NAME collate UNICODE_CI_AI";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            ps.setInt(2, userId);
            ps.setInt(3, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BoardMemberType bmt = new BoardMemberType();
                    bmt.setId(rs.getInt("BOARD_MEMBER_TYPE_ID"));
                    bmt.setDescription(rs.getString("BOARD_MEMBER_TYPE"));
                    User u = new User();
                    u.setId(rs.getInt("USER_ID"));
                    u.setFirstName(rs.getString("FIRST_NAME"));
                    u.setLastName(rs.getString("LAST_NAME"));
                    u.setSalutation(rs.getString("SALUTATION"));
                    result = new BoardMember();
                    result.setBoardMemberType(bmt);
                    result.setUser(u);
                }
            }
        }
        
        return result;
    }
    
    
    public void addBoardMember(int companyId, int userId, int typeId) throws SQLException {
        String selectUser = "SELECT a.ID FROM \"USER\" a WHERE a.COMPANY_ID = ? AND a.ID = ? AND a.ENABLED = 1";
        String selectType = "SELECT a.ID FROM BOARD_MEMBER_TYPE a WHERE a.ID = ?";
        String selectBoard = "SELECT a.USER_ID FROM BOARD_MEMBER a WHERE a.USER_ID = ? AND a.BOARD_MEMBER_TYPE_ID = ?";
        String insert = "INSERT INTO BOARD_MEMBER (USER_ID, BOARD_MEMBER_TYPE_ID) VALUES (?,?)";
        
        //-- Does user exist?
        try (PreparedStatement ps = cnn.prepareStatement(selectUser)) {
            ps.setInt(1, companyId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return;
                }
            }
        }
        
        //-- Does type exist?
        try (PreparedStatement ps = cnn.prepareStatement(selectType)) {
            ps.setInt(1, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return;
                }
            }
        }
        
        //-- Does board member exist?
        try (PreparedStatement ps = cnn.prepareStatement(selectBoard)) {
            ps.setInt(1, userId);
            ps.setInt(2, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return;
                }
            }
        }
        
        //-- Insert board member
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, userId);
            ps.setInt(2, typeId);
            ps.execute();
        }
    }   
    
    
    public void deleteBoardMember(int companyId, int userId, int typeId) throws SQLException {
        String selectUser = "SELECT a.ID FROM \"USER\" a WHERE a.COMPANY_ID = ? AND a.ID = ? AND a.ENABLED = 1";
        String delete = "DELETE FROM BOARD_MEMBER WHERE USER_ID = ? AND BOARD_MEMBER_TYPE_ID = ?";
    
        //-- Does user exist?
        try (PreparedStatement ps = cnn.prepareStatement(selectUser)) {
            ps.setInt(1, companyId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return;
                }
            }
        }
        
        //-- Delete board member
        try (PreparedStatement ps = cnn.prepareStatement(delete)) {
            ps.setInt(1, userId);
            ps.setInt(2, typeId);
            ps.execute();
        }
    }
}
