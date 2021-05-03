/*
 *       Error400BadRequestCmd.java
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

public class Error400BadRequestCmd extends Command {
    
    private String message;
    
    public Error400BadRequestCmd(CmdContext ctx) {
        super(ctx);
        this.message = "";
    }
    
    public Error400BadRequestCmd(CmdContext ctx, String message) {
        super(ctx);
        this.message = message;
    }

    @Override
    public void execute() throws Exception {
        getResponse().setStatus(400);
        getRequest().setAttribute("message", message);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/400_BadRequest.jsp");
        rd.forward(getRequest(), getResponse());
    }

}
