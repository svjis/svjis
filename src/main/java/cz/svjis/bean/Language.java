/*
 *       Language.java
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

import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author berk
 */
public class Language implements Serializable {

    private static final long serialVersionUID = -4665070584674500953L;
    
    private int id;
    private String description;
    private Properties phrases = new Properties();
    
    public String getText(String id) {
        if (phrases.getProperty(id) == null) {
            return "?" + description + ": " + id;
        } else {
            return this.phrases.getProperty(id);
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
