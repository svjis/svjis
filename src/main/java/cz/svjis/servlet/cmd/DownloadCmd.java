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
import cz.svjis.validator.InputValidationException;
import cz.svjis.validator.Validator;

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
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);

        ArticleDAO dao = new ArticleDAO(getCnn());
        ArticleAttachment aa = dao.getArticleAttachment(parId);
        if ((aa == null) || (dao.getArticle(getUser(), aa.getArticleId()) == null)) {
            throw new InputValidationException("Bad attachment id");
        }
        HttpUtils.writeBinaryData(aa.getContentType(), aa.getFileName(), aa.getData(), getRequest(), getResponse());
    }
}
