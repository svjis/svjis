/*
 *       RedactionArticleMenuEditCmd.java
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

import cz.svjis.bean.Menu;
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
public class RedactionArticleMenuEditCmd extends Command {

    public RedactionArticleMenuEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        MenuDAO menuDao = new MenuDAO(getCnn());
        
        MenuNode menuNode = new MenuNode();
        if (parId != 0) {
            menuNode = menuDao.getMenuNode(parId, getUser().getCompanyId());
        }
        getRequest().setAttribute("menuNode", menuNode);
        
        Menu menu = menuDao.getMenu(getCompany().getId());
        menu.setActiveSection(-1);
        getRequest().setAttribute("menu", menu);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_ArticleMenuEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
