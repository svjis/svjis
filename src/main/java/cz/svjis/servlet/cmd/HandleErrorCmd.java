/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.MailDAO;
import cz.svjis.bean.User;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

/**
 *
 * @author berk
 */
public class HandleErrorCmd extends Command {
    
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

            Properties setup = (Properties) session.getAttribute("setup");
            if ((setup != null) && (setup.getProperty("error.report.recipient") != null)) {
                MailDAO mailDao = new MailDAO(
                        getCnn(),
                        setup.getProperty("mail.smtp"),
                        setup.getProperty("mail.login"),
                        setup.getProperty("mail.password"),
                        setup.getProperty("mail.sender"));

                mailDao.sendErrorReport(
                        user.getCompanyId(),
                        setup.getProperty("error.report.recipient"), 
                        getRequest().getRequestURL().toString() + "/" + getRequest().getQueryString(), 
                        userId,
                        userAgent,
                        throwable);
            }
            RequestDispatcher rd = getRequest().getRequestDispatcher("/Error.jsp");
            rd.forward(getRequest(), getResponse());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
