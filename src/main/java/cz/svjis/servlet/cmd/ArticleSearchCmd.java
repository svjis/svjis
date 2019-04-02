/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.ArticleListInfo;
import cz.svjis.bean.Company;
import cz.svjis.bean.Inquiry;
import cz.svjis.bean.InquiryDAO;
import cz.svjis.bean.Menu;
import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.MiniNews;
import cz.svjis.bean.MiniNewsDAO;
import cz.svjis.bean.SliderImpl;
import cz.svjis.bean.User;
import cz.svjis.servlet.ICommand;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaroslav_b
 */
public class ArticleSearchCmd implements ICommand {

    private Company company;
    private Properties setup;
    private User user;

    @Override
    public void run(HttpServletRequest request, HttpServletResponse response, Connection cnn) throws Exception {

        MenuDAO menuDao = new MenuDAO(cnn);
        ArticleDAO articleDao = new ArticleDAO(cnn);
        MiniNewsDAO newsDao = new MiniNewsDAO(cnn);
        InquiryDAO inquiryDao = new InquiryDAO(cnn);

        String search = request.getParameter("search");

        Menu menu = menuDao.getMenu(getCompany().getId());
        int section = 0;
        if (request.getParameter("section") != null) {
            section = Integer.valueOf(request.getParameter("section"));
        }
        menu.setActiveSection(section);
        request.setAttribute("menu", menu);

        int pageNo = 1;
        if (request.getParameter("pageNo") != null) {
            pageNo = Integer.valueOf(request.getParameter("pageNo"));
        }
        SliderImpl sl = new SliderImpl();
        sl.setSliderWide(10);
        sl.setCurrentPage(pageNo);
        sl.setNumOfItemsAtPage(Integer.valueOf(getSetup().getProperty("article.page.size")));
        sl.setTotalNumOfItems(articleDao.getNumOfArticlesFromSearch(search, getUser(), section, true, false));
        request.setAttribute("slider", sl);
        ArrayList<Article> articleList = articleDao.getArticleListFromSearch(search, getUser(),
                section,
                pageNo,
                Integer.valueOf(getSetup().getProperty("article.page.size")),
                true, false);
        request.setAttribute("articleList", articleList);
        ArrayList<Article> articleTopList = articleDao.getArticleTopList(getUser(), Integer.valueOf(getSetup().getProperty("article.top.size")));
        request.setAttribute("articleTopList", articleTopList);
        ArticleListInfo articleListInfo = new ArticleListInfo();
        articleListInfo.setNumOfArticles(articleDao.getNumOfArticlesFromSearch(search, getUser(),
                section,
                true, false));
        articleListInfo.setPageSize(Integer.valueOf(getSetup().getProperty("article.page.size")));
        articleListInfo.setActualPage(pageNo);
        articleListInfo.setMenuNodeId(section);
        request.setAttribute("articleListInfo", articleListInfo);
        ArrayList<MiniNews> miniNewsList = newsDao.getMiniNews(getUser(), true);
        request.setAttribute("miniNewsList", miniNewsList);
        ArrayList<Inquiry> inquiryList = inquiryDao.getInquiryList(getUser(), true);
        request.setAttribute("inquiryList", inquiryList);
        RequestDispatcher rd = request.getRequestDispatcher("/ArticleList.jsp");
        rd.forward(request, response);
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
