/*
 *       LogDAO.java
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
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author berk
 */
public class LogDAO extends DAO {

    public static final int ID_NULL = 0;
    public static final int OPERATION_TYPE_LOGIN = 1;
    public static final int OPERATION_TYPE_LOGOUT = 2;
    public static final int OPERATION_TYPE_READ = 3;
    public static final int OPERATION_TYPE_SEND_LOST_PASSWORD = 4;
    public static final int OPERATION_TYPE_SEND_ARTICLE_NOTIFICATION = 5;
    public static final int OPERATION_TYPE_CREATE_ARTICLE = 6;
    public static final int OPERATION_TYPE_MODIFY_ARTICLE = 7;
    public static final int OPERATION_TYPE_INSERT_ATTACHMENT = 8;
    public static final int OPERATION_TYPE_DELETE_ATTACHMENT = 9;
    public static final int OPERATION_TYPE_READ_FAULT = 10;
    public static final int OPERATION_TYPE_CREATE_FAULT = 11;
    public static final int OPERATION_TYPE_MODIFY_FAULT = 12;
    public static final int OPERATION_TYPE_CLOSE_FAULT= 13;
    public static final int OPERATION_TYPE_REOPEN_FAULT = 14;
    public static final int OPERATION_TYPE_INSERT_FAULT_ATTACHMENT = 15;
    public static final int OPERATION_TYPE_DELETE_FAULT_ATTACHMENT = 16;

    public LogDAO (Connection cnn) {
        super(cnn);
    }
    
    public void log(int userId, int operationId, int documentId, String remoteIp, String userAgent) throws SQLException {
        int articleId;
        int faultId;

        switch (operationId) {
            case OPERATION_TYPE_READ:
            case OPERATION_TYPE_SEND_ARTICLE_NOTIFICATION:
            case OPERATION_TYPE_CREATE_ARTICLE:
            case OPERATION_TYPE_MODIFY_ARTICLE:
            case OPERATION_TYPE_INSERT_ATTACHMENT:
            case OPERATION_TYPE_DELETE_ATTACHMENT:
                    articleId = documentId;
                    faultId = ID_NULL;
                    break;
            case OPERATION_TYPE_READ_FAULT:
            case OPERATION_TYPE_CREATE_FAULT:
            case OPERATION_TYPE_MODIFY_FAULT:
            case OPERATION_TYPE_CLOSE_FAULT:
            case OPERATION_TYPE_REOPEN_FAULT:
            case OPERATION_TYPE_INSERT_FAULT_ATTACHMENT:
            case OPERATION_TYPE_DELETE_FAULT_ATTACHMENT:
                    articleId = ID_NULL;
                    faultId = documentId;
                    break;
            default:
                    articleId = ID_NULL;
                    faultId = ID_NULL;
        }

        String insert = "INSERT INTO LOG (\"TIME\", USER_ID, OPERATION_ID, ARTICLE_ID, FAULT_ID, REMOTE_IP, USER_AGENT) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
            ps.setInt(2, userId);
            ps.setInt(3, operationId);
            if (articleId != ID_NULL) {
                ps.setInt(4, articleId);
            } else {
                ps.setNull(4, java.sql.Types.NULL);
            }
            if (faultId != ID_NULL) {
                ps.setInt(5, faultId);
            } else {
                ps.setNull(5, java.sql.Types.NULL);
            }
            ps.setString(6, trim(remoteIp, 16));
            ps.setString(7, trim(userAgent, 150));
            ps.execute();
        }
    }
    
    private String trim(String s, int maxLength) {
        if (s.length() > maxLength) {
            s = s.substring(0, maxLength - 1);
        }
        return s;
    }
}
