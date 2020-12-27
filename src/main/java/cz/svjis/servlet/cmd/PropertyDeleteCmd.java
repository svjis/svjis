/*
 *       PropertyDeleteCmd.java
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

import cz.svjis.bean.ApplicationSetupDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

/**
 *
 * @author jaroslav_b
 */
public class PropertyDeleteCmd extends Command {

    public PropertyDeleteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        String parKey = Validator.getString(getRequest(), "key", 0, 50, false, false);

        ApplicationSetupDAO setupDao = new ApplicationSetupDAO(getCnn());
        
        setupDao.deleteProperty(getCompany().getId(), parKey);
        getRequest().getSession().setAttribute("setup", null);
        String url = "Dispatcher?page=propertyList";
        getResponse().sendRedirect(url);
    }
}
