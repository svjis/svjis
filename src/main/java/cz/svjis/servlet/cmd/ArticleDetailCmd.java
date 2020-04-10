/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.Menu;
import cz.svjis.bean.MenuDAO;
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
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        Validator.getString(getRequest(), "search", 0, 50, true, false);

        MenuDAO menuDao = new MenuDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        int articleId = parId;
        Article article = articleDao.getArticle(getUser(),
                articleId);
        if ((article == null) || (article.getId() == 0)) {
            Menu menu = menuDao.getMenu(getCompany().getId());
            getRequest().setAttribute("menu", menu);
            RequestDispatcher rd = getRequest().getRequestDispatcher("/ArticleNotFound.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        getRequest().setAttribute("article", article);

        Menu menu = menuDao.getMenu(getCompany().getId());
        menu.setActiveSection(article.getMenuNodeId());
        int defaultSection = (getSetup().get("article.menu.default.item") != null) ? Integer.valueOf(getSetup().getProperty("article.menu.default.item")) : 0;
        menu.setDefaultSection(defaultSection);
        getRequest().setAttribute("menu", menu);
        
        String watching = (articleDao.isUserWatchingArticle(article.getId(), getUser().getId())) ? "1" : "0";
        getRequest().setAttribute("watching", watching);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/ArticleDetail.jsp");
        rd.forward(getRequest(), getResponse());
        logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_READ, article.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
    }
}
