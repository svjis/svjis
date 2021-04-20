/*
 *       Permission.java
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

/**
 *
 * @author berk
 */
public class Permission {
    
    public static final String MENU_ADMINISTRATION = "menu_administration";
    public static final String MENU_ARTICLES = "menu_articles";
    public static final String MENU_PHONE_LIST = "menu_phone_list";
    public static final String MENU_BUILDING_UNITS = "menu_building_units";
    public static final String MENU_PERSONAL_SETTINGS = "menu_personal_settings";
    public static final String MENU_REDACTION = "menu_redaction";
    public static final String MENU_CONTACT = "menu_contact";
    public static final String CAN_INSERT_ARTICLE_COMMENT = "can_insert_article_comment";
    public static final String CAN_VOTE_INQUIRY = "can_vote_inquiry";
    public static final String CAN_WRITE_HTML = "can_write_html";
    public static final String REDACTION_ARTICLES = "redaction_articles";
    public static final String REDACTION_ARTICLES_ALL = "redaction_articles_all";
    public static final String REDACTION_MINI_NEWS = "redaction_mini_news";
    public static final String REDACTION_INQUIRY = "redaction_inquiry";
    public static final String REDACTION_MENU = "redaction_menu";
    public static final String MENU_FAULT_REPORTING = "menu_fault_reporting";
    public static final String FAULT_REPORTING_REPORTER = "fault_reporting_reporter";
    public static final String FAULT_REPORTING_RESOLVER = "fault_reporting_resolver";
    public static final String FAULT_REPORTING_COMMENT = "fault_reporting_comment";
    public static final String MENU_ADVERTS = "menu_adverts";
    public static final String CAN_INSERT_ADVERT = "can_insert_advert";
    
    private int id;
    private String description;

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
    
}
