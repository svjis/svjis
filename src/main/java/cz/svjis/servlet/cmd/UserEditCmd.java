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
        
        String parId = getRequest().getParameter("id");
        
        if (!validateInput(parId)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        CompanyDAO compDao = new CompanyDAO(getCnn());
        RoleDAO roleDao = new RoleDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        LanguageDAO languageDao = new LanguageDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        User cUser = null;
        int id = Integer.valueOf(parId);
        if (id == 0) {
            cUser = new User();
            cUser.setCompanyId(getCompany().getId());
        } else {
            cUser = userDao.getUser(getCompany().getId(), id);
        }
        getRequest().setAttribute("cUser", cUser);
        ArrayList<Language> languageList = languageDao.getLanguageList();
        getRequest().setAttribute("languageList", languageList);
        ArrayList<Role> roleList = roleDao.getRoleList(getCompany().getId());
        getRequest().setAttribute("roleList", roleList);
        getRequest().setAttribute("sendCredentials", new cz.svjis.bean.Boolean(false));
        getRequest().setAttribute("message", "");
        getRequest().setAttribute("errorMessage", "");
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_userDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parId) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }

        return result;
    }
}
