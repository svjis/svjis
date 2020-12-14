/*
 *       RoleListCmd.java
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

import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RoleListCmd extends Command {

    public RoleListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        CompanyDAO compDao = new CompanyDAO(getCnn());
        RoleDAO roleDao = new RoleDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        ArrayList<Role> roleList = new ArrayList(roleDao.getRoleList(getCompany().getId()));
        getRequest().setAttribute("roleList", roleList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Administration_roleList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
