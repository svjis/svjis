/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class ArticleFastCmd extends FaultAbstractCmd {
    
    public ArticleFastCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);
        boolean parWatch = Validator.getBoolean(getRequest(), "watch");

        ArticleDAO articleDao = new ArticleDAO(getCnn());

        Article a = articleDao.getArticle(getUser(), parId);
        
        if ((getRequest().getParameter("watch") != null) && (a != null)) {
            if (parWatch) {
                articleDao.setUserWatchingArticle(a.getId(), getUser().getId());
            } else {
                articleDao.unsetUserWatchingArticle(a.getId(), getUser().getId());
            }
        }

        String url = "Dispatcher?page=articleDetail&id=" + parId;
        getRequest().setAttribute("url", url);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
