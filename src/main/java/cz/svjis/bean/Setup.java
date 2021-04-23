/*
 *       Setup.java
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jarberan
 */
public class Setup implements Serializable {
    
    private static final long serialVersionUID = 3626374526742634517L;
    private static final Logger LOGGER = Logger.getLogger(Setup.class.getName());
    
    private static final String ADVERT_MENU_DEFAULT_KEY = "advert.menu.default";
    private static final int ADVERT_MENU_DEFAULT = 10;
    private static final String ANONIMOUS_USER_ID_KEY = "anonymous.user.id";
    private static final int ANONIMOUS_USER_ID_DEFAULT = 0;
    private static final String ARTICLE_MENU_DEFAULT_ITEM_KEY = "article.menu.default.item";
    private static final int ARTICLE_MENU_DEFAULT_ITEM_DEFAULT = 0;
    private static final String ARTICLE_PAGE_SIZE_KEY = "article.page.size";
    private static final int ARTICLE_PAGE_SIZE_DEFAULT = 10;
    private static final String ARTICLE_TOP_MONTHS_KEY = "article.top.months";
    private static final int ARTICLE_TOP_MONTHS_DEFAULT = 12;
    private static final String ARTICLE_TOP_SIZE_KEY = "article.top.size";
    private static final int ARTICLE_TOP_SIZE_DEFAULT = 10;
    private static final String ERROR_REPORT_RECIPIENT_KEY = "error.report.recipient";
    private static final String ERROR_REPORT_RECIPIENT_DEFAULT = "";
    private static final String FAULT_PAGE_SIZE_KEY = "faults.page.size";
    private static final int FAULT_PAGE_SIZE_DEFAULT = 10;
    private static final String GOOGLE_ANALYTICS_ID_KEY = "google.analytics.id";
    private static final String GOOGLE_ANALYTICS_ID_DEFAULT = "";
    private static final String HTTP_META_DESCRIPTION_KEY = "http.meta.description";
    private static final String HTTP_META_DESCRIPTION_DEFAULT = "";
    private static final String HTTP_META_KEYWORDS_KEY = "http.meta.keywords";
    private static final String HTTP_META_KEYWORDS_DEFAULT = "";
    private static final String MAIL_LOGIN_KEY = "mail.login";
    private static final String MAIL_LOGIN_DEFAULT = "";
    private static final String MAIL_PASSWORD_KEY = "mail.password";
    private static final String MAIL_PASSWORD_DEFAULT = "";
    private static final String MAIL_SENDER_KEY = "mail.sender";
    private static final String MAIL_SENDER_DEFAULT = "";
    private static final String MAIL_SMTP_KEY = "mail.smtp";
    private static final String MAIL_SMTP_DEFAULT = "";
    private static final String MAIL_SMTP_SSL_KEY = "mail.smtp.ssl";
    private static final boolean MAIL_SMTP_SSL_DEFAULT = false;
    private static final String MAIL_SMTP_PORT_KEY = "mail.smtp.port";
    private static final int MAIL_SMTP_PORT_DEFAULT = 25;
    private static final String MAIL_TEMPLATE_ARTICLE_NOTIFICATION_KEY = "mail.template.article.notification";
    private static final String MAIL_TEMPLATE_ARTICLE_NOTIFICATION_DEFAULT = "";
    private static final String MAIL_TEMPLATE_COMMENT_NOTIFICATION_KEY = "mail.template.comment.notification";
    private static final String MAIL_TEMPLATE_COMMENT_NOTIFICATION_DEFAULT = "";
    private static final String MAIL_TEMPLATE_FAULT_ASSIGNED_KEY = "mail.template.fault.assigned";
    private static final String MAIL_TEMPLATE_FAULT_ASSIGNED_DEFAULT = "";
    private static final String MAIL_TEMPLATE_FAULT_CLOSED_KEY = "mail.template.fault.closed";
    private static final String MAIL_TEMPLATE_FAULT_CLOSED_DEFAULT = "";
    private static final String MAIL_TEMPLATE_FAULT_COMMENT_KEY = "mail.template.fault.comment.notification";
    private static final String MAIL_TEMPLATE_FAULT_COMMENT_DEFAULT = "";
    private static final String MAIL_TEMPLATE_FAULT_NOTIFICATION_KEY = "mail.template.fault.notification";
    private static final String MAIL_TEMPLATE_FAULT_NOTIFICATION_DEFAULT = "";
    private static final String MAIL_TEMPLATE_FAULT_REOPENED_KEY = "mail.template.fault.reopened";
    private static final String MAIL_TEMPLATE_FAULT_REOPENED_DEFAULT = "";
    private static final String MAIL_TEMPLATE_LOST_PASSWORD_KEY = "mail.template.lost.password";
    private static final String MAIL_TEMPLATE_LOST_PASSWORD_DEFAULT = "<html><body>Dobrý den,<br>Vaše přihlašovací údaje jsou:<br><br>%s<br>Heslo si můžete změnit v menu <b>Osobní nastavení - Změna hesla</b><br><br>Web SVJ</body></html>";
    private static final String PERMANENT_LOGIN_HOURS_KEY = "permanent.login.hours";
    private static final int PERMANENT_LOGIN_HOURS_DEFAULT = 336;
    
    private Properties props;
    
    public Setup() {
        this.props = new Properties();
    }
    
    public Setup(Properties setup) {
        this.props = setup;
    }
    
    /**
     * 
     * @return Id of user used for anonymous access
     */
    public int getAdvertMenuDefault() {
        return getIntValue(Setup.ADVERT_MENU_DEFAULT_KEY, Setup.ADVERT_MENU_DEFAULT);
    }
    
    /**
     * 
     * @return Id of user used for anonymous access
     */
    public int getAnonymousUserId() {
        return getIntValue(Setup.ANONIMOUS_USER_ID_KEY, Setup.ANONIMOUS_USER_ID_DEFAULT);
    }
    
    /**
     * 
     * @return Id of menu item selected by default (0 means not item is selected by default)
     */
    public int getMenuDefaultItem() {
        return getIntValue(Setup.ARTICLE_MENU_DEFAULT_ITEM_KEY, Setup.ARTICLE_MENU_DEFAULT_ITEM_DEFAULT);
    }
    
    /**
     * 
     * @return number of articles at page
     */
    public int getArticlePageSize() {
        return getIntValue(Setup.ARTICLE_PAGE_SIZE_KEY, Setup.ARTICLE_PAGE_SIZE_DEFAULT);
    }
    
    /**
     * 
     * @return number of months for which top articles are calculated
     */
    public int getArticleTopMonths() {
        return getIntValue(Setup.ARTICLE_TOP_MONTHS_KEY, Setup.ARTICLE_TOP_MONTHS_DEFAULT);
    }
    
    /**
     * 
     * @return number of articles in top articles section
     */
    public int getArticleTopSize() {
        return getIntValue(Setup.ARTICLE_TOP_SIZE_KEY, Setup.ARTICLE_TOP_SIZE_DEFAULT);
    }
    
    /**
     * 
     * @return e-mail address of recipient for error reports
     */
    public String getErrorReportRecipient() {
        return getStringValue(Setup.ERROR_REPORT_RECIPIENT_KEY, Setup.ERROR_REPORT_RECIPIENT_DEFAULT);
    }
    
    /**
     * 
     * @return number of faults at page
     */
    public int getFaultPageSize() {
        return getIntValue(Setup.FAULT_PAGE_SIZE_KEY, Setup.FAULT_PAGE_SIZE_DEFAULT);
    }
    
    /**
     * 
     * @return google analytics id
     */
    public String getGoogleAnalyticsId() {
        return getStringValue(Setup.GOOGLE_ANALYTICS_ID_KEY, Setup.GOOGLE_ANALYTICS_ID_DEFAULT);
    }
    
    /**
     * 
     * @return meta description for web
     */
    public String getHttpMetaDescription() {
        return getStringValue(Setup.HTTP_META_DESCRIPTION_KEY, Setup.HTTP_META_DESCRIPTION_DEFAULT);
    }
    
    /**
     * 
     * @return meta keywords for web
     */
    public String getHttpMetaKeywords() {
        return getStringValue(Setup.HTTP_META_KEYWORDS_KEY, Setup.HTTP_META_KEYWORDS_DEFAULT);
    }
    
    /**
     * 
     * @return login for smtp server
     */
    public String getMailLogin() {
        return getStringValue(Setup.MAIL_LOGIN_KEY, Setup.MAIL_LOGIN_DEFAULT);
    }
    
    /**
     * 
     * @return password for smtp server
     */
    public String getMailPassword() {
        return getStringValue(Setup.MAIL_PASSWORD_KEY, Setup.MAIL_PASSWORD_DEFAULT);
    }
    
    /**
     * 
     * @return e-mail address of sender for notifications
     */
    public String getMailSender() {
        return getStringValue(Setup.MAIL_SENDER_KEY, Setup.MAIL_SENDER_DEFAULT);
    }
    
    /**
     * 
     * @return address of smtp server
     */
    public String getMailSmtp() {
        return getStringValue(Setup.MAIL_SMTP_KEY, Setup.MAIL_SMTP_DEFAULT);
    }
    
    /**
     * 
     * @return smtpSSL option
     */
    public boolean getMailSmtpSSL() {
        return getBooleanValue(Setup.MAIL_SMTP_SSL_KEY, Setup.MAIL_SMTP_SSL_DEFAULT);
    }
    
    /**
     * 
     * @return port of smtp server
     */
    public int getMailSmtpPort() {
        return getIntValue(Setup.MAIL_SMTP_PORT_KEY, Setup.MAIL_SMTP_PORT_DEFAULT);
    }
    
    /**
     * 
     * @return new article notification template
     */
    public String getMailTemplateArticleNotification() {
        return getStringValue(Setup.MAIL_TEMPLATE_ARTICLE_NOTIFICATION_KEY, Setup.MAIL_TEMPLATE_ARTICLE_NOTIFICATION_DEFAULT);
    }
    
    /**
     * 
     * @return new article comment notification template
     */
    public String getMailTemplateCommentNotification() {
        return getStringValue(Setup.MAIL_TEMPLATE_COMMENT_NOTIFICATION_KEY, Setup.MAIL_TEMPLATE_COMMENT_NOTIFICATION_DEFAULT);
    }
    
    /**
     * 
     * @return fault assignment notification template
     */
    public String getMailTemplateFaultAssigned() {
        return getStringValue(Setup.MAIL_TEMPLATE_FAULT_ASSIGNED_KEY, Setup.MAIL_TEMPLATE_FAULT_ASSIGNED_DEFAULT);
    }
    
    /**
     * 
     * @return fault closure notification template
     */
    public String getMailTemplateFaultClosed() {
        return getStringValue(Setup.MAIL_TEMPLATE_FAULT_CLOSED_KEY, Setup.MAIL_TEMPLATE_FAULT_CLOSED_DEFAULT);
    }
    
    /**
     * 
     * @return new fault comment notification template
     */
    public String getMailTemplateFaultComment() {
        return getStringValue(Setup.MAIL_TEMPLATE_FAULT_COMMENT_KEY, Setup.MAIL_TEMPLATE_FAULT_COMMENT_DEFAULT);
    }
    
    /**
     * 
     * @return new fault notification template
     */
    public String getMailTemplateFaultNotification() {
        return getStringValue(Setup.MAIL_TEMPLATE_FAULT_NOTIFICATION_KEY, Setup.MAIL_TEMPLATE_FAULT_NOTIFICATION_DEFAULT);
    }
    
    /**
     * 
     * @return fault reopening notification template
     */
    public String getMailTemplateFaultReopened() {
        return getStringValue(Setup.MAIL_TEMPLATE_FAULT_REOPENED_KEY, Setup.MAIL_TEMPLATE_FAULT_REOPENED_DEFAULT);
    }
    
    /**
     * 
     * @return lost password e-mail template
     */
    public String getMailTemplateLostPassword() {
        return getStringValue(Setup.MAIL_TEMPLATE_LOST_PASSWORD_KEY, Setup.MAIL_TEMPLATE_LOST_PASSWORD_DEFAULT);
    }
    
    /**
     * 
     * @return permanent login ttl in hours
     */
    public int getPermanentLoginInHours() {
        return getIntValue(Setup.PERMANENT_LOGIN_HOURS_KEY, Setup.PERMANENT_LOGIN_HOURS_DEFAULT);
    }
    
    /**
     * @return the setup
     */
    public Properties getSetupProps() {
        return props;
    }
    
    private boolean getBooleanValue(String key, boolean defaultValue) {
        boolean result = defaultValue;
        
        if (props.getProperty(key) != null) {
            if (props.getProperty(key).equals("true")) {
                result = true;
            } else {
                result = false;
            }
        }
        
        return result;
    }
    
    private int getIntValue(String key, int defaultValue) {
        int result = defaultValue;
        
        if (props.getProperty(key) != null) {
            try {
                result = Integer.valueOf(props.getProperty(key));
            } catch (java.lang.NumberFormatException ex) {
                LOGGER.log(Level.WARNING, "Could not convert string to integer", ex);
            }
        }
        
        return result;
    }
    
    private String getStringValue(String key, String defaultValue) {
        String result = defaultValue;
        
        if (props.getProperty(key) != null) {
            result = props.getProperty(key);
        }
        
        return result;
    }
}
