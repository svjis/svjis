/*
 *       UserEditCmd.java
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
import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class UserEditCmd extends Command {

    public UserEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        CompanyDAO compDao = new CompanyDAO(getCnn());
        RoleDAO roleDao = new RoleDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        LanguageDAO languageDao = new LanguageDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        User cUser;
        if (parId == 0) {
            cUser = new User();
            cUser.setCompanyId(getCompany().getId());
        } else {
            cUser = userDao.getUser(getCompany().getId(), parId);
        }
        getRequest().setAttribute("cUser", cUser);
        ArrayList<Language> languageList = new ArrayList<>(languageDao.getLanguageList());
        getRequest().setAttribute("languageList", languageList);
        ArrayList<Role> roleList = new ArrayList<>(roleDao.getRoleList(getCompany().getId()));
        getRequest().setAttribute("roleList", roleList);
        getRequest().setAttribute("sendCredentials", new cz.svjis.bean.Boolean(false));
        getRequest().setAttribute("message", "");
        getRequest().setAttribute("errorMessage", "");
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Administration_userDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
