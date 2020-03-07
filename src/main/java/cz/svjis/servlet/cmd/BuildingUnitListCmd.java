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
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class BuildingUnitListCmd extends Command {

    public BuildingUnitListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parTypeId = Validator.getInt(getRequest(), "typeId", 0, Validator.MAX_INT_ALLOWED, true);

        CompanyDAO compDao = new CompanyDAO(getCnn());
        BuildingDAO buildingDao = new BuildingDAO(getCnn());
        
        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        ArrayList<BuildingUnitType> buildingUnitType = new ArrayList(buildingDao.getBuildingUnitTypeList());
        getRequest().setAttribute("buildingUnitType", buildingUnitType);
        int typeId = parTypeId;
        ArrayList<BuildingUnit> buildingUnitList = new ArrayList(buildingDao.getBuildingUnitList(
                buildingDao.getBuilding(getCompany().getId()).getId(),
                typeId));
        getRequest().setAttribute("buildingUnitList", buildingUnitList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_buildingUnitList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
