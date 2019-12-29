/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.ArticleAttachment;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.common.HttpUtils;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class DownloadCmd extends Command {

    public DownloadCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        String parId = getRequest().getParameter("id");
        
        if (!validateInput(parId)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        ArticleDAO dao = new ArticleDAO(getCnn());
        ArticleAttachment aa = dao.getArticleAttachment(Integer.parseInt(parId));
        if ((aa == null) || (dao.getArticle(getUser(), aa.getArticleId()) == null)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        HttpUtils.writeBinaryData(aa.getContentType(), aa.getFileName(), aa.getData(), getRequest(), getResponse());
    }
    
    private boolean validateInput(String id) {
        boolean result = true;
        
        if (!Validator.validateInteger(id, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        return result;
    }
}
