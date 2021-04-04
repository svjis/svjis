/*
 *       Role.java
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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author berk
 */
public class Role {
    private int id;
    private int companyId;
    private String description;
    private int numOfUsers;
    private HashMap<Integer, String> permissions;
    
    public Role() {
        clear();
    }
    
    public void clear() {
        id = 0;
        companyId = 0;
        description = "";
        permissions = new HashMap<>();
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
    public Map<Integer, String> getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(Map<Integer, String> permissions) {
        this.permissions = new HashMap<>(permissions);
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
