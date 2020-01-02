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

        String parAddress = Validator.getString(getRequest(), "address", 0, 50, false, false);
        String parCity = Validator.getString(getRequest(), "city", 0, 50, false, false);
        String parPostCode = Validator.getString(getRequest(), "postCode", 0, 10, false, false);
        String parRegNo = Validator.getString(getRequest(), "registrationNo", 0, 20, false, false);

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
}
