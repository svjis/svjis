/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Permission;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
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
public class RoleSaveCmd extends Command {

    public RoleSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);
        String parDescription = Validator.getString(getRequest(), "description", 0, 50, false, false);

        RoleDAO roleDao = new RoleDAO(getCnn());

        Role role = new Role();
        role.setId(parId);
        role.setCompanyId(getCompany().getId());
        role.setDescription(parDescription);
        HashMap props = new HashMap();
        ArrayList<Permission> perms = roleDao.getPermissionList();
        Iterator<Permission> permsI = perms.iterator();
        while (permsI.hasNext()) {
            Permission p = permsI.next();
            if (Validator.getBoolean(getRequest(), "p_" + p.getId())) {
                props.put(new Integer(p.getId()), p.getDescription());
            }
        }
        role.setPermissions(props);
        if (role.getId() == 0) {
            role.setId(roleDao.insertRole(role));
        } else {
            roleDao.modifyRole(role);
        }
        String url = "Dispatcher?page=roleEdit&id=" + role.getId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
