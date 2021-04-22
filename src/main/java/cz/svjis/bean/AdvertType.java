/*
 *       AdvertType.java
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

public class AdvertType {
    private int id;
    private String description;
    private int cnt;

    
    public AdvertType() {
        super();
        clear();
    }
    
    public void clear() {
        id = 0;
        description = "";
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
     * @return the cnt
     */
    public int getCnt() {
        return cnt;
    }

    /**
     * @param cnt the cnt to set
     */
    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
