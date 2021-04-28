/*
 *       MenuNode.java
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

/**
 *
 * @author jaroslav_b
 */
public class MenuNode {
    
    private int id;
    private String description;
    private int parentId;
    private int numOfChilds;
    private boolean hide;
    
    
    public MenuNode() {
        clear();
    }
    
    public void clear() {
        id = 0;
        description = "";
        parentId = 0;
        hide = false;
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
     * @return the parentId
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the numOfChilds
     */
    public int getNumOfChilds() {
        return numOfChilds;
    }

    /**
     * @param numOfChilds the numOfChilds to set
     */
    public void setNumOfChilds(int numOfChilds) {
        this.numOfChilds = numOfChilds;
    }
    
    /**
     * @return the hide
     */
    public boolean isHide() {
        return hide;
    }

    /**
     * @param hide the hide to set
     */
    public void setHide(boolean hide) {
        this.hide = hide;
    }
}
