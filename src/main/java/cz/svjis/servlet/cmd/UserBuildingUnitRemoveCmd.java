/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.BuildingDAO;
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

        int parUnitId = Validator.getInt(getRequest(), "unitId", 0, Validator.maxIntAllowed, false);
        int parUserId = Validator.getInt(getRequest(), "userId", 0, Validator.maxIntAllowed, false);

        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        buildingDao.deleteUserHasBuildingUnitConnection(parUserId, parUnitId);
        String url = "Dispatcher?page=userBuildingUnits&id=" + parUserId;
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
