/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        BuildingUnit unit = buildingDao.getBuildingUnit(parUnitId);
        User u = userDao.getUser(getCompany().getId(), parUserId);

        if ((unit != null) && (unit.getBuildingId() == b.getId()) && u != null) {
            buildingDao.deleteUserHasBuildingUnitConnection(parUserId, parUnitId);
        }

        String url = "Dispatcher?page=userBuildingUnits&id=" + parUserId;
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
