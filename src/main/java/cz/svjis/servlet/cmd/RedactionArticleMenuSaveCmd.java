/*
 *       RedactionArticleMenuSaveCmd.java
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
import cz.svjis.bean.Permission;
import cz.svjis.servlet.Cmd;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleMenuSaveCmd extends Command {

    public RedactionArticleMenuSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.REDACTION_MENU)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        String parDescription = Validator.getString(getRequest(), "description", 0, 50, false, false);
        int parParentId = Validator.getInt(getRequest(), "parent", 0, Validator.MAX_INT_ALLOWED, false);
        boolean parHide = Validator.getBoolean(getRequest(), "hide");

        MenuDAO menuDao = new MenuDAO(getCnn());
        MenuNode n = new MenuNode();
        
        n.setId(parId);
        n.setDescription(parDescription);
        n.setHide(parHide);
        n.setParentId(parParentId);
        //-- disable recursive join
        if ((n.getId() != 0) && (n.getId() == n.getParentId())) {
            n.setParentId(0);
        }
        //--
        if (n.getId() == 0) {
            n.setId(menuDao.insertMenuNode(n, getUser().getCompanyId()));
        } else {
            menuDao.updateMenuNode(n, getUser().getCompanyId());
        }
        String url = String.format("Dispatcher?page=%s", Cmd.REDACTION_MENU);
        getResponse().sendRedirect(url);
    }
}
