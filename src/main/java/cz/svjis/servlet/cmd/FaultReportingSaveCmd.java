/*
 *       FaultReportingSaveCmd.java
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

import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.validator.InputValidationException;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingSaveCmd extends FaultAbstractCmd {
    
    public FaultReportingSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        String parSubject = Validator.getString(getRequest(), "subject", 0, 50, false, false);
        String parBody = Validator.getString(getRequest(), "body", 0, Validator.MAX_STRING_LEN_ALLOWED, false, getUser().hasPermission("can_write_html"));
        int parResolver = Validator.getInt(getRequest(), "resolverId", 0, Validator.MAX_INT_ALLOWED, true);
        int parEntrance = Validator.getInt(getRequest(), "entranceId", 0, Validator.MAX_INT_ALLOWED, true);
        boolean parClosed = Validator.getBoolean(getRequest(), "closed");

        BuildingDAO buildingDao = new BuildingDAO(getCnn());
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());

        if ((parSubject == null || parSubject.equals("")) && (parBody == null || parBody.equals(""))) {
            String url = "Dispatcher?page=faultReportingList";
            getRequest().setAttribute("url", url);
            RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }

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
        f.setBuildingEntrance((parEntrance != 0) ? buildingDao.getBuildingEntrance(parEntrance) : null);
        
        boolean isNew;
        User origAssignedTo = null;
        
        if (f.getId() == 0) {
            isNew = true;
            if (getUser().hasPermission("fault_reporting_reporter")) {
                int newId = faultDao.insertFault(f);
                f.setId(newId);
                faultDao.setUserWatchingFaultReport(f.getId(), getUser().getId());
                if (f.getAssignedToUser() != null) {
                    faultDao.setUserWatchingFaultReport(f.getId(), f.getAssignedToUser().getId());
                }
                logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_CREATE_FAULT, f.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
                if (f.isClosed()) {
                    sendNotification(f, "mail.template.fault.closed", faultDao.getUserListWatchingFaultReport(f.getId()));
                    logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_CLOSE_FAULT, f.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
                }
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
                if (f.getAssignedToUser() != null) {
                    faultDao.setUserWatchingFaultReport(f.getId(), f.getAssignedToUser().getId());
                }
                logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_MODIFY_FAULT, f.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
                if (!origFault.isClosed() && f.isClosed()) {
                    sendNotification(f, "mail.template.fault.closed", faultDao.getUserListWatchingFaultReport(f.getId()));
                    logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_CLOSE_FAULT, f.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
                }
                if (origFault.isClosed() && !f.isClosed()) {
                    sendNotification(f, "mail.template.fault.reopened", faultDao.getUserListWatchingFaultReport(f.getId()));
                    logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_REOPEN_FAULT, f.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
                }
            }
        }

        // send new fault notification
        if (isNew && (f.getAssignedToUser() == null)) {
            sendNotification(f, "mail.template.fault.notification", userDao.getUserListWithPermission(getCompany().getId(), "fault_reporting_resolver"));
        }
        
        // send fault assignment notification
        if (
            (isNew && (f.getAssignedToUser() != null)) ||
            (!isNew  && (f.getAssignedToUser() != null) && ((origAssignedTo == null) || (origAssignedTo.getId() != f.getAssignedToUser().getId())))
           ) {
            
            if ((f.getAssignedToUser().getId() != getUser().getId()) && 
                (!f.getAssignedToUser().geteMail().equals("")) && 
                (getSetup().getProperty("mail.template.fault.assigned") != null)) {
                
                ArrayList<User> recipient = new ArrayList<>();
                recipient.add(f.getAssignedToUser());
                sendNotification(f, "mail.template.fault.assigned", recipient);
            }
        }
        
        String url = "Dispatcher?page=faultDetail&id=" + f.getId();
        getRequest().setAttribute("url", url);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
