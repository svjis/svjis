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
    
    public LogDAO (Connection cnn) {
        this.cnn = cnn;
    }
    
    public void log(int userId, int operationId, int articleId) throws SQLException {
        String insert = "INSERT INTO LOG (\"TIME\", USER_ID, OPERATION_ID, ARTICLE_ID) VALUES (?,?,?,?)";
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
        ps.setInt(2, userId);
        ps.setInt(3, operationId);
        if (articleId != 0) {
            ps.setInt(4, articleId);
        } else {
            ps.setNull(4, java.sql.Types.NULL);
        }
        ps.execute();
        ps.close();
    }
}
