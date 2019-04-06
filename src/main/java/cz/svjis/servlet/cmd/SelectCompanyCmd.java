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

        ArrayList<Company> companyList = compDao.getCompanyList();
        getRequest().setAttribute("companyList", companyList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/CompanyList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
