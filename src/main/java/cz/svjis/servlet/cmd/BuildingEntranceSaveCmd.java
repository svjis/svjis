/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.BuildingEntrance;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class BuildingEntranceSaveCmd extends Command {
    
    public BuildingEntranceSaveCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        String parDescription = Validator.getString(getRequest(), "description", 0, 50, false, false);
        String parAddress = Validator.getString(getRequest(), "address", 0, 50, false, false);
        
        BuildingDAO buildingDao = new BuildingDAO(getCnn());
        
        BuildingEntrance be = new BuildingEntrance();
        be.setId(parId);
        be.setBuildingId(buildingDao.getBuilding(getCompany().getId()).getId());
        be.setDescription(parDescription);
        be.setAddress(parAddress);
        if (be.getId() == 0) {
            be.setId(buildingDao.insertBuildingEntrance(be));
        } else {
            buildingDao.modifyBuildingEntrance(be);
        }
        String url = "Dispatcher?page=buildingEntranceList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
