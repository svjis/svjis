/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleComment;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.Company;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.User;
import cz.svjis.servlet.ICommand;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaroslav_b
 */
public class ArticleInsertCommentCmd implements ICommand {

    private Company company;
    private Properties setup;
    private User user;

    @Override
    public void run(HttpServletRequest request, HttpServletResponse response, Connection cnn) throws Exception {

        ArticleDAO articleDao = new ArticleDAO(cnn);

        int articleId = Integer.valueOf(request.getParameter("id"));
        Article article = articleDao.getArticle(getUser(),
                articleId);
        request.setAttribute("article", article);

        if ((article != null)
                && article.isCommentsAllowed()
                && getUser().hasPermission("can_insert_article_comment")
                && (request.getParameter("body") != null)
                && (!request.getParameter("body").equals(""))) {

            // insert comment
            ArticleComment ac = new ArticleComment();
            ac.setArticleId(article.getId());
            ac.setUserId(getUser().getId());
            ac.setInsertionTime(new Date());
            ac.setBody(request.getParameter("body"));
            articleDao.insertArticleComment(ac);

            // send notification
            String subject = getCompany().getInternetDomain() + ": " + article.getHeader() + " (New comment)";
            MailDAO mailDao = new MailDAO(
                    cnn,
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
        request.setAttribute("url", url);
        RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
        rd.forward(request, response);
        return;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the company
     */
    public Company getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * @return the setup
     */
    public Properties getSetup() {
        return setup;
    }

    /**
     * @param setup the setup to set
     */
    public void setSetup(Properties setup) {
        this.setup = setup;
    }

}
