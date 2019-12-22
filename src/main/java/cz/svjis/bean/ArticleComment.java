/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.util.Date;

/**
 *
 * @author berk
 */
public class ArticleComment {
    private int id;
    private int articleId;
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
     * @return the articleId
     */
    public int getArticleId() {
        return articleId;
    }

    /**
     * @param articleId the articleId to set
     */
    public void setArticleId(int articleId) {
        this.articleId = articleId;
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
}
