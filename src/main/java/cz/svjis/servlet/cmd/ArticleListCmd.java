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
public class ArticleListCmd extends Command {
    
    public ArticleListCmd(CmdContext ctx) {
        super(ctx);
    }    

    @Override
    public void execute() throws Exception {
        
        int parSection = Validator.getInt(getRequest(), "section", 0, Validator.MAX_INT_ALLOWED, true);
        int parPageNo = Validator.getInt(getRequest(), "pageNo", 0, Validator.MAX_INT_ALLOWED, true);
        
        MenuDAO menuDao = new MenuDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

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
        sl.setSliderWide(10);
        sl.setCurrentPage(pageNo);
        sl.setNumOfItemsAtPage(getSetup().getArticlePageSize());
        sl.setTotalNumOfItems(articleDao.getNumOfArticles(getUser(), section, true, false, 0));
        getRequest().setAttribute("slider", sl);
        ArrayList<Article> articleList = new ArrayList<>(articleDao.getArticleList(getUser(),
                section,
                pageNo,
                getSetup().getArticlePageSize(),
                true, false, 0));
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
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/ArticleList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
