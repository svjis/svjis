/*
 *       BuildingUnitSaveCmd.java
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
import cz.svjis.bean.Permission;
import cz.svjis.servlet.Cmd;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

/**
 *
 * @author jaroslav_b
 */
public class BuildingUnitSaveCmd extends Command {

    public BuildingUnitSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.MENU_ADMINISTRATION)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        int parBuildingUnitTypeId = Validator.getInt(getRequest(), "typeId", 0, Validator.MAX_INT_ALLOWED, false);
        String parRegistrationId = Validator.getString(getRequest(), "registrationNo", 0, 50, false, false);
        String parDescription = Validator.getString(getRequest(), "description", 0, 50, false, false);
        int parNumerator = Validator.getInt(getRequest(), "numerator", 0, Validator.MAX_INT_ALLOWED, false);
        int parDenominator = Validator.getInt(getRequest(), "denominator", 0, Validator.MAX_INT_ALLOWED, false);
        
        BuildingDAO buildingDao = new BuildingDAO(getCnn());
        
        BuildingUnit u = new BuildingUnit();
        u.setId(parId);
        u.setBuildingId(buildingDao.getBuilding(getCompany().getId()).getId());
        u.setBuildingUnitTypeId(parBuildingUnitTypeId);
        u.setRegistrationId(parRegistrationId);
        u.setDescription(parDescription);
        u.setNumerator(parNumerator);
        u.setDenominator(parDenominator);
        if (u.getId() == 0) {
            u.setId(buildingDao.insertBuildingUnit(u));
        } else {
            BuildingUnit buildingUnit = buildingDao.getBuildingUnit(parId, buildingDao.getBuilding(getCompany().getId()).getId());
            if (buildingUnit == null) {
                new Error404NotFoundCmd(getCtx()).execute();
                return;
            }
            
            buildingDao.modifyBuildingUnit(u);
        }
        
        String url = String.format("Dispatcher?page=%s", Cmd.BUILDING_UNIT_LIST);
        getResponse().sendRedirect(url);
    }
}
