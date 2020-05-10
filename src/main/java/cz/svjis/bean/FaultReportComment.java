/*
 *       FaultReportComment.java
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

/**
 *
 * @author jarberan
 */
public class FaultReportComment {
    private int id;
    private int faultReportId;
    private User user;
    private Date insertionTime;
    private String body;

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
     * @return the faultReportId
     */
    public int getFaultReportId() {
        return faultReportId;
    }

    /**
     * @param faultReportId the faultReportId to set
     */
    public void setFaultReportId(int faultReportId) {
        this.faultReportId = faultReportId;
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
     * @return the insertionTime
     */
    public Date getInsertionTime() {
        return insertionTime;
    }

    /**
     * @param insertionTime the insertionTime to set
     */
    public void setInsertionTime(Date insertionTime) {
        this.insertionTime = insertionTime;
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
}
