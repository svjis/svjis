/*
 *       RoleDeleteCmd.java
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

import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RoleDeleteCmd extends Command {

    public RoleDeleteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        RoleDAO roleDao = new RoleDAO(getCnn());

        Role role = roleDao.getRole(getCompany().getId(), parId);
        if ((role != null)) {
            if (role.getNumOfUsers() != 0) {
                String message = "Cannot delete role which is assigned to users.";
                getRequest().setAttribute("messageHeader", "Error");
                getRequest().setAttribute("message", message);
                RequestDispatcher rd = getRequest().getRequestDispatcher("/_message.jsp");
                rd.forward(getRequest(), getResponse());
                return;
            } else {
                roleDao.deleteRole(role);
            }
        }
        
        String url = "Dispatcher?page=roleList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
