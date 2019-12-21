/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportingDAO;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.Date;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingSaveCmd extends Command {
    
    public FaultReportingSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        String parId = Validator.fixTextInput(getRequest().getParameter("id"), false);
        String parSubject = Validator.fixTextInput(getRequest().getParameter("subject"), false);
        String parBody = Validator.fixTextInput(getRequest().getParameter("body"), false);
        String parResolver = Validator.fixTextInput(getRequest().getParameter("resolverId"), false);
        
        if (!validateInput(parId, parSubject, parBody, parResolver)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        FaultReportingDAO faultDao = new FaultReportingDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        
        FaultReport f = new FaultReport();
        f.setId(Integer.valueOf(parId));
        f.setCompanyId(getCompany().getId());
        f.setSubject(parSubject);
        f.setDescription(parBody);
        if (f.getId() == 0) {
            f.setCreatedByUser(getUser());
            f.setCreationDate(new Date());
        }
        if (getUser().hasPermission("fault_reporting_resolver")) {
            int resolver = Integer.valueOf(parResolver);
            f.setAssignedToUser((resolver != 0) ? userDao.getUser(getCompany().getId(), resolver) : null);
            f.setClosed((getRequest().getParameter("closed") != null) ? true : false);
        }
        if (f.getId() == 0) {
            if (getUser().hasPermission("fault_reporting_reporter")) {
                faultDao.insertFault(f);
            }
        } else {
            if (getUser().hasPermission("fault_reporting_resolver")) {
                faultDao.modifyFault(f);
            }
        }
        
        String url = "Dispatcher?page=faultReportingList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parId, String parSubject, String parBody, String parResolver) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parSubject, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parBody, 0, Validator.maxStringLenAllowed)) {
            result = false;
        }
        
        if ((parResolver != null) && !Validator.validateInteger(parResolver, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        return result;
    }
}
