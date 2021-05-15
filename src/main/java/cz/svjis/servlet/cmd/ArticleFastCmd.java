/*
 *       ArticleFastCmd.java
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

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.Permission;
import cz.svjis.servlet.Cmd;
import cz.svjis.servlet.CmdContext;
import cz.svjis.validator.Validator;

/**
 *
 * @author jarberan
 */
public class ArticleFastCmd extends FaultAbstractCmd {
    
    public ArticleFastCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.MENU_ARTICLES)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        boolean parWatch = Validator.getBoolean(getRequest(), "watch");

        ArticleDAO articleDao = new ArticleDAO(getCnn());

        Article a = articleDao.getArticle(getUser(), parId);
        if (a == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        
        if (getRequest().getParameter("watch") != null) {
            if (parWatch) {
                articleDao.setUserWatchingArticle(a.getId(), getUser().getId());
            } else {
                articleDao.unsetUserWatchingArticle(a.getId(), getUser().getId());
            }
        }

        String url = String.format("Dispatcher?page=%s&id=%d", Cmd.ARTICLE_DETAIL, parId);
        getResponse().sendRedirect(url);
    }
}
