/*
 *       InquiryDAO.java
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author berk
 */
public class InquiryDAO extends DAO {

    public InquiryDAO (Connection cnn) {
        super(cnn);
    }
    
    public boolean canUserVoteInquiry(User user, int inquiryId) throws SQLException {
        boolean result = true;
        
        if (!user.hasPermission(Permission.CAN_VOTE_INQUIRY)) {
            result = false;
        }
        
        String select = "SELECT "
                + "a.INQUIRY_OPTION_ID "
                + "FROM INQUIRY_VOTING_LOG a "
                + "LEFT JOIN INQUIRY_OPTION o ON (o.ID = a.INQUIRY_OPTION_ID) "
                + "WHERE (a.USER_ID = ?) AND (o.INQUIRY_ID = ?)";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, user.getId());
            ps.setInt(2, inquiryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = false;
                }
            }
        }
        
        return result;
    }
    
    public int getInquirySize(User user, boolean publishedOnly) throws SQLException {
        int result = 0;
        
        String filter = "";
        if (publishedOnly) {
            filter = "AND (a.ENABLED = 1) ";
        }
        
        String select = "SELECT "
                + "count(*) AS CNT "
                + "FROM INQUIRY a "
                + "LEFT JOIN \"USER\" u ON (u.ID = USER_ID) "
                + "WHERE (a.COMPANY_ID = " + user.getCompanyId() + ") "
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
    
    public List<Inquiry> getInquiryList(int pageNo, int pageSize, User user, boolean publishedOnly) throws SQLException {
        ArrayList<Inquiry> result = new ArrayList<>();
        
        String filter = "";
        if (publishedOnly) {
            filter = "AND (a.ENABLED = 1) ";
        }
        
        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.USER_ID, "
                + "u.SALUTATION AS CREATED_BY_SALUTATION, "
                + "u.FIRST_NAME AS CREATED_BY_FIRST_NAME, "
                + "u.LAST_NAME AS CREATED_BY_LAST_NAME, "
                + "a.DESCRIPTION, "
                + "a.STARTING_DATE, "
                + "a.ENDING_DATE, "
                + "a.ENABLED, "
                + "(SELECT COUNT(*) FROM INQUIRY_VOTING_LOG l LEFT JOIN INQUIRY_OPTION o ON (o.ID = l.INQUIRY_OPTION_ID) WHERE (o.INQUIRY_ID = a.ID)) AS CNT, "
                + "(SELECT MAX(CNT) FROM (SELECT COUNT(*) AS CNT FROM INQUIRY_VOTING_LOG l LEFT JOIN INQUIRY_OPTION o ON (o.ID = l.INQUIRY_OPTION_ID) WHERE (o.INQUIRY_ID = a.ID) GROUP BY o.ID)) AS \"MAX\" "
                + "FROM INQUIRY a "
                + "LEFT JOIN \"USER\" u ON (u.ID = USER_ID) "
                + "WHERE (a.COMPANY_ID = ?) "
                + filter
                + "ORDER BY a.ID DESC";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, user.getCompanyId());
            
            try (ResultSet rs = ps.executeQuery()) {
            
                int cPageNo = 1;
                int cArtNo = 0;
                
                while (rs.next()) {
                    if (cPageNo == pageNo) {
                        Inquiry i = new Inquiry();
                        i.setId(rs.getInt("ID"));
                        i.setCompanyId(rs.getInt("COMPANY_ID"));
                        User p = new User();
                        p.setId(rs.getInt("USER_ID"));
                        p.setSalutation(rs.getString("CREATED_BY_SALUTATION"));
                        p.setFirstName(rs.getString("CREATED_BY_FIRST_NAME"));
                        p.setLastName(rs.getString("CREATED_BY_LAST_NAME"));
                        i.setUser(p);
                        i.setDescription(rs.getString("DESCRIPTION"));
                        i.setStartingDate(rs.getDate("STARTING_DATE"));
                        i.setEndingDate(rs.getDate("ENDING_DATE"));
                        i.setEnabled(rs.getBoolean("ENABLED"));
                        i.setCount(rs.getInt("CNT"));
                        i.setMaximum(rs.getInt("MAX"));
                        result.add(i);
                    }
                    
                    cArtNo++;
                    if (cArtNo == pageSize) {
                        cPageNo++;
                        cArtNo = 0;
                    }
                }
            }
        }
        
        Iterator<Inquiry> iI = result.iterator();
        while (iI.hasNext()) {
            Inquiry i = iI.next();
            i.setOptionList(getInquiryOptionList(i.getId()));
            i.setUserCanVote(canUserVoteInquiry(user, i.getId()));
        }
        
        return result;
    }
    
    public Inquiry getInquiry(User user, int id) throws SQLException {
        Inquiry result = new Inquiry();
        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.USER_ID, "
                + "u.SALUTATION AS CREATED_BY_SALUTATION, "
                + "u.FIRST_NAME AS CREATED_BY_FIRST_NAME, "
                + "u.LAST_NAME AS CREATED_BY_LAST_NAME, "
                + "a.DESCRIPTION, "
                + "a.STARTING_DATE, "
                + "a.ENDING_DATE, "
                + "a.ENABLED, "
                + "(SELECT COUNT(*) FROM INQUIRY_VOTING_LOG l LEFT JOIN INQUIRY_OPTION o ON (o.ID = l.INQUIRY_OPTION_ID) WHERE (o.INQUIRY_ID = a.ID)) AS CNT, "
                + "(SELECT MAX(CNT) FROM (SELECT COUNT(*) AS CNT FROM INQUIRY_VOTING_LOG l LEFT JOIN INQUIRY_OPTION o ON (o.ID = l.INQUIRY_OPTION_ID) WHERE (o.INQUIRY_ID = a.ID) GROUP BY o.ID)) AS \"MAX\" "
                + "FROM INQUIRY a "
                + "LEFT JOIN \"USER\" u ON (u.ID = USER_ID) "
                + "WHERE (a.COMPANY_ID = ?) AND (a.ID = ?)";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, user.getCompanyId());
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.setId(rs.getInt("ID"));
                    result.setCompanyId(rs.getInt("COMPANY_ID"));
                    User p = new User();
                    p.setId(rs.getInt("USER_ID"));
                    p.setSalutation(rs.getString("CREATED_BY_SALUTATION"));
                    p.setFirstName(rs.getString("CREATED_BY_FIRST_NAME"));
                    p.setLastName(rs.getString("CREATED_BY_LAST_NAME"));
                    result.setUser(p);
                    result.setDescription(rs.getString("DESCRIPTION"));
                    result.setStartingDate(rs.getDate("STARTING_DATE"));
                    result.setEndingDate(rs.getDate("ENDING_DATE"));
                    result.setEnabled(rs.getBoolean("ENABLED"));
                    result.setCount(rs.getInt("CNT"));
                    result.setMaximum(rs.getInt("MAX"));
                }
            }
        }
        result.setOptionList(getInquiryOptionList(result.getId()));
        result.setUserCanVote(canUserVoteInquiry(user, result.getId()));
        return result;
    }
    
    public List<InquiryLog> getInquiryLog(User user, int id) throws SQLException {
        ArrayList<InquiryLog> result = new ArrayList<>();
        String select = "SELECT " +
                        "  a.VOTING_TIME " +
                        "  ,u.FIRST_NAME " +
                        "  ,u.LAST_NAME " +
                        "  ,b.DESCRIPTION " +
                        "FROM INQUIRY_VOTING_LOG a " +
                        "left join INQUIRY_OPTION b on b.ID = a.INQUIRY_OPTION_ID " +
                        "left join INQUIRY c on c.ID = b.INQUIRY_ID " +
                        "left join \"USER\" u on u.ID = a.USER_ID " +
                        "where c.COMPANY_ID = ? and c.ID = ? " +
                        "order by a.ID;";
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, user.getCompanyId());
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InquiryLog l = new InquiryLog();
                    l.setTime(rs.getTimestamp("VOTING_TIME"));
                    l.setUser(rs.getString("FIRST_NAME") + " " + rs.getString("LAST_NAME"));
                    l.setOptionDescription(rs.getString("DESCRIPTION"));
                    result.add(l);
                }
            }
        }
        return result;
    }
    
    public List<InquiryOption> getInquiryOptionList(int inquiryId) throws SQLException {
        ArrayList<InquiryOption> result = new ArrayList<>();
        String select = "SELECT "
                + "a.ID, "
                + "a.INQUIRY_ID, "
                + "a.DESCRIPTION, "
                + "(SELECT count(*) FROM INQUIRY_VOTING_LOG l WHERE (l.INQUIRY_OPTION_ID = a.ID)) AS CNT "
                + "FROM INQUIRY_OPTION a "
                + "WHERE a.INQUIRY_ID = ? "
                + "ORDER BY a.ID";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, inquiryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InquiryOption o = new InquiryOption();
                    o.setId(rs.getInt("ID"));
                    o.setInquiryId(rs.getInt("INQUIRY_ID"));
                    o.setDescription(rs.getString("DESCRIPTION"));
                    o.setCount(rs.getInt("CNT"));
                    result.add(o);
                }
            }
        }
        return result;
    }
    
    public InquiryOption getInquiryOption(int companyId, int id) throws SQLException {
        InquiryOption result = null;
        String select = "SELECT "
                + "a.ID, "
                + "a.INQUIRY_ID, "
                + "a.DESCRIPTION "
                + "FROM INQUIRY_OPTION a "
                + "LEFT JOIN INQUIRY i ON (i.ID = a.INQUIRY_ID) "
                + "WHERE (a.ID = ?) AND (i.COMPANY_ID = ?)";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, id);
            ps.setInt(2, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new InquiryOption();
                    result.setId(rs.getInt("ID"));
                    result.setInquiryId(rs.getInt("INQUIRY_ID"));
                    result.setDescription(rs.getString("DESCRIPTION"));
                }
            }
        }
        return result;
    }
  
    public int insertInquiry(Inquiry i) throws SQLException {
        int result = 0;
        cnn.setAutoCommit(false);
        String update = "INSERT INTO INQUIRY ("
                + "COMPANY_ID, "
                + "USER_ID, "
                + "DESCRIPTION, "
                + "STARTING_DATE, "
                + "ENDING_DATE, "
                + "ENABLED"
                + ") VALUES (?,?,?,?,?,?) returning ID";
        
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, i.getCompanyId());
            ps.setInt(2, i.getUser().getId());
            ps.setString(3, i.getDescription());
            ps.setDate(4, new java.sql.Date(i.getStartingDate().getTime()));
            ps.setDate(5, new java.sql.Date(i.getEndingDate().getTime()));
            ps.setBoolean(6, i.isEnabled());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("ID");
                    i.setId(result);
                }
            }
        }
        
        i.setId(result);
        modifyInquiryOptions(i);
        
        cnn.commit();
        cnn.setAutoCommit(true);
        
        return result;
    }
    
    public void modifyInquiry(Inquiry i) throws SQLException {
        int updated;
        cnn.setAutoCommit(false);
        String update = "UPDATE INQUIRY SET "
                + "USER_ID = ?, "
                + "DESCRIPTION = ?, "
                + "STARTING_DATE = ?, "
                + "ENDING_DATE = ?, "
                + "ENABLED = ? "
                + "WHERE (ID = ?) AND (COMPANY_ID = ?)";
        
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, i.getUser().getId());
            ps.setString(2, i.getDescription());
            ps.setDate(3, new java.sql.Date(i.getStartingDate().getTime()));
            ps.setDate(4, new java.sql.Date(i.getEndingDate().getTime()));
            ps.setBoolean(5, i.isEnabled());
            ps.setInt(6, i.getId());
            ps.setInt(7, i.getCompanyId());
            updated = ps.executeUpdate();
        }
        if (updated == 1) {
            modifyInquiryOptions(i);
        }
        cnn.commit();
        cnn.setAutoCommit(true);
    }
    
    public void modifyInquiryOptions(Inquiry i) throws SQLException {
        Iterator<InquiryOption> it = i.getOptionList().iterator();
        while (it.hasNext()) {
            InquiryOption io = it.next();
            io.setInquiryId(i.getId());
            if (io.getId() == 0) {
                insertInquiryOption(io);
            } else {
                updateInquiryOption(io);
            }
        }
    }

    public void insertInquiryOption(InquiryOption o) throws SQLException {
        String insert = "INSERT INTO INQUIRY_OPTION (INQUIRY_ID, DESCRIPTION) VALUES (?,?)";
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, o.getInquiryId());
            ps.setString(2, o.getDescription());
            ps.execute();
        }
    }
    
    public void updateInquiryOption(InquiryOption o) throws SQLException {
        String update = "UPDATE INQUIRY_OPTION SET INQUIRY_ID = ?, DESCRIPTION = ? WHERE (ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, o.getInquiryId());
            ps.setString(2, o.getDescription());
            ps.setInt(3, o.getId());
            ps.execute();
        }
    }
    
    public void deleteInquiryOption(InquiryOption o) throws SQLException {
        String update = "DELETE FROM INQUIRY_OPTION WHERE (ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, o.getId());
            ps.execute();
        }
    }
    
    public void insertInquiryVote(int inquiryOptionId, int userId) throws SQLException {
        String insert = "INSERT INTO INQUIRY_VOTING_LOG (INQUIRY_OPTION_ID, USER_ID, VOTING_TIME) VALUES (?,?,?)";
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, inquiryOptionId);
            ps.setInt(2, userId);
            ps.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
            ps.execute();
        }
    }
}
