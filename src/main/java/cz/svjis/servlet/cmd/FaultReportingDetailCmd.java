/*
 *       FaultReportingDetailCmd.java
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
import cz.svjis.bean.FaultReportMenuCounters;
import cz.svjis.bean.LogDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingDetailCmd extends Command {
    
    public FaultReportingDetailCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        Validator.getString(getRequest(), "search", 0, 50, true, false);
        String parSearch = Validator.getString(getRequest(), "search", 0, Validator.MAX_STRING_LEN_ALLOWED, true, false);
        
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        FaultReport report = faultDao.getFault(getCompany().getId(), parId);
        if (report == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        getRequest().setAttribute("report", report);
        
        FaultReportMenuCounters counters = faultDao.getMenuCounters(getCompany().getId(), getUser().getId());
        getRequest().setAttribute("counters", counters);

        String watching = (faultDao.isUserWatchingFaultReport(report.getId(), getUser().getId())) ? "1" : "0";
        getRequest().setAttribute("watching", watching);
        
        getRequest().setAttribute("searchKey", parSearch);

        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Faults_reportDetail.jsp");
        rd.forward(getRequest(), getResponse());

        logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_READ_FAULT, report.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
    }
}
