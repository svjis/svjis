/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.Permission;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RoleEditCmd extends Command {

    public RoleEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        CompanyDAO compDao = new CompanyDAO(getCnn());
        RoleDAO roleDao = new RoleDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);
        Role role = null;
        if (parId == 0) {
            role = new Role();
            role.setCompanyId(getCompany().getId());
        } else {
            role = roleDao.getRole(getCompany().getId(), parId);
        }
        getRequest().setAttribute("role", role);
        ArrayList<Permission> permissionList = new ArrayList(roleDao.getPermissionList());
        getRequest().setAttribute("permissionList", permissionList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_roleDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
