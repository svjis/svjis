/*
 *       UserBuildingUnitRemoveCmd.java
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
import cz.svjis.bean.BuildingUnit;
import cz.svjis.bean.Permission;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

/**
 *
 * @author jaroslav_b
 */
public class UserBuildingUnitRemoveCmd extends Command {

    public UserBuildingUnitRemoveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.MENU_ADMINISTRATION)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }

        int parUnitId = Validator.getInt(getRequest(), "unitId", 0, Validator.MAX_INT_ALLOWED, false);
        int parUserId = Validator.getInt(getRequest(), "userId", 0, Validator.MAX_INT_ALLOWED, false);

        BuildingDAO buildingDao = new BuildingDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());

        Building b = buildingDao.getBuilding(getCompany().getId());
        if (b == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        
        BuildingUnit unit = buildingDao.getBuildingUnit(parUnitId, buildingDao.getBuilding(getCompany().getId()).getId());
        if (unit == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        
        User u = userDao.getUser(getCompany().getId(), parUserId);
        if (u == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }

        if (unit.getBuildingId() == b.getId()) {
            buildingDao.deleteUserHasBuildingUnitConnection(parUserId, parUnitId);
        }

        String url = "Dispatcher?page=userBuildingUnits&id=" + parUserId;
        getResponse().sendRedirect(url);
    }
}
