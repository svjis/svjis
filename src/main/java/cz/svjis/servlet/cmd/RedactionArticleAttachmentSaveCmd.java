/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

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

        String parArticleId = getRequest().getParameter("articleId");
        
        if (!validateInput(parArticleId)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());
        
        int articleId = Integer.parseInt(parArticleId);
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        //upload.setSizeMax(yourMaxRequestSize);
        List items = upload.parseRequest(getRequest());
        java.util.Iterator iterator = items.iterator();
        while (iterator.hasNext()) {
            FileItem item = (FileItem) iterator.next();
            File f = new File(item.getName());
            if (!item.isFormField()) {
                ArticleAttachment aa = new ArticleAttachment();
                String fileName = f.getName().replace(" ", "_");
                if (fileName.lastIndexOf("\\") > -1) {
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                }
                aa.setFileName(fileName);
                aa.setContentType(item.getContentType());
                aa.setData(item.get());
                aa.setUserId(getUser().getId());
                aa.setArticleId(articleId);
                aa.setUploadTime(new Date());
                if (!aa.getFileName().equals("")) {
                    articleDao.insertArticleAttachment(aa);
                }
            }
        }
        String url = "Dispatcher?page=redactionArticleEdit&id=" + articleId;
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
        logDao.log(getUser().getId(), LogDAO.operationTypeInsertAttachment, articleId, getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
    }
    
    private boolean validateInput(String parArticleId) {
        boolean result = true;
        
        if (!Validator.validateInteger(parArticleId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        return result;
    }
}
