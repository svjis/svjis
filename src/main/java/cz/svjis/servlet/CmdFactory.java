/*
 *       CmdFactory.java
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

import cz.svjis.bean.Permission;
import cz.svjis.servlet.cmd.*;


/**
 *
 * @author jaroslav_b
 */
public class CmdFactory {
    
    private CmdFactory() {}
    
    public static Command create(String page, CmdContext ctx) {

        // *****************
        // * Login         *
        // *****************
        if (page.equals(Cmd.LOGIN)) {
            return new LoginCmd(ctx);
        }
        
        if (page.equals(Cmd.LOGOUT)) {
            return new LogoutCmd(ctx);
        }

        // *****************
        // * Lost login    *
        // *****************
        if (page.equals(Cmd.LOST_PWD)) {
            return new LostPasswordCmd(ctx);
        }

        if (page.equals(Cmd.LOST_PWD_SUBMIT)) {
            return new LostPasswordSubmitCmd(ctx);
        }

        // *****************
        // * Article       *
        // *****************
        if (ctx.getUser().hasPermission(Permission.MENU_ARTICLES)) {

            if (page.equals(Cmd.ARTICLE_LIST) || page.equals(Cmd.ARTICLE_SEARCH)) {
                return new ArticleListCmd(ctx);
            }

            if (page.equals(Cmd.INQUIRY_VOTE)) {
                return new ArticleInquiryVoteCmd(ctx);
            }

            if (page.equals(Cmd.ARTICLE_DETAIL)) {
                return new ArticleDetailCmd(ctx);
            }

            if (page.equals(Cmd.ARTICLE_COMMENT)) {
                return new ArticleInsertCommentCmd(ctx);
            }
            
            if (page.equals(Cmd.ARTICLE_FAST_OP)) {
                return new ArticleFastCmd(ctx);
            }
        }

        // *****************
        // * Contact       *
        // *****************
        if (ctx.getUser().hasPermission(Permission.MENU_CONTACT)) {

            if (page.equals(Cmd.CONTACT)) {
                return new ContactCompanyCmd(ctx);
            }

            if (page.equals(Cmd.PHONE_LIST)) {
                return new ContactPhoneListCmd(ctx);
            }
        }

        // ***********************
        // * My bulilding units  *
        // ***********************
        if (ctx.getUser().hasPermission(Permission.MENU_BUILDING_UNITS)) {
            
            if (page.equals(Cmd.MY_BUILDING_UNITS)) {
                return new MyBuildingUnitsCmd(ctx);
            }
        }

        // **********************
        // * Personal settings  *
        // **********************
        if (ctx.getUser().hasPermission(Permission.MENU_PERSONAL_SETTINGS)) {
            
            if (page.equals(Cmd.PERSONAL_DETAIL)) {
                return new PersonalUserDetailCmd(ctx);
            }

            if (page.equals(Cmd.PERSONAL_DETAIL_SAVE)) {
                return new PersonalUserDetailSaveCmd(ctx);
            }

            if (page.equals(Cmd.PERSONAL_PW_CHANGE)) {
                return new PersonalPasswordChangeCmd(ctx);
            }
            if (page.equals(Cmd.PERSONAL_PW_CHANGE_SAVE)) {
                return new PersonalPasswordChangeSaveCmd(ctx);
            }
        }

        // *****************
        // * Redaction     *
        // *****************
        if (ctx.getUser().hasPermission(Permission.MENU_REDACTION)) {
            
            if (page.equals(Cmd.REDACTION_ARTICLE_LIST) || page.equals(Cmd.REDACTION_ARTICLE_SEARCH)) {
                return new RedactionArticleListCmd(ctx);
            }

            if (page.equals(Cmd.REDACTION_ARTICLE_EDIT)) {
                return new RedactionArticleEditCmd(ctx);
            }

            if (page.equals(Cmd.REDACTION_ARTICLE_SAVE)) {
                return new RedactionArticleSaveCmd(ctx);
            }

            if (page.equals(Cmd.REDACTION_ARTICLE_NOTIF)) {
                return new RedactionArticleSendNotificationsCmd(ctx);
            }

            if (page.equals(Cmd.REDACTION_ARTICLE_NOTIF_CONFIRMATION)) {
                return new RedactionArticleSendNotificationsConfirmationCmd(ctx);
            }

            if (page.equals(Cmd.REDACTION_ARTICLE_ATT_SAVE)) {
                return new RedactionArticleAttachmentSaveCmd(ctx);
            }
            if (page.equals(Cmd.REDACTION_ARTICLE_ATT_DELETE)) {
                return new RedactionArticleAttachmentDeleteCmd(ctx);
            }

            if (page.equals(Cmd.REDACTION_NEWS_LIST)) {
                return new RedactionNewsListCmd(ctx);
            }
            if (page.equals(Cmd.REDACTION_NEWS_EDIT)) {
                return new RedactionNewsEditCmd(ctx);
            }
            if (page.equals(Cmd.REDACTION_NEWS_SAVE)) {
                return new RedactionNewsEditSaveCmd(ctx);
            }
            if (page.equals(Cmd.REDACTION_NEWS_DELETE)) {
                return new RedactionNewsDeleteCmd(ctx);
            }

            if (page.equals(Cmd.REDACTION_INQUIRY_LIST)) {
                return new RedactionInquiryListCmd(ctx);
            }
            if (page.equals(Cmd.REDACTION_INQUIRY_LOG)) {
                return new RedactionInquiryLogCmd(ctx);
            }
            if (page.equals(Cmd.REDACTION_INQUIRY_EDIT)) {
                return new RedactionInquiryEditCmd(ctx);
            }
            if (page.equals(Cmd.REDACTION_INQUIRY_SAVE)) {
                return new RedactionInquirySaveCmd(ctx);
            }
            if (page.equals(Cmd.REDACTION_INQUIRY_OPT_DELETE)) {
                return new RedactionInquiryOptionDeleteCmd(ctx);
            }

            if (page.equals(Cmd.REDACTION_MENU)) {
                return new RedactionArticleMenuCmd(ctx);
            }
            if (page.equals(Cmd.REDACTION_MENU_EDIT)) {
                return new RedactionArticleMenuEditCmd(ctx);
            }
            if (page.equals(Cmd.REDACTION_MENU_SAVE)) {
                return new RedactionArticleMenuSaveCmd(ctx);
            }
            if (page.equals(Cmd.REDACTION_MENU_DELETE)) {
                return new RedactionArticleMenuDeleteCmd(ctx);
            }
        }
        
        // *******************
        // * Fault reporting *
        // *******************
        if (ctx.getUser().hasPermission(Permission.MENU_FAULT_REPORTING)) {
            
            if (page.equals(Cmd.FAULT_LIST) || page.equals(Cmd.FAULT_LIST_CREATED) || page.equals(Cmd.FAULT_LIST_ASSIGNED) || page.equals(Cmd.FAULT_LIST_CLOSED) || page.equals(Cmd.FAULT_LIST_SEARCH)) {
                return new FaultReportingListCmd(ctx);
            }

            if (page.equals(Cmd.FAULT_EDIT)) {
                return new FaultReportingEditCmd(ctx);
            }

            if (page.equals(Cmd.FAULT_SAVE)) {
                return new FaultReportingSaveCmd(ctx);
            }
            
            if (page.equals(Cmd.FAULT_DETAIL)) {
                return new FaultReportingDetailCmd(ctx);
            }
            
            if (page.equals(Cmd.FAULT_COMMENT)) {
                return new FaultReportingInsertCommentCmd(ctx);
            }
            
            if (page.equals(Cmd.FAULT_FAST_OP)) {
                return new FaultReportingFastCmd(ctx);
            }
            
            if (page.equals(Cmd.FAULT_ATT_SAVE)) {
                return new FaultReportingAttachmentSaveCmd(ctx);
            }
            
            if (page.equals(Cmd.FAULT_ATT_DELETE)) {
                return new FaultReportingAttachmentDeleteCmd(ctx);
            }
        }
        
        // *******************
        // * Adverts         *
        // *******************
        if (ctx.getUser().hasPermission(Permission.MENU_ADVERTS)) {
            
            if (page.equals(Cmd.ADVERT_LIST)) {
                return new AdvertListCmd(ctx);
            }
            
            if (page.equals(Cmd.ADVERT_EDIT)) {
                return new AdvertEditCmd(ctx);
            }
            
            if (page.equals(Cmd.ADVERT_SAVE)) {
                return new AdvertSaveCmd(ctx);
            }
            
            if (page.equals(Cmd.ADVERT_ATT_SAVE)) {
                return new AdvertAttachmentSaveCmd(ctx);
            }
            
            if (page.equals(Cmd.ADVERT_ATT_DELETE)) {
                return new AdvertAttachmentDeleteCmd(ctx);
            }
            
        }
        
        // ******************
        // * Administration *
        // ******************
        if (ctx.getUser().hasPermission(Permission.MENU_ADMINISTRATION)) {
            
            if (page.equals(Cmd.COMPANY_DETAIL)) {
                return new CompanyDetailCmd(ctx);
            }

            if (page.equals(Cmd.COMPANY_SAVE)) {
                return new CompanySaveCmd(ctx);
            }

            if (page.equals(Cmd.BUILDING_DETAIL)) {
                return new BuildingDetailCmd(ctx);
            }

            if (page.equals(Cmd.BUILDING_SAVE)) {
                return new BuildingSaveCmd(ctx);
            }

            if (page.equals(Cmd.BUILDING_PIC_SAVE)) {
                return new BuildingPictureSaveCmd(ctx);
            }

            if (page.equals(Cmd.BUILDING_ENT_LIST)) {
                return new BuildingEntranceListCmd(ctx);
            }
            
            if (page.equals(Cmd.BUILDING_ENT_EDIT)) {
                return new BuildingEntranceEditCmd(ctx);
            }
            
            if (page.equals(Cmd.BUILDING_ENT_SAVE)) {
                return new BuildingEntranceSaveCmd(ctx);
            }
            
            if (page.equals(Cmd.BUILDING_ENT_DELETE)) {
                return new BuildingEntranceDeleteCmd(ctx);
            }
            
            if (page.equals(Cmd.BUILDING_UNIT_LIST)) {
                return new BuildingUnitListCmd(ctx);
            }

            if (page.equals(Cmd.BUILDING_UNIT_EDIT)) {
                return new BuildingUnitEditCmd(ctx);
            }

            if (page.equals(Cmd.BUILDING_UNIT_SAVE)) {
                return new BuildingUnitSaveCmd(ctx);
            }

            if (page.equals(Cmd.BUILDING_UNIT_DELETE)) {
                return new BuildingUnitDeleteCmd(ctx);
            }

            if (page.equals(Cmd.BUILDING_UNIT_OWNER)) {
                return new BuildingUnitOwnerCmd(ctx);
            }
            
            if (page.equals(Cmd.BOARD_LIST)) {
                return new BoardListCmd(ctx);
            }
            
            if (page.equals(Cmd.BOARD_EDIT)) {
                return new BoardEditCmd(ctx);
            }
            
            if (page.equals(Cmd.BOARD_SAVE)) {
                return new BoardSaveCmd(ctx);
            }
            
            if (page.equals(Cmd.BOARD_DELETE)) {
                return new BoardDeleteCmd(ctx);
            }

            if (page.equals(Cmd.USER_LIST)) {
                return new UserListCmd(ctx);
            }

            if (page.equals(Cmd.USER_EDIT)) {
                return new UserEditCmd(ctx);
            }

            if (page.equals(Cmd.USER_SAVE)) {
                return new UserSaveCmd(ctx);
            }

            if (page.equals(Cmd.USER_DELETE)) {
                return new UserDeleteCmd(ctx);
            }

            if (page.equals(Cmd.USER_BU)) {
                return new UserBuildingUnitsCmd(ctx);
            }

            if (page.equals(Cmd.USER_BU_ADD)) {
                return new UserBuildingUnitAddCmd(ctx);
            }

            if (page.equals(Cmd.USER_BU_REMOVE)) {
                return new UserBuildingUnitRemoveCmd(ctx);
            }

            if (page.equals(Cmd.ROLE_LIST)) {
                return new RoleListCmd(ctx);
            }

            if (page.equals(Cmd.ROLE_EDIT)) {
                return new RoleEditCmd(ctx);
            }

            if (page.equals(Cmd.ROLE_SAVE)) {
                return new RoleSaveCmd(ctx);
            }

            if (page.equals(Cmd.ROLE_DELETE)) {
                return new RoleDeleteCmd(ctx);
            }

            if (page.equals(Cmd.PROPERTY_LIST)) {
                return new PropertyListCmd(ctx);
            }

            if (page.equals(Cmd.PROPERTY_EDIT)) {
                return new PropertyEditCmd(ctx);
            }

            if (page.equals(Cmd.PROPERTY_SAVE)) {
                return new PropertySaveCmd(ctx);
            }

            if (page.equals(Cmd.PROPERTY_DELETE)) {
                return new PropertyDeleteCmd(ctx);
            }

            if (page.equals(Cmd.MESSAGES)) {
                return new MessagesPendingCmd(ctx);
            }
        }

        // ************************
        // * Page does not exists *
        // ************************
        return new Error404NotFoundCmd(ctx);
    }
}
