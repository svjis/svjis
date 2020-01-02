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
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

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

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);
        int parBuildingUnitTypeId = Validator.getInt(getRequest(), "typeId", 0, Validator.maxIntAllowed, false);
        String parRegistrationId = Validator.getString(getRequest(), "registrationNo", 0, 50, false, false);
        String parDescription = Validator.getString(getRequest(), "description", 0, 50, false, false);
        int parNumerator = Validator.getInt(getRequest(), "numerator", 0, Validator.maxIntAllowed, false);
        int parDenominator = Validator.getInt(getRequest(), "denominator", 0, Validator.maxIntAllowed, false);
        
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
            buildingDao.modifyBuildingUnit(u);
        }
        String url = "Dispatcher?page=buildingUnitEdit&id=" + u.getId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
