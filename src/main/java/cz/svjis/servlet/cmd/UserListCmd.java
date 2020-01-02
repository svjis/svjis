/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
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
public class UserListCmd extends Command {

    public UserListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parRoleId = Validator.getInt(getRequest(), "roleId", 0, Validator.maxIntAllowed, true);
        boolean parDisabledUsers = Validator.getBoolean(getRequest(), "disabledUsers");

        CompanyDAO compDao = new CompanyDAO(getCnn());
        RoleDAO roleDao = new RoleDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());

        getRequest().setAttribute("disabledUsers", new cz.svjis.bean.Boolean(parDisabledUsers));
        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        ArrayList<Role> roleList = roleDao.getRoleList(getCompany().getId());
        getRequest().setAttribute("roleList", roleList);
        ArrayList<User> userList = userDao.getUserList(getCompany().getId(), false, parRoleId, !parDisabledUsers);
        getRequest().setAttribute("userList", userList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_userList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
