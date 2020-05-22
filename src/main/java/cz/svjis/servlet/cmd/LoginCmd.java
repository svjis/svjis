/*
 *       LoginCmd.java
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

import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.common.PermanentLoginUtils;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class LoginCmd extends Command {

    public LoginCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        String parLogin = Validator.getString(getRequest(), "login", 0, 50, false, false);
        String parPassword = Validator.getString(getRequest(), "password", 0, 50, false, true);

        UserDAO userDao = new UserDAO(getCnn());
        LanguageDAO languageDao = new LanguageDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        User u = userDao.getUserByLogin(getCompany().getId(), parLogin);
        if ((u != null) && userDao.verifyPassword(u, parPassword, true)) {
            User user = u;
            getRequest().getSession().setAttribute("user", user);
            Language language = languageDao.getDictionary(user.getLanguageId());
            getRequest().getSession().setAttribute("language", language);
            logDao.log(user.getId(), LogDAO.OPERATION_TYPE_LOGIN, LogDAO.ID_NULL, getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
            PermanentLoginUtils.savePermanentLogin(getResponse(), user, userDao, getSetup());
            
            String url = "Dispatcher?page=articleList";
            getRequest().setAttribute("url", url);
            RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
            rd.forward(getRequest(), getResponse());
        } else {
            getRequest().setAttribute("messageHeader", getLanguage().getText("Bad login"));
            getRequest().setAttribute("message", "<p>" + getLanguage().getText("You can continue") + " <a href=\"Dispatcher\">" + getLanguage().getText("here") + "</a>.</p><p><a href=\"Dispatcher?page=lostPassword\">" + getLanguage().getText("Forgot password?") + "</a></p>");
            RequestDispatcher rd = getRequest().getRequestDispatcher("/_message.jsp");
            rd.forward(getRequest(), getResponse());
        }
    }
}
