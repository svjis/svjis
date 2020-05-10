/*
 *       ApplicationSetupDAO.java
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
import java.util.Properties;

/**
 *
 * @author berk
 */
public class ApplicationSetupDAO extends DAO {
    
    public ApplicationSetupDAO (Connection cnn) {
        super(cnn);
    }
    
    public Properties getApplicationSetup(int companyId) throws SQLException {
        Properties result = new Properties();
        String select = "SELECT a.ID, a.\"VALUE\" FROM APPLICATION_SETUP a WHERE a.COMPANY_ID = ? ORDER BY a.ID";
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.setProperty(rs.getString("ID"), rs.getString("VALUE"));
                }
            }
        }
        return result;
    }
    
    public void deleteProperty(int companyId, String key) throws SQLException {
        String delete = "DELETE FROM APPLICATION_SETUP WHERE (COMPANY_ID = ?) AND (ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(delete)) {
            ps.setInt(1, companyId);
            ps.setString(2, key);
            ps.execute();
        }
    }
    
    public void insertProperty(int companyId, String key, String value) throws SQLException {
        String delete = "INSERT INTO APPLICATION_SETUP (COMPANY_ID, ID, \"VALUE\") VALUES (?,?,?)";
        try (PreparedStatement ps = cnn.prepareStatement(delete)) {
            ps.setInt(1, companyId);
            ps.setString(2, key);
            ps.setString(3, value);
            ps.execute();
        }
    }
}
