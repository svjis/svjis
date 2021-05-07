/*
 *       RedactionArticleSendNotificationsCmd.java
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
import cz.svjis.bean.User;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleSendNotificationsCmd extends Command {

    public RedactionArticleSendNotificationsCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.REDACTION_ARTICLES) && !getUser().hasPermission(Permission.REDACTION_ARTICLES_ALL)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        ArticleDAO articleDao = new ArticleDAO(getCnn());
        
        Article article = articleDao.getArticle(getUser(), parId);
        if (article == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        if ((article.getAuthor().getId() != getUser().getId()) && !getUser().hasPermission(Permission.REDACTION_ARTICLES_ALL)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        
        getRequest().setAttribute("article", article);

        ArrayList<User> userList = new ArrayList<>(articleDao.getUserListForNotificationAboutNewArticle(parId));
        RequestDispatcher rd;
        
        if (userList.isEmpty()) {
            getRequest().setAttribute("messageHeader", getLanguage().getText("This article is not visible to any user"));
            getRequest().setAttribute("message", "<p>" + getLanguage().getText("You can continue") + " <a href=\"javascript:history.go(-1)\">" + getLanguage().getText("here") + "</a>.</p>");
            rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/_message.jsp");
        } else {
            getRequest().setAttribute("userList", userList);
            rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Redaction_ArticleSendNotifications.jsp");
        }
        
        rd.forward(getRequest(), getResponse());
    }
}
