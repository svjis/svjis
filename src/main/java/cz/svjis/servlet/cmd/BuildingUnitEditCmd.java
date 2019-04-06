/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.BuildingUnit;
import cz.svjis.bean.BuildingUnitType;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
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
        CompanyDAO compDao = new CompanyDAO(getCnn());
        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        ArrayList<BuildingUnitType> buildingUnitType = buildingDao.getBuildingUnitTypeList();
        getRequest().setAttribute("buildingUnitType", buildingUnitType);
        BuildingUnit buildingUnit = null;
        int id = Integer.valueOf(getRequest().getParameter("id"));
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
