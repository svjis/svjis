/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.ArticleListInfo;
import cz.svjis.bean.Inquiry;
import cz.svjis.bean.InquiryDAO;
import cz.svjis.bean.Menu;
import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.MiniNews;
import cz.svjis.bean.MiniNewsDAO;
import cz.svjis.bean.SliderImpl;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class ArticleSearchCmd extends Command {

    public ArticleSearchCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        MenuDAO menuDao = new MenuDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        String search = getRequest().getParameter("search");

        Menu menu = menuDao.getMenu(getCompany().getId());
        int section = 0;
        if (getRequest().getParameter("section") != null) {
            section = Integer.valueOf(getRequest().getParameter("section"));
        }
        menu.setActiveSection(section);
        getRequest().setAttribute("menu", menu);

        int pageNo = 1;
        if (getRequest().getParameter("pageNo") != null) {
            pageNo = Integer.valueOf(getRequest().getParameter("pageNo"));
        }
        SliderImpl sl = new SliderImpl();
        sl.setSliderWide(10);
        sl.setCurrentPage(pageNo);
        sl.setNumOfItemsAtPage(Integer.valueOf(getSetup().getProperty("article.page.size")));
        sl.setTotalNumOfItems(articleDao.getNumOfArticlesFromSearch(search, getUser(), section, true, false));
        getRequest().setAttribute("slider", sl);
        ArrayList<Article> articleList = articleDao.getArticleListFromSearch(search, getUser(),
                section,
                pageNo,
                Integer.valueOf(getSetup().getProperty("article.page.size")),
                true, false);
        getRequest().setAttribute("articleList", articleList);
        ArrayList<Article> articleTopList = articleDao.getArticleTopList(getUser(), Integer.valueOf(getSetup().getProperty("article.top.size")));
        getRequest().setAttribute("articleTopList", articleTopList);
        ArticleListInfo articleListInfo = new ArticleListInfo();
        articleListInfo.setNumOfArticles(articleDao.getNumOfArticlesFromSearch(search, getUser(),
                section,
                true, false));
        articleListInfo.setPageSize(Integer.valueOf(getSetup().getProperty("article.page.size")));
        articleListInfo.setActualPage(pageNo);
        articleListInfo.setMenuNodeId(section);
        getRequest().setAttribute("articleListInfo", articleListInfo);
        ArrayList<MiniNews> miniNewsList = newsDao.getMiniNews(getUser(), true);
        getRequest().setAttribute("miniNewsList", miniNewsList);
        ArrayList<Inquiry> inquiryList = inquiryDao.getInquiryList(getUser(), true);
        getRequest().setAttribute("inquiryList", inquiryList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/ArticleList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
