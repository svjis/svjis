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
        CompanyDAO compDao = new CompanyDAO(getCnn());
        
        String parName = Validator.fixTextInput(getRequest().getParameter("name"), false);
        String parAddress = Validator.fixTextInput(getRequest().getParameter("address"), false);
        String parCity = Validator.fixTextInput(getRequest().getParameter("city"), false);
        String parPostCode = Validator.fixTextInput(getRequest().getParameter("postCode"), false);
        String parPhone = Validator.fixTextInput(getRequest().getParameter("phone"), false);
        String parFax = Validator.fixTextInput(getRequest().getParameter("fax"), false);
        String parEMail = Validator.fixTextInput(getRequest().getParameter("eMail"), false);
        String parRegNo = Validator.fixTextInput(getRequest().getParameter("registrationNo"), false);
        String parVatNo = Validator.fixTextInput(getRequest().getParameter("vatRegistrationNo"), false);
        String parDomain = Validator.fixTextInput(getRequest().getParameter("internetDomain"), false);

        if (!validateInput(parName, parAddress, parCity, parPostCode, parPhone, parFax, parEMail, parRegNo, parVatNo, parDomain)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
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
    
    private boolean validateInput(String parName, String parAddress, String parCity, String parPostCode, String parPhone, String parFax, String parEMail, String parRegNo, String parVatNo, String parDomain) {
        boolean result = true;
        
        if (!Validator.validateString(parName, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parAddress, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parCity, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parPostCode, 0, 10)) {
            result = false;
        }
        
        if (!Validator.validateString(parPhone, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parFax, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parEMail, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parRegNo, 0, 20)) {
            result = false;
        }
        
        if (!Validator.validateString(parVatNo, 0, 20)) {
            result = false;
        }
        
        if (!Validator.validateString(parDomain, 0, 50)) {
            result = false;
        }
        
        return result;
    }
}
