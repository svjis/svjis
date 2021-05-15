/*
 *       CmdFactoryUpload.java
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
import cz.svjis.servlet.cmd.AdvertAttachmentDownloadCmd;
import cz.svjis.servlet.cmd.BuildingPictureCmd;
import cz.svjis.servlet.cmd.DownloadCmd;
import cz.svjis.servlet.cmd.ExportBuildingUnitListToXlsCmd;
import cz.svjis.servlet.cmd.ExportInquiryLogToXlsCmd;
import cz.svjis.servlet.cmd.ExportUserListToXlsCmd;
import cz.svjis.servlet.cmd.FaultReportingDownloadCmd;

/**
 *
 * @author jaroslav_b
 */
public class CmdFactoryUpload {
    
    private CmdFactoryUpload() {}

    public static Command create(String page, CmdContext ctx) {
        
        if (page.equals(Cmd.ARTICLE_ATT_DOWNLOAD)) {
            return new DownloadCmd(ctx);
        }

        if (page.equals(Cmd.BUILDING_PIC_GET)) {
            return new BuildingPictureCmd(ctx);
        }
        
        // ******************
        // * Administration *
        // ******************
        if (ctx.getUser().hasPermission(Permission.MENU_ADMINISTRATION)) {
            
            if (page.equals(Cmd.BUILDING_UNIT_XLS)) {
                return new ExportBuildingUnitListToXlsCmd(ctx);
            }
            
            if (page.equals(Cmd.USER_XLS)) {
                return new ExportUserListToXlsCmd(ctx);
            }
        }
        
        // *******************
        // * Fault reporting *
        // *******************
        if (ctx.getUser().hasPermission(Permission.MENU_FAULT_REPORTING)) {
            if (page.equals(Cmd.FAULT_ATT_DOWNLOAD)) {
                return new FaultReportingDownloadCmd(ctx);
            }
        }
        
        // *******************
        // * Adverts         *
        // *******************
        if (ctx.getUser().hasPermission(Permission.MENU_ADVERTS)) {
            if (page.equals(Cmd.ADVERT_ATT_DOWNLOAD)) {
                return new AdvertAttachmentDownloadCmd(ctx);
            }
        }
        
        // *****************
        // * Redaction     *
        // *****************
        if (ctx.getUser().hasPermission(Permission.MENU_REDACTION)) {
            if (page.equals(Cmd.REDACTION_INQUIRY_XLS)) {
                return new ExportInquiryLogToXlsCmd(ctx);
            }
        }
        
        return null;
    }
}
