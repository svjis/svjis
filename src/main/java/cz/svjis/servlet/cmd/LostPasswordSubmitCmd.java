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
import cz.svjis.validator.Validator;
import java.util.List;
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
        
        String parEmail = Validator.getString(getRequest(), "email", 0, 100, false, false);

        UserDAO userDao = new UserDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        if ((parEmail == null) || (parEmail.equals(""))) {
            String url = "Dispatcher?page=lostPassword";
            getRequest().setAttribute("url", url);
            RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        RequestDispatcher rd;
        List<User> result = userDao.findLostPassword(getCompany().getId(), parEmail);
        if (!result.isEmpty()) {
            String logins = "";
            for (User u : result) {
                String newPassword = RandomString.randomString(8);
                userDao.storeNewPassword(u.getCompanyId(), u.getLogin(), newPassword);
                logins += "Login: " + u.getLogin() + " " + "Password: " + newPassword + "<br>";
                logDao.log(u.getId(), LogDAO.OPERATION_TYPE_SEND_LOST_PASSWORD, LogDAO.ID_NULL, getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
            }
            String body = getSetup().getProperty("mail.template.lost.password");
            body = String.format(body, logins);
            MailDAO mailDao = new MailDAO(
                    getCnn(),
                    getSetup().getProperty("mail.smtp"),
                    getSetup().getProperty("mail.login"),
                    getSetup().getProperty("mail.password"),
                    getSetup().getProperty("mail.sender"));
            mailDao.sendInstantMail(parEmail, getCompany().getName(), body);
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
