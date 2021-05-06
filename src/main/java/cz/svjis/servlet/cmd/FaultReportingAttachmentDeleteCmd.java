/*
 *       FaultReportingAttachmentDeleteCmd.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.servlet.cmd;

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.Attachment;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.Permission;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

/**
 *
 * @author jarberan
 */
public class FaultReportingAttachmentDeleteCmd extends Command {
    
    public FaultReportingAttachmentDeleteCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.MENU_FAULT_REPORTING)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        int id = parId;
        Attachment fa = faultDao.getFaultReportAttachment(id);
        if (fa == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        
        FaultReport f = faultDao.getFault(getCompany().getId(), fa.getDocumentId());
        if (f == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        
        if (!f.isClosed() && (fa.getUser().getId() == getUser().getId())) {
            faultDao.deleteFaultAttachment(id);
            logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_DELETE_FAULT_ATTACHMENT, f.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
        }
        String url = "Dispatcher?page=faultDetail&id=" + fa.getDocumentId();
        getResponse().sendRedirect(url);
    }
}
