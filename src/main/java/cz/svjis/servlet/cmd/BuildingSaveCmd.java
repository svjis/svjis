/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Building;
import cz.svjis.bean.BuildingDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class BuildingSaveCmd extends Command {

    public BuildingSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        String parAddress = Validator.fixTextInput(getRequest().getParameter("address"), false);
        String parCity = Validator.fixTextInput(getRequest().getParameter("city"), false);
        String parPostCode = Validator.fixTextInput(getRequest().getParameter("postCode"), false);
        String parRegNo = Validator.fixTextInput(getRequest().getParameter("registrationNo"), false);
        
        if (!validateInput(parAddress, parCity, parPostCode, parRegNo)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        BuildingDAO buildingDao = new BuildingDAO(getCnn());
                
        Building b = buildingDao.getBuilding(getCompany().getId());
        b.setAddress(parAddress);
        b.setCity(parCity);
        b.setPostCode(parPostCode);
        b.setRegistrationNo(parRegNo);
        buildingDao.modifyBuilding(b);
        String url = "Dispatcher?page=buildingDetail";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parAddress, String parCity, String parPostCode, String parRegNo) {
        boolean result = true;
        
        if (!Validator.validateString(parAddress, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parCity, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parPostCode, 0, 10)) {
            result = false;
        }
        
        if (!Validator.validateString(parRegNo, 0, 20)) {
            result = false;
        }
        
        return result;
    }
}
