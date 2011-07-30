/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author berk
 */
public class ApplicationSetupDAO {
    private Connection cnn;
    
    public ApplicationSetupDAO (Connection cnn) {
        this.cnn = cnn;
    }
    
    public Properties getApplicationSetup(int companyId) throws SQLException {
        Properties result = new Properties();
        String select = "SELECT a.ID, a.\"VALUE\" FROM APPLICATION_SETUP a WHERE a.COMPANY_ID = ? ";
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            result.setProperty(rs.getString("ID"), rs.getString("VALUE"));
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public void deleteProperty(int companyId, String key) throws SQLException {
        String delete = "DELETE FROM APPLICATION_SETUP WHERE (COMPANY_ID = ?) AND (ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(delete);
        ps.setInt(1, companyId);
        ps.setString(2, key);
        ps.execute();
        ps.close();
    }
    
    public void insertProperty(int companyId, String key, String value) throws SQLException {
        String delete = "INSERT INTO APPLICATION_SETUP (COMPANY_ID, ID, \"VALUE\") VALUES (?,?,?)";
        PreparedStatement ps = cnn.prepareStatement(delete);
        ps.setInt(1, companyId);
        ps.setString(2, key);
        ps.setString(3, value);
        ps.execute();
        ps.close();
    }
}
