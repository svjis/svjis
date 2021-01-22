/*
 *       PersonalUserDetailSaveCmd.java
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
public class PersonalUserDetailSaveCmd extends Command {

    public PersonalUserDetailSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        String parSalutation = Validator.getString(getRequest(), "salutation", 0, 30, false, false);
        String parFirstName = Validator.getString(getRequest(), "firstName", 0, 30, false, false);
        String parLastName = Validator.getString(getRequest(), "lastName", 0, 30, false, false);
        int parLangId = Validator.getInt(getRequest(), "language", 0, Validator.MAX_INT_ALLOWED, false);
        String parLogin = Validator.getString(getRequest(), "login", 0, 50, false, false);
        String parAddress = Validator.getString(getRequest(), "address", 0, 50, false, false);
        String parCity = Validator.getString(getRequest(), "city", 0, 50, false, false);
        String parPostCode = Validator.getString(getRequest(), "postCode", 0, 10, false, false);
        String parCountry = Validator.getString(getRequest(), "country", 0, 50, false, false);
        String parFixedPhone = Validator.getString(getRequest(), "fixedPhone", 0, 30, false, false);
        String parCellPhone = Validator.getString(getRequest(), "cellPhone", 0, 30, false, false);
        String parEMail = Validator.getString(getRequest(), "eMail", 0, 50, false, false);

        LanguageDAO languageDao = new LanguageDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        
        User u = userDao.getUser(getCompany().getId(), getUser().getId());
        u.setSalutation(parSalutation);
        u.setFirstName(parFirstName);
        u.setLastName(parLastName);
        u.setLanguageId(parLangId);
        u.setLogin(parLogin);
        u.setAddress(parAddress);
        u.setCity(parCity);
        u.setPostCode(parPostCode);
        u.setCountry(parCountry);
        u.setFixedPhone(parFixedPhone);
        u.setCellPhone(parCellPhone);
        u.seteMail(parEMail);
        u.setShowInPhoneList(getRequest().getParameter("phoneList") != null);

        String message = "";
        String errorMessage = "";
        if (!userDao.testLoginValidity(u.getLogin())) {
            errorMessage += getLanguage().getText("Login is not valid.") + " (" + u.getLogin() + ")<br>";
        }
        if (!userDao.testLoginDuplicity(u.getLogin(), u.getId(), u.getCompanyId())) {
            errorMessage += getLanguage().getText("Login already exists.") + " (" + u.getLogin() + ")<br>";
        }
        if (errorMessage.equals("")) {
            userDao.modifyUser(u);
            Language language = languageDao.getDictionary(u.getLanguageId());
            getRequest().getSession().setAttribute("language", language);
            getRequest().getSession().setAttribute("user", u);
            u.setUserLogged(true);
            message = getLanguage().getText("User has been saved.") + "<br>";
        }

        getRequest().setAttribute("message", message);
        getRequest().setAttribute("errorMessage", errorMessage);
        ArrayList<Language> languageList = new ArrayList(languageDao.getLanguageList());
        getRequest().setAttribute("languageList", languageList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/PersonalSettings_userDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
