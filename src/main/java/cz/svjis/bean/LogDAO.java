/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class LogDAO {
    private Connection cnn;

    public static final int idNull = 0;
    public static final int operationTypeLogin = 1;
    public static final int operationTypeLogout = 2;
    public static final int operationTypeRead = 3;
    public static final int operationTypeSendLostPassword = 4;
    public static final int operationTypeSendArticleNotification = 5;
    public static final int operationTypeCreateArticle = 6;
    public static final int operationTypeModifyArticle = 7;
    public static final int operationTypeInsertAttachment = 8;
    public static final int operationTypeDeleteAttachment = 9;
    public static final int operationTypeReadFault = 10;
    public static final int operationTypeCreateFault = 11;
    public static final int operationTypeModifyFault = 12;
    public static final int operationTypeCloseFault = 13;
    public static final int operationTypeReopenFault = 14;
    public static final int operationTypeInsertFaultAttachment = 15;
    public static final int operationTypeDeleteFaultAttachment = 16;

    public LogDAO (Connection cnn) {
        this.cnn = cnn;
    }
    
    public void log(int userId, int operationId, int documentId, String remoteIp, String userAgent) throws SQLException {
        int articleId = idNull;
        int faultId = idNull;

        switch (operationId) {
            case operationTypeRead:
            case operationTypeSendArticleNotification:
            case operationTypeCreateArticle:
            case operationTypeModifyArticle:
            case operationTypeInsertAttachment:
            case operationTypeDeleteAttachment:
                    articleId = documentId;
                    break;
            case operationTypeReadFault:
            case operationTypeCreateFault:
            case operationTypeModifyFault:
            case operationTypeCloseFault:
            case operationTypeReopenFault:
            case operationTypeInsertFaultAttachment:
            case operationTypeDeleteFaultAttachment:
                    faultId = documentId;
                    break;
        }

        String insert = "INSERT INTO LOG (\"TIME\", USER_ID, OPERATION_ID, ARTICLE_ID, FAULT_ID, REMOTE_IP, USER_AGENT) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
        ps.setInt(2, userId);
        ps.setInt(3, operationId);
        if (articleId != idNull) {
            ps.setInt(4, articleId);
        } else {
            ps.setNull(4, java.sql.Types.NULL);
        }
        if (faultId != idNull) {
            ps.setInt(5, faultId);
        } else {
            ps.setNull(5, java.sql.Types.NULL);
        }
        ps.setString(6, trim(remoteIp, 16));
        ps.setString(7, trim(userAgent, 150));
        ps.execute();
        ps.close();
    }
    
    private String trim(String s, int maxLength) {
        if (s.length() > maxLength) {
            s = s.substring(0, maxLength - 1);
        }
        return s;
    }
}
