/*
 *       ArticleDetailCmd.java
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
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.Menu;
import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.Permission;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class ArticleDetailCmd extends Command {

    public ArticleDetailCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.MENU_ARTICLES)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        Validator.getString(getRequest(), "search", 0, 50, true, false);
        String parSearch = Validator.getString(getRequest(), "search", 0, Validator.MAX_STRING_LEN_ALLOWED, true, false);

        MenuDAO menuDao = new MenuDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        int articleId = parId;
        Article article = articleDao.getArticle(getUser(),
                articleId);
        if ((article == null) || (article.getId() == 0)) {
            new Error404ArticleNotFoundCmd(getCtx()).execute();
            return;
        }
        getRequest().setAttribute("article", article);
        getRequest().setAttribute("searchKey", parSearch);
        
        Menu menu = menuDao.getMenu(getCompany().getId());
        menu.setActiveSection(article.getMenuNodeId());
        int defaultSection = getSetup().getMenuDefaultItem();
        menu.setDefaultSection(defaultSection);
        getRequest().setAttribute("menu", menu);
        
        String watching = (articleDao.isUserWatchingArticle(article.getId(), getUser().getId())) ? "1" : "0";
        getRequest().setAttribute("watching", watching);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/ArticleDetail.jsp");
        rd.forward(getRequest(), getResponse());
        logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_READ, article.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
    }
}
