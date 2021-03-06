/*
 *       RedactionArticleSendNotificationsConfirmationCmd.java
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
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.Menu;
import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.Permission;
import cz.svjis.bean.User;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleSendNotificationsConfirmationCmd extends Command {

    public RedactionArticleSendNotificationsConfirmationCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());
        MenuDAO menuDao = new MenuDAO(getCnn());

        Article article;
        if (parId == 0) {
            article = new Article();
        } else {
            article = articleDao.getArticle(getUser(), parId);
            if ((article.getAuthor().getId() != getUser().getId()) && !getUser().hasPermission(Permission.REDACTION_ARTICLES_ALL)) {
                Menu menu = menuDao.getMenu(getCompany().getId());
                getRequest().setAttribute("menu", menu);
                RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/ArticleNotFound.jsp");
                rd.forward(getRequest(), getResponse());
                return;
            }
        }
        getRequest().setAttribute("article", article);

        String subject = getCompany().getInternetDomain() + ": " + article.getHeader();
        String body = getSetup().getMailTemplateArticleNotification();
        body = String.format(body, "<a href=\"http://" + getCompany().getInternetDomain() + "/Dispatcher?page=articleDetail&id=" + article.getId() + "\">" + article.getHeader() + "</a>");
        MailDAO mailDao = new MailDAO(
                getCnn(),
                getSetup().getMailSmtp(),
                getSetup().getMailLogin(),
                getSetup().getMailPassword(),
                getSetup().getMailSender());

        int counter = 0;
        ArrayList<User> userList = new ArrayList(articleDao.getUserListForNotificationAboutNewArticle(parId));
        Iterator<User> it = userList.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if (getRequest().getParameter("u_" + u.getId()) != null) {
                mailDao.queueMail(getCompany().getId(), u.geteMail(), subject, body);
                counter++;
            }
        }
        
        logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_SEND_ARTICLE_NOTIFICATION, article.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
        article.setNumOfReads(counter);

        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Redaction_ArticleSendNotificationsConfirmation.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
