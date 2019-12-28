/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author jarberan
 */
public class FaultReportDAO {
    
    private Connection cnn;
    
    public FaultReportDAO (Connection cnn) {
        this.cnn = cnn;
    }
    
    public ArrayList<FaultReport> getFaultList(int companyId, int closed) throws SQLException {
        return getFaultList(companyId, closed, "WHERE a.COMPANY_ID = ? AND a.CLOSED = ? ");
    }
    
    public ArrayList<FaultReport> getFaultListByCreator(int companyId, int userId) throws SQLException {
        return getFaultList(companyId, userId, "WHERE a.COMPANY_ID = ? AND a.CREATED_BY_USER_ID = ? ");
    }
    
    public ArrayList<FaultReport> getFaultListByResolver(int companyId, int userId) throws SQLException {
        return getFaultList(companyId, userId, "WHERE a.COMPANY_ID = ? AND a.ASSIGNED_TO_USER_ID = ? ");
    }
    
    private ArrayList<FaultReport> getFaultList(int companyId, int value, String where) throws SQLException {
        
        ArrayList<FaultReport> result = new ArrayList<FaultReport>();
        
        String select = "SELECT \n" +
                        "    a.ID, \n" +
                        "    a.COMPANY_ID, \n" +
                        "    a.SUBJECT, \n" +
                        "    a.DESCRIPTION, \n" +
                        "    a.CREATION_DATE, \n" +
                        "    a.CREATED_BY_USER_ID, \n" +
                        "    cr.FIRST_NAME as CR_FIRST_NAME, \n" +
                        "    cr.LAST_NAME as CR_LAST_NAME, \n" +
                        "    a.ASSIGNED_TO_USER_ID, \n" +
                        "    ass.FIRST_NAME as AS_FIRST_NAME, \n" +
                        "    ass.LAST_NAME as AS_LAST_NAME, \n" +
                        "    a.CLOSED\n" +
                        "FROM FAULT_REPORT a \n" +
                        "LEFT JOIN \"USER\" cr on cr.ID = a.CREATED_BY_USER_ID \n" +
                        "LEFT JOIN \"USER\" ass on ass.ID = a.ASSIGNED_TO_USER_ID \n" +
                        where +
                        "ORDER BY a.CREATION_DATE desc;";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ps.setInt(2, value);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            FaultReport f = new FaultReport();
            f.setId(rs.getInt("ID"));
            f.setCompanyId(rs.getInt("COMPANY_ID"));
            f.setSubject(rs.getString("SUBJECT"));
            f.setCreationDate(new Date(rs.getTimestamp("CREATION_DATE").getTime()));
            if (rs.getInt("CREATED_BY_USER_ID") != 0) {
                User u = new User();
                u.setId(rs.getInt("CREATED_BY_USER_ID"));
                u.setFirstName(rs.getString("CR_FIRST_NAME"));
                u.setLastName(rs.getString("CR_LAST_NAME"));
                f.setCreatedByUser(u);
            }
            if (rs.getInt("ASSIGNED_TO_USER_ID") != 0) {
                User u = new User();
                u.setId(rs.getInt("ASSIGNED_TO_USER_ID"));
                u.setFirstName(rs.getString("AS_FIRST_NAME"));
                u.setLastName(rs.getString("AS_LAST_NAME"));
                f.setAssignedToUser(u);
            }
            f.setClosed(rs.getBoolean("CLOSED"));
            result.add(f);
        }
        rs.close();
        ps.close();
        
        return result;
    }
    
    public FaultReport getFault(int companyId, int id) throws SQLException {
        FaultReport result = null;
        
        String select = "SELECT \n" +
                        "    a.ID, \n" +
                        "    a.COMPANY_ID, \n" +
                        "    a.SUBJECT, \n" +
                        "    a.DESCRIPTION, \n" +
                        "    a.CREATION_DATE, \n" +
                        "    a.CREATED_BY_USER_ID, \n" +
                        "    cr.FIRST_NAME as CR_FIRST_NAME, \n" +
                        "    cr.LAST_NAME as CR_LAST_NAME, \n" +
                        "    a.ASSIGNED_TO_USER_ID, \n" +
                        "    ass.FIRST_NAME as AS_FIRST_NAME, \n" +
                        "    ass.LAST_NAME as AS_LAST_NAME, \n" +
                        "    a.CLOSED\n" +
                        "FROM FAULT_REPORT a \n" +
                        "LEFT JOIN \"USER\" cr on cr.ID = a.CREATED_BY_USER_ID \n" +
                        "LEFT JOIN \"USER\" ass on ass.ID = a.ASSIGNED_TO_USER_ID \n" +
                        "WHERE a.COMPANY_ID = ? AND a.ID = ? ;";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ps.setInt(2, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = new FaultReport();
            result.setId(rs.getInt("ID"));
            result.setCompanyId(rs.getInt("COMPANY_ID"));
            result.setSubject(rs.getString("SUBJECT"));
            result.setDescription(rs.getString("DESCRIPTION"));
            result.setCreationDate(new Date(rs.getTimestamp("CREATION_DATE").getTime()));
            if (rs.getInt("CREATED_BY_USER_ID") != 0) {
                User u = new User();
                u.setId(rs.getInt("CREATED_BY_USER_ID"));
                u.setFirstName(rs.getString("CR_FIRST_NAME"));
                u.setLastName(rs.getString("CR_LAST_NAME"));
                result.setCreatedByUser(u);
            }
            if (rs.getInt("ASSIGNED_TO_USER_ID") != 0) {
                User u = new User();
                u.setId(rs.getInt("ASSIGNED_TO_USER_ID"));
                u.setFirstName(rs.getString("AS_FIRST_NAME"));
                u.setLastName(rs.getString("AS_LAST_NAME"));
                result.setAssignedToUser(u);
            }
            result.setClosed(rs.getBoolean("CLOSED"));
        }
        rs.close();
        ps.close();
        
        if (result != null) {
            result.setAttachmentList(this.getFaultReportAttachmentList(result.getId()));
            result.setFaultReportCommentList(this.getFaultReportCommentList(result.getId()));
        }
        
        return result;
    }
    
    private ArrayList<FaultReportAttachment> getFaultReportAttachmentList(int reportId) throws SQLException {
        ArrayList<FaultReportAttachment> result = new ArrayList<FaultReportAttachment>();
        String select = "SELECT "
                + "a.ID, "
                + "a.FAULT_REPORT_ID, "
                + "a.USER_ID, "
                + "a.UPLOAD_TIME, "
                + "a.CONTENT_TYPE, "
                + "a.FILENAME "
                //+ "a.DATA "
                + "FROM FAULT_REPORT_ATTACHMENT a "
                + "WHERE (a.FAULT_REPORT_ID = ?) "
                + "ORDER BY a.ID";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, reportId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            FaultReportAttachment a = new FaultReportAttachment();
            a.setId(rs.getInt("ID"));
            a.setFaultReportId(rs.getInt("FAULT_REPORT_ID"));
            a.setUserId(rs.getInt("USER_ID"));
            a.setUploadTime(rs.getTimestamp("UPLOAD_TIME"));
            a.setContentType(rs.getString("CONTENT_TYPE"));
            a.setFileName(rs.getString("FILENAME"));
            result.add(a);
        }
        ps.close();
        return result;
    }
    
    public void insertFaultReportAttachment(FaultReportAttachment fa) throws SQLException {
        String insert = "INSERT INTO FAULT_REPORT_ATTACHMENT (FAULT_REPORT_ID, USER_ID, UPLOAD_TIME, CONTENT_TYPE, FILENAME, DATA) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, fa.getFaultReportId());
        ps.setInt(2, fa.getUserId());
        ps.setTimestamp(3, new java.sql.Timestamp(fa.getUploadTime().getTime()));
        ps.setString(4, fa.getContentType());
        ps.setString(5, fa.getFileName());
        ps.setBytes(6, fa.getData());
        ps.execute();
        ps.close();
    }
    
    public FaultReportAttachment getFaultReportAttachment(int id) throws SQLException {
        FaultReportAttachment result = null;
        String select = "SELECT "
                + "a.ID, "
                + "a.FAULT_REPORT_ID, "
                + "a.USER_ID, "
                + "a.UPLOAD_TIME, "
                + "a.CONTENT_TYPE, "
                + "a.FILENAME, "
                + "a.DATA "
                + "FROM FAULT_REPORT_ATTACHMENT a "
                + "WHERE (a.ID = ?) ";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = new FaultReportAttachment();
            result.setId(rs.getInt("ID"));
            result.setFaultReportId(rs.getInt("FAULT_REPORT_ID"));
            result.setUserId(rs.getInt("USER_ID"));
            result.setUploadTime(rs.getTimestamp("UPLOAD_TIME"));
            result.setContentType(rs.getString("CONTENT_TYPE"));
            result.setFileName(rs.getString("FILENAME"));
            java.sql.Blob blob = null;
            blob = rs.getBlob("DATA");
            result.setData(blob.getBytes(1, (int) blob.length()));
        }
        ps.close();
        return result;
    }
    
    public void deleteFaultAttachment(int id) throws SQLException {
        String delete = "DELETE FROM FAULT_REPORT_ATTACHMENT a WHERE (a.ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(delete);
        ps.setInt(1, id);
        ps.execute();
        ps.close();
    }
    
    public int insertFault(FaultReport f) throws SQLException {
        int result = 0;
        
        String insert = "INSERT INTO FAULT_REPORT ("
                        + "COMPANY_ID, "
                        + "SUBJECT, "
                        + "DESCRIPTION, "
                        + "CREATION_DATE, "
                        + "CREATED_BY_USER_ID, "
                        + "ASSIGNED_TO_USER_ID, "
                        + "CLOSED) " +
                        " VALUES (?,?,?,?,?,?,?) returning ID;";
        
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, f.getCompanyId());
        ps.setString(2, f.getSubject());
        ps.setString(3, f.getDescription());
        ps.setTimestamp(4, new java.sql.Timestamp(f.getCreationDate().getTime()));
        ps.setInt(5, f.getCreatedByUser().getId());
        if (f.getAssignedToUser() != null) {
            ps.setInt(6, f.getAssignedToUser().getId());
        } else {
            ps.setNull(6, java.sql.Types.INTEGER);
        }
        ps.setBoolean(7, f.isClosed());
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getInt("ID");
        }
        rs.close();
        ps.close();
        
        return result;
    }
    
    public void modifyFault(FaultReport f) throws SQLException {
        String update = "UPDATE FAULT_REPORT a \n" +
                        "SET a.SUBJECT = ?, a.DESCRIPTION = ?, a.ASSIGNED_TO_USER_ID = ?, a.CLOSED = ?\n" +
                        "WHERE a.COMPANY_ID = ? AND a.ID = ? ;";
        
        PreparedStatement ps = cnn.prepareStatement(update);
        ps.setString(1, f.getSubject());
        ps.setString(2, f.getDescription());
        if (f.getAssignedToUser() != null) {
            ps.setInt(3, f.getAssignedToUser().getId());
        } else {
            ps.setNull(3, java.sql.Types.INTEGER);
        }
        ps.setBoolean(4, f.isClosed());
        ps.setInt(5, f.getCompanyId());
        ps.setInt(6, f.getId());

        ps.execute();
        ps.close();
    }
    
    public FaultReportMenuCounters getMenuCounters(int companyId, int userId) throws SQLException {
        FaultReportMenuCounters result = new FaultReportMenuCounters();
        String select = "SELECT\n" +
                        "    (SELECT count (*) FROM FAULT_REPORT a WHERE a.COMPANY_ID = c.ID AND a.CLOSED = 0) AS CNT_ALL,\n" +
                        "    (SELECT count (*) FROM FAULT_REPORT a WHERE a.COMPANY_ID = c.ID AND a.CREATED_BY_USER_ID = ? AND a.CLOSED = 0) AS CNT_CRT,\n" +
                        "    (SELECT count (*) FROM FAULT_REPORT a WHERE a.COMPANY_ID = c.ID AND a.ASSIGNED_TO_USER_ID = ? AND a.CLOSED = 0) AS CNT_ASG,\n" +
                        "    (SELECT count (*) FROM FAULT_REPORT a WHERE a.COMPANY_ID = c.ID AND a.CLOSED = 1) AS CNT_CLOSED\n" +
                        "FROM COMPANY c WHERE c.ID = ?;";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, userId);
        ps.setInt(2, userId);
        ps.setInt(3, companyId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result.setAllOpenCnt(rs.getInt("CNT_ALL"));
            result.setAllCreatedByMeCnt(rs.getInt("CNT_CRT"));
            result.setAllAssignedToMeCnt(rs.getInt("CNT_ASG"));
            result.setAllClosedCnt(rs.getInt("CNT_CLOSED"));
        }
        rs.close();
        ps.close();
        
        return result;
    }
    
    private ArrayList<FaultReportComment> getFaultReportCommentList(int faultReportId) throws SQLException {
        ArrayList<FaultReportComment> result = new ArrayList<FaultReportComment>();
        String select = "SELECT "
                + "a.ID, "
                + "a.FAULT_REPORT_ID, "
                + "a.USER_ID, "
                + "u.FIRST_NAME, "
                + "u.LAST_NAME, "
                + "a.INSERTION_TIME, "
                + "a.BODY "
                + "FROM FAULT_REPORT_COMMENT a "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.USER_ID) "
                + "WHERE a.FAULT_REPORT_ID = ? "
                + "ORDER BY a.ID";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, faultReportId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            FaultReportComment a = new FaultReportComment();
            a.setId(rs.getInt("ID"));
            a.setFaultReportId(rs.getInt("FAULT_REPORT_ID"));
            a.setUser(new User());
            a.getUser().setId(rs.getInt("USER_ID"));
            a.getUser().setFirstName(rs.getString("FIRST_NAME"));
            a.getUser().setLastName(rs.getString("LAST_NAME"));
            a.setInsertionTime(rs.getTimestamp("INSERTION_TIME"));
            a.setBody(rs.getString("BODY"));
            result.add(a);
        }
        ps.close();
        return result;
    }
    
    public void insertFaultReportComment(FaultReportComment c) throws SQLException {
        String insert = "INSERT INTO FAULT_REPORT_COMMENT (FAULT_REPORT_ID, USER_ID, INSERTION_TIME, BODY) VALUES (?,?,?,?)";
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, c.getFaultReportId());
        ps.setInt(2, c.getUser().getId());
        ps.setTimestamp(3, new java.sql.Timestamp(c.getInsertionTime().getTime()));
        ps.setString(4, c.getBody());
        ps.execute();
        ps.close();
    }
    
    public ArrayList<User> getUserListForNotificationAboutNewComment(int faultReportId) throws SQLException {
        ArrayList<User> result = new ArrayList<User>();
        String select = "SELECT \n" +
                        "    a.ID, \n" +
                        "    a.LAST_NAME, \n" +
                        "    a.FIRST_NAME, \n" +
                        "    a.E_MAIL \n" +
                        "FROM \"USER\" a \n" +
                        "WHERE (a.ENABLED = 1) AND (a.E_MAIL <> '') AND  \n" +
                        "    a.ID in ( \n" +
                        "        SELECT a.USER_ID \n" +
                        "        FROM FAULT_REPORT_COMMENT a \n" +
                        "        WHERE a.FAULT_REPORT_ID = ? \n" +
                        "    UNION \n" +
                        "        SELECT a.CREATED_BY_USER_ID AS USER_ID \n" +
                        "        FROM FAULT_REPORT a \n" +
                        "        WHERE a.ID = ? \n" +
                        "    UNION\n" +
                        "        SELECT a.ASSIGNED_TO_USER_ID AS USER_ID \n" +
                        "        FROM FAULT_REPORT a \n" +
                        "        WHERE a.ID = ? \n" +
                        "    GROUP BY USER_ID \n" +
                        "    )";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, faultReportId);
        ps.setInt(2, faultReportId);
        ps.setInt(3, faultReportId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User u = new User();
            u.setId(rs.getInt("ID"));
            u.setFirstName(rs.getString("FIRST_NAME"));
            u.setLastName(rs.getString("LAST_NAME"));
            u.seteMail(rs.getString("E_MAIL"));
            result.add(u);
        }
        ps.close();
        return result;
    }
}
