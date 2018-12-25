/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet;

import cz.svjis.bean.MailDAO;
import cz.svjis.bean.User;
import java.sql.Connection;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author berk
 */
public class HandleErrorCmd implements ICommand {
    
    private Throwable throwable;
    private Connection cnn;
    
    @Override
    public void run(HttpServletRequest request, HttpServletResponse response) throws Exception {
                
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");
        if (user == null) user = new User();
        String userId = user.getFirstName() + " " + user.getLastName() + " (" + user.getId() + ")";
        String userAgent = request.getHeader("User-Agent");

        Properties setup = (Properties) session.getAttribute("setup");
        if ((setup != null) && (setup.getProperty("error.report.recipient") != null)) {
            MailDAO mailDao = new MailDAO(
                    cnn,
                    setup.getProperty("mail.smtp"),
                    setup.getProperty("mail.login"),
                    setup.getProperty("mail.password"),
                    setup.getProperty("mail.sender"));

            mailDao.sendErrorReport(
                    user.getCompanyId(),
                    setup.getProperty("error.report.recipient"), 
                    request.getRequestURL().toString() + "/" + request.getQueryString(), 
                    userId,
                    userAgent,
                    throwable);
        }
        RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
        rd.forward(request, response);
    }

    /**
     * @return the throwable
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * @param throwable the throwable to set
     */
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * @return the cnn
     */
    public Connection getCnn() {
        return cnn;
    }

    /**
     * @param cnn the cnn to set
     */
    public void setCnn(Connection cnn) {
        this.cnn = cnn;
    }
    
}
