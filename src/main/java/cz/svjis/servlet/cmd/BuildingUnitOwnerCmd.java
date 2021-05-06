/*
 *       BuildingUnitOwnerCmd.java
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
import cz.svjis.bean.BuildingUnit;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.Permission;
import cz.svjis.bean.User;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class BuildingUnitOwnerCmd extends Command {

    public BuildingUnitOwnerCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.MENU_ADMINISTRATION)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        CompanyDAO compDao = new CompanyDAO(getCnn());
        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        
        BuildingUnit buildingUnit = buildingDao.getBuildingUnit(parId, buildingDao.getBuilding(getCompany().getId()).getId());
        if (buildingUnit == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        getRequest().setAttribute("buildingUnit", buildingUnit);
        
        ArrayList<User> userList = new ArrayList<>(buildingDao.getBuildingUnitHasUserList(parId));
        getRequest().setAttribute("userList", userList);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Administration_buildingUnitOwner.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
