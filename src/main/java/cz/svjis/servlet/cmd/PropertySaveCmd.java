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
