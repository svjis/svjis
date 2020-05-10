/*
 *       PersonalPasswordChangeSaveCmd.java
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

import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class PersonalPasswordChangeSaveCmd extends Command {

    public PersonalPasswordChangeSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        String oldPass = Validator.getString(getRequest(), "oldPassword", 0, 30, false, true);
        String newPass1 = Validator.getString(getRequest(), "newPassword", 0, 30, false, true);
        String newPass2 = Validator.getString(getRequest(), "newPassword2", 0, 30, false, true);
        
        String message = "";
        String errorMessage = "";
        
        UserDAO userDao = new UserDAO(getCnn());
        
        if (!userDao.verifyPassword(getUser(), oldPass, false)) {
            errorMessage += getLanguage().getText("You typed wrong current password.") + "<br>";
        }
        
        if (!newPass1.equals(newPass2)) {
            errorMessage += getLanguage().getText("New passwords doesnt match.") + "<br>";
        }
        
        if (!userDao.testPasswordValidity(newPass2)) {
            errorMessage += getLanguage().getText("Password is too short. Minimum is 6 characters.") + "<br>";
        }
        
        if (errorMessage.equals("")) {
            userDao.storeNewPassword(getUser().getCompanyId(), getUser().getLogin(), newPass1);
            message += getLanguage().getText("New password has been set.") + "<br>";
        }
        
        getRequest().setAttribute("message", message);
        getRequest().setAttribute("errorMessage", errorMessage);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/PersonalSettings_passwordChange.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
