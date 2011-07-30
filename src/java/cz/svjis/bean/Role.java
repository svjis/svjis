/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.util.HashMap;

/**
 *
 * @author berk
 */
public class Role {
    private int id;
    private int companyId;
    private String description;
    private int numOfUsers;
    private HashMap permissions;
    
    public Role() {
        clear();
    }
    
    public void clear() {
        id = 0;
        companyId = 0;
        description = "";
        permissions = new HashMap();
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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

    /**
     * @return the numOfUsers
     */
    public int getNumOfUsers() {
        return numOfUsers;
    }

    /**
     * @param numOfUsers the numOfUsers to set
     */
    public void setNumOfUsers(int numOfUsers) {
        this.numOfUsers = numOfUsers;
    }
}
