/*
 *       FaultReportingInsertCommentCmd.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.servlet.cmd;

import cz.svjis.bean.Comment;
import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.Permission;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.Date;
import java.util.List;

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
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        String parBody = Validator.getString(getRequest(), "body", 0, Validator.MAX_STRING_LEN_ALLOWED, false, getUser().hasPermission(Permission.CAN_WRITE_HTML));
        
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());

        FaultReport report = faultDao.getFault(getCompany().getId(), parId);
        
        if ((report.getId() != 0) 
                && (!report.isClosed()) 
                && getUser().hasPermission(Permission.FAULT_REPORTING_COMMENT)
                && (parBody != null)
                && (!parBody.equals(""))) {
            Comment c = new Comment();
            c.setDocumentId(report.getId());
            c.setInsertionTime(new Date());
            c.setUser(getUser());
            c.setBody(parBody);
            faultDao.insertFaultReportComment(c);

            faultDao.setUserWatchingFaultReport(report.getId(), getUser().getId());

            // send notification
            String subject = String.format("%s: %s (New comment)", getCompany().getInternetDomain(), report.getEmailSubject());
            String tBody = getSetup().getMailTemplateFaultComment();
            MailDAO mailDao = new MailDAO(
                    getCnn(),
                    getSetup().getMailSmtp(),
                    getSetup().getMailSmtpPort(),
                    getSetup().getMailSmtpSSL(),
                    getSetup().getMailLogin(),
                    getSetup().getMailPassword(),
                    getSetup().getMailSender());
            List<User> userList = faultDao.getUserListWatchingFaultReport(report.getId());
            
            if (report.getAssignedToUser() == null) {
                List<User> resolvers = userDao.getUserListWithPermission(getCompany().getId(), Permission.FAULT_REPORTING_RESOLVER);
                for(User r: resolvers) {
                    boolean add = true;
                    for (User u: userList) {
                        if (r.getId() == u.getId()) {
                            add = false;
                            break;
                        }
                    }
                    if (add) {
                        userList.add(r);
                    }
                }
            }
            
            for (User u : userList) {
                if (u.getId() == getUser().getId()) {
                    continue;
                }
                String link = String.format("<a href=\"http://%s/Dispatcher?page=faultDetail&id=%s\">%s</a>",
                        getCompany().getInternetDomain(), String.valueOf(report.getId()), report.getEmailSubject());
                String body = String.format(tBody,
                        getUser().getFirstName() + " " + getUser().getLastName(),
                        link,
                        c.getBody().replace("\n", "<br>"));
                mailDao.queueMail(getCompany().getId(), u.geteMail(), subject, body);
            }
        }
        
        String url = "Dispatcher?page=faultDetail&id=" + parId;
        getResponse().sendRedirect(url);
    }
    
}
