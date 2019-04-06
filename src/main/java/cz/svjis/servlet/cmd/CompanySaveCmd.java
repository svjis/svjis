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
public class CompanySaveCmd extends Command {

    public CompanySaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        CompanyDAO compDao = new CompanyDAO(getCnn());

        Company c = new Company();
        c.setId(getCompany().getId());
        c.setName(getRequest().getParameter("name"));
        c.setAddress(getRequest().getParameter("address"));
        c.setCity(getRequest().getParameter("city"));
        c.setPostCode(getRequest().getParameter("postCode"));
        c.setPhone(getRequest().getParameter("phone"));
        c.setFax(getRequest().getParameter("fax"));
        c.seteMail(getRequest().getParameter("eMail"));
        c.setRegistrationNo(getRequest().getParameter("registrationNo"));
        c.setVatRegistrationNo(getRequest().getParameter("vatRegistrationNo"));
        c.setInternetDomain(getRequest().getParameter("internetDomain"));
        compDao.modifyCompany(c);
        getRequest().getSession().setAttribute("company", compDao.getCompany(getCompany().getId()));
        String url = "Dispatcher?page=companyDetail";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
