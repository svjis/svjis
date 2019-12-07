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
        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        String parId = getRequest().getParameter("id");
        String parBuildingUnitTypeId = getRequest().getParameter("typeId");
        String parRegistrationId = Validator.fixTextInput(getRequest().getParameter("registrationNo"), false);
        String parDescription = Validator.fixTextInput(getRequest().getParameter("description"), false);
        String parNumerator = getRequest().getParameter("numerator");
        String parDenominator = getRequest().getParameter("denominator");
        
        if (!validateInput(parId, parBuildingUnitTypeId, parRegistrationId, parDescription, parNumerator, parDenominator)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        BuildingUnit u = new BuildingUnit();
        u.setId(Integer.valueOf(parId));
        u.setBuildingId(buildingDao.getBuilding(getCompany().getId()).getId());
        u.setBuildingUnitTypeId(Integer.valueOf(parBuildingUnitTypeId));
        u.setRegistrationId(parRegistrationId);
        u.setDescription(parDescription);
        u.setNumerator(Integer.valueOf(parNumerator));
        u.setDenominator(Integer.valueOf(parDenominator));
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
    
    private boolean validateInput(String parId, String parBuildingUnitTypeId, String parRegistrationId, String parDescription, String parNumerator, String parDenominator) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateInteger(parBuildingUnitTypeId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateInteger(parBuildingUnitTypeId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parRegistrationId, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parDescription, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateInteger(parNumerator, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateInteger(parDenominator, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        return result;
    }
}
