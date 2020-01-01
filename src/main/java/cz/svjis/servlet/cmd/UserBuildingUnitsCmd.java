/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.BuildingUnit;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class UserBuildingUnitsCmd extends Command {

    public UserBuildingUnitsCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);

        CompanyDAO compDao = new CompanyDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        User cUser = userDao.getUser(getCompany().getId(), parId);
        getRequest().setAttribute("cUser", cUser);
        ArrayList<BuildingUnit> userHasUnitList = buildingDao.getUserHasBuildingUnitList(cUser.getId());
        getRequest().setAttribute("userHasUnitList", userHasUnitList);
        ArrayList<BuildingUnit> unitList = buildingDao.getBuildingUnitList(getCompany().getId(), 0);
        getRequest().setAttribute("unitList", unitList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_userUnits.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
