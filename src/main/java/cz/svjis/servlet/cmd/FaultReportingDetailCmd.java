/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.bean.FaultReportMenuCounters;
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
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);
        Validator.getString(getRequest(), "search", 0, 50, true, false);
        
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        FaultReport report = faultDao.getFault(getCompany().getId(), parId);
        getRequest().setAttribute("report", report);
        
        FaultReportMenuCounters counters = faultDao.getMenuCounters(getCompany().getId(), getUser().getId());
        getRequest().setAttribute("counters", counters);

        String watching = (faultDao.isUserWatchingFaultReport(report.getId(), getUser().getId())) ? "1" : "0";
        getRequest().setAttribute("watching", watching);

        RequestDispatcher rd = getRequest().getRequestDispatcher("/Faults_reportDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
