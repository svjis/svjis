/*
 *       RedactionArticleAttachmentSaveCmd.java
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
import cz.svjis.bean.ArticleAttachment;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.io.File;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleAttachmentSaveCmd extends Command {

    public RedactionArticleAttachmentSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parArticleId = Validator.getInt(getRequest(), "articleId", 0, Validator.MAX_INT_ALLOWED, false);

        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());
        
        Article a = articleDao.getArticle(getUser(), parArticleId);
        if ((a == null) || ((a.getAuthor().getId() != getUser().getId()) && !getUser().hasPermission("redaction_articles_all"))) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List items = upload.parseRequest(getRequest());
        java.util.Iterator iterator = items.iterator();
        while (iterator.hasNext()) {
            FileItem item = (FileItem) iterator.next();
            File f = new File(item.getName());
            if (!item.isFormField()) {
                ArticleAttachment aa = new ArticleAttachment();
                String fileName = f.getName().replace(" ", "_");
                if (fileName.lastIndexOf('\\') > -1) {
                    fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
                }
                aa.setFileName(fileName);
                aa.setContentType(item.getContentType());
                aa.setData(item.get());
                aa.setUserId(getUser().getId());
                aa.setArticleId(parArticleId);
                aa.setUploadTime(new Date());
                if (!aa.getFileName().equals("")) {
                    articleDao.insertArticleAttachment(aa);
                }
            }
        }
        String url = "Dispatcher?page=redactionArticleEdit&id=" + parArticleId;
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
        logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_INSERT_ATTACHMENT, parArticleId, getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
    }
}
