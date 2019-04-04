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
        BuildingDAO buildingDao = new BuildingDAO(getCnn());

        Building b = buildingDao.getBuilding(getCompany().getId());
        b.setAddress(getRequest().getParameter("address"));
        b.setCity(getRequest().getParameter("city"));
        b.setPostCode(getRequest().getParameter("postCode"));
        b.setRegistrationNo(getRequest().getParameter("registrationNo"));
        buildingDao.modifyBuilding(b);
        String url = "Dispatcher?page=buildingDetail";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
