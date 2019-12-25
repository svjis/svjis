/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingFastCmd extends Command {
    
    public FaultReportingFastCmd(CmdContext ctx) {
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
        
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        
        FaultReport f = faultDao.getFault(getCompany().getId(), Integer.valueOf(parId));
        if (getUser().hasPermission("fault_reporting_resolver") && (f != null)) {
            
            if (getRequest().getParameter("takeTicket") != null) {
                f.setAssignedToUser(getUser());
                faultDao.modifyFault(f);
            }
            if (getRequest().getParameter("closeTicket") != null) {
                f.setClosed(true);
                faultDao.modifyFault(f);
            }
        }
        
        String url = "Dispatcher?page=faultDetail&id=" + parId;
        getRequest().setAttribute("url", url);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
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
