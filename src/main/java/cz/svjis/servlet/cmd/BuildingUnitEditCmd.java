/*
 *       BuildingUnitEditCmd.java
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
import cz.svjis.bean.BuildingUnitType;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class BuildingUnitEditCmd extends Command {

    public BuildingUnitEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        CompanyDAO compDao = new CompanyDAO(getCnn());
        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        ArrayList<BuildingUnitType> buildingUnitType = new ArrayList(buildingDao.getBuildingUnitTypeList());
        getRequest().setAttribute("buildingUnitType", buildingUnitType);
        BuildingUnit buildingUnit = null;
        int id = parId;
        if (id == 0) {
            buildingUnit = new BuildingUnit();
            buildingUnit.setBuildingId(buildingDao.getBuilding(getCompany().getId()).getId());
        } else {
            buildingUnit = buildingDao.getBuildingUnit(id);
        }
        getRequest().setAttribute("buildingUnit", buildingUnit);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_buildingUnitDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
