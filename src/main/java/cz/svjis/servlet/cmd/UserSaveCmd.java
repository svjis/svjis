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
        CompanyDAO compDao = new CompanyDAO(getCnn());
        RoleDAO roleDao = new RoleDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        LanguageDAO languageDao = new LanguageDAO(getCnn());

        User u = new User();
        u.setId(Integer.valueOf(getRequest().getParameter("id")));
        u.setCompanyId(getCompany().getId());
        u.setSalutation(getRequest().getParameter("salutation"));
        u.setFirstName(getRequest().getParameter("firstName"));
        u.setLastName(getRequest().getParameter("lastName"));
        u.setLanguageId(Integer.valueOf(getRequest().getParameter("language")));
        u.setAddress(getRequest().getParameter("address"));
        u.setCity(getRequest().getParameter("city"));
        u.setPostCode(getRequest().getParameter("postCode"));
        u.setCountry(getRequest().getParameter("country"));
        u.setFixedPhone(getRequest().getParameter("fixedPhone"));
        u.setCellPhone(getRequest().getParameter("cellPhone"));
        u.seteMail(getRequest().getParameter("eMail"));
        u.setShowInPhoneList(getRequest().getParameter("phoneList") != null);
        u.setLogin(getRequest().getParameter("login"));
        u.setPassword(getRequest().getParameter("password"));
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
        if (!userDao.testLoginDuplicity(u.getLogin(), u.getId())) {
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
}
