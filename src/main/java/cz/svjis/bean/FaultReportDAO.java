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
    
    public int getNumOfFaults(int companyId, int closed) throws SQLException {
        return getFaultListSize(companyId, closed, "WHERE a.COMPANY_ID = ? AND a.CLOSED = ? ");
    }
    
    public ArrayList<FaultReport> getFaultList(int companyId, int pageNo, int pageSize, int closed) throws SQLException {
        return getFaultList(companyId, pageNo, pageSize, closed, "WHERE a.COMPANY_ID = ? AND a.CLOSED = ? ");
    }
    
    public int getNumOfFaultsByCreator(int companyId, int userId) throws SQLException {
        return getFaultListSize(companyId, userId, "WHERE a.COMPANY_ID = ? AND a.CREATED_BY_USER_ID = ? ");
    }
    
    public ArrayList<FaultReport> getFaultListByCreator(int companyId, int pageNo, int pageSize, int userId) throws SQLException {
        return getFaultList(companyId, pageNo, pageSize, userId, "WHERE a.COMPANY_ID = ? AND a.CREATED_BY_USER_ID = ? ");
    }
    
    public int getNumOfFaultsByResolver(int companyId, int userId) throws SQLException {
        return getFaultListSize(companyId, userId, "WHERE a.COMPANY_ID = ? AND a.ASSIGNED_TO_USER_ID = ? ");
    }
    
    public ArrayList<FaultReport> getFaultListByResolver(int companyId, int pageNo, int pageSize, int userId) throws SQLException {
        return getFaultList(companyId, pageNo, pageSize, userId, "WHERE a.COMPANY_ID = ? AND a.ASSIGNED_TO_USER_ID = ? ");
    }
    
    private int getFaultListSize(int companyId, int value, String where) throws SQLException {
        int result = 0;
        
        String select = "SELECT \n" +
                        "    count(*) AS CNT \n" +
                        "FROM FAULT_REPORT a \n" +
                        "LEFT JOIN \"USER\" cr on cr.ID = a.CREATED_BY_USER_ID \n" +
                        "LEFT JOIN \"USER\" ass on ass.ID = a.ASSIGNED_TO_USER_ID \n" +
                        where +
                        ";";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ps.setInt(2, value);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getInt("CNT");
        }
        rs.close();
        ps.close();
        
        return result;
    }
    
    private ArrayList<FaultReport> getFaultList(int companyId, int pageNo, int pageSize, int value, String where) throws SQLException {

        String select = "SELECT FIRST " + (pageNo * pageSize) + "\n" +
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
        ArrayList<FaultReport> result = getFaultReportListFromResultSet(rs, pageNo, pageSize);
        rs.close();
        ps.close();
        
        return result;
    }
    
    public int getFaultListSizeFromSearch(int companyId, String search) throws SQLException {
        int result = 0;
        
        String select = "SELECT \n" +
                        "    count(*) AS CNT \n" +
                        "FROM FAULT_REPORT a \n" +
                        "LEFT JOIN \"USER\" cr on cr.ID = a.CREATED_BY_USER_ID \n" +
                        "LEFT JOIN \"USER\" ass on ass.ID = a.ASSIGNED_TO_USER_ID \n" +
                        "WHERE a.COMPANY_ID = ? AND (\n" +
                            "(UPPER(a.SUBJECT) like UPPER('%" + search + "%')) OR \n" +
                            "(UPPER(a.DESCRIPTION) like UPPER('%" + search + "%')) \n" +
                        ");";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getInt("CNT");
        }
        rs.close();
        ps.close();
        
        return result;        
    }
    
    public ArrayList<FaultReport> getFaultListFromSearch(int companyId, int pageNo, int pageSize, String search) throws SQLException {
        
        String select = "SELECT FIRST " + (pageNo * pageSize) + "\n" +
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
                        "WHERE a.COMPANY_ID = ? AND (\n" +
                            "(UPPER(a.SUBJECT) like UPPER('%" + search + "%')) OR \n" +
                            "(UPPER(a.DESCRIPTION) like UPPER('%" + search + "%')) \n" +
                        ") \n" +
                        "ORDER BY a.CREATION_DATE desc;";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ResultSet rs = ps.executeQuery();
        ArrayList<FaultReport> result = getFaultReportListFromResultSet(rs, pageNo, pageSize);
        rs.close();
        ps.close();
        
        return result;
    }
    
    private ArrayList<FaultReport> getFaultReportListFromResultSet(ResultSet rs, int pageNo, int pageSize) throws SQLException {
        ArrayList<FaultReport> result = new ArrayList<FaultReport>();
        
        int cPageNo = 1;
        int cArtNo = 0;
        
        while (rs.next()) {
            if (cPageNo == pageNo) {
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
            
            cArtNo++;
            if (cArtNo == pageSize) {
                cPageNo++;
                cArtNo = 0;
            }
        }
        
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
                + "b.COMPANY_ID, "
                + "b.FIRST_NAME, "
                + "b.LAST_NAME, "
                + "a.UPLOAD_TIME, "
                + "a.CONTENT_TYPE, "
                + "a.FILENAME "
                //+ "a.DATA "
                + "FROM FAULT_REPORT_ATTACHMENT a "
                + "LEFT JOIN \"USER\" b on b.ID = a.USER_ID "
                + "WHERE (a.FAULT_REPORT_ID = ?) "
                + "ORDER BY a.ID";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, reportId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            FaultReportAttachment a = new FaultReportAttachment();
            a.setId(rs.getInt("ID"));
            a.setFaultReportId(rs.getInt("FAULT_REPORT_ID"));
            User u = new User();
            u.setId(rs.getInt("USER_ID"));
            u.setCompanyId(rs.getInt("COMPANY_ID"));
            u.setFirstName(rs.getString("FIRST_NAME"));
            u.setLastName(rs.getString("LAST_NAME"));
            a.setUser(u);
            a.setUploadTime(rs.getTimestamp("UPLOAD_TIME"));
            a.setContentType(rs.getString("CONTENT_TYPE"));
            a.setFileName(rs.getString("FILENAME"));
            result.add(a);
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public void insertFaultReportAttachment(FaultReportAttachment fa) throws SQLException {
        String insert = "INSERT INTO FAULT_REPORT_ATTACHMENT (FAULT_REPORT_ID, USER_ID, UPLOAD_TIME, CONTENT_TYPE, FILENAME, DATA) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, fa.getFaultReportId());
        ps.setInt(2, fa.getUser().getId());
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
                + "b.COMPANY_ID, "
                + "b.FIRST_NAME, "
                + "b.LAST_NAME, "
                + "a.UPLOAD_TIME, "
                + "a.CONTENT_TYPE, "
                + "a.FILENAME, "
                + "a.DATA "
                + "FROM FAULT_REPORT_ATTACHMENT a "
                + "LEFT JOIN \"USER\" b on b.ID = a.USER_ID "
                + "WHERE (a.ID = ?) ";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = new FaultReportAttachment();
            result.setId(rs.getInt("ID"));
            result.setFaultReportId(rs.getInt("FAULT_REPORT_ID"));
            User u = new User();
            u.setId(rs.getInt("USER_ID"));
            u.setCompanyId(rs.getInt("COMPANY_ID"));
            u.setFirstName(rs.getString("FIRST_NAME"));
            u.setLastName(rs.getString("LAST_NAME"));
            result.setUser(u);
            result.setUploadTime(rs.getTimestamp("UPLOAD_TIME"));
            result.setContentType(rs.getString("CONTENT_TYPE"));
            result.setFileName(rs.getString("FILENAME"));
            java.sql.Blob blob = null;
            blob = rs.getBlob("DATA");
            result.setData(blob.getBytes(1, (int) blob.length()));
        }
        rs.close();
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
        rs.close();
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
                        "LEFT JOIN FAULT_REPORT_WATCHING b ON b.USER_ID = a.ID \n" +
                        "WHERE (a.ENABLED = 1) AND (a.E_MAIL <> '') AND (b.FAULT_REPORT_ID = ?)";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, faultReportId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User u = new User();
            u.setId(rs.getInt("ID"));
            u.setFirstName(rs.getString("FIRST_NAME"));
            u.setLastName(rs.getString("LAST_NAME"));
            u.seteMail(rs.getString("E_MAIL"));
            result.add(u);
        }
        rs.close();
        ps.close();
        return result;
    }

    public boolean isUserWatchingFaultReport(int faultReportId, int userId) throws SQLException {
        String select = "SELECT count(*) as CNT FROM FAULT_REPORT_WATCHING a WHERE a.FAULT_REPORT_ID = ? AND a.USER_ID = ?";

        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, faultReportId);
        ps.setInt(2, userId);
        ResultSet rs = ps.executeQuery();
        int cnt = 0;
        if (rs.next()) {
            cnt = rs.getInt("CNT");
        }
        rs.close();
        ps.close();
        return (cnt != 0);
    }

    public void setUserWatchingFaultReport(int faultReportId, int userId) throws SQLException {
        String insert = "INSERT INTO FAULT_REPORT_WATCHING (FAULT_REPORT_ID, USER_ID) VALUES (?, ?)";

        if (!isUserWatchingFaultReport(faultReportId, userId)) {
            PreparedStatement ps = cnn.prepareStatement(insert);
            ps.setInt(1, faultReportId);
            ps.setInt(2, userId);
            ps.execute();
            ps.close();
        }
    }

    public void unsetUserWatchingFaultReport(int faultReportId, int userId) throws SQLException {
        String delete = "DELETE FROM FAULT_REPORT_WATCHING a WHERE a.FAULT_REPORT_ID = ? AND a.USER_ID = ?";
        PreparedStatement ps = cnn.prepareStatement(delete);
        ps.setInt(1, faultReportId);
        ps.setInt(2, userId);
        ps.execute();
        ps.close();
    }
}
