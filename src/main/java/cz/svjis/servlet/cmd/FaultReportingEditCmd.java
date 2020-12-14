/*
 *       FaultReportingEditCmd.java
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
                
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Faults_reportEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
