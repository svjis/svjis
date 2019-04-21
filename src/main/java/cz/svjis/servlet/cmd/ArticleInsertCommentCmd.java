/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleComment;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.User;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.RequestDispatcher;

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
        
        String parId = getRequest().getParameter("id");
        String parBody = getRequest().getParameter("body");
        
        if (!validateInput(parId, parBody)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/BadPage.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }

        ArticleDAO articleDao = new ArticleDAO(getCnn());

        int articleId = Integer.valueOf(parId);
        Article article = articleDao.getArticle(getUser(),
                articleId);
        getRequest().setAttribute("article", article);

        if ((article != null)
                && article.isCommentsAllowed()
                && getUser().hasPermission("can_insert_article_comment")
                && (parBody != null)
                && (!parBody.equals(""))) {

            // insert comment
            ArticleComment ac = new ArticleComment();
            ac.setArticleId(article.getId());
            ac.setUserId(getUser().getId());
            ac.setInsertionTime(new Date());
            ac.setBody(parBody);
            articleDao.insertArticleComment(ac);

            // send notification
            String subject = getCompany().getInternetDomain() + ": " + article.getHeader() + " (New comment)";
            MailDAO mailDao = new MailDAO(
                    getCnn(),
                    getSetup().getProperty("mail.smtp"),
                    getSetup().getProperty("mail.login"),
                    getSetup().getProperty("mail.password"),
                    getSetup().getProperty("mail.sender"));

            ArrayList<User> userList = articleDao.getUserListForNotificationAboutNewComment(article.getId());
            for (User u : userList) {
                String body = getSetup().getProperty("mail.template.comment.notification");
                body = String.format(body,
                        getUser().getFirstName() + " " + getUser().getLastName(),
                        "<a href=\"http://" + getCompany().getInternetDomain() + "/Dispatcher?page=articleDetail&id=" + article.getId() + "\">" + article.getHeader() + "</a>",
                        ac.getBody().replace("\n", "<br>"));
                mailDao.queueMail(getCompany().getId(), u.geteMail(), subject, body);
            }
        }

        String url = "Dispatcher?page=articleDetail&id=" + article.getId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    
    private boolean validateInput(String id, String body) {
        boolean result = true;
        
        if (!Validator.validateInteger(id, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if ((body != null) && !Validator.validateString(body, 0, 10000)) {
            result = false;
        }
        
        return result;
    }
}
