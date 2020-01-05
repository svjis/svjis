/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.InputValidationException;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
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
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);
        String parSubject = Validator.getString(getRequest(), "subject", 0, 50, false, false);
        String parBody = Validator.getString(getRequest(), "body", 0, Validator.maxStringLenAllowed, false, false);
        int parResolver = Validator.getInt(getRequest(), "resolverId", 0, Validator.maxIntAllowed, true);
        boolean parClosed = Validator.getBoolean(getRequest(), "closed");

        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
                        MailDAO mailDao = new MailDAO(
                        getCnn(),
                        getSetup().getProperty("mail.smtp"),
                        getSetup().getProperty("mail.login"),
                        getSetup().getProperty("mail.password"),
                        getSetup().getProperty("mail.sender"));
        
        FaultReport f = new FaultReport();
        f.setId(parId);
        f.setCompanyId(getCompany().getId());
        f.setSubject(parSubject);
        f.setDescription(parBody);
        if (f.getId() == 0) {
            f.setCreatedByUser(getUser());
            f.setCreationDate(new Date());
        }
        if (getUser().hasPermission("fault_reporting_resolver")) {
            int resolver = parResolver;
            f.setAssignedToUser((resolver != 0) ? userDao.getUser(getCompany().getId(), resolver) : null);
            f.setClosed(parClosed);
        }
        
        boolean isNew;
        User origAssignedTo = null;
        
        if (f.getId() == 0) {
            isNew = true;
            if (getUser().hasPermission("fault_reporting_reporter")) {
                int newId = faultDao.insertFault(f);
                f.setId(newId);
            }
        } else {
            isNew = false;
            FaultReport origFault = faultDao.getFault(getCompany().getId(), f.getId());
            if (origFault == null) {
                throw new InputValidationException("Fault id " + f.getId() + " doesn't exist");
            }
            origAssignedTo = origFault.getAssignedToUser();
            
            if (getUser().hasPermission("fault_reporting_resolver")) {
                faultDao.modifyFault(f);
            }
        }
        
        // notifications
        String subject = getCompany().getInternetDomain() + ": #" + f.getId() + " - " + f.getSubject();
        String link = "<a href=\"http://" + getCompany().getInternetDomain() + "/Dispatcher?page=faultDetail&id=" + f.getId() + "\">#" + f.getId() + " - " + f.getSubject() + "</a>";

        // send new fault notification
        if (isNew && (f.getAssignedToUser() == null) && (getSetup().getProperty("mail.template.fault.notification") != null)) {
            String tBody = getSetup().getProperty("mail.template.fault.notification");
            ArrayList<User> userList = userDao.getUserListWithPermission(getCompany().getId(), "fault_reporting_resolver");
            for (User u : userList) {
                if ((u.getId() == getUser().getId()) || (u.geteMail().equals(""))) {
                    continue;
                }
                String body = String.format(tBody,
                        getUser().getFirstName() + " " + getUser().getLastName(),
                        link,
                        f.getDescription().replace("\n", "<br>"));
                mailDao.queueMail(getCompany().getId(), u.geteMail(), subject, body);
            }
        }
        
        // send fault assignment notification
        if (
            (isNew && (f.getAssignedToUser() != null)) ||
            (!isNew  && (f.getAssignedToUser() != null) && ((origAssignedTo == null) || (origAssignedTo.getId() != f.getAssignedToUser().getId())))
           ) {
            
            if ((f.getAssignedToUser().getId() != getUser().getId()) && 
                (!f.getAssignedToUser().geteMail().equals("")) && 
                (getSetup().getProperty("mail.template.fault.assigned") != null)) {
                
                String tBody = getSetup().getProperty("mail.template.fault.assigned");
                String body = String.format(tBody,
                        getUser().getFirstName() + " " + getUser().getLastName(),
                        link,
                        f.getDescription().replace("\n", "<br>"));
                mailDao.queueMail(getCompany().getId(), f.getAssignedToUser().geteMail(), subject, body);
            }
        }
        
        String url = "Dispatcher?page=faultDetail&id=" + f.getId();
        getRequest().setAttribute("url", url);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
