/*
 *       RedactionArticleMenuDeleteCmd.java
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

import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.MenuNode;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleMenuDeleteCmd extends Command {

    public RedactionArticleMenuDeleteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parMenuId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        MenuDAO menuDao = new MenuDAO(getCnn());
        
        MenuNode n = menuDao.getMenuNode(parMenuId, getCompany().getId());
        if (n != null) {
            if (n.getNumOfChilds() != 0) {
                String message = "Cannot delete menu which has child nodes.";
                getRequest().setAttribute("messageHeader", "Error");
                getRequest().setAttribute("message", message);
                RequestDispatcher rd = getRequest().getRequestDispatcher("/_message.jsp");
                rd.forward(getRequest(), getResponse());
                return;
            }
            menuDao.deleteMenuNode(n, getUser().getCompanyId());
        }
        String url = "Dispatcher?page=redactionArticleMenu";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
