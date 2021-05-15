/*
 *       RoleSaveCmd.java
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

import cz.svjis.bean.Permission;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.servlet.Cmd;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
        
        if (!getUser().hasPermission(Permission.MENU_ADMINISTRATION)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        String parDescription = Validator.getString(getRequest(), "description", 0, 50, false, false);

        RoleDAO roleDao = new RoleDAO(getCnn());

        Role role = new Role();
        role.setId(parId);
        role.setCompanyId(getCompany().getId());
        role.setDescription(parDescription);
        HashMap<Integer, String> props = new HashMap<>();
        ArrayList<Permission> perms = new ArrayList<>(roleDao.getPermissionList());
        Iterator<Permission> permsI = perms.iterator();
        while (permsI.hasNext()) {
            Permission p = permsI.next();
            if (Validator.getBoolean(getRequest(), "p_" + p.getId())) {
                props.put(p.getId(), p.getDescription());
            }
        }
        role.setPermissions(props);
        if (role.getId() == 0) {
            role.setId(roleDao.insertRole(role));
        } else {
            roleDao.modifyRole(role);
        }
        String url = String.format("Dispatcher?page=%s", Cmd.ROLE_LIST);
        getResponse().sendRedirect(url);
    }
}
