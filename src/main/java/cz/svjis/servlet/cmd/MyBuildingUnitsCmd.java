/*
 *       MyBuildingUnitsCmd.java
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
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class MyBuildingUnitsCmd extends Command {

    public MyBuildingUnitsCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        BuildingDAO buildingDao = new BuildingDAO(getCnn());
        
        ArrayList<BuildingUnit> userHasUnitList = new ArrayList(buildingDao.getUserHasBuildingUnitList(getUser().getId()));
        getRequest().setAttribute("userHasUnitList", userHasUnitList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Units_userUnitList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
