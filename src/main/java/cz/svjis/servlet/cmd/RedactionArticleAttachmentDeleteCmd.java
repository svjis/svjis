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
import cz.svjis.validator.Validator;
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

        String parId = getRequest().getParameter("id");
        String parArticleId = getRequest().getParameter("articleId");
        
        if (!validateInput(parId, parArticleId)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        int id = Integer.parseInt(parId);
        int articleId = Integer.parseInt(parArticleId);
        articleDao.deleteArticleAttachment(id);
        String url = "Dispatcher?page=redactionArticleEdit&id=" + articleId;
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
        logDao.log(getUser().getId(), LogDAO.operationTypeDeleteAttachment, articleId, getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
    }
    
    private boolean validateInput(String parId, String parArticleId) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateInteger(parArticleId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        return result;
    }
}
