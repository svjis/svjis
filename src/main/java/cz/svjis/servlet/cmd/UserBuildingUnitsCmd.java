/*
 *       UserBuildingUnitsCmd.java
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
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class UserBuildingUnitsCmd extends Command {

    public UserBuildingUnitsCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        CompanyDAO compDao = new CompanyDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        User cUser = userDao.getUser(getCompany().getId(), parId);
        getRequest().setAttribute("cUser", cUser);
        ArrayList<BuildingUnit> userHasUnitList = new ArrayList<>(buildingDao.getUserHasBuildingUnitList(cUser.getId()));
        getRequest().setAttribute("userHasUnitList", userHasUnitList);
        ArrayList<BuildingUnit> unitList = new ArrayList<>(buildingDao.getBuildingUnitList(getCompany().getId(), 0));
        getRequest().setAttribute("unitList", unitList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Administration_userUnits.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
