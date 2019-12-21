/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportComment;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.Date;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingInsertCommentCmd extends Command {
    
    public FaultReportingInsertCommentCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {
        
        String parId = Validator.fixTextInput(getRequest().getParameter("id"), false);
        String parBody = Validator.fixTextInput(getRequest().getParameter("body"), false);
        
        if (!validateInput(parId, parBody)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        
        FaultReport report = faultDao.getFault(getCompany().getId(), Integer.valueOf(parId));
        
        if ((report.getId() != 0) && (!report.isClosed()) && getUser().hasPermission("fault_reporting_comment")) {
            FaultReportComment c = new FaultReportComment();
            c.setFaultReportId(report.getId());
            c.setInsertionTime(new Date());
            c.setUser(getUser());
            c.setBody(parBody);
            faultDao.insertFaultReportComment(c);
        }
        
        String url = "Dispatcher?page=faultDetail&id=" + parId;
        getRequest().setAttribute("url", url);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parId, String parBody) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parBody, 0, Validator.maxStringLenAllowed)) {
            result = false;
        }
        
        return result;
    }
}
