/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.BuildingEntrance;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class BuildingEntranceListCmd extends Command {
    
    public BuildingEntranceListCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {

        CompanyDAO compDao = new CompanyDAO(getCnn());
        BuildingDAO buildingDao = new BuildingDAO(getCnn());
        
        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        ArrayList<BuildingEntrance> buildingEntranceList = new ArrayList(buildingDao.getBuildingEntranceList(
                buildingDao.getBuilding(getCompany().getId()).getId()));
        getRequest().setAttribute("buildingEntranceList", buildingEntranceList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_buildingEntranceList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
