/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.LogDAO;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.common.RandomString;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class LostPasswordSubmitCmd extends Command {

    public LostPasswordSubmitCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        UserDAO userDao = new UserDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        String email = getRequest().getParameter("email");
        if ((email == null) || (email.equals(""))) {
            String url = "Dispatcher?page=lostPassword";
            getRequest().setAttribute("url", url);
            RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        RequestDispatcher rd = null;
        ArrayList<User> result = userDao.findLostPassword(getCompany().getId(), email);
        if (!result.isEmpty()) {
            String logins = "";
            for (User u : result) {
                String newPassword = RandomString.randomString(8);
                userDao.storeNewPassword(u.getCompanyId(), u.getLogin(), newPassword);
                logins += "Login: " + u.getLogin() + " " + "Password: " + newPassword + "<br>";
                logDao.log(u.getId(), LogDAO.operationTypeSendLostPassword, LogDAO.idNull, getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
            }
            String body = getSetup().getProperty("mail.template.lost.password");
            body = String.format(body, logins);
            MailDAO mailDao = new MailDAO(
                    getCnn(),
                    getSetup().getProperty("mail.smtp"),
                    getSetup().getProperty("mail.login"),
                    getSetup().getProperty("mail.password"),
                    getSetup().getProperty("mail.sender"));
            mailDao.sendInstantMail(email, getCompany().getName(), body);
            getRequest().setAttribute("messageHeader", getLanguage().getText("Password assistance"));
            getRequest().setAttribute("message", getLanguage().getText("Your login and password were sent to your mail."));
            rd = getRequest().getRequestDispatcher("/_message.jsp");
        } else {
            getRequest().setAttribute("messageHeader", getLanguage().getText("Password assistance"));
            getRequest().setAttribute("message", getLanguage().getText("There is not user assigned to e-mail."));
            rd = getRequest().getRequestDispatcher("/_message.jsp");
        }
        rd.forward(getRequest(), getResponse());
    }
}
