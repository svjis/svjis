/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.MailDAO;
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

        String parId = getRequest().getParameter("id");
        
        if (!validateInput(parId)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());
        
        int articleId = 0;
        if (parId != null) {
            articleId = Integer.valueOf(parId);
        }
        
        Article article = null;
        if (articleId == 0) {
            article = new Article();
        } else {
            article = articleDao.getArticle(getUser(), articleId);
        }
        getRequest().setAttribute("article", article);

        String subject = getCompany().getInternetDomain() + ": " + article.getHeader();
        String body = getSetup().getProperty("mail.template.article.notification");
        body = String.format(body, "<a href=\"http://" + getCompany().getInternetDomain() + "/Dispatcher?page=articleDetail&id=" + article.getId() + "\">" + article.getHeader() + "</a>");
        MailDAO mailDao = new MailDAO(
                getCnn(),
                getSetup().getProperty("mail.smtp"),
                getSetup().getProperty("mail.login"),
                getSetup().getProperty("mail.password"),
                getSetup().getProperty("mail.sender"));

        int counter = 0;
        ArrayList<User> userList = articleDao.getUserListForNotificationAboutNewArticle(articleId);
        Iterator<User> it = userList.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if (getRequest().getParameter("u_" + u.getId()) != null) {
                mailDao.queueMail(getCompany().getId(), u.geteMail(), subject, body);
                counter++;
            }
        }
        
        logDao.log(getUser().getId(), LogDAO.operationTypeSendArticleNotification, article.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
        article.setNumOfReads(counter);

        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_ArticleSendNotificationsConfirmation.jsp");
        rd.forward(getRequest(), getResponse());
    }

    private boolean validateInput(String parId) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }

        return result;
    }
}
