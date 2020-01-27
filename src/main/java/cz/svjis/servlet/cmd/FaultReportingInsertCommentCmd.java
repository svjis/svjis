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
import cz.svjis.validator.Validator;
import java.util.Date;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingInsertCommentCmd extends FaultAbstractCmd {
    
    public FaultReportingInsertCommentCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);
        String parBody = Validator.getString(getRequest(), "body", 0, Validator.maxStringLenAllowed, false, getUser().hasPermission("can_write_html"));
        
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());

        FaultReport report = faultDao.getFault(getCompany().getId(), parId);
        
        if ((report.getId() != 0) 
                && (!report.isClosed()) 
                && getUser().hasPermission("fault_reporting_comment")
                && (parBody != null)
                && (!parBody.equals(""))) {
            FaultReportComment c = new FaultReportComment();
            c.setFaultReportId(report.getId());
            c.setInsertionTime(new Date());
            c.setUser(getUser());
            c.setBody(parBody);
            faultDao.insertFaultReportComment(c);

            faultDao.setUserWatchingFaultReport(report.getId(), getUser().getId());

            sendNotification(report, "mail.template.fault.comment.notification", faultDao.getUserListForNotificationAboutNewComment(report.getId()));
        }
        
        String url = "Dispatcher?page=faultDetail&id=" + parId;
        getRequest().setAttribute("url", url);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
}
