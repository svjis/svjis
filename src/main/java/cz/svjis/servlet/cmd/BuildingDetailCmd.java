/*
 *       BuildingDetailCmd.java
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

import cz.svjis.bean.Building;
import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.Permission;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class BuildingDetailCmd extends Command {

    public BuildingDetailCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.MENU_ADMINISTRATION)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        
        CompanyDAO compDao = new CompanyDAO(getCnn());
        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        if (currCompany == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        getRequest().setAttribute("currCompany", currCompany);
        
        Building building = buildingDao.getBuilding(getCompany().getId());
        if (building == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        getRequest().setAttribute("building", building);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Administration_buildingDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
