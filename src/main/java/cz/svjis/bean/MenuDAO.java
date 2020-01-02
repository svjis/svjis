/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author berk
 */
public class MenuDAO {
    
    private Connection cnn;
    
    public MenuDAO (Connection cnn) {
        this.cnn = cnn;
    }
    
    public Menu getMenu(int companyId) throws SQLException {
        Menu result = new Menu(getMenuNodeList(companyId));
        return result;
    }
    
    public ArrayList<MenuNode> getMenuNodeList(int companyId) throws SQLException {
        ArrayList<MenuNode> result = new ArrayList<MenuNode>();
        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.PARENT_ID, "
                + "a.DESCRIPTION "
                + "FROM MENU_TREE a "
                + "WHERE (a.COMPANY_ID = ?) "
                + "order by a.DESCRIPTION collate UNICODE_CI_AI";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            MenuNode n = new MenuNode();
            n.setId(rs.getInt("ID"));
            n.setDescription(rs.getString("DESCRIPTION"));
            n.setParentId(rs.getInt("PARENT_ID"));
            result.add(n);
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public MenuNode getMenuNode(int id, int companyId) throws SQLException {
        MenuNode result = new MenuNode();
        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.PARENT_ID, "
                + "a.DESCRIPTION, "
                + "(SELECT count(*) FROM MENU_TREE m WHERE m.PARENT_ID = a.ID)  AS CNT "
                + "FROM MENU_TREE a "
                + "WHERE (a.ID = ?) and (a.COMPANY_ID = ?)";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, id);
        ps.setInt(2, companyId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            result.setId(rs.getInt("ID"));
            result.setDescription(rs.getString("DESCRIPTION"));
            result.setParentId(rs.getInt("PARENT_ID"));
            result.setNumOfChilds(rs.getInt("CNT"));
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public int insertMenuNode(MenuNode node, int companyId) throws SQLException {
        int result = 0;
        String insert = "INSERT INTO MENU_TREE (COMPANY_ID, PARENT_ID, DESCRIPTION) VALUES (?,?,?) RETURNING ID";
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, companyId);
        if (node.getParentId() == 0) {
            ps.setNull(2, java.sql.Types.NULL);
        } else {
            ps.setInt(2, node.getParentId());
        }
        ps.setString(3, node.getDescription());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getInt("ID");
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public void updateMenuNode(MenuNode node, int companyId) throws SQLException {
        String update = "UPDATE MENU_TREE SET "
                + "PARENT_ID = ?, "
                + "DESCRIPTION = ? "
                + "WHERE (ID = ?) AND (COMPANY_ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(update);
        if (node.getParentId() == 0) {
            ps.setNull(1, java.sql.Types.NULL);
        } else {
            ps.setInt(1, node.getParentId());
        }
        ps.setString(2, node.getDescription());
        ps.setInt(3, node.getId());
        ps.setInt(4, companyId);
        ps.execute();
        ps.close();
    }
    
    public void deleteMenuNode(MenuNode node, int companyId) throws SQLException {
        String update = "DELETE FROM MENU_TREE WHERE (ID = ?) AND (COMPANY_ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(update);
        ps.setInt(1, node.getId());
        ps.setInt(2, companyId);
        ps.execute();
        ps.close();
    }
}
