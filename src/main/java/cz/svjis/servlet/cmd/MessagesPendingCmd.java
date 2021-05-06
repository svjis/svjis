/*
 *       MessagesPendingCmd.java
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

import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.Message;
import cz.svjis.bean.Permission;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class MessagesPendingCmd extends Command {

    public MessagesPendingCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.MENU_ADMINISTRATION)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        
        CompanyDAO compDao = new CompanyDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        MailDAO mailDao = new MailDAO(
                getCnn(),
                getSetup().getMailSmtp(),
                getSetup().getMailSmtpPort(),
                getSetup().getMailSmtpSSL(),
                getSetup().getMailLogin(),
                getSetup().getMailPassword(),
                getSetup().getMailSender());
        ArrayList<Message> messageList = new ArrayList<>(mailDao.getWaitingMessages(getCompany().getId()));
        getRequest().setAttribute("messageList", messageList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Administration_messageList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
