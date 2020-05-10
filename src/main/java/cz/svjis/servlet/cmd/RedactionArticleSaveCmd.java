/*
 *       RedactionArticleSaveCmd.java
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
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleSaveCmd extends Command {

    public RedactionArticleSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        String parHeader = Validator.getString(getRequest(), "header", 0, 50, false, false);
        String parDescription = Validator.getString(getRequest(), "description", 0, Validator.MAX_STRING_LEN_ALLOWED, false, true);
        String parBody = Validator.getString(getRequest(), "body", 0, Validator.MAX_STRING_LEN_ALLOWED, false, true);
        int parLangId = Validator.getInt(getRequest(), "language", 0, Validator.MAX_INT_ALLOWED, false);
        int parAuthorId = Validator.getInt(getRequest(), "authorId", 0, Validator.MAX_INT_ALLOWED, false);
        String parCreationDate = Validator.getString(getRequest(), "creationDate", 0, 30, false, false);
        int parMenuId = Validator.getInt(getRequest(), "menuId", 0, Validator.MAX_INT_ALLOWED, false);
        boolean parCommentsAllowed = Validator.getBoolean(getRequest(), "commentsAllowed");
        boolean parPublish = Validator.getBoolean(getRequest(), "publish");

        RoleDAO roleDao = new RoleDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());
        MenuDAO menuDao = new MenuDAO(getCnn());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Article a = new Article();
        a.setId(parId);
        a.setCompanyId(getCompany().getId());
        a.setHeader(parHeader);
        a.setDescription(parDescription);
        a.setBody(parBody);
        a.setLanguageId(parLangId);
        a.setCommentsAllowed(parCommentsAllowed);
        a.setPublished(parPublish);
        a.setAuthorId((parAuthorId == 0) ? getUser().getId() : parAuthorId);
        a.setCreationDate(sdf.parse(parCreationDate));
        a.setMenuNodeId(parMenuId);

        HashMap uRoles = new HashMap();
        ArrayList<Role> roles = new ArrayList(roleDao.getRoleList(getCompany().getId()));
        Iterator<Role> roleI = roles.iterator();
        while (roleI.hasNext()) {
            Role r = roleI.next();
            if (getRequest().getParameter("r_" + r.getId()) != null) {
                uRoles.put(r.getId(), r.getDescription());
            }
        }
        a.setRoles(uRoles);
        if (a.getId() == 0) {
            a.setId(articleDao.insertArticle(a));
            articleDao.setUserWatchingArticle(a.getId(), getUser().getId());
            logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_CREATE_ARTICLE, a.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
        } else {
            //-- check permission
            Article article = articleDao.getArticle(getUser(), a.getId());
            if ((article.getAuthor().getId() != getUser().getId()) && !getUser().hasPermission("redaction_articles_all")) {
                Menu menu = menuDao.getMenu(getCompany().getId());
                getRequest().setAttribute("menu", menu);
                RequestDispatcher rd = getRequest().getRequestDispatcher("/ArticleNotFound.jsp");
                rd.forward(getRequest(), getResponse());
                return;
            }
            //-- save
            articleDao.modifyArticle(a);
            logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_MODIFY_ARTICLE, a.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
        }
        String url = "Dispatcher?page=redactionArticleEdit&id=" + a.getId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
