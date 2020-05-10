/*
 *       BuildingEntranceEditCmd.java
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
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class BuildingEntranceEditCmd extends Command {
    
    public BuildingEntranceEditCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        CompanyDAO compDao = new CompanyDAO(getCnn());
        BuildingDAO buildingDao = new BuildingDAO(getCnn());
        
        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        BuildingEntrance buildingEntrance;
        int id = parId;
        if (id == 0) {
            buildingEntrance = new BuildingEntrance();
            buildingEntrance.setBuildingId(buildingDao.getBuilding(getCompany().getId()).getId());
        } else {
            buildingEntrance = buildingDao.getBuildingEntrance(id);
        }
        getRequest().setAttribute("buildingEntrance", buildingEntrance);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_buildingEntranceDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
