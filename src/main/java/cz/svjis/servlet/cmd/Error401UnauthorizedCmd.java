/*
 *       Error401UnauthorizedCmd.java
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

import javax.servlet.RequestDispatcher;

import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;

public class Error401UnauthorizedCmd extends Command {
    
    public Error401UnauthorizedCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        getResponse().setStatus(401);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/401_Unauthorized.jsp");
        rd.forward(getRequest(), getResponse());
    }

}
