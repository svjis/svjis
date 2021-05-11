/*
 *       Cmd.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.servlet;


public class Cmd {

    
    // *****************
    // * Login         *
    // *****************
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";

    // *****************
    // * Lost login    *
    // *****************
    public static final String LOST_PWD = "lostPassword";
    public static final String LOST_PWD_SUBMIT = "lostPassword_submit";

    // *****************
    // * Article       *
    // *****************
    public static final String ARTICLE_LIST = "articleList";
    public static final String ARTICLE_SEARCH = "search";
    public static final String INQUIRY_VOTE = "inquiryVote";
    public static final String ARTICLE_DETAIL = "articleDetail";
    public static final String ARTICLE_COMMENT = "insertArticleComment";
    public static final String ARTICLE_FAST_OP = "articleFast";
    public static final String ARTICLE_ATT_DOWNLOAD = "download";

    // *****************
    // * Contact       *
    // *****************
    public static final String CONTACT = "contact";
    public static final String PHONE_LIST = "phonelist";

    // ***********************
    // * My bulilding units  *
    // ***********************
    public static final String MY_BUILDING_UNITS = "myBuildingUnitList";

    // **********************
    // * Personal settings  *
    // **********************
    public static final String PERSONAL_DETAIL = "psUserDetail";
    public static final String PERSONAL_DETAIL_SAVE = "psUserDetailSave";
    public static final String PERSONAL_PW_CHANGE = "psPasswordChange";
    public static final String PERSONAL_PW_CHANGE_SAVE = "psPasswordChangeSave";

    // *****************
    // * Redaction     *
    // *****************
    public static final String REDACTION_ARTICLE_LIST = "redactionArticleList";
    public static final String REDACTION_ARTICLE_SEARCH = "redactionArticleSearch";
    public static final String REDACTION_ARTICLE_EDIT = "redactionArticleEdit";
    public static final String REDACTION_ARTICLE_SAVE = "redactionArticleSave";
    public static final String REDACTION_ARTICLE_NOTIF = "redactionArticleSendNotifications";
    public static final String REDACTION_ARTICLE_NOTIF_CONFIRMATION = "redactionArticleSendNotificationsConfirmation";
    public static final String REDACTION_ARTICLE_ATT_SAVE = "redactionArticleAttachmentSave";
    public static final String REDACTION_ARTICLE_ATT_DELETE = "redactionArticleAttachmentDelete";
    public static final String REDACTION_NEWS_LIST = "redactionNewsList";
    public static final String REDACTION_NEWS_EDIT = "redactionNewsEdit";
    public static final String REDACTION_NEWS_SAVE = "redactionNewsEditSave";
    public static final String REDACTION_NEWS_DELETE = "redactionNewsDelete";
    public static final String REDACTION_INQUIRY_LIST = "redactionInquiryList";
    public static final String REDACTION_INQUIRY_LOG = "redactionInquiryLog";
    public static final String REDACTION_INQUIRY_XLS = "exportInquiryLogToXls";
    public static final String REDACTION_INQUIRY_EDIT = "redactionInquiryEdit";
    public static final String REDACTION_INQUIRY_SAVE = "redactionInquirySave";
    public static final String REDACTION_INQUIRY_OPT_DELETE = "redactionInquiryOptionDelete";
    public static final String REDACTION_MENU = "redactionArticleMenu";
    public static final String REDACTION_MENU_EDIT = "redactionArticleMenuEdit";
    public static final String REDACTION_MENU_SAVE = "redactionArticleMenuSave";
    public static final String REDACTION_MENU_DELETE = "redactionArticleMenuDelete";
    
    // *******************
    // * Fault reporting *
    // *******************
    public static final String FAULT_LIST = "faultReportingList";
    public static final String FAULT_LIST_CREATED = "faultReportingListCreatedByMe";
    public static final String FAULT_LIST_CLOSED = "faultReportingListClosed";
    public static final String FAULT_EDIT = "faultReportingEdit";
    public static final String FAULT_SAVE = "faultReportingSave";
    public static final String FAULT_DETAIL = "faultDetail";
    public static final String FAULT_COMMENT = "faultInsertComment";
    public static final String FAULT_FAST_OP = "faultReportingFast";
    public static final String FAULT_ATT_SAVE = "faultReportingAttachmentSave";
    public static final String FAULT_ATT_DELETE = "faultReportingAttachmentDelete";
    public static final String FAULT_ATT_DOWNLOAD = "faultReportingDownload";
    
    // *******************
    // * Adverts         *
    // *******************
    public static final String ADVERT_LIST = "advertList";
    public static final String ADVERT_EDIT = "advertEdit";
    public static final String ADVERT_SAVE = "advertSave";
    public static final String ADVERT_ATT_SAVE = "advertAttachmentSave";
    public static final String ADVERT_ATT_DELETE = "advertAttachmentDelete";
    public static final String ADVERT_ATT_DOWNLOAD = "advertAttachmentDownload";
    
    // ******************
    // * Administration *
    // ******************
    public static final String COMPANY_DETAIL = "companyDetail";
    public static final String COMPANY_SAVE = "companySave";
    public static final String BUILDING_DETAIL = "buildingDetail";
    public static final String BUILDING_SAVE = "buildingSave";
    public static final String BUILDING_PIC_SAVE = "buildingPictureSave";
    public static final String BUILDING_PIC_GET = "getBuildingPicture";
    public static final String BUILDING_ENT_LIST = "buildingEntranceList";
    public static final String BUILDING_ENT_EDIT = "buildingEntranceEdit";
    public static final String BUILDING_ENT_SAVE = "buildingEntranceSave";
    public static final String BUILDING_ENT_DELETE = "buildingEntranceDelete";
    public static final String BUILDING_UNIT_LIST = "buildingUnitList";
    public static final String BUILDING_UNIT_EDIT = "buildingUnitEdit";
    public static final String BUILDING_UNIT_SAVE = "buildingUnitSave";
    public static final String BUILDING_UNIT_DELETE = "buildingUnitDelete";
    public static final String BUILDING_UNIT_OWNER = "buildingUnitOwner";
    public static final String BUILDING_UNIT_XLS = "exportBuildingUnitListToXls";
    public static final String BOARD_LIST = "boardMemberList";
    public static final String BOARD_EDIT = "boardMemberEdit";
    public static final String BOARD_SAVE = "boardMemberSave";
    public static final String BOARD_DELETE = "boardMemberDelete";
    public static final String USER_LIST = "userList";
    public static final String USER_EDIT = "userEdit";
    public static final String USER_SAVE = "userSave";
    public static final String USER_DELETE = "userDelete";
    public static final String USER_BU = "userBuildingUnits";
    public static final String USER_BU_ADD = "userBuildingUnitAdd";
    public static final String USER_BU_REMOVE = "userBuildingUnitRemove";
    public static final String USER_XLS = "exportUserListToXls";
    public static final String ROLE_LIST = "roleList";
    public static final String ROLE_EDIT = "roleEdit";
    public static final String ROLE_SAVE = "roleSave";
    public static final String ROLE_DELETE = "roleDelete";
    public static final String PROPERTY_LIST = "propertyList";
    public static final String PROPERTY_EDIT = "propertyEdit";
    public static final String PROPERTY_SAVE = "propertySave";
    public static final String PROPERTY_DELETE = "propertyDelete";
    public static final String MESSAGES = "messagesPending";

}
