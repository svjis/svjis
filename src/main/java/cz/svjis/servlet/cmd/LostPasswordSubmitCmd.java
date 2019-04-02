/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Company;
import cz.svjis.bean.Language;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.common.RandomString;
import cz.svjis.servlet.ICommand;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaroslav_b
 */
public class LostPasswordSubmitCmd implements ICommand {

    private Company company;
    private Properties setup;
    private Language language;

    @Override
    public void run(HttpServletRequest request, HttpServletResponse response, Connection cnn) throws Exception {

        UserDAO userDao = new UserDAO(cnn);
        LogDAO logDao = new LogDAO(cnn);

        String email = request.getParameter("email");
        if ((email == null) || (email.equals(""))) {
            String url = "Dispatcher?page=lostPassword";
            request.setAttribute("url", url);
            RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
            rd.forward(request, response);
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
                logDao.log(u.getId(), LogDAO.operationTypeSendLostPassword, LogDAO.idNull, request.getRemoteAddr(), request.getHeader("User-Agent"));
            }
            String body = getSetup().getProperty("mail.template.lost.password");
            body = String.format(body, logins);
            MailDAO mailDao = new MailDAO(
                    cnn,
                    getSetup().getProperty("mail.smtp"),
                    getSetup().getProperty("mail.login"),
                    getSetup().getProperty("mail.password"),
                    getSetup().getProperty("mail.sender"));
            mailDao.sendInstantMail(email, getCompany().getName(), body);
            request.setAttribute("messageHeader", getLanguage().getText("Password assistance"));
            request.setAttribute("message", getLanguage().getText("Your login and password were sent to your mail."));
            rd = request.getRequestDispatcher("/_message.jsp");
        } else {
            request.setAttribute("messageHeader", getLanguage().getText("Password assistance"));
            request.setAttribute("message", getLanguage().getText("There is not user assigned to e-mail."));
            rd = request.getRequestDispatcher("/_message.jsp");
        }
        rd.forward(request, response);
        return;
    }

    /**
     * @return the company
     */
    public Company getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * @return the setup
     */
    public Properties getSetup() {
        return setup;
    }

    /**
     * @param setup the setup to set
     */
    public void setSetup(Properties setup) {
        this.setup = setup;
    }

    /**
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(Language language) {
        this.language = language;
    }
}
