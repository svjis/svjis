/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.ArticleListInfo;
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

        String parSection = getRequest().getParameter("section");
        String parPageNo = getRequest().getParameter("pageNo");
        String parRoleId = getRequest().getParameter("roleId");
        
        if (!validateInput(parSection, parPageNo, parRoleId)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        MenuDAO menuDao = new MenuDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        RoleDAO roleDao = new RoleDAO(getCnn());

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
        int roleId = Integer.valueOf((parRoleId == null) ? "0" : parRoleId);
        SliderImpl sl = new SliderImpl();
        sl.setSliderWide(10);
        sl.setCurrentPage(pageNo);
        sl.setNumOfItemsAtPage(Integer.valueOf(getSetup().getProperty("article.page.size")));
        sl.setTotalNumOfItems(articleDao.getNumOfArticles(getUser(), section, false, !getUser().hasPermission("redaction_articles_all"), roleId));
        getRequest().setAttribute("slider", sl);
        ArrayList<Article> articleList = articleDao.getArticleList(
                getUser(),
                section,
                pageNo,
                Integer.valueOf(getSetup().getProperty("article.page.size")),
                false,
                !getUser().hasPermission("redaction_articles_all"),
                roleId);
        getRequest().setAttribute("articleList", articleList);

        ArticleListInfo articleListInfo = new ArticleListInfo();
        articleListInfo.setNumOfArticles(articleDao.getNumOfArticles(
                getUser(),
                section,
                false,
                !getUser().hasPermission("redaction_articles_all"),
                roleId));
        articleListInfo.setPageSize(Integer.valueOf(getSetup().getProperty("article.page.size")));
        articleListInfo.setActualPage(pageNo);
        articleListInfo.setMenuNodeId(section);
        getRequest().setAttribute("articleListInfo", articleListInfo);
        ArrayList<Role> roleList = roleDao.getRoleList(getCompany().getId());
        getRequest().setAttribute("roleList", roleList);

        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_ArticleList.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parSection, String parPageNo, String parRoleId) {
        boolean result = true;
        
        //-- parArticleId can be null
        if ((parSection != null) && !Validator.validateInteger(parSection, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        //-- parPageNo can be null
        if ((parPageNo != null) && !Validator.validateInteger(parPageNo, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        //-- parRoleId can be null
        if ((parRoleId != null) && !Validator.validateInteger(parRoleId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        return result;
    }
}
