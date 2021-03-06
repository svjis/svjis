/*
 *       ArticleInsertCommentCmd.java
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
import cz.svjis.bean.ArticleComment;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.Permission;
import cz.svjis.bean.User;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author jaroslav_b
 */
public class ArticleInsertCommentCmd extends Command {

    public ArticleInsertCommentCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        String parBody = Validator.getString(getRequest(), "body", 0, 10000, true, getUser().hasPermission(Permission.CAN_WRITE_HTML));

        ArticleDAO articleDao = new ArticleDAO(getCnn());

        int articleId = parId;
        Article article = articleDao.getArticle(getUser(), articleId);
        getRequest().setAttribute("article", article);

        if ((article != null)
                && article.isCommentsAllowed()
                && getUser().hasPermission(Permission.CAN_INSERT_ARTICLE_COMMENT)
                && (parBody != null)
                && (!parBody.equals(""))) {

            // insert comment
            ArticleComment ac = new ArticleComment();
            ac.setArticleId(article.getId());
            ac.setUser(getUser());
            ac.setInsertionTime(new Date());
            ac.setBody(parBody);
            articleDao.insertArticleComment(ac);
            articleDao.setUserWatchingArticle(article.getId(), getUser().getId());

            // send notification
            String subject = getCompany().getInternetDomain() + ": " + article.getHeader() + " (New comment)";
            String tBody = getSetup().getMailTemplateCommentNotification();
            MailDAO mailDao = new MailDAO(
                    getCnn(),
                    getSetup().getMailSmtp(),
                    getSetup().getMailLogin(),
                    getSetup().getMailPassword(),
                    getSetup().getMailSender());

            ArrayList<User> userList = new ArrayList(articleDao.getUserListWatchingArticle(article.getId()));
            for (User u : userList) {
                if (u.getId() == getUser().getId()) {
                    continue;
                }
                String body = String.format(tBody,
                        getUser().getFirstName() + " " + getUser().getLastName(),
                        "<a href=\"http://" + getCompany().getInternetDomain() + "/Dispatcher?page=articleDetail&id=" + article.getId() + "\">" + article.getHeader() + "</a>",
                        ac.getBody().replace("\n", "<br>"));
                mailDao.queueMail(getCompany().getId(), u.geteMail(), subject, body);
            }
        }

        String url = "Dispatcher?page=articleDetail&id=" + articleId;
        getResponse().sendRedirect(url);
    }
}
