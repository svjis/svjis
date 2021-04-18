/*
 *       Advert.java
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

import java.util.Date;

public class Advert {
    private int id;
    private int companyId;
    private AdvertType type;
    private String header;
    private String body;
    private User user;
    private Date creationDate;
    private boolean published;
    
    public Advert() {
        super();
        clear();
    }
    
    public void clear() {
        id = 0;
        companyId = 0;
        type = new AdvertType();
        header = "";
        body = "";
        user = new User();
        creationDate = new Date();
        published = false;
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
     * @return the type
     */
    public AdvertType getType() {
        return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(AdvertType type) {
        this.type = type;
    }
    
    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }
    
    /**
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }
    
    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }
    
    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }
    
    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }
    
    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }
    
    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    /**
     * @return the published
     */
    public boolean isPublished() {
        return published;
    }
    
    /**
     * @param published the published to set
     */
    public void setPublished(boolean published) {
        this.published = published;
    }

}
