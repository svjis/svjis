/*
 *       DownloadCmd.java
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

import cz.svjis.bean.Attachment;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.common.JspSnippets;
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
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        ArticleDAO dao = new ArticleDAO(getCnn());
        Attachment aa = dao.getArticleAttachment(parId);
        if ((aa == null) || (dao.getArticle(getUser(), aa.getDocumentId()) == null)) {
            throw new InputValidationException("Bad attachment id");
        }
        JspSnippets.writeBinaryData(aa.getContentType(), aa.getFileName(), aa.getData(), getRequest(), getResponse());
    }
}
