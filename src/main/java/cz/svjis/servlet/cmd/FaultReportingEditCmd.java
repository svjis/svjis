/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportingDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingEditCmd extends Command {
    
    public FaultReportingEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        String parId = Validator.fixTextInput(getRequest().getParameter("id"), false);
        
        if (!validateInput(parId)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        FaultReportingDAO faultDao = new FaultReportingDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        
        FaultReport report = new FaultReport();
        int id = Integer.valueOf(parId);
        if ((id != 0) && getUser().hasPermission("fault_reporting_resolver")) {
            report = faultDao.getFault(getCompany().getId(), id);
        }
        getRequest().setAttribute("report", report);
        
        ArrayList<User> resolverList = userDao.getUserListByPermission(getCompany().getId(), "fault_reporting_resolver");
        getRequest().setAttribute("resolverList", resolverList);
                
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Faults_reportEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parId) {
        boolean result = true;
        
        if ((parId != null) && (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed))) {
            result = false;
        }
        
        return result;
    }
}
