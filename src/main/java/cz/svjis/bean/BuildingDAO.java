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

/**
 *
 * @author berk
 */
public class BuildingDAO {
    
    private Connection cnn;
    
    public BuildingDAO (Connection cnn) {
        this.cnn = cnn;
    }
    
    
    public Building getBuilding(int companyId) throws SQLException {
        Building result = null;
        
        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.ADDRESS, "
                + "a.CITY, "
                + "a.POST_CODE, "
                + "a.REGISTRATION_ID "
                + "FROM BUILDING a "
                + "WHERE a.COMPANY_ID = ?";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = new Building();
            result.setId(rs.getInt("ID"));
            result.setCompanyId(rs.getInt("COMPANY_ID"));
            result.setAddress(rs.getString("ADDRESS"));
            result.setCity(rs.getString("CITY"));
            result.setPostCode(rs.getString("POST_CODE"));
            result.setRegistrationNo(rs.getString("REGISTRATION_ID"));
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public void insertBuilding(Building b) throws SQLException {
        String insert = "INSERT INTO BUILDING (COMPANY_ID, ADDRESS, CITY, POST_CODE, REGISTRATION_ID) VALUES (?,?,?,?,?)";
        
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, b.getCompanyId());
        ps.setString(2, b.getAddress());
        ps.setString(3, b.getCity());
        ps.setString(4, b.getPostCode());
        ps.setString(5, b.getRegistrationNo());
        ps.execute();
        ps.close();
    }
    
    public void modifyBuilding(Building b) throws SQLException {
        String update = "UPDATE BUILDING a SET "
                + "a.ADDRESS = ?, "
                + "a.CITY = ?, "
                + "a.POST_CODE = ?, "
                + "a.REGISTRATION_ID = ? "
                + "WHERE a.ID = ?";
        
        PreparedStatement ps = cnn.prepareStatement(update);
        ps.setString(1, b.getAddress());
        ps.setString(2, b.getCity());
        ps.setString(3, b.getPostCode());
        ps.setString(4, b.getRegistrationNo());
        ps.setInt(5, b.getId());
        ps.executeUpdate();
        ps.close();
    }
    
    
    public ArrayList<BuildingUnitType> getBuildingUnitTypeList() throws SQLException {
        ArrayList<BuildingUnitType> result = new ArrayList<BuildingUnitType>();
        String select = "SELECT a.ID, a.DESCRIPTION FROM BUILDING_UNIT_TYPE a ORDER BY a.ID";
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(select);
        while (rs.next()) {
            BuildingUnitType but = new BuildingUnitType();
            but.setId(rs.getInt("ID"));
            but.setDescription(rs.getString("DESCRIPTION"));
            result.add(but);
        }
        rs.close();
        st.close();
        return result;
    }
    
    public ArrayList<BuildingUnit> getBuildingUnitList(int buildingId, int typeId) throws SQLException {
        ArrayList<BuildingUnit> result = new ArrayList<BuildingUnit>();
        String select = "SELECT "
                + "a.ID, "
                + "a.BUILDING_ID, "
                + "a.BUILDING_UNIT_TYPE_ID, "
                + "b.DESCRIPTION AS BUILDING_UNIT_TYPE, "
                + "a.REGISTRATION_ID, "
                + "a.DESCRIPTION, "
                + "a.NUMERATOR, "
                + "a.DENOMINATOR "
                + "FROM BUILDING_UNIT a "
                + "LEFT JOIN BUILDING_UNIT_TYPE b ON (b.ID = a.BUILDING_UNIT_TYPE_ID) "
                + "WHERE (a.BUILDING_ID = ?) ";
        if (typeId != 0) {
                select += "AND (a.BUILDING_UNIT_TYPE_ID = ?) ";
        }
        select += "ORDER BY a.BUILDING_UNIT_TYPE_ID, a.ID";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, buildingId);
        if (typeId != 0) {
            ps.setInt(2, typeId);
        }
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            BuildingUnit bu = new BuildingUnit();
            bu.setId(rs.getInt("ID"));
            bu.setBuildingId(rs.getInt("BUILDING_ID"));
            bu.setBuildingUnitTypeId(rs.getInt("BUILDING_UNIT_TYPE_ID"));
            bu.setBuildingUnitType(rs.getString("BUILDING_UNIT_TYPE"));
            bu.setRegistrationId(rs.getString("REGISTRATION_ID"));
            bu.setDescription(rs.getString("DESCRIPTION"));
            bu.setNumerator(rs.getInt("NUMERATOR"));
            bu.setDenominator(rs.getInt("DENOMINATOR"));
            result.add(bu);
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public ArrayList<BuildingUnit> getUnconnectedBuildingUnitList(int companyId) throws SQLException {
        ArrayList<BuildingUnit> result = new ArrayList<BuildingUnit>();
        String select = "SELECT "
                + "u.ID, "
                + "u.BUILDING_ID, "
                + "u.BUILDING_UNIT_TYPE_ID, "
                + "t.DESCRIPTION AS BUILDING_UNIT_TYPE, "
                + "u.REGISTRATION_ID, "
                + "u.DESCRIPTION, "
                + "u.NUMERATOR, "
                + "u.DENOMINATOR "
                + "FROM BUILDING b "
                + "LEFT JOIN BUILDING_UNIT u ON (u.BUILDING_ID = b.ID) "
                + "LEFT JOIN BUILDING_UNIT_TYPE t ON (t.ID = u.BUILDING_UNIT_TYPE_ID) "
                + "LEFT JOIN USER_HAS_BUILDING_UNIT ub ON (ub.BUILDING_UNIT_ID = u.ID) "
                + "WHERE (b.COMPANY_ID = ?) and (ub.USER_ID is null) "
                + "ORDER BY u.BUILDING_UNIT_TYPE_ID, u.ID";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            BuildingUnit bu = new BuildingUnit();
            bu.setId(rs.getInt("ID"));
            bu.setBuildingId(rs.getInt("BUILDING_ID"));
            bu.setBuildingUnitTypeId(rs.getInt("BUILDING_UNIT_TYPE_ID"));
            bu.setBuildingUnitType(rs.getString("BUILDING_UNIT_TYPE"));
            bu.setRegistrationId(rs.getString("REGISTRATION_ID"));
            bu.setDescription(rs.getString("DESCRIPTION"));
            bu.setNumerator(rs.getInt("NUMERATOR"));
            bu.setDenominator(rs.getInt("DENOMINATOR"));
            result.add(bu);
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public ArrayList<BuildingUnit> getUserHasBuildingUnitList(int userId) throws SQLException {
        ArrayList<BuildingUnit> result = new ArrayList<BuildingUnit>();
        String select = "SELECT "
                + "u.ID, "
                + "u.BUILDING_ID, "
                + "u.BUILDING_UNIT_TYPE_ID, "
                + "t.DESCRIPTION AS BUILDING_UNIT_TYPE, "
                + "u.REGISTRATION_ID, "
                + "u.DESCRIPTION, "
                + "u.NUMERATOR, "
                + "u.DENOMINATOR "
                + "FROM USER_HAS_BUILDING_UNIT a "
                + "LEFT JOIN BUILDING_UNIT u ON (u.ID = a.BUILDING_UNIT_ID) "
                + "LEFT JOIN BUILDING_UNIT_TYPE t ON (t.ID = u.BUILDING_UNIT_TYPE_ID) "
                + "WHERE (a.USER_ID = ?)"
                + "ORDER BY u.BUILDING_UNIT_TYPE_ID, u.ID";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            BuildingUnit bu = new BuildingUnit();
            bu.setId(rs.getInt("ID"));
            bu.setBuildingId(rs.getInt("BUILDING_ID"));
            bu.setBuildingUnitTypeId(rs.getInt("BUILDING_UNIT_TYPE_ID"));
            bu.setBuildingUnitType(rs.getString("BUILDING_UNIT_TYPE"));
            bu.setRegistrationId(rs.getString("REGISTRATION_ID"));
            bu.setDescription(rs.getString("DESCRIPTION"));
            bu.setNumerator(rs.getInt("NUMERATOR"));
            bu.setDenominator(rs.getInt("DENOMINATOR"));
            result.add(bu);
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public ArrayList<User> getBuildingUnitHasUserList(int buildingUnitId) throws SQLException {
        ArrayList<User> result = new ArrayList<User>();
        String select = "SELECT "
                + "a.USER_ID, "
                + "u.SALUTATION, "
                + "u.FIRST_NAME, "
                + "u.LAST_NAME "
                + "FROM USER_HAS_BUILDING_UNIT a "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.USER_ID) "
                + "WHERE (a.BUILDING_UNIT_ID = ?) "
                + "ORDER BY u.LAST_NAME collate UNICODE_CI_AI";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, buildingUnitId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User u = new User();
            u.setId(rs.getInt("USER_ID"));
            u.setSalutation(rs.getString("SALUTATION"));
            u.setFirstName(rs.getString("FIRST_NAME"));
            u.setLastName(rs.getString("LAST_NAME"));
            result.add(u);
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public void addUserHasBuildingUnitConnection(int userId, int buildingUnitId) throws SQLException {
        String insert = "INSERT INTO USER_HAS_BUILDING_UNIT "
                + "(USER_ID, BUILDING_UNIT_ID) VALUES (" + userId + ", " + buildingUnitId + ")";
        Statement st = cnn.createStatement();
        st.execute(insert);
        st.close();
    }
    
    public void deleteUserHasBuildingUnitConnection(int userId, int buildingUnitId) throws SQLException {
        String insert = "DELETE FROM USER_HAS_BUILDING_UNIT "
                + "WHERE (USER_ID = " + userId + ") AND (BUILDING_UNIT_ID = " + buildingUnitId + ")";
        Statement st = cnn.createStatement();
        st.execute(insert);
        st.close();
    }
    
    public BuildingUnit getBuildingUnit(int id) throws SQLException {
        BuildingUnit result = null;
        String select = "SELECT "
                + "a.ID, "
                + "a.BUILDING_ID, "
                + "a.BUILDING_UNIT_TYPE_ID, "
                + "b.DESCRIPTION AS BUILDING_UNIT_TYPE, "
                + "a.REGISTRATION_ID, "
                + "a.DESCRIPTION, "
                + "a.NUMERATOR, "
                + "a.DENOMINATOR "
                + "FROM BUILDING_UNIT a "
                + "LEFT JOIN BUILDING_UNIT_TYPE b ON (b.ID = a.BUILDING_UNIT_TYPE_ID) "
                + "WHERE (a.ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = new BuildingUnit();
            result.setId(rs.getInt("ID"));
            result.setBuildingId(rs.getInt("BUILDING_ID"));
            result.setBuildingUnitTypeId(rs.getInt("BUILDING_UNIT_TYPE_ID"));
            result.setBuildingUnitType(rs.getString("BUILDING_UNIT_TYPE"));
            result.setRegistrationId(rs.getString("REGISTRATION_ID"));
            result.setDescription(rs.getString("DESCRIPTION"));
            result.setNumerator(rs.getInt("NUMERATOR"));
            result.setDenominator(rs.getInt("DENOMINATOR"));
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public int insertBuildingUnit(BuildingUnit u) throws SQLException {
        int result = 0;
        String insert = "INSERT INTO BUILDING_UNIT ("
                + "BUILDING_ID, "
                + "BUILDING_UNIT_TYPE_ID, "
                + "REGISTRATION_ID, "
                + "DESCRIPTION, "
                + "NUMERATOR, "
                + "DENOMINATOR"
                + ") VALUES (?,?,?,?,?,?) returning ID";
        
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, u.getBuildingId());
        ps.setInt(2, u.getBuildingUnitTypeId());
        ps.setString(3, u.getRegistrationId());
        ps.setString(4, u.getDescription());
        ps.setInt(5, u.getNumerator());
        ps.setInt(6, u.getDenominator());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getInt("ID");
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public void modifyBuildingUnit(BuildingUnit u) throws SQLException {
        String insert = "UPDATE BUILDING_UNIT SET "
                + "BUILDING_ID = ?, "
                + "BUILDING_UNIT_TYPE_ID = ?, "
                + "REGISTRATION_ID = ?, "
                + "DESCRIPTION = ?, "
                + "NUMERATOR = ?, "
                + "DENOMINATOR = ? "
                + "WHERE (ID = ?) ";
        
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, u.getBuildingId());
        ps.setInt(2, u.getBuildingUnitTypeId());
        ps.setString(3, u.getRegistrationId());
        ps.setString(4, u.getDescription());
        ps.setInt(5, u.getNumerator());
        ps.setInt(6, u.getDenominator());
        ps.setInt(7, u.getId());
        ps.executeUpdate();
        ps.close();
    }
    
    public void deleteBuildingUnit(BuildingUnit u) throws SQLException {
        String delete = "DELETE FROM BUILDING_UNIT WHERE (ID = ?) AND (BUILDING_ID = ?) ";
        PreparedStatement ps = cnn.prepareStatement(delete);
        ps.setInt(1, u.getId());
        ps.setInt(2, u.getBuildingId());
        ps.execute();
        ps.close();
    }
}
