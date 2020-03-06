/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.Message;
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
        CompanyDAO compDao = new CompanyDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        MailDAO mailDao = new MailDAO(
                getCnn(),
                getSetup().getProperty("mail.smtp"),
                getSetup().getProperty("mail.login"),
                getSetup().getProperty("mail.password"),
                getSetup().getProperty("mail.sender"));
        ArrayList<Message> messageList = new ArrayList(mailDao.getWaitingMessages(getCompany().getId()));
        getRequest().setAttribute("messageList", messageList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_messageList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
