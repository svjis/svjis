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
public class PropertyDeleteCmd extends Command {

    public PropertyDeleteCmd(CmdContext ctx) {
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
        
        ApplicationSetupDAO setupDao = new ApplicationSetupDAO(getCnn());
        
        setupDao.deleteProperty(getCompany().getId(), parKey);
        getRequest().getSession().setAttribute("setup", null);
        String url = "Dispatcher?page=propertyList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
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
