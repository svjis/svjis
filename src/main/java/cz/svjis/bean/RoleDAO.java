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
public class RoleDAO {
    private Connection cnn;
    
    public RoleDAO (Connection cnn) {
        this.cnn = cnn;
    }
    
    public Role getRole(int companyId, int roleId) throws SQLException  {
        Role result = null;
        String select = "SELECT \n" +
                        "    a.ID, \n" +
                        "    a.COMPANY_ID, \n" +
                        "    a.DESCRIPTION, \n" +
                        "    (SELECT count(*) FROM USER_HAS_ROLE h WHERE (h.ROLE_ID = a.ID)) AS USERS \n" +
                        "FROM \"ROLE\" a WHERE (a.COMPANY_ID = ?) AND (a.ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ps.setInt(2, roleId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = new Role();
            result.setId(rs.getInt("ID"));
            result.setCompanyId(rs.getInt("COMPANY_ID"));
            result.setDescription(rs.getString("DESCRIPTION"));
            result.setNumOfUsers(rs.getInt("USERS"));
        }
        rs.close();
        ps.close();
        if (result != null) {
            result.setPermissions(getRolePermissions(roleId));
        }
        return result;
    }
    
    private HashMap getRolePermissions(int roleId) throws SQLException  {
        HashMap result = new HashMap();
        String select  = "SELECT a.PERMISSION_ID, p.DESCRIPTION FROM ROLE_HAS_PERMISSION a LEFT JOIN PERMISSION p ON (p.ID = a.PERMISSION_ID) WHERE (a.ROLE_ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, roleId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            result.put(new Integer(rs.getInt("PERMISSION_ID")), rs.getString("DESCRIPTION"));
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public ArrayList<Role> getRoleList(int companyId) throws SQLException {
        ArrayList<Role> result = new ArrayList<Role>();
        String select  = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.DESCRIPTION, "
                + "(SELECT count(*) FROM USER_HAS_ROLE h WHERE (h.ROLE_ID = a.ID)) AS USERS "
                + "FROM \"ROLE\" a WHERE (a.COMPANY_ID = ?) ORDER BY a.DESCRIPTION collate UNICODE_CI_AI";
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Role r = new Role();
            r.setId(rs.getInt("ID"));
            r.setCompanyId(rs.getInt("COMPANY_ID"));
            r.setDescription(rs.getString("DESCRIPTION"));
            r.setNumOfUsers(rs.getInt("USERS"));
            result.add(r);
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public ArrayList<Permission> getPermissionList() throws SQLException {
        ArrayList<Permission> result = new ArrayList<Permission>();
        String select  = "SELECT a.ID, a.DESCRIPTION FROM PERMISSION a ORDER BY a.ID";
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(select);
        while (rs.next()) {
            Permission p = new Permission();
            p.setId(rs.getInt("ID"));
            p.setDescription(rs.getString("DESCRIPTION"));
            result.add(p);
        }
        rs.close();
        st.close();
        return result;
    }
    
    public void modifyRole(Role role) throws SQLException {
        int updated = 0;
        String update = "UPDATE \"ROLE\" SET DESCRIPTION = ? WHERE (ID = ?) and (COMPANY_ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(update);
        ps.setString(1, role.getDescription());
        ps.setInt(2, role.getId());
        ps.setInt(3, role.getCompanyId());
        updated = ps.executeUpdate();
        ps.close();
        if (updated == 1) {
            modifyRolePermissions(role);
        }
    }
    
    public int insertRole(Role role) throws SQLException {
        int result = 0;
        cnn.setAutoCommit(false);
        
        String update = "INSERT INTO \"ROLE\" (COMPANY_ID, DESCRIPTION) VALUES (?,?) returning ID";
        PreparedStatement ps = cnn.prepareStatement(update);
        ps.setInt(1, role.getCompanyId());
        ps.setString(2, role.getDescription());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getInt("ID");
        }
        ps.close();
        
        role.setId(result);
        modifyRolePermissions(role);
        
        cnn.commit();
        cnn.setAutoCommit(true);
        
        return result;
    }
    
    private void modifyRolePermissions(Role role) throws SQLException {
        String delete = "DELETE FROM ROLE_HAS_PERMISSION WHERE (ROLE_ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(delete);
        ps.setInt(1, role.getId());
        ps.execute();
        ps.close();

        String insert = "INSERT INTO ROLE_HAS_PERMISSION (ROLE_ID, PERMISSION_ID) VALUES (?, ?)";
        ps = cnn.prepareStatement(insert);
        Iterator<Integer> permsI = role.getPermissions().keySet().iterator();
        while (permsI.hasNext()) {
            int permId = permsI.next();
            ps.setInt(1, role.getId());
            ps.setInt(2, permId);
            ps.execute();
        }
        ps.close();
    }
    
    public void deleteRole(int companyId, int roleId) throws SQLException {
        String delete = "DELETE FROM \"ROLE\" WHERE (ID = ?) and (COMPANY_ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(delete);
        ps.setInt(1, roleId);
        ps.setInt(2, companyId);
        ps.execute();
        ps.close();
    }
}
