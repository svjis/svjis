/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleSaveCmd extends Command {

    public RedactionArticleSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        String parId = getRequest().getParameter("id");
        String parHeader = Validator.fixTextInput(getRequest().getParameter("header"), false);
        String parDescription = Validator.fixTextInput(getRequest().getParameter("description"), true);
        String parBody = Validator.fixTextInput(getRequest().getParameter("body"), true);
        String parLangId = getRequest().getParameter("language");
        String parAuthorId = getRequest().getParameter("authorId");
        String parCreationDate = Validator.fixTextInput(getRequest().getParameter("creationDate"), false);
        String parMenuId = getRequest().getParameter("menuId");
        
        if (!validateInput(parId, parHeader, parDescription, parBody, parLangId, parAuthorId, parCreationDate, parMenuId)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        RoleDAO roleDao = new RoleDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Article a = new Article();
        a.setId(Integer.valueOf(parId));
        a.setCompanyId(getCompany().getId());
        a.setHeader(parHeader);
        a.setDescription(parDescription);
        a.setBody(parBody);
        a.setLanguageId(Integer.valueOf(parLangId));
        a.setCommentsAllowed(getRequest().getParameter("commentsAllowed") != null);
        a.setPublished(getRequest().getParameter("publish") != null);
        a.setAuthorId((Integer.valueOf(parAuthorId) == 0) ? getUser().getId() : Integer.valueOf(parAuthorId));
        a.setCreationDate(sdf.parse(parCreationDate));
        a.setMenuNodeId(Integer.valueOf(parMenuId));

        HashMap uRoles = new HashMap();
        ArrayList<Role> roles = roleDao.getRoleList(getCompany().getId());
        Iterator<Role> roleI = roles.iterator();
        while (roleI.hasNext()) {
            Role r = roleI.next();
            if (getRequest().getParameter("r_" + r.getId()) != null) {
                uRoles.put(new Integer(r.getId()), r.getDescription());
            }
        }
        a.setRoles(uRoles);
        if (a.getId() == 0) {
            a.setId(articleDao.insertArticle(a));
            logDao.log(getUser().getId(), LogDAO.operationTypeCreateArticle, a.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
        } else {
            articleDao.modifyArticle(a);
            logDao.log(getUser().getId(), LogDAO.operationTypeModifyArticle, a.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
        }
        String url = "Dispatcher?page=redactionArticleEdit&id=" + a.getId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parId, String parHeader, String parDescription, String parBody, String parLangId, String parAuthorId, String parCreationDate, String parMenuId) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parHeader, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parDescription, 0, Validator.maxStringLenAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parBody, 0, Validator.maxStringLenAllowed)) {
            result = false;
        }
        
        if (!Validator.validateInteger(parLangId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateInteger(parAuthorId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parCreationDate, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateInteger(parMenuId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
                
        return result;
    }
}
