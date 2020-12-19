/*
 *       RedactionArticleMenuCmd.java
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
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleMenuCmd extends Command {

    public RedactionArticleMenuCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        MenuDAO menuDao = new MenuDAO(getCnn());

        Menu menu = menuDao.getMenu(getCompany().getId());
        menu.setActiveSection(-1);
        getRequest().setAttribute("menu", menu);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Redaction_ArticleMenu.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
