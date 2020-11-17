/*
 *       BuildingSaveCmd.java
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
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class BuildingSaveCmd extends Command {

    public BuildingSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        String parAddress = Validator.getString(getRequest(), "address", 0, 50, false, false);
        String parCity = Validator.getString(getRequest(), "city", 0, 50, false, false);
        String parPostCode = Validator.getString(getRequest(), "postCode", 0, 10, false, false);
        String parRegNo = Validator.getString(getRequest(), "registrationNo", 0, 20, false, false);

        BuildingDAO buildingDao = new BuildingDAO(getCnn());
                
        Building b = buildingDao.getBuilding(getCompany().getId());
        b.setAddress(parAddress);
        b.setCity(parCity);
        b.setPostCode(parPostCode);
        b.setRegistrationNo(parRegNo);
        buildingDao.modifyBuilding(b);
        String message = getLanguage().getText("Saved") + "<br>";
        getRequest().setAttribute("message", message);
        getRequest().setAttribute("building", b);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_buildingDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
