/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author berk
 */
public class RoleDAO extends DAO {
    
    public RoleDAO (Connection cnn) {
        super(cnn);
    }
    
    public Role getRole(int companyId, int roleId) throws SQLException  {
        Role result = null;
        String select = "SELECT \n" +
                        "    a.ID, \n" +
                        "    a.COMPANY_ID, \n" +
                        "    a.DESCRIPTION, \n" +
                        "    (SELECT count(*) FROM USER_HAS_ROLE h WHERE (h.ROLE_ID = a.ID)) AS USERS \n" +
                        "FROM \"ROLE\" a WHERE (a.COMPANY_ID = ?) AND (a.ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            ps.setInt(2, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new Role();
                    result.setId(rs.getInt("ID"));
                    result.setCompanyId(rs.getInt("COMPANY_ID"));
                    result.setDescription(rs.getString("DESCRIPTION"));
                    result.setNumOfUsers(rs.getInt("USERS"));
                }
            }
        }
        if (result != null) {
            result.setPermissions(getRolePermissions(roleId));
        }
        return result;
    }
    
    private HashMap getRolePermissions(int roleId) throws SQLException  {
        HashMap result = new HashMap();
        String select  = "SELECT a.PERMISSION_ID, p.DESCRIPTION FROM ROLE_HAS_PERMISSION a LEFT JOIN PERMISSION p ON (p.ID = a.PERMISSION_ID) WHERE (a.ROLE_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getInt("PERMISSION_ID"), rs.getString("DESCRIPTION"));
                }
            }
        }
        return result;
    }
    
    public ArrayList<Role> getRoleList(int companyId) throws SQLException {
        ArrayList<Role> result = new ArrayList<>();
        String select  = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.DESCRIPTION, "
                + "(SELECT count(*) FROM USER_HAS_ROLE h LEFT JOIN \"USER\" u ON u.ID = h.USER_ID WHERE (h.ROLE_ID = a.ID) AND (u.ENABLED = 1)) AS USERS "
                + "FROM \"ROLE\" a WHERE (a.COMPANY_ID = ?) ORDER BY a.DESCRIPTION collate UNICODE_CI_AI";
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Role r = new Role();
                    r.setId(rs.getInt("ID"));
                    r.setCompanyId(rs.getInt("COMPANY_ID"));
                    r.setDescription(rs.getString("DESCRIPTION"));
                    r.setNumOfUsers(rs.getInt("USERS"));
                    result.add(r);
                }
            }
        }
        return result;
    }
    
    public ArrayList<Permission> getPermissionList() throws SQLException {
        ArrayList<Permission> result = new ArrayList<>();
        String select  = "SELECT a.ID, a.DESCRIPTION FROM PERMISSION a ORDER BY a.ID";
        try (Statement st = cnn.createStatement(); ResultSet rs = st.executeQuery(select)) {
            while (rs.next()) {
                Permission p = new Permission();
                p.setId(rs.getInt("ID"));
                p.setDescription(rs.getString("DESCRIPTION"));
                result.add(p);
            }
        }
        return result;
    }
    
    public void modifyRole(Role role) throws SQLException {
        int updated;
        String update = "UPDATE \"ROLE\" SET DESCRIPTION = ? WHERE (ID = ?) and (COMPANY_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setString(1, role.getDescription());
            ps.setInt(2, role.getId());
            ps.setInt(3, role.getCompanyId());
            updated = ps.executeUpdate();
        }
        if (updated == 1) {
            modifyRolePermissions(role);
        }
    }
    
    public int insertRole(Role role) throws SQLException {
        int result = 0;
        cnn.setAutoCommit(false);
        
        String update = "INSERT INTO \"ROLE\" (COMPANY_ID, DESCRIPTION) VALUES (?,?) returning ID";
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, role.getCompanyId());
            ps.setString(2, role.getDescription());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("ID");
                }
            }
        }
        
        role.setId(result);
        modifyRolePermissions(role);
        
        cnn.commit();
        cnn.setAutoCommit(true);
        
        return result;
    }
    
    private void modifyRolePermissions(Role role) throws SQLException {
        String delete = "DELETE FROM ROLE_HAS_PERMISSION WHERE (ROLE_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(delete)) {
            ps.setInt(1, role.getId());
            ps.execute();
        }

        String insert = "INSERT INTO ROLE_HAS_PERMISSION (ROLE_ID, PERMISSION_ID) VALUES (?, ?)";
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            Iterator<Integer> permsI = role.getPermissions().keySet().iterator();
            while (permsI.hasNext()) {
                int permId = permsI.next();
                ps.setInt(1, role.getId());
                ps.setInt(2, permId);
                ps.execute();
            }
        }
    }
    
    public void deleteRole(Role r) throws SQLException {
        String delete1 = "DELETE FROM ROLE_HAS_PERMISSION WHERE (ROLE_ID = ?)";
        String delete2 = "DELETE FROM ARTICLE_IS_VISIBLE_TO_ROLE a WHERE (a.ROLE_ID = ?);";
        String delete3 = "DELETE FROM \"ROLE\" WHERE (ID = ?) and (COMPANY_ID = ?)";

        try (PreparedStatement ps = cnn.prepareStatement(delete1)) {
            ps.setInt(1, r.getId());
            ps.execute();
        }

        try (PreparedStatement ps = cnn.prepareStatement(delete2)) {
            ps.setInt(1, r.getId());
            ps.execute();
        }

        try (PreparedStatement ps = cnn.prepareStatement(delete3)) {
            ps.setInt(1, r.getId());
            ps.setInt(2, r.getCompanyId());
            ps.execute();
        }
    }
}
