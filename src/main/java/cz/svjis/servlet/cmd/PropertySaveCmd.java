/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

        String parOrigKey = Validator.fixTextInput(getRequest().getParameter("origKey"), false);
        String parKey = Validator.fixTextInput(getRequest().getParameter("key"), false);
        String parValue = Validator.fixTextInput(getRequest().getParameter("value"), true);
        
        if (!validateInput(parOrigKey, parKey, parValue)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        ApplicationSetupDAO setupDao = new ApplicationSetupDAO(getCnn());
        
        if (getSetup().getProperty(parOrigKey) != null) {
            setupDao.deleteProperty(getCompany().getId(), parOrigKey);
        }
        setupDao.insertProperty(getCompany().getId(), parKey, parValue);
        getRequest().getSession().setAttribute("setup", null);
        String url = "Dispatcher?page=propertyEdit&key=" + parKey;
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parOrigKey, String parKey, String parValue) {
        boolean result = true;
        
        if (!Validator.validateString(parOrigKey, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parKey, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parValue, 0, 1000)) {
            result = false;
        }
        
        return result;
    }
}
