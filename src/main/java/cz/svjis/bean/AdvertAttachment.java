/*
 *       AdvertAttachment.java
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

public class AdvertAttachment {
    private int id;
    private int advertId;
    private User user;
    private Date uploadTime;
    private String contentType;
    private String fileName;
    private byte[] data;
    
    public AdvertAttachment() {
        super();
        clear();
    }
    
    public void clear() {
        id = 0;
        advertId = 0;
        user = new User();
        uploadTime = new Date();
        contentType = "";
        fileName = "";
        data = null;
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
     * @return the advertId
     */
    public int getAdvertId() {
        return advertId;
    }
    
    /**
     * @param advertId the advertId to set
     */
    public void setAdvertId(int advertId) {
        this.advertId = advertId;
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
     * @return the uploadTime
     */
    public Date getUploadTime() {
        return uploadTime;
    }
    
    /**
     * @param uploadTime the uploadTime to set
     */
    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
    
    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }
    
    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }
    
    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }
    
    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }
    
}
