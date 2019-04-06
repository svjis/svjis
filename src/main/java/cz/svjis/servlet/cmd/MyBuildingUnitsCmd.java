/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        
        ArrayList<BuildingUnit> userHasUnitList = buildingDao.getUserHasBuildingUnitList(getUser().getId());
        getRequest().setAttribute("userHasUnitList", userHasUnitList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Units_userUnitList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
