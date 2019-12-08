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
import cz.svjis.validator.Validator;
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
        
        String parSection = getRequest().getParameter("section");
        String parPageNo = getRequest().getParameter("pageNo");
        String parSearch = Validator.fixTextInput(getRequest().getParameter("search"), false);
        
        if (!validateInput(parSection, parPageNo, parSearch)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }

        MenuDAO menuDao = new MenuDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        Menu menu = menuDao.getMenu(getCompany().getId());
        int section = 0;
        if (parSection != null) {
            section = Integer.valueOf(parSection);
        }
        menu.setActiveSection(section);
        getRequest().setAttribute("menu", menu);

        int pageNo = 1;
        if (parPageNo != null) {
            pageNo = Integer.valueOf(parPageNo);
        }
        SliderImpl sl = new SliderImpl();
        sl.setSliderWide(10);
        sl.setCurrentPage(pageNo);
        sl.setNumOfItemsAtPage(Integer.valueOf(getSetup().getProperty("article.page.size")));
        sl.setTotalNumOfItems(articleDao.getNumOfArticlesFromSearch(parSearch, getUser(), section, true, false));
        getRequest().setAttribute("slider", sl);
        ArrayList<Article> articleList = articleDao.getArticleListFromSearch(parSearch, getUser(),
                section,
                pageNo,
                Integer.valueOf(getSetup().getProperty("article.page.size")),
                true, false);
        getRequest().setAttribute("articleList", articleList);
        ArrayList<Article> articleTopList = articleDao.getArticleTopList(getUser(), Integer.valueOf(getSetup().getProperty("article.top.size")));
        getRequest().setAttribute("articleTopList", articleTopList);
        ArticleListInfo articleListInfo = new ArticleListInfo();
        articleListInfo.setNumOfArticles(articleDao.getNumOfArticlesFromSearch(parSearch, getUser(),
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
    
    private boolean validateInput(String section, String pageNo, String search) {
        boolean result = true;
        
        //-- section can be null
        if ((section != null) && !Validator.validateInteger(section, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        //-- pageNo can be null
        if ((pageNo != null) && !Validator.validateInteger(pageNo, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(search, 0, 50)) {
            result = false;
        }
        
        return result;
    }
}
