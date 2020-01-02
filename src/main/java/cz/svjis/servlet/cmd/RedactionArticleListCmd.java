/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.Menu;
import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
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
public class RedactionArticleListCmd extends Command {

    public RedactionArticleListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        String parPage = Validator.getString(getRequest(), "page", 0, 100, false, false);
        int parSection = Validator.getInt(getRequest(), "section", 0, Validator.maxIntAllowed, true);
        int parPageNo = Validator.getInt(getRequest(), "pageNo", 0, Validator.maxIntAllowed, true);
        int parRoleId = Validator.getInt(getRequest(), "roleId", 0, Validator.maxIntAllowed, true);
        String parSearch = Validator.getString(getRequest(), "search", 0, Validator.maxStringLenAllowed, true, false);

        MenuDAO menuDao = new MenuDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        RoleDAO roleDao = new RoleDAO(getCnn());

        Menu menu = menuDao.getMenu(getCompany().getId());
        menu.setActiveSection(parSection);
        getRequest().setAttribute("menu", menu);

        parPageNo = (parPageNo == 0) ? 1 : parPageNo;
        SliderImpl sl = new SliderImpl();
        sl.setSliderWide(10);
        sl.setCurrentPage(parPageNo);
        sl.setNumOfItemsAtPage(Integer.valueOf(getSetup().getProperty("article.page.size")));

        ArrayList<Article> articleList = new ArrayList<Article>();
        if (parPage.equals("redactionArticleList")) {
            sl.setTotalNumOfItems(articleDao.getNumOfArticles(getUser(), parSection, false, !getUser().hasPermission("redaction_articles_all"), parRoleId));
            articleList = articleDao.getArticleList(
                getUser(),
                parSection,
                parPageNo,
                Integer.valueOf(getSetup().getProperty("article.page.size")),
                false,
                !getUser().hasPermission("redaction_articles_all"),
                parRoleId);
        }

        if (parPage.equals("redactionArticleSearch")) {
            sl.setTotalNumOfItems(articleDao.getNumOfArticlesFromSearch(parSearch, getUser(), parSection, false, !getUser().hasPermission("redaction_articles_all")));
            articleList = articleDao.getArticleListFromSearch(parSearch, getUser(),
                parSection,
                parPageNo,
                Integer.valueOf(getSetup().getProperty("article.page.size")),
                false,
                !getUser().hasPermission("redaction_articles_all"));
        }

        getRequest().setAttribute("slider", sl);
        getRequest().setAttribute("articleList", articleList);

        ArrayList<Role> roleList = roleDao.getRoleList(getCompany().getId());
        getRequest().setAttribute("roleList", roleList);

        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_ArticleList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
