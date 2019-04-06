/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.BuildingDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class UserBuildingUnitAddCmd extends Command {

    public UserBuildingUnitAddCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        if (Integer.valueOf(getRequest().getParameter("unitId")) != 0) {
            buildingDao.addUserHasBuildingUnitConnection(
                    Integer.valueOf(getRequest().getParameter("userId")),
                    Integer.valueOf(getRequest().getParameter("unitId")));
        }
        String url = "Dispatcher?page=userBuildingUnits&id=" + getRequest().getParameter("userId");
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
