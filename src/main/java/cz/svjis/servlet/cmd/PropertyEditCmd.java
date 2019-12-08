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
public class PropertyEditCmd extends Command {

    public PropertyEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        String parKey = getRequest().getParameter("key");
        
        if (!validateInput(parKey)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        CompanyDAO compDao = new CompanyDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        getRequest().setAttribute("key", parKey);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_propertyDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parKey) {
        boolean result = true;
        
        if (!Validator.validateString(parKey, 0, 50)) {
            result = false;
        }
        
        return result;
    }
}
