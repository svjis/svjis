/*
 *       RedactionArticleAttachmentDeleteCmd.java
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
import cz.svjis.bean.Attachment;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.Permission;
import cz.svjis.servlet.Cmd;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

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
        
        if (!getUser().hasPermission(Permission.REDACTION_ARTICLES) && !getUser().hasPermission(Permission.REDACTION_ARTICLES_ALL)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        Attachment aa = articleDao.getArticleAttachment(parId);
        if (aa == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        Article a = articleDao.getArticle(getUser(), aa.getDocumentId());
        if ((a == null) || ((a.getAuthor().getId() != getUser().getId()) && !getUser().hasPermission(Permission.REDACTION_ARTICLES_ALL))) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        articleDao.deleteArticleAttachment(parId);
        
        String url = String.format("Dispatcher?page=%s&id=%d", Cmd.REDACTION_ARTICLE_EDIT, aa.getDocumentId());
        getResponse().sendRedirect(url);
        logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_DELETE_ATTACHMENT, aa.getDocumentId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
    }
}
