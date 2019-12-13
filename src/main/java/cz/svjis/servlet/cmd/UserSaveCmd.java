/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.HashMap;
import java.util.Iterator;
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
        
        String parId = getRequest().getParameter("id");
        String parSalutation = Validator.fixTextInput(getRequest().getParameter("salutation"), false);
        String parFirstName = Validator.fixTextInput(getRequest().getParameter("firstName"), false);
        String parLastName = Validator.fixTextInput(getRequest().getParameter("lastName"), false);
        String parLangId = getRequest().getParameter("language");
        String parAddress = Validator.fixTextInput(getRequest().getParameter("address"), false);
        String parCity = Validator.fixTextInput(getRequest().getParameter("city"), false);
        String parPostCode = Validator.fixTextInput(getRequest().getParameter("postCode"), false);
        String parCountry = Validator.fixTextInput(getRequest().getParameter("country"), false);
        String parFixedPhone = Validator.fixTextInput(getRequest().getParameter("fixedPhone"), false);
        String parCellPhone = Validator.fixTextInput(getRequest().getParameter("cellPhone"), false);
        String parEMail = Validator.fixTextInput(getRequest().getParameter("eMail"), false);
        String parLogin = Validator.fixTextInput(getRequest().getParameter("login"), false);
        String parPassword = Validator.fixTextInput(getRequest().getParameter("password"), true);
        
        if (!validateInput(parId, parSalutation, parFirstName, parLastName, parLangId, parAddress, parCity, parPostCode, parCountry, parFixedPhone, parCellPhone, parEMail, parLogin, parPassword)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        CompanyDAO compDao = new CompanyDAO(getCnn());
        RoleDAO roleDao = new RoleDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        LanguageDAO languageDao = new LanguageDAO(getCnn());

        User u = new User();
        u.setId(Integer.valueOf(parId));
        u.setCompanyId(getCompany().getId());
        u.setSalutation(parSalutation);
        u.setFirstName(parFirstName);
        u.setLastName(parLastName);
        u.setLanguageId(Integer.valueOf(parLangId));
        u.setAddress(parAddress);
        u.setCity(parCity);
        u.setPostCode(parPostCode);
        u.setCountry(parCountry);
        u.setFixedPhone(parFixedPhone);
        u.setCellPhone(parCellPhone);
        u.seteMail(parEMail);
        u.setShowInPhoneList(getRequest().getParameter("phoneList") != null);
        u.setLogin(parLogin);
        u.setPassword(parPassword);
        u.setEnabled(getRequest().getParameter("enabled") != null);
        HashMap uRoles = new HashMap();
        ArrayList<Role> roles = roleDao.getRoleList(getCompany().getId());
        Iterator<Role> roleI = roles.iterator();
        while (roleI.hasNext()) {
            Role r = roleI.next();
            if (getRequest().getParameter("r_" + r.getId()) != null) {
                uRoles.put(new Integer(r.getId()), r.getDescription());
            }
        }
        u.setRoles(uRoles);
        String message = "";
        if (!userDao.testLoginValidity(u.getLogin())) {
            message += getLanguage().getText("Login is not valid.") + " (" + u.getLogin() + ")<br>";
        }
        if (!userDao.testLoginDuplicity(u.getLogin(), u.getId(), u.getCompanyId())) {
            message += getLanguage().getText("Login already exists.") + " (" + u.getLogin() + ")<br>";
        }
        //if (!userDao.testPasswordValidity(u.getPassword())) {
        //    message += language.getText("Password is too short. Minimum is 6 characters.") + "<br>";
        //}
        if (message.equals("")) {
            if (u.getId() == 0) {
                u.setId(userDao.insertUser(u));
            } else {
                userDao.modifyUser(u);
            }
            message = getLanguage().getText("User has been saved.");
        }
        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        getRequest().setAttribute("cUser", u);
        ArrayList<Language> languageList = languageDao.getLanguageList();
        getRequest().setAttribute("languageList", languageList);
        ArrayList<Role> roleList = roleDao.getRoleList(getCompany().getId());
        getRequest().setAttribute("roleList", roleList);
        getRequest().setAttribute("message", message);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_userDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parId, String parSalutation, String parFirstName, String parLastName, String parLangId, String parAddress, String parCity, String parPostCode, String parCountry, String parFixedPhone, String parCellPhone, String parEMail, String parLogin, String parPassword) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parSalutation, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parFirstName, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parLastName, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateInteger(parLangId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parAddress, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parCity, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parPostCode, 0, 10)) {
            result = false;
        }
        
        if (!Validator.validateString(parCountry, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parFixedPhone, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parCellPhone, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parEMail, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parLogin, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parPassword, 0, 20)) {
            result = false;
        }

        return result;
    }
}
