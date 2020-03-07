/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
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

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        ArticleDAO articleDao = new ArticleDAO(getCnn());
        
        Article article = null;
        
        if (parId == 0) {
            article = new Article();
        } else {
            article = articleDao.getArticle(getUser(), parId);
        }
        
        getRequest().setAttribute("article", article);

        ArrayList<User> userList = new ArrayList(articleDao.getUserListForNotificationAboutNewArticle(parId));
        RequestDispatcher rd;
        
        if (userList.isEmpty()) {
            getRequest().setAttribute("messageHeader", getLanguage().getText("This article is not visible to any user"));
            getRequest().setAttribute("message", "<p>" + getLanguage().getText("You can continue") + " <a href=\"javascript:history.go(-1)\">" + getLanguage().getText("here") + "</a>.</p>");
            rd = getRequest().getRequestDispatcher("/_message.jsp");
        } else {
            getRequest().setAttribute("userList", userList);
            rd = getRequest().getRequestDispatcher("/Redaction_ArticleSendNotifications.jsp");
        }
        
        rd.forward(getRequest(), getResponse());
    }
}
