/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.Company;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.Menu;
import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.User;
import cz.svjis.servlet.ICommand;
import java.sql.Connection;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaroslav_b
 */
public class ArticleDetailCmd implements ICommand {

    private Company company;
    private User user;

    @Override
    public void run(HttpServletRequest request, HttpServletResponse response, Connection cnn) throws Exception {

        MenuDAO menuDao = new MenuDAO(cnn);
        ArticleDAO articleDao = new ArticleDAO(cnn);
        LogDAO logDao = new LogDAO(cnn);

        int articleId = 0;
        if (request.getParameter("id") != null) {
            articleId = Integer.valueOf(request.getParameter("id"));
        }
        Article article = articleDao.getArticle(getUser(),
                articleId);
        if ((article == null) || (article.getId() == 0)) {
            Menu menu = menuDao.getMenu(getCompany().getId());
            request.setAttribute("menu", menu);
            RequestDispatcher rd = request.getRequestDispatcher("/ArticleNotFound.jsp");
            rd.forward(request, response);
            return;
        }
        request.setAttribute("article", article);

        Menu menu = menuDao.getMenu(getCompany().getId());
        menu.setActiveSection(article.getMenuNodeId());
        request.setAttribute("menu", menu);
        RequestDispatcher rd = request.getRequestDispatcher("/ArticleDetail.jsp");
        rd.forward(request, response);
        logDao.log(getUser().getId(), LogDAO.operationTypeRead, article.getId(), request.getRemoteAddr(), request.getHeader("User-Agent"));
        return;
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
}
