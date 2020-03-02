/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author berk
 */
public class Language implements Serializable {
    private int id;
    private String description;
    private Properties phrases = new Properties();
    
    public String getText(String Id) {
        if (phrases.getProperty(Id) == null) {
            return "?" + description + ": " + Id;
        } else {
            return this.phrases.getProperty(Id);
        }
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
     * @return the phrases
     */
    public Properties getPhrases() {
        return phrases;
    }

    /**
     * @param phrases the phrases to set
     */
    public void setPhrases(Properties phrases) {
        this.phrases = phrases;
    }
    
}
