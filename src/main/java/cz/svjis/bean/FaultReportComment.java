/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
