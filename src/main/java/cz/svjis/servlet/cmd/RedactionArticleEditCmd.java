/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.MenuItem;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleEditCmd extends Command {

    public RedactionArticleEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parArticleId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, true);

        MenuDAO menuDao = new MenuDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        RoleDAO roleDao = new RoleDAO(getCnn());
        LanguageDAO languageDao = new LanguageDAO(getCnn());

        Article article;
        
        if (parArticleId == 0) {
            article = new Article();
        } else {
            article = articleDao.getArticle(getUser(), parArticleId);
        }
        
        getRequest().setAttribute("article", article);

        ArrayList<MenuItem> menuNodeList = new ArrayList(menuDao.getMenu(getCompany().getId()).getMenu());
        getRequest().setAttribute("menuNodeList", menuNodeList);
        ArrayList<Language> languageList = new ArrayList(languageDao.getLanguageList());
        getRequest().setAttribute("languageList", languageList);
        ArrayList<Role> roleList = new ArrayList(roleDao.getRoleList(getCompany().getId()));
        getRequest().setAttribute("roleList", roleList);

        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_ArticleEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
