/*
 *       HandleErrorCmd.java
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

import cz.svjis.bean.MailDAO;
import cz.svjis.bean.User;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.InputValidationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 *
 * @author berk
 */
public class HandleErrorCmd extends Command {
    
    private static final Logger logger = Logger.getLogger(HandleErrorCmd.class.getName());
    private final Throwable throwable;

    public HandleErrorCmd(CmdContext ctx, Throwable throwable) {
        super(ctx);
        this.throwable = throwable;
    }
    
    @Override
    public void execute() {
        try {
            HttpSession session = getRequest().getSession();

            User user = (User) session.getAttribute("user");
            if (user == null) user = new User();
            String userId = user.getFirstName() + " " + user.getLastName() + " (" + user.getId() + ")";
            String userAgent = getRequest().getHeader("User-Agent");

            if (!getSetup().getErrorReportRecipient().equals("")) {
                MailDAO mailDao = new MailDAO(
                        getCnn(),
                        getSetup().getMailSmtp(),
                        getSetup().getMailSmtpPort(),
                        getSetup().getMailSmtpSSL(),
                        getSetup().getMailLogin(),
                        getSetup().getMailPassword(),
                        getSetup().getMailSender());

                mailDao.sendErrorReport(
                        getSetup().getErrorReportRecipient(), 
                        getRequest().getRequestURL().toString() + "/" + getRequest().getQueryString(), 
                        userId,
                        userAgent,
                        throwable);
            }

            if (this.throwable instanceof InputValidationException) {
                new Error400BadRequestCmd(getCtx(), this.throwable.getMessage()).execute();
            } else {
                new Error500InternalServerErrorCmd(getCtx()).execute();
            }
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not run HandleErrorCmd", throwable);
        }
    }
}
