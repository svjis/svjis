/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleAttachmentDeleteCmd extends Command {

    public RedactionArticleAttachmentDeleteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        int id = Integer.parseInt(getRequest().getParameter("id"));
        int articleId = Integer.parseInt(getRequest().getParameter("articleId"));
        articleDao.deleteArticleAttachment(id);
        String url = "Dispatcher?page=redactionArticleEdit&id=" + articleId;
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
        logDao.log(getUser().getId(), LogDAO.operationTypeDeleteAttachment, articleId, getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
    }
}
