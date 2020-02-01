/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.User;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author jarberan
 */
public abstract class FaultAbstractCmd  extends Command {

    public FaultAbstractCmd(CmdContext cmdCtx) {
        super(cmdCtx);
    }
    
    protected void sendNotification(FaultReport f, String templatePropertyName, ArrayList<User> userList) throws SQLException {
        if (getSetup().getProperty(templatePropertyName) != null) {
            
            MailDAO mailDao = new MailDAO(
                getCnn(),
                getSetup().getProperty("mail.smtp"),
                getSetup().getProperty("mail.login"),
                getSetup().getProperty("mail.password"),
                getSetup().getProperty("mail.sender"));
            
            String subject = getCompany().getInternetDomain() + ": #" + f.getId() + " - " + f.getSubject();
            String link = String.format("<a href=\"http://%s/Dispatcher?page=faultDetail&id=%s\">#%s - %s</a>",
                    getCompany().getInternetDomain(), String.valueOf(f.getId()), String.valueOf(f.getId()), f.getSubject());
            String tBody = getSetup().getProperty(templatePropertyName);

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
}
