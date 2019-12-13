/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author berk
 */
public class UserDAO {
    private Connection cnn;
    
    public UserDAO (Connection cnn) {
        this.cnn = cnn;
    }
    
    /**
     * Returns list of users for company
     * @param companyId ID of company
     * @param inPhoneListOnly Gives user list from phonel ist only
     * @param roleId Gives users owning specified role (0 = give all users)
     * @param enabledUsersOnly Gives only enabled users
     * @return List of users
     * @throws SQLException 
     */
    public ArrayList<User> getUserList(int companyId, boolean inPhoneListOnly, int roleId, boolean enabledUsersOnly) throws SQLException {
        ArrayList<User> result = new ArrayList<User>();
        String filter = "";
        
        if (inPhoneListOnly) {
            filter += "AND (a.SHOW_IN_PHONELIST = 1) ";
        }
        
        if (roleId != 0) {
            filter += "AND (b.ROLE_ID IS NOT NULL) ";
        }
        
        if (enabledUsersOnly) {
            filter += "AND (a.ENABLED = 1) ";
        }
        
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
                + "(SELECT FIRST 1 l.\"TIME\" FROM LOG l WHERE (l.USER_ID = a.ID) AND (l.OPERATION_ID = 1) ORDER BY l.ID DESC) AS LAST_LOGIN, "
                + "b.ROLE_ID "
                + "FROM \"USER\" a "
                + "LEFT JOIN USER_HAS_ROLE b on b.USER_ID = a.ID and b.ROLE_ID = ? "
                + "WHERE (a.COMPANY_ID = ?) "
                + filter
                + "ORDER BY a.LAST_NAME collate UNICODE_CI_AI";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, roleId);
        ps.setInt(2, companyId);
        ResultSet rs = ps.executeQuery();
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
            result.add(u);
        }
        rs.close();
        ps.close();
        
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
                + "a.LANGUAGE_ID "
                + "FROM \"USER\" a "
                + "WHERE (a.COMPANY_ID = ?) AND (a.ID = ?) ";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ps.setInt(2, userId);
        ResultSet rs = ps.executeQuery();
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
        }
        rs.close();
        ps.close();
        
        result.setRoles(getUserRoles(result.getId()));
        result.setPermissions(this.getUserPermissions(result.getId()));
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
                + "a.LANGUAGE_ID "
                + "FROM \"USER\" a "
                + "WHERE (a.COMPANY_ID = ?) AND (a.LOGIN  collate UNICODE_CI_AI = ?) ";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ps.setString(2, login);
        ResultSet rs = ps.executeQuery();
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
        }
        rs.close();
        ps.close();
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
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            result.put(new Integer(rs.getInt("ROLE_ID")), rs.getString("DESCRIPTION"));
        }
        rs.close();
        ps.close();
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
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            result.put(rs.getString("DESCRIPTION"),"");
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public void storeNewPassword(int company, String login, String password) throws SQLException {
        String salt = UUID.randomUUID().toString();
        String hash = generateHash(password, salt);
        String sql = "UPDATE \"USER\" SET \"PASSWORD\" = NULL, \"PASSWORD_HASH\" = ?, \"PASSWORD_SALT\" = ? WHERE \"COMPANY_ID\" = ? AND \"LOGIN\" = ?;";
        
        PreparedStatement ps = cnn.prepareStatement(sql);
        ps.setString(1, hash);
        ps.setString(2, salt);
        ps.setInt(3, company);
        ps.setString(4, login);
        ps.execute();
        ps.close();
    }
    
    public static String generateHash(String password, String salt){
        String result = "";
        try {
            byte[] saltBytes = salt.getBytes("UTF-16LE");
            byte[] passwordBytes = password.getBytes("UTF-16LE");
            
            byte[] bytesToHash = new byte[saltBytes.length + passwordBytes.length];
            
            System.arraycopy(saltBytes, 0, bytesToHash, 0, saltBytes.length);
            System.arraycopy(passwordBytes, 0, bytesToHash, saltBytes.length, passwordBytes.length);
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(bytesToHash);
            //result = Base64.encodeBase64String(hash);
            result = DatatypeConverter.printHexBinary(hash).toLowerCase();

        } catch (Exception ex) {
            ex.printStackTrace();
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
            
            PreparedStatement ps = cnn.prepareStatement(select);
            ps.setInt(1, u.getCompanyId());
            ps.setString(2, u.getLogin());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                hash = rs.getString("PASSWORD_HASH");
                salt = rs.getString("PASSWORD_SALT");
            }
            rs.close();
            ps.close();
            
            if ((hash != null) && (salt != null) && (password != null) && !password.equals("")) {
                String pwdHash = generateHash(password, salt);
                if (pwdHash.equals(hash)) {
                    if (loginUser) u.setUserLogged(true);
                    result = true;
                }
            }
        }
        
        if (loginUser && (result == false)) {
            u.clear();
        }
        
        return result;
    }
    
    public String getAuthToken(int companyId, String login) throws SQLException, NoSuchAlgorithmException {
        String result = "";
        String select = "SELECT "
                + "a.PASSWORD_SALT "
                + "FROM \"USER\" a "
                + "WHERE (a.COMPANY_ID = ?) AND (a.LOGIN  collate UNICODE_CI_AI = ?) ";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ps.setString(2, login);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getString("PASSWORD_SALT");
            result = getMd5Digest(result);
        }
        rs.close();
        ps.close();
        
        return result;
    }
    
    public boolean verifyAuthToken(int companyId, String login, String token) throws SQLException, NoSuchAlgorithmException {
        boolean result = false;
        
        /* find plain passwords and encrypt them */
        convertPasswords();
        
        String t = getAuthToken(companyId, login);
        if ((t != null) && (token != null) && !token.equals("") && token.equals(t)) {
            result = true;
        }
        
        return result;
    }
    
    private String getMd5Digest(String pInput) throws NoSuchAlgorithmException {    
        MessageDigest lDigest = MessageDigest.getInstance("MD5");
        lDigest.update(pInput.getBytes());
        BigInteger lHashInt = new BigInteger(1, lDigest.digest());
        return String.format("%1$032X", lHashInt);
    }
    
    private void convertPasswords() throws SQLException {
        String select = "SELECT a.COMPANY_ID, a.\"LOGIN\", a.\"PASSWORD\" FROM \"USER\" a WHERE a.\"PASSWORD\" IS NOT NULL";
        ArrayList<User> list = new ArrayList<User>();
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User u = new User();
            u.setCompanyId(rs.getInt("COMPANY_ID"));
            u.setLogin(rs.getString("LOGIN"));
            u.setPassword(rs.getString("PASSWORD"));
            list.add(u);
        }
        rs.close();
        ps.close();
        
        for (User u: list) {
            storeNewPassword(u.getCompanyId(), u.getLogin(), u.getPassword());
        }
    }
    
    public void modifyUser(User user) throws SQLException {
        int updated = 0;
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
                + "LANGUAGE_ID = ? "
                + "WHERE (ID = ?) AND (COMPANY_ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(update);
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
        ps.setInt(15, user.getId());
        ps.setInt(16, user.getCompanyId());
        updated = ps.executeUpdate();
        ps.close();
        
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
                + "LANGUAGE_ID"
                + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning ID";
        
        PreparedStatement ps = cnn.prepareStatement(update);
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
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getInt("ID");
        }
        ps.close();
        
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
        PreparedStatement ps = cnn.prepareStatement(delete);
        ps.setInt(1, user.getId());
        ps.execute();
        ps.close();

        String insert = "INSERT INTO USER_HAS_ROLE (USER_ID, ROLE_ID) VALUES (?, ?)";
        ps = cnn.prepareStatement(insert);
        Iterator<Integer> roleI = user.getRoles().keySet().iterator();
        while (roleI.hasNext()) {
            int roleId = roleI.next();
            ps.setInt(1, user.getId());
            ps.setInt(2, roleId);
            ps.execute();
        }
        ps.close();
    }
    
    public void deleteUser(User user) throws SQLException {
        cnn.setAutoCommit(false);
        String delete = "DELETE FROM USER_HAS_ROLE WHERE (USER_ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(delete);
        ps.setInt(1, user.getId());
        ps.execute();
        ps.close();
        
        String delete2 = "DELETE FROM \"USER\" WHERE (ID = ?) and (COMPANY_ID = ?)";
        ps = cnn.prepareStatement(delete2);
        ps.setInt(1, user.getId());
        ps.setInt(2, user.getCompanyId());
        ps.execute();
        ps.close();
        cnn.commit();
        cnn.setAutoCommit(true);
    }
    
    public boolean testLoginDuplicity(String login, int userId, int companyId) throws SQLException {
        boolean result = true;
        String select = "SELECT a.ID FROM \"USER\" a WHERE (upper(a.LOGIN) = upper('" + login + "')) and (a.ID <> " + userId + ") and (a.COMPANY_ID = " + companyId + ")";
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(select);
        if (rs.next()) {
            result = false;
        }
        rs.close();
        st.close();
        return result;
    }
    
    public boolean testLoginValidity(String login) {
        boolean result = true;
        StringBuilder sb = new StringBuilder(login);
        for (int i = 0; i < sb.length(); i++) {
            if ((sb.charAt(i) < 32) || (sb.charAt(i) > 126)) {
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
    
    public ArrayList<User> findLostPassword(int companyId, String email) throws SQLException {
        ArrayList<User> result = new ArrayList<User>();

        String select = "SELECT a.ID, a.COMPANY_ID, a.E_MAIL, a.LOGIN FROM \"USER\" a "
                + "WHERE (a.COMPANY_ID = ?) and (trim(a.E_MAIL) collate UNICODE_CI_AI = ?) and (a.ENABLED = 1)";

        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, companyId);
        ps.setString(2, email);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User u = new User();
            u.setId(rs.getInt("ID"));
            u.setCompanyId(rs.getInt("COMPANY_ID"));
            u.seteMail(rs.getString("E_MAIL"));
            u.setLogin(rs.getString("LOGIN"));
            result.add(u);
        }
        rs.close();
        ps.close();
        return result;
    }
}
