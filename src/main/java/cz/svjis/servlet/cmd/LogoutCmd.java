/*
 *       LogoutCmd.java
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
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class LogoutCmd extends Command {

    public LogoutCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        UserDAO userDao = new UserDAO(getCnn());
        LanguageDAO languageDao = new LanguageDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        if (getUser() != null) {
            logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_LOGOUT, LogDAO.ID_NULL, getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
            PermanentLoginUtils.clearPermanentLogin(getResponse(), getUser(), userDao, getSetup());
        }
        User user = new User();
        user.setCompanyId(getCompany().getId());

        if (userDao.getUser(getCompany().getId(), getSetup().getAnonymousUserId()) != null) {
            user = userDao.getUser(getCompany().getId(), getSetup().getAnonymousUserId());
        }

        getRequest().getSession().setAttribute("user", user);
        Language language = languageDao.getDictionary(user.getLanguageId());
        getRequest().getSession().setAttribute("language", language);

        String url = "Dispatcher?page=articleList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
