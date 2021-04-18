/*
 *       AdvertListCmd.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.servlet.cmd;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;

import cz.svjis.bean.AdvertDAO;
import cz.svjis.bean.AdvertType;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;

public class AdvertListCmd extends Command {
    
    public AdvertListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        AdvertDAO advertDao = new AdvertDAO(getCnn());
        
        ArrayList<AdvertType> advertTypeList = new ArrayList<>(advertDao.getAdvertTypeList());
        getRequest().setAttribute("advertTypeList", advertTypeList);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/AdvertList.jsp");
        rd.forward(getRequest(), getResponse());
    }

}
