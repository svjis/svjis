/*
 *       UserDAO.java
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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author berk
 */
public class UserDAO extends DAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    
    public UserDAO (Connection cnn) {
        super(cnn);
    }
    
    /**
     * Returns list of users for company
     * @param companyId ID of company
     * @param inPhoneListOnly Gives user list from phonel ist only
     * @param roleId Gives users owning specified role (0 = give all users)
     * @param enabled Gives enabled or disabled users
     * @return List of users
     * @throws SQLException 
     */
    public List<User> getUserList(int companyId, boolean inPhoneListOnly, int roleId, boolean enabled) throws SQLException {
        ArrayList<User> result = new ArrayList<>();
        String filter = "";
        
        if (inPhoneListOnly) {
            filter += "AND (a.SHOW_IN_PHONELIST = 1) ";
        }
        
        if (roleId != 0) {
            filter += "AND (b.ROLE_ID IS NOT NULL) ";
        }
        
        filter += "AND (a.ENABLED = " + ((enabled) ? "1" : "0") + ") ";

        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.FIRST_NAME, "
                + "a.LAST_NAME, "
                + "a.SALUTATION, "
                + "a.ADDRESS, "
                + "a.CITY, "
                + "a.POST_CODE, "
                + "a.COUNTRY, "
                + "a.FIXED_PHONE, "
                + "a.CELL_PHONE, "
                + "a.E_MAIL, "
                + "a.LOGIN, "
                + "a.ENABLED, "
                + "a.SHOW_IN_PHONELIST,"
                + "a.LANGUAGE_ID, "
                + "a.INTERNAL_NOTE, "
                + "(SELECT FIRST 1 l.\"TIME\" FROM LOG l WHERE (l.USER_ID = a.ID) AND (l.OPERATION_ID = 1) ORDER BY l.ID DESC) AS LAST_LOGIN, "
                + "b.ROLE_ID "
                + "FROM \"USER\" a "
                + "LEFT JOIN USER_HAS_ROLE b on b.USER_ID = a.ID and b.ROLE_ID = ? "
                + "WHERE (a.COMPANY_ID = ?) "
                + filter
                + "ORDER BY a.LAST_NAME collate UNICODE_CI_AI";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, roleId);
            ps.setInt(2, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("ID"));
                    u.setCompanyId(rs.getInt("COMPANY_ID"));
                    u.setFirstName(rs.getString("FIRST_NAME"));
                    u.setLastName(rs.getString("LAST_NAME"));
                    u.setSalutation(rs.getString("SALUTATION"));
                    u.setAddress(rs.getString("ADDRESS"));
                    u.setCity(rs.getString("CITY"));
                    u.setPostCode(rs.getString("POST_CODE"));
                    u.setCountry(rs.getString("COUNTRY"));
                    u.setFixedPhone(rs.getString("FIXED_PHONE"));
                    u.setCellPhone(rs.getString("CELL_PHONE"));
                    u.seteMail(rs.getString("E_MAIL"));
                    u.setLogin(rs.getString("LOGIN"));
                    u.setEnabled(rs.getBoolean("ENABLED"));
                    u.setShowInPhoneList(rs.getBoolean("SHOW_IN_PHONELIST"));
                    u.setLanguageId(rs.getInt("LANGUAGE_ID"));
                    u.setLastLogin(rs.getTimestamp("LAST_LOGIN"));
                    u.setInternalNote(rs.getString("INTERNAL_NOTE"));
                    result.add(u);
                }
            }
        }
        
        return result;
    }
    
    public List<User> getUserListWithPermission(int companyId, String permission) throws SQLException {
        ArrayList<User> result = new ArrayList<>();
        String select = "SELECT a.ID, a.COMPANY_ID, a.FIRST_NAME, a.LAST_NAME, a.E_MAIL, d.DESCRIPTION\n" +
                        "FROM \"USER\" a\n" +
                        "LEFT JOIN USER_HAS_ROLE b on b.USER_ID = a.ID\n" +
                        "LEFT JOIN ROLE_HAS_PERMISSION c on c.ROLE_ID = b.ROLE_ID\n" +
                        "LEFT JOIN PERMISSION d on d.ID = c.PERMISSION_ID\n" +
                        "where a.COMPANY_ID = ? and d.DESCRIPTION = ? and a.ENABLED = 1\n" +
                        "group by a.ID, a.COMPANY_ID, a.FIRST_NAME, a.E_MAIL, a.LAST_NAME, d.DESCRIPTION\n" +
                        "order by a.FIRST_NAME, a.LAST_NAME";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            ps.setString(2, permission);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("ID"));
                    u.setCompanyId(rs.getInt("COMPANY_ID"));
                    u.setFirstName(rs.getString("FIRST_NAME"));
                    u.setLastName(rs.getString("LAST_NAME"));
                    u.seteMail(rs.getString("E_MAIL"));
                    result.add(u);
                }
            }
        }
        
        return result;
    }
    
    public User getUser(int companyId, int userId) throws SQLException {
        User result = null;
        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.FIRST_NAME, "
                + "a.LAST_NAME, "
                + "a.SALUTATION, "
                + "a.ADDRESS, "
                + "a.CITY, "
                + "a.POST_CODE, "
                + "a.COUNTRY, "
                + "a.FIXED_PHONE, "
                + "a.CELL_PHONE, "
                + "a.E_MAIL, "
                + "a.LOGIN, "
                + "a.ENABLED, "
                + "a.SHOW_IN_PHONELIST, "
                + "a.LANGUAGE_ID, "
                + "a.INTERNAL_NOTE "
                + "FROM \"USER\" a "
                + "WHERE (a.COMPANY_ID = ?) AND (a.ID = ?) ";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new User();
                    result.setId(rs.getInt("ID"));
                    result.setCompanyId(rs.getInt("COMPANY_ID"));
                    result.setFirstName(rs.getString("FIRST_NAME"));
                    result.setLastName(rs.getString("LAST_NAME"));
                    result.setSalutation(rs.getString("SALUTATION"));
                    result.setAddress(rs.getString("ADDRESS"));
                    result.setCity(rs.getString("CITY"));
                    result.setPostCode(rs.getString("POST_CODE"));
                    result.setCountry(rs.getString("COUNTRY"));
                    result.setFixedPhone(rs.getString("FIXED_PHONE"));
                    result.setCellPhone(rs.getString("CELL_PHONE"));
                    result.seteMail(rs.getString("E_MAIL"));
                    result.setLogin(rs.getString("LOGIN"));
                    result.setEnabled(rs.getBoolean("ENABLED"));
                    result.setShowInPhoneList(rs.getBoolean("SHOW_IN_PHONELIST"));
                    result.setLanguageId(rs.getInt("LANGUAGE_ID"));
                    result.setInternalNote(rs.getString("INTERNAL_NOTE"));
                }
            }
        }
        if (result != null) {
            result.setRoles(getUserRoles(result.getId()));
            result.setPermissions(this.getUserPermissions(result.getId()));
        }
        return result;
    }
    
    public User getUserByLogin(int companyId, String login) throws SQLException {
        User result = null;
        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.FIRST_NAME, "
                + "a.LAST_NAME, "
                + "a.SALUTATION, "
                + "a.ADDRESS, "
                + "a.CITY, "
                + "a.POST_CODE, "
                + "a.COUNTRY, "
                + "a.FIXED_PHONE, "
                + "a.CELL_PHONE, "
                + "a.E_MAIL, "
                + "a.LOGIN, "
                + "a.ENABLED, "
                + "a.SHOW_IN_PHONELIST,"
                + "a.LANGUAGE_ID, "
                + "a.INTERNAL_NOTE "
                + "FROM \"USER\" a "
                + "WHERE (a.COMPANY_ID = ?) AND (a.LOGIN  collate UNICODE_CI_AI = ?) ";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            ps.setString(2, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new User();
                    result.setId(rs.getInt("ID"));
                    result.setCompanyId(rs.getInt("COMPANY_ID"));
                    result.setFirstName(rs.getString("FIRST_NAME"));
                    result.setLastName(rs.getString("LAST_NAME"));
                    result.setSalutation(rs.getString("SALUTATION"));
                    result.setAddress(rs.getString("ADDRESS"));
                    result.setCity(rs.getString("CITY"));
                    result.setPostCode(rs.getString("POST_CODE"));
                    result.setCountry(rs.getString("COUNTRY"));
                    result.setFixedPhone(rs.getString("FIXED_PHONE"));
                    result.setCellPhone(rs.getString("CELL_PHONE"));
                    result.seteMail(rs.getString("E_MAIL"));
                    result.setLogin(rs.getString("LOGIN"));
                    result.setEnabled(rs.getBoolean("ENABLED"));
                    result.setShowInPhoneList(rs.getBoolean("SHOW_IN_PHONELIST"));
                    result.setLanguageId(rs.getInt("LANGUAGE_ID"));
                    result.setInternalNote(rs.getString("INTERNAL_NOTE"));
                }
            }
        }
        if (result != null) {
            result.setRoles(getUserRoles(result.getId()));
            result.setPermissions(this.getUserPermissions(result.getId()));
        }
        return result;
    }
    
    private HashMap getUserRoles(int userId) throws SQLException {
        HashMap result = new HashMap();
        String select = "SELECT "
                + "a.ROLE_ID, "
                + "r.DESCRIPTION "
                + "FROM USER_HAS_ROLE a "
                + "LEFT JOIN \"ROLE\" r ON (r.ID = a.ROLE_ID) "
                + "WHERE (a.USER_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getInt("ROLE_ID"), rs.getString("DESCRIPTION"));
                }
            }
        }
        return result;
    }
    
    private HashMap getUserPermissions(int userId) throws SQLException {
        HashMap result = new HashMap();
        String select = "SELECT "
                + "a.ROLE_ID, "
                + "p.DESCRIPTION "
                + "FROM USER_HAS_ROLE a "
                + "LEFT JOIN \"ROLE\" r ON (r.ID = a.ROLE_ID) "
                + "LEFT JOIN ROLE_HAS_PERMISSION rp ON (rp.ROLE_ID = r.ID) "
                + "LEFT JOIN \"PERMISSION\" p ON (p.ID = rp.PERMISSION_ID) "
                + "WHERE (a.USER_ID = ?) "
                + "GROUP BY a.ROLE_ID, p.DESCRIPTION ";
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("DESCRIPTION"),"");
                }
            }
        }
        return result;
    }
    
    public void storeNewPassword(int company, String login, String password) throws SQLException {
        String salt = UUID.randomUUID().toString();
        String hash = generateHash(password, salt);
        String sql = "UPDATE \"USER\" SET \"PASSWORD\" = NULL, \"PASSWORD_HASH\" = ?, \"PASSWORD_SALT\" = ? WHERE \"COMPANY_ID\" = ? AND \"LOGIN\" = ?;";
        
        try (PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setString(2, salt);
            ps.setInt(3, company);
            ps.setString(4, login);
            ps.execute();
        }
    }
    
    public static String generateHash(String password, String salt){
        String result = "";
        try {
            byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_16LE);
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_16LE);
            
            byte[] bytesToHash = new byte[saltBytes.length + passwordBytes.length];
            
            System.arraycopy(saltBytes, 0, bytesToHash, 0, saltBytes.length);
            System.arraycopy(passwordBytes, 0, bytesToHash, saltBytes.length, passwordBytes.length);
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(bytesToHash);
            result = DatatypeConverter.printHexBinary(hash).toLowerCase();

        } catch (NoSuchAlgorithmException ex) {
            LOGGER.log(Level.SEVERE, "Culd not generate hash", ex);
        }
        return result;
    }
    
    public boolean verifyPassword(User u, String password, boolean loginUser) throws SQLException {
        boolean result = false;
        String hash = null;
        String salt = null;
        String select = "SELECT  a.PASSWORD_HASH, a.PASSWORD_SALT FROM \"USER\" a WHERE a.COMPANY_ID = ? AND a.LOGIN = ?";
        
        /* find plain passwords and encrypt them */
        convertPasswords();
        
        if ((u.isEnabled()) && (u.getLogin() != null) && (!u.getLogin().equals(""))) {
            
            try (PreparedStatement ps = cnn.prepareStatement(select)) {
                ps.setInt(1, u.getCompanyId());
                ps.setString(2, u.getLogin());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        hash = rs.getString("PASSWORD_HASH");
                        salt = rs.getString("PASSWORD_SALT");
                    }
                }
            }
            
            if ((hash != null) && (salt != null) && (password != null) && !password.equals("")) {
                String pwdHash = generateHash(password, salt);
                if (pwdHash.equals(hash)) {
                    if (loginUser) u.setUserLogged(true);
                    result = true;
                }
            }
        }
        
        if (loginUser && (!result)) {
            u.clear();
        }
        
        return result;
    }
    
    public String getAuthToken(int companyId, String login) throws SQLException {
        String result = null;
        String hash = null;
        Timestamp exp = null;
        
        String select = "SELECT "
                + "a.PERM_LOGIN_HASH, "
                + "a.PERM_LOGIN_EXPIRES "
                + "FROM \"USER\" a "
                + "WHERE (a.COMPANY_ID = ?) AND (a.LOGIN  collate UNICODE_CI_AI = ?) ";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            ps.setString(2, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hash = rs.getString("PERM_LOGIN_HASH");
                    exp = rs.getTimestamp("PERM_LOGIN_EXPIRES");
                }
            }
        }
        
        if ((exp != null) && (exp.getTime() > new Date().getTime())) {
            result = hash;
        }
        
        return result;
    }
    
    public String createNewAuthToken(int companyId, String login, int validityInHours) throws SQLException {
        String update = "UPDATE \"USER\" a "
                + "SET a.PERM_LOGIN_HASH = ?, a.PERM_LOGIN_EXPIRES = ? "
                + "WHERE (a.COMPANY_ID = ?) AND (a.LOGIN  collate UNICODE_CI_AI = ?) ";
        
        String token = UUID.randomUUID().toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, validityInHours);
        Date expires = cal.getTime();
        
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setString(1, token);
            ps.setTimestamp(2, new Timestamp(expires.getTime()));
            ps.setInt(3, companyId);
            ps.setString(4, login);
            ps.execute();
        }
        
        return token;
    }
    
    private void convertPasswords() throws SQLException {
        String select = "SELECT a.COMPANY_ID, a.\"LOGIN\", a.\"PASSWORD\" FROM \"USER\" a WHERE a.\"PASSWORD\" IS NOT NULL";
        ArrayList<User> list = new ArrayList<>();
        
        try (PreparedStatement ps = cnn.prepareStatement(select); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setCompanyId(rs.getInt("COMPANY_ID"));
                u.setLogin(rs.getString("LOGIN"));
                u.setPassword(rs.getString("PASSWORD"));
                list.add(u);
            }
        }
        
        for (User u: list) {
            storeNewPassword(u.getCompanyId(), u.getLogin(), u.getPassword());
        }
    }
    
    public void modifyUser(User user) throws SQLException {
        int updated;
        cnn.setAutoCommit(false);
        String update = "UPDATE \"USER\" SET "
                + "FIRST_NAME = ?, "
                + "LAST_NAME = ?, "
                + "SALUTATION = ?, "
                + "ADDRESS = ?, "
                + "CITY = ?, "
                + "POST_CODE = ?, "
                + "COUNTRY = ?, "
                + "FIXED_PHONE = ?, "
                + "CELL_PHONE = ?, "
                + "E_MAIL = ?, "
                + "LOGIN = ?, "
                + "ENABLED = ?, "
                + "SHOW_IN_PHONELIST = ?, "
                + "LANGUAGE_ID = ?, "
                + "INTERNAL_NOTE = ? "
                + "WHERE (ID = ?) AND (COMPANY_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getSalutation());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getCity());
            ps.setString(6, user.getPostCode());
            ps.setString(7, user.getCountry());
            ps.setString(8, user.getFixedPhone());
            ps.setString(9, user.getCellPhone());
            ps.setString(10, user.geteMail());
            ps.setString(11, user.getLogin());
            ps.setBoolean(12, user.isEnabled());
            ps.setBoolean(13, user.isShowInPhoneList());
            ps.setInt(14, user.getLanguageId());
            ps.setString(15, user.getInternalNote());
            ps.setInt(16, user.getId());
            ps.setInt(17, user.getCompanyId());
            updated = ps.executeUpdate();
        }
        
        if ((user.getPassword() != null) && (!user.getPassword().equals(""))) {
            storeNewPassword(user.getCompanyId(), user.getLogin(), user.getPassword());
        }
        
        if (updated == 1) {
            modifyUserRoles(user);
        }
        cnn.commit();
        cnn.setAutoCommit(true);
    }
    
    public int insertUser(User user) throws SQLException {
        int result = 0;
        cnn.setAutoCommit(false);
        
        String update = "INSERT INTO \"USER\" ("
                + "COMPANY_ID, "
                + "FIRST_NAME, "
                + "LAST_NAME, "
                + "SALUTATION, "
                + "ADDRESS, "
                + "CITY, "
                + "POST_CODE, "
                + "COUNTRY, "
                + "FIXED_PHONE, "
                + "CELL_PHONE, "
                + "E_MAIL, "
                + "LOGIN, "
                + "ENABLED, "
                + "SHOW_IN_PHONELIST, "
                + "LANGUAGE_ID, "
                + "INTERNAL_NOTE "
                + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning ID";
        
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
            ps.setInt(1, user.getCompanyId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getSalutation());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getCity());
            ps.setString(7, user.getPostCode());
            ps.setString(8, user.getCountry());
            ps.setString(9, user.getFixedPhone());
            ps.setString(10, user.getCellPhone());
            ps.setString(11, user.geteMail());
            ps.setString(12, user.getLogin());
            ps.setBoolean(13, user.isEnabled());
            ps.setBoolean(14, user.isShowInPhoneList());
            ps.setInt(15, user.getLanguageId());
            ps.setString(16, user.getInternalNote());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("ID");
                }
            }
        }
        
        if ((user.getPassword() != null) && (!user.getPassword().equals(""))) {
            storeNewPassword(user.getCompanyId(), user.getLogin(), user.getPassword());
        }
        
        user.setId(result);
        modifyUserRoles(user);
        
        cnn.commit();
        cnn.setAutoCommit(true);

        return result;
    }
    
    private void modifyUserRoles(User user) throws SQLException {
        String delete = "DELETE FROM USER_HAS_ROLE WHERE (USER_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(delete)) {
            ps.setInt(1, user.getId());
            ps.execute();
        }

        String insert = "INSERT INTO USER_HAS_ROLE (USER_ID, ROLE_ID) VALUES (?, ?)";
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            Iterator<Integer> roleI = user.getRoles().keySet().iterator();
            while (roleI.hasNext()) {
                int roleId = roleI.next();
                ps.setInt(1, user.getId());
                ps.setInt(2, roleId);
                ps.execute();
            }
        }
    }
    
    public void deleteUser(User user) throws SQLException {
        cnn.setAutoCommit(false);
        String delete = "DELETE FROM USER_HAS_ROLE WHERE (USER_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(delete)) {
            ps.setInt(1, user.getId());
            ps.execute();
        }
        
        String delete2 = "DELETE FROM \"USER\" WHERE (ID = ?) and (COMPANY_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(delete2)) {
            ps.setInt(1, user.getId());
            ps.setInt(2, user.getCompanyId());
            ps.execute();
        }
        cnn.commit();
        cnn.setAutoCommit(true);
    }
    
    public boolean testLoginDuplicity(String login, int userId, int companyId) throws SQLException {
        boolean result = true;
        String select = "SELECT a.ID FROM \"USER\" a WHERE (upper(a.LOGIN) = upper(?)) and (a.ID <> ?) and (a.COMPANY_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setString(1, login);
            ps.setInt(2, userId);
            ps.setInt(3, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = false;
                }
            }
        }
        return result;
    }
    
    public boolean testLoginValidity(String login) {
        boolean result = true;
        String s = login;
        for (int i = 0; i < s.length(); i++) {
            if ((s.charAt(i) < 32) || (s.charAt(i) > 126)) {
                result = false;
            }
        }
        if (login.isEmpty()) {
            result = false;
        }
        return result;
    }
    
    public boolean testPasswordValidity (String password) {
        boolean result = true;
        if (password.length() < 6) {
            result = false;
        }
        return result;
    }
    
    public List<User> findLostPassword(int companyId, String email) throws SQLException {
        ArrayList<User> result = new ArrayList<>();

        String select = "SELECT a.ID, a.COMPANY_ID, a.E_MAIL, a.LOGIN FROM \"USER\" a "
                + "WHERE (a.COMPANY_ID = ?) and (trim(a.E_MAIL) collate UNICODE_CI_AI = ?) and (a.ENABLED = 1)";

        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, companyId);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("ID"));
                    u.setCompanyId(rs.getInt("COMPANY_ID"));
                    u.seteMail(rs.getString("E_MAIL"));
                    u.setLogin(rs.getString("LOGIN"));
                    result.add(u);
                }
            }
        }
        return result;
    }
}
