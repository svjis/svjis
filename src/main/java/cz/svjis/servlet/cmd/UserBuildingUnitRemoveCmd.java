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
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

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

        int parUnitId = Validator.getInt(getRequest(), "unitId", 0, Validator.MAX_INT_ALLOWED, false);
        int parUserId = Validator.getInt(getRequest(), "userId", 0, Validator.MAX_INT_ALLOWED, false);

        BuildingDAO buildingDao = new BuildingDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());

        Building b = buildingDao.getBuilding(getCompany().getId());
        BuildingUnit unit = buildingDao.getBuildingUnit(parUnitId, buildingDao.getBuilding(getCompany().getId()).getId());
        User u = userDao.getUser(getCompany().getId(), parUserId);

        if ((unit != null) && (unit.getBuildingId() == b.getId()) && u != null) {
            buildingDao.deleteUserHasBuildingUnitConnection(parUserId, parUnitId);
        }

        String url = "Dispatcher?page=userBuildingUnits&id=" + parUserId;
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
