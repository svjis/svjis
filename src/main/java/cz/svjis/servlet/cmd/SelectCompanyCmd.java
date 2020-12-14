/*
 *       SelectCompanyCmd.java
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
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class SelectCompanyCmd extends Command {

    public SelectCompanyCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        CompanyDAO compDao = new CompanyDAO(getCnn());

        ArrayList<Company> companyList = new ArrayList(compDao.getCompanyList());
        getRequest().setAttribute("companyList", companyList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/CompanyList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
