/*
 *       FaultReport.java
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jarberan
 */
public class FaultReport {
    
    private int id;
    private int companyId;
    private String subject;
    private String description;
    private Date creationDate;
    private User createdByUser;
    private User assignedToUser;
    private boolean closed;
    private BuildingEntrance buildingEntrance;
    private List<Attachment> attachmentList;
    private List<FaultReportComment> faultReportCommentList = new ArrayList<>();
    
    public FaultReport() {
        clear();
    }

    public void clear() {
        id = 0;
        companyId = 0;
        subject = "";
        description = "";
        creationDate = new Date();
        createdByUser = null;
        assignedToUser = null;
        closed = false;
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
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
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
     * @return the createdByUser
     */
    public User getCreatedByUser() {
        return createdByUser;
    }

    /**
     * @param createdByUser the createdByUser to set
     */
    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    /**
     * @return the assignedToUser
     */
    public User getAssignedToUser() {
        return assignedToUser;
    }

    /**
     * @param assignedToUser the assignedToUser to set
     */
    public void setAssignedToUser(User assignedToUser) {
        this.assignedToUser = assignedToUser;
    }

    /**
     * @return the closed
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @param closed the closed to set
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    /**
     * @return the FaultReportCommentList
     */
    public List<FaultReportComment> getFaultReportCommentList() {
        return faultReportCommentList;
    }

    /**
     * @param faultReportCommentList the faultReportCommentList to set
     */
    public void setFaultReportCommentList(List<FaultReportComment> faultReportCommentList) {
        this.faultReportCommentList = new ArrayList<>(faultReportCommentList);
    }

    /**
     * @return the attachmentList
     */
    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    /**
     * @param attachmentList the attachmentList to set
     */
    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = new ArrayList<>(attachmentList);
    }

    /**
     * @return the buildingEntrance
     */
    public BuildingEntrance getBuildingEntrance() {
        return buildingEntrance;
    }

    /**
     * @param buildingEntrance the buildingEntrance to set
     */
    public void setBuildingEntrance(BuildingEntrance buildingEntrance) {
        this.buildingEntrance = buildingEntrance;
    }
    
    public String getEmailSubject() {
        String result;
        String entrance = "";
        
        if ((getBuildingEntrance() != null) && (!getBuildingEntrance().getDescription().equals(""))) {
            entrance = String.format(" - %s", getBuildingEntrance().getDescription());
        }
        result = String.format("#%d - %s%s", getId(), getSubject(), entrance);
        
        return result;
    }
}
