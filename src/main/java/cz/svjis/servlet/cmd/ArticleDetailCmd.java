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

        MenuDAO menuDao = new MenuDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        int articleId = 0;
        if (getRequest().getParameter("id") != null) {
            articleId = Integer.valueOf(getRequest().getParameter("id"));
        }
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
        getRequest().setAttribute("menu", menu);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/ArticleDetail.jsp");
        rd.forward(getRequest(), getResponse());
        logDao.log(getUser().getId(), LogDAO.operationTypeRead, article.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
    }
}
