/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        
        int parSection = Validator.getInt(getRequest(), "section", 0, Validator.maxIntAllowed, true);
        int parPageNo = Validator.getInt(getRequest(), "pageNo", 0, Validator.maxIntAllowed, true);
        
        MenuDAO menuDao = new MenuDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        Menu menu = menuDao.getMenu(getCompany().getId());
        int section = parSection;
        if ((section == 0) && (getSetup().get("article.menu.default.item") != null)) {
            section = Integer.valueOf(getSetup().getProperty("article.menu.default.item"));
        }
        menu.setActiveSection(section);
        getRequest().setAttribute("menu", menu);

        int pageNo = (parPageNo == 0) ? 1 : parPageNo;
        SliderImpl sl = new SliderImpl();
        sl.setSliderWide(10);
        sl.setCurrentPage(pageNo);
        sl.setNumOfItemsAtPage(Integer.valueOf(getSetup().getProperty("article.page.size")));
        sl.setTotalNumOfItems(articleDao.getNumOfArticles(getUser(), section, true, false, 0));
        getRequest().setAttribute("slider", sl);
        ArrayList<Article> articleList = new ArrayList(articleDao.getArticleList(getUser(),
                section,
                pageNo,
                Integer.valueOf(getSetup().getProperty("article.page.size")),
                true, false, 0));
        getRequest().setAttribute("articleList", articleList);
        ArrayList<Article> articleTopList = new ArrayList(articleDao.getArticleTopList(getUser(),
                Integer.valueOf(getSetup().getProperty("article.top.size")),
                (getSetup().getProperty("article.top.months") != null) ? Integer.valueOf(getSetup().getProperty("article.top.months")) : 12));
        getRequest().setAttribute("articleTopList", articleTopList);
        getRequest().setAttribute("sectionId", String.valueOf(parSection));
        ArrayList<MiniNews> miniNewsList = newsDao.getMiniNews(getUser(), true);
        getRequest().setAttribute("miniNewsList", miniNewsList);
        ArrayList<Inquiry> inquiryList = new ArrayList(inquiryDao.getInquiryList(getUser(), true));
        getRequest().setAttribute("inquiryList", inquiryList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/ArticleList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
