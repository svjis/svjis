/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
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
        UserDAO userDao = new UserDAO(getCnn());

        String message = "";
        String oldPass = getRequest().getParameter("oldPassword");
        String newPass1 = getRequest().getParameter("newPassword");
        String newPass2 = getRequest().getParameter("newPassword2");
        
        if (!userDao.verifyPassword(getUser(), oldPass, false)) {
            message += getLanguage().getText("You typed wrong current password.") + "<br>";
        }
        
        if (!newPass1.equals(newPass2)) {
            message += getLanguage().getText("New passwords doesnt match.") + "<br>";
        }
        
        if (!userDao.testPasswordValidity(newPass2)) {
            message += getLanguage().getText("Password is too short. Minimum is 6 characters.") + "<br>";
        }
        
        if (message.equals("")) {
            userDao.storeNewPassword(getUser().getCompanyId(), getUser().getLogin(), newPass1);
            message += getLanguage().getText("New password has been set.") + "<br>";
        }
        
        getRequest().setAttribute("message", message);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/PersonalSettings_passwordChange.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
