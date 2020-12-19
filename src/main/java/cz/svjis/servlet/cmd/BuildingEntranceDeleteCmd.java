/*
 *       BuildingEntranceDeleteCmd.java
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
import cz.svjis.bean.BuildingEntrance;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class BuildingEntranceDeleteCmd extends Command {
    
    public BuildingEntranceDeleteCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        
        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        BuildingEntrance be = new BuildingEntrance();
        be.setId(parId);
        be.setBuildingId(buildingDao.getBuilding(getCompany().getId()).getId());
        boolean result = buildingDao.deleteBuildingEntrance(be);

        if (!result) {
            String errorMessage = getLanguage().getText("This entrance cannot be deleted.");
            getRequest().setAttribute("message", errorMessage);
            RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/_message.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        String url = "Dispatcher?page=buildingEntranceList";
        getResponse().sendRedirect(url);
    }
}
