/*
 *       Article.java
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author berk
 */
public class Article {
    private int id;
    private int companyId;
    private int menuNodeId;
    private String menuNodeDescription;
    private int languageId;
    private String header;
    private String description;
    private String body;
    private int authorId;
    private User author;
    private Date creationDate;
    private boolean published;
    private boolean commentsAllowed;
    private int numOfComments;
    private int numOfReads;
    private ArrayList<ArticleAttachment> attachmentList;
    private ArrayList<ArticleComment> commentList;
    private HashMap roles;
    
    public Article() {
        clear();
    }

    public void clear() {
        id = 0;
        companyId = 0;
        menuNodeId = 0;
        menuNodeDescription = "";
        languageId = 0;
        header = "";
        description = "";
        body = "";
        authorId = 0;
        author = null;
        creationDate = new Date();
        published = false;
        commentsAllowed = false;
        numOfComments = 0;
        numOfReads = 0;
        attachmentList = new ArrayList<>();
        commentList = null;
        roles = new HashMap();
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
     * @return the menuNodeId
     */
    public int getMenuNodeId() {
        return menuNodeId;
    }

    /**
     * @param menuNodeId the menuNodeId to set
     */
    public void setMenuNodeId(int menuNodeId) {
        this.menuNodeId = menuNodeId;
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
     * @return the author
     */
    public int getAuthorId() {
        return authorId;
    }

    /**
     * @param authorId the author to set
     */
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
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

    /**
     * @return the commentsAllowed
     */
    public boolean isCommentsAllowed() {
        return commentsAllowed;
    }

    /**
     * @param commentsAllowed the commentsAllowed to set
     */
    public void setCommentsAllowed(boolean commentsAllowed) {
        this.commentsAllowed = commentsAllowed;
    }

    /**
     * @return the author
     */
    public User getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * @return the attachmentList
     */
    public List<ArticleAttachment> getAttachmentList() {
        return attachmentList;
    }

    /**
     * @param attachmentList the attachmentList to set
     */
    public void setAttachmentList(List<ArticleAttachment> attachmentList) {
        this.attachmentList = new ArrayList(attachmentList);
    }

    /**
     * @return the commentList
     */
    public List<ArticleComment> getCommentList() {
        return commentList;
    }

    /**
     * @param commentList the commentList to set
     */
    public void setCommentList(List<ArticleComment> commentList) {
        this.commentList = new ArrayList(commentList);
    }

    /**
     * @return the numOfComments
     */
    public int getNumOfComments() {
        return numOfComments;
    }

    /**
     * @param numOfComments the numOfComments to set
     */
    public void setNumOfComments(int numOfComments) {
        this.numOfComments = numOfComments;
    }

    /**
     * @return the roleList
     */
    public Map getRoles() {
        return roles;
    }

    /**
     * @param roleList the roleList to set
     */
    public void setRoles(Map roleList) {
        this.roles = new HashMap(roleList);
    }

    /**
     * @return the menuNodeDescription
     */
    public String getMenuNodeDescription() {
        return menuNodeDescription;
    }

    /**
     * @param menuNodeDescription the menuNodeDescription to set
     */
    public void setMenuNodeDescription(String menuNodeDescription) {
        this.menuNodeDescription = menuNodeDescription;
    }

    /**
     * @return the numOfReads
     */
    public int getNumOfReads() {
        return numOfReads;
    }

    /**
     * @param numOfReads the numOfReads to set
     */
    public void setNumOfReads(int numOfReads) {
        this.numOfReads = numOfReads;
    }
}
