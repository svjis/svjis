/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.BuildingEntrance;
import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.bean.FaultReportMenuCounters;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingEditCmd extends Command {
    
    public FaultReportingEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        
        BuildingDAO buildingDao = new BuildingDAO(getCnn());
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        
        FaultReport report = new FaultReport();
        int id = parId;
        if ((id != 0) && getUser().hasPermission("fault_reporting_resolver")) {
            report = faultDao.getFault(getCompany().getId(), id);
        }
        getRequest().setAttribute("report", report);
        
        ArrayList<User> resolverList = new ArrayList(userDao.getUserListWithPermission(getCompany().getId(), "fault_reporting_resolver"));
        getRequest().setAttribute("resolverList", resolverList);
        
        ArrayList<BuildingEntrance> entranceList = new ArrayList(buildingDao.getBuildingEntranceList(buildingDao.getBuilding(getCompany().getId()).getId()));
        getRequest().setAttribute("entranceList", entranceList);
        
        FaultReportMenuCounters counters = faultDao.getMenuCounters(getCompany().getId(), getUser().getId());
        getRequest().setAttribute("counters", counters);
                
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Faults_reportEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
