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
import cz.svjis.bean.FaultReportAttachment;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

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

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        int id = parId;
        FaultReportAttachment fa = faultDao.getFaultReportAttachment(id);
        if (fa == null) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        FaultReport f = faultDao.getFault(getCompany().getId(), fa.getFaultReportId());
        if ((f != null) && (!f.isClosed()) && (fa.getUser().getId() == getUser().getId())) {
            faultDao.deleteFaultAttachment(id);
            logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_DELETE_FAULT_ATTACHMENT, f.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
        }
        String url = "Dispatcher?page=faultDetail&id=" + fa.getFaultReportId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
