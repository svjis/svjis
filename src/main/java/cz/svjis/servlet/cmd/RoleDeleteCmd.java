/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RoleDeleteCmd extends Command {

    public RoleDeleteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);

        RoleDAO roleDao = new RoleDAO(getCnn());

        Role role = roleDao.getRole(getCompany().getId(), parId);
        if ((role != null)) {
            if (role.getNumOfUsers() != 0) {
                String message = "Cannot delete role which is not empty.";
                getRequest().setAttribute("messageHeader", "Error");
                getRequest().setAttribute("message", message);
                RequestDispatcher rd = getRequest().getRequestDispatcher("/_message.jsp");
                rd.forward(getRequest(), getResponse());
                return;
            } else {
                roleDao.deleteRole(role.getCompanyId(), role.getId());
            }
        }
        
        String url = "Dispatcher?page=roleList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
