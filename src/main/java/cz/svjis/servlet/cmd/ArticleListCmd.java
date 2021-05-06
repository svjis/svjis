/*
 *       ArticleListCmd.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.Inquiry;
import cz.svjis.bean.InquiryDAO;
import cz.svjis.bean.Language;
import cz.svjis.bean.Menu;
import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.MiniNews;
import cz.svjis.bean.MiniNewsDAO;
import cz.svjis.bean.Permission;
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
public class ArticleListCmd extends Command {
    
    public ArticleListCmd(CmdContext ctx) {
        super(ctx);
    }    

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.MENU_ARTICLES)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        
        String parPage = Validator.getString(getRequest(), "page", 0, Validator.MAX_STRING_LEN_ALLOWED, true, false);
        int parSection = Validator.getInt(getRequest(), "section", 0, Validator.MAX_INT_ALLOWED, true);
        int parPageNo = Validator.getInt(getRequest(), "pageNo", 0, Validator.MAX_INT_ALLOWED, true);
        String parSearch = Validator.getString(getRequest(), "search", 0, 50, true, false);
        
        MenuDAO menuDao = new MenuDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());
        
        if (parPage == null) 
            parPage = "articleList";
        
        if ((parSearch != null) && (parSearch.length() < 3)) {
            Language lang = (Language) this.getRequest().getSession().getAttribute("language");
            getRequest().setAttribute("messageHeader", lang.getText("Search"));
            getRequest().setAttribute("message", lang.getText("Text to be searched should has 3 chars at least."));
            RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/_message.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }

        Menu menu = menuDao.getMenu(getCompany().getId());
        int section = parSection;
        int defaultSection = getSetup().getMenuDefaultItem();
        if ((section == 0) && (defaultSection != 0)) {
            section = defaultSection;
        }
        menu.setActiveSection(section);
        menu.setDefaultSection(defaultSection);
        getRequest().setAttribute("menu", menu);

        int pageNo = (parPageNo == 0) ? 1 : parPageNo;
        SliderImpl sl = new SliderImpl();
        sl.setPageId(parPage);
        sl.setSliderWide(10);
        sl.setCurrentPage(pageNo);
        sl.setNumOfItemsAtPage(getSetup().getArticlePageSize());
        if (parSearch != null) {
            sl.setTotalNumOfItems(articleDao.getNumOfArticlesFromSearch(parSearch, getUser(), section, true, false));
            if (sl.getTotalNumOfItems() == 0) {
                Language lang = (Language) this.getRequest().getSession().getAttribute("language");
                getRequest().setAttribute("messageHeader", lang.getText("Search"));
                getRequest().setAttribute("message", lang.getText("Nothing found."));
                RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/_message.jsp");
                rd.forward(getRequest(), getResponse());
                return;
            }
        } else {
            sl.setTotalNumOfItems(articleDao.getNumOfArticles(getUser(), section, true, false, 0));
        }
        getRequest().setAttribute("slider", sl);
        
        ArrayList<Article> articleList;
        if (parSearch != null) {
            articleList = new ArrayList<>(articleDao.getArticleListFromSearch(parSearch, getUser(),
                    section,
                    pageNo,
                    getSetup().getArticlePageSize(),
                    true, false));
        } else {
            articleList = new ArrayList<>(articleDao.getArticleList(getUser(),
                    section,
                    pageNo,
                    getSetup().getArticlePageSize(),
                    true, false, 0));
        }
        getRequest().setAttribute("articleList", articleList);
        
        ArrayList<Article> articleTopList = new ArrayList<>(articleDao.getArticleTopList(getUser(),
                getSetup().getArticleTopSize(),
                getSetup().getArticleTopMonths()));
        getRequest().setAttribute("articleTopList", articleTopList);
        getRequest().setAttribute("sectionId", String.valueOf(parSection));
        ArrayList<MiniNews> miniNewsList = new ArrayList<>(newsDao.getMiniNewsList(1 ,getSetup().getArticlePageSize() ,getUser(), true));
        getRequest().setAttribute("miniNewsList", miniNewsList);
        ArrayList<Inquiry> inquiryList = new ArrayList<>(inquiryDao.getInquiryList(1, getSetup().getArticlePageSize(), getUser(), true));
        getRequest().setAttribute("inquiryList", inquiryList);
        
        if (parSearch != null)
            getRequest().setAttribute("searchKey", parSearch);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/ArticleList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
