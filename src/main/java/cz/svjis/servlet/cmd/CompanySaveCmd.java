/*
 *       CompanySaveCmd.java
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
import cz.svjis.validator.Validator;
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

        String parName = Validator.getString(getRequest(), "name", 0, 50, false, false);
        String parAddress = Validator.getString(getRequest(), "address", 0, 50, false, false);
        String parCity = Validator.getString(getRequest(), "city", 0, 50, false, false);
        String parPostCode = Validator.getString(getRequest(), "postCode", 0, 10, false, false);
        String parPhone = Validator.getString(getRequest(), "phone", 0, 30, false, false);
        String parFax = Validator.getString(getRequest(), "fax", 0, 30, false, false);
        String parEMail = Validator.getString(getRequest(), "eMail", 0, 30, false, false);
        String parRegNo = Validator.getString(getRequest(), "registrationNo", 0, 20, false, false);
        String parVatNo = Validator.getString(getRequest(), "vatRegistrationNo", 0, 20, false, false);
        String parDomain = Validator.getString(getRequest(), "internetDomain", 0, 50, false, false);

        CompanyDAO compDao = new CompanyDAO(getCnn());
        
        Company c = new Company();
        c.setId(getCompany().getId());
        c.setName(parName);
        c.setAddress(parAddress);
        c.setCity(parCity);
        c.setPostCode(parPostCode);
        c.setPhone(parPhone);
        c.setFax(parFax);
        c.seteMail(parEMail);
        c.setRegistrationNo(parRegNo);
        c.setVatRegistrationNo(parVatNo);
        c.setInternetDomain(parDomain);
        compDao.modifyCompany(c);
        getRequest().getSession().setAttribute("company", compDao.getCompany(getCompany().getId()));
        String url = "Dispatcher?page=companyDetail";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
