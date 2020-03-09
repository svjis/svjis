/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet;

import cz.svjis.servlet.cmd.BuildingPictureCmd;
import cz.svjis.servlet.cmd.DownloadCmd;
import cz.svjis.servlet.cmd.ExportBuildingUnitListToXlsCmd;
import cz.svjis.servlet.cmd.ExportUserListToXlsCmd;
import cz.svjis.servlet.cmd.FaultReportingDownloadCmd;

/**
 *
 * @author jaroslav_b
 */
public class CmdFactoryUpload {
    
    private CmdFactoryUpload() {}

    public static Command create(String page, CmdContext ctx) {
        
        if (page.equals("download")) {
            return new DownloadCmd(ctx);
        }

        if (page.equals("getBuildingPicture")) {
            return new BuildingPictureCmd(ctx);
        }
        
        // ******************
        // * Administration *
        // ******************
        if (ctx.getUser().hasPermission("menu_administration")) {
            
            if (page.equals("exportBuildingUnitListToXls")) {
                return new ExportBuildingUnitListToXlsCmd(ctx);
            }
            
            if (page.equals("exportUserListToXls")) {
                return new ExportUserListToXlsCmd(ctx);
            }
        }
        
        // *******************
        // * Fault reporting *
        // *******************
        if (ctx.getUser().hasPermission("menu_fault_reporting")) {
            if (page.equals("faultReportingDownload")) {
                return new FaultReportingDownloadCmd(ctx);
            }
        }
        
        return null;
    }
}
