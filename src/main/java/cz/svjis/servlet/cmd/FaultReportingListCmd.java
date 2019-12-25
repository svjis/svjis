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
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingListCmd extends Command {
    
    public FaultReportingListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        String parPage = getRequest().getParameter("page");
        
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        
        ArrayList<FaultReport> reportList = new ArrayList<FaultReport>();
        if (parPage.equals("faultReportingList")) {
            reportList = faultDao.getFaultList(getCompany().getId(), 0);
        }
        if (parPage.equals("faultReportingListCreatedByMe")) {
            reportList = faultDao.getFaultListByCreator(getCompany().getId(), getUser().getId());
        }
        if (parPage.equals("faultReportingListAssignedToMe")) {
            reportList = faultDao.getFaultListByResolver(getCompany().getId(), getUser().getId());
        }
        if (parPage.equals("faultReportingListClosed")) {
            reportList = faultDao.getFaultList(getCompany().getId(), 1);
        }

        getRequest().setAttribute("reportList", reportList);
        
        FaultReportMenuCounters counters = faultDao.getMenuCounters(getCompany().getId(), getUser().getId());
        getRequest().setAttribute("counters", counters);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Faults_reportList.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
}
