/*
 *       BuildingUnitDeleteCmd.java
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
import cz.svjis.validator.Validator;

/**
 *
 * @author jaroslav_b
 */
public class BuildingUnitDeleteCmd extends Command {

    public BuildingUnitDeleteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        
        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        BuildingUnit u = buildingDao.getBuildingUnit(parId, buildingDao.getBuilding(getCompany().getId()).getId());
        if (u != null) {
            buildingDao.deleteBuildingUnit(u);
        }
        
        String url = "Dispatcher?page=buildingUnitList";
        getResponse().sendRedirect(url);
    }
}
