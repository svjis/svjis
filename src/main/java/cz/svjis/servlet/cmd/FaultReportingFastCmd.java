/*
 *       FaultReportingFastCmd.java
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
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingFastCmd extends FaultAbstractCmd {
    
    public FaultReportingFastCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        boolean parWatch = Validator.getBoolean(getRequest(), "watch");

        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        FaultReport f = faultDao.getFault(getCompany().getId(), parId);
        if (getUser().hasPermission("fault_reporting_resolver") && (f != null)) {
            
            if (getRequest().getParameter("takeTicket") != null) {
                f.setAssignedToUser(getUser());
                faultDao.modifyFault(f);
                faultDao.setUserWatchingFaultReport(f.getId(), f.getAssignedToUser().getId());
                logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_MODIFY_FAULT, f.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
            }
            if (getRequest().getParameter("closeTicket") != null) {
                f.setClosed(true);
                faultDao.modifyFault(f);
                sendNotification(f, getSetup().getMailTemplateFaultClosed(), faultDao.getUserListWatchingFaultReport(f.getId()));
                logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_CLOSE_FAULT, f.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
            }
        }
        
        if ((getRequest().getParameter("watch") != null) && (f != null)) {
            if (parWatch) {
                faultDao.setUserWatchingFaultReport(f.getId(), getUser().getId());
            } else {
                faultDao.unsetUserWatchingFaultReport(f.getId(), getUser().getId());
            }
        }

        String url = "Dispatcher?page=faultDetail&id=" + parId;
        getRequest().setAttribute("url", url);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
