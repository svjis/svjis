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

        String parKey = Validator.getString(getRequest(), "key", 0, 50, false, false);

        ApplicationSetupDAO setupDao = new ApplicationSetupDAO(getCnn());
        
        setupDao.deleteProperty(getCompany().getId(), parKey);
        getRequest().getSession().setAttribute("setup", null);
        String url = "Dispatcher?page=propertyList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
