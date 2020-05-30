/*
 *       FaultAbstractCmd.java
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

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.User;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jarberan
 */
public abstract class FaultAbstractCmd  extends Command {

    public FaultAbstractCmd(CmdContext cmdCtx) {
        super(cmdCtx);
    }
    
    protected void sendNotification(FaultReport f, String template, List<User> userList) throws SQLException {
            
        MailDAO mailDao = new MailDAO(
            getCnn(),
            getSetup().getMailSmtp(),
            getSetup().getMailLogin(),
            getSetup().getMailPassword(),
            getSetup().getMailSender());

        String subject = getCompany().getInternetDomain() + ": #" + f.getId() + " - " + f.getSubject();
        String link = String.format("<a href=\"http://%s/Dispatcher?page=faultDetail&id=%s\">#%s - %s</a>",
                getCompany().getInternetDomain(), String.valueOf(f.getId()), String.valueOf(f.getId()), f.getSubject());
        String tBody = template;

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
}
