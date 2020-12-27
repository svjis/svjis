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
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

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
        String parAddress = Validator.getString(getRequest(), "address", 0, 50, false, false);
        String parCity = Validator.getString(getRequest(), "city", 0, 50, false, false);
        String parPostCode = Validator.getString(getRequest(), "postCode", 0, 10, false, false);
        String parCountry = Validator.getString(getRequest(), "country", 0, 50, false, false);
        String parFixedPhone = Validator.getString(getRequest(), "fixedPhone", 0, 30, false, false);
        String parCellPhone = Validator.getString(getRequest(), "cellPhone", 0, 30, false, false);
        String parEMail = Validator.getString(getRequest(), "eMail", 0, 50, false, false);

        LanguageDAO languageDao = new LanguageDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        
        getUser().setSalutation(parSalutation);
        getUser().setFirstName(parFirstName);
        getUser().setLastName(parLastName);
        getUser().setLanguageId(parLangId);
        getUser().setAddress(parAddress);
        getUser().setCity(parCity);
        getUser().setPostCode(parPostCode);
        getUser().setCountry(parCountry);
        getUser().setFixedPhone(parFixedPhone);
        getUser().setCellPhone(parCellPhone);
        getUser().seteMail(parEMail);
        getUser().setShowInPhoneList(getRequest().getParameter("phoneList") != null);
        userDao.modifyUser(getUser());
        Language language = languageDao.getDictionary(getUser().getLanguageId());
        getRequest().getSession().setAttribute("language", language);
        String url = "Dispatcher?page=psUserDetail";
        getResponse().sendRedirect(url);
    }
}
