/*
 *       PropertySaveCmd.java
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
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class PropertySaveCmd extends Command {

    public PropertySaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        String parOrigKey = Validator.getString(getRequest(), "origKey", 0, 50, false, false);
        String parKey = Validator.getString(getRequest(), "key", 0, 50, false, false);
        String parValue = Validator.getString(getRequest(), "value", 0, 1000, false, true);

        ApplicationSetupDAO setupDao = new ApplicationSetupDAO(getCnn());
        
        if (getSetup().getProperty(parOrigKey) != null) {
            setupDao.deleteProperty(getCompany().getId(), parOrigKey);
        }
        setupDao.insertProperty(getCompany().getId(), parKey, parValue);
        getRequest().getSession().setAttribute("setup", null);
        String url = "Dispatcher?page=propertyList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
