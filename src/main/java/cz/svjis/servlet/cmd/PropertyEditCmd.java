/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class PropertyEditCmd extends Command {

    public PropertyEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        CompanyDAO compDao = new CompanyDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        String key = getRequest().getParameter("key");
        getRequest().setAttribute("key", key);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_propertyDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
