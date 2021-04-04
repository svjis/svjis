/*
 *       UserSaveCmd.java
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
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.common.RandomString;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class UserSaveCmd extends Command {

    public UserSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        String parSalutation = Validator.getString(getRequest(), "salutation", 0, 30, false, false);
        String parFirstName = Validator.getString(getRequest(), "firstName", 0, 30, false, false);
        String parLastName = Validator.getString(getRequest(), "lastName", 0, 30, false, false);
        int parLangId = Validator.getInt(getRequest(), "language", 0, Validator.MAX_INT_ALLOWED, false);
        String parAddress = Validator.getString(getRequest(), "address", 0, 50, false, false);
        String parCity = Validator.getString(getRequest(), "city", 0, 50, false, false);
        String parPostCode = Validator.getString(getRequest(), "postCode", 0, 10, false, false);
        String parCountry = Validator.getString(getRequest(), "country", 0, 50, false, false);
        String parFixedPhone = Validator.getString(getRequest(), "fixedPhone", 0, 30, false, false);
        String parCellPhone = Validator.getString(getRequest(), "cellPhone", 0, 30, false, false);
        String parEMail = Validator.getString(getRequest(), "eMail", 0, 50, false, false);
        String parLogin = Validator.getString(getRequest(), "login", 0, 50, false, false);
        String parPassword = Validator.getString(getRequest(), "password", 0, 20, false, true);
        boolean parShowInPhoneList = Validator.getBoolean(getRequest(), "phoneList");
        boolean parEnabled = Validator.getBoolean(getRequest(), "enabled");
        boolean parSendCredentials = Validator.getBoolean(getRequest(), "sendCredentials");
        String parInternalNote = Validator.getString(getRequest(), "internalNote", 0, 250, false, false);

        CompanyDAO compDao = new CompanyDAO(getCnn());
        RoleDAO roleDao = new RoleDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        LanguageDAO languageDao = new LanguageDAO(getCnn());

        User u = new User();
        u.setId(parId);
        u.setCompanyId(getCompany().getId());
        u.setSalutation(parSalutation);
        u.setFirstName(parFirstName);
        u.setLastName(parLastName);
        u.setLanguageId(parLangId);
        u.setAddress(parAddress);
        u.setCity(parCity);
        u.setPostCode(parPostCode);
        u.setCountry(parCountry);
        u.setFixedPhone(parFixedPhone);
        u.setCellPhone(parCellPhone);
        u.seteMail(parEMail);
        u.setShowInPhoneList(parShowInPhoneList);
        u.setLogin(parLogin);
        u.setPassword(parPassword);
        u.setEnabled(parEnabled);
        u.setInternalNote(parInternalNote);
        HashMap<Integer, String> uRoles = new HashMap<>();
        List<Role> roles = roleDao.getRoleList(getCompany().getId());
        Iterator<Role> roleI = roles.iterator();
        while (roleI.hasNext()) {
            Role r = roleI.next();
            if (Validator.getBoolean(getRequest(), "r_" + r.getId())) {
                uRoles.put(r.getId(), r.getDescription());
            }
        }
        u.setRoles(uRoles);

        String message = "";
        String errorMessage = "";
        if (!userDao.testLoginValidity(u.getLogin())) {
            errorMessage += getLanguage().getText("Login is not valid.") + " (" + u.getLogin() + ")<br>";
        }
        if (!userDao.testLoginDuplicity(u.getLogin(), u.getId(), u.getCompanyId())) {
            errorMessage += getLanguage().getText("Login already exists.") + " (" + u.getLogin() + ")<br>";
        }
        if (parSendCredentials && (u.geteMail() == null || u.geteMail().equals(""))) {
            errorMessage += getLanguage().getText("E-Mail is missing.") + "<br>";
        }
        if (errorMessage.equals("")) {
            if (u.getId() == 0) {
                u.setId(userDao.insertUser(u));
            } else {
                userDao.modifyUser(u);
            }
            message = getLanguage().getText("User has been saved.") + "<br>";

            if (parSendCredentials) {
                if ((u.getPassword() == null || u.getPassword().equals(""))) {
                    u.setPassword(RandomString.randomString(8));
                    userDao.storeNewPassword(u.getCompanyId(), u.getLogin(), u.getPassword());
                }
                String logins = "Login: " + u.getLogin() + " " + "Password: " + u.getPassword() + "<br>";
                String body = getSetup().getMailTemplateLostPassword();
                body = String.format(body, logins);
                MailDAO mailDao = new MailDAO(
                        getCnn(),
                        getSetup().getMailSmtp(),
                        getSetup().getMailLogin(),
                        getSetup().getMailPassword(),
                        getSetup().getMailSender());
                mailDao.queueMail(getCompany().getId(), u.geteMail(), getCompany().getName(), body);
                message += getLanguage().getText("Credentials has been send by e-mail.") + "<br>";
            }
        }
        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        getRequest().setAttribute("cUser", u);
        ArrayList<Language> languageList = new ArrayList<>(languageDao.getLanguageList());
        getRequest().setAttribute("languageList", languageList);
        ArrayList<Role> roleList = new ArrayList<>(roleDao.getRoleList(getCompany().getId()));
        getRequest().setAttribute("roleList", roleList);
        getRequest().setAttribute("sendCredentials", new cz.svjis.bean.Boolean(parSendCredentials));
        getRequest().setAttribute("message", message);
        getRequest().setAttribute("errorMessage", errorMessage);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Administration_userDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
