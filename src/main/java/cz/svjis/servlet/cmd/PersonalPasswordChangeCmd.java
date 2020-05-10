/*
 *       PersonalPasswordChangeCmd.java
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

import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class PersonalPasswordChangeCmd extends Command {
    
    public PersonalPasswordChangeCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {
        getRequest().setAttribute("message", "");
        getRequest().setAttribute("errorMessage", "");
        RequestDispatcher rd = getRequest().getRequestDispatcher("/PersonalSettings_passwordChange.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
