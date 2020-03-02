/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author berk
 */
public final class User implements Serializable {
    private int id;
    private int companyId;
    private String firstName;
    private String lastName;
    private String salutation;
    private String address;
    private String city;
    private String postCode;
    private String country;
    private String fixedPhone;
    private String cellPhone;
    private String eMail;
    private String login;
    private String password;
    private boolean enabled;
    private boolean showInPhoneList;
    private boolean userLogged;
    private int languageId;
    private Date lastLogin;
    private HashMap roles;
    private HashMap permissions;
    private String internalNote;

    public User() {
        clear();
    }
    
    public void clear() {
        id = 0;
        companyId = 0;
        firstName = "";
        lastName = "";
        salutation = "";
        address = "";
        city = "";
        postCode = "";
        country = "";
        fixedPhone = "";
        cellPhone = "";
        eMail = "";
        login = "";
        password = "";
        enabled = false;
        showInPhoneList = false;
        userLogged = false;
        languageId = 0;
        lastLogin = null;
        roles = new HashMap();
        permissions = new HashMap();
        internalNote = "";
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the companyId
     */
    public int getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the salutation
     */
    public String getSalutation() {
        return salutation;
    }

    /**
     * @param salutation the salutation to set
     */
    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the postCode
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * @param postCode the postCode to set
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the fixedPhone
     */
    public String getFixedPhone() {
        return fixedPhone;
    }

    /**
     * @param fixedPhone the fixedPhone to set
     */
    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    /**
     * @return the cellPhone
     */
    public String getCellPhone() {
        return cellPhone;
    }

    /**
     * @param cellPhone the cellPhone to set
     */
    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    /**
     * @return the eMail
     */
    public String geteMail() {
        return eMail;
    }

    /**
     * @param eMail the eMail to set
     */
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the showInPhoneList
     */
    public boolean isShowInPhoneList() {
        return showInPhoneList;
    }

    /**
     * @param showInPhoneList the showInPhoneList to set
     */
    public void setShowInPhoneList(boolean showInPhoneList) {
        this.showInPhoneList = showInPhoneList;
    }

    /**
     * @return the userLogged
     */
    public boolean isUserLogged() {
        return userLogged;
    }

    /**
     * @param userLogged the userLogged to set
     */
    public void setUserLogged(boolean userLogged) {
        this.userLogged = userLogged;
    }

    /**
     * @return the roles
     */
    public HashMap getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(HashMap roles) {
        this.roles = roles;
    }
    
    public boolean hasRole(String r) {
        return getRoles().get(r) != null;
    }

    /**
     * @return the languageId
     */
    public int getLanguageId() {
        return languageId;
    }

    /**
     * @param languageId the languageId to set
     */
    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    /**
     * @return the permissions
     */
    public HashMap getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(HashMap permissions) {
        this.permissions = permissions;
    }
    
    public boolean hasPermission(String p) {
        return getPermissions().get(p) != null;
    }

    /**
     * @return the lastLogin
     */
    public Date getLastLogin() {
        return lastLogin;
    }

    /**
     * @param lastLogin the lastLogin to set
     */
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * @return the internalNote
     */
    public String getInternalNote() {
        return internalNote;
    }

    /**
     * @param internalNote the internalNote to set
     */
    public void setInternalNote(String internalNote) {
        this.internalNote = internalNote;
    }
    
}
