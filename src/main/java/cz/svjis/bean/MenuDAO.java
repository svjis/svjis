/*
 *       MenuDAO.java
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author berk
 */
public class MenuDAO extends DAO {
    
    public MenuDAO (Connection cnn) {
        super(cnn);
    }
    
    public Menu getMenu(int companyId) throws SQLException {
        return new Menu(getMenuNodeList(companyId));
    }
    
    public List<MenuNode> getMenuNodeList(int companyId) throws SQLException {
        ArrayList<MenuNode> result = new ArrayList<>();
        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.PARENT_ID, "
                + "a.DESCRIPTION, "
                + "a.HIDE "
                + "FROM MENU_TREE a "
                + "WHERE (a.COMPANY_ID = ?) "
                + "order by a.DESCRIPTION collate UNICODE_CI_AI";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MenuNode n = new MenuNode();
                    n.setId(rs.getInt("ID"));
                    n.setDescription(rs.getString("DESCRIPTION"));
                    n.setParentId(rs.getInt("PARENT_ID"));
                    n.setHide(rs.getBoolean("HIDE"));
                    result.add(n);
                }
            }
        }
        return result;
    }
    
    public MenuNode getMenuNode(int id, int companyId) throws SQLException {
        MenuNode result = new MenuNode();
        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.PARENT_ID, "
                + "a.DESCRIPTION, "
                + "a.HIDE, "
                + "(SELECT count(*) FROM MENU_TREE m WHERE m.PARENT_ID = a.ID)  AS CNT "
                + "FROM MENU_TREE a "
                + "WHERE (a.ID = ?) and (a.COMPANY_ID = ?)";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, id);
            ps.setInt(2, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.setId(rs.getInt("ID"));
                    result.setDescription(rs.getString("DESCRIPTION"));
                    result.setParentId(rs.getInt("PARENT_ID"));
                    result.setHide(rs.getBoolean("HIDE"));
                    result.setNumOfChilds(rs.getInt("CNT"));
                }
            }
        }
        return result;
    }
    
    public int insertMenuNode(MenuNode node, int companyId) throws SQLException {
        int result = 0;
        String insert = "INSERT INTO MENU_TREE (COMPANY_ID, PARENT_ID, DESCRIPTION, HIDE) VALUES (?,?,?,?) RETURNING ID";
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, companyId);
            if (node.getParentId() == 0) {
                ps.setNull(2, java.sql.Types.NULL);
            } else {
                ps.setInt(2, node.getParentId());
            }
            ps.setString(3, node.getDescription());
            ps.setInt(4, (node.isHide()) ? 1 : 0);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("ID");
                }
            }
        }
        return result;
    }
    
    public void updateMenuNode(MenuNode node, int companyId) throws SQLException {
        String update = "UPDATE MENU_TREE SET "
                + "PARENT_ID = ?, "
                + "DESCRIPTION = ?, "
                + "HIDE = ? "
                + "WHERE (ID = ?) AND (COMPANY_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            if (node.getParentId() == 0) {
                ps.setNull(1, java.sql.Types.NULL);
            } else {
                ps.setInt(1, node.getParentId());
            }
            ps.setString(2, node.getDescription());
            ps.setInt(3, (node.isHide()) ? 1 : 0);
            ps.setInt(4, node.getId());
            ps.setInt(5, companyId);
            ps.execute();
        }
    }
    
    public void deleteMenuNode(MenuNode node, int companyId) throws SQLException {
        String update = "DELETE FROM MENU_TREE WHERE (ID = ?) AND (COMPANY_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, node.getId());
            ps.setInt(2, companyId);
            ps.execute();
        }
    }
}
